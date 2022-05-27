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
        final Result[] finalResult = {null};
//        String website = "https://www.cbsnews.com/";
//        Input input1 = new Input("神聖幾何学マルセイユ タロット ミレニアムエディション入門書","ウィルフリード・ウドワン","サンジェルマン出版","日本","en");
//        Input input1 = new Input("Where the Crawdads Sing","Delia Owens","G.P. Putnam's Sons","美国","en");
        Input input = new Input(bookname,author,publication,"-",lan);
        try {
//          1. 把resources/website/下的所有的语言文件读入进来。
            System.out.println("开始读入初始化配置 ··· ");
            initConfService.initConf();
            System.out.println("初始化配置完成");

//          输出：ko  jp  en  am  zh
/*            for (String t:Constant.WEBSITE_MAP_SECOND.keySet()) {
                System.out.println(t+"|"+Constant.WEBSITE_MAP_SECOND.get(t));
            }*/
            ArrayList<String> targetSite = new ArrayList<>();
            List<Topic> wangyeList = new ArrayList<>();
            if (Constant.WEBSITE_MAP.keySet().contains(lan)){
                targetSite = Constant.WEBSITE_MAP.get(lan);  // 拿到对应文件下的网址集合
                Constant.LANG_NAME = lan;
                CountDownLatch countDownLatch = new CountDownLatch(targetSite.size());  // 建立倒计时

/*
                BingWebSearch_gaoji bingWebSearch = new BingWebSearch_gaoji();
                wangyeList = bingWebSearch.search_sites(false, targetSite,input);
                System.out.println("网址循环线程名称"+Thread.currentThread().getName()+"取回的内容条数是:"+wangyeList.size());
*/
                List<Topic> finalWangyeList = wangyeList;
                final boolean[] flag_1 = {true};
                for (String site:targetSite) {
                    executorService.submit(new Runnable() {
                        @Override
                        public void run() {
                            List<Topic> tempTopicList = new ArrayList<>();
                            try {
                                BingWebSearch_gaoji bingWebSearch = new BingWebSearch_gaoji();
                                tempTopicList = bingWebSearch.search_oneSite(false, site,input);
                                System.out.println("网址循环线程名称"+Thread.currentThread().getName()+"取回的内容条数是:"+tempTopicList.size());
                            } catch (Exception e) {
                                flag_1[0] = false;
                                e.printStackTrace();
                                finalResult[0] =  Result.fail("bing搜索程序出现问题");
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

                if (flag_1[0] && wangyeList.size()>0) {  // bing搜索没有抛出异常,并且拿到了结果。
//                    System.out.println("----bing搜索没有抛出异常,并且拿到了结果----");
                    int total = wangyeList.size();
                    finalResult[0] =Result.success(ResultCodeEnum.SUCCESS,wangyeList,total);
                }
                if (wangyeList.size()<=0) { //反正就是没有拿到结果。去第二层寻找。
                    System.out.println("======================进入社评搜索界面=================");
                    targetSite = Constant.WEBSITE_MAP_SECOND.get(lan);
                    CountDownLatch countDownLatch2 = new CountDownLatch(targetSite.size());  // 建立倒计时
                    final boolean[] flag_2 = {true};
                    for (String site:targetSite) {
                        executorService.submit(new Runnable() {
                            @Override
                            public void run() {
                                List<Topic> tempTopicList = new ArrayList<>();
                                try {
                                    BingWebSearch_gaoji bingWebSearch = new BingWebSearch_gaoji();
                                    tempTopicList = bingWebSearch.search_oneSite(false, site,input);
                                    System.out.println("网址循环线程名称"+Thread.currentThread().getName()+"取回的内容条数是:"+tempTopicList.size());
                                } catch (Exception e) {
                                    flag_2[0] = false;
                                    e.printStackTrace();
                                    finalResult[0] =  Result.fail("bing搜索程序出现问题");
                                }
                                synchronized (this){
                                    for (Topic topic:tempTopicList) {
                                        finalWangyeList.add(topic);
                                    }
                                }
                                countDownLatch2.countDown(); //减一次
                            }
                        });
                    }
                    countDownLatch2.await(); //等待执行完毕
                    if (flag_2[0]) {
                        int total = wangyeList.size();
                        finalResult[0] =Result.success(ResultCodeEnum.SUCCESS,wangyeList,total);
                    }
                }
                executorService.shutdown(); // 关闭线程池

            }else {  // 输入的语言是不对的。那么直接返回空数据，不进行任何搜索
                wangyeList= null;
//                finalResult[0] =Result.success(ResultCodeEnum.SUCCESS,wangyeList,0);
                finalResult[0] = Result.fail("输入语言两位代码错误");
                executorService.shutdown();
            }
        } catch (Exception e) {
            e.printStackTrace();
            finalResult[0] = Result.fail("获取结果失败");
            executorService.shutdown();
        }
        System.out.println("============本次处理完成============");
        return finalResult[0];
    }

}
