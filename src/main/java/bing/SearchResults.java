package bing;

import java.util.HashMap;

/**
 * @author saysky
 * @date 2021/11/15 8:15 下午
 */

public class SearchResults {

    HashMap<String, String> relevantHeaders;
    String jsonResponse;
    SearchResults(HashMap<String, String> headers, String json) {
        relevantHeaders = headers;
        jsonResponse = json;
    }
}
