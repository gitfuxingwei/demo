package org.example.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
     private Integer user_id;
     private Integer dept_id;
     private String user_name;
     private String nick_name;
     private Integer user_type;         //0 系统用户 1 管理员 2 访客
     private String email;
     private String phonenumber;
     private Integer sex;                //0 男 1女
     private String avatar;            //头像
     private String user_password;      //密码
     private String status;            //0 正常 1 停用
     private String del_flag;           //删除标志 0 正常 1 删除
      private String login_ip;          //登录IP
      private String login_date;        //最后登录时间
      private String create_by;         //创建者
      private String create_time;       //创建时间
      private String update_by;         //更新者
      private String update_time;       //更新时间
      private String remark;            //备注
}
