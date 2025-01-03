package com.project.linkedIn.api_gateway.filter;

import com.project.linkedIn.api_gateway.service.JwtService;
import io.jsonwebtoken.JwtException;
import lombok.Data;
import org.slf4j.Logger;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

/**
 * To mutate the incoming request
 * */

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(AuthenticationFilter.class);
    private final JwtService jwtService;

    //Constructor
    public AuthenticationFilter(JwtService jwtService) {
        super(Config.class);
        this.jwtService = jwtService;
    }

    @Override
    public GatewayFilter apply(Config config) {

        return (exchange, chain) -> {

            log.info("Login Request:{}", exchange.getRequest().getURI());

            final String tokenHeader = exchange.getRequest().getHeaders().getFirst("Authorization");

            if (null == tokenHeader || !tokenHeader.startsWith("Bearer")) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                log.error("Authorization Token header not found");
                return exchange.getResponse().setComplete();
            }

            final String token = tokenHeader.split("Bearer ")[1];

            try {
                String userId = jwtService.getUserIdFromToken(token);

                ServerWebExchange modifiedExchange = exchange
                        .mutate()
                        .request(r -> r.header("X-User-Id", userId))
                        .build();

                return chain.filter(modifiedExchange);
            }catch (JwtException e){
                log.error("JWT Exception: {}", e.getLocalizedMessage());
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
        };
    }

    @Data
    public static class Config {
        private boolean isEnabled;
    }

}
