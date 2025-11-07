package com.mulberry.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.Date;

public class TimingFilter implements Filter {

    @Override
    public void doFilter(
            ServletRequest servletRequest,
            ServletResponse servletResponse,
            FilterChain filterChain
    ) throws IOException, ServletException {
        long startTime = System.currentTimeMillis();
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        System.out.println("Request URL: " + request.getRequestURI());

        filterChain.doFilter(servletRequest, servletResponse);

        long totalTime = System.currentTimeMillis() - startTime;
        System.out.println("Total time = " + totalTime + "ms");
    }
}
