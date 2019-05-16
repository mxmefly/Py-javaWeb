import re
import time
import pymysql
import time
from getWeiboData import  getWeiboData

if __name__ == "__main__":
    x=10
    while(x):
        selectSql="SELECT word,count FROM new_words WHERE count>100 ";
        getWeiboData().dbCursor.execute(selectSql)
        newWords = getWeiboData().dbCursor.fetchall();
        for word in newWords:
            insertSql="INSERT INTO base_words (word,counts,is_show,type,word_length) VALUES ('%s',%s,1,'newWords',%s)"%(word[0],word[1],len(word[0]))
            try:
                getWeiboData().dbCursor.execute(insertSql)
            except:
                getWeiboData().db.rollback()
        if(len(newWords)>0):
            try:
                getWeiboData().dbCursor.execute("INSERT INTO `sbhdb`.`sys_msg`(`message`, `message_date`) VALUES ('%s', '%s')"%("添加"+str(len(newWords))+"条新词入词库",time.strftime('%Y-%m-%d %H:%M:%S',time.localtime(time.time()))))
                getWeiboData().dbCursor.execute("DELETE FROM new_words WHERE count>100")
            except:
                getWeiboData().db.rollback()
        try:
            getWeiboData().db.commit()
        except:
            print("事务提交出错")
        count = 0;
        getWeiboData.words={}
        selectSql = "SELECT comment_user_id,content,created_at,id,weibo_url FROM weibo_comment WHERE isProcess=0 ORDER  BY  rand() LIMIT 2000"
        getWeiboData().dbCursor.execute(selectSql)
        contentResults = getWeiboData().dbCursor.fetchall();
        print("此次需要处理" + str(len(contentResults)) + "条评论数据")
        for content in contentResults:
            updateSql = "UPDATE `sbhdb`.`weibo_comment` SET `isProcess` = 1 WHERE `id` = '%s'" % (content[3])
            try:
                getWeiboData().dbCursor.execute(updateSql)
            except:
                getWeiboData().db.rollback()
            # 过滤掉@用户 转发微博
            text = re.sub(r'(回复@.*:)|(@.*\s?)|(转发微博)|(转发)', "", content[1])
            count = count + 1
            print(str(count) + "  " + text)
            # 长度小于2直接跳过
            if (len(text) < 2 or len(text)>80):
                continue
            weiboDate = content[2][0:10]
            userId = content[0]
            upUserId = content[4][18:28]
            topicList = getWeiboData().getTopic(content[1])
            # 获取分词
            wordList = getWeiboData().getSepWords(text)
            wordCount = getWeiboData().getWordsCount(wordList)
            if(len(text)<100):
                getWeiboData().insertNewWord(wordList)
            # 获取表情
            expressionList = getWeiboData().getExpression(text)
            for expression in expressionList:
                selectSql = "SELECT id,counts FROM weibo_base_data WHERE type='expression' AND user_id='%s' AND date='%s' AND name='%s' AND up_user='%s'" % (
                    userId, weiboDate, expression, upUserId)
                getWeiboData().dbCursor.execute(selectSql)
                wordsResults = getWeiboData().dbCursor.fetchall()
                if (len(wordsResults) == 0):
                    insertSql = "INSERT INTO `sbhdb`.`weibo_base_data`(`user_id`, `name`, `counts`, `type`, `date`,`up_user`) VALUES ('%s', '%s', %s, 'expression', '%s','%s')" % (
                        userId, expression, 1, weiboDate, upUserId)
                    try:
                        getWeiboData().dbCursor.execute(insertSql)
                    except:
                        getWeiboData().db.rollback()
                else:
                    updateSql = "UPDATE `sbhdb`.`weibo_base_data` SET `counts` = %s WHERE `id` = %s" % (
                        wordsResults[0][1] + 1, wordsResults[0][0])
                    try:
                        getWeiboData().dbCursor.execute(updateSql)
                    except:
                        getWeiboData().db.rollback()
            for key in wordCount.keys():
                selectSql = "SELECT id,counts FROM base_words WHERE word = '%s'" % (key)
                getWeiboData().dbCursor.execute(selectSql)
                wordsResults = getWeiboData().dbCursor.fetchall()
                if (len(wordsResults) != 0):
                    updateSql = "UPDATE `sbhdb`.`base_words` SET `counts` = %s WHERE `id` = %s" % (
                        wordsResults[0][1] + wordCount[key], wordsResults[0][0])
                    try:
                        getWeiboData().dbCursor.execute(updateSql)
                    except:
                        getWeiboData().db.rollback()
                else:
                    sql="INSERT INTO `sbhdb`.`base_words`(`word`, `counts`, `is_show`, `type`, `word_length`) VALUES ('%s', %s, 1, 'BaseWords', %s)"%(key, wordCount[key],len(key))
                    try:
                        getWeiboData().dbCursor.execute(sql)
                    except:
                        getWeiboData().db.rollback()
                selectSql = "SELECT id,counts FROM weibo_base_data WHERE type='word' AND user_id='%s' AND date='%s' AND name='%s' AND up_user='%s'" % (
                userId, weiboDate, key, upUserId)
                getWeiboData().dbCursor.execute(selectSql)
                wordsResults = getWeiboData().dbCursor.fetchall()
                if (len(wordsResults) == 0):
                    insertSql = "INSERT INTO `sbhdb`.`weibo_base_data`(`user_id`, `name`, `counts`, `type`, `date`,`up_user`) VALUES ('%s', '%s', %s, 'word', '%s','%s')" % (
                    userId, key, wordCount[key], weiboDate, upUserId)
                    try:
                        getWeiboData().dbCursor.execute(insertSql)
                    except:
                        getWeiboData().db.rollback()
                else:
                    updateSql = "UPDATE `sbhdb`.`weibo_base_data` SET `counts` = %s WHERE `id` = %s" % (
                    wordsResults[0][1] + wordCount[key], wordsResults[0][0])
                    try:
                        getWeiboData().dbCursor.execute(updateSql)
                    except:
                        getWeiboData().db.rollback()
            try:
                getWeiboData().db.commit()
            except:
                print("事务提交出错")
        MsgSql="INSERT INTO `sbhdb`.`sys_msg`(`message`, `message_date`) VALUES ('%s', '%s')"%("处理"+str(len(contentResults))+"条评论",time.strftime('%Y-%m-%d %H:%M:%S',time.localtime(time.time())))
        try:
            getWeiboData().dbCursor.execute(MsgSql)
            getWeiboData().db.commit()
        except:
            getWeiboData().db.rollback()










