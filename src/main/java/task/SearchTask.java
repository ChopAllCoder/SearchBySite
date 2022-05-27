package task;

import bing.BingWebSearch_gaoji;
import bing.Topic;
import dao.OutputDao;
import input.Input;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author saysky
 * @date 2021/11/13
 */
public class SearchTask implements Runnable {

    Input input;
    int num;
    int total;

/*    public List<Topic> bingSearch() throws Exception {
        BingWebSearch bingWebSearch = new BingWebSearch();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(sdf.format(new Date()) + ">>>============================= " + (num + 1) + "/" + total + " " + input.getName() + " start =============================== ");
        List<Topic> wangyeList = bingWebSearch.search(false, input);
        System.out.println(sdf.format(new Date()) + "<<<============================= " + (num + 1) + "/" + total + " " + input.getName() + " end =============================== ");

        return wangyeList;
    }*/

    @Override
    public void run() {
        BingWebSearch_gaoji bingWebSearch = new BingWebSearch_gaoji();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        OutputDao outputDao = new OutputDao();
        System.out.println(sdf.format(new Date()) + ">>>============================= " + input.getBookname() + " start =============================== ");

        try {
//            String lan = input.getLanguage();  // 得到要搜索的语言,据此然后得到要搜索的网址集合
/*            ArrayList<String> websitesArrFromLan = new ArrayList<>();
            if (Constant.WEBSITE_MAP.keySet().contains(Constant.LANG_NAME)){
                websitesArrFromLan = Constant.WEBSITE_MAP.get(Constant.LANG_NAME);
            }*/
            String website = "www.baidu.com";
//          调用Bing搜索，拿到搜索返回结果。
            List<Topic> wangyeList = bingWebSearch.search_oneSite(false, website,input);
            for (Topic topic : wangyeList) {
                System.out.println("topic  : " +topic.getTitle()+"|"+topic.getContent()+"|"+topic.getLink());
                /*topic.setKeyword(input.getName().trim());
                topic.setCategoryCode(input.getNum().trim());
                topic.setCategoryName(Constant.CATEGORY_MAP.get(topic.getCategoryCode()));
                topic.setLang(Constant.LANG_NAME);
                Output output = Output.build(topic);*/
//                outputDao.insert(output);
            }
            System.out.println( "查询到" + wangyeList.size() + "条网页");


        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
//        System.out.println(sdf.format(new Date()) + "<<<============================= 进度" + (num + 1) + "/" + total + " " + input.getName() + " end =============================== ");
    }

    public Input getInput() {
        return input;
    }

    public void setInput(Input input) {
        this.input = input;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
