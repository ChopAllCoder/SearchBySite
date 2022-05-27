import bing.BingWebSearch_gaoji;
import bing.Topic;
import conf.InitConfService;
import input.Input;
import input.InputService;

import java.util.List;

/**
 * @author Meng Ling'en
 * @create 2022-03-20-16:42
 **/
public class Test {

    public static void main(String[] args) throws Exception {
        InputService inputService = new InputService();
        InitConfService initConfService = new InitConfService();
        String website = "https://www.cbsnews.com/";
//        Input input1 = new Input("神聖幾何学マルセイユ タロット ミレニアムエディション入門書","ウィルフリード・ウドワン","サンジェルマン出版","日本","en");
        Input input1 = new Input("How to Spot a Mom","Donna Amey Bhatt","Wide Eyed Editions","美国","en");
        System.out.println("开始读入初始化配置 ··· ");
        initConfService.initConf();
        System.out.println("初始化配置完成");

        BingWebSearch_gaoji bingWebSearch = new BingWebSearch_gaoji();
        List<Topic> wangyeList;

            try {
                wangyeList = bingWebSearch.search_oneSite(false, website,input1);
                System.out.println("取回的内容条数是:"+wangyeList.size());
                for (Topic topic : wangyeList) {
                    System.out.println("topic  : " +topic.getLink()+"|"+topic.getTitle()+"|"+topic.getContent());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

}
