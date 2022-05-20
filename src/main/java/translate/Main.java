package translate;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class Main {

    // 在平台申请的APP_ID 详见 http://api.fanyi.baidu.com/api/trans/product/desktop?req=developer
    private static final String APP_ID = "20211111000996870";
    private static final String SECURITY_KEY = "zeMiByl9wrF6HzkzUs4Q";

    public static void main(String[] args) {
        TransApi api = new TransApi(APP_ID, SECURITY_KEY);

        String query = "聚乙烯";
        String transResultStr = api.getTransResult(query, "zh", "en");
        JSONObject result = JSONObject.parseObject(transResultStr);
        JSONArray jsonArray = result.getJSONArray("trans_result");
        JSONObject jsonObject = jsonArray.getJSONObject(0);
        String dst = jsonObject.getString("dst");
        System.out.println(dst);
    }

}
