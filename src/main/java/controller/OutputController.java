package controller;

import bing.BingWebSearch_gaoji;
import bing.Topic;
import conf.Constant;
import conf.InitConfService;
import dao.OutputDao;
import input.Input;
import input.InputService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import output.Output;
import output.Result;
import output.ResultCodeEnum;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * @author Meng Ling'en
 * @create 2022-03-21-10:47
 **/
@RestController
public class OutputController {

    @ResponseBody
    @PostMapping("/post/test")
    public Map bookpost(@RequestBody String content,@RequestParam String bookname,@RequestParam String author){
        Map<String,Object> map = new HashMap<>();
        map.put("content",content);
        map.put("bookname",bookname);
        map.put("author",author);
        return  map;
    }

//  传入书籍的四个参数：书名、作者、出版社，要检索的语言。
    @ResponseBody
    @PostMapping(value = "/book/socialInfluenceList")
    public Result getBookInfluence(@RequestParam String bookname,@RequestParam String author,@RequestParam String publication,@RequestParam String lan){
//        InputService inputService = new InputService();
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        InitConfService initConfService = new InitConfService();
        Result finalResult = null;
//        String website = "https://www.cbsnews.com/";
//        Input input1 = new Input("神聖幾何学マルセイユ タロット ミレニアムエディション入門書","ウィルフリード・ウドワン","サンジェルマン出版","日本","en");
//        Input input1 = new Input("Where the Crawdads Sing","Delia Owens","G.P. Putnam's Sons","美国","en");
        Input input = new Input(bookname,author,publication,"zhanweizhuanyong",lan);
        try {
//          1. 把resources/website/下的所有的语言文件读入进来。
            System.out.println("开始读入初始化配置 ··· ");
            initConfService.initConf();
            System.out.println("初始化配置完成");

/*          输出：ko  jp  en  am  zh
            for (String t:Constant.WEBSITE_MAP.keySet()) {
                System.out.println(t);
            }
*/
            ArrayList<String> targetSite = new ArrayList<>();
            List<Topic> wangyeList = new ArrayList<>();
            if (Constant.WEBSITE_MAP.keySet().contains(lan)){
                targetSite = Constant.WEBSITE_MAP.get(lan);  // 拿到对应文件下的网址集合
                Constant.LANG_NAME = lan;
                CountDownLatch countDownLatch = new CountDownLatch(targetSite.size());  // 建立倒计时
                for (String website : targetSite) {
                    List<Topic> finalWangyeList = wangyeList;
                    executorService.submit(new Runnable() {
                        @Override
                        public void run() {
                            List<Topic> tempTopicList = new ArrayList<>();
                            try {
                                BingWebSearch_gaoji bingWebSearch = new BingWebSearch_gaoji();
                                tempTopicList = bingWebSearch.search(false, website,input);
                                System.out.println("网址循环线程名称："+website+"||"+Thread.currentThread().getName()+"取回的内容条数是:"+tempTopicList.size());
                               /* for (Topic topic : tempTopicList) {
                                    System.out.println("topic  : " +topic.getTitle()+"|"+topic.getContent()+"|"+topic.getUrl());
                                }*/
                                /*synchronized (this){
                                    for (Topic topic:tempTopicList) {
                                        wangyeList.add(topic);
                                    }
                                }*/
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            synchronized (this){
                                for (Topic topic:tempTopicList) {
                                    finalWangyeList.add(topic);
                                }
                            }
                            countDownLatch.countDown(); //减一次
                        }
                    });
                }
                countDownLatch.await(); //等待执行完毕
                executorService.shutdown(); // 关闭线程池
                int total = wangyeList.size();
                finalResult=Result.success(ResultCodeEnum.SUCCESS,wangyeList,total);
            }else {  // 输入的语言是不对的。那么直接返回空数据，不进行任何搜索
                wangyeList= null;
                finalResult=Result.success(ResultCodeEnum.SUCCESS,wangyeList,0);
                executorService.shutdown();
            }
        } catch (Exception e) {
            e.printStackTrace();
            finalResult = Result.fail();
            executorService.shutdown();
        }
        System.out.println("============本次处理完成============");
        return finalResult;
    }

    //    @Autowired OutputDao dao;
    @ResponseBody
    @RequestMapping(value = "/search_v1/{bookname}/{author}/{publication}/{lan}")
    public Result getResult(@PathVariable("bookname")String bookname,@PathVariable("author")String author,@PathVariable("publication")String publication,@PathVariable("lan")String lan){
//        InputService inputService = new InputService();
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        InitConfService initConfService = new InitConfService();
        Result finalResult = null;
//        String website = "https://www.cbsnews.com/";
//        Input input1 = new Input("神聖幾何学マルセイユ タロット ミレニアムエディション入門書","ウィルフリード・ウドワン","サンジェルマン出版","日本","en");
//        Input input1 = new Input("Where the Crawdads Sing","Delia Owens","G.P. Putnam's Sons","美国","en");
        Input input = new Input(bookname,author,publication,"zhanweizhuanyong",lan);
        try {
//          1. 把resources/website/下的所有的语言文件读入进来。
            System.out.println("开始读入初始化配置 ··· ");
            initConfService.initConf();
            System.out.println("初始化配置完成");

            ArrayList<String> targetSite = new ArrayList<>();
            List<Topic> wangyeList = new ArrayList<>();
            if (Constant.WEBSITE_MAP.keySet().contains(lan)){
                targetSite = Constant.WEBSITE_MAP.get(lan);
                Constant.LANG_NAME = lan;
                CountDownLatch countDownLatch = new CountDownLatch(targetSite.size());  // 建立倒计时
                for (String website : targetSite) {
                    List<Topic> finalWangyeList = wangyeList;
                    executorService.submit(new Runnable() {
                        @Override
                        public void run() {
                            List<Topic> tempTopicList = new ArrayList<>();
                            try {
                                BingWebSearch_gaoji bingWebSearch = new BingWebSearch_gaoji();
                                tempTopicList = bingWebSearch.search(false, website,input);
                                System.out.println("网址循环线程名称："+website+"||"+Thread.currentThread().getName()+"取回的内容条数是:"+tempTopicList.size());
                               /* for (Topic topic : tempTopicList) {
                                    System.out.println("topic  : " +topic.getTitle()+"|"+topic.getContent()+"|"+topic.getUrl());
                                }*/
                                /*synchronized (this){
                                    for (Topic topic:tempTopicList) {
                                        wangyeList.add(topic);
                                    }
                                }*/
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            synchronized (this){
                                for (Topic topic:tempTopicList) {
                                    finalWangyeList.add(topic);
                                }
                            }
                            countDownLatch.countDown(); //减一次
                        }
                    });
                }
                countDownLatch.await(); //等待执行完毕
                executorService.shutdown(); // 关闭线程池
                int total = wangyeList.size();
                finalResult=Result.success(ResultCodeEnum.SUCCESS,wangyeList,total);
            }else {  // 返回空，不进行任何搜索
                wangyeList= null;
                finalResult=Result.success(ResultCodeEnum.SUCCESS,wangyeList,0);
                executorService.shutdown();
            }
        } catch (Exception e) {
            e.printStackTrace();
            finalResult = Result.fail();
            executorService.shutdown();
        }
        System.out.println("============本次处理完成============");
        return finalResult;
    }


    @ResponseBody
    @RequestMapping(value = "/testgj/{lan}/{keyword}/{site}")
    public Result getResultFromSite(@PathVariable("lan")String lan, @PathVariable("keyword")String keyword, @PathVariable("site")String site, Model model) {
        ExecutorService threadPool = Executors.newFixedThreadPool(10);
        InputService inputService = new InputService();
        InitConfService initConfService = new InitConfService();

        // 初始化配置
        System.out.println("开始初始化配置......");
        Result finalResult = null;
        try {
//            读入初始化配置
            initConfService.initConf();
            //            # 语言(只能写一种语言)：中简、英语、法语、俄语、德语、韩语、日语
            Constant.LANG_NAME = lan;
            // 构造一个输入用例测试输出
            Input input1 = new Input();
//            input1.setNum("5");
//            input1.setName(keyword);

            BingWebSearch_gaoji bingWebSearch = new BingWebSearch_gaoji();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            System.out.println(sdf.format(new Date()) + ">>>============================= " + input1.getName() + " start =============================== ");
            List<Topic> wangyeList = bingWebSearch.search(false, site,input1);
//            System.out.println(sdf.format(new Date()) + "<<<============================= " + input1.getName() + " end =============================== ");

            int total = wangyeList.size();  // 一共搜索到的条数
            finalResult = Result.success(ResultCodeEnum.SUCCESS,wangyeList,total);

        } catch (Exception e) {
            e.printStackTrace();
            finalResult = Result.fail();
        }
//        System.out.println("初始化配置完成");
        return finalResult;
    }

//    输入语言名称，根据语言进行检索
    @ResponseBody
    @RequestMapping(value = "/knowledge/analysis/institution/technicalList/{lan}",method = RequestMethod.GET)
    public List<Output> fromLanGetOutput(@PathVariable("lan")String lan, Model model) throws SQLException {
        OutputDao outputDao = new OutputDao();
        List<Output> resultList = outputDao.queryByLan(lan);
//        Result successResult = Result.success(resultList);
//        if (resultList.)
        model.addAttribute("successResult",resultList);
        return resultList;
    }

    @ResponseBody
    @RequestMapping(value = "/test/{id}/{lan}/{keyword}",method = RequestMethod.GET)
    public Result getOutputByRest(@PathVariable("id")String id,@PathVariable("lan")String lan,@PathVariable("keyword")String keyword,Model model)  {
        ExecutorService threadPool = Executors.newFixedThreadPool(10);
        InputService inputService = new InputService();
        InitConfService initConfService = new InitConfService();

        // 初始化配置
        System.out.println("开始初始化配置......");
        Result finalResult = null;
        try {
//            读入初始化配置
            initConfService.initConf();
            //            # 语言(只能写一种语言)：中简、英语、法语、俄语、德语、韩语、日语
            Constant.LANG_NAME = lan;
            // 构造一个输入用例测试输出
            Input input1 = new Input();
//            input1.setNum("5");
/*
            input1.setName(keyword);

            BingWebSearch bingWebSearch = new BingWebSearch();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            System.out.println(sdf.format(new Date()) + ">>>============================= " + input1.getName() + " start =============================== ");
            List<Topic> wangyeList = bingWebSearch.search(false, input1);
            System.out.println(sdf.format(new Date()) + "<<<============================= " + input1.getName() + " end =============================== ");
*/

/*
            int total = wangyeList.size();  // 一共搜索到的条数
            finalResult = Result.success(ResultCodeEnum.SUCCESS,wangyeList,total);
*/

        } catch (Exception e) {
            e.printStackTrace();
            finalResult = Result.fail();
        }
//        System.out.println("初始化配置完成");
        return finalResult;
    }

}
