package com.example.apigateway.errorhandling;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.Arrays;
import java.util.Map;
import java.util.NoSuchElementException;

@Component
public class GatewayErrorHandler extends DefaultErrorAttributes {

    public static final String ROUTE_DEFINITION_NOT_FOUND_ERR = "No route definition with path '%s' found.";
    public static final String INVALID_STATUS_CODE_ERR = "No HttpStatus corresponds to status code %d";
    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        Throwable error = super.getError(request);
        Map<String, Object> map = super.getErrorAttributes(request, options);

        int statusCode = (int) map.get("status");
        HttpStatus status = getHttpStatusFromStatusCode(statusCode);

        map.put("message", populateMapWithErrorMessage(error, map, status));
        return map;
    }

    private HttpStatus getHttpStatusFromStatusCode(int statusCode) {
        return Arrays.stream(HttpStatus.values())
                .filter(v -> v.value() == statusCode)
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(String.format(INVALID_STATUS_CODE_ERR, statusCode)));
    }

    private String populateMapWithErrorMessage(Throwable error, Map<String, Object> map, HttpStatus status) {
        return isErrorCausedByNoRouteDefinitionFound(error, map) ?
                String.format(ROUTE_DEFINITION_NOT_FOUND_ERR, map.get("path")) :
                trimStatusAndQuotesFromErrorMessage(error.getMessage(), status);
    }

    private boolean isErrorCausedByNoRouteDefinitionFound(Throwable error, Map<String, Object> map) {
        return map.containsKey("requestId") && error.getMessage().contains("404");
    }

    private String trimStatusAndQuotesFromErrorMessage(String errorMessage, HttpStatus status) {
        return errorMessage
                .replace(status.toString(), "")
                .replace("\"", "")
                .trim();
    }
}
