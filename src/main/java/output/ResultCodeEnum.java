package output;

/**
 * @author Meng Ling'en
 * @create 2022-03-22-15:40
 **/
public enum ResultCodeEnum {

    SUCCESS(1,"操作成功"),
    ERROR(0,"操作失败");

    private final int code;
    private final String msg;
//    private final int total;

    ResultCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
//        this.total = total;
    }

/*    public int getTotal() {
        return total;
    }*/

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
