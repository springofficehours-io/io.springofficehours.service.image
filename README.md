# spring-office-hours-image-service

Developed during [Code episode 66](https://tanzu.vmware.com/developer/tv/code/0066/)

## Prerequisites

- Java 17

## Quick Start

```bash
git clone git@github.com:dashaun/spring-office-hours-image-service.git
cd spring-office-hours-image-service
./mvnw clean package spring-boot:run
```
> Open your browser to http://localhost:8080/ to see the generated documentation

## Made with

- Spring Boot 3.0.3
- Spring Web
- Spring Rest Docs
- (and ChatGPT)

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.0.1/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/3.0.1/maven-plugin/reference/html/#build-image)
* [Spring Web](https://docs.spring.io/spring-boot/docs/3.0.1/reference/htmlsingle/#web)
* [Spring REST Docs](https://docs.spring.io/spring-restdocs/docs/current/reference/html5/)
* [Spring Boot Actuator](https://docs.spring.io/spring-boot/docs/3.0.1/reference/htmlsingle/#actuator)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
* [Building a RESTful Web Service with Spring Boot Actuator](https://spring.io/guides/gs/actuator-service/)

### Workflows

Create credentials to store in secret variable AZURE_CREDENTIALS:

```bash
az ad sp create-for-rbac --name "dashaun-tanzu-tv" --role contributor --scopes /subscriptions/5449797d-453a-477d-8e8b-9e714d207115/resourceGroups/tanzu-tv --sdk-auth
```