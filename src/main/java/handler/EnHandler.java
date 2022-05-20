package handler;

import bing.Topic;

import java.util.List;

/**
 * 中文
 *
 * @author saysky
 * @date 2021/12/17 12:05 上午
 */
public class EnHandler implements HandlerService {

    @Override
    public void handle(List<Topic> topicList) {
        for (Topic topic : topicList) {
            String title = topic.getTitle();
            String content = topic.getContent();
            String webName = topic.getSource();
            String url = topic.getLink();

            // 1.如果标题或内容包含Redirect
            if (title.toLowerCase().contains("redirect") || content.toLowerCase().contains("redirect") ) {
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
                    ||url.contains("europepmc.org")

            )) {
                topic.setContent(null);
                continue;
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
