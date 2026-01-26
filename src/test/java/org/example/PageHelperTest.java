package org.example;

import org.example.dto.PageRequestDTO;
import org.example.dto.PageResultDTO;
import org.example.service.UserService;
import org.example.vo.UserVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class PageHelperTest {

    @Autowired
    private UserService userService;

    @Test
    public void testPageHelperPagination() {
        // 测试分页功能
        PageRequestDTO pageRequest = new PageRequestDTO();
        pageRequest.setPageNum(1);
        pageRequest.setPageSize(5);

        PageResultDTO<UserVO> result = userService.getUserVOByPage(pageRequest);

        System.out.println("总记录数：" + result.getTotal());
        System.out.println("总页数：" + result.getTotalPages());
        System.out.println("当前页：" + result.getPageNum());
        System.out.println("每页大小：" + result.getPageSize());
        System.out.println("用户列表大小：" + result.getRecords().size());

        // 遍历显示第一页的用户信息
        for (UserVO user : result.getRecords()) {
            System.out.println("用户ID: " + user.getUserId() + ", 用户名: " + user.getUserName());
        }
    }

    @Test
    public void testPageHelperSecondPage() {
        // 测试第二页
        PageRequestDTO pageRequest = new PageRequestDTO();
        pageRequest.setPageNum(2);
        pageRequest.setPageSize(5);

        PageResultDTO<UserVO> result = userService.getUserVOByPage(pageRequest);

        System.out.println("第二页 - 总记录数：" + result.getTotal());
        System.out.println("第二页 - 总页数：" + result.getTotalPages());
        System.out.println("第二页 - 当前页：" + result.getPageNum());
        System.out.println("第二页 - 每页大小：" + result.getPageSize());
        System.out.println("第二页 - 用户列表大小：" + result.getRecords().size());

        // 遍历显示第二页的用户信息
        for (UserVO user : result.getRecords()) {
            System.out.println("用户ID: " + user.getUserId() + ", 用户名: " + user.getUserName());
        }
    }
}