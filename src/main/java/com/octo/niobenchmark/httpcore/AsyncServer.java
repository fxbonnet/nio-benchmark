package com.octo.niobenchmark.httpcore;

import java.net.InetSocketAddress;

import org.apache.http.impl.nio.DefaultHttpServerIODispatch;
import org.apache.http.impl.nio.DefaultNHttpServerConnection;
import org.apache.http.impl.nio.DefaultNHttpServerConnectionFactory;
import org.apache.http.impl.nio.reactor.DefaultListeningIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.nio.NHttpConnectionFactory;
import org.apache.http.nio.protocol.HttpAsyncService;
import org.apache.http.nio.protocol.UriHttpAsyncRequestHandlerMapper;
import org.apache.http.nio.reactor.IOEventDispatch;
import org.apache.http.nio.reactor.ListeningIOReactor;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.HttpProcessorBuilder;
import org.apache.http.protocol.ResponseConnControl;
import org.apache.http.protocol.ResponseContent;
import org.apache.http.protocol.ResponseDate;

import com.octo.niobenchmark.Parameters;

public class AsyncServer {
    public static void main(String[] args) throws Exception {
        // Create HTTP protocol basic processing chain
        HttpProcessor httpProcessor = HttpProcessorBuilder.create().add(new ResponseDate()).add(new ResponseContent())
                .add(new ResponseConnControl()).build();
        // Create request handler registry and register handlers by URI
        UriHttpAsyncRequestHandlerMapper uriMapper = new UriHttpAsyncRequestHandlerMapper();
        uriMapper.register("/hello", new HelloServiceRequestHandler());
        uriMapper.register("/slowhello", new SlowHelloServiceRequestHandler());
        uriMapper.register("/test", new HelloRequestHandler());
        uriMapper.register("/clock", new ClockRequestHandler());
        // Create server
        HttpAsyncService protocolHandler = new HttpAsyncService(httpProcessor, uriMapper);
        NHttpConnectionFactory<DefaultNHttpServerConnection> connFactory = new DefaultNHttpServerConnectionFactory();
        IOEventDispatch ioEventDispatch = new DefaultHttpServerIODispatch(protocolHandler, connFactory);
        IOReactorConfig config = IOReactorConfig.custom().setIoThreadCount(Parameters.HTTP_ASYNC_SERVER_THREADS)
                .setSoReuseAddress(true).build();
        ListeningIOReactor ioReactor = new DefaultListeningIOReactor(config);
        // Start server
        ioReactor.listen(new InetSocketAddress(Parameters.SERVER_PORT));
        ioReactor.execute(ioEventDispatch);
    }

}
