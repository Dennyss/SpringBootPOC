package com.configuration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;

/**
 * Created by denys.kovalenko on 5/23/2018.
 */
public class LoggableDispatcherServlet extends DispatcherServlet {
    private static final Logger LOGGER = LogManager.getLogger("httpReqLogger");

    @Override
    protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (!(request instanceof ContentCachingRequestWrapper)) {
            request = new ContentCachingRequestWrapper(request);
        }
        if (!(response instanceof ContentCachingResponseWrapper)) {
            response = new ContentCachingResponseWrapper(response);
        }
        try {
            super.doDispatch(request, response);
        } finally {
            log(request, response);
            updateResponse(response);
        }
    }

    private void log(HttpServletRequest request, HttpServletResponse response) {
        // Request logs portion
        LOGGER.info(" ------------------------ HTTP Request ------------------------");
        LOGGER.info("HttpMethod: " + request.getMethod());
        LOGGER.info("URI: " + request.getRequestURI());
        LOGGER.info("Request Body: " + getRequestPayload(request));
        LOGGER.info("Request Headers: " + buildRequestHeadersMessage(request));

        // Response logs portion
        LOGGER.info(" ------------------------ HTTP Response ------------------------");
        LOGGER.info("HTTP Status: " + response.getStatus());
        LOGGER.info("Response Body: " + getResponsePayload(response));
        LOGGER.info("Response Headers: " + buildResponseHeadersMessage(response));

    }

    private String buildRequestHeadersMessage(HttpServletRequest request) {
        StringBuilder headerBuilder = new StringBuilder();
        Enumeration<String> headerNames = request.getHeaderNames();
        while(headerNames.hasMoreElements()){
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            headerBuilder.append(headerName + ": " + headerValue);
            headerBuilder.append(": ");
            headerBuilder.append(headerValue);
            headerBuilder.append(" ");
        }

        return headerBuilder.toString();
    }

    private String buildResponseHeadersMessage(HttpServletResponse response) {
        StringBuilder headerBuilder = new StringBuilder();
        for(String headerName : response.getHeaderNames()){
            String headerValue = response.getHeader(headerName);
            headerBuilder.append(headerName + ": " + headerValue);
            headerBuilder.append(": ");
            headerBuilder.append(headerValue);
            headerBuilder.append(" ");
        }

        return headerBuilder.toString();
    }

    private String getResponsePayload(HttpServletResponse response) {
        ContentCachingResponseWrapper wrapper = WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
        if (wrapper != null) {
            byte[] buf = wrapper.getContentAsByteArray();
            if (buf.length > 0) {
                int length = Math.min(buf.length, 5120);
                try {
                    return new String(buf, 0, length, wrapper.getCharacterEncoding());
                } catch (UnsupportedEncodingException ex) {
                    return "[unknown]";
                }
            }
        }
        return "[unknown]";
    }

    private String getRequestPayload(HttpServletRequest request) {
        ContentCachingRequestWrapper wrapper = WebUtils.getNativeRequest(request, ContentCachingRequestWrapper.class);
        if (wrapper != null) {
            byte[] buf = wrapper.getContentAsByteArray();
            if (buf.length > 0) {
                int length = Math.min(buf.length, 5120);
                try {
                    return new String(buf, 0, length, wrapper.getCharacterEncoding());
                } catch (UnsupportedEncodingException ex) {
                    return "[unknown]";
                }
            }
        }
        return "[unknown]";
    }

    private void updateResponse(HttpServletResponse response) throws IOException {
        ContentCachingResponseWrapper responseWrapper = WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
        responseWrapper.copyBodyToResponse();
    }

}
