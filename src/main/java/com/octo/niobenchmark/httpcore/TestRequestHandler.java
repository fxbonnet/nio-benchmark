package com.octo.niobenchmark.httpcore;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.nio.protocol.BasicAsyncRequestConsumer;
import org.apache.http.nio.protocol.HttpAsyncExchange;
import org.apache.http.nio.protocol.HttpAsyncRequestConsumer;
import org.apache.http.nio.protocol.HttpAsyncRequestHandler;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import com.octo.niobenchmark.Parameters;

public final class TestRequestHandler implements
		HttpAsyncRequestHandler<HttpRequest> {
	private final CloseableHttpAsyncClient httpClient;

	public TestRequestHandler() {
		IOReactorConfig ioReactorConfig = IOReactorConfig.custom()
				.setIoThreadCount(Parameters.HTTP_ASYNC_CLIENT_THREADS).build();
		httpClient = HttpAsyncClients.custom()
				.setDefaultIOReactorConfig(ioReactorConfig)
				.setMaxConnPerRoute(Parameters.HTTP_CLIENT_MAX_CONNECTIONS)
				.setMaxConnTotal(Parameters.HTTP_CLIENT_MAX_CONNECTIONS).build();
		httpClient.start();
	}

	@Override
	public HttpAsyncRequestConsumer<HttpRequest> processRequest(
			final HttpRequest request, final HttpContext context) {
		// Buffer request content in memory for simplicity
		return new BasicAsyncRequestConsumer();
	}

	@Override
	public void handle(final HttpRequest request,
			final HttpAsyncExchange httpexchange, final HttpContext context) {
		HttpGet httpGet = new HttpGet(HelloServer.SLOW_HELLO_URL);

		FutureCallback<HttpResponse> responseCallback = new FutureCallback<HttpResponse>() {

			@Override
			public void completed(final HttpResponse httpClientResponse) {
				try {
					String result = EntityUtils.toString(httpClientResponse
							.getEntity());
					HttpResponse response = httpexchange.getResponse();
					response.setStatusCode(HttpStatus.SC_OK);
					response.setEntity(new NStringEntity(result, ContentType
							.create("text/html", "UTF-8")));
					httpexchange.submitResponse();
				} catch (IOException e) {
					sendError(e, httpexchange);
				}
			}

			@Override
			public void failed(final Exception e) {
				sendError(e, httpexchange);
			}

			@Override
			public void cancelled() {
			}

		};

		httpClient.execute(httpGet, responseCallback);
	}

	private void sendError(Exception e, HttpAsyncExchange httpexchange) {
		e.printStackTrace();
		HttpResponse response = httpexchange.getResponse();
		response.setStatusCode(500);
		StringWriter stringWriter = new StringWriter();
		e.printStackTrace(new PrintWriter(stringWriter));
		response.setEntity(new NStringEntity(stringWriter.toString(),
				ContentType.create("text/plain", "UTF-8")));
		httpexchange.submitResponse();
	}
}
