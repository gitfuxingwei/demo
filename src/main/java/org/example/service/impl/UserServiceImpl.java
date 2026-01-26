package org.example.service.impl;


import org.example.dto.PageRequestDTO;
import org.example.dto.PageResultDTO;

import org.example.dto.PasswordUpdateDTO;
import org.example.dto.UserDTO;
import org.example.mapper.UserMapper;
import org.example.mapper.UserMapperStruct;
import org.example.pojo.User;
import org.example.service.UserService;
import org.example.utils.PasswordAttemptTracker;
import org.example.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * 用户服务实现类
 * 遾循分层架构：接收DTO -> 转换为PO -> 调用DAO(Mapper) -> 返回DTO/VO
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDTO getUserById(Integer id) {
        // 查询：从数据库获取PO -> 通过MapStruct转换为 -> DTO
        User user = userMapper.selectUserById(id);
        return UserMapperStruct.INSTANCE.toDTO(user);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        // 查询：从数据库获取List<PO> -> 通过MapStruct转换为 -> List<DTO>
        List<User> users = userMapper.selectAllUser();
        return UserMapperStruct.INSTANCE.toDTOList(users);
    }

    @Override
    public List<UserDTO> getUsersByCondition(UserDTO userDTO) {
        // 输入：DTO -> 转换为 -> PO -> 查询 -> 从数据库获取List<PO> -> 通过MapStruct转换为 -> List<DTO>
        User user = UserMapperStruct.INSTANCE.toEntity(userDTO);
        List<User> users = userMapper.selectUserByCondition(user);
        return UserMapperStruct.INSTANCE.toDTOList(users);
    }

    @Override
    public boolean addUser(UserDTO userDTO) {
        // 输入：DTO -> 转换为 -> PO -> 存储
        User user = UserMapperStruct.INSTANCE.toEntity(userDTO);
        boolean result = userMapper.insertUser(user);
        // 如果插入成功，更新DTO中的ID
        if (result && user.getUser_id() != null) {
            userDTO.setUserId(user.getUser_id());
        }
        return result;
    }

    @Override
    public boolean updateUser(UserDTO userDTO) {
        // 输入：DTO -> 转换为 -> PO -> 更新
        User user = UserMapperStruct.INSTANCE.toEntity(userDTO);
        return userMapper.updateUserById(user);
    }

    @Override
    public boolean deleteUserById(Integer id) {
        return userMapper.deleteUserById(id);
    }

    @Override
    public boolean batchDeleteUsers(List<Integer> ids) {
        return userMapper.deleteUsers(ids);
    }

    @Override
    public UserDTO getUserByUserName(String userName) {
        // 查询：从数据库获取PO -> 通过MapStruct转换为 -> DTO
        User user = userMapper.selectUserByName(userName);
        return UserMapperStruct.INSTANCE.toDTO(user);
    }

    @Override
    public boolean insertUsers(List<UserDTO> usersDTO) {
        // 批量输入：List<DTO> -> 转换为 -> List<PO> -> 批量存储
        List<User> users = UserMapperStruct.INSTANCE.toEntityList(usersDTO);
        boolean result = userMapper.insertUsers(users);
        // 如果插入成功，更新DTO中的ID
        if (result) {
            for (int i = 0; i < usersDTO.size() && i < users.size(); i++) {
                usersDTO.get(i).setUserId(users.get(i).getUser_id());
            }
        }
        return result;
    }

    @Override
    public int updateUsers(List<UserDTO> usersDTO) {
        // 批量输入：List<DTO> -> 转换为 -> List<PO> -> 批量更新
        List<User> users = UserMapperStruct.INSTANCE.toEntityList(usersDTO);
        return userMapper.updateUsers(users);
    }

    @Override
    public UserVO getUserVOById(Integer id) {
        // 查询：从数据库获取PO -> 通过MapStruct转换为 -> VO（不含敏感信息）
        User user = userMapper.selectUserById(id);
        return UserMapperStruct.INSTANCE.toVO(user);
    }

    @Override
    public List<UserVO> getAllUserVOs() {
        // 查询：从数据库获取List<PO> -> 通过MapStruct转换为 -> List<VO>（不含敏感信息）
        List<User> users = userMapper.selectAllUser();
        return UserMapperStruct.INSTANCE.toVOList(users);
    }

    @Override
    public UserVO getUserVOByUserName(String userName) {
        // 查询：从数据库获取PO -> 通过MapStruct转换为 -> VO（不含敏感信息）
        User user = userMapper.selectUserByName(userName);
        return UserMapperStruct.INSTANCE.toVO(user);
    }

    @Override
    public boolean updatePasswordByUsername(PasswordUpdateDTO passwordUpdateDTO) {
        String username = passwordUpdateDTO.getUsername();
        String oldPassword = passwordUpdateDTO.getOldPassword();
        String newPassword = passwordUpdateDTO.getNewPassword();

        // 检查用户是否被锁定
        if (PasswordAttemptTracker.isLocked(username)) {
            long remainingTime = PasswordAttemptTracker.getRemainingLockTime(username);
            long minutesLeft = (remainingTime / 1000 / 60) + 1; // 向上取整
            throw new IllegalArgumentException("密码错误尝试次数过多，账户已被锁定 " + minutesLeft + " 分钟");
        }

        // 验证新密码是否为空
        if (newPassword == null || newPassword.isEmpty()) {
            throw new IllegalArgumentException("新密码不能为空");
        }

        // 验证旧密码是否为空
        if (oldPassword == null || oldPassword.isEmpty()) {
            throw new IllegalArgumentException("旧密码不能为空");
        }

        // 根据用户名查询用户
        User user = userMapper.selectUserByName(username);

        if (user == null) {
            throw new IllegalArgumentException("用户不存在");
        }

        // 验证旧密码是否正确
        if (!oldPassword.equals(user.getUser_password())) {
            // 记录失败的尝试
            PasswordAttemptTracker.recordFailedAttempt(username);
            int attempts = PasswordAttemptTracker.getAttemptCount(username);
            int remainingAttempts = 3 - attempts;

            if (PasswordAttemptTracker.isLocked(username)) {
                long remainingTime = PasswordAttemptTracker.getRemainingLockTime(username);
                long minutesLeft = (remainingTime / 1000 / 60) + 1; // 向上取整
                throw new IllegalArgumentException("密码错误尝试次数过多，账户已被锁定 " + minutesLeft + " 分钟");
            } else {
                throw new IllegalArgumentException("旧密码不正确，还剩 " + remainingAttempts + " 次机会");
            }
        }

        // 如果旧密码正确，重置尝试计数
        PasswordAttemptTracker.resetAttempts(username);

        // 验证新旧密码是否相同
        if (newPassword.equals(oldPassword)) {
            throw new IllegalArgumentException("新旧密码不能相同");
        }

        // 更新密码
        user.setUser_password(newPassword);
        return userMapper.updateUserById(user);
    }



    @Override
    public PageResultDTO<UserVO> getUserVOByPage(PageRequestDTO pageRequest) {
        // 分页查询
        PageHelper.startPage(pageRequest.getPageNum(), pageRequest.getPageSize());

        // 执行查询
        List<User> users = userMapper.selectAllUser(); // 直接查询所有用户，PageHelper会自动应用分页

        // 获取分页信息
        PageInfo<User> pageInfo = new PageInfo<>(users);

        // 转换为VO列表
        List<UserVO> userVOs = UserMapperStruct.INSTANCE.toVOList(users);

        // 构建分页结果
        return new PageResultDTO<>(userVOs, pageInfo.getTotal(), pageInfo.getPageNum(), pageInfo.getPageSize(), pageInfo.getPages());
    }

    @Override
    public List<UserDTO> getAllUsersForExport() {
        // 查询所有用户用于Excel导出
        List<User> users = userMapper.selectAllUser();

        // 限制导出数量为最多1000条
//        if (users.size() > 1000) {
//            users = users.subList(0, 1000);
//        }

        return UserMapperStruct.INSTANCE.toDTOList(users);
    }

    @Override
    public List<String> checkUsernames(List<UserDTO> users) {
        // 提取待检查的用户名列表
        List<String> usernames = users.stream()
                .map(UserDTO::getUserName)
                .filter(username -> username != null && !username.trim().isEmpty())
                .distinct()
                .toList();

        if (usernames.isEmpty()) {
            return List.of();
        }

        // 检查数据库中是否已存在这些用户名
        List<String> duplicateUsernames = userMapper.selectUsernames(usernames);

        // 检查导入数据内部是否有重复用户名
        Set<String> seenUsernames = new HashSet<>();
        Set<String> duplicateInImport = new HashSet<>();
        for (UserDTO user : users) {
            if (user.getUserName() != null) {
                if (seenUsernames.contains(user.getUserName().toLowerCase())) {
                    duplicateInImport.add(user.getUserName());
                } else {
                    seenUsernames.add(user.getUserName().toLowerCase());
                }
            }
        }

        // 合并两个重复列表
        Set<String> allDuplicates = new HashSet<>(duplicateUsernames);
        allDuplicates.addAll(duplicateInImport);

        return new ArrayList<>(allDuplicates);
    }

    @Override
    public List<String> checkPhoneNumbers(List<UserDTO> users) {
        // 提取待检查的手机号列表
        List<String> phoneNumbers = users.stream()
                .map(UserDTO::getPhonenumber)
                .filter(phone -> phone != null && !phone.trim().isEmpty())
                .distinct()
                .toList();

        if (phoneNumbers.isEmpty()) {
            return List.of();
        }

        // 检查数据库中是否已存在这些手机号
        List<String> duplicatePhoneNumbers = userMapper.selectPhoneNumbers(phoneNumbers);

        // 检查导入数据内部是否有重复手机号
        Set<String> seenPhones = new HashSet<>();
        Set<String> duplicateInImport = new HashSet<>();
        for (UserDTO user : users) {
            if (user.getPhonenumber() != null) {
                if (seenPhones.contains(user.getPhonenumber())) {
                    duplicateInImport.add(user.getPhonenumber());
                } else {
                    seenPhones.add(user.getPhonenumber());
                }
            }
        }

        // 合并两个重复列表
        Set<String> allDuplicates = new HashSet<>(duplicatePhoneNumbers);
        allDuplicates.addAll(duplicateInImport);

        return new ArrayList<>(allDuplicates);
    }

}
