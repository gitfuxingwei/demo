package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分页请求数据传输对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageRequestDTO {
    private Integer pageNum = 1;      // 页码，默认第一页
    private Integer pageSize = 10;    // 每页大小，默认10条

}
