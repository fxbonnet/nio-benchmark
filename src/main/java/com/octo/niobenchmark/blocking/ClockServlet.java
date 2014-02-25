package com.octo.niobenchmark.blocking;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.octo.niobenchmark.Clock;
import com.octo.niobenchmark.Parameters;

public class ClockServlet extends HttpServlet {

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws IOException,
            ServletException {
        try {
            Thread.sleep(Parameters.CLOCK_INTERVAL);
        } catch (InterruptedException e) {
            throw new ServletException(e);
        }
        response.addHeader("Refresh", "0");
        response.getWriter().write(Clock.getTime());
    }
}
