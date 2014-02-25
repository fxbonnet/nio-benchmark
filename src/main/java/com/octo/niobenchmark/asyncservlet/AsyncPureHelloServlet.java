package com.octo.niobenchmark.asyncservlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.AsyncContext;
import javax.servlet.ServletConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(asyncSupported = true)
public class AsyncPureHelloServlet extends HttpServlet {
    private ExecutorService executor = Executors.newFixedThreadPool(10);

    @Override
    public void init(ServletConfig config) {
    }

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) {
        final AsyncContext asyncContext = request.startAsync();
        executor.execute(new Runnable() {
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
        });
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
