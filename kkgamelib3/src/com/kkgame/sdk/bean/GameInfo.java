package com.kkgame.sdk.bean;

import java.io.Serializable;

/**
 * 游戏
 * @author wjy
 *
 */
public class GameInfo implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public String name;     //游戏名
    public String id;       //游戏id
    public long size;       //大小
    public String category; //类别
    public String description;  //介绍
    public String url_id;       //下载地址
            
    public String upfile;       //图片地址
    
    public String qqgroup;      //qq群

    @Override
    public String toString() {
        return "GameInfo [name=" + name + ", id=" + id + ", size=" + size
                + ", category=" + category + ", description=" + description
                + ", url_id=" + url_id + ", upfile=" + upfile + ", qqgroup="
                + qqgroup + "]";
    }
    
    
    
}
