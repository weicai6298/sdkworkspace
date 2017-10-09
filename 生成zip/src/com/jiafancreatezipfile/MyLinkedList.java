package com.jiafancreatezipfile;


import java.util.Collection;
import java.util.LinkedList;
import java.util.TreeSet;

/**
 * Created by Administrator on 2016/10/11.
 */

public class MyLinkedList<T>  {

    private  String id;

    private LinkedList<T> mData=new LinkedList<T>();
    private TreeSet<T> mData_set=new TreeSet<T>();

    public LinkedList<T> getmData() {
        return mData;
    }

    public void addAll(Collection<? extends T> mdata){
        mData.clear();

        mData_set.addAll(mdata);
       // Logger.d("添加前："+mData.size());
        mData.addAll(mData_set);
      //  Logger.d("添加后："+mData.size());


    }

    public void add(T mdata){
        mData.clear();
        mData_set.add(mdata);
        mData.addAll(mData_set);

    }

    public T get(int location){
        return mData.get(location);
    }

    public int size(){

        return mData.size();

    }


    public void clear() {
        mData.clear();
    }
}
