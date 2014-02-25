nio-benchmark
=============

Sample applications to compare performance between regular java servlets/web services with nio versions.

Build the project with maven. You will get 3 artifacts:
* war.war: webapp to deploy on an application server, port 8080
* HelloServer.jar: launch with java -jar HelloServer.jar will listen on port 8081
* TestServer.jar: launch with java -jar TestServer.jar will listen on port 8082

Then go to http://localhost:8080/war/