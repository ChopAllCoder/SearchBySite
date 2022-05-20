package util;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author saysky
 * @create 2022/1/9 22:15
 */
public class HtmlUtil {


    public static void main(String[] args) throws UnsupportedEncodingException {
        String htmlContent = HttpUtil.getHtmlContent("http://www.xincailiao.com/news/news_detail.aspx?id=603375");

        String result = getArticleContent(htmlContent);
        String result2 = HttpUtil.delHTMLTag(result);
        System.out.println(result2);
        String title = getTitle(htmlContent);
                  System.out.println(result2);

//    System.out.println(title);
    }

    public static String getArticleContent(String htmlContent) {
        String bodyContent = getBody(htmlContent);
        return getContent(bodyContent);
    }

    private static final String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; // 定义script的正则表达式
    private static final String regEx_script2 = "<SCRIPT [^>]*?>[\\s\\S]*?<\\/SCRIPT >"; // 定义script的正则表达式
    private static final String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; // 定义style的正则表达式
    private static final String regEx_style2 = "<STYLE[^>]*?>[\\s\\S]*?<\\/STYLE>"; // 定义style的正则表达式
    private static final String regEx3 = "<!--((?!<!--).)*-->"; // 定义style的正则表达式

    /**
     * 获取html中body的内容 包含body标签
     *
     * @param htmlStr html代码
     * @return
     */
    private static String getBody(String htmlStr) {


        String pattern = "<body[^>]*>([\\s\\S]*)<\\/body>";

        Pattern p_body = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
        Matcher m_body = p_body.matcher(htmlStr);
        if (m_body.find()) {

            htmlStr = m_body.group();
        }
        Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
        Matcher m_script = p_script.matcher(htmlStr);
        htmlStr = m_script.replaceAll(" "); // 过滤script标签

        Pattern p_script2 = Pattern.compile(regEx_script2, Pattern.CASE_INSENSITIVE);
        Matcher m_script2 = p_script2.matcher(htmlStr);
        htmlStr = m_script2.replaceAll(" "); // 过滤script标签


        Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
        Matcher m_style = p_style.matcher(htmlStr);
        htmlStr = m_style.replaceAll(" "); // 过滤style标签

        Pattern p_style2 = Pattern.compile(regEx_style2, Pattern.CASE_INSENSITIVE);
        Matcher m_style2 = p_style2.matcher(htmlStr);
        htmlStr = m_style2.replaceAll(" "); // 过滤style标签

        Pattern pattern3 = Pattern.compile(regEx3, Pattern.CASE_INSENSITIVE);
        Matcher matcher3 = pattern3.matcher(htmlStr);
        htmlStr = matcher3.replaceAll(" "); // 过滤style标签


        return htmlStr;
    }


    private static int _depth = 5;
    private static int _limitCount = 150;
    private static int _headEmptyLines = 2;
    private static int _endLimitCharCount = 20;
    private static boolean _appendMode = true;


    /// <summary>
    /// 从body标签文本中分析正文内容
    /// </summary>
    /// <param name="bodyText">只过滤了script和style标签的body文本内容</param>
    /// <param name="content">返回文本正文，不包含标签</param>
    /// <param name="contentWithTags">返回文本正文包含标签</param>
    private static String getContent(String bodyText) {

        if (!bodyText.contains("\n")) {
            bodyText = bodyText.replaceAll(">", ">\n");
        }

        String[] orgLines = null;   // 保存原始内容，按行存储
        String[] lines = null;      // 保存干净的文本内容，不包含标签

        orgLines = bodyText.split("\n");
        lines = new String[orgLines.length];
        // 去除每行的空白字符,剔除标签
        for (int i = 0; i < orgLines.length; i++) {
            String lineInfo = orgLines[i];
            // 处理回车，使用[crlf]做为回车标记符，最后统一处理
//            if(lineInfo.contains("alert") || lineInfo.contains("window")
//                    || lineInfo.contains("return")
//                    || lineInfo.contains("script")
//                    || lineInfo.contains("img")
//                    || lineInfo.contains("input")
//                    || lineInfo.contains("&#")
//
//            ) {
//                lines[i] = "";
//            } else {
            lineInfo = lineInfo.replaceAll("(?is)</p >|<br.*?/>", "[crlf]");
            lines[i] = lineInfo.replaceAll("(?is)<.*?>", "").trim();
//            }
        }

        for (String str : lines) {
            if (str.contains("玻璃PVB胶是什么")) {
                System.out.println("");
            }
        }


        StringBuilder sb = new StringBuilder();
        StringBuilder orgSb = new StringBuilder();

        int preTextLen = 0;         // 记录上一次统计的字符数量
        int startPos = -1;          // 记录文章正文的起始位置
        for (int i = 0; i < lines.length - _depth; i++) {
            int len = 0;
            for (int j = 0; j < _depth; j++) {
                len += lines[i + j].length();
            }

            if (startPos == -1)     // 还没有找到文章起始位置，需要判断起始位置
            {
                if (preTextLen > _limitCount && len > 0)    // 如果上次查找的文本数量超过了限定字数，且当前行数字符数不为0，则认为是开始位置
                {
                    // 查找文章起始位置, 如果向上查找，发现2行连续的空行则认为是头部
                    int emptyCount = 0;
                    for (int j = i - 1; j > 0; j--) {
                        if (lines[j] == null || "".equals(lines[j].trim())) {
                            emptyCount++;
                        } else {
                            emptyCount = 0;
                        }
                        if (emptyCount == _headEmptyLines) {
                            startPos = j + _headEmptyLines;
                            break;
                        }
                    }
                    // 如果没有定位到文章头，则以当前查找位置作为文章头
                    if (startPos == -1) {
                        startPos = i;
                    }
                    // 填充发现的文章起始部分
                    for (int j = startPos; j <= i; j++) {
                        String str = lines[j];
                        if (str.contains("alert")
                                || str.contains("window")
                                || str.contains("return")
                                || str.contains("script")
//                                || str.contains("img")
                                || str.contains(".php")
//                                || str.contains(".html")
                                || str.contains("input")
                                || str.contains("&#5")

                        ) {
                            str = "";
                        }
                        sb = sb.append(str);
                        orgSb = orgSb.append(orgLines[j]);
                    }
                }
            } else {
                //if (len == 0 && preTextLen == 0)    // 当前长度为0，且上一个长度也为0，则认为已经结束
                if (len <= _endLimitCharCount && preTextLen < _endLimitCharCount)    // 当前长度为0，且上一个长度也为0，则认为已经结束
                {
                    if (!_appendMode) {
                        break;
                    }
                    startPos = -1;
                }
                String str = lines[i];
                if (str.contains("alert") || str.contains("window")
                        || str.contains("return")
                        || str.contains("script")
//                        || str.contains("img")
                        || str.contains(".php")
//                        || str.contains(".html")
                        || str.contains("input")
                        || str.contains("&#5")

                ) {
                    str = "";
                }
                sb = sb.append(str);
                orgSb = orgSb.append(orgLines[i]);
            }
            preTextLen = len;
        }

        String result = sb.toString();
        // 处理回车符，更好的将文本格式化输出
        String content = result.replace("[crlf]", "");
        return content;
    }

    /// <summary>
    /// 获取时间
    /// </summary>
    /// <param name="html"></param>
    /// <returns></returns>
    public static String getTitle(String html) {

//        String titlePattern = "<title>.*?</title>";
//        String h1Pattern = "<h1>.*?</h1>";
//        String h2Pattern = "<h2>.*?</h2>";

        String titlePattern = "<title((?!<title).)*</title>";
        String h1Pattern = "<h1((?!<h1).)*</h1>";
        String h2Pattern = "<h2((?!<h2).)*</h2>";
        String h3Pattern = "<h3((?!<h3).)*</h3>";

        String title = getRegexStr(html, h1Pattern);
        title = HttpUtil.delHTMLTag(title);
        if (title == null || "".equals(title.trim()) || title.contains("&#")) {
            title = getRegexStr(html, titlePattern);
            title = HttpUtil.delHTMLTag(title);
        }
        if (title == null || "".equals(title.trim()) || title.contains("&#")) {
            title = getRegexStr(html, h2Pattern);
            title = HttpUtil.delHTMLTag(title);
        }
        if (title == null || "".equals(title.trim()) || title.contains("&#")) {
            title = getRegexStr(html, h3Pattern);
            title = HttpUtil.delHTMLTag(title);
        }
        return title;
    }

    private static String getRegexStr(String content, String regex) {
        Pattern p_body = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher m_body = p_body.matcher(content);
        if (m_body.find()) {
            return m_body.group();
        }
        return null;
    }
}
