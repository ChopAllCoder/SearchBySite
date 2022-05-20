package input;

/**
 * @author saysky
 * @date 2021/11/15 10:45 下午
 */

public class Input {
    private String bookname;  //书名
    private String author;  // 作者
    private String publication;  //出版社
    private String country; //国家
    private String language; //需要在哪个国家的网址集合里面进行检索，zh-hans,en,jp,ko 这是标准选项

    public Input() {
    }

    public Input(String bookname, String author, String publication, String country, String language) {
        this.bookname = bookname;
        this.author = author;
        this.publication = publication;
        this.country = country;
        this.language = language;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setBookname(String bookname) {
        this.bookname = bookname;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setPublication(String publication) {
        this.publication = publication;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getBookname() {
        return bookname;
    }

    public String getAuthor() {
        return author;
    }

    public String getPublication() {
        return publication;
    }

    public String getCountry() {
        return country;
    }
}
