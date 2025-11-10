package com.mulberry.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

public class TimingFilter implements Filter {

    @Override
    public void doFilter(
            ServletRequest servletRequest,
            ServletResponse servletResponse,
            FilterChain filterChain
    ) throws IOException, ServletException {
        long startTime = System.currentTimeMillis();
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        filterChain.doFilter(servletRequest, servletResponse);

        long totalTime = System.currentTimeMillis() - startTime;
        System.out.printf("Total time:%6dms    Request url: %s\n", totalTime, request.getRequestURI());
    }
}
