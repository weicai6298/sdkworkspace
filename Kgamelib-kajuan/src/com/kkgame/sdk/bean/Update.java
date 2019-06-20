package com.kkgame.sdk.bean;

public class Update {

    public String pack_url;

    public String ver;

    public boolean isUpdate;

    public String note;

    public long size;

    @Override
    public String toString() {
        return "Update [pack_url=" + pack_url + ", ver=" + ver + ", isUpdate="
                + isUpdate + ", note=" + note + ", size=" + size + "]";
    }

}
