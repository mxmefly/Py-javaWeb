from bs4 import BeautifulSoup
import requests

URL = "https://m.weibo.cn/?display=0&retcode=6102"
html = requests.get(URL).text
print(html)
soup = BeautifulSoup(html, 'lxml')