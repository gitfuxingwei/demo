package org.example.controller;

import org.example.dto.PasswordUpdateDTO;
import org.example.dto.UserDTO;
import org.example.mapper.UserMapperStruct;
import org.example.pojo.User;
import org.example.result.Result;
import org.example.service.UserService;
<<<<<<< HEAD
import org.example.utils.ExcelUtils;
import org.example.utils.UserDataListener;
import org.example.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
=======
import org.example.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

>>>>>>> b6d44132eac700221db2fd8745278eb9753c5ee9
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

<<<<<<< HEAD


=======
>>>>>>> b6d44132eac700221db2fd8745278eb9753c5ee9
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
<<<<<<< HEAD

    /**
     * 分页查询用户
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 分页用户列表
     */
    @GetMapping("/page")
    public Result<org.example.dto.PageResultDTO<UserVO>> getUserByPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize)
            {

        org.example.dto.PageRequestDTO pageRequest = new org.example.dto.PageRequestDTO(pageNum, pageSize);
        org.example.dto.PageResultDTO<UserVO> result = userService.getUserVOByPage(pageRequest);
        return Result.success(result);
    }
    /**
     * 导出用户数据为Excel
     * @param response HttpServletResponse
     */
    @GetMapping(value = "/export", produces = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public void exportUsers(HttpServletResponse response) {
        try {
            // 获取所有用户数据用于导出
            List<UserDTO> userDTOs = userService.getAllUsersForExport();

            // 将DTO转换为VO
            List<UserVO> userVOs = UserMapperStruct.INSTANCE.toVOListFromDTO(userDTOs);

            // 使用EasyExcel导出
            ExcelUtils.exportExcel(response, userVOs, "用户", UserVO.class);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    /**
     * 批量导入用户（通过Excel文件）
     * @param file 上传的Excel文件
     * @return 操作结果
     */
    @PostMapping(value = "/import", consumes = "multipart/form-data")
    public Result<String> importUsers(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return Result.error("上传的文件不能为空");
        }

        try {
            // 使用监听器读取并校验Excel数据
            UserDataListener listener = new UserDataListener();
            com.alibaba.excel.EasyExcel.read(file.getInputStream(), UserDTO.class, listener).sheet().doRead();

            // 检查校验过程中是否有错误
            if (listener.hasErrors()) {
                StringBuilder errorMsg = new StringBuilder("导入失败，发现以下错误：\n");
                for (String error : listener.getErrorMessages()) {
                    errorMsg.append(error).append("\n");
                }
                return Result.error(errorMsg.toString());
            }

            List<UserDTO> userDTOs = listener.getUserDataList();

            if (userDTOs.isEmpty()) {
                return Result.error("Excel文件中没有有效的用户数据");
            }

            // 检查数据库中是否存在重复的用户名或手机号
            List<String> duplicateUsernames = userService.checkUsernames(userDTOs);
            List<String> duplicatePhoneNumbers = userService.checkPhoneNumbers(userDTOs);

            if (!duplicateUsernames.isEmpty() || !duplicatePhoneNumbers.isEmpty()) {
                StringBuilder errorMsg = new StringBuilder("导入失败，发现重复数据：\n");
                if (!duplicateUsernames.isEmpty()) {
                    errorMsg.append("重复的用户名：").append(String.join(", ", duplicateUsernames)).append("\n");
                }
                if (!duplicatePhoneNumbers.isEmpty()) {
                    errorMsg.append("重复的手机号：").append(String.join(", ", duplicatePhoneNumbers)).append("\n");
                }
                return Result.error(errorMsg.toString());
            }

            boolean success = userService.insertUsers(userDTOs);
            if (success) {
                return Result.success("成功导入 " + userDTOs.size() + " 条用户数据");
            } else {
                return Result.error("导入失败");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error("读取文件失败: " + e.getMessage());
        }
    }

    /**
     * 批量导入用户（通过JSON）
     * @param users 用户列表
     * @return 操作结果
     */
    @PostMapping(value = "/import/json", consumes = "application/json")
    public Result<String> importUsersJson(@RequestBody List<User> users) {
        List<UserDTO> userDTOs = UserMapperStruct.INSTANCE.toDTOList(users);
        boolean success = userService.insertUsers(userDTOs);
        if (success) {
            return Result.success("成功导入 " + userDTOs.size() + " 条用户数据");
        } else {
            return Result.error("导入失败");
        }
    }
=======
>>>>>>> b6d44132eac700221db2fd8745278eb9753c5ee9
}
