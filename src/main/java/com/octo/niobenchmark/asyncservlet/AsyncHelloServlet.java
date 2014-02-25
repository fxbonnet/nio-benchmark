package com.octo.niobenchmark.asyncservlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.AsyncContext;
import javax.servlet.ServletConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.util.EntityUtils;

import com.octo.niobenchmark.Parameters;

@WebServlet(asyncSupported = true)
public class AsyncHelloServlet extends HttpServlet {
    private CloseableHttpAsyncClient httpClient;

    @Override
    public void init(ServletConfig config) {
        IOReactorConfig ioReactorConfig = IOReactorConfig.custom()
                .setIoThreadCount(Parameters.HTTP_ASYNC_CLIENT_THREADS).build();
        httpClient = HttpAsyncClients.custom().setDefaultIOReactorConfig(ioReactorConfig)
                .setMaxConnPerRoute(Parameters.MAX_CONNECTIONS).setMaxConnTotal(Parameters.MAX_CONNECTIONS).build();
        httpClient.start();
    }

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) {
        final AsyncContext asyncContext = request.startAsync();

        HttpGet httpGet = new HttpGet("http://localhost:8081/slowhello");

        FutureCallback<HttpResponse> responseCallback = new FutureCallback<HttpResponse>() {

            @Override
            public void completed(final HttpResponse httpClientResponse) {
                try {
                    String result = EntityUtils.toString(httpClientResponse.getEntity());
                    response.getWriter().write(result);
                } catch (IOException e) {
                    sendError(e, response);
                } finally {
                    asyncContext.complete();
                }
            }

            @Override
            public void failed(final Exception e) {
                sendError(e, response);
            }

            @Override
            public void cancelled() {
            }

        };

        httpClient.execute(httpGet, responseCallback);
    }

    private void sendError(Exception e, HttpServletResponse response) {
        response.setStatus(500);
        try {
            e.printStackTrace(new PrintWriter(response.getWriter()));
        } catch (IOException e1) {
            // Give up
        }
    }

}
