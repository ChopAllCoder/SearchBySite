package dao;

import output.Output;
import util.DBUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class OutputDao {

    //添加
    public void insert(Output u) throws SQLException, ParseException {
        // 判断标题是否已经存在
        boolean urlExistFlag = urlHasExist(u.getUrl());
        if(urlExistFlag) {
            return;
        }

        // 判断内容是否已经存在
//        boolean contentExistFlag = contentHasExist(u.getText_publish());
//        if(contentExistFlag) {
//            return;
//        }

        Connection conn = DBUtil.getConnection();
        String sql = "INSERT INTO output (" +
                "id, abstract1, all_keywords, areacountry, author, category1," +
                "category_id, columnurl, content_keywords, country_zh,created, language," +
                "media_type, news_section, orgs, publish_date, text, text_publish," +
                "title,title_keywords, url, web_name, zh_abstract, zh_content," +
                "zh_title" +
                ") VALUES (?, ?, ?, ? ,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
        PreparedStatement ptmt = conn.prepareStatement(sql);
        ptmt.setString(1, null);
        ptmt.setString(2, u.getAbstract1());
        ptmt.setString(3, u.getAll_keywords());
        ptmt.setString(4, u.getAreacountry());
        ptmt.setString(5, u.getAuthor());
        ptmt.setString(6, u.getCategory1());
        ptmt.setString(7, u.getCategory_id());
        ptmt.setString(8, u.getColumnurl());
        ptmt.setString(9, u.getContent_keywords());
        ptmt.setString(10, u.getCountry_zh());
        ptmt.setString(11, u.getCreated());
        ptmt.setString(12, u.getLanguage());
        ptmt.setString(13, u.getMedia_type());
        ptmt.setString(14, u.getNews_section());
        ptmt.setString(15, u.getOrgs());
        ptmt.setString(16, u.getPublish_date());
        ptmt.setString(17, u.getText());
        ptmt.setString(18, u.getText_publish());
        ptmt.setString(19, u.getTitle());
        ptmt.setString(20, u.getTitle_keywords());
        ptmt.setString(21, u.getUrl());
        ptmt.setString(22, u.getWeb_name());
        ptmt.setString(23, u.getZh_abstract());
        ptmt.setString(24, u.getZh_content());
        ptmt.setString(25, u.getZh_title());
        try {
            ptmt.executeUpdate();
        } catch (Exception e) {

        }
    }
    //遍历用户信息
    public List<Output> query() throws SQLException {
        List<Output> result = new ArrayList<Output>();
        Connection conn = DBUtil.getConnection();
        String sql = " select * from output";
        PreparedStatement ptmt = conn.prepareStatement(sql);
        ResultSet rs = ptmt.executeQuery();
        Output u = null;
        while(rs.next()) {
            u = new Output();
            u.setId(rs.getString("id"));
            u.setAbstract1(rs.getString("abstract1"));
            u.setAll_keywords(rs.getString("all_keywords"));
            u.setAreacountry(rs.getString("areacountry"));
            u.setAuthor(rs.getString("author"));
            u.setCategory1(rs.getString("category1"));
            u.setCategory_id(rs.getString("category_id"));
            u.setColumnurl(rs.getString("columnurl"));
            u.setContent_keywords(rs.getString("content_keywords"));
            u.setCountry_zh(rs.getString("country_zh"));
            u.setCreated(rs.getString("created"));
            u.setLanguage(rs.getString("language"));
            u.setMedia_type(rs.getString("media_type"));
            u.setNews_section(rs.getString("news_section"));
            u.setOrgs(rs.getString("orgs"));
            u.setPublish_date(rs.getString("publish_date"));
            u.setText(rs.getString("text"));
            u.setText_publish(rs.getString("text_publish"));
            u.setTitle(rs.getString("title"));
            u.setTitle_keywords(rs.getString("title_keywords"));
            u.setUrl(rs.getString("url"));
            u.setWeb_name(rs.getString("web_name"));
            u.setZh_abstract(rs.getString("zh_abstract"));
            u.setZh_content(rs.getString("zh_content"));
            u.setZh_title(rs.getString("zh_title"));
            result.add(u);
        }
        return result;
    }

    //根据输入的语言类型，遍历返回满足条件的用户信息
    public List<Output> queryByLan(String lan) throws SQLException {
        List<Output> result = new ArrayList<Output>();
        Connection conn = DBUtil.getConnection();
        String sql = " select * from output where language=\""+lan+"\"";
        PreparedStatement ptmt = conn.prepareStatement(sql);
        ResultSet rs = ptmt.executeQuery();
        Output u = null;
        while(rs.next()) {
            u = new Output();
            u.setId(rs.getString("id"));
            u.setAbstract1(rs.getString("abstract1"));
            u.setAll_keywords(rs.getString("all_keywords"));
            u.setAreacountry(rs.getString("areacountry"));
            u.setAuthor(rs.getString("author"));
            u.setCategory1(rs.getString("category1"));
            u.setCategory_id(rs.getString("category_id"));
            u.setColumnurl(rs.getString("columnurl"));
            u.setContent_keywords(rs.getString("content_keywords"));
            u.setCountry_zh(rs.getString("country_zh"));
            u.setCreated(rs.getString("created"));
            u.setLanguage(rs.getString("language"));
            u.setMedia_type(rs.getString("media_type"));
            u.setNews_section(rs.getString("news_section"));
            u.setOrgs(rs.getString("orgs"));
            u.setPublish_date(rs.getString("publish_date"));
            u.setText(rs.getString("text"));
            u.setText_publish(rs.getString("text_publish"));
            u.setTitle(rs.getString("title"));
            u.setTitle_keywords(rs.getString("title_keywords"));
            u.setUrl(rs.getString("url"));
            u.setWeb_name(rs.getString("web_name"));
            u.setZh_abstract(rs.getString("zh_abstract"));
            u.setZh_content(rs.getString("zh_content"));
            u.setZh_title(rs.getString("zh_title"));
            result.add(u);
        }
        return result;
    }

    public boolean urlHasExist(String title) throws SQLException {
        Connection conn = DBUtil.getConnection();
        String sql = " select * from output where url = \""+title+"\"";
        PreparedStatement ptmt = conn.prepareStatement(sql);
        ResultSet rs = ptmt.executeQuery();
        if(rs.next()) {
            return true;
        }
        return false;
    }

    /**
     * 内容是否存在
     * @param textPublish
     * @return
     * @throws SQLException
     */
    public boolean contentHasExist(String textPublish) throws SQLException {
        Connection conn = DBUtil.getConnection();
        String sql = " select * from output where text_publish  = \""+textPublish+"\"";
        PreparedStatement ptmt = conn.prepareStatement(sql);
        ResultSet rs = ptmt.executeQuery();
        if(rs.next()) {
            return true;
        }
        return false;
    }

    /**
     * 内容是否存在
     * @param language
     * @return
     * @throws SQLException
     */
    public boolean searchByLang(String language) throws SQLException {
        Connection conn = DBUtil.getConnection();
        String sql = " select * from output where language  = \""+language+"\"";
        PreparedStatement ptmt = conn.prepareStatement(sql);
        ResultSet rs = ptmt.executeQuery();
        if(rs.next()) {
            return true;
        }
        return false;
    }



    public static void main(String[] args) throws SQLException {
        OutputDao outputDao = new OutputDao();
        boolean result = outputDao.contentHasExist("333");
//        List<Output> outputs = outputDao.queryByLan("中简");
        System.out.println(result);
    }
}
