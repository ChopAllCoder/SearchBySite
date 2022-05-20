package util;

public class StringUtil {

    /**
     * 删除后面的内容
     * @param content
     * @param keywords
     * @return
     */
    public static String deleteAfter(String content, String keywords) {
        if(content == null || "".equals(content) || keywords == null) {
            return "";
        }
        if(!content.contains(keywords)) {
            return content;
        }
        return content.substring(0, content.indexOf(keywords));
    }

    public static void main(String[] args) {
        System.out.println(deleteAfter("北京-ss", "-"));
    }
}
