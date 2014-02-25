package com.octo.niobenchmark.asyncservlet;

import java.io.IOException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.servlet.AsyncContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.octo.niobenchmark.Clock;
import com.octo.niobenchmark.Parameters;

@WebServlet(asyncSupported = true)
public class AsyncClockServlet extends HttpServlet {
    private ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) {
        final AsyncContext asyncContext = request.startAsync();
        executor.schedule(new Runnable() {
            @Override
            public void run() {
                try {
                    response.addHeader("Refresh", "0");
                    response.getWriter().write(Clock.getTime());
                } catch (IOException e) {
                    // Give up
                } finally {
                    asyncContext.complete();
                }
            }
        }, Parameters.CLOCK_INTERVAL, TimeUnit.MILLISECONDS);
    }

}
