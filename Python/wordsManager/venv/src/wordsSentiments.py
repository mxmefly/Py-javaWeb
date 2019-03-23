#原生连接Mysql驱动
import pymysql
#导入snownlp库
from snownlp import SnowNLP
# 打开数据库连接
db = pymysql.connect("localhost", "root", "", "sbhdb")
# 使用 cursor() 方法创建一个游标对象 cursor
cursor = db.cursor()
# 查询获得base_word总条数
cursor.execute("SELECT count(*) FROM `base_words`")
lengthData = cursor.fetchone()
length=int(lengthData[0])
print("词库中总词条数%d"%length)
selectSql="select id,word from `base_words` "
cursor.execute(selectSql)
wordsResults=cursor.fetchall()
count=0;
for row in wordsResults:
    wordId=str(row[0])
    s=SnowNLP(str(row[1]))
    wordSent=int(round(s.sentiments*10))
    updateCursor = db.cursor()
    updateSql="UPDATE `base_words` SET sentiments =" +str(wordSent)+ " WHERE id = "+wordId
    try:
        # 执行SQL语句
        updateCursor.execute(updateSql)
        # 提交到数据库执行
        db.commit()
        count=count+1
    except:
        # 发生错误时回滚
        db.rollback()
    print(str(row[0])+"  "+str(row[1])+"  "+str(wordSent))
print("共处理了%d条词库数据"%count)
# 关闭数据库连接
db.close()


