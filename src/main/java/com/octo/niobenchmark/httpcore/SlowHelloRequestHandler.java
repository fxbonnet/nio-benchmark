package com.octo.niobenchmark.httpcore;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.nio.protocol.BasicAsyncRequestConsumer;
import org.apache.http.nio.protocol.HttpAsyncExchange;
import org.apache.http.nio.protocol.HttpAsyncRequestConsumer;
import org.apache.http.nio.protocol.HttpAsyncRequestHandler;
import org.apache.http.protocol.HttpContext;

public final class SlowHelloRequestHandler implements HttpAsyncRequestHandler<HttpRequest> {
    private ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);

    @Override
    public HttpAsyncRequestConsumer<HttpRequest> processRequest(final HttpRequest request, final HttpContext context) {
        // Buffer request content in memory for simplicity
        return new BasicAsyncRequestConsumer();
    }

    @Override
    public void handle(final HttpRequest request, final HttpAsyncExchange httpexchange, final HttpContext context) {
        executor.schedule(new Runnable() {
            @Override
            public void run() {
                HttpResponse response = httpexchange.getResponse();
                response.setStatusCode(HttpStatus.SC_OK);
                response.setEntity(new NStringEntity(("Slow hello world"), ContentType.create("text/html", "UTF-8")));
                httpexchange.submitResponse();
            }
        }, 50, TimeUnit.MILLISECONDS);
    }
}
