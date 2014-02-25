package com.octo.niobenchmark.httpcore;

import java.net.InetSocketAddress;

import org.apache.http.HttpRequest;
import org.apache.http.impl.nio.DefaultHttpServerIODispatch;
import org.apache.http.impl.nio.DefaultNHttpServerConnection;
import org.apache.http.impl.nio.DefaultNHttpServerConnectionFactory;
import org.apache.http.impl.nio.reactor.DefaultListeningIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.nio.NHttpConnectionFactory;
import org.apache.http.nio.protocol.HttpAsyncRequestHandler;
import org.apache.http.nio.protocol.HttpAsyncService;
import org.apache.http.nio.protocol.UriHttpAsyncRequestHandlerMapper;
import org.apache.http.nio.reactor.IOEventDispatch;
import org.apache.http.nio.reactor.ListeningIOReactor;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.HttpProcessorBuilder;
import org.apache.http.protocol.ResponseConnControl;
import org.apache.http.protocol.ResponseContent;
import org.apache.http.protocol.ResponseDate;

public class AsyncServer {
	private final UriHttpAsyncRequestHandlerMapper uriMapper = new UriHttpAsyncRequestHandlerMapper();
	private final int threads;
	private final int port;

	public AsyncServer(int threads, int port) {
		this.threads = threads;
		this.port = port;
	}

	public void start() throws Exception {
		// Create HTTP protocol basic processing chain
		HttpProcessor httpProcessor = HttpProcessorBuilder.create()
				.add(new ResponseDate()).add(new ResponseContent())
				.add(new ResponseConnControl()).build();
		// Create server
		HttpAsyncService protocolHandler = new HttpAsyncService(httpProcessor,
				uriMapper);
		NHttpConnectionFactory<DefaultNHttpServerConnection> connFactory = new DefaultNHttpServerConnectionFactory();
		IOEventDispatch ioEventDispatch = new DefaultHttpServerIODispatch(
				protocolHandler, connFactory);
		IOReactorConfig config = IOReactorConfig.custom()
				.setIoThreadCount(threads).setSoReuseAddress(true).build();
		ListeningIOReactor ioReactor = new DefaultListeningIOReactor(config);
		// Start server
		ioReactor.listen(new InetSocketAddress(port));
		ioReactor.execute(ioEventDispatch);
	}

	public void register(String pattern,
			HttpAsyncRequestHandler<HttpRequest> handler) {
		uriMapper.register(pattern, handler);
	}

}
