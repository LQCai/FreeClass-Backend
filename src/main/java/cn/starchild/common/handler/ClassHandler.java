package cn.starchild.common.handler;

import cn.starchild.common.model.ClassModel;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


@MappedTypes(value = {ClassModel.class})
@MappedJdbcTypes(value = JdbcType.VARCHAR, includeNullJdbcType = true)
public class ClassHandler implements TypeHandler<ClassModel> {
    @Override
    public void setParameter(PreparedStatement preparedStatement, int i, ClassModel classModel, JdbcType jdbcType) throws SQLException {

    }

    @Override
    public ClassModel getResult(ResultSet resultSet, String s) throws SQLException {
        return null;
    }

    @Override
    public ClassModel getResult(ResultSet resultSet, int i) throws SQLException {
        return null;
    }

    @Override
    public ClassModel getResult(CallableStatement callableStatement, int i) throws SQLException {
        return null;
    }
}
