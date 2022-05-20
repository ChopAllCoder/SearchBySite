package output;

import org.hibernate.tool.schema.internal.exec.GenerationTarget;

/**
 * @author Meng Ling'en
 * @create 2022-03-25-14:55
 * 盛放Result 中的data
 **/
public class Result_Data {

    private String link;
    private String title;
    private String originTitle;//如原文是中文则为空
    private String summary;
    private String originSummary; //如原文是中文则为空
    private String content;
    private String originContent;
    private String source;
    private String time;  //format后的时间，YYYY年MM月DD日 HH:mm格式


//    get   set
    public void setLink(String link) {
        this.link = link;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setOriginTitle(String originTitle) {
        this.originTitle = originTitle;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setOriginSummary(String originSummary) {
        this.originSummary = originSummary;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setOriginContent(String originContent) {
        this.originContent = originContent;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLink() {
        return link;
    }

    public String getTitle() {
        return title;
    }

    public String getOriginTitle() {
        return originTitle;
    }

    public String getSummary() {
        return summary;
    }

    public String getOriginSummary() {
        return originSummary;
    }

    public String getContent() {
        return content;
    }

    public String getOriginContent() {
        return originContent;
    }

    public String getSource() {
        return source;
    }

    public String getTime() {
        return time;
    }
}
