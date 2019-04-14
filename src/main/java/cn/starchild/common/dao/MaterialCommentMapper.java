package cn.starchild.common.dao;

import cn.starchild.common.model.MaterialComment;

public interface MaterialCommentMapper {
    int deleteByPrimaryKey(String id);

    int insert(MaterialComment record);

    int insertSelective(MaterialComment record);

    MaterialComment selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(MaterialComment record);

    int updateByPrimaryKey(MaterialComment record);
}