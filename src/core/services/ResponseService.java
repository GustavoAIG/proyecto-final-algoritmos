package core.services;

public class ResponseService<T> {
    /*
    success: true
    result: "candidato",
    
    success: true
    result: [],
    */
    public boolean success;
    public T result;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
    
    
}
