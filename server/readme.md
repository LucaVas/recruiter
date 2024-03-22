## API Reference

### Users

#### Login

```http
  POST /api/v1/login
```
Allows user login.

Request payload description:

| Key        | Type     | Description                          | Requirements                                                                                 |
|:-----------|:---------|:-------------------------------------|:---------------------------------------------------------------------------------------------|
| `email`    | `string` | **Required**: Email of user.         | Min: 5 chars. Max: 50 chars.                                                                 |
| `password` | `string` | **Required**: Password of user.      | Min: 8 chars. Max: 64 chars. Required uppercase, lowercase, numbers & symbols such as /-_+?! |

Request payload example:
```json
{
  "username": "johndoe",
  "email": "john.doe@mail.com"
}
```
Response example:
```json
{
  "token": "ey*",
  "username": "username"
}
```
---
#### Signup

```http
  POST /api/v1/signup
```
Allows user registration.

Request payload description:

| Key        | Type     | Description                          | Requirements                                                                                 |
|:-----------|:---------|:-------------------------------------|:---------------------------------------------------------------------------------------------|
| `username` | `string` | **Required**: Name/username of user. | Min: 3 chars. Max: 50 chars.                                                                 |
| `email`    | `string` | **Required**: Email of user.         | Min: 5 chars. Max: 50 chars.                                                                 |
| `password` | `string` | **Required**: Password of user.      | Min: 8 chars. Max: 64 chars. Required uppercase, lowercase, numbers & symbols such as /-_+?! |
| `mobile`   | `string` | **Required**: Mobile of user.        | Min: 5 chars. Max: 10 chars.                                                                 |
| `city`     | `string` | **Required**: City of user.          | Min: 3 chars. Max: 50 chars.                                                                 |
| `country`  | `string` | **Required**: Country of user.       | Min: 3 chars. Max: 50 chars.                                                                 |
| `role`     | `string` | **Required**: Role of user.          | Must be one of: "RECRUITER", "ADMIN".                                                        |


Request payload example:
```json
{
  "username": "johndoe",
  "email": "john.doe@mail.com",
  "password": "Password123",
  "mobile": "1234567890",
  "city": "the city",
  "country": "the country",
  "role": "RECRUITER"
}
```

Response example:
```json
{
  "id": 1
}
```
---
#### User approval

```http
  POST /api/v1/users/{userId}/approve
```
Allows admin to approve other users.

| Parameter | Type     | Description                              |
|:----------|:---------|:-----------------------------------------|
| `userId`  | `number` | **Required**: Id of the user to approve. |

Request payload description:

| Key        | Type      | Description                            | Requirements                                                   |
|:-----------|:----------|:---------------------------------------|:---------------------------------------------------------------|
| `approved` | `boolean` | **Required**: Approval status of user. |                                                                |
| `comments` | `string`  | **Required**: Comments from approver.  |                                                                |

Request payload example:
```json
{
  "approved": true,
  "comments": "No comments"
}
```

Response example:
```json
{
  "id": 1
}
```
---
#### Get users

```http
  GET /api/v1/users
```
Allows to retrieve all users.

Response example:
```json
[
  {
    "id": 1,
    "username": "johndoe",
    "email": "john.doe@mail.com",
    "mobile": "1234567890",
    "city": "The city",
    "country": "The country",
    "role": "RECRUITER",
    "comments": "Comments here",
    "approved": true,
    "approver": {
      "id": 1,
      "username": "johndoe"
    },
    "approvedOn": "01/01/2024"
  }
]
```
---