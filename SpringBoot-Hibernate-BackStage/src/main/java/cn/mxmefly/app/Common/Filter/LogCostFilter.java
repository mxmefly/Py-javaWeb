package cn.mxmefly.app.Common.Filter;

import javax.servlet.*;
import java.io.IOException;

public class LogCostFilter implements Filter {

    @Override
    public void init(javax.servlet.FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        long start = System.currentTimeMillis();
        filterChain.doFilter(servletRequest,servletResponse);
        /*System.out.println("Execute cost="+(System.currentTimeMillis()-start));*/
    }

    @Override
    public void destroy() {

    }
}