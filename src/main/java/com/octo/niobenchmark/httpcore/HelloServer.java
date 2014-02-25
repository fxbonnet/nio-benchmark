package com.octo.niobenchmark.httpcore;

public class HelloServer {
	
	private static final int THREADS = 1;
	public static final int PORT = 8081;
	
	public static final String SLOW_HELLO_URL = "http://localhost:"
			+ HelloServer.PORT + "/war/slowhello";

	public static void main(String[] args) throws Exception {
		AsyncServer asyncServer = new AsyncServer(THREADS, PORT);
		asyncServer.register("/war/hello", new HelloRequestHandler());
		asyncServer.register("/war/slowhello", new SlowHelloRequestHandler());
		asyncServer.start();
	}

}
