# SpringSecurityGuide
Welcome to the Spring Security Examples repository! In Spring Security, we've witnessed significant changes, notably the deprecation of the trusted `WebSecurityConfigurerAdapter` in favor of a more streamlined and powerful approach to securing your web applications. This repository serves as a comprehensive resource for developers seeking to grasp the latest techniques and best practices in securing their Spring-based web applications.

## Basic Authentication

To learn more about implementing Basic Authentication in this project, please refer to the [`Basic Auth branch`](https://github.com/BykaWF/SpringSecurityGuide/tree/Basic-Auth).

In the `basic-auth` branch, you will find code examples and detailed instructions on setting up Basic Authentication for your application.

### PermitAll()
With Basic Auth we should be authorized to any resourse. But if we want to have resources were any user can be without any permission. Please refer to the [`PermitAll() branch`](https://github.com/BykaWF/SpringSecurityGuide/tree/PermitAll). 

### Multiple Security Configuration
Do you want to use different types of authentication? please refer to the [`Multiple Security branch`](https://github.com/BykaWF/SpringSecurityGuide/tree/MultipleConfiguration).

## Users' Roles and Authorities

Previously to get access to any resource we should use a generated password in our console and in the field username pass user. But if you want to create your User with username and password, role and so forth. You can store your User in memory or your database.
Roles and Permission (Authority)

Let's consider a basic situation when you are building a Library Management System and you have :

    - Seller (library stuff)

    - Customer

In this case, our Seller has the role of Admin and permission to write and read a book. Our Customer has the role of User and permission only read a book. We need APIs that are accessible only for admin or only users. And of course, you can assign multiple roles to your user.


- Role-based Auth: [Role Based Auth branch](https://github.com/BykaWF/SpringSecurityGuide/tree/Role_Auth).
- Permission-based Auth: [Permission Based Auth branch](https://github.com/BykaWF/SpringSecurityGuide/tree/Permission_Based_Authentication)
