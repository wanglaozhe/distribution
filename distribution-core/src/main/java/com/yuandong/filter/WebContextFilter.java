package com.yuandong.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import com.yuandong.common.webcontext.DefaultWebContext;
import com.yuandong.common.webcontext.WebContextFactory;

@Component
public class WebContextFilter implements Filter {

	public void init(FilterConfig filterConfig) throws ServletException {
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		DefaultWebContext ctx = (DefaultWebContext) WebContextFactory.getWebContext();
		WebContextFactory.setContext(request.getServletContext());
		if (request instanceof HttpServletRequest && response instanceof HttpServletResponse) {
			ctx.setRequest((HttpServletRequest) request);
			ctx.setResponse((HttpServletResponse) response);
			ctx.setSession(((HttpServletRequest) request).getSession());
		}
		chain.doFilter(request, response);
	}

	public void destroy() {

	}

}
