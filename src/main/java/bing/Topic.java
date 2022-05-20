package bing;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author saysky
 * @date 2021/11/15 10:27 下午
 */

public class Topic {
//    private String categoryCode;
//    private String categoryName;
//    private String keyword;
    private String title;
    private String oriTitle;

    private String summary;
    private String originSummary;

    private String content;
    private String originContent;

    private String link;
    private String time;
//    private String type;
    @JsonIgnore
    private String lang;
//    private String domain;
    private String source;


    public Topic(String title, String summary, String url, String createTime) {
        this.title = title;
        this.summary = summary;
        this.link = url;
        this.time = createTime;
    }

/*    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }*/

    public Topic() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getCreateTime() {
        return time;
    }

    public void setCreateTime(String createTime) {
        this.time = createTime;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public Topic(String title, String summary, String url, String createTime, String lang) {
        this.title = title;
        this.summary = summary;
        this.link = url;
        this.time = createTime;
//        this.type = type;
        this.lang = lang;
    }

/*    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }*/

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

/*    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }*/

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
