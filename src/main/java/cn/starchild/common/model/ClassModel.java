package cn.starchild.common.model;

import org.apache.ibatis.type.Alias;
import org.apache.ibatis.type.MappedTypes;

import javax.persistence.Table;
import java.util.Date;

@Alias(value = "Class")
@Table(name = "t_class")
public class ClassModel {
    private String id;

    private String name;

    private String teacherId;

    private String invitationCode;

    private Boolean peopleMaximum;

    private Boolean status;

    private Boolean topping;

    private Date created;

    private Date modified;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId == null ? null : teacherId.trim();
    }

    public String getInvitationCode() {
        return invitationCode;
    }

    public void setInvitationCode(String invitationCode) {
        this.invitationCode = invitationCode == null ? null : invitationCode.trim();
    }

    public Boolean getPeopleMaximum() {
        return peopleMaximum;
    }

    public void setPeopleMaximum(Boolean peopleMaximum) {
        this.peopleMaximum = peopleMaximum;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Boolean getTopping() {
        return topping;
    }

    public void setTopping(Boolean topping) {
        this.topping = topping;
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