package output;

import java.io.Serializable;

/**
 * @author Meng Ling'en
 * @create 2022-03-22-15:57
 **/
public class ResultCode implements Serializable {

    private static final long serialVersionUID = -6269841958947880397L;
    /**
     * 状态码
     */
    private int code;

    /**
     * 状态信息
     */
    private String msg;
    /**
     * 总条数
     */
    private int total;



    public ResultCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
