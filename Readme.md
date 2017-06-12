## Financial Tracker app

### Stack
  - Database: PostgreSQL
  - Backend Restful API - Scala + https4s + doobie
  - Frontend: React + Redux

### Getting started

Note all commands below are executed in root

To start a server with static assets
  ```
    sbt run (in root folder)
  ```

To enable updating frontend live:
  ```
    cd frontend && npm i && npm run build
    cd ../target/scala-2.12/classes && rm app.js && ln -s ../../../public/app.js
  ```

### Running tests

Backend unit tests:
  ```
    sbt testPackage/test
  ```

Backend integration tests:
  ```
    sbt testPackage/it:test
  ```

Frontend tests:
  ```
    cd frontend && npm test
  ```

### Creating and running docker images locally:
  To create images
  ```
    deploy/bin/build
  ```

  To run server with docker images
  ```
    deploy/bin/server
  ```
  
### Deploying the app

  Publish docker images by running:
  ```
    deploy/bin/deploy
  ```

  This will rebuild all images and push them to docker registry

  Then update AWS ECS to user this images in AWS console
  