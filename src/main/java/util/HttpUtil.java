package util;

import conf.Constant;
import net.htmlparser.jericho.*;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author saysky
 * @date 2021/11/12
 */
public class HttpUtil {

    private static final String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; // 定义script的正则表达式
    private static final String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; // 定义style的正则表达式
    private static final String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
    //    private static final String regEx_space = "\\s*|\t|\r|\n";// 定义空格回车换行符
    private static final String regEx_space = "\\s{2,}|\t|\r|\n";// 定义空格回车换行符
    private static final String regEx_w = "<w[^>]*?>[\\s\\S]*?<\\/w[^>]*?>";//定义所有w标签

    /**
     * 根据百度url,获取原本url
     *
     * @throws IOException
     */
    public static String getRealUrlFromBaiduUrl(String url) {
        Connection.Response res = null;
        int itimeout = 60000;
        try {
            res = Jsoup.connect(url).timeout(itimeout).method(Connection.Method.GET).followRedirects(false).execute();
            return res.header("Location");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 读取网页全部内容
     */

    public static String getHtmlContent(String htmlurl) {
        // 获取线程池
        ExecutorService es = Executors.newFixedThreadPool(1);

        // Future用于执行多线程的执行结果
        Future<String> future = es.submit(() -> {
            try {
                String charset = getCharset(htmlurl);
                if (charset == null) {
                    return httpsGet1(htmlurl);
                }
                URL url;
                String temp;
                StringBuffer sb = new StringBuffer();
                try {
                    url = new URL(htmlurl);
                    BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), charset));// 读取网页全部内容
                    while ((temp = in.readLine()) != null) {
                        sb.append(temp);
                    }
                    in.close();
                } catch (final MalformedURLException me) {
                    System.out.println("你输入的URL格式有问题!");
                    me.getMessage();
                } catch (final Exception e) {
//                    e.printStackTrace();
                }
                return sb.toString();

            } catch (Exception e) {
                System.out.println("获取网页失败:" + htmlurl);
                return "";
            }
        });

        try {
            // futrue.get()测试被执行的程序是否能在timeOut时限内返回字符串
            return future.get(Constant.MAX_TIMEOUT, TimeUnit.SECONDS);//任务处理超时时间设为 3 秒
        } catch (Exception ex) {
            System.out.println("获取网页超时:" + htmlurl);
            return "";
        } finally {
            // 关闭线程池
            es.shutdown();
        }

    }

    public static String getCharset(String htmlurl) {
        URL url;
        String temp;
        try {
            url = new URL(htmlurl);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), "utf-8"));// 读取网页全部内容
            while ((temp = in.readLine()) != null) {
                if (temp.contains("charset=")) {
                    if (temp.contains("meta") && (temp.contains("utf") || temp.contains("UT"))) {
                        return "utf-8";
                    } else if (temp.contains("gb2312")) {
                        return "gb2312";
                    } else if (temp.contains("gbk") || temp.contains("GBK")) {
                        return "gbk";
                    }
                }
            }
            in.close();
        } catch (Exception e) {
//            e.printStackTrace();
            return "utf-8";
        }
        return null;
    }


    /**
     * https get请求
     * 中简
     *
     * @param url
     * @return
     */
    public static String httpsGet1(String url) {
//        MicrosoftConditionalCommentTagTypes.register();
        PHPTagTypes.register();
        PHPTagTypes.PHP_SHORT.deregister(); // remove PHP short tags for this example otherwise they override processing instructions
        MasonTagTypes.register();

        // 获取线程池
        ExecutorService es = Executors.newFixedThreadPool(1);

        // Future用于执行多线程的执行结果
        Future<String> future = es.submit(() -> {
            try {
                Source source = new Source(new URL(url));
                return source.toString();
            } catch (Exception e) {
                System.out.println("获取网页失败:" + url);
//            e.printStackTrace();
                return "";

            }
        });

        try {
            // futrue.get()测试被执行的程序是否能在timeOut时限内返回字符串
            return future.get(5, TimeUnit.SECONDS);//任务处理超时时间设为 3 秒
        } catch (Exception ex) {
            System.out.println("获取网页超时:" + url);
            return "";
        } finally {
            // 关闭线程池
            es.shutdown();
        }
    }

    public static String httpsGet2(String url) {
//        MicrosoftConditionalCommentTagTypes.register();
//        PHPTagTypes.register();
//        PHPTagTypes.PHP_SHORT.deregister(); // remove PHP short tags for this example otherwise they override processing instructions
//        MasonTagTypes.register();

        // 获取线程池
        ExecutorService es = Executors.newFixedThreadPool(1);

        // Future用于执行多线程的执行结果
        Future<String> future = es.submit(() -> {
            try {
                //1.生成httpclient，相当于该打开一个浏览器
                CloseableHttpClient httpClient = HttpClients.createDefault();
                CloseableHttpResponse response = null;
                //2.创建get请求，相当于在浏览器地址栏输入 网址
                HttpGet request = new HttpGet(url);
                //设置请求头，将爬虫伪装成浏览器
                request.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36");

                //3.执行get请求，相当于在输入地址栏后敲回车键
                response = httpClient.execute(request);

                //4.判断响应状态为200，进行处理
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    //5.获取响应内容
                    HttpEntity httpEntity = response.getEntity();
                    String html = EntityUtils.toString(httpEntity, "utf-8");
                    System.out.println("获取网页成功:" + url);
                    return html;

                } else {
                    //如果返回状态不是200，比如404（页面不存在）等，根据情况做处理，这里略
                    System.out.println("获取网页失败:" + url);
                    return "";
                }
            } catch (Exception e) {
                System.out.println("获取网页失败:" + url);
//            e.printStackTrace();
                return "";

            }
        });

        try {
            // futrue.get()测试被执行的程序是否能在timeOut时限内返回字符串
            return future.get(5, TimeUnit.SECONDS);//任务处理超时时间设为 3 秒
        } catch (Exception ex) {
            System.out.println("获取网页超时:" + url);
            return "";
        } finally {
            // 关闭线程池
            es.shutdown();
        }
    }


    /**
     * 获得html内容
     *
     * @param htmlurl
     * @return
     */
    public static String[] getHtmlContentText(String htmlurl) throws UnsupportedEncodingException {
        String htmlContent = getHtmlContent(htmlurl);
        String domainUrl = getDomainName(htmlurl);
        // 标题内容
        String title = HtmlUtil.getTitle(htmlContent);
        String webNameContent = getHtmlContent(domainUrl);
        String webName = getTitle(webNameContent);
        webName = delHTMLTag(webName);
        if(webName == null || "".equals(webName)) {
            webName = getHostName(htmlurl);
        }


//        String content = delHTMLTag(htmlContent);
//        String content = getPTagContent(htmlContent);
//        if(content == null || "".equals(content) || content.length() < 10) {
//            content = delHTMLTag(htmlContent);
//        }
        String content = HtmlUtil.getArticleContent(htmlContent);
        content = delHTMLTag(content);
        return new String[]{webName, title, content};
    }

//    /**
//     * 获得html内容
//     *
//     * @param htmlurl
//     * @return
//     */
//    public static String[] getHtmlContentText(String htmlurl) throws UnsupportedEncodingException {
//        String htmlContent = Constant.LANG_NAME.equals("中简") ? httpsGet1(htmlurl) :  httpsGet2(htmlurl);
//        String domainUrl = getDomainName(htmlurl);
//        String htmlTitle = Constant.LANG_NAME.equals("中简") ? httpsGet1(domainUrl) :  httpsGet2(domainUrl);
//        String webName = getTitle(htmlTitle);
//        String title = getTitle(htmlContent);
//        String content = delHTMLTag(htmlContent);
//        return new String[]{webName, title, content};
//    }

//    /**
//     * 获得html内容
//     *
//     * @param htmlurl
//     * @return
//     */
//    public static String[] getHtmlContentText(String htmlurl) throws UnsupportedEncodingException {
//        String htmlContent = getHtmlContent(htmlurl);
//        String domainUrl = getDomainName(htmlurl);
//        String htmlTitle = getHtmlContent(domainUrl);
//        String webName = getTitle(htmlTitle);
//        String title = getTitle(htmlContent);
//        String content = getPTagContent(htmlContent);
//        return new String[]{webName, title, content};
//    }


    /**
     * @param htmlStr
     * @return 删除Html标签
     * @author LongJin
     */
    public static String delHTMLTag(String htmlStr) {
        if(htmlStr == null) {
            return null;
        }
        Pattern p_w = Pattern.compile(regEx_w, Pattern.CASE_INSENSITIVE);
        Matcher m_w = p_w.matcher(htmlStr);
        htmlStr = m_w.replaceAll(" "); // 过滤script标签


        Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
        Matcher m_script = p_script.matcher(htmlStr);
        htmlStr = m_script.replaceAll(" "); // 过滤script标签


        Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
        Matcher m_style = p_style.matcher(htmlStr);
        htmlStr = m_style.replaceAll(" "); // 过滤style标签


        Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(htmlStr);
        htmlStr = m_html.replaceAll(" "); // 过滤html标签


        Pattern p_space = Pattern.compile(regEx_space, Pattern.CASE_INSENSITIVE);
        Matcher m_space = p_space.matcher(htmlStr);
        htmlStr = m_space.replaceAll(""); // 过滤空格回车标签

        htmlStr = htmlStr.replaceAll("\r\n", "");

        return deleteSpecialWord(htmlStr).trim();
    }

    public static String deleteSpecialWord(String htmlStr) {
        htmlStr = htmlStr.replace("&nbsp;", "");
        htmlStr = htmlStr.replace("&amp;", "");
        htmlStr = htmlStr.replace("&quot;", "");
        htmlStr = htmlStr.replace("&lt;", "");
        htmlStr = htmlStr.replace("&gt;", "");
        htmlStr = htmlStr.replace("&yen;", "");
        htmlStr = htmlStr.replace("&#xff0c;", "");
        htmlStr = htmlStr.replace("&#xff08;", "");
        htmlStr = htmlStr.replace("&#xff09;", "");
        htmlStr = htmlStr.replace("&#xff1a;", "");
        htmlStr = htmlStr.replace("&#xff1b;", "");
        htmlStr = htmlStr.replace("&#xe619;", "");
        htmlStr = htmlStr.replace("&#xe681;", "");
        htmlStr = htmlStr.replace("&rdquo;", "");
        htmlStr = htmlStr.replace("&times;", "");
        htmlStr = htmlStr.replace("&copy;", "");
        htmlStr = htmlStr.replace("&ldquo;", "");
        htmlStr = htmlStr.replace("&mdash;", "");
        htmlStr = htmlStr.replace("&ensp;", "");
        htmlStr = htmlStr.replace("&#x1007f;", "");
        htmlStr = htmlStr.replace("&#x10080;", "");
        htmlStr = htmlStr.replace("&#x10081;", "");
        htmlStr = htmlStr.replace("&#x10082;", "");
        htmlStr = htmlStr.replace("&#x10083;", "");
        htmlStr = htmlStr.replace("&#x10084;", "");
        htmlStr = htmlStr.replace("&#x10085;", "");
        htmlStr = htmlStr.replace("&#x10086;", "");
        htmlStr = htmlStr.replace("&#x10087;", "");
        htmlStr = htmlStr.replace("&#x10088;", "");
        htmlStr = htmlStr.replace("&raquo;", "");
        htmlStr = htmlStr.replace("&#8211;", "");
        htmlStr = htmlStr.replace("&#43;", "");
        return htmlStr;
    }

    public static String getWebName(String url) {
        String domainName = getDomainName(url);
        if ("".equals(domainName)) {
            return "";
        }

        String result = Constant.WEB_NAME_MAP.get(domainName);
        if (result != null && !"".equals(result)) {
            return result;
        }
        String htmlTitle = getHtmlTitle(domainName);
        Constant.WEB_NAME_MAP.put(domainName, htmlTitle);
        return htmlTitle;
    }

    private static String getHtmlTitle(String htmlUrl) {
        String htmlSource = "";
        htmlSource = getHtmlSource(htmlUrl);//获取htmlUrl网址网页的源码
        String title = getTitle(htmlSource);
        return title;
    }

    /**
     * 根据网址返回网页的源码
     *
     * @param htmlUrl
     * @return
     */
    private static String getHtmlSource(String htmlUrl) {
        URL url;
        StringBuffer sb = new StringBuffer();
        try {
            url = new URL(htmlUrl);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));//读取网页全部内容
            String temp;
            while ((temp = in.readLine()) != null) {
                sb.append(temp);
            }
            in.close();
        } catch (MalformedURLException e) {
            System.out.println("你输入的URL格式有问题！请仔细输入");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * 从html源码(字符串)中去掉标题
     *
     * @param htmlSource
     * @return
     */
    private static String getTitle(String htmlSource) {
        List<String> list = new ArrayList<String>();
        String title = "";

        //Pattern pa = Pattern.compile("<title>.*?</title>", Pattern.CANON_EQ);也可以
        Pattern pa = Pattern.compile("<title>.*?</title>");//源码中标题正则表达式
        Matcher ma = pa.matcher(htmlSource);
        while (ma.find())//寻找符合el的字串
        {
            list.add(ma.group());//将符合el的字串加入到list中
        }
        for (int i = 0; i < list.size(); i++) {
            title = title + list.get(i);
        }
        return outTag(title);
    }

    /**
     * @param s
     * @return 去掉标记
     */
    private static String outTag(final String s) {
        return s.replaceAll("<.*?>", " ");
    }

    private static String getDomainName(String url) {
        String host = "";
        try {
            URL Url = new URL(url);
            host = Url.getProtocol() + "://" + Url.getHost();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return host;
    }


    private static String getHostName(String url) {
        String host = "";
        try {
            URL Url = new URL(url);
            host = Url.getHost();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return host;
    }



    /**
     * 获得p标签内容
     *
     * @param htmlContent
     * @return
     */
    private static String getPTagContent(String htmlContent) {
        StringBuilder sb = new StringBuilder();
        Matcher m = Pattern.compile("<p.{11,}(!<p>)*</p>").matcher(htmlContent);
        while (m.find()) {
            String filepath = m.group(0);
//            sb.append(filepath).append("\n");
            sb.append(filepath);
        }
        return delHTMLTag(sb.toString());
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
//        System.out.println(getWebName("https://blog.csdn.net/ziele_008/article/details/106275703"));
//        String htmlSource = httpsGet1("https://www.mysteel.com/hot/67426.html");
//        Redirecting to http://www.bilibili.com/read/mobile?id=14151535 .
//        String htmlurl = "http://m.zys.com.cn/news/202110261745311949.html";
        String[] content = getHtmlContentText("http://www.xinhuanet.com/techpro/");
        System.out.println(content);
//        System.out.println("bilibili的报Redirecting to http://www.bilibili.com/read/mobile?id=14151535 .".length());

//        String content = httpsGet1(htmlurl);
//        String pContent = getPTagContent(content);
//        System.out.println(pContent);
//        System.out.println(content);

//        System.out.println(getPTagContent("<div class=\"js_qrcode_wrap hidden\" id=\"js_qrcode_top\">\n" +
//                "                            <div class=\"js_qrcode_arr\"></div>\n" +
//                "                            <a href=\"javascript:;\" target=\"_self\" class=\"js_qrcode_close\" title=\"关闭\"></a>\n" +
//                "                            <div class=\"js_qrcode_img js_share_qrcode\"></div>\n" +
//                "                            <p>用微信扫码二维码</p>\n" +
//                "                            <p>分享至好友和朋友圈</p>\n" +
//                "                        </div>"));
    }
//    public static void main(String[] args) throws UnsupportedEncodingException {
//        String htmlurl = "https://www.cnbeta.com/articles/science/1192645.htm";
//        String htmlContentText = getHtmlContentText(htmlurl);
//        System.out.println(htmlContentText);
//    }

}
