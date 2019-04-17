package cn.starchild.common.model;

import org.apache.ibatis.type.Alias;

import javax.persistence.Table;
import java.util.Date;

@Alias(value = "Class")
@Table(name = "t_class")
public class ClassModel {
    private String id;

    private String name;

    private String teacherId;

    private String invitationCode;

    private byte peopleMaximum;

    private byte status;

    private byte topping;

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

    public byte getPeopleMaximum() {
        return peopleMaximum;
    }

    public void setPeopleMaximum(byte peopleMaximum) {
        this.peopleMaximum = peopleMaximum;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public byte getTopping() {
        return topping;
    }

    public void setTopping(byte topping) {
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