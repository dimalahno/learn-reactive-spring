package com.learnreactivespring.router;

import com.learnreactivespring.handler.ItemsHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static com.learnreactivespring.constants.ItemConstants.ITEM_FUNCTIONAL_END_POINT_V1;
import static com.learnreactivespring.constants.ItemConstants.ITEM_STREAM_FUNCTIONAL_END_POINT;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class ItemsRouter {

    @Bean
    public RouterFunction<ServerResponse> itemsRoute(ItemsHandler handler){
        return RouterFunctions.route(GET(ITEM_FUNCTIONAL_END_POINT_V1).and(accept(APPLICATION_JSON)),
                        handler::getAllItems)
                .andRoute(GET(ITEM_FUNCTIONAL_END_POINT_V1.concat("/{id}")).and(accept(APPLICATION_JSON)),
                        handler::getOneItem)
                .andRoute(POST(ITEM_FUNCTIONAL_END_POINT_V1).and(accept(APPLICATION_JSON)),
                        handler::createItem)
                .andRoute(DELETE(ITEM_FUNCTIONAL_END_POINT_V1.concat("/{id}")).and(accept(APPLICATION_JSON)),
                        handler::deleteItem)
                .andRoute(PUT(ITEM_FUNCTIONAL_END_POINT_V1.concat("/{id}")).and(accept(APPLICATION_JSON)),
                        handler::updateItem);
    }

    @Bean
    public RouterFunction<ServerResponse> errorRoute(ItemsHandler handler) {
        return RouterFunctions.route(GET("/fun/runtimeException").and(accept(APPLICATION_JSON)),
                handler::handleRuntimeExceptions);
    }

    @Bean
    public RouterFunction<ServerResponse> itemStreamRoute(ItemsHandler handler) {
        return RouterFunctions.route(GET(ITEM_STREAM_FUNCTIONAL_END_POINT).and(accept(APPLICATION_JSON)),
                handler::itemsStream);
    }
}
