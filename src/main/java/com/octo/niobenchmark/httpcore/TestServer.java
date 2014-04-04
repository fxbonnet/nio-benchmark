package com.octo.niobenchmark.httpcore;

public class TestServer {
	private static final int THREADS = 4;
	public static final int PORT = 8082;

	public static void main(String[] args) throws Exception {
		AsyncServer asyncServer = new AsyncServer(THREADS, PORT);
		asyncServer.register("/war/test3", new TestRequestHandler());
		asyncServer.start();
	}

}
