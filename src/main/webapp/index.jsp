<%@page import="com.octo.niobenchmark.httpcore.TestServer"%>
<%@page import="com.octo.niobenchmark.httpcore.HelloServer"%>
<html>
<head>
<title>NIO benchmark</title>
</head>
<body>
	Available urls:
	<ul>
		<li><a href="http://localhost:8080/war/hello">a test service implemented with a classic servlet that says hello</a></li>
		<li><a href="http://localhost:8080/war/helloasync">an async servlet that says hello after 1 s</a></li>
		<li><a href="http://localhost:8080/war/test1">a classic servlet that calls the test service with a classic HttpClient</a></li>
		<li><a href="http://localhost:8080/war/test2">an async servlet that calls the test service wiht an HttpAsyncClient</a></li>
		<li><a href="http://localhost:8081/war/hello">an async test service implemented with HttpCore NIO that says hello</a></li>
		<li><a href="http://localhost:8081/war/slowhello">an async test service implemented with HttpCore NIO that says hello after 50 ms</a></li>
		<li><a href="http://localhost:8082/war/test3 ">an async server implemented with HttpCore NIO that calls the test service with an HttpAsyncClient</a></li>
	</ul>
	You can test with Apache Benchmark, ex:
	<pre>ab -r -k -c10000 -n10000 http://localhost:8080/war/test1</pre>
</body>
</html>
