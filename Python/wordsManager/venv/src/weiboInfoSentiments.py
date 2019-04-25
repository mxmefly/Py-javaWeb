#原生连接Mysql驱动
import pymysql
#导入snownlp库
from snownlp import SnowNLP
# 打开数据库连接
db = pymysql.connect("localhost", "root", "", "sbhdb")
# 使用 cursor() 方法创建一个游标对象 cursor
cursor = db.cursor()
# 查询获得base_word总条数
#cursor.execute("SELECT count(*) FROM `weibo_info` where  sentiments=0")
cursor.execute("SELECT count(*) FROM `weibo_info` ")
lengthData = cursor.fetchone()
length=int(lengthData[0])
print("需要修改的总微博条数%d"%length)
#selectSql="select _id,content from `weibo_info`  where  sentiments=0"
selectSql="select _id,content from `weibo_info` "
cursor.execute(selectSql)
wordsResults=cursor.fetchall()
count=0;
for row in wordsResults:
    try:
        s = SnowNLP(str(row[1]))
        updateCursor = db.cursor()
        updateSql = "UPDATE `sbhdb`.`weibo_info` SET `sentiments` = %s WHERE `_id` = '%s'" % (
        str(s.sentiments*10)[0:8], row[0])
        try:
            # 执行SQL语句
            updateCursor.execute(updateSql)
            # 提交到数据库执行
            db.commit()
            count = count + 1
        except:
            # 发生错误时回滚
            db.rollback()
        print(str(row[1]) + "   " + str(s.sentiments)[0:8])
    except:
        print(str(row[1]) + "   出现错误" )
print("共处理了%d条微博数据"%count)
# 关闭数据库连接
db.close()


