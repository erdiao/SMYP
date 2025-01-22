# 用户管理接口文档

## 1. 创建用户

### 基本信息
- 接口URL：`/api/users`
- 请求方式：POST
- 接口描述：创建新用户

### 请求参数
| 参数名    | 类型   | 必填 | 描述        | 示例                      |
|-----------|--------|------|-------------|---------------------------|
| openid    | String | 是   | 微信用户ID  | wx_123456789              |
| unionid   | String | 否   | 微信unionid | unionid_123               |
| nickname  | String | 是   | 用户昵称    | 张三                      |
| avatarUrl | String | 是   | 头像URL     | https://example.com/avatar.jpg |
| gender    | Integer| 否   | 性别        | 1                         |
| country   | String | 否   | 国家        | 中国                      |
| province  | String | 否   | 省份        | 上海                      |
| city      | String | 否   | 城市        | 上海                      |
| language  | String | 否   | 语言        | zh_CN                     |
| totalGames| Integer| 否   | 总场次      | 0                         |
| totalWins | Integer| 否   | 胜场数      | 0                         |
| totalScore| Integer| 否   | 总得分      | 0                         |
| level     | Integer| 否   | 用户等级    | 1                         |
| status    | Integer| 否   | 状态        | 1                         |

### 请求示例
```json
{
    "openid": "wx_123456789",
    "unionid": "unionid_123",
    "nickname": "张三",
    "avatarUrl": "https://example.com/avatar.jpg",
    "gender": 1,
    "country": "中国",
    "province": "上海",
    "city": "上海",
    "language": "zh_CN",
    "totalGames": 0,
    "totalWins": 0,
    "totalScore": 0,
    "level": 1,
    "status": 1
}
```

### 响应参数
| 参数名  | 类型    | 描述     |
|---------|---------|----------|
| code    | Integer | 状态码   |
| message | String  | 响应消息 |
| data    | Object  | 用户信息 |

### 响应示例
```json
{
    "code": 200,
    "message": "success",
    "data": {
        "openid": "wx_123456789",
        "unionid": "unionid_123",
        "nickname": "张三",
        "avatarUrl": "https://example.com/avatar.jpg",
        "gender": 1,
        "country": "中国",
        "province": "上海",
        "city": "上海",
        "language": "zh_CN",
        "totalGames": 0,
      
        "totalWins": 0,
        "totalScore": 0,
        "totalTrainingGames": 0,
        "level": 1,
        "status": 1
    }
}
```

## 2. 获取用户信息

### 基本信息
- 接口URL：`/api/profile/info`
- 请求方式：POST
- 接口描述：根据 openId 获取用户信息

### 请求参数
| 参数名 | 类型   | 必填 | 描述     | 示例        |
|--------|--------|------|----------|-------------|
| openId | String | 是   | 用户openId | wx_123456789 |

### 请求示例
```json
{
    "openId": "wx_123456789"
}
```

### 响应参数
| 参数名  | 类型    | 描述     |
|---------|---------|----------|
| code    | Integer | 状态码   |
| message | String  | 响应消息 |
| data    | Object  | 用户信息 |

### 响应示例
```json
{
    "code": 200,
    "message": "success",
    "data": {
        "openid": "wx_123456789",
        "unionid": "unionid_123",
        "nickname": "张三",
        "avatarUrl": "https://example.com/avatar.jpg",
        "gender": 1,
        "country": "中国",
        "province": "上海",
        "city": "上海",
        "language": "zh_CN",
        "totalGames": 0,
        "totalWins": 0,
        "totalScore": 0,
        "totalTrainingGames": 0,
        "level": 1,
        "status": 1
    }
}
```

## 3. 更新用户信息

### 基本信息
- 接口URL：`/api/profile/update`
- 请求方式：POST
- 接口描述：更新用户信息

### 请求参数
| 参数名    | 类型   | 必填 | 描述        | 示例                      |
|-----------|--------|------|-------------|---------------------------|
| openid    | String | 是   | 微信用户ID  | wx_123456789              |
| unionid   | String | 否   | 微信unionid | unionid_123               |
| nickname  | String | 是   | 用户昵称    | 张三                      |
| avatarUrl | String | 是   | 头像URL     | https://example.com/avatar.jpg |
| gender    | Integer| 否   | 性别        | 1                         |
| country   | String | 否   | 国家        | 中国                      |
| province  | String | 否   | 省份        | 上海                      |
| city      | String | 否   | 城市        | 上海                      |
| language  | String | 否   | 语言        | zh_CN                     |

### 请求示例
```json
{
    "openid": "wx_123456789",
    "unionid": "unionid_123",
    "nickname": "张三",
    "avatarUrl": "https://example.com/avatar.jpg",
    "gender": 1,
    "country": "中国",
    "province": "上海",
    "city": "上海",
    "language": "zh_CN"
}
```

### 响应示例
```json
{
    "code": 200,
    "message": "success",
    "data": {
        "openid": "wx_123456789",
        "unionid": "unionid_123",
        "nickname": "张三",
        "avatarUrl": "https://example.com/avatar.jpg",
        "gender": 1,
        "country": "中国",
        "province": "上海",
        "city": "上海",
        "language": "zh_CN"
    }
}
```

## 4. 微信登录获取 OpenID

### 基本信息
- 接口URL：`/api/wx/login`
- 请求方式：POST
- 接口描述：通过微信登录 code 获取用户 openid

### 请求参数
| 参数名 | 类型   | 必填 | 描述        | 示例        |
|--------|--------|------|-------------|-------------|
| code   | String | 是   | 微信登录code | 0f1H01ml2LwrIe4OScml2Ulsc61H01mz |

### 请求示例
```json
{
    "code": "0f1H01ml2LwrIe4OScml2Ulsc61H01mz"
}
```

### 响应参数
| 参数名  | 类型    | 描述     |
|---------|---------|----------|
| code    | Integer | 状态码   |
| message | String  | 响应消息 |
| data    | Object  | 响应数据 |

### 响应示例
```json
{
    "code": 200,
    "message": "success",
    "data": {
        "openid": "wx_123456789"
    }
}
```

### 错误码说明
| 状态码 | 说明           | 示例响应 |
|--------|----------------|----------|
| 200    | 成功          | {"code":200,"message":"success","data":{...}} |
| 400    | 参数错误      | {"code":400,"message":"code不能为空"} |
| 500    | 服务器错误    | {"code":500,"message":"微信登录失败"} |

## 5. 保存用户重要信息

### 基本信息
- 接口URL：`/api/profile/saveImportantInfo`
- 请求方式：POST
- 接口描述：保存用户的重要信息（昵称、头像、性别）

### 请求参数
| 参数名    | 类型    | 必填 | 描述        | 示例                      |
|-----------|---------|------|-------------|---------------------------|
| openId    | String  | 是   | 用户openId  | wx_123456789              |
| nickname  | String  | 是   | 用户昵称    | 张三                      |
| avatarUrl | String  | 是   | 头像URL     | https://example.com/avatar.jpg |
| gender    | Integer | 是   | 性别        | 1（0-未知，1-男性，2-女性）|

### 请求示例
```json
{
    "openId": "wx_123456789",
    "nickname": "张三",
    "avatarUrl": "https://example.com/avatar.jpg",
    "gender": 1
}
```

### 响应参数
| 参数名  | 类型    | 描述     |
|---------|---------|----------|
| code    | Integer | 状态码   |
| message | String  | 响应消息 |
| data    | Object  | 用户信息 |

### 响应示例
```json
{
    "code": 200,
    "message": "success",
    "data": {
        "id": 1,
        "openid": "wx_123456789",
        "nickname": "张三",
        "avatarUrl": "https://example.com/avatar.jpg",
        "gender": 1,
        "country": "中国",
        "province": "上海",
        "city": "上海",
        "language": "zh_CN",
        "totalGames": 0,
        "totalWins": 0,
        "totalScore": 0,
        "level": 1,
        "status": 1
    }
}
```

### 错误码说明
| 状态码 | 说明           | 示例响应 |
|--------|----------------|----------|
| 200    | 成功          | {"code":200,"message":"success","data":{...}} |
| 400    | 参数错误      | {"code":400,"message":"openId不能为空"} |
| 404    | 用户不存在    | {"code":404,"message":"用户不存在"} |
| 500    | 服务器错误    | {"code":500,"message":"保存用户重要信息失败"} |

## 6. 获取排行榜

### 基本信息
- 接口URL：`/api/profile/leaderboard`
- 请求方式：POST
- 接口描述：获取用户排行榜信息，按等级和总分倒序排序

### 请求参数
| 参数名    | 类型    | 必填 | 描述        | 示例  |
|-----------|---------|------|-------------|-------|
| pageNum   | Integer | 是   | 页码，从1开始 | 1     |
| pageSize  | Integer | 是   | 每页数量    | 20    |

### 请求示例
```json
{
    "pageNum": 1,
    "pageSize": 20
}
```

### 响应参数
| 参数名  | 类型    | 描述     |
|---------|---------|----------|
| code    | Integer | 状态码   |
| message | String  | 响应消息 |
| data    | Array   | 排行榜数据 |

#### data 数组中的对象结构
| 参数名     | 类型    | 描述     |
|------------|---------|----------|
| id         | Long    | 用户ID   |
| openid     | String  | 用户openid |
| nickname   | String  | 用户昵称 |
| avatarUrl  | String  | 头像URL  |
| totalScore | Integer | 总分数   |
| totalWins  | Integer | 总胜场   |
| totalGames | Integer | 总场数   |
| level      | Integer | 等级     |

### 响应示例
```json
{
    "code": 200,
    "message": "success",
    "data": [
        {
            "id": 1,
            "openid": "wx_123456789",
            "nickname": "张三",
            "avatarUrl": "https://example.com/avatar1.jpg",
            "totalScore": 1000,
            "totalWins": 10,
            "totalGames": 20,
            "level": 5
        },
        {
            "id": 2,
            "openid": "wx_234567890",
            "nickname": "李四",
            "avatarUrl": "https://example.com/avatar2.jpg",
            "totalScore": 800,
            "totalWins": 8,
            "totalGames": 15,
            "level": 4
        }
    ]
}
```

### 错误码说明
| 状态码 | 说明           | 示例响应 |
|--------|----------------|----------|
| 200    | 成功          | {"code":200,"message":"success","data":[...]} |
| 500    | 服务器错误    | {"code":500,"message":"获取排行榜失败"} |

## 7. 查询用户参与的对局

### 基本信息
- 接口URL：`/api/matches/list`
- 请求方式：POST
- 接口描述：查询用户创建或参与的八人转比赛列表

### 请求参数
| 参数名    | 类型    | 必填 | 描述        | 示例  |
|-----------|---------|------|-------------|-------|
| openid    | String  | 是   | 用户openid  | wx_123456789 |
| pageNum   | Integer | 否   | 页码，从1开始 | 1     |
| pageSize  | Integer | 否   | 每页数量    | 10    |

### 请求示例
```json
{
    "openid": "wx_123456789",
    "pageNum": 1,
    "pageSize": 10
}
```

### 响应参数
| 参数名  | 类型    | 描述     |
|---------|---------|----------|
| code    | Integer | 状态码   |
| message | String  | 响应消息 |
| data    | Object  | 分页数据 |

#### data 对象结构
| 参数名        | 类型    | 描述           |
|---------------|---------|----------------|
| total         | Integer | 总记录数       |
| pages         | Integer | 总页数         |
| pageNum       | Integer | 当前页码       |
| pageSize      | Integer | 每页记录数     |
| list          | Array   | 比赛列表       |

#### list 数组中的对象结构
| 参数名          | 类型    | 描述           |
|----------------|---------|----------------|
| id             | Long    | 比赛ID         |
| matchName      | String  | 比赛名称       |
| status         | Integer | 比赛状态       |
| creatorId      | String  | 创建人ID       |
| creatorName    | String  | 创建人昵称     |
| totalRounds    | Integer | 总回合数       |
| finishedRounds | Integer | 已完成回合数   |
| isSettled      | Integer | 是否已结算     |
| userNames      | String  | 参与用户名称列表,使用｜分隔 |
| createdAt      | String  | 创建时间       |
| updatedAt      | String  | 更新时间       |

### 响应示例
```json
{
    "code": 200,
    "message": "success",
    "data": {
        "total": 100,
        "pages": 10,
        "pageNum": 1,
        "pageSize": 10,
        "list": [
            {
                "id": 1,
                "matchName": "周末八人转",
                "status": 1,
                "creatorId": "wx_123456789",
                "creatorName": "张三",
                "totalRounds": 10,
                "finishedRounds": 10,
                "isSettled": 1,
                "userNames": "张三|李四|王五|赵六|钱七|孙八|周九|吴十",
                "createdAt": "2024-01-12 12:00:00",
                "updatedAt": "2024-01-12 15:00:00"
            }
        ]
    }
}
```

### 错误码说明
| 状态码 | 说明           | 示例响应 |
|--------|----------------|----------|
| 200    | 成功          | {"code":200,"message":"success","data":{...}} |
| 400    | 参数错误      | {"code":400,"message":"openid不能为空"} |
| 500    | 服务器错误    | {"code":500,"message":"查询比赛列表失败"} |