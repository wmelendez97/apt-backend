package com.apt.api.util;

import java.io.IOException;
import java.util.UUID;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

@Component
public class TransactionFilter implements Filter {

	// Generates a unique ID per request, adds it to header and MDC for logging
	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		HttpServletResponse response = (HttpServletResponse) res;
		String txId = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
		response.addHeader("X-Transaction-ID", txId);
		MDC.put("txId", txId);
		try {
			chain.doFilter(req, res);
		} finally {
			MDC.clear();
		}
	}
}