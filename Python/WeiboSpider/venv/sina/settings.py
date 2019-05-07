# -*- coding: utf-8 -*-

BOT_NAME = 'sina'

SPIDER_MODULES = ['sina.spiders']
NEWSPIDER_MODULE = 'sina.spiders'

ROBOTSTXT_OBEY = False

#请求头
DEFAULT_REQUEST_HEADERS = {
    'User-Agent': 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10.13; rv:61.0) Gecko/20100101 Firefox/61.0',
}

#并发请求的最大数量
CONCURRENT_REQUESTS = 4
#下载连续页面之前应等待的时间
DOWNLOAD_DELAY = 0.4

DOWNLOADER_MIDDLEWARES = {
    'weibo.middlewares.UserAgentMiddleware': None,
    'scrapy.downloadermiddlewares.cookies.CookiesMiddleware': None,
    'scrapy.downloadermiddlewares.redirect.RedirectMiddleware': None,
    'sina.middlewares.CookieMiddleware': 300,
    'sina.middlewares.RedirectMiddleware': 200,
}
"""
#暂停自带的item数据库操作 有点问题
ITEM_PIPELINES = {
    'sina.pipelines.MySqlPipeline': 1,
}
"""

#mysql db 配置
MYSQL_HOST='127.0.0.1'
MYSQL_USER='root'
MYSQL_PASSWORD=''
MYSQL_DB='sbhdb'

MAX_WEIBO_PAGES = 50 
MAX_COMMENT_PAGES = 4
MIN_WEIBO_DATE='2017-01-01 00:00:00'
