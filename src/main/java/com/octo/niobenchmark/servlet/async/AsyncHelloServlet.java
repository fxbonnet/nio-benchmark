package com.octo.niobenchmark.servlet.async;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(asyncSupported = true)
public class AsyncHelloServlet extends HttpServlet {
	private ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(
			1);

	@Override
	protected void doGet(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException {
		final AsyncContext asyncContext = request.startAsync();
		executor.schedule(new Runnable() {
			@Override
			public void run() {
				try {
					response.getWriter().write("Hello world");
				} catch (IOException e) {
					sendError(e, response);
				} finally {
					asyncContext.complete();
				}
			}
		}, 1, TimeUnit.SECONDS);
	}

	private void sendError(Exception e, HttpServletResponse response) {
		response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		try {
			e.printStackTrace(new PrintWriter(response.getWriter()));
		} catch (IOException e1) {
			// Give up
		}
	}

}
