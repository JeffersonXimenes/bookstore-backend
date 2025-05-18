package br.com.bookstore.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.Set;
import java.util.UUID;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class LoggingFilter implements Filter {

	private static final Logger logger 					= LoggerFactory.getLogger(LoggingFilter.class);
	private static final Set<String> SENSITIVE_FIELDS	= Set.of("password", "token");
	private static final String OMITTED 				= "OMITTED";
	private static final String EMPTY 					= "EMPTY";

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String transaction = UUID.randomUUID().toString();

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(httpRequest);
		ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(httpResponse);

		chain.doFilter(wrappedRequest, wrappedResponse);

		logRequest(wrappedRequest, transaction);
		logResponse(wrappedResponse, transaction);

		wrappedResponse.copyBodyToResponse();
	}

	private void logRequest(ContentCachingRequestWrapper request, String transaction) {
        String body = new String(request.getContentAsByteArray(), StandardCharsets.UTF_8);
        String bodyOmitted = omitSensitiveData(body);
		String headers = omitSensitiveData(getHeadersAsJson(request));

		String queryString = request.getQueryString();
		String fullUrl = request.getRequestURL() + (queryString != null ? "?" + queryString : "");

		logger.info("[{}] METHOD: {} - PATH: {}", transaction, request.getMethod(), fullUrl);
		logger.info("[{}] REQUEST HEADERS: {}", transaction, headers);
		logger.info("[{}] REQUEST BODY: {}", transaction, bodyOmitted);
	}

	private void logResponse(ContentCachingResponseWrapper response, String transaction) throws IOException {
		String body = new String(response.getContentAsByteArray(), StandardCharsets.UTF_8);
        String bodyOmitted = omitSensitiveData(body);
		String headers = omitSensitiveData(getHeadersAsJson(response));

		logger.info("[{}] RESPONSE STATUS: {}", transaction, response.getStatus());
		logger.info("[{}] RESPONSE HEADERS: {}", transaction, headers);
		logger.info("[{}] RESPONSE BODY: {}", transaction, bodyOmitted);
	}

	private String omitSensitiveData(String body) {
		try {
			ObjectMapper mapper = new ObjectMapper();

			if (StringUtils.isNotBlank(body)) {
				JsonNode rootNode = mapper.readTree(body);

				if (rootNode.isObject()) {
					ObjectNode objectNode = (ObjectNode) rootNode;

					for (String field : SENSITIVE_FIELDS) {
						if (objectNode.has(field)) {
							objectNode.put(field, OMITTED);
						}
					}
					return objectNode.toString();

				} else if (rootNode.isArray()) {
					for (JsonNode node : rootNode) {
						if (node.isObject()) {
							ObjectNode obj = (ObjectNode) node;

							for (String field : SENSITIVE_FIELDS) {
								if (obj.has(field)) {
									obj.put(field, OMITTED);
								}
							}
						}
					}

					return rootNode.toString();
				}
			}
			return EMPTY;
		} catch (Exception e) {
			logger.error("Erro ao omitir dados sensiveis body: {}", e.getMessage());
			return body;
		}
	}

	private String getHeadersAsJson(HttpServletRequest request) {
		ObjectNode headersNode = new ObjectMapper().createObjectNode();

		Enumeration<String> headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String headerName = headerNames.nextElement();
			String headerValue = request.getHeader(headerName);
			headersNode.put(headerName, headerValue);
		}

		return headersNode.toString();
	}

	private String getHeadersAsJson(HttpServletResponse response) {
		ObjectNode headersNode = new ObjectMapper().createObjectNode();

		for (String headerName : response.getHeaderNames()) {
			String headerValue = response.getHeader(headerName);
			headersNode.put(headerName, headerValue);
		}

		return headersNode.toString();
	}

	@Override
	public void destroy() {
	}
}