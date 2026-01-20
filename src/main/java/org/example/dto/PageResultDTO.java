package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 分页结果数据传输对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResultDTO<T> {
    private List<T> records;    // 当前页数据
    private Long total;         // 总记录数
    private Integer pageNum;    // 当前页码
    private Integer pageSize;   // 每页大小
    private Integer totalPages; // 总页数

    public PageResultDTO(List<T> records, Long total, Integer pageNum, Integer pageSize) {
        this.records = records;
        this.total = total;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.totalPages = (int) Math.ceil((double) total / pageSize);
    }
}