package org.example;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.example.dto.UserDTO;
import org.junit.Test;

import java.util.*;

public class easyexcel {

    @Getter
    @Setter
    @EqualsAndHashCode
    public class DemoData {
        @ExcelProperty("字符串标题")
        private String string;
        @ExcelProperty("日期标题")
        private Date date;
        @ExcelProperty("数字标题")
        private Double doubleData;

    }
    @Test
    public void test() {
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
        // 这里默认读取第一个sheet
        DemoData demoData = new DemoData();
        String fileName = "C:\\Users\\Administrator\\Desktop\\JSP_SSM_BookStore-master\\simpleWrite" + System.currentTimeMillis() + ".xlsx";
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        // 如果这里想使用03 则 传入excelType参数即可
        EasyExcel.write(fileName, DemoData.class).sheet("模板").doWrite(Collections.singletonList(demoData.getDate()));
    }

    @Test
    public void generateTestData() {
        // 生成1000条用户测试数据
        List<UserDTO> userDTOs = new ArrayList<>();

        Random random = new Random();
        String[] names = {"张三", "李四", "王五", "赵六", "钱七", "孙八", "周九", "吴十", "郑一", "刘二"};
        String[] emails = {"@gmail.com", "@yahoo.com", "@outlook.com", "@qq.com", "@163.com", "@126.com"};
        String[] userTypes = {"USER", "ADMIN", "GUEST"};
        String[] sexes = {"0", "1"}; // 0男 1女
        String[] statuses = {"0", "1"}; // 0正常 1停用

        for (int i = 1; i <= 1000; i++) {
            UserDTO user = new UserDTO();
            user.setUserId(i);
            user.setDeptId(random.nextInt(5) + 1); // 部门ID 1-5
            user.setUserName("user" + i);
            user.setNickName(names[random.nextInt(names.length)] + i);
            user.setUserType(Integer.valueOf(userTypes[random.nextInt(userTypes.length)]));

            String email = "user" + i + emails[random.nextInt(emails.length)];
            user.setEmail(email);

            // 生成随机手机号，以1开头的11位数字
            String phoneNumber = "1" + String.format("%010d", random.nextInt(1000000000));
            user.setPhonenumber(phoneNumber);

            user.setSex(sexes[random.nextInt(sexes.length)]);
            user.setAvatar("");
            user.setUserPassword("password123");
            user.setStatus(statuses[random.nextInt(statuses.length)]);
            user.setDelFlag("0");
            user.setLoginIp("192.168.1." + (i % 254));
            user.setLoginDate("2024-" + String.format("%02d", (random.nextInt(12) + 1)) + "-" + String.format("%02d", (random.nextInt(28) + 1)));
            user.setCreateBy("system");
            user.setCreateTime("2024-" + String.format("%02d", (random.nextInt(12) + 1)) + "-" + String.format("%02d", (random.nextInt(28) + 1)));
            user.setUpdateBy("system");
            user.setUpdateTime("2024-" + String.format("%02d", (random.nextInt(12) + 1)) + "-" + String.format("%02d", (random.nextInt(28) + 1)));
            user.setRemark("测试用户" + i);

            userDTOs.add(user);
        }

        // 导出测试数据到Excel
        String exportFileName = "user_test_data_1000_rows_" + System.currentTimeMillis() + ".xlsx";
        try {
            EasyExcel.write(exportFileName, UserDTO.class).sheet("用户测试数据").doWrite(userDTOs);
            System.out.println("Excel文件已生成：" + exportFileName);
            System.out.println("包含 " + userDTOs.size() + " 条用户数据");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
