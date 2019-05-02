package cn.mxmefly.app.Bussiness.Bean;

public class WordsCounts {
    String word;
    int count;
    public WordsCounts(String word,int counts){
        this.word=word;
        this.count=counts;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
