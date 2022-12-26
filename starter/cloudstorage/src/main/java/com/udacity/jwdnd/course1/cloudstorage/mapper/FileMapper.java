package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileMapper {
    @Select("SELECT id, name FROM FILES WHERE userid = #{userId}")
    List<File> getAllUsersFiles(@Param("userId") Integer userId);

    @Select("Select id FROM FILES WHERE name = #{fileName}")
    File getFileByName(@Param("fileName") String fileName);

    @Select("Select * FROM FILES WHERE id = #{id}")
    File getFileById(@Param("id") int id);

    @Insert("INSERT INTO FILES (id, name, contenttype, size, data, userid) " +
            "VALUES(#{id}, #{name}, #{contentType}, #{size}, #{data}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(File file);

    @Delete("DELETE FROM FILES WHERE id = #{id}")
    void delete(int id);
}
