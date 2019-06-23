package cn.starchild.common.model;

import javax.persistence.Table;
import java.util.Date;

@Table(name = "t_article")
public class ArticleModel {
    private String id;

    private String userId;

    private String content;

    private byte status;

    private Date created;

    private Date modified;

    private String imageUrlArray;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public String getImageUrlArray() {
        return imageUrlArray;
    }

    public void setImageUrlArray(String imageUrlArray) {
        this.imageUrlArray = imageUrlArray == null ? null : imageUrlArray.trim();
    }
}