package cn.mxmefly.app.SystemManage.Bean;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class BaseWords {
    @Id
    @GeneratedValue
    int id;
    String word;

    String type;
    int counts;
    int wordLength;
    int isShow;
    String wordIndex;
    int sentiments;
    public BaseWords(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCounts() {
        return counts;
    }

    public void setCounts(int counts) {
        this.counts = counts;
    }

    public int getWordLength() {
        return wordLength;
    }

    public void setWordLength(int wordLength) {
        this.wordLength = wordLength;
    }

    public int getIsShow() {
        return isShow;
    }

    public void setIsShow(int isShow) {
        this.isShow = isShow;
    }

    public String getWordIndex() {
        return wordIndex;
    }

    public void setWordIndex(String wordIndex) {
        this.wordIndex = wordIndex;
    }

    public int getSentiments() {
        return sentiments;
    }

    public void setSentiments(int sentiments) {
        this.sentiments = sentiments;
    }
}
