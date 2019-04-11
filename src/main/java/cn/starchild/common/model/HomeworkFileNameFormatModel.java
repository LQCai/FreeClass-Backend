package cn.starchild.common.model;

import javax.persistence.Table;
import java.util.Date;

@Table(name = "t_homework_file_name_format")
public class HomeworkFileNameFormatModel {
    private String id;

    private String homeworkId;

    private String zipName;

    private String firstPrefix;

    private String middlePrefix;

    private String lastPrefix;

    private Date created;

    private Date modified;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getHomeworkId() {
        return homeworkId;
    }

    public void setHomeworkId(String homeworkId) {
        this.homeworkId = homeworkId == null ? null : homeworkId.trim();
    }

    public String getZipName() {
        return zipName;
    }

    public void setZipName(String zipName) {
        this.zipName = zipName == null ? null : zipName.trim();
    }

    public String getFirstPrefix() {
        return firstPrefix;
    }

    public void setFirstPrefix(String firstPrefix) {
        this.firstPrefix = firstPrefix == null ? null : firstPrefix.trim();
    }

    public String getMiddlePrefix() {
        return middlePrefix;
    }

    public void setMiddlePrefix(String middlePrefix) {
        this.middlePrefix = middlePrefix == null ? null : middlePrefix.trim();
    }

    public String getLastPrefix() {
        return lastPrefix;
    }

    public void setLastPrefix(String lastPrefix) {
        this.lastPrefix = lastPrefix == null ? null : lastPrefix.trim();
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
}