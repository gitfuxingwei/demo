# 用户分页查询功能测试指南

## 1. 启动应用程序
在项目根目录执行以下命令启动应用：
```bash
mvn spring-boot:run
```

## 2. 测试分页查询API

### 2.1 使用curl命令测试

#### 测试默认分页（第1页，每页10条）
```bash
curl -X GET "http://localhost:8080/api/users/page" \
  -H "Content-Type: application/json"
```

#### 测试指定页码和页面大小
```bash
curl -X GET "http://localhost:8080/api/users/page?pageNum=1&pageSize=5" \
  -H "Content-Type: application/json"
```

#### 测试带排序的分页查询
```bash
curl -X GET "http://localhost:8080/api/users/page?pageNum=1&pageSize=10&orderBy=create_time&orderDir=DESC" \
  -H "Content-Type: application/json"
```

### 2.2 使用浏览器测试
直接在浏览器中访问以下URL：
- `http://localhost:8080/api/users/page` (默认分页)
- `http://localhost:8080/api/users/page?pageNum=1&pageSize=5` (每页5条)
- `http://localhost:8080/api/users/page?pageNum=2&pageSize=10` (第2页)

## 3. ApiFox导入步骤

### 3.1 导入OpenAPI规范
1. 打开ApiFox工具
2. 点击"导入"按钮
3. 选择"导入Swagger/OpenAPI文件"
4. 选择文件：`src/main/resources/api-docs/user-pagination-api.json`
5. 点击"确定"完成导入

### 3.2 导入Postman集合（兼容ApiFox）
1. 打开ApiFox工具
2. 点击"导入"按钮
3. 选择"导入Postman Collection"
4. 选择文件：`src/main/resources/api-docs/user-pagination-apifox-collection.json`
5. 点击"确定"完成导入

## 4. 预期响应格式

成功响应应该类似如下格式：
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "records": [
      {
        "userId": 1,
        "userName": "admin",
        "nickName": "管理员",
        "email": "admin@example.com",
        "createTime": "2023-12-01 09:00:00",
        "...": "其他用户属性"
      }
    ],
    "total": 100,
    "pageNum": 1,
    "pageSize": 10,
    "totalPages": 10
  }
}
```

## 5. 常见问题排查

### 5.1 404错误
- 检查应用是否已启动
- 确认URL路径是否正确：`/api/users/page`

### 5.2 参数无效
- 确认pageNum和pageSize为正整数
- 确认orderBy字段存在于数据库表中
- 确认orderDir为ASC或DESC（不区分大小写）

### 5.3 数据库无数据
- 检查数据库是否有用户数据
- 如需测试数据，可先调用POST /api/users接口添加用户

## 6. ApiFox测试配置建议

### 6.1 环境变量配置
在ApiFox中设置以下环境变量：
- `baseUrl`: `http://localhost:8080`
- `pageNum`: `1`
- `pageSize`: `10`

### 6.2 预设参数
- 默认分页：pageNum=1, pageSize=10
- 小页面：pageNum=1, pageSize=5
- 大页面：pageNum=1, pageSize=20
- 排序：orderBy=create_time, orderDir=DESC

按照以上步骤，您就可以在ApiFox中成功测试用户分页查询功能了。