# encoding: utf-8
import time
import pymysql
from sina.settings import MYSQL_HOST, MYSQL_USER, MYSQL_PASSWORD,MYSQL_DB


class CookieMiddleware(object):
    """
    每次请求都随机从账号池中选择一个账号去访问
    """

    def __init__(self):
        db = pymysql.connect("localhost", "root", "", "sbhdb")
        # 使用 cursor() 方法创建一个游标对象 cursor
        self.cursor = db.cursor()

    def process_request(self, request, spider):
        #all_count = self.account_collection.find({'status': 'success'}).count()
        #获取账号池账号个数
        self.cursor.execute("SELECT count(*) FROM `account_pool`")
        all_count = int(self.cursor.fetchone()[0])
        if all_count == 0:
            raise Exception('当前账号池为空')
        self.cursor.execute("SELECT * FROM account_pool WHERE status='success' ORDER  BY  rand() LIMIT 1")
        random_account = self.cursor.fetchall()
        time.sleep(1.5)
        request.headers.setdefault('Cookie', str(random_account[0][3]))
        request.meta['account'] = random_account[0]


class RedirectMiddleware(object):
    """
    检测账号是否正常
    302 / 403,说明账号cookie失效/账号被封，状态标记为error
    418,偶尔产生,需要再次请求
    """

    def __init__(self):
        self.db = pymysql.connect("localhost", "root", "", "sbhdb")
        # 使用 cursor() 方法创建一个游标对象 cursor
        self.cursor = self.db.cursor()

    def process_response(self, request, response, spider):
        http_code = response.status
        if http_code == 302 or http_code == 403:                                                     #{'$set': {'status': 'error'}}, )
            try:
                # 执行SQL语句
                sql="UPDATE account_pool SET status='error' WHERE _ip='"+str(request.meta['account'][0])+"'"
                self.cursor.execute(sql)
                # 提交到数据库执行
                self.db.commit()
            except:
                print("修改出错，回滚")
                self.db.rollback()
            return request
        elif http_code == 418:
            spider.logger.error('ip 被封了!!!请更换ip,或者停止程序...')
            return request
        else:
            return response
