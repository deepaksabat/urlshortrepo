# URL Shortener

Spring Boot based REST API that takes a URL and returns a shortened URL and uses H2 to persist data.

# Getting Started

## Dependencies

This project depends on 
* spring-boot-starter-web (Spring boot framework)
* spring-boot-starter-data-jpa (for data persistence)
* spring-boot-starter-actuator (for API statistics)
* commons-validator:1.6 (for URL validation)
* h2 (in memory database to store url data)
* spring-boot-starter-test (for testss)

## Project Build 

To build this project, run

```shell script
git clone https://gitlab.com/kdeepak910/urlshortener.git
cd urlshortener/urlshortener
mvn clean install
```

## Deployment

Project build can be deployed using Dockerfile

To deploy the project, run

```shell script
docker build -t urlshort:latest .
docker tag urlshort:latest kdeepak910/urlshort:backend
docker push kdeepak910/urlshort:backend
docker run --name urlshort -d -p 9090:9090 kdeepak910/urlshort:backend
```

**The application will be accessible on http://localhost:9090**

## API Endpoints

You can access following API endpoints at http://localhost:9090

### POST `/shorten`
It takes a JSON object in the following format as payload

```json
{
  "longUrl":"<The URL to be shortened>"
}
```

#### cURL

```shell script
curl --location 'http://localhost:9090/shorten' \
--header 'Accept: application/json' \
--header 'Content-Type: application/json' \
--data '{
    "longUrl": "https://udemy.com/learning-paths/7005430/45671234"
}'
```

Response:

```json
{
  "shortUrl": "<shortened url for the fullUrl provided in the request payload>"
}
```

Please note that API works only with valid HTTP or HTTPS Urls. In case of malformed Url, it returns `400 Bad Request` error with response body containing a JSON object in the following format

```json
{
  "field":"fullUrl",
  "value":"<Malformed Url provided in the request>",
  "message":"<Exception message>"
}
```

### GET `/<shortened_text>`

This endpoint redirects to the corresponding fullUrl.

### GET `/actuator/health`

Included the spring boot actuator dependency for API metrics. You can try this endpoint for health checks.

#### cURL

```shell script
curl -X GET   http://localhost:9090/actuator/health
```

Pull docker image and run it using below command docker pull kdeepak910/urlshort