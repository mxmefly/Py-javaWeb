import re
import time
import pymysql
import time
from sina.settings import  MYSQL_HOST, MYSQL_USER, MYSQL_PASSWORD,MYSQL_DB

class getWeiboData():
    db = pymysql.connect(MYSQL_HOST, MYSQL_USER, MYSQL_PASSWORD,MYSQL_DB)
    # 使用 cursor() 方法创建一个游标对象 cursor
    dbCursor = db.cursor()
    words = {}

    def getSepWords(self, str):
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
                if (temstr in self.words.keys()):
                    wordsResults = self.words[temstr]
                else:
                    selectSql = "SELECT word,is_show FROM base_words WHERE word LIKE '%s'" % (temstr + "%")
                    self.dbCursor.execute(selectSql)
                    wordsResults = self.dbCursor.fetchall()
                    self.words[temstr] = wordsResults
                if (len(wordsResults) == 0):
                    wordList.append(res[cursor])
                    cursor = cursor + 1
                else:
                    candidateStr = temstr
                    isAdded = 1
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
                                isAdded = word[1]
                                maxMatchLen = wordLen
                                continue
                    if (isAdded == 1):
                        wordList.append(candidateStr)
                    cursor += maxMatchLen
            wordList.append("++")
        return wordList

    # 过滤掉 表情 话题  标点符号断句
    def articleToSentence(self, articleStr):
        articleStr = re.sub(r'#(((?!#(.*)#).)*)#', "", articleStr)
        articleStr = re.sub(r'\[(((?!\[(.*)\]).)*)\]', "", articleStr)
        return re.split('[\s+\.\!\/_,$%^*(+\"\')]+|[+——()?【】“”！，。？、~@#￥%……&*（）]+|[a-zA-Z0-9]+', articleStr)

    def getExpression(self, articleStr):
        expression = re.findall('\[(((?!\[(.*)\]).)*)\]', articleStr)
        expressionList = []
        for exp in expression:
            expressionList.append(exp[0])
        return expressionList

    def getTopic(self, articleStr):
        topic = re.findall('#(((?!#(.*)#).)*)#', articleStr)
        topicList = []
        for top in topic:
            topicList.append(top[0])
        return topicList

    def getWordsCount(self, wordList):
        wordsCount = {}
        for word in wordList:
            if (len(word) > 1 and word != "++"):
                if (word in wordsCount.keys()):
                    wordsCount[word] = wordsCount[word] + 1
                else:
                    wordsCount[word] = 1
        return wordsCount

    def insertNewWord(self, wordList):
        temStr = ""
        temStrList = []
        for i in range(len(wordList)):
            if (len(wordList[i]) == 1 and self.ishan(wordList[i])):
                temStr += wordList[i]
            if (len(wordList[i]) > 1):
                if (len(temStr) > 1):
                    temStrList.append(temStr)
                temStr = ""
        if (len(temStr) > 1):
            temStrList.append(temStr)
        for temStr in temStrList:
            selectSql = "SELECT * FROM new_words WHERE word = '%s'" % (temStr)
            self.dbCursor.execute(selectSql)
            wordsResults = self.dbCursor.fetchall()
            if (len(wordsResults) == 0):
                insertSql = "INSERT INTO `sbhdb`.`new_words`(`word`, `count`) VALUES ('%s', 0)" % (temStr)
                try:
                    # 执行SQL语句
                    self.dbCursor.execute(insertSql)
                except:
                    # 发生错误时回滚
                    self.db.rollback()
            else:
                updataSql = "UPDATE `sbhdb`.`new_words` SET `count` = %s WHERE `id` = %s" % (
                wordsResults[0][1] + 1, wordsResults[0][2])
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

    def ishan(self, text):
        # for python 3.x
        # sample: ishan('一') == True, ishan('我&&你') == False
        return all('\u4e00' <= char <= '\u9fff' for char in text)

