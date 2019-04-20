import os
import pymysql
from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
import sys

sys.path.append(os.getcwd())

TEMPLATES_FOLDER = os.getcwd() + '/sina/account_build/templates/'


class WeiboLogin():
    def __init__(self, username, password):
        os.system('pkill -f phantom')
        self.url = 'https://passport.weibo.cn/signin/login?entry=mweibo&r=https://weibo.cn/'
        #PhantomJS驱动加载有问题 暂时绝对路径引入
        self.browser = webdriver.PhantomJS(executable_path="C:/phantomjs-2.1.1-windows/bin/phantomjs.exe")
        self.browser.set_window_size(1050, 840)
        self.wait = WebDriverWait(self.browser, 20)
        self.username = username
        self.password = password

    def open(self):
        """
        打开网页输入用户名密码并点击
        :return: None
        """
        self.browser.get(self.url)
        username = self.wait.until(EC.presence_of_element_located((By.ID, 'loginName')))
        password = self.wait.until(EC.presence_of_element_located((By.ID, 'loginPassword')))
        submit = self.wait.until(EC.element_to_be_clickable((By.ID, 'loginAction')))
        username.send_keys(self.username)
        password.send_keys(self.password)
        submit.click()

    def run(self):
        """
        破解入口
        :return:
        """
        self.open()
        WebDriverWait(self.browser, 30).until(
            EC.title_is('我的首页')
        )
        cookies = self.browser.get_cookies()
        cookie = [item["name"] + "=" + item["value"] for item in cookies]
        cookie_str = '; '.join(item for item in cookie)
        self.browser.quit()
        return cookie_str


if __name__ == '__main__':
    db = pymysql.connect("localhost", "root", "", "sbhdb")
    # 使用 cursor() 方法创建一个游标对象 cursor
    cursor = db.cursor() 
    
    # 在目录中新建一个account.txt文件，格式需要与account_sample.txt相同
    file_path='account.txt'
    with open(file_path, 'r') as f:
        lines = f.readlines()
    for line in lines:
        line = line.strip()
        username = line.split('----')[0]
        password = line.split('----')[1]
        print('=' * 10 + username + '=' * 10)
        try:
            cookie_str = WeiboLogin(username, password).run()
        except Exception as e:
            print(e)
            continue
        print('获取cookie成功')
        print('Cookie:', cookie_str)

        insertSql="INSERT INTO `sbhdb`.`account_pool`(`_ip`, `password`, `status`, `cookie`) VALUES ('"+username+"', '"+password+"', 'success', '"+cookie_str+"')"
        try:
            # 执行SQL语句
            cursor.execute(insertSql)
            # 提交到数据库执行
            db.commit()
        except:
            print("插入操作有误，回滚")
            db.rollback()
    #关闭数据库连接
    db.close()