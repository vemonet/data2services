# data2services-api

<img src="https://travis-ci.org/nunogit/data2services.svg?branch=master"/> 
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/d2894976f0d44fecb591fe898070dd46)](https://app.codacy.com/app/nunogit/data2services?utm_source=github.com&utm_medium=referral&utm_content=nunogit/data2services&utm_campaign=Badge_Grade_Dashboard)

## Build

```shell
docker build -t data2services-api .
```

## Run

```shell
docker run -p 8080:8080 data2services-api
```

## Access

http://localhost:8080/webjars/swagger-ui/3.19.0/?url=http://localhost:8080/rest/swag