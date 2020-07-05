# SmartBPractice

This Web Application is on :
http://smartbeeexam-env.eba-9pmxwptw.ap-east-1.elasticbeanstalk.com/

## Tools

- **Spring Boot** : application framework
- **JUnit** : test runner
- **Spring Security** : access control
- **Swagger API** : maintain api document
- **MockMVC** : testing Spring MVC controllers

## Spring Security

Use the following three permissions to control and meet the document requirements 

- account `admin`   represents super user
- account `manager` represents manager 
- account `user`    represents operator

password is same as account.
## Swagger Api Doc

Please refer to the following

http://smartbeeexam-env.eba-9pmxwptw.ap-east-1.elasticbeanstalk.com/swagger-ui.html

## How to Test

Please refer `wen.SmartBPractice.test.*` or use the following way

> first login to get role
````
POST http://smartbeeexam-env.eba-9pmxwptw.ap-east-1.elasticbeanstalk.com/api/login

{ "account":"user", "password":"user" }
````
> this role is user 
```
{ "authority": "ROLE_USER" }
```

> after getting the role, try to create a Company
````
POST http://smartbeeexam-env.eba-9pmxwptw.ap-east-1.elasticbeanstalk.com/api/company/create

{ "address":"Taipei, Taiwan", "name":"YourName" }
````
> create successful
````
{
    "id": 1,
    "name": "YourName",
    "address": "Taipei, Taiwan",
    "create_by": "user",
    "create_at": "2020-07-04T18:01:44.850+00:00",
    "update_by": null,
    "update_at": "2020-07-04T18:01:44.850+00:00"
}
````