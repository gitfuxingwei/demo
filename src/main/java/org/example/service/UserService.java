package org.example.service;

import org.example.dto.PasswordUpdateDTO;
import org.example.dto.UserDTO;
import org.example.vo.UserVO;

import java.util.List;

public interface UserService {
    UserDTO getUserById(Integer id);

    List<UserDTO> getAllUsers();

    List<UserDTO> getUsersByCondition(UserDTO user);

    boolean addUser(UserDTO user);

    boolean updateUser(UserDTO user);

    boolean deleteUserById(Integer id);

    boolean batchDeleteUsers(List<Integer> ids);

    UserDTO getUserByUserName(String userName);

    boolean insertUsers(List<UserDTO> users);

    int updateUsers(List<UserDTO> users);

    // 添加返回VO的方法
    UserVO getUserVOById(Integer id);

    List<UserVO> getAllUserVOs();

    UserVO getUserVOByUserName(String userName);
    
    // 密码修改方法 - 使用DTO封装参数
    boolean updatePasswordByUsername(PasswordUpdateDTO passwordUpdateDTO);
}