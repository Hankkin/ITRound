package com.hankkin.itround.bean;

import com.hankkin.itround.db.StringConverter;
import com.hankkin.library.bean.BaseBean;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Property;

import java.util.List;

/**
 * Created by hankkin on 2017/10/12.
 * Blog: http://hankkin.cn
 * Mail: 1019283569@qq.com
 */

@Entity(nameInDb = "gankbean")
public class GankBean extends BaseBean {

    /**
     * _id : 59dc4e01421aa94e07d1848f
     * createdAt : 2017-10-10T12:35:13.51Z
     * desc : iOS 底部菜单栏效果，很漂亮哦。
     * images : ["http://img.gank.io/b6c7c65e-4f55-4780-aa7c-ac41738594fb"]
     * publishedAt : 2017-10-10T12:41:34.882Z
     * source : chrome
     * type : iOS
     * url : https://github.com/alexaubry/BulletinBoard
     * used : true
     * who : 代码家
     */


    @Property(nameInDb = "_id")
    private String _id;

    @Property(nameInDb = "createdAt")
    private String createdAt;

    @Property(nameInDb = "desc")
    private String desc;

    @Property(nameInDb = "publishedAt")
    private String publishedAt;

    @Property(nameInDb = "source")
    private String source;

    @Property(nameInDb = "type")
    private String type;

    @Property(nameInDb = "url")
    private String url;

    @Property(nameInDb = "used")
    private boolean used;

    @Property(nameInDb = "who")
    private String who;

    @Convert(columnType = String.class,converter = StringConverter.class)
    private List<String> images;

    @Generated(hash = 423726535)
    public GankBean(String _id, String createdAt, String desc, String publishedAt,
            String source, String type, String url, boolean used, String who,
            List<String> images) {
        this._id = _id;
        this.createdAt = createdAt;
        this.desc = desc;
        this.publishedAt = publishedAt;
        this.source = source;
        this.type = type;
        this.url = url;
        this.used = used;
        this.who = who;
        this.images = images;
    }

    @Generated(hash = 1453199415)
    public GankBean() {
    }

    public String get_id() {
        return this._id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPublishedAt() {
        return this.publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getSource() {
        return this.source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean getUsed() {
        return this.used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public String getWho() {
        return this.who;
    }

    public void setWho(String who) {
        this.who = who;
    }

    public List<String> getImages() {
        return this.images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
    
}
