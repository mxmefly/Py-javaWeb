import re
import time
import pymysql
import time
from snownlp import SnowNLP
class getWeiboData():
    db = pymysql.connect("localhost", "root", "", "sbhdb")
    # 使用 cursor() 方法创建一个游标对象 cursor
    dbCursor = db.cursor()
    words={}


    def getSepWords(self,str):
        resAll = self.articleToSentence(str)
        wordList = []
        for res in resAll:
            oprStrLen = len(res)
            oprStr = res
            cursor = 0
            var = 1
            while (var == 1):
                temstr = ""
                if (oprStrLen - cursor >= 2):
                    temstr = oprStr[cursor:cursor + 2]
                elif (oprStrLen - cursor == 1):
                    wordList.append(oprStr[oprStrLen - 1])
                    break
                else:
                    break
                if(temstr in self.words.keys()):
                    wordsResults=self.words[temstr]
                else:
                    selectSql = "SELECT word,is_show FROM base_words WHERE word LIKE '%s'" % (temstr + "%")
                    self.dbCursor.execute(selectSql)
                    wordsResults = self.dbCursor.fetchall()
                    self.words[temstr]=wordsResults
                if (len(wordsResults) == 0):
                    wordList.append(res[cursor])
                    cursor = cursor + 1
                else:
                    candidateStr = temstr
                    isAdded=1
                    maxMatchLen = 2
                    for word in wordsResults:
                        wordLen = len(word[0])
                        if (wordLen > maxMatchLen):
                            if (cursor + wordLen <= oprStrLen):
                                temstr = oprStr[cursor:wordLen - 1]
                            else:
                                continue
                            if (temstr == word[0]):
                                candidateStr = temstr
                                isAdded=word[1]
                                maxMatchLen = wordLen
                                continue
                    if(isAdded==1):
                        wordList.append(candidateStr)
                    cursor += maxMatchLen
            wordList.append("++")
        return  wordList
    #过滤掉 表情 话题  标点符号断句
    def articleToSentence(self,articleStr):
        articleStr=re.sub(r'#(((?!#(.*)#).)*)#',"",articleStr)
        articleStr = re.sub(r'\[(((?!\[(.*)\]).)*)\]', "",articleStr)
        return re.split('[\s+\.\!\/_,$%^*(+\"\')]+|[+——()?【】“”！，。？、~@#￥%……&*（）]+|[a-zA-Z0-9]+', articleStr)

    def getExpression(self,articleStr):
        expression = re.findall('\[(((?!\[(.*)\]).)*)\]', articleStr)
        expressionList = []
        for exp in expression:
            expressionList.append(exp[0])
        return expressionList
    def getTopic(self,articleStr):
        topic = re.findall('#(((?!#(.*)#).)*)#', articleStr)
        topicList = []
        for top in topic:
            topicList.append(top[0])
        return topicList
    def getWordsCount(self,wordList):
        wordsCount={}
        for word in wordList:
            if(len(word)>1 and word!="++"):
                if(word in wordsCount.keys()):
                    wordsCount[word]= wordsCount[word]+1
                else:
                    wordsCount[word] = 1
        return  wordsCount
    def insertNewWord(self,wordList):
        temStr = ""
        temStrList = []
        for i in range(len(wordList)):
            if(len(wordList[i])==1 and self.ishan(wordList[i])):
                temStr+=wordList[i]
            if(len(wordList[i])>1):
                if(len(temStr)>1):
                    temStrList.append(temStr)
                temStr=""
        if (len(temStr) > 1):
            temStrList.append(temStr)
        for temStr in temStrList:
            selectSql = "SELECT * FROM new_words WHERE word = '%s'" % (temStr)
            self.dbCursor.execute(selectSql)
            wordsResults = self.dbCursor.fetchall()
            if(len(wordsResults)==0):
                insertSql="INSERT INTO `sbhdb`.`new_words`(`word`, `count`) VALUES ('%s', 0)"%(temStr)
                try:
                    # 执行SQL语句
                    self.dbCursor.execute(insertSql)
                except:
                    # 发生错误时回滚
                    self.db.rollback()
            else:
                updataSql="UPDATE `sbhdb`.`new_words` SET `count` = %s WHERE `id` = %s"%(wordsResults[0][1]+1,wordsResults[0][2])
                try:
                    # 执行SQL语句
                    self.dbCursor.execute(updataSql)
                except:
                    # 发生错误时回滚
                    self.db.rollback()
        try:
            self.db.commit()
        except:
            print("提交出错")

    def ishan(self,text):
        # for python 3.x
        # sample: ishan('一') == True, ishan('我&&你') == False
        return all('\u4e00' <= char <= '\u9fff' for char in text)
    


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
        selectSql = "SELECT comment_user_id,content,created_at,id,weibo_url FROM weibo_comment WHERE isProcess=0 ORDER BY created_at DESC LIMIT 2000"
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










