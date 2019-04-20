import pymysql
db = pymysql.connect("localhost", "root", "", "sbhdb")
# 使用 cursor() 方法创建一个游标对象 cursor
cursor = db.cursor()
cursor.execute("SELECT count(*) FROM `account_pool`")
all_count =int(cursor.fetchone()[0])
cursor.execute("SELECT * FROM account_pool ORDER BY  rand() LIMIT 1")
random_account = cursor.fetchall()
cookie=random_account[0][3]
db.close()