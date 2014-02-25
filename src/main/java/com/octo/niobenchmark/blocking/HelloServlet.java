package com.octo.niobenchmark.blocking;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.octo.niobenchmark.Parameters;

public class HelloServlet extends HttpServlet {
    private CloseableHttpClient httpClient;

    @Override
    public void init(ServletConfig config) {
        httpClient = HttpClients.custom().setMaxConnPerRoute(Parameters.MAX_CONNECTIONS)
                .setMaxConnTotal(Parameters.MAX_CONNECTIONS).build();
    }

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        HttpGet httpGet = new HttpGet("http://localhost:8081/slowhello");
        CloseableHttpResponse httpClientResponse = httpClient.execute(httpGet);
        try {
            String result = EntityUtils.toString(httpClientResponse.getEntity());
            response.getWriter().write(result);
        } finally {
            httpClientResponse.close();
        }
    }

}
