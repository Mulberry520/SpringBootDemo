package com.mulberry.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

public class TimingFilter implements Filter {
    private long startTime;

    @Override
    public void doFilter(
            ServletRequest servletRequest,
            ServletResponse servletResponse,
            FilterChain filterChain
    ) throws IOException, ServletException {
        this.startTime = System.currentTimeMillis();
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        System.out.println(request.getRequestURI());

        filterChain.doFilter(servletRequest, servletResponse);

        long totalTime = System.currentTimeMillis() - this.startTime;
        System.out.println("Total time = " + totalTime + "ms");
    }
}
