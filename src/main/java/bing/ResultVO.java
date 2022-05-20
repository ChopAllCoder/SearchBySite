package bing;

import java.util.List;

/**
 * @author saysky
 * @date 2021/11/15 10:03 下午
 */

public class ResultVO {

    private WebPages webPages;
    private News news;
    private List<NewsDetail> value;

    public WebPages getWebPages() {
        return webPages;
    }

    public void setWebPages(WebPages webPages) {
        this.webPages = webPages;
    }

    public News getNews() {
        return news;
    }

    public void setNews(News news) {
        this.news = news;
    }

    public List<NewsDetail> getValue() {
        return value;
    }

    public void setValue(List<NewsDetail> value) {
        this.value = value;
    }
}

class WebPages {

    private List<WebPagesDetail> value;

    public List<WebPagesDetail> getValue() {
        return value;
    }

    public void setValue(List<WebPagesDetail> value) {
        this.value = value;
    }
}


class News {
    private List<NewsDetail> value;

    public List<NewsDetail> getValue() {
        return value;
    }

    public void setValue(List<NewsDetail> value) {
        this.value = value;
    }
}

class WebPagesDetail {
    private String id;
    private String name;
    private String url;
    private String snippet;
    private String dateLastCrawled;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public String getDateLastCrawled() {
        return dateLastCrawled;
    }

    public void setDateLastCrawled(String dateLastCrawled) {
        this.dateLastCrawled = dateLastCrawled;
    }
}

class NewsDetail {

    private String id;
    private String name;
    private String url;
    private String description;
    private String datePublished;
    private String category;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDatePublished() {
        return datePublished;
    }

    public void setDatePublished(String datePublished) {
        this.datePublished = datePublished;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
