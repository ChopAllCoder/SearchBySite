package bing;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import conf.Constant;
import handler.*;
import input.Input;
import util.HttpUtil;

import javax.net.ssl.HttpsURLConnection;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * @author saysky
 * @date 2021/11/15 8:13 下午
 * 高级搜索，在p参数后加了更多参数 site 来限定搜索域名。
 */

public class BingWebSearch_gaoji {

    static String host = "https://api.bing.microsoft.com";
    static String wangye_path = "/v7.0/search";
    static String news_path = "/v7.0/news";

    /**
     * https://docs.microsoft.com/en-us/bing/search-apis/bing-web-search/reference/market-codes#country-codes
     * <p>
     * <p>
     * 日期参数 &freshness=2019-02-01..2019-05-30
     * 分页count=10&offset=0，或offert=10
     * 要将结果限制为单个日期，请将此参数设置为特定日期。例如，&freshness=2019-02-04
     *
     * @return
     * @throws Exception
     */

    private static SearchResults SearchWeb(
            boolean isNews,
            String keywords, String site, String time,
            Integer count, Integer offset) throws Exception {
//        System.out.println(keywords);
        String urlString = "?q=" +
                URLEncoder.encode(keywords, "UTF-8")
                +"+site:"+site
//                + "&mkt=" + mkt
//                + "&mkt=" + Constant.BING_MKT_CODE_MAP.get(Constant.LANG_NAME)
                + "&responseFilter=webpages"
//                + "&setLang=" + langName
//                + "&setLang=" + Constant.BING_SETLANG_CODE_MAP.get(Constant.LANG_NAME)
//                + "&freshness=" + time
//                "&category=" + "ScienceAndTechnology" +
                + "&count=" + count + "&offset=" + offset;
//        System.out.println("url："+urlString);
        // Construct the URL.
        URL url = new URL(host + (isNews ? news_path : wangye_path) + urlString);
//        System.out.println("构造好的URL为："+url);
//        URL url = new URL("https://api.bing.microsoft.com/v7.0/news/search?q="+keywords);

        // Open the connection.
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestProperty("Ocp-Apim-Subscription-Key", Constant.BING_KEY);

        // Receive the JSON response body.
        InputStream stream = connection.getInputStream();
        String response = new Scanner(stream).useDelimiter("\\A").next();

        // Construct the result object.
        SearchResults results = new SearchResults(new HashMap<String, String>(), response);

        // Extract Bing-related HTTP headers.
        Map<String, List<String>> headers = connection.getHeaderFields();
        for (String header : headers.keySet()) {
            if (header == null) {
                continue;      // may have null key
            }
            if (header.startsWith("BingAPIs-") || header.startsWith("X-MSEdge-")) {
                results.relevantHeaders.put(header, headers.get(header).get(0));
            }
        }
        stream.close();
        return results;
    }

    private static String prettify(String json_text) {
        JsonParser parser = new JsonParser();
        JsonObject json = parser.parse(json_text).getAsJsonObject();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(json);
    }


    /**
     * 搜索接口
     *
     * @param keywords
     * @param count
     * @param offset
     * @return
     * @throws Exception
     */
    private List<Topic> search(boolean isNews, String keywords, String site,
                               String time,
                               Integer count, Integer offset) throws Exception {
        // Confirm the subscriptionKey is valid.
        if (Constant.BING_KEY.length() != 32) {
            throw new Exception("请配置正确的必应订阅key!");
        }

        // Call the SearchWeb method and print the response.
        try {
            SearchResults result = SearchWeb(isNews, keywords, site, time, count, offset);
            String json = prettify(result.jsonResponse);
            ResultVO resultVO = JSON.parseObject(json, ResultVO.class);

            List<Topic> topicList = new ArrayList<>();

            if (!isNews) {
                if (resultVO.getWebPages() != null) {
                    List<WebPagesDetail> value = resultVO.getWebPages().getValue();
                    if (value != null && value.size() > 0) {
                        for (WebPagesDetail temp : value) {
                            Topic topic = new Topic(temp.getName(), temp.getSnippet(), temp.getUrl(), temp.getDateLastCrawled());
//                            topic.setType("网页");
                            topicList.add(topic);
                        }
                    }
                }
            }

            if (isNews) {
                if (resultVO.getValue() != null) {
                    List<NewsDetail> value = resultVO.getValue();
                    if (value != null && value.size() > 0) {
                        for (NewsDetail temp : value) {
                            Topic topic = new Topic(temp.getName(), temp.getDescription(), temp.getUrl(), temp.getDatePublished());
//                            topic.setType("新闻");
                            topicList.add(topic);
                        }
                    }
                }
            }
            return topicList;

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("调用Bing API 接口失败");
        }
    }

    /**
     * 搜索
     *
     * @param input
     * @return
     * @throws Exception
     */
    public List<Topic> search(boolean isNews,String site, Input input) throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        List<Topic> topicList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        System.out.println(sdf.format(new Date()) + ">>>============================= " );
        // Call the SearchWeb method and print the response.
        for (int i = 0; i < Constant.PAGE_NUM; i++) {
            int offset = i * Constant.PAGE_SIZE;
            String bookname = input.getBookname();
            if (bookname.equals("/")){ bookname ="";}
            String author = input.getAuthor();
            if (author.equals("/")){ author ="";}
            String publication = input.getPublication();
            if (publication.equals("/")){ publication ="";}

//            String lan = input.getLanguage();  // 要返回的语言
//            Constant.LANG_NAME = lan;
            String keywords = bookname + " "+ author + " "+ publication;// 用书名、作者、出版社构造搜索关键词

            System.out.println(sdf.format(new Date()) + ">>>=====调用BING搜索["+site+"]开始的时间========== " );
            List<Topic> tempList = search(isNews, keywords, site, Constant.BING_TIME, Constant.PAGE_SIZE, offset);
            System.out.println("BING搜索["+site+"]拿回来的搜索条数："+tempList.size());
            CountDownLatch countDownLatch = new CountDownLatch(tempList.size());
            System.out.println(sdf.format(new Date()) + ">>>=====调用BING搜索["+site+"]完成的时间========== " );

            if (tempList == null || tempList.size() == 0) {
                break;
            }

            for (Topic topic : tempList) {
//                链接如果是个PDF文件,不进行处理，进入下一次循环
                if (topic.getLink().contains(".pdf") || topic.getLink().contains(".PDF")) {
                    continue;
                }
                List<Topic> finalTopicList = topicList;
                executorService.submit(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            System.out.println(Thread.currentThread().getName()+"||"+sdf.format(new Date()) + ">>>======爬取["+site+"]一个链接内容开始的时间======== " );
                            String[] titleContentArr = HttpUtil.getHtmlContentText(topic.getLink());
                            System.out.println(Thread.currentThread().getName()+"||"+sdf.format(new Date()) + ">>>======爬取["+site+"]一个链接内容完成的时间======== " );
                            // 设置标题
                            topic.setSource(titleContentArr[0]);
                            topic.setTitle(titleContentArr[1]);
                            topic.setContent(titleContentArr[2]);

//                            topic.setLang(topic.);
                            // 内容为空，过滤
                            if (null == topic.getContent() || "".equals(topic.getContent())) {

                            }else {
                                // 时间处理
                                if (topic.getCreateTime() != null) {
                                    try {
                                        topic.setCreateTime(topic.getCreateTime().substring(0, topic.getCreateTime().indexOf(".")).replace("T", " "));
                                    } catch (Exception e) {

                                    }
                                }
                                finalTopicList.add(topic);
                                // 需要二次筛选.针对已经选定的各个语种的特定网页，筛选到最终的有信服力的书籍评论内容，写入到content中
/*                                if ("Y".equals(Constant.SECOND_FILTER)) {
                                    if (topic.getTitle() != null && topic.getTitle().toLowerCase().contains(keywords.toLowerCase())) {
                                        topicList.add(topic);
                                    } else if (topic.getSummary() != null && topic.getSummary().toLowerCase().contains(keywords.toLowerCase())) {
                                        topicList.add(topic);
                                    } else if (topic.getContent().toLowerCase().contains(keywords.toLowerCase())) {
                                        topicList.add(topic);
                                    }
                                } else {
                                    topicList.add(topic);
                                }*/
                            }

                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        countDownLatch.countDown();
                    }
                });

            }
            countDownLatch.await();  // 等待
        }
        executorService.shutdown(); // 关闭线程池

        List<Topic> resultList = new ArrayList<>();
/*        String url = input.getUrl();
        if (url != null && !"".equals(url)) {
            for (Topic topic : topicList) {
                if (topic.getUrl().contains(input.getUrl())) {
                    resultList.add(topic);
                }
            }
        }*/

        if (resultList.size() != 0) {
            return resultList;
        }
//        System.out.println("===强过滤：" + input.getName() + "===符合" + resultList.size() + "条");

        // 处理器
        HandlerService handlerService = null;
        System.out.println(Constant.LANG_NAME);
/*        if ("am".equals(Constant.LANG_NAME)) {
            handlerService = new ZhHandler();
        } else if ("en".equals(Constant.LANG_NAME)) {
            handlerService = new EnHandler();
        } else if ("ko".equals(Constant.LANG_NAME)) {
            handlerService = new FrHandler();
        } else if ("jp".equals(Constant.LANG_NAME)) {
            handlerService = new RuHandler();
        } else if ("zh".equals(Constant.LANG_NAME)) {
            handlerService = new GeHandler();
        } else if ("韩语".equals(Constant.LANG_NAME)) {
            handlerService = new KrHandler();
        } else if ("日语".equals(Constant.LANG_NAME)) {
            handlerService = new JpHandler();
        }
        if (handlerService != null) {
            handlerService.handle(topicList);
        }*/

        topicList = topicList.stream().filter(p -> p != null && p.getContent() != null).collect(Collectors.toList());

//        输出前三条
        /*
        if (topicList.size() > 3) {
            topicList = topicList.subList(0, 3);
        }
        */
        return topicList;
    }
}
