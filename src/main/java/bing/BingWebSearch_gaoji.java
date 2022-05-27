package bing;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import conf.Constant;
import handler.*;
import input.Input;
import org.apache.commons.lang3.StringUtils;
import util.HttpUtil;
import util.StringUtil;

import javax.net.ssl.HttpsURLConnection;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;
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
        System.out.println("构造好的URL为："+url);
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
    public List<Topic> search_oneSite(boolean isNews,String site, Input input) throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
//        ExecutorService executorService = Executors.newSingleThreadExecutor();
        String langName = input.getLanguage();
//        List<Future<Topic>> finalTopicList = new ArrayList<>();
//        String site = StringUtils.join(siteArr," OR site:");  // 所有的定向网址进行组合
        List<Topic> topicList = new ArrayList<>();
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
//            String keywords = input.getName();
            List<Topic> tempList = search(isNews, keywords, site, Constant.BING_TIME, Constant.PAGE_SIZE, offset);
//            List<Topic> tempList = search(isNews, langName, site,keywords, Constant.BING_TIME, Constant.PAGE_SIZE, offset);
//            System.out.println(tempList.size());

/*            for (Topic t:tempList) {
                System.out.println(t.getLink());
            }*/

            CountDownLatch countDownLatch = new CountDownLatch(tempList.size()); //建立倒计时

            if (tempList == null || tempList.size() == 0) {
                break;
            }

            List<Future> finalTopicList = new ArrayList<>();  // 用做顺序处理

            for (Topic topic : tempList) {
//                Topic topic = futureTopic.get();
//                System.out.println("[" + langName +"] 循环刚开始的顺序：Topic的URL："+topic.getLink());
                if (topic.getLink().contains(".pdf") || topic.getLink().contains(".PDF")) {
                    continue;
                }
                //拿到网页的内容。 这里应该有个先后顺序，也就是拿完网页内容后按照原有顺序再排列。
//                List<Future<Topic>> finalTopicList = topicList;
//                List<Future<Topic>> finalTopicList = new ArrayList<>();
                Future<Topic> oneTopicresult= executorService.submit(new Callable<Topic>() {
                    @Override
                    public Topic call() {
                        try {
//                            System.out.println(Thread.currentThread().getName()+"---"+topic.getUrl()+"开始");
                            String[] titleContentArr = HttpUtil.getHtmlContentText(topic.getLink());
//                            System.out.println(Thread.currentThread().getName()+"---"+topic.getUrl()+"结束");
                            // 设置标题
                            topic.setSource(titleContentArr[0]);
                            topic.setTitle(titleContentArr[1]);
                            topic.setContent(titleContentArr[2]);
                            topic.setLang(langName);

                            // 内容为空，过滤
                            if (null == topic.getContent() || "".equals(topic.getContent())) {
//                            continue;
                            }

                            // 时间处理
                            if (topic.getCreateTime() != null) {
                                try {
                                    topic.setCreateTime(topic.getCreateTime().substring(0, topic.getCreateTime().indexOf(".")).replace("T", " "));
                                } catch (Exception e) {

                                }
                            }

                        } catch (UnsupportedEncodingException e) {
//                            System.out.println("||||||");
                            e.printStackTrace();
                        }
                        countDownLatch.countDown();
                        return topic;
                    }
                });
                finalTopicList.add(oneTopicresult);
            }

            countDownLatch.await();  // 等待
            for (Future<Topic> finaltopic: finalTopicList) {  // 将顺序转换回来
//                System.out.println(finaltopic.get().getLang()+"-多线程处理后输出+"+finaltopic.get().getLink());
                topicList.add(finaltopic.get());
            }
        }
        executorService.shutdown(); // 关闭线程池



        List<Topic> resultList = new ArrayList<>();
//        System.out.println("==============强过滤：[" + langName +"] "+input.getName() + "=================符合" + resultList.size() + "条");

        // 处理器
        HandlerService handlerService = null;
        if ("中简".equals(langName)) {
            handlerService = new ZhHandler();
        } else if ("英语".equals(langName)) {
            handlerService = new EnHandler();
        } else if ("法语".equals(langName)) {
            handlerService = new FrHandler();

        } else if ("俄语".equals(langName)) {
            handlerService = new RuHandler();

        } else if ("德语".equals(langName)) {
            handlerService = new GeHandler();

        } else if ("韩语".equals(langName)) {
            handlerService = new KrHandler();

        } else if ("日语".equals(langName)) {
            handlerService = new JpHandler();

        }
        if (handlerService != null) {
            handlerService.handle(topicList);
        }


        // 把其他的也加上去。按指定集合的Iterator返回的顺序将指定集合中的所有元素追加到此列表的末尾。
        resultList.addAll(topicList);

        resultList = resultList.stream().filter(p -> p != null && p.getContent() != null && !"".equals(p.getContent())).collect(Collectors.toList());

        // 根据URL去重。 这儿变成无序了
//        resultList = resultList.stream().collect(collectingAndThen(toCollection(() -> new TreeSet<>(Comparator.comparing(Topic::getLink))), ArrayList::new));
        ArrayList<Topic> finalResList = new ArrayList<>();
        for (Topic topic : resultList) {
            String urlTemp = topic.getLink();
            boolean flag = true; //默认是不重复
            for (Topic t:finalResList) {
                if (urlTemp.equals(t.getLink())){
                    flag = false;
                    break;
                }
            }
            if (flag){
                finalResList.add(topic);
            }
        }

        return resultList;
    }


    public List<Topic> search_sites(boolean isNews,ArrayList<String> siteArr, Input input) throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
//        ExecutorService executorService = Executors.newSingleThreadExecutor();
        String langName = input.getLanguage();
//        List<Future<Topic>> finalTopicList = new ArrayList<>();
        String site = StringUtils.join(siteArr," OR site:");  // 所有的定向网址进行组合
        List<Topic> topicList = new ArrayList<>();
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
//            String keywords = input.getName();
            List<Topic> tempList = search(isNews, keywords, site, Constant.BING_TIME, Constant.PAGE_SIZE, offset);
//            List<Topic> tempList = search(isNews, langName, site,keywords, Constant.BING_TIME, Constant.PAGE_SIZE, offset);
//            System.out.println(tempList.size());

/*            for (Topic t:tempList) {
                System.out.println(t.getLink());
            }*/

            CountDownLatch countDownLatch = new CountDownLatch(tempList.size()); //建立倒计时

            if (tempList == null || tempList.size() == 0) {
                break;
            }

            List<Future> finalTopicList = new ArrayList<>();  // 用做顺序处理

            for (Topic topic : tempList) {
//                Topic topic = futureTopic.get();
//                System.out.println("[" + langName +"] 循环刚开始的顺序：Topic的URL："+topic.getLink());
                if (topic.getLink().contains(".pdf") || topic.getLink().contains(".PDF")) {
                    continue;
                }
                //拿到网页的内容。 这里应该有个先后顺序，也就是拿完网页内容后按照原有顺序再排列。
//                List<Future<Topic>> finalTopicList = topicList;
//                List<Future<Topic>> finalTopicList = new ArrayList<>();
                Future<Topic> oneTopicresult= executorService.submit(new Callable<Topic>() {
                    @Override
                    public Topic call() {
                        try {
//                            System.out.println(Thread.currentThread().getName()+"---"+topic.getUrl()+"开始");
                            String[] titleContentArr = HttpUtil.getHtmlContentText(topic.getLink());
//                            System.out.println(Thread.currentThread().getName()+"---"+topic.getUrl()+"结束");
                            // 设置标题
                            topic.setSource(titleContentArr[0]);
                            topic.setTitle(titleContentArr[1]);
                            topic.setContent(titleContentArr[2]);
                            topic.setLang(langName);

                            // 内容为空，过滤
                            if (null == topic.getContent() || "".equals(topic.getContent())) {
//                            continue;
                            }

                            // 时间处理
                            if (topic.getCreateTime() != null) {
                                try {
                                    topic.setCreateTime(topic.getCreateTime().substring(0, topic.getCreateTime().indexOf(".")).replace("T", " "));
                                } catch (Exception e) {

                                }
                            }

                        } catch (UnsupportedEncodingException e) {
//                            System.out.println("||||||");
                            e.printStackTrace();
                        }
                        countDownLatch.countDown();
                        return topic;
                    }
                });
                finalTopicList.add(oneTopicresult);
            }

            countDownLatch.await();  // 等待
            for (Future<Topic> finaltopic: finalTopicList) {  // 将顺序转换回来
//                System.out.println(finaltopic.get().getLang()+"-多线程处理后输出+"+finaltopic.get().getLink());
                topicList.add(finaltopic.get());
            }
        }
        executorService.shutdown(); // 关闭线程池



        List<Topic> resultList = new ArrayList<>();
//        System.out.println("==============强过滤：[" + langName +"] "+input.getName() + "=================符合" + resultList.size() + "条");

        // 处理器
        HandlerService handlerService = null;
        if ("中简".equals(langName)) {
            handlerService = new ZhHandler();
        } else if ("英语".equals(langName)) {
            handlerService = new EnHandler();
        } else if ("法语".equals(langName)) {
            handlerService = new FrHandler();

        } else if ("俄语".equals(langName)) {
            handlerService = new RuHandler();

        } else if ("德语".equals(langName)) {
            handlerService = new GeHandler();

        } else if ("韩语".equals(langName)) {
            handlerService = new KrHandler();

        } else if ("日语".equals(langName)) {
            handlerService = new JpHandler();

        }
        if (handlerService != null) {
            handlerService.handle(topicList);
        }


        // 把其他的也加上去。按指定集合的Iterator返回的顺序将指定集合中的所有元素追加到此列表的末尾。
        resultList.addAll(topicList);

        resultList = resultList.stream().filter(p -> p != null && p.getContent() != null && !"".equals(p.getContent())).collect(Collectors.toList());

        // 根据URL去重。 这儿变成无序了
//        resultList = resultList.stream().collect(collectingAndThen(toCollection(() -> new TreeSet<>(Comparator.comparing(Topic::getLink))), ArrayList::new));
        ArrayList<Topic> finalResList = new ArrayList<>();
        for (Topic topic : resultList) {
            String urlTemp = topic.getLink();
            boolean flag = true; //默认是不重复
            for (Topic t:finalResList) {
                if (urlTemp.equals(t.getLink())){
                    flag = false;
                    break;
                }
            }
            if (flag){
                finalResList.add(topic);
            }
        }

        return resultList;
    }
}
