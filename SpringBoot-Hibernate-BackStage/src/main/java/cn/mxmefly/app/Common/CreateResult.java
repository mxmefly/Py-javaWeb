package cn.mxmefly.app.Common;

public class CreateResult {
    public Results getResults(Object data){
        Results results = new Results(false);
        results.setData(data);
        results.setMessage("请求成功");
        results.setSuccess(true);
        return results;
    }
    public Results getResults(Object data,String message){
        Results results = new Results(false);
        results.setData(data);
        results.setMessage(message);
        results.setSuccess(true);
        return results;
    }
    public Results getResults(Boolean success,String message){
        return new Results(success,message);
    }
    public Results getResults(Boolean success,String message,Object data){
        return new Results(success,message,data);
    }
}
