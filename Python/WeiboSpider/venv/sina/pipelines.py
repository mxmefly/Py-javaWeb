# -*- coding: utf-8 -*-

import pymysql
from sina.items import RelationshipsItem, TweetsItem, InformationItem, CommentItem
from sina.settings import MYSQL_HOST, MYSQL_USER, MYSQL_PASSWORD,MYSQL_DB

class MySqlPipeline(object):
    def __init__(self):

        self.db = pymysql.connect(MYSQL_HOST, MYSQL_USER, MYSQL_PASSWORD, MYSQL_DB)
        # 使用 cursor() 方法创建一个游标对象 cursor
        self.cursor = self.db.cursor()

    def process_item(self, item, spider):
        """ 判断item的类型，并作相应的处理，再入数据库 """
        if isinstance(item, RelationshipsItem):
            try:
                # 执行插入SQL语句
                self.cursor.execute("INSERT INTO `sbhdb`.`weibo_user_rela`(`_id`, `followed_id`, `fan_id`, `crawl_time`) VALUES ('"
                                    +item._id+"', '"+item.followed_id+"', '"+item.fan_id+"',"+str(item.crawl_time)+" )")
                self.db.commit()
            except:
               #数据有重复
                pass
        elif isinstance(item, TweetsItem):
            try:
                # 执行插入SQL语句
                self.cursor.execute("INSERT INTO `sbhdb`.`weibo_info`(`_id`, `weibo_url`, `user_id`, `content`, `created_at`, `repost_num`, `comment_num`, `like_num`, `crawl_time`) "
                                    +"VALUES ('"+item._id+"', '"
                                    +item.weibo_url+"', '"+item.user_id+"', '"+item.content+"', '"+item.created_at+"', "
                                    +str(item.repost_num)+", "+str(item.comment_num)+", "+str(item.like_num)+", "+str(item.crawl_time)+")")
                self.db.commit()
            except:
                # 数据有重复
                pass
        elif isinstance(item, InformationItem):
            try:
                # 执行插入SQL语句
                self.cursor.execute("INSERT INTO `sbhdb`.`weibo_user_info`(`_id`, `nick_name`, `gender`, `province`, `city`, `brief_introduction`, `birthday`, `tweets_num`, `follows_num`, `fans_num`, `sex_orientation`, `sentiment`, `vip_level`, `authentication`, `person_url`, `crawl_time`, `labels`)"
                                    +" VALUES ('"+item._id+"', '"+item.nick_name+"', '"+item.gender+"', '"+item.province+"', '"
                                    +item.city+"', '"+item.brief_introduction+"', '"+item.birthday+"', "+str(item.tweets_num)+", "+str(item.follows_num)+", "
                                    +str(item.fans_num)+", '"+item.sex_orientation+"', '"+item.sentiment+"', '"+item.vip_level+"', '"+item.authentication+"', '"
                                    +item.person_url+"', "+str(item.crawl_time)+", '"+item.labels+"')")
                self.db.commit()
            except:
                # 数据有重复
                pass
        elif isinstance(item, CommentItem):
            #INSERT INTO `sbhdb`.`weibo_comment`(`_id`, `comment_user_id`, `content`, `weibo_url`, `created_at`, `crawl_time`) VALUES ('1', '2', '3', '4', '5', 6)
            try:
                # 执行插入SQL语句
                self.cursor.execute("INSERT INTO `sbhdb`.`weibo_comment`(`_id`, `comment_user_id`, `content`, `weibo_url`, `created_at`, `crawl_time`) VALUES ('"+item._id+"', '"+item.comment_user_id+"', '"+item.content
                                    +"', '"+item.weibo_url+"', '"+item.created_at+"', "+str(item.crawl_time)+")")
                self.db.commit()
            except:
                # 数据有重复
                pass
        return item

    @staticmethod
    def insert_item(collection, item):
        try:
            collection.insert(dict(item))
        except DuplicateKeyError:
            """
            说明有重复数据
            """
            pass
