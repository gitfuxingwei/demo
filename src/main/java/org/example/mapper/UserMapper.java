package org.example.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import org.example.dto.PageRequestDTO;

import org.example.pojo.User;

import java.util.List;

@Mapper
public interface UserMapper {
    /**
     * 根据id查询用户
     * @param id
     * @return
     */
    User selectUserById(@Param("id") Integer id);
    /*
    * 根据id更新用户
     */
    boolean updateUserById(@Param("user") User user);
    /*
    * 根据id删除用户
     */
    boolean deleteUserById(@Param("id") Integer id);
    /*
    * 添加用户
     */
    boolean insertUser(@Param("user") User user);
    /*
    * 查询所有用户
     */
    List<User> selectAllUser();
    /*
    * 根据用户名查询用户
     */
    User selectUserByName(@Param("name") String name);
    /*
    * 根据用户名查询用户列表
     */
    List<User> selectUserListByName(@Param("name") String name);
    /*
    * 根据条件查询用户
     */
    List<User> selectUserByCondition(@Param("user") User user);

    /*
    * 批量删除用户
     */
    boolean deleteUsers(@Param("ids") List<Integer> ids);
    /*
    * 批量添加用户
     */
    boolean insertUsers(@Param("users") List<User> users);
    /*
    * 批量更新用户
     */
    int updateUsers(@Param("users") List<User> users);


    /**
     * 根据用户名列表查询已存在的用户名
     */
    List<String> selectUsernames(@Param("usernames") List<String> usernames);

    /**
     * 根据手机号列表查询已存在的手机号
     */
    List<String> selectPhoneNumbers(@Param("phoneNumbers") List<String> phoneNumbers);


}
