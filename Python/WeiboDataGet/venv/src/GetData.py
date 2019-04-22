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
        temStrList=[]
        wordsCount={}
        for word in wordList:
            if(len(word)>1 and word!="++"):
                if(word in wordsCount.keys()):
                    wordsCount[word]= wordsCount[word]+1
                else:
                    wordsCount[word] = 1
        return  wordsCount

    def ishan(self,text):
        # for python 3.x
        # sample: ishan('一') == True, ishan('我&&你') == False
        return all('\u4e00' <= char <= '\u9fff' for char in text)
    


if __name__ == "__main__":
    str="#好难过#我好伤心，还有点害怕[闭眼][闭眼][闭眼]"
    print(getWeiboData().getSepWords(str))
    print(getWeiboData().getWordsCount(getWeiboData().getSepWords(str)))



