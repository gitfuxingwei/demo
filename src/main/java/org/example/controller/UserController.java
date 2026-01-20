package org.example.controller;

import org.example.dto.PasswordUpdateDTO;
import org.example.dto.UserDTO;
import org.example.mapper.UserMapperStruct;
import org.example.pojo.User;
import org.example.result.Result;
import org.example.service.UserService;
import org.example.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 获取所有用户
     * @return 用户列表
     */
    @GetMapping
    public Result<List<UserVO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        List<UserVO> userVOs = UserMapperStruct.INSTANCE.toVOListFromDTO(users);
        return Result.success(userVOs);
    }

    /**
     * 根据ID获取用户
     * @param id 用户ID
     * @return 用户信息
     */
    @GetMapping("/{id}")
    public Result<UserVO> getUserById(@PathVariable Integer id) {
        UserDTO user = userService.getUserById(id);
        if (user != null) {
            UserVO userVO = UserMapperStruct.INSTANCE.toVO(user);
            return Result.success(userVO);
        } else {
            return Result.error("用户不存在");
        }
    }

    /**
     * 创建新用户
     * @param user 用户信息
     * @return 操作结果
     */
    @PostMapping
    public Result<String> createUser(@RequestBody User user) {
        // 将实体转换为DTO
        UserDTO userDTO = UserMapperStruct.INSTANCE.toDTO(user);
        boolean success = userService.addUser(userDTO);
        if (success) {
            return Result.success("用户创建成功", String.valueOf(userDTO.getUserId()));
        } else {
            return Result.error("用户创建失败");
        }
    }

    /**
     * 更新用户信息
     * @param id 用户ID
     * @param user 用户信息
     * @return 操作结果ss
     */
    @PutMapping("/{id}")
    public Result<String> updateUser(@PathVariable Integer id, @RequestBody User user) {
        // 将实体转换为DTO，并确保ID正确
        UserDTO userDTO = UserMapperStruct.INSTANCE.toDTO(user);
        userDTO.setUserId(id);
        boolean success = userService.updateUser(userDTO);
        if (success) {
            return Result.success("用户更新成功", String.valueOf(id));
        } else {
            return Result.error("用户更新失败");
        }
    }

    /**
     * 删除用户
     * @param id 用户ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public Result<String> deleteUser(@PathVariable Integer id) {
        boolean success = userService.deleteUserById(id);
        if (success) {
            return Result.success("用户删除成功", String.valueOf(id));
        } else {
            return Result.error("用户删除失败");
        }
    }

    /**
     * 根据用户名搜索用户
     * @param name 用户名
     * @return 用户信息
     */
    @GetMapping("/search")
    public Result<UserVO> getUserByName(@RequestParam String name) {
        UserDTO user = userService.getUserByUserName(name);
        if (user != null) {
            UserVO userVO = UserMapperStruct.INSTANCE.toVO(user);
            return Result.success(userVO);
        } else {
            return Result.error("用户不存在");
        }
    }

    /**
     * 根据用户名更新用户密码
     * @param passwordUpdateDTO 密码更新请求对象
     * @return 操作结果
     */
    @PatchMapping("/password")
    public Result<String> updatePasswordByUsername(@RequestBody PasswordUpdateDTO passwordUpdateDTO) {
        try {
            boolean success = userService.updatePasswordByUsername(passwordUpdateDTO);
            if (success) {
                return Result.success("密码更新成功");
            } else {
                return Result.error("密码更新失败");
            }
        } catch (IllegalArgumentException e) {
            return Result.error(e.getMessage());
        }
    }
}
