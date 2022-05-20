package conf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author saysky
 * @date 2021/11/12
 */
public class Constant {

    public static String BAIDU_TRANSLATE_APP_ID = "";
    public static String BAIDU_TRANSLATE_SECURITY_KEY = "";
    public static Integer PAGE_NUM = 1;
    public static Integer PAGE_SIZE = 10;
    public static String BING_KEY = "";
    public static String BING_FILTER = "";
    public static String LANG = "";
    public static String BING_TIME = "";
    public static String SECOND_FILTER = "";

    public static String DB_NAME = "";
    public static String DB_USER = "";
    public static String DB_PASSWORD = "";


    public static String ROOT_PATH = "";
    public static String BING_WANGYE_RESULT_OUTPUT_PATH = "";
    public static String BING_NEWS_RESULT_OUTPUT_PATH = "";

    public static String INPUT_PATH = "";

    public static String LANG_NAME = "";
    public static Integer MAX_TIMEOUT = 5;

    public static Boolean SPIDER_WANGYE = false;
    public static Boolean SPIDER_NEWS = false;

    public static HashMap<String, ArrayList<String>> WEBSITE_MAP = new HashMap<>(); // 盛放website/ 下读入的定向检索网址
    public static HashMap<String, String> BAIDU_LANG_CODE_MAP = new HashMap<>();
    public static HashMap<String, String> BING_MKT_CODE_MAP = new HashMap<>();
    public static HashMap<String, String> BING_SETLANG_CODE_MAP = new HashMap<>();
    public static HashMap<String, String> CATEGORY_MAP = new HashMap<>();

    static {
        BAIDU_LANG_CODE_MAP.put("中简", "zh");
        BAIDU_LANG_CODE_MAP.put("英语", "en");
        BAIDU_LANG_CODE_MAP.put("法语", "fra");
        BAIDU_LANG_CODE_MAP.put("俄语", "ru");
        BAIDU_LANG_CODE_MAP.put("德语", "de");
        BAIDU_LANG_CODE_MAP.put("韩语", "kor");
        BAIDU_LANG_CODE_MAP.put("日语", "jp");
    }

    static {
        BING_MKT_CODE_MAP.put("中简", "zh-CN");
        BING_MKT_CODE_MAP.put("英语", "en-US");
        BING_MKT_CODE_MAP.put("法语", "fr-FR");
        BING_MKT_CODE_MAP.put("俄语", "ru-RU");
        BING_MKT_CODE_MAP.put("德语", "de-DE");
        BING_MKT_CODE_MAP.put("韩语", "ko-KR");
        BING_MKT_CODE_MAP.put("日语", "ja-JP");

        BING_SETLANG_CODE_MAP.put("中简", "zh");
        BING_SETLANG_CODE_MAP.put("英语", "en");
        BING_SETLANG_CODE_MAP.put("法语", "fr");
        BING_SETLANG_CODE_MAP.put("俄语", "ru");
        BING_SETLANG_CODE_MAP.put("德语", "de");
        BING_SETLANG_CODE_MAP.put("韩语", "ko");
        BING_SETLANG_CODE_MAP.put("日语", "ja");
    }

    static {
        CATEGORY_MAP.put("1", "先进钢铁材料");
        CATEGORY_MAP.put("2", "先进有色金属材料");
        CATEGORY_MAP.put("3", "先进高分子材料");
        CATEGORY_MAP.put("4", "先进无机非金属材料");
        CATEGORY_MAP.put("5", "高性能纤维及制品和复合材料");
        CATEGORY_MAP.put("6", "前沿新材料");
    }

    public static Map<String, String> WEB_NAME_MAP = new HashMap<>();

}
