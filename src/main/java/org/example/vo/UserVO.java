package org.example.vo;

<<<<<<< HEAD
import com.alibaba.excel.annotation.ExcelProperty;
=======
>>>>>>> b6d44132eac700221db2fd8745278eb9753c5ee9
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户视图对象
 * 用于向前端展示用户信息，不包含敏感字段
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserVO {
<<<<<<< HEAD
    @ExcelProperty("用户ID")
    private Integer userId;

    @ExcelProperty("部门ID")
    private Integer deptId;

    @ExcelProperty("用户名")
    private String userName;

    @ExcelProperty("昵称")
    private String nickName;

    @ExcelProperty("用户类型")
    private Integer userType;

    @ExcelProperty("邮箱")
    private String email;

    @ExcelProperty("手机号")
    private String phonenumber;

    @ExcelProperty("性别")
    private String sex;                //0 男 1女

    @ExcelProperty("头像")
    private String avatar;             //头像

    @ExcelProperty("状态")
    private String status;             //0 正常 1 停用

    @ExcelProperty("登录IP")
    private String loginIp;            //登录IP

    @ExcelProperty("最后登录时间")
    private String loginDate;          //最后登录时间

    @ExcelProperty("创建时间")
    private String createTime;         //创建时间

    @ExcelProperty("更新时间")
    private String updateTime;         //更新时间

    @ExcelProperty("备注")
    private String remark;
}
=======
    private Integer userId;
    private Integer deptId;
    private String userName;
    private String nickName;
    private String userType;
    private String email;
    private String phonenumber;
    private String sex;                //0 男 1女
    private String avatar;             //头像
    private String status;             //0 正常 1 停用
    private String loginIp;            //登录IP
    private String loginDate;          //最后登录时间
    private String createTime;         //创建时间
    private String updateTime;         //更新时间
    private String remark;             //备注
}
>>>>>>> b6d44132eac700221db2fd8745278eb9753c5ee9
