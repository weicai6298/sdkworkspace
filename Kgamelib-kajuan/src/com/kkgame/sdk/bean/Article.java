package com.kkgame.sdk.bean;

/**
 * 文章信息
 * @author wjy
 *
 */
public class Article {

    public String id;       //id
    public String name;     //标题名
    public String description;  //简介
    public String create_time;  //创建时间
    public String upfile;       //图片地址
    public String clicknum;     //点击数
    @Override
    public String toString() {
        return "Article [id=" + id + ", name=" + name + ", description="
                + description + ", create_time=" + create_time + ", upfile="
                + upfile + ", clicknum=" + clicknum + "]";
    }
        
    
    
}
