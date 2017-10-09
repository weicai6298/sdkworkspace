package com.jiafancreatezipfile;

public class DataBean implements Comparable<DataBean>{
	 private String author_nickname;
     private String author_uid;
     private String content;
     private int create_time;
     private String id;
     private String s_id;
     private String status;
     private String type;
     private String uid;

     @Override
     public String toString() {
         return "DataBean{" +
                 "author_nickname='" + author_nickname + '\'' +
                 ", author_uid='" + author_uid + '\'' +
                 ", content='" + content + '\'' +
                 ", create_time='" + create_time + '\'' +
                 ", id='" + id + '\'' +
                 ", s_id='" + s_id + '\'' +
                 ", status='" + status + '\'' +
                 ", type='" + type + '\'' +
                 ", uid='" + uid + '\'' +
                 '}';
     }

     public String getAuthor_nickname() {
         return author_nickname;
     }

     public void setAuthor_nickname(String author_nickname) {
         this.author_nickname = author_nickname;
     }

     public String getAuthor_uid() {
         return author_uid;
     }

     public void setAuthor_uid(String author_uid) {
         this.author_uid = author_uid;
     }

     public String getContent() {
         return content;
     }

     public void setContent(String content) {
         this.content = content;
     }

     public int getCreate_time() {
         return create_time;
     }

     public void setCreate_time(int create_time) {
         this.create_time = create_time;
     }

     public String getId() {
         return id;
     }

     public void setId(String id) {
         this.id = id;
     }

     public String getS_id() {
         return s_id;
     }

     public void setS_id(String s_id) {
         this.s_id = s_id;
     }

     public String getStatus() {
         return status;
     }

     public void setStatus(String status) {
         this.status = status;
     }

     public String getType() {
         return type;
     }

     public void setType(String type) {
         this.type = type;
     }

     public String getUid() {
         return uid;
     }

     public void setUid(String uid) {
         this.uid = uid;
     }

     public int compareTo(DataBean dataBean) {
         if (this.id.equals(dataBean.getId())){
             System.out.println("相同："+this.id+"=="+dataBean.getId());
             return 0;
         }else {
        	 System.out.println("bu相同："+this.id+"=="+dataBean.getId());
             if (this.create_time>dataBean.create_time)
             {
                 return 1;
             }else {
                 return -1;
             }
         }
     }
}
