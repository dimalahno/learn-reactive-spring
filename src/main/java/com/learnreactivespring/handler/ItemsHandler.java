package com.learnreactivespring.handler;

import com.learnreactivespring.document.Item;
import com.learnreactivespring.repository.ItemReactiveRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

import static com.learnreactivespring.constants.ItemConstants.ITEM_FUNCTIONAL_END_POINT_V1;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@Component
public class ItemsHandler {

    static Mono<ServerResponse> notFound = ServerResponse.notFound().build();
    private final String ID = "id";

    final ItemReactiveRepository repository;

    public ItemsHandler(ItemReactiveRepository repository) {
        this.repository = repository;
    }

    public Mono<ServerResponse> getAllItems(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(repository.findAll(), Item.class);
    }

    public Mono<ServerResponse> getOneItem(ServerRequest request) {
        var id = request.pathVariable(ID);
        Mono<Item> itemMono = repository.findById(id);
        return itemMono.flatMap(item -> ServerResponse.ok().contentType(APPLICATION_JSON).body(fromValue(item)))
                .switchIfEmpty(notFound);
    }

    public Mono<ServerResponse> createItem(ServerRequest request) {
        Mono<Item> createdItem = request.bodyToMono(Item.class);
        return createdItem.flatMap(item -> ServerResponse.created(URI.create(ITEM_FUNCTIONAL_END_POINT_V1))
                .contentType(APPLICATION_JSON)
                .body(repository.save(item), Item.class));
    }

    public Mono<ServerResponse> deleteItem(ServerRequest request) {
        var id = request.pathVariable(ID);
        Mono<Void> deletedItem = repository.deleteById(id);
        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(deletedItem, Void.class);
    }

    public Mono<ServerResponse> updateItem(ServerRequest request) {
        var id = request.pathVariable(ID);
        Mono<Item> updatedItem = request.bodyToMono(Item.class)
                .flatMap(item -> repository.findById(id)
                                            .flatMap(currentItem -> {
                                                currentItem.setDescription(item.getDescription());
                                                currentItem.setPrice(item.getPrice());
                                                return repository.save(currentItem);
                                            }));
        return updatedItem.flatMap(item -> ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(fromValue(item)))
                .switchIfEmpty(notFound);
    }
}
