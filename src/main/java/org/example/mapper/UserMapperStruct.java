package org.example.mapper;

import org.example.dto.UserDTO;
import org.example.pojo.User;
import org.example.vo.UserVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapperStruct {

    UserMapperStruct INSTANCE = Mappers.getMapper(UserMapperStruct.class);

    // POJO  to DTO
    @Mapping(source = "user_id", target = "userId")
    @Mapping(source = "dept_id", target = "deptId")
    @Mapping(source = "user_name", target = "userName")
    @Mapping(source = "nick_name", target = "nickName")
    @Mapping(source = "user_type", target = "userType")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "phonenumber", target = "phonenumber")
    @Mapping(source = "sex", target = "sex")
    @Mapping(source = "avatar", target = "avatar")
    @Mapping(source = "user_password", target = "userPassword")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "del_flag", target = "delFlag")
    @Mapping(source = "login_ip", target = "loginIp")
    @Mapping(source = "login_date", target = "loginDate")
    @Mapping(source = "create_by", target = "createBy")
    @Mapping(source = "create_time", target = "createTime")
    @Mapping(source = "update_by", target = "updateBy")
    @Mapping(source = "update_time", target = "updateTime")
    @Mapping(source = "remark", target = "remark")
    UserDTO toDTO(User user);

    // DTO to POJO
    @Mapping(source = "userId", target = "user_id")
    @Mapping(source = "deptId", target = "dept_id")
    @Mapping(source = "userName", target = "user_name")
    @Mapping(source = "nickName", target = "nick_name")
    @Mapping(source = "userType", target = "user_type")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "phonenumber", target = "phonenumber")
    @Mapping(source = "sex", target = "sex")
    @Mapping(source = "avatar", target = "avatar")
    @Mapping(source = "userPassword", target = "user_password")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "delFlag", target = "del_flag")
    @Mapping(source = "loginIp", target = "login_ip")
    @Mapping(source = "loginDate", target = "login_date")
    @Mapping(source = "createBy", target = "create_by")
    @Mapping(source = "createTime", target = "create_time")
    @Mapping(source = "updateBy", target = "update_by")
    @Mapping(source = "updateTime", target = "update_time")
    @Mapping(source = "remark", target = "remark")
    User toEntity(UserDTO userDTO);

    // POJO  to VO
    @Mapping(source = "user_id", target = "userId")
    @Mapping(source = "dept_id", target = "deptId")
    @Mapping(source = "user_name", target = "userName")
    @Mapping(source = "nick_name", target = "nickName")
    @Mapping(source = "user_type", target = "userType")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "phonenumber", target = "phonenumber")
    @Mapping(source = "sex", target = "sex")
    @Mapping(source = "avatar", target = "avatar")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "login_ip", target = "loginIp")
    @Mapping(source = "login_date", target = "loginDate")
    @Mapping(source = "create_time", target = "createTime")
    @Mapping(source = "update_time", target = "updateTime")
    @Mapping(source = "remark", target = "remark")
    UserVO toVO(User user);

    // DTO to VO
    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "deptId", target = "deptId")
    @Mapping(source = "userName", target = "userName")
    @Mapping(source = "nickName", target = "nickName")
    @Mapping(source = "userType", target = "userType")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "phonenumber", target = "phonenumber")
    @Mapping(source = "sex", target = "sex")
    @Mapping(source = "avatar", target = "avatar")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "loginIp", target = "loginIp")
    @Mapping(source = "loginDate", target = "loginDate")
    @Mapping(source = "createTime", target = "createTime")
    @Mapping(source = "updateTime", target = "updateTime")
    @Mapping(source = "remark", target = "remark")
    UserVO toVO(UserDTO userDTO);

    // List转换
    List<UserDTO> toDTOList(List<User> userList);

    List<User> toEntityList(List<UserDTO> userDTOList);

    List<UserVO> toVOList(List<User> userList);

    List<UserVO> toVOListFromDTO(List<UserDTO> userDTOList);
<<<<<<< HEAD

=======
>>>>>>> b6d44132eac700221db2fd8745278eb9753c5ee9
}
