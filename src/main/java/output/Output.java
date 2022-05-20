package output;


import bing.Topic;

import javax.persistence.*;

@Entity
@Table(name = "output")

public class Output {
    @Id
    @Column(name = "id")
    private String id = "";
    private String abstract1 = "";
    private String all_keywords = "";
    private String areacountry = "";
    private String author = "";
    private String category1 = "";
    private String category_id = "";
    private String columnurl = "";
    private String content_keywords = "";
    private String country_zh = "";
    private String created = "";
    private String language = ""; // 语言
    private String media_type = "";
    private String news_section = "";
    private String orgs = "";
    private String publish_date = "";
    private String text = "";
    private String text_publish = "";
    private String title = "";
    private String title_keywords = "";
    private String url = "";
    private String web_name = "";
    private String zh_abstract = "";
    private String zh_content = "";
    private String zh_title = "";

    public static Output build(Topic topic) {
        Output output = new Output();
        output.setTitle(topic.getTitle());
        output.setUrl(topic.getLink());
//        output.setCategory1(topic.getCategoryName());
//        output.setCategory_id(topic.getCategoryCode());
        output.setText_publish(topic.getContent());
        output.setWeb_name(topic.getSource());
        output.setCreated(topic.getCreateTime());
        output.setLanguage(topic.getLang());
//        output.setContent_keywords(topic.getKeyword());
        return output;
    }

    public String getAbstract1() {
        return abstract1;
    }

    public void setAbstract1(String abstract1) {
        this.abstract1 = abstract1;
    }

    public String getAll_keywords() {
        return all_keywords;
    }

    public void setAll_keywords(String all_keywords) {
        this.all_keywords = all_keywords;
    }

    public String getAreacountry() {
        return areacountry;
    }

    public void setAreacountry(String areacountry) {
        this.areacountry = areacountry;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory1() {
        return category1;
    }

    public void setCategory1(String category1) {
        this.category1 = category1;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getColumnurl() {
        return columnurl;
    }

    public void setColumnurl(String columnurl) {
        this.columnurl = columnurl;
    }

    public String getContent_keywords() {
        return content_keywords;
    }

    public void setContent_keywords(String content_keywords) {
        this.content_keywords = content_keywords;
    }

    public String getCountry_zh() {
        return country_zh;
    }

    public void setCountry_zh(String country_zh) {
        this.country_zh = country_zh;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    @Id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getMedia_type() {
        return media_type;
    }

    public void setMedia_type(String media_type) {
        this.media_type = media_type;
    }

    public String getNews_section() {
        return news_section;
    }

    public void setNews_section(String news_section) {
        this.news_section = news_section;
    }

    public String getOrgs() {
        return orgs;
    }

    public void setOrgs(String orgs) {
        this.orgs = orgs;
    }

    public String getPublish_date() {
        return publish_date;
    }

    public void setPublish_date(String publish_date) {
        this.publish_date = publish_date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText_publish() {
        return text_publish;
    }

    public void setText_publish(String text_publish) {
        this.text_publish = text_publish;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle_keywords() {
        return title_keywords;
    }

    public void setTitle_keywords(String title_keywords) {
        this.title_keywords = title_keywords;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getWeb_name() {
        return web_name;
    }

    public void setWeb_name(String web_name) {
        this.web_name = web_name;
    }

    public String getZh_abstract() {
        return zh_abstract;
    }

    public void setZh_abstract(String zh_abstract) {
        this.zh_abstract = zh_abstract;
    }

    public String getZh_content() {
        return zh_content;
    }

    public void setZh_content(String zh_content) {
        this.zh_content = zh_content;
    }

    public String getZh_title() {
        return zh_title;
    }

    public void setZh_title(String zh_title) {
        this.zh_title = zh_title;
    }


    @Override
    public String toString() {
        return "Output{" +
                "id='" + id + '\'' +
                ", abstract1='" + abstract1 + '\'' +
                ", all_keywords='" + all_keywords + '\'' +
                ", areacountry='" + areacountry + '\'' +
                ", author='" + author + '\'' +
                ", category1='" + category1 + '\'' +
                ", category_id='" + category_id + '\'' +
                ", columnurl='" + columnurl + '\'' +
                ", content_keywords='" + content_keywords + '\'' +
                ", country_zh='" + country_zh + '\'' +
                ", created='" + created + '\'' +
                ", language='" + language + '\'' +
                ", media_type='" + media_type + '\'' +
                ", news_section='" + news_section + '\'' +
                ", orgs='" + orgs + '\'' +
                ", publish_date='" + publish_date + '\'' +
                ", text='" + text + '\'' +
                ", text_publish='" + text_publish + '\'' +
                ", title='" + title + '\'' +
                ", title_keywords='" + title_keywords + '\'' +
                ", url='" + url + '\'' +
                ", web_name='" + web_name + '\'' +
                ", zh_abstract='" + zh_abstract + '\'' +
                ", zh_content='" + zh_content + '\'' +
                ", zh_title='" + zh_title + '\'' +
                '}';
    }

}

