package com.example.littleprince.ImageList;

import java.io.Serializable;

/**
 * Created by zhaoyonghe on 2018/6/10.
 */

public class CloudImageItem implements Serializable {

    //云图片名称
    final private String name;
    //云图片url
    final private String path;
    //云图片上传日期
    final private String date;
    //标题日期
    final private String header;
    //header相同，headerId就相同；header不同，headerId就不同
    final private int headerId;

    public CloudImageItem(String name, String path, String date) {
        this.name = name;
        this.path = path;
        this.date = date;
        this.header = date;
        this.headerId = date.hashCode();
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
}
