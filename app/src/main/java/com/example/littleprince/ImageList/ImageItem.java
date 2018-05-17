package com.example.littleprince.ImageList;

/**
 * Created by zhaoyonghe on 2018/4/29.
 */


import java.io.Serializable;
import java.text.SimpleDateFormat;

/**
 * 显示的图片单个实体
 */

public class ImageItem implements Serializable {
    //图片名称
    final private String name;
    //图片路径
    final private String path;
    //图片的拍照时间（从1970年） 毫秒
    final private String imageTaken;
    //图片的空间占用大小
    final private long imageSize;
    final private int height;
    final private int width;
    //标题日期 yyyy-MM-dd
    final private String header;
    //header相同，headerId就相同；header不同，headerId就不同
    final private int headerId;


    public ImageItem(String name, String path, String imageTaken, long imageSize,int height,int width) {
        this.name = name;
        this.path = path;
        this.imageTaken = imageTaken;
        this.imageSize = imageSize;
        this.height=height;
        this.width=width;

        //设置日期形态
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        this.header = sdf.format(Long.parseLong(imageTaken)).split(" ")[0];
        this.headerId = header.hashCode();
    }


    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public String getImageTaken() {
        return imageTaken;
    }

    public long getImageSize() {
        return imageSize;
    }

    public String getHeader() {
        return header;
    }

    public int getHeaderId() {
        return headerId;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}
