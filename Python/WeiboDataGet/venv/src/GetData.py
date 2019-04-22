import re
import time
import pymysql
class getWeiboData():
    db = pymysql.connect("localhost", "root", "", "sbhdb")
    # 使用 cursor() 方法创建一个游标对象 cursor
    dbCursor = db.cursor()

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
                selectSql = "SELECT word FROM base_words WHERE word LIKE '%s'" % (temstr + "%")
                self.dbCursor.execute(selectSql)
                wordsResults = self.dbCursor.fetchall()
                if (len(wordsResults) == 0):
                    wordList.append(res[cursor])
                    cursor = cursor + 1
                else:
                    candidateStr = temstr
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
                                maxMatchLen = wordLen
                                continue
                    wordList.append(candidateStr)
                    cursor += maxMatchLen
            wordList.append("++")
        return  wordList
    #过滤掉 表情 话题  标点符号断句
    def articleToSentence(self,articleStr):
        topic=re.findall('#(((?!#(.*)#).)*)#',articleStr)
        for top in topic:
            articleStr = articleStr.replace("#"+top[0]+"#","")
        expression = re.findall('\[(((?!\[(.*)\]).)*)\]', articleStr)
        for exp in expression:
            articleStr = articleStr.replace("["+exp[0]+"]","")
        return re.split('[\s+\.\!\/_,$%^*(+\"\')]+|[+——()?【】“”！，。？、~@#￥%……&*（）]+', articleStr)

    def getTopic(self,articleStr):
        topic = re.findall('#(((?!#(.*)#).)*)#', articleStr)
        topicList = []
        for top in topic:
            topicList.append(top[0])
        return topicList

    def getExpression(self,articleStr):
        expression = re.findall('\[(((?!\[(.*)\]).)*)\]', articleStr)
        expressionList = []
        for exp in expression:
            expressionList.append(exp[0])
        return expressionList

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
            if(wordList[i]!="++" and len(wordList[i])==1):
                if(len(temStr)==0  ):
                    temStr=wordList[i]
                if(i<len(wordList)-1):
                    if(len(wordList[i+1])==1):
                        temStr=temStr+wordList[i+1]
                    else:
                        if(len(temStr)>1):
                            temStrList.append(temStr)
                            temStr=""
        print(temStrList)
        for temStr in temStrList:
            selectSql = "SELECT * FROM new_words WHERE word = '%s'" % (temStr)
            self.dbCursor.execute(selectSql)
            wordsResults = self.dbCursor.fetchall()
            if(len(wordsResults)==0):
                insertSql="INSERT INTO `sbhdb`.`new_words`(`word`, `count`) VALUES ('%s', 0)"%(temStr)
                try:
                    # 执行SQL语句
                    self.dbCursor.execute(insertSql)
                    # 提交到数据库执行
                    self.db.commit()
                except:
                    # 发生错误时回滚
                    self.db.rollback()
            else:
                updataSql="UPDATE `sbhdb`.`new_words` SET `count` = %s WHERE `id` = %s"%(wordsResults[0][1]+1,wordsResults[0][2])
                try:
                    # 执行SQL语句
                    self.dbCursor.execute(updataSql)
                    # 提交到数据库执行
                    self.db.commit()
                except:
                    # 发生错误时回滚
                    self.db.rollback()


    def ishan(self,text):
        # for python 3.x
        # sample: ishan('一') == True, ishan('我&&你') == False
        return all('\u4e00' <= char <= '\u9fff' for char in text)
    


if __name__ == "__main__":
    selectSql="SELECT user_id,content,created_at,_id FROM weibo_info WHERE isProcess=0 ORDER BY created_at DESC"
    getWeiboData().dbCursor.execute(selectSql)
    contentResults=getWeiboData().dbCursor.fetchall();
    print("此次需要处理"+str(len(contentResults))+"条微博数据")
    for content in contentResults:
        print(content)
        updateSql = "UPDATE `sbhdb`.`weibo_info` SET `isProcess` = 1 WHERE `_id` = '%s'" % (content[3])
        try:
            getWeiboData().dbCursor.execute(updateSql)
            getWeiboData().db.commit()
        except:
            getWeiboData().db.rollback()
        weiboDate=content[2][0:10]
        userId=content[0]
        #获取话题
        topicList=getWeiboData().getTopic(content[1])
        for topic in topicList:
            insertSql = "INSERT INTO `sbhdb`.`weibo_topic`(`topic`, `userId`, `date`) VALUES ('%s', '%s', '%s')" % (
                topic,userId, weiboDate)
            try:
                getWeiboData().dbCursor.execute(insertSql)
                getWeiboData().db.commit()
            except:
                getWeiboData().db.rollback()
        #获取分词
        wordCount=getWeiboData().getWordsCount(getWeiboData().getSepWords(content[1]))
        # 获取表情
        expressionList = getWeiboData().getExpression(content[1])
        for expression in expressionList:
            selectSql = "SELECT id,counts FROM weibo_base_data WHERE type='expression' AND user_id='%s' AND date='%s' AND name='%s'" % (
            userId, weiboDate, expression)
            getWeiboData().dbCursor.execute(selectSql)
            wordsResults = getWeiboData().dbCursor.fetchall()
            if (len(wordsResults) == 0):
                insertSql = "INSERT INTO `sbhdb`.`weibo_base_data`(`user_id`, `name`, `counts`, `type`, `date`) VALUES ('%s', '%s', %s, 'expression', '%s')" % (
                userId, expression, 1, weiboDate)
                try:
                    getWeiboData().dbCursor.execute(insertSql)
                    getWeiboData().db.commit()
                except:
                    getWeiboData().db.rollback()
            else:
                updateSql = "UPDATE `sbhdb`.`weibo_base_data` SET `counts` = %s WHERE `id` = %s" % (
                wordsResults[0][1] + 1, wordsResults[0][0])
                try:
                    getWeiboData().dbCursor.execute(updateSql)
                    getWeiboData().db.commit()
                except:
                    getWeiboData().db.rollback()
        for key in wordCount.keys():
            selectSql = "SELECT id,counts FROM base_words WHERE word = '%s'" % (key)
            getWeiboData().dbCursor.execute(selectSql)
            wordsResults = getWeiboData().dbCursor.fetchall()
            if(len(wordsResults)!=0):
                updateSql = "UPDATE `sbhdb`.`base_words` SET `counts` = %s WHERE `id` = %s" % (
                wordsResults[0][1] + wordCount[key], wordsResults[0][0])
                try:
                    getWeiboData().dbCursor.execute(updateSql)
                    getWeiboData().db.commit()
                except:
                    getWeiboData().db.rollback()
            selectSql = "SELECT id,counts FROM weibo_base_data WHERE type='word' AND user_id='%s' AND date='%s' AND name='%s'" % (userId,weiboDate,key)
            getWeiboData().dbCursor.execute(selectSql)
            wordsResults = getWeiboData().dbCursor.fetchall()
            if(len(wordsResults)==0):
                insertSql="INSERT INTO `sbhdb`.`weibo_base_data`(`user_id`, `name`, `counts`, `type`, `date`) VALUES ('%s', '%s', %s, 'word', '%s')"%(userId,key,wordCount[key],weiboDate)
                try:
                    getWeiboData().dbCursor.execute(insertSql)
                    getWeiboData().db.commit()
                except:
                    getWeiboData().db.rollback()
            else:
                updateSql = "UPDATE `sbhdb`.`weibo_base_data` SET `counts` = %s WHERE `id` = %s" % (wordsResults[0][1] + wordCount[key], wordsResults[0][0])
                try:
                    getWeiboData().dbCursor.execute(updateSql)
                    getWeiboData().db.commit()
                except:
                    getWeiboData().db.rollback()











