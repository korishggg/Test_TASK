package org.example.task.web.filters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.task.dto.ErrorStatus;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class XmlAcceptHeaderCheckFilter implements WebFilter {

    private final ObjectMapper objectMapper;

    public XmlAcceptHeaderCheckFilter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        if (exchange.getRequest().getHeaders().getAccept().contains(MediaType.APPLICATION_XML)) {
            ErrorStatus errorStatus = new ErrorStatus(HttpStatus.NOT_ACCEPTABLE.value(), "XML Accept header is not supported.");
            return createErrorResponse(exchange, errorStatus);
        }
        return chain.filter(exchange);
    }

    private Mono<Void> createErrorResponse(ServerWebExchange exchange, ErrorStatus errorStatus) {
        exchange.getResponse().setStatusCode(HttpStatus.valueOf(errorStatus.status()));
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        try {
            byte[] bytes = objectMapper.writeValueAsBytes(errorStatus);
            return exchange.getResponse().writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(bytes)));
        } catch (JsonProcessingException e) {
            return Mono.error(e);
        }
    }
}