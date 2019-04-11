package cn.starchild.common.model;

import javax.persistence.Table;
import java.util.Date;

@Table(name = "t_help_info")
public class HelpInfoModel {
    private String id;

    private String helpCategoryId;

    private String title;

    private Boolean status;

    private Boolean sort;

    private String creator;

    private Date created;

    private String modifier;

    private Date modified;

    private String content;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getHelpCategoryId() {
        return helpCategoryId;
    }

    public void setHelpCategoryId(String helpCategoryId) {
        this.helpCategoryId = helpCategoryId == null ? null : helpCategoryId.trim();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Boolean getSort() {
        return sort;
    }

    public void setSort(Boolean sort) {
        this.sort = sort;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator == null ? null : creator.trim();
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier == null ? null : modifier.trim();
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }
}