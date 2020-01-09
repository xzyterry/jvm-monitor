# dashboard-service文档
```
生产前缀: https://common.dcgstd.cn/dashboard-service
开发前缀: http://127.0.0.1/dashboard-service
```

## 1.查询所有主机-服务名 列表
- 请求地址: `/api/data/hostList`
- 请求方式: `get`
- 请求返回值:
```json
{
  "resultCode": 0,
  "message": null,
  "data": [{
    "hostName": "",
    "serviceName": ""
  }]
}
```

## 2.查询jstack状态
- 请求地址: `/api/data/jstack`
- 请求方式: `post`
- 请求参数: 
```json
{
  "dateStr": "",
  "minStr": "",
  "hostName": "",
  "serviceName": ""
}
```
- 请求返回值:
```json
{
  "resultCode": 0,
  "message": null,
  "data": [{
    "dateStr": "",
    "minStr": "",
    "hostName": "",
    "serviceName": "",
    "state": "",
    "count": 10
  }]
}
```