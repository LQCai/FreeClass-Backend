package cn.starchild.common.model;

import javax.persistence.Table;
import java.util.Date;

@Table(name = "t_homework_comment")
public class HomeworkCommentModel {
    private String id;

    private String homeworkId;

    private String userId;

    private Boolean role;

    private String comment;

    private Boolean status;

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public Boolean getRole() {
        return role;
    }

    public void setRole(Boolean role) {
        this.role = role;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment == null ? null : comment.trim();
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
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
}