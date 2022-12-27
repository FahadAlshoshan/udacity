package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CredentialMapper {

    @Select("SELECT * FROM CREDENTIALS WHERE userid = #{userId}")
    List<Credential> getAllUsersCredentials(int userId);

    @Select("SELECT * FROM CREDENTIALS WHERE id = #{id}")
    Credential getCredentialById(int id);

    @Insert(
            "INSERT INTO CREDENTIALS (id, url, username, key, password, userid) VALUES(#{id}, #{url}, #{username}, #{key}, #{password}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Credential credential);

    @Update("UPDATE CREDENTIALS SET username = #{username}, password = #{password}, url = #{url} WHERE id = #{id}")
    void update(Credential credential);

    @Delete("DELETE FROM CREDENTIALS WHERE id = #{id}")
    void delete(int id);
}
