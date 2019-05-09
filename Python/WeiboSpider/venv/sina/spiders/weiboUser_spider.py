#!/usr/bin/env python
# encoding: utf-8
import re
from lxml import etree
from scrapy import Spider
from scrapy.crawler import CrawlerProcess
from scrapy.selector import Selector
from scrapy.http import Request
from scrapy.utils.project import get_project_settings
from sina.items import TweetsItem, InformationItem, RelationshipsItem, CommentItem
from sina.settings import MYSQL_HOST, MYSQL_USER, MYSQL_PASSWORD, MYSQL_DB
from sina.settings import MAX_WEIBO_PAGES, MAX_COMMENT_PAGES, MIN_WEIBO_DATE
from sina.spiders.utils import time_fix
import time
import pymysql
import copy
from snownlp import SnowNLP


class WeiboSpider(Spider):
    name = 'weiboUser_spider'
    base_url = "https://weibo.cn"

    # 连接数据库
    db = pymysql.connect(MYSQL_HOST, MYSQL_USER, MYSQL_PASSWORD, MYSQL_DB)
    # 使用 cursor() 方法创建一个游标对象 cursor
    cursor = db.cursor()

    def start_requests(self):
        
        start_uids = [
            '1196235387',
            '1195230310',
            '1197362373',
            '1251000504',
            '1287373332',
            '1338738570',
            '1642566747',
            '1692391497',
            '1808624312',
            '5676095533'
        ]
        """
                for uid in start_uids:
            self.cursor.execute("SELECT fan_id FROM weibo_user_rela WHERE followed_id = '%s' ORDER  BY  rand() LIMIT 100"%(uid))
            fansUids = self.cursor.fetchall();
            for funId in fansUids:
                yield Request(url="https://weibo.cn/%s/info" % funId[0], callback=self.parse_information)

        """
        for i in range(20):
            self.cursor.execute(
                "SELECT fan_id FROM weibo_user_rela WHERE fan_id NOT IN (SELECT user_id FROM weibo_info) ORDER  BY  rand() LIMIT 10")
            randomUids = self.cursor.fetchall();
            for uid in randomUids:
                yield Request(url="https://weibo.cn/%s/info" % uid[0], callback=self.parse_information)

    def parse_information(self, response):
        """ 抓取个人信息 """
        information_item = InformationItem()
        information_item['crawl_time'] = int(time.time())
        selector = Selector(response)
        information_item['_id'] = re.findall('(\d+)/info', response.url)[0]
        text1 = ";".join(selector.xpath('body/div[@class="c"]//text()').extract())  # 获取标签里的所有text()
        nick_name = re.findall('昵称;?[：:]?(.*?);', text1)
        gender = re.findall('性别;?[：:]?(.*?);', text1)
        place = re.findall('地区;?[：:]?(.*?);', text1)
        briefIntroduction = re.findall('简介;?[：:]?(.*?);', text1)
        birthday = re.findall('生日;?[：:]?(.*?);', text1)
        sex_orientation = re.findall('性取向;?[：:]?(.*?);', text1)
        sentiment = re.findall('感情状况;?[：:]?(.*?);', text1)
        vip_level = re.findall('会员等级;?[：:]?(.*?);', text1)
        authentication = re.findall('认证;?[：:]?(.*?);', text1)
        labels = re.findall('标签;?[：:]?(.*?)更多>>', text1)
        # 昵称
        if nick_name and nick_name[0]:
            information_item["nick_name"] = nick_name[0].replace(u"\xa0", "")
        else:
            information_item["nick_name"] = ''
        # 性别
        if gender and gender[0]:
            information_item["gender"] = gender[0].replace(u"\xa0", "")
        else:
            information_item["gender"] = ''
        # 地点
        if place and place[0]:
            place = place[0].replace(u"\xa0", "").split(" ")
            information_item["province"] = place[0]
            if len(place) > 1:
                information_item["city"] = place[1]
            else:
                information_item["city"] = ''
        else:
            information_item["province"] = ''
            information_item["city"] = ''
        # 简介
        if briefIntroduction and briefIntroduction[0]:
            information_item["brief_introduction"] = briefIntroduction[0].replace(u"\xa0", "")
        else:
            information_item["brief_introduction"] = ''
        # 生日
        if birthday and birthday[0]:
            information_item['birthday'] = birthday[0]
        else:
            information_item['birthday'] = ''
        if sex_orientation and sex_orientation[0]:
            if sex_orientation[0].replace(u"\xa0", "") == gender[0]:
                information_item["sex_orientation"] = "同性恋"
            else:
                information_item["sex_orientation"] = "异性恋"
        else:
            information_item["sex_orientation"] = "未知"
        if sentiment and sentiment[0]:
            information_item["sentiment"] = sentiment[0].replace(u"\xa0", "")
        else:
            information_item["sentiment"] = ''
        if vip_level and vip_level[0]:
            information_item["vip_level"] = vip_level[0].replace(u"\xa0", "")
        else:
            information_item["vip_level"] = ''
        if authentication and authentication[0]:
            information_item["authentication"] = authentication[0].replace(u"\xa0", "")
        else:
            information_item["authentication"] = ''
        if labels and labels[0]:
            information_item["labels"] = labels[0].replace(u"\xa0", ",").replace(';', '').strip(',')
        else:
            information_item["labels"] = ''
        request_meta = response.meta
        request_meta['item'] = information_item
        yield Request(self.base_url + '/u/{}'.format(information_item['_id']),
                      callback=self.parse_further_information,
                      meta=request_meta, dont_filter=True, priority=1)

    def parse_further_information(self, response):
        text = response.text
        information_item = response.meta['item']
        tweets_num = re.findall('微博\[(\d+)\]', text)
        if tweets_num:
            information_item['tweets_num'] = int(tweets_num[0])
        else:
            information_item['tweets_num'] = 0
        follows_num = re.findall('关注\[(\d+)\]', text)
        if follows_num:
            information_item['follows_num'] = int(follows_num[0])
        else:
            information_item['follows_num'] = 0;
        fans_num = re.findall('粉丝\[(\d+)\]', text)
        if fans_num:
            information_item['fans_num'] = int(fans_num[0])
        else:
            information_item['fans_num'] = 0
        # 写入数据库
        try:
            sql = "INSERT INTO `sbhdb`.`weibo_user_info`(`_id`, `nick_name`, `gender`, `province`, `city`, `brief_introduction`, `birthday`, `tweets_num`, `follows_num`, `fans_num`, `sex_orientation`, `sentiment`, `vip_level`, `authentication`, `person_url`, `crawl_time`, `labels`) VALUES ('%s', '%s', '%s', '%s', '%s', '%s', '%s', %s, %s, %s, '%s', '%s', '%s', '%s', '%s', %s, '%s')" % (
                information_item['_id'], information_item['nick_name'], information_item['gender'],
                information_item['province'], information_item['city'], information_item['brief_introduction'],
                information_item['birthday'], information_item['tweets_num'], information_item['follows_num'],
                information_item['fans_num'],
                information_item['sex_orientation'], information_item['sentiment'], information_item['vip_level'],
                information_item['authentication'], '', information_item['crawl_time'], information_item['labels']
            )
            self.cursor.execute(sql)
            self.cursor.execute("INSERT INTO `sbhdb`.`sys_msg`(`message`, `message_date`) VALUES ('%s', '%s')" % (
                "获取" + information_item['nick_name'] + "微博信息",
                time.strftime('%Y-%m-%d %H:%M:%S', time.localtime(time.time()))))
            self.db.commit()
        except:
            # 数据有重复
            pass

        yield information_item





if __name__ == "__main__":
    process = CrawlerProcess(get_project_settings())
    process.crawl('weiboUser_spider')
    process.start()


