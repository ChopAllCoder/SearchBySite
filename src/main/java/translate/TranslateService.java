package translate;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import conf.Constant;

import java.util.Objects;

/**
 * @author  saysky
 * @date 2021/11/12
 */
public class TranslateService {

    public static final String DEFAULT_LANG = "zh";

    /**
     * 翻译
     *
     * @param toLang
     * @param word
     * @return
     */
    public String translate(String toLang, String word) {
        if(Objects.equals(toLang, "zh")) {
            return word;
        }
        TransApi api = new TransApi(Constant.BAIDU_TRANSLATE_APP_ID, Constant.BAIDU_TRANSLATE_SECURITY_KEY);
        String transResultStr = api.getTransResult(word, DEFAULT_LANG, toLang);
        JSONObject result = JSONObject.parseObject(transResultStr);
        JSONArray jsonArray = result.getJSONArray("trans_result");
        JSONObject jsonObject = jsonArray.getJSONObject(0);
        String dst = jsonObject.getString("dst");
        return dst;
    }
}
