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
from sina.settings import  MYSQL_HOST, MYSQL_USER, MYSQL_PASSWORD,MYSQL_DB
from sina.settings import MAX_WEIBO_PAGES,MAX_COMMENT_PAGES
from sina.spiders.utils import time_fix
import time
import pymysql
import copy
from snownlp import SnowNLP


class WeiboSpider(Spider):
    name = 'weibo_spider'
    base_url = "https://weibo.cn"

    #连接数据库
    db = pymysql.connect(MYSQL_HOST, MYSQL_USER, MYSQL_PASSWORD, MYSQL_DB)
    # 使用 cursor() 方法创建一个游标对象 cursor
    cursor = db.cursor()

    def start_requests(self):
        start_uids = [
            '1409340537'
        ]
        for i in range(10):
            self.cursor.execute("SELECT followed_id FROM weibo_user_rela WHERE followed_id NOT IN (SELECT user_id FROM weibo_info) ORDER  BY  rand() LIMIT 10")
            randomUids=self.cursor.fetchall();
            for uid in randomUids:
                yield Request(url="https://weibo.cn/%s/info" % uid[0], callback=self.parse_information)

        #for uid in start_uids:
            #yield Request(url="https://weibo.cn/%s/info" % uid, callback=self.parse_information)


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
        #昵称
        if nick_name and nick_name[0]:
            information_item["nick_name"] = nick_name[0].replace(u"\xa0", "")
        else:
            information_item["nick_name"]=''
        #性别
        if gender and gender[0]:
            information_item["gender"] = gender[0].replace(u"\xa0", "")
        else:
            information_item["gender"] =''
        #地点
        if place and place[0]:
            place = place[0].replace(u"\xa0", "").split(" ")
            information_item["province"] = place[0]
            if len(place) > 1:
                information_item["city"] = place[1]
            else:
                information_item["city"] = ''
        else:
            information_item["province"] =''
            information_item["city"] = ''
        #简介
        if briefIntroduction and briefIntroduction[0]:
            information_item["brief_introduction"] = briefIntroduction[0].replace(u"\xa0", "")
        else:
            information_item["brief_introduction"] =''
        #生日
        if birthday and birthday[0]:
            information_item['birthday'] = birthday[0]
        else:
            information_item['birthday'] =''
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
            information_item["sentiment"] =''
        if vip_level and vip_level[0]:
            information_item["vip_level"] = vip_level[0].replace(u"\xa0", "")
        else:
            information_item["vip_level"] =''
        if authentication and authentication[0]:
            information_item["authentication"] = authentication[0].replace(u"\xa0", "")
        else:
            information_item["authentication"] =''
        if labels and labels[0]:
            information_item["labels"] = labels[0].replace(u"\xa0", ",").replace(';', '').strip(',')
        else:
            information_item["labels"] =''
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
            information_item['tweets_num'] =0
        follows_num = re.findall('关注\[(\d+)\]', text)
        if follows_num:
            information_item['follows_num'] = int(follows_num[0])
        else:
            information_item['follows_num'] =0;
        fans_num = re.findall('粉丝\[(\d+)\]', text)
        if fans_num:
            information_item['fans_num'] = int(fans_num[0])
        else:
            information_item['fans_num'] =0
        #yield information_item
        print(information_item)
        #写入数据库
        try:
            sql="INSERT INTO `sbhdb`.`weibo_user_info`(`_id`, `nick_name`, `gender`, `province`, `city`, `brief_introduction`, `birthday`, `tweets_num`, `follows_num`, `fans_num`, `sex_orientation`, `sentiment`, `vip_level`, `authentication`, `person_url`, `crawl_time`, `labels`) VALUES ('%s', '%s', '%s', '%s', '%s', '%s', '%s', %s, %s, %s, '%s', '%s', '%s', '%s', '%s', %s, '%s')"%(
                information_item['_id'],information_item['nick_name'],information_item['gender'],information_item['province'],information_item['city'],information_item['brief_introduction'],information_item['birthday'],information_item['tweets_num'],information_item['follows_num'],information_item['fans_num'],
                information_item['sex_orientation'],information_item['sentiment'],information_item['vip_level'],information_item['authentication'],'',information_item['crawl_time'],information_item['labels']
            )
            self.cursor.execute(sql)
            self.db.commit()
        except:
            # 数据有重复
            pass

        # 获取该用户微博
        yield Request(url=self.base_url + '/{}/profile?page=1'.format(information_item['_id']),
                      callback=self.parse_tweet,
                      priority=1)

        # 获取粉丝列表
        yield Request(url=self.base_url + '/{}/fans?page=1'.format(information_item['_id']),
                      callback=self.parse_fans,
                      dont_filter=True)
        
        # 获取关注列表
        yield Request(url=self.base_url + '/{}/follow?page=1'.format(information_item['_id']),
                      callback=self.parse_follow,
                      dont_filter=True)
        

    def parse_tweet(self, response):
        if response.url.endswith('page=1'):
            # 如果是第1页，一次性获取后面的所有页
            all_page = re.search(r'/>&nbsp;1/(\d+)页</div>', response.text)
            if all_page:
                all_page = all_page.group(1)
                all_page = int(all_page)
                if(all_page>MAX_WEIBO_PAGES):
                    all_page=MAX_WEIBO_PAGES
                for page_num in range(2, all_page + 1):
                    page_url = response.url.replace('page=1', 'page={}'.format(page_num))
                    yield Request(page_url, self.parse_tweet, dont_filter=True, meta=response.meta)
        """
        解析本页的数据
        """
        tree_node = etree.HTML(response.body)
        tweet_nodes = tree_node.xpath('//div[@class="c" and @id]')
        for tweet_node in tweet_nodes:
            try:
                tweet_item = TweetsItem()
                tweet_item['crawl_time'] = int(time.time())
                tweet_repost_url = tweet_node.xpath('.//a[contains(text(),"转发[")]/@href')[0]
                user_tweet_id = re.search(r'/repost/(.*?)\?uid=(\d+)', tweet_repost_url)
                tweet_item['weibo_url'] = 'https://weibo.com/{}/{}'.format(user_tweet_id.group(2),
                                                                           user_tweet_id.group(1))
                tweet_item['user_id'] = user_tweet_id.group(2)
                tweet_item['_id'] = '{}_{}'.format(user_tweet_id.group(2), user_tweet_id.group(1))
                create_time_info = tweet_node.xpath('.//span[@class="ct"]/text()')[-1]
                if "来自" in create_time_info:
                    tweet_item['created_at'] = time_fix(create_time_info.split('来自')[0].strip())
                else:
                    tweet_item['created_at'] = time_fix(create_time_info.strip())

                like_num = tweet_node.xpath('.//a[contains(text(),"赞[")]/text()')[-1]
                tweet_item['like_num'] = int(re.search('\d+', like_num).group())

                repost_num = tweet_node.xpath('.//a[contains(text(),"转发[")]/text()')[-1]
                tweet_item['repost_num'] = int(re.search('\d+', repost_num).group())

                comment_num = tweet_node.xpath(
                    './/a[contains(text(),"评论[") and not(contains(text(),"原文"))]/text()')[-1]
                tweet_item['comment_num'] = int(re.search('\d+', comment_num).group())

                # 检测由没有阅读全文:
                all_content_link = tweet_node.xpath('.//a[text()="全文" and contains(@href,"ckAll=1")]')
                if all_content_link:
                    all_content_url = self.base_url + all_content_link[0].xpath('./@href')[0]
                    yield Request(all_content_url, callback=self.parse_all_content, meta={'item': tweet_item},
                                  priority=1)

                else:
                    all_content_text = tweet_node.xpath('string(.)')
                    if '转发理由:' in all_content_text:
                        all_content_text = all_content_text.split('转发理由:')[1]
                    all_content_text = all_content_text.split('\xa0', maxsplit=1)[0]
                    tweet_item['content'] = all_content_text.strip()
                    s = SnowNLP(tweet_item['content'])
                    tweet_item['sentiments'] = str(s.sentiments * 10)[0:8]
                    #yield tweet_item
                    print(tweet_item)
                    try:
                        sql = "INSERT INTO `sbhdb`.`weibo_info`(`_id`, `weibo_url`, `user_id`, `content`, `created_at`, `repost_num`, `comment_num`, `like_num`, `crawl_time`, `sentiments`) VALUES ('%s', '%s', '%s', '%s', '%s', %s,%s, %s, %s,%s)"%(
                                  tweet_item['_id'],
                                  tweet_item['weibo_url'],
                                  tweet_item['user_id'],
                                  tweet_item['content'],
                                  tweet_item['created_at'],
                                  tweet_item['repost_num'],
                                  tweet_item['comment_num'],
                                  tweet_item['like_num'],
                                  tweet_item['crawl_time'],
                                  tweet_item['sentiments']
                              )
                        self.cursor.execute(sql)
                        self.db.commit()
                    except:
                        # 数据有重复
                        continue
                        pass

                # 抓取该微博的评论信息
                comment_url = self.base_url + '/comment/' + tweet_item['weibo_url'].split('/')[-1] + '?page=1'
                yield Request(url=comment_url, callback=self.parse_comment, meta={'weibo_url': tweet_item['weibo_url']})

            except Exception as e:
                self.logger.error(e)

    def parse_all_content(self, response):
        # 有阅读全文的情况，获取全文
        tree_node = etree.HTML(response.body)
        tweet_item = response.meta['item']
        content_node = tree_node.xpath('//*[@id="M_"]/div[1]')[0]
        all_content_text = content_node.xpath('string(.)').split(':', maxsplit=1)[1]
        all_content_text = all_content_text.split('\xa0')[0]
        tweet_item['content'] = all_content_text.strip()
        s = SnowNLP(tweet_item['content'])
        tweet_item['sentiments'] = str(s.sentiments*10)[0:8]
        # yield tweet_item
        print(tweet_item)
        try:
            sql = "INSERT INTO `sbhdb`.`weibo_info`(`_id`, `weibo_url`, `user_id`, `content`, `created_at`, `repost_num`, `comment_num`, `like_num`, `crawl_time`, `sentiments`) VALUES ('%s', '%s', '%s', '%s', '%s', %s,%s, %s, %s,%s)" % (
                tweet_item['_id'],
                tweet_item['weibo_url'],
                tweet_item['user_id'],
                tweet_item['content'],
                tweet_item['created_at'],
                tweet_item['repost_num'],
                tweet_item['comment_num'],
                tweet_item['like_num'],
                tweet_item['crawl_time'],
                tweet_item['sentiments']
            )
            self.cursor.execute(sql)
            self.db.commit()
        except:
            # 数据有重复
            pass
        #yield tweet_item
        print(tweet_item)

    def parse_follow(self, response):
        """
        抓取关注列表
        """
        # 如果是第1页，一次性获取后面的所有页
        if response.url.endswith('page=1'):
            all_page = re.search(r'/>&nbsp;1/(\d+)页</div>', response.text)
            if all_page:
                all_page = all_page.group(1)
                all_page = int(all_page)
                for page_num in range(2, all_page + 1):
                    page_url = response.url.replace('page=1', 'page={}'.format(page_num))
                    yield Request(page_url, self.parse_follow, dont_filter=True, meta=response.meta)
        selector = Selector(response)
        urls = selector.xpath('//a[text()="关注他" or text()="关注她" or text()="取消关注"]/@href').extract()
        uids = re.findall('uid=(\d+)', ";".join(urls), re.S)
        ID = re.findall('(\d+)/follow', response.url)[0]
        for uid in uids:
            relationships_item = RelationshipsItem()
            relationships_item['crawl_time'] = int(time.time())
            relationships_item["fan_id"] = ID
            relationships_item["followed_id"] = uid
            relationships_item["_id"] = ID + '-' + uid
            try:
                sql ="INSERT INTO `sbhdb`.`weibo_user_rela`(`_id`, `followed_id`, `fan_id`, `crawl_time`) VALUES ('%s', '%s', '%s', %s)"%(
                    relationships_item["_id"],
                    relationships_item["followed_id"],
                    relationships_item["fan_id"],
                    relationships_item['crawl_time']
                )
                self.cursor.execute(sql)
                self.db.commit()
            except:
                # 数据有重复
                pass
            #yield relationships_item
            print(relationships_item)

    def parse_fans(self, response):
        """
        抓取粉丝列表
        """
        # 如果是第1页，一次性获取后面的所有页
        if response.url.endswith('page=1'):
            all_page = re.search(r'/>&nbsp;1/(\d+)页</div>', response.text)
            if all_page:
                all_page = all_page.group(1)
                all_page = int(all_page)
                for page_num in range(2, all_page + 1):
                    page_url = response.url.replace('page=1', 'page={}'.format(page_num))
                    yield Request(page_url, self.parse_fans, dont_filter=True, meta=response.meta)
        selector = Selector(response)
        urls = selector.xpath('//a[text()="关注他" or text()="关注她" or text()="移除"]/@href').extract()
        uids = re.findall('uid=(\d+)', ";".join(urls), re.S)
        ID = re.findall('(\d+)/fans', response.url)[0]
        for uid in uids:
            relationships_item = RelationshipsItem()
            relationships_item['crawl_time'] = int(time.time())
            relationships_item["fan_id"] = uid
            relationships_item["followed_id"] = ID
            relationships_item["_id"] = uid + '-' + ID
            try:
                sql ="INSERT INTO `sbhdb`.`weibo_user_rela`(`_id`, `followed_id`, `fan_id`, `crawl_time`) VALUES ('%s', '%s', '%s', %s)"%(
                    relationships_item["_id"],
                    relationships_item["followed_id"],
                    relationships_item["fan_id"],
                    relationships_item['crawl_time']
                )
                self.cursor.execute(sql)
                self.db.commit()
            except:
                # 数据有重复
                pass
            print(relationships_item)

            #yield relationships_item

    def parse_comment(self, response):
        # 如果是第1页，一次性获取后面的所有页
        if response.url.endswith('page=1'):
            all_page = re.search(r'/>&nbsp;1/(\d+)页</div>', response.text)
            if all_page:
                all_page = all_page.group(1)
                all_page = int(all_page)
                if(all_page>MAX_COMMENT_PAGES):
                    all_page=MAX_COMMENT_PAGES
                for page_num in range(2, all_page + 1):
                    page_url = response.url.replace('page=1', 'page={}'.format(page_num))
                    yield Request(page_url, self.parse_comment, dont_filter=True, meta=response.meta)
        selector = Selector(response)
        comment_nodes = selector.xpath('//div[@class="c" and contains(@id,"C_")]')
        for comment_node in comment_nodes:
            comment_user_url = comment_node.xpath('.//a[contains(@href,"/u/")]/@href').extract_first()
            if not comment_user_url:
                continue
            comment_item = CommentItem()
            comment_item['crawl_time'] = int(time.time())
            comment_item['weibo_url'] = response.meta['weibo_url']
            comment_item['comment_user_id'] = re.search(r'/u/(\d+)', comment_user_url).group(1)
            comment_item['content'] = comment_node.xpath('.//span[@class="ctt"]').xpath('string(.)').extract_first()
            comment_item['_id'] = comment_node.xpath('./@id').extract_first()
            created_at = comment_node.xpath('.//span[@class="ct"]/text()').extract_first()
            comment_item['created_at'] = time_fix(created_at.split('\xa0')[0])
            s = SnowNLP(comment_item['content'])
            comment_item['sentiments']= str(s.sentiments * 10)[0:8]
            try:
                sql = "INSERT INTO `sbhdb`.`weibo_comment`(`_id`, `comment_user_id`, `content`, `weibo_url`, `created_at`, `crawl_time`, `sentiments`) VALUES ('%s', '%s', '%s', '%s', '%s', %s,%s)"%(
                    comment_item['_id'],
                    comment_item['comment_user_id'],
                    comment_item['content'],
                    comment_item['weibo_url'],
                    comment_item['created_at'],
                    comment_item['crawl_time'],
                    comment_item['sentiments']
                )
                self.cursor.execute(sql)
                self.db.commit()
            except:
                # 数据有重复
                continue
                pass

            #yield comment_item
            print(comment_item)


if __name__ == "__main__":
    process = CrawlerProcess(get_project_settings())
    process.crawl('weibo_spider')
    process.start()


