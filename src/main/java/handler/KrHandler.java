package handler;

import bing.Topic;

import java.util.List;

/**
 * 中文
 *
 * @author saysky
 * @date 2021/12/17 12:05 上午
 */
public class KrHandler implements HandlerService {
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
            String title = topic.getTitle();
            String content = topic.getContent();
            String webName = topic.getSource();
            // 1.标题包含“价格”、“招聘”关键词的过滤掉
            if (title.contains("价格") || title.contains("招聘")) {
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
            if (title.toLowerCase().contains("redirect") || content.toLowerCase().contains("redirect") ) {
                topic.setContent(null);
                continue;
            }



            // 2.处理首页标题是否包含网
            if(!webName.contains("网易")) {
                int index3 = webName.indexOf("网");
                if (index3 > -1) {
                    webName = webName.substring(0, index3 + "网".length());
                    topic.setSource(webName);
                }
            }


            // 3. 是否包含正文
            int index1 = content.indexOf("正文");
            if (index1 > -1) {
                content = content.substring(index1 + "正文".length());
                topic.setContent(content);
            } else {
                int index2 = content.indexOf(title);
                if (index2 > -1) {
                    content = content.substring(index2);
                    topic.setContent(content);

                }
            }

        }
    }

    public static void main(String[] args) {
        String title = "ssss正文网";
        int index = title.indexOf("网");
        if(index > -1) {
            title = title.substring(0, index+"网".length());
        }
        System.out.println(title);
    }
}
