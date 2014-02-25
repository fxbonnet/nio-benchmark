<%@page import="com.octo.niobenchmark.httpcore.TestServer"%>
<%@page import="com.octo.niobenchmark.httpcore.HelloServer"%>
<html>
<head>
<title>NIO benchmark</title>
</head>
<body>
	Hello world service implementations:
	<ul>
		<li><a href="hello">Blocking servlet</a></li>
		<li><a href="http://localhost:<%=HelloServer.PORT%>/war/hello">Async server
				(HttpCore)</a></li>
		<li><a href="http://localhost:<%=HelloServer.PORT%>/war/slowhello">Slow async
				server (HttpCore): waits 50 ms</a></li>
	</ul>
	Servlet that calls the web service using an HttpClient and displays the result:
	<ul>
		<li><a href="test1">Test 1: Regular servlet (blocking IO) with regular
		HttpClient (also blocking IO)</a></li>
		<li><a href="test2">Test 2: Async servlet (servlet 3.0 API) with
		HttpAsyncClient</a></li>
		<li><a href="http://localhost:<%=TestServer.PORT%>/war/test3">Test 3: Async server
		(HttpCore) with HttpAsyncClient</a></li>
	</ul>
	You can test with Apache Benchmark, ex:
	<pre>ab -k -c20 -n10000 http://localhost:8080<%=request.getContextPath()%>/test1</pre>
</body>
</html>
