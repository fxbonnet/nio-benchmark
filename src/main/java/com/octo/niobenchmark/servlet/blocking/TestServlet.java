package com.octo.niobenchmark.servlet.blocking;

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
import com.octo.niobenchmark.httpcore.HelloServer;

public class TestServlet extends HttpServlet {
	private CloseableHttpClient httpClient;

	@Override
	public void init(ServletConfig config) {
		httpClient = HttpClients.custom()
				.setMaxConnPerRoute(Parameters.HTTP_CLIENT_MAX_CONNECTIONS)
				.setMaxConnTotal(Parameters.HTTP_CLIENT_MAX_CONNECTIONS).build();
	}

	@Override
	protected void doGet(final HttpServletRequest request,
			final HttpServletResponse response) throws IOException {
		HttpGet httpGet = new HttpGet(HelloServer.SLOW_HELLO_URL);
		CloseableHttpResponse httpClientResponse = httpClient.execute(httpGet);
		try {
			String result = EntityUtils
					.toString(httpClientResponse.getEntity());
			response.getWriter().write(result);
		} finally {
			httpClientResponse.close();
		}
	}

}
