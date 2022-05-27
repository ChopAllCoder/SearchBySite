package output;

import java.io.ObjectInputStream;
import java.io.Serializable;

import static output.ResultCodeEnum.ERROR;
import static output.ResultCodeEnum.SUCCESS;


/**
 * @author Meng Ling'en
 * @create 2022-03-22-15:13
 **/
public class Result<T> implements Serializable {
    private static final long serialVersionUID = 4580737268023862568L;

    private int code;
    private String msg;
    private T data;
    private int total; // 总条数


//    是否成功
    public boolean isSuccess(){
        return this.code == 1;
    }

//    成功时候引用
    public static <T> Result<T> success() {
        return success(SUCCESS);
    }

    public static <T> Result<T> success(T data) {
        return success(SUCCESS,data);
    }

    public static <T> Result<T> success(ResultCodeEnum rsc) {
        return success(rsc,null);
    }

    public static <T> Result<T> success(ResultCodeEnum rsc,T data) {
        int code=rsc.getCode();
        String msg = rsc.getMsg();
        return success(code,msg,data);
    }

    private static <T> Result<T> success(int code, String msg, T data) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }

    public static <T> Result<T> success(ResultCodeEnum rsc, T data, int total) {
        Result<T> result = new Result<>();
        result.setCode(rsc.getCode());
        result.setMsg(rsc.getMsg());
        result.setData(data);
        result.setTotal(total);
        return result;
    }

//    失败调用

    public static <T> Result<T> fail() {
        return fail(ERROR);
    }

    private static <T> Result<T> fail(T data) {
        return fail(ERROR,data);
    }

    public static <T> Result<T> fail(String msg) {
        return fail(ERROR.getCode(),msg,null);
    }

    private static <T> Result<T> fail(ResultCodeEnum rsc) {
        return fail(rsc,null);
    }

    private static <T> Result<T> fail(ResultCodeEnum rsc, T data) {
        int code = rsc.getCode();
        String msg = rsc.getMsg();
        return fail(code,msg,data);
    }

    private static <T> Result<T> fail(int code, String msg, T data) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }

    //    构造方法
    public Result() {

    }

    public Result(ResultCodeEnum rcs) {
        this.code = rcs.getCode();
        this.msg=rcs.getMsg();
    }
    public Result(ResultCodeEnum rcs,T data) {
        this.code = rcs.getCode();
        this.msg=rcs.getMsg();
        this.data=data;
    }



    public Result(int code, String msg, T data, int total) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.total = total;
    }

    public Result(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Result(String msg, T data) {
        this.msg = msg;
        this.data = data;
    }

    public Result(String msg) {
        this.msg = msg;
    }

    public Result(T data) {
        this.data = data;
    }

//    get、set方法
    public void setCode(int code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

    public int getTotal() {
        return total;
    }
}
