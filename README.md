nio-benchmark
=============

Sample applications to compare performance between regular java servlets/web services with nio versions.

Build the project with maven. You will get 3 artifacts:
* war.war: webapp to deploy on an application server, port 8080
* HelloServer.jar: launch with java -jar HelloServer.jar will listen on port 8081
* TestServer.jar: launch with java -jar TestServer.jar will listen on port 8082

Available urls:
Url | Comment
--- | ---
http://localhost:8080/war/hello | a test service implemented with a classic servlet that says hello
http://localhost:8080/war/helloasync | an async servlet that says hello after 1 s
http://localhost:8080/war/test1 | a classic servlet that calls the test service with a classic HttpClient
http://localhost:8080/war/test2 | an async servlet that calls the test service wiht an HttpAsyncClient
http://localhost:8081/war/hello | an async test service implemented with HttpCore NIO that says hello
http://localhost:8081/war/slowhello | an async test service implemented with HttpCore NIO that says hello after 50 ms
http://localhost:8082/war/test3 | an async server implemented with HttpCore NIO that calls the test service with an HttpAsyncClient