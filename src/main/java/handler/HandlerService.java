package handler;

import bing.Topic;

import java.util.List;

/**
 * @author saysky
 * @date 2021/12/17 12:04 上午
 */

public interface HandlerService {

    void handle(List<Topic> topicList);
}
