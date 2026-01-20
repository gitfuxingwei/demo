# ApiFox 测试用例说明：用户分页查询功能

## 1. 接口概述
- **接口名称**：用户分页查询
- **接口路径**：GET /api/users/page
- **功能描述**：根据指定的页码和页面大小分页获取用户列表

## 2. 请求参数
| 参数名 | 类型 | 必填 | 默认值 | 描述 |
|--------|------|------|--------|------|
| pageNum | Integer | 否 | 1 | 页码，从1开始 |
| pageSize | Integer | 否 | 10 | 每页显示的记录数 |
| orderBy | String | 否 | - | 排序字段名 |
| orderDir | String | 否 | - | 排序方向，ASC(升序) 或 DESC(降序) |

## 3. 示例请求

### 3.1 获取第一页数据（默认）
```
GET http://localhost:8080/api/users/page
```

### 3.2 获取第二页数据，每页5条记录
```
GET http://localhost:8080/api/users/page?pageNum=2&pageSize=5
```

### 3.3 按创建时间降序排列
```
GET http://localhost:8080/api/users/page?pageNum=1&pageSize=10&orderBy=create_time&orderDir=DESC
```

### 3.4 按用户名升序排列
```
GET http://localhost:8080/api/users/page?pageNum=1&pageSize=10&orderBy=user_name&orderDir=ASC
```

## 4. 响应示例

### 4.1 成功响应
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "records": [
      {
        "userId": 1,
        "deptId": 1,
        "userName": "admin",
        "nickName": "管理员",
        "userType": "ADMIN",
        "email": "admin@example.com",
        "phonenumber": "13800138000",
        "sex": "0",
        "avatar": "/upload/avatar/admin.jpg",
        "status": "0",
        "loginIp": "192.168.1.100",
        "loginDate": "2024-01-01 10:00:00",
        "createTime": "2023-12-01 09:00:00",
        "updateTime": "2024-01-01 10:00:00",
        "remark": "系统管理员"
      }
    ],
    "total": 100,
    "pageNum": 1,
    "pageSize": 10,
    "totalPages": 10
  }
}
```

### 4.2 响应参数说明
| 参数名 | 类型 | 描述 |
|--------|------|------|
| code | Integer | 状态码，200表示成功 |
| message | String | 响应消息 |
| data | Object | 响应数据主体 |
| data.records | Array | 当前页的用户数据列表 |
| data.total | Long | 总记录数 |
| data.pageNum | Integer | 当前页码 |
| data.pageSize | Integer | 每页大小 |
| data.totalPages | Integer | 总页数 |

## 5. ApiFox导入步骤

1. 打开ApiFox工具
2. 点击"导入"按钮
3. 选择"导入Swagger/OpenAPI文件"
4. 上传 `user-pagination-api.json` 文件
5. 完成导入后即可看到用户分页查询接口

## 6. 测试步骤

1. 在ApiFox中找到 `/api/users/page` 接口
2. 选择GET方法
3. 设置请求参数：
   - pageNum: 1
   - pageSize: 10
   - orderBy: create_time
   - orderDir: DESC
4. 点击"发送"按钮
5. 查看响应结果

## 7. 注意事项

1. 确保服务正在运行在 `localhost:8080`
2. 如果未提供pageNum参数，默认为第1页
3. 如果未提供pageSize参数，默认为10条记录
4. orderBy和orderDir参数可选，用于排序控制
5. 响应中的UserVO对象不包含用户密码等敏感信息

## 8. 预期结果

成功调用后应返回包含以下信息的JSON响应：
- 当前页的用户数据列表
- 总记录数
- 当前页码
- 每页大小
- 总页数

这表明分页查询功能正常工作。