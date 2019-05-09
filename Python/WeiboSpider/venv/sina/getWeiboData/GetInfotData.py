import re
import time
import pymysql
from getWeiboData import  getWeiboData
if __name__ == "__main__":
    count=0
    a=10
    while(a):
        getWeiboData.words = {}
        selectSql = "SELECT user_id,content,created_at,id FROM weibo_info WHERE isProcess=0 ORDER BY created_at DESC limit 1000"
        getWeiboData().dbCursor.execute(selectSql)
        contentResults = getWeiboData().dbCursor.fetchall();
        print("此次需要处理" + str(len(contentResults)) + "条微博数据")
        for content in contentResults:
            updateSql = "UPDATE `sbhdb`.`weibo_info` SET `isProcess` = 1 WHERE `id` = '%s'" % (content[3])
            try:
                getWeiboData().dbCursor.execute(updateSql)
            except:
                getWeiboData().db.rollback()
            # 过滤掉@用户 转发微博
            text = re.sub(r'(回复@.*:)|(@.*\s?)|(转发微博)|(转发)', "", content[1])
            count = count + 1
            print(str(count) + "  " + text)
            # 长度小于2直接跳过
            if (len(text) < 2 or len(text) > 80):
                continue
            weiboDate = content[2][0:10]
            userId = content[0]
            topicList = getWeiboData().getTopic(text)
            # 获取分词
            wordList = getWeiboData().getSepWords(text)
            wordCount = getWeiboData().getWordsCount(wordList)
            if (len(text) < 100):
                getWeiboData().insertNewWord(wordList)
            # 获取话题
            topicList = getWeiboData().getTopic(text)
            for topic in topicList:
                insertSql = "INSERT INTO `sbhdb`.`weibo_topic`(`topic`, `user_id`, `date`) VALUES ('%s', '%s', '%s')" % (
                    topic, userId, weiboDate)
            try:
                getWeiboData().dbCursor.execute(insertSql)
            except:
                getWeiboData().db.rollback()
            # 获取表情
            expressionList = getWeiboData().getExpression(text)
            for expression in expressionList:
                selectSql = "SELECT id,counts FROM weibo_base_data WHERE type='expression' AND user_id='%s' AND date='%s' AND name='%s' " % (
                    userId, weiboDate, expression)
                getWeiboData().dbCursor.execute(selectSql)
                wordsResults = getWeiboData().dbCursor.fetchall()
                if (len(wordsResults) == 0):
                    insertSql = "INSERT INTO `sbhdb`.`weibo_base_data`(`user_id`, `name`, `counts`, `type`, `date`) VALUES ('%s', '%s', %s, 'expression', '%s')" % (
                        userId, expression, 1, weiboDate)
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
                    sql = "INSERT INTO `sbhdb`.`base_words`(`word`, `counts`, `is_show`, `type`, `word_length`) VALUES ('%s', %s, 1, 'BaseWords', %s)" % (
                        key, wordCount[key], len(key))
                    try:
                        getWeiboData().dbCursor.execute(sql)
                    except:
                        getWeiboData().db.rollback()
                selectSql = "SELECT id,counts FROM weibo_base_data WHERE type='word' AND user_id='%s' AND date='%s' AND name='%s' " % (
                    userId, weiboDate, key)
                getWeiboData().dbCursor.execute(selectSql)
                wordsResults = getWeiboData().dbCursor.fetchall()
                if (len(wordsResults) == 0):
                    insertSql = "INSERT INTO `sbhdb`.`weibo_base_data`(`user_id`, `name`, `counts`, `type`, `date`) VALUES ('%s', '%s', %s, 'word', '%s')" % (
                    userId, key, wordCount[key], weiboDate)
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
        MsgSql = "INSERT INTO `sbhdb`.`sys_msg`(`message`, `message_date`) VALUES ('%s', '%s')" % (
        "处理" + str(len(contentResults)) + "条评论", time.strftime('%Y-%m-%d %H:%M:%S', time.localtime(time.time())))
        try:
            getWeiboData().dbCursor.execute(MsgSql)
            getWeiboData().db.commit()
        except:
            getWeiboData().db.rollback()
        











