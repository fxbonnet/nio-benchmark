<html>
<head>
<title>Test async serlvet 3.0</title>
</head>
<body>
	This servlet calls a web service using HttpClient. The service just returns "Hello world" then the servlet displays the result in the response.<br /><br />
	<a href="test1">Test 1: Regular servlet (blocking IO) with regular HttpClient (also blocking IO)</a><br />
	<a href="test2">Test 2: Async servlet (servlet 3.0 API) with HttpAsyncClient</a><br /><br />
	You can test with Apache Benchmark, ex:<br />
	<pre>ab -c50 -n100000 http://localhost:8080/test-servlet30/test1</pre><br />
	<a href="clock1">Clock 1: Long polling exemple with a blocking servlet</a><br />
	<a href="clock2">Clock 2: Long polling exemple with a Async servlet (servlet 3.0 API)</a><br />
</body>
</html>
