## ユーザリスト取得 API

### URI

```
GET
/api/admin/users
```

### レスポンスボディ

```json
{
  "users": [
    {
      "id": 0,
      "firstName": "string",
      "lastName": "string",
      "email": "string",
      "entranceYear": 0,
      "userGroups": [
        {
          "id": 0,
          "name": "string",
          "roles": [0]
        }
      ]
    }
  ]
}
```

## ユーザ取得 API

### URI

```
GET
/api/admin/users/{user_id}
```

### レスポンスボディ

```json
{
  "id": 0,
  "firstName": "string",
  "lastName": "string",
  "email": "string",
  "entranceYear": 0,
  "userGroups": [
    {
      "id": 0,
      "name": "string",
      "roles": [0]
    }
  ]
}
```
