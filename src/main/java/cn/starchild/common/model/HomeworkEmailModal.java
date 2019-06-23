package cn.starchild.common.model;

import javax.persistence.Table;
import java.util.Date;

@Table(name = "t_homework_email")
public class HomeworkEmailModal {
    private String id;

    private String homeworkId;

    private Date created;

    private byte status;

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

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }
}