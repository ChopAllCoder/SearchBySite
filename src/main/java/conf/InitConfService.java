package conf;

import util.FileUtil;

import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author saysky
 * @date 2021/11/12
 *
 * 把resources/conf.txt 配置文件读进来，并写入conf/Constant类中。
 *
 */
public class InitConfService {

    public void initConf() throws Exception {

        // 根目录
//        String currentPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
//        File file = new File(currentPath);
//        Constant.ROOT_PATH = file.getParent() + File.separator + "data" + File.separator;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String now = sdf.format(new Date());

        // 创建输出目录
/*
        FileUtil.createDirectory(Constant.ROOT_PATH);
        FileUtil.createDirectory(Constant.ROOT_PATH + "result");
        FileUtil.createDirectory(Constant.ROOT_PATH + "result" + File.separator + now);
*/


        // 创建输出文件
//        Constant.INPUT_PATH = Thread.currentThread().getContextClassLoader().getResource("input.txt").getPath();

        // 解析配置
        try {
//            String configPath = Thread.currentThread().getContextClassLoader().getResource("conf.txt").getPath();
            InputStream configPath = this.getClass().getClassLoader().getResourceAsStream("conf.txt");
//            System.out.println(configPath);
            //            将website下的文件读入map中
/*            String zh = Thread.currentThread().getContextClassLoader().getResource("website/zh.txt").getPath();
            String en = Thread.currentThread().getContextClassLoader().getResource("website/en.txt").getPath();
            String jp = Thread.currentThread().getContextClassLoader().getResource("website/jp.txt").getPath();
            String ko = Thread.currentThread().getContextClassLoader().getResource("website/ko.txt").getPath();*/

            InputStream zh = this.getClass().getClassLoader().getResourceAsStream("website/zh.txt");
            String content = FileUtil.readFileByStream(zh);
//                System.out.println(content);
            String[] websiteS = content.split("\n");
            ArrayList<String> webSiteArr = new ArrayList<>();
            for (String website: websiteS) {
                if (website.trim().startsWith("#")) {
                    continue;
                }
                webSiteArr.add(website.trim());
            }
            Constant.WEBSITE_MAP.put("zh",webSiteArr);

            InputStream en = this.getClass().getClassLoader().getResourceAsStream("website/en.txt");
            String content2 = FileUtil.readFileByStream(en);
//                System.out.println(content);
            String[] websiteS2 = content2.split("\n");
            ArrayList<String> webSiteArr2 = new ArrayList<>();
            for (String website: websiteS2) {
                if (website.trim().startsWith("#")) {
                    continue;
                }
                webSiteArr2.add(website.trim());
            }
            Constant.WEBSITE_MAP.put("en",webSiteArr2);

            InputStream en_second = this.getClass().getClassLoader().getResourceAsStream("website/second/en.txt");
            String content2_second = FileUtil.readFileByStream(en_second);
//                System.out.println(content);
            String[] websiteS2_second = content2_second.split("\n");
            ArrayList<String> webSiteArr2_second = new ArrayList<>();
            for (String website: websiteS2_second) {
                if (website.trim().startsWith("#")) {
                    continue;
                }
                webSiteArr2_second.add(website.trim());
            }
            Constant.WEBSITE_MAP_SECOND.put("en",webSiteArr2_second);

            InputStream jp = this.getClass().getClassLoader().getResourceAsStream("website/jp.txt");
            String content3 = FileUtil.readFileByStream(jp);
//                System.out.println(content);
            String[] websiteS3 = content3.split("\n");
            ArrayList<String> webSiteArr3 = new ArrayList<>();
            for (String website: websiteS3) {
                if (website.trim().startsWith("#")) {
                    continue;
                }
                webSiteArr3.add(website.trim());
            }
            Constant.WEBSITE_MAP.put("jp",webSiteArr3);

            InputStream jp_second = this.getClass().getClassLoader().getResourceAsStream("website/second/jp.txt");
            String content3_second = FileUtil.readFileByStream(jp_second);
//                System.out.println(content);
            String[] websiteS3_second = content3_second.split("\n");
            ArrayList<String> webSiteArr3_second = new ArrayList<>();
            for (String website: websiteS3_second) {
                if (website.trim().startsWith("#")) {
                    continue;
                }
                webSiteArr3_second.add(website.trim());
            }
            Constant.WEBSITE_MAP_SECOND.put("jp",webSiteArr3_second);

            InputStream ko = this.getClass().getClassLoader().getResourceAsStream("website/ko.txt");
            String content4 = FileUtil.readFileByStream(ko);
//                System.out.println(content);
            String[] websiteS4 = content4.split("\n");
            ArrayList<String> webSiteArr4 = new ArrayList<>();
            for (String website: websiteS4) {
                if (website.trim().startsWith("#")) {
                    continue;
                }
                webSiteArr4.add(website.trim());
            }
            Constant.WEBSITE_MAP.put("ko",webSiteArr4);

            InputStream ko_second = this.getClass().getClassLoader().getResourceAsStream("website/second/ko.txt");
            String content4_second = FileUtil.readFileByStream(ko_second);
//                System.out.println(content);
            String[] websiteS4_second = content4_second.split("\n");
            ArrayList<String> webSiteArr4_second = new ArrayList<>();
            for (String website: websiteS4_second) {
                if (website.trim().startsWith("#")) {
                    continue;
                }
                webSiteArr4_second.add(website.trim());
            }
            Constant.WEBSITE_MAP_SECOND.put("ko",webSiteArr4_second);

            InputStream am = this.getClass().getClassLoader().getResourceAsStream("website/am.txt");
            String content5 = FileUtil.readFileByStream(am);
//                System.out.println(content);
            String[] websiteS5 = content5.split("\n");
            ArrayList<String> webSiteArr5 = new ArrayList<>();
            for (String website: websiteS5) {
                if (website.trim().startsWith("#")) {
                    continue;
                }
                webSiteArr5.add(website.trim());
            }
            Constant.WEBSITE_MAP.put("am",webSiteArr5);
//zh.
//            zh = zh1.
//            System.out.println(zh);
/*            InputStream[] lanSPath = {zh,en,jp,ko}; // 四个盛放网址集合的文件路径
            for (InputStream lan:lanSPath) {
                String content = FileUtil.readFileByStream(lan);
//                System.out.println(content);
                String[] websiteS = content.split("\n");
                ArrayList<String> webSiteArr = new ArrayList<>();
                for (String website: websiteS) {
                    if (website.trim().startsWith("#")) {
                        continue;
                    }
                    webSiteArr.add(website.trim());
                }
                System.out.println("resources/website文件夹下的网址集合文件[ "+lan+" ]下的[ "+webSiteArr.size()+" ]个定向搜索网址加载完毕");
                if (lan.("zh.txt")){
                    lan = "zh-hans";
                }
                if (lan.contains("en.txt")){
                    lan = "en";
                }
                if (lan.contains("jp.txt")){
                    lan = "jp";
                }
                if (lan.contains("ko.txt")){
                    lan = "ko";
                }
                Constant.WEBSITE_MAP.put(lan,webSiteArr);
            }*/

            String s = FileUtil.readFileByStream(configPath);
            String[] split = s.split("\n");
            Map<String, String> map = new HashMap<>();
            for (String str : split) {
                if (str.contains("#") || !str.contains("=")) {
                    continue;
                }
                map.put(str.split("=")[0], str.split("=")[1]);
            }

            Constant.BAIDU_TRANSLATE_APP_ID = map.get("BAIDU_APP_ID");
            Constant.BAIDU_TRANSLATE_SECURITY_KEY = map.get("BAIDU_SECURITY_KEY");

//            bing的订阅
            Constant.BING_KEY = map.get("BING_KEY");
            Constant.PAGE_NUM = Integer.valueOf(map.get("PAGE_NUM"));
            Constant.PAGE_SIZE = Integer.valueOf(map.get("PAGE_SIZE"));

            Constant.LANG = map.get("LANG");
//            System.out.println(Constant.LANG);
            Constant.BING_FILTER = map.get("BING_FILTER");
            Constant.BING_TIME = map.get("BING_TIME");
            Constant.SECOND_FILTER = map.get("SECOND_FILTER");

            Constant.DB_NAME = map.get("DB_NAME");
//            System.out.println(Constant.DB_NAME);
            Constant.DB_USER = map.get("DB_USER");
//            System.out.println(Constant.DB_USER);
            Constant.DB_PASSWORD = map.get("DB_PASSWORD");
//            System.out.println(Constant.DB_PASSWORD);

            String spiderRange = map.get("SPIDER_RANGE");
            if (spiderRange.contains("网页")) {
                Constant.SPIDER_WANGYE = true;
//                Constant.BING_WANGYE_RESULT_OUTPUT_PATH = Constant.ROOT_PATH + "result" + File.separator + now + File.separator + "wangye_result.json";
//                FileUtil.createFile(Constant.BING_WANGYE_RESULT_OUTPUT_PATH);
//                FileUtil.appendFile(Constant.BING_WANGYE_RESULT_OUTPUT_PATH, "[\n");
            }
            if (spiderRange.contains("新闻")) {
                Constant.SPIDER_NEWS = true;
                Constant.BING_NEWS_RESULT_OUTPUT_PATH = Constant.ROOT_PATH + "result" + File.separator + now + File.separator + "news_result.json";
                FileUtil.createFile(Constant.BING_NEWS_RESULT_OUTPUT_PATH);
                FileUtil.appendFile(Constant.BING_NEWS_RESULT_OUTPUT_PATH, "[\n");
            }

            Constant.SECOND_FILTER = map.get("SECOND_FILTER");
            Constant.LANG_NAME = map.get("LANG_NAME");
            Constant.MAX_TIMEOUT = Integer.valueOf(map.get("MAX_TIMEOUT"));

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("系统读取配置文件失败，请检查conf.txt");
        }
    }

    public static void main(String[] args) {
        System.out.println();
    }
}
