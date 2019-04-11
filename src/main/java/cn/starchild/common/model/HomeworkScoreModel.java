package cn.starchild.common.model;

import javax.persistence.Table;
import java.util.Date;

@Table(name = "t_homework_score")
public class HomeworkScoreModel {
    private String id;

    private String homeworkSubmitId;

    private Integer score;

    private Date created;

    private Date modified;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getHomeworkSubmitId() {
        return homeworkSubmitId;
    }

    public void setHomeworkSubmitId(String homeworkSubmitId) {
        this.homeworkSubmitId = homeworkSubmitId == null ? null : homeworkSubmitId.trim();
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
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