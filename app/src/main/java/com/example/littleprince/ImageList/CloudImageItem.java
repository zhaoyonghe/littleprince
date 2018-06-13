package com.example.littleprince.ImageList;

import java.io.Serializable;

/**
 * Created by zhaoyonghe on 2018/6/10.
 */

public class CloudImageItem implements Serializable {

    //云图片名称
    final private String name;
    //云图片缩略图url
    final private String path;
    //云图片真实url
    final private String realPath;
    //云图片上传日期
    final private String date;
    //标题日期
    final private String header;
    //header相同，headerId就相同；header不同，headerId就不同
    final private int headerId;

    public CloudImageItem(String name, String path, String realPath, String date) {
        this.name = name;
        this.path = path;
        this.realPath = realPath;
        this.date = date;
        this.header = date.split("[A-Za-z]")[0];
        this.headerId = this.header.hashCode();
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public String getDate() {
        return date;
    }

    public String getHeader() {
        return header;
    }

    public int getHeaderId() {
        return headerId;
    }

    public String getRealPath() {
        return realPath;
    }

    @Override
    public String toString() {
        return "CloudImageItem{" +
                "name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", realPath='" + realPath + '\'' +
                ", date='" + date + '\'' +
                ", header='" + header + '\'' +
                ", headerId=" + headerId +
                '}';
    }
}
