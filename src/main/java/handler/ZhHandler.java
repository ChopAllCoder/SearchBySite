package handler;

import bing.Topic;
import util.StringUtil;
import java.util.List;

/**
 * 中文
 *
 * @author saysky
 * @date 2021/12/17 12:05 上午
 */
public class ZhHandler implements HandlerService {
    //    （1）——>（只对中文）直接从正文字符串中匹配标题，找到标题的位置，截取从这个位置到结束的位置的字符串，不采用p标签（p标签方法备份）
//            1、优先匹配“正文”关键字，判断有无“正文”关键词，则截取正文后面内容；其次若无“正文”关键词，但有标题，则截取标题（包含标题）到结尾的。若无，就不做处理。
//            （2）——>标题：把****网留下，逻辑：先判断有没有“网”字，有网就截取，没有网就不处理。
//            （3）—>可以根据标题过滤，标题包含“价格”、“招聘”关键词的过滤掉
//（4）把搜狐网站内容替换成摘要；B站网址替换http获取内容；另外内容字符个数低于15，直接过滤
//（5）没有title，因为<title
//
    @Override
    public void handle(List<Topic> topicList) {
        for (Topic topic : topicList) {
            try {
                String title = topic.getTitle();
                String url = topic.getLink();
                String content = topic.getContent();
                String webName = topic.getSource();
                // 1.标题包含“价格”、“招聘”关键词的过滤掉
                if (title != null && (title.contains("301")  ||
                        title.contains("天眼查")  || title.contains("价格") ||
                        title.contains("招聘")
                        || title.contains("行情走势")
                        ||title.contains("豆丁")
                        ||title.contains("厂家")
                        ||title.toLowerCase().contains(".doc")
                        ||title.contains("报价")
                        ||title.contains("招采")
                        ||title.contains("采招")
                        ||title.contains("交易平台")
                        ||title.contains("招生")
                        ||title.contains("就业")
                        ||title.contains("市场月报")
                        ||title.contains("公司经营")
                        ||title.contains("笔试面试真题")
                        ||title.contains("有限公司")
                        ||title.contains(".pdf")
                        ||title.contains(".ppt")
                )) {
                    topic.setContent(null);
                    continue;
                }
                // url包含
                if (url != null && (url.contains("mysteel.com")

                        ||url.contains("3g.k.sohu.com")
                        ||url.contains("bbs.")
                        ||url.contains("qixin.com")
                        ||url.contains("former-www.chinapp.com")
                        ||url.contains("ansteelgroup.com")
                        ||url.contains("safehoo.com")
                        ||url.contains("jxzxmetal.com")
                        ||url.contains("zhihu.com")
                        ||url.contains("tireworld.com")
                        ||url.contains("jxdl.ctgu.edu.cn")
                        ||url.contains("wenkunet.com")
                        ||url.contains("cnblogs.com")
                        ||url.contains("dswenku.com")
                        ||url.contains("ekdoc.com")
                        ||url.contains("csdn.net")
                        ||url.contains("wenanjuzi.com")
                        ||url.contains("cnfyg.com")
                        ||url.contains("xitongzhijia.net")

                )) {
                    topic.setContent(null);
                    continue;
                }
                if(url.contains("xueqiu.com")) {
                    topic.setSource("雪球网");
                    continue;
                }

                if (webName != null && (webName.contains("百度爱采购") ||
                        webName.contains("中研智业研究网") || webName.contains("价格")
                        || webName.contains("行情走势")||webName.contains("豆丁")||webName.contains("厂家")
                        ||webName.contains("机电之家")||webName.contains("淘金地")||webName.contains("汽车之家")
                        ||webName.toLowerCase().contains(".doc")||webName.contains("原创力")
                        ||webName.contains("淘宝")||webName.contains("商务网")
                        || webName.contains("�")
                        ||webName.contains("301")
                        ||webName.contains("专利")
                        ||webName.contains("论文")
                        ||webName.contains("X技术")
                        ||webName.contains("商城")
                        ||webName.contains("全球机械网")
                        ||webName.contains("仪表网")
                        ||webName.contains("仪器")
                        ||webName.contains("化工产品网")
                        ||webName.contains("中商情报网")
                        ||webName.contains("商品资讯")
                        ||webName.contains("装修问答")
                        ||webName.contains("供应")
                        ||webName.contains("股票")
                        ||webName.contains("企业新闻")
                        ||webName.contains("本地企业信息")
                        ||webName.contains("财经首页")
                        ||webName.contains("水泥网")
                        ||webName.contains("中钢国检专注检验")
                        ||webName.contains("化工仪器网")
                        ||webName.contains("第一枪")
                        ||webName.contains("51行业报告网")
                        ||webName.contains("百科知识大全")
                        ||webName.contains("zhulong")
                        ||webName.contains("东方财富博客")
                        ||webName.contains("求职")
                        ||webName.contains("bokee.net")
                        ||webName.contains("中国钢材网")
                        ||webName.contains("实用技术推广网")
                        ||webName.contains("木业网")
                        ||webName.contains("研究报告")
                        ||webName.contains("华研中商研究网")
                        ||webName.contains("百度知道")
                        ||webName.contains("b2b免费发布信息网")
                        ||webName.contains("交易平台")
                        ||webName.contains("就业")
                        ||webName.contains("知乎")
                        ||webName.contains("CSDN")
                        ||webName.contains("有限公司")
                        ||webName.contains("销售")
                        ||webName.contains("磐金钢管")
                        ||webName.contains("招标")
                        ||webName.contains("销售部")
                        ||webName.contains("百铸网")
                        ||webName.contains("市场分析")
                        ||webName.contains("手游")
                        ||webName.contains("游戏")
                        ||webName.contains("CE认证")
                        ||webName.contains("招生")
                        ||webName.contains("股市")
                        ||webName.contains("行情")
                        ||webName.contains("装修网")
                        ||webName.contains("器械集团")
                        ||webName.contains("作文")
                        ||webName.contains("文库")



                )){
                    topic.setContent(null);
                    continue;
                }


                if (content != null && (content.contains("今日价格") || content.contains("行情走势") || content.contains("最新报价"))) {
                    topic.setContent(null);
                    continue;
                }

                if(content != null &&
                        (content.contains("您使用的浏览器版本过低"))
                        || content.contains("百度安全验证")
                        ||content.contains("订购")
                        ||  content.contains("�")
                        || content.contains("�")
                        || (content.contains("æ") && content.contains("ã")
                        || content.length() <= 20

                )
                ) {
                    topic.setContent(null);
                    continue;
                }
//            // 2.处理标题是否包含网
//            int index = title.indexOf("网");
//            if(index > -1) {
//                title = title.substring(0, index+"网".length());
//                topic.setTitle(title);
//
//            }

                // 1.如果标题或内容包含Redirect
                if (title.toLowerCase().contains("redirect") || content.toLowerCase().contains("redirect")) {
                    topic.setContent(null);
                    continue;
                }


                // 2.处理首页标题是否包含网
                if (!webName.contains("网易")) {
                    int index3 = webName.indexOf("网");
                    if (index3 > -1) {
                        webName = webName.substring(0, index3 + "网".length());
                        topic.setSource(webName);
                    }
                }


//                // 3. 是否包含正文
//                int index1 = content.indexOf("正文");
//                if (index1 > -1) {
//                    content = content.substring(index1 + "正文".length());
//                    topic.setContent(content);
//                } else {
//                    int index2 = content.indexOf(title);
//                    if (index2 > -1) {
//                        content = content.substring(index2);
//                        topic.setContent(content);
//                    }
//                }

                // 4.把标题“-”或“_”及后面的文字去掉
                if (webName != null) {
                    webName = webName.replace("\t", "");
                    webName = webName.replace("\n", "");
//                    webName = StringUtil.deleteAfter(webName, "-");
//                    webName = StringUtil.deleteAfter(webName, "_");
//                    webName = StringUtil.deleteAfter(webName, "—");
//                    webName = StringUtil.deleteAfter(webName, "|");

//                    webName = StringUtil.deleteAfter(webName, ".docx");
                    topic.setSource(webName);
                }
                if (title != null) {
                    title = title.replace("\\t", "");
                    title = title.replace("\t", "");
                    title = title.replace("\\n", "");
                    title = title.replace("\n", "");
                    title = StringUtil.deleteAfter(title, "-");
                    title = StringUtil.deleteAfter(title, "―");
                    title = StringUtil.deleteAfter(title, "_");
                    title = StringUtil.deleteAfter(title, "—");
                    title = StringUtil.deleteAfter(title, "|");
//                    title = StringUtil.deleteAfter(title, ".");
//                    title = StringUtil.deleteAfter(title,"（");

//                    title = StringUtil.deleteAfter(title, ".docx");

                    topic.setTitle(title);
                }


                // 删除内容后面的
                if (content != null) {
                    content = StringUtil.deleteAfter(content, "版权说明");
                    content = StringUtil.deleteAfter(content, "特别声明");
                    content = StringUtil.deleteAfter(content, "免责声明");
                    content = StringUtil.deleteAfter(content, "版权所有");
                    content = StringUtil.deleteAfter(content, "凡本网注明");
                    content = StringUtil.deleteAfter(content, "Copyright");
                    content = StringUtil.deleteAfter(content, "本站所展示");
                    content = StringUtil.deleteAfter(content, "客服咨询电话");
                    content = StringUtil.deleteAfter(content, "（4）感谢");


                    content = content.replace("拼 命 加 载 中 ...", "");
                    topic.setContent(content);
                }




            } catch (Exception e) {

            }
        }
    }


    public static void main(String[] args) {
//        String title = "北京-";
//        int index = title.indexOf("-");
//        if(index > -1) {
//            title = title.substring(0, index+"-".length()-1);
//        }
//        System.out.println(title);
        String title = "北京-";
        int index = title.indexOf("-");
        if(index > -1) {
            title = title.substring(0, index+"-".length()-1);
        }
        System.out.println(title);
    }
}
