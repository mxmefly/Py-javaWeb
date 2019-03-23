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
}
