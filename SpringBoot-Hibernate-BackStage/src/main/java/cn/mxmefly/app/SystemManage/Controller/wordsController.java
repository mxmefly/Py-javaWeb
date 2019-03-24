package cn.mxmefly.app.SystemManage.Controller;

import cn.mxmefly.app.SystemManage.Bean.BaseWords;
import cn.mxmefly.app.SystemManage.Dao.Repository.BaseWordsRespository;
import cn.mxmefly.app.Common.CreateResult;
import cn.mxmefly.app.Common.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class wordsController {
    @Autowired
    private BaseWordsRespository baseWordsRespository;

    private CreateResult createResult = new CreateResult();

    @PostMapping("/wordsImport")
    public Results baseWordsImport(@RequestBody Map map){
        String path = (String) map.get("filePath");
        String type = (String) map.get("wordType");
        int count=0;
        try {
            FileInputStream inputStream = new FileInputStream(path);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String word = null;
            while((word = bufferedReader.readLine()) != null)
            {
                System.err.println(count+" : "+word);
                BaseWords baseWords =new BaseWords();
                baseWords.setWord(word);
                baseWords.setType(type);
                baseWords.setCounts(0);
                baseWords.setIsShow(1);
                baseWords.setSentiments(-1);
                baseWords.setWordLength(word.length());
                baseWordsRespository.save(baseWords);
                count++;
            }
            inputStream.close();
            bufferedReader.close();
            return createResult.getResults("共导入"+count+"条词库");
        }catch (IOException ie){
            ie.printStackTrace();
            return createResult.getResults(false,"未找到该路径请核实路径");
        }
    }
    @PostMapping("/test")
    public Results Test(@RequestBody Map map){
        String str= (String) map.get("str");
        return createResult.getResults(getWordsInfo(str));
    }

    /*获取断词信息 时间复杂度<On！*/
    public Map getWordsInfo(String str){
        Map map = new HashMap();
        String[] res= articleToSentence(str);
        /*存储断句词*/
        List<String> wordList = new ArrayList<>();
        for(int resI=0;resI<res.length;resI++){
            String oprStr =  res[resI];
            int oprStrLen = oprStr.length();
            /*记录指针移动的游标*/
            int cursor=0;
            while(true){
                String temStr="";
                if(oprStrLen-cursor>2){
                    /*得到游标所在字符和下一个字符*/
                    temStr=oprStr.substring(cursor,cursor+2);
                }else if(oprStrLen-cursor==2) {
                    temStr=oprStr.substring(cursor);
                }else if(oprStrLen-cursor==1){
                    wordList.add(oprStr.substring(cursor));
                    break;
                }else{
                    break;
                }
                int temStrLenth=2;
                List<BaseWords> lbws = baseWordsRespository.findAllByWordLike(temStr+"%");
                if(lbws.size()==0){
                    wordList.add(oprStr.charAt(cursor)+"");
                    cursor++;
                }else{
                    int maxMatchLen=2;
                    String candidateStr=temStr;
                    for(int i=0;i<lbws.size();i++){
                        BaseWords baseWords = lbws.get(i);
                        if(baseWords.getWordLength()>maxMatchLen){
                            if(cursor+2+baseWords.getWordLength()-maxMatchLen<oprStrLen){
                                temStr=oprStr.substring(cursor,cursor+2+baseWords.getWordLength()-maxMatchLen);
                            }else if(cursor+2+baseWords.getWordLength()-maxMatchLen==oprStrLen){
                                temStr=oprStr.substring(cursor);
                            }else {
                                break;
                            }
                            if(temStr.equals(baseWords.getWord())){
                                candidateStr=temStr;
                                maxMatchLen=baseWords.getWordLength();
                                continue;
                            }
                        }
                    }
                    wordList.add(candidateStr);
                    cursor+=maxMatchLen;
                }
            }
            /*++用来区分是否来自两句话*/
            wordList.add("++");
        }
        return getWordResults(wordList);
    }
    /*将文段安装句读分成若干字符数组*/
    private String[] articleToSentence(String articleStr){
        return articleStr.split("[\\p{P}+~$`^=|<>～｀＄＾＋＝｜＜＞￥×]");
    }
    /*处理分词结果集 时间复杂度On*/
    private Map getWordResults(List<String> wordList){
        List<BaseWords> listWords = new ArrayList<>();
        List<String> temStrList = new ArrayList<>();
        String temStr="";
        int[] sentiments={0,0,0,0,0,0,0,0,0,0,0};
        int sentimentsAll=0;
        int sentimentsCount=0;
        for(int i= 0;i<wordList.size();i++){
            String str = wordList.get(i);
            if(str!="++"){
                if(str.length()==1){
                    if(baseWordsRespository.findByWord(str)==null){
                        BaseWords baseWords = new BaseWords();
                        baseWords.setWord(str);
                        listWords.add(baseWords);
                    }else{
                        BaseWords baseWords= baseWordsRespository.findByWord(str);
                        sentiments[baseWords.getSentiments()-1]++;
                        sentimentsAll+=5;
                        sentimentsCount++;
                        listWords.add(baseWords);
                    }
                    if(temStr.length()==0){
                        temStr+=str;
                    }
                    if(i<wordList.size()-1){
                        if(wordList.get(i+1).length()==1){
                            temStr+=wordList.get(i+1);
                        }else {
                            if(temStr.length()>1){
                                temStrList.add(temStr);
                                temStr="";
                            }
                        }
                    }
                }
                if (str.length()>1){
                    if(baseWordsRespository.findByWord(str)!=null){
                        BaseWords baseWords= baseWordsRespository.findByWord(str);
                        baseWords.setCounts(baseWords.getCounts()+1);
                        baseWordsRespository.save(baseWords);
                        sentiments[baseWords.getSentiments()-1]++;
                        sentimentsAll+=5;
                        sentimentsCount++;
                        listWords.add(baseWords);
                    }
                }
            }
        }
        Map map = new HashMap();
        map.put("listWords",listWords);
        map.put("temStrList",temStrList);
        map.put("sentiments",sentiments);
        map.put("sentiment",(float)sentimentsAll/sentimentsCount);
        return map;
    }
}
