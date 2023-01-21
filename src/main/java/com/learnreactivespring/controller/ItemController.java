package com.learnreactivespring.controller;

import com.learnreactivespring.document.Item;
import com.learnreactivespring.repository.ItemReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.learnreactivespring.constants.ItemConstants.ITEM_END_POINT_V1;

@RestController
@Slf4j
public class ItemController {

    @ExceptionHandler
    public ResponseEntity<String> handleRuntimeException(RuntimeException exception) {
        log.error("Exception caught in handleRuntimeException : {} ", exception.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
    }

    final ItemReactiveRepository itemRepository;

    public ItemController(ItemReactiveRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @GetMapping(ITEM_END_POINT_V1)
    public Flux<Item> getAllItems() {
        return itemRepository.findAll();
    }

    @GetMapping(ITEM_END_POINT_V1+"/{id}")
    public Mono<ResponseEntity<Item>> getOneItem(@PathVariable String id) {
        return itemRepository.findById(id)
                .map(item -> new ResponseEntity<>(item, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping(ITEM_END_POINT_V1)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Item> createItem(@RequestBody Item item) {
        return itemRepository.save(item);
    }

    @DeleteMapping(ITEM_END_POINT_V1+"/{id}")
    public Mono<Void> deleteItem(@PathVariable String id){
        return itemRepository.deleteById(id);
    }

    @PutMapping(ITEM_END_POINT_V1+"/{id}")
    public Mono<ResponseEntity<Item>> updateItem(@PathVariable String id, @RequestBody Item item) {
        return itemRepository.findById(id)
                .flatMap(currentItem -> {
                    currentItem.setPrice(item.getPrice());
                    currentItem.setDescription(item.getDescription());
                    return itemRepository.save(currentItem);
                })
                .map(updatedItem -> new ResponseEntity<>(updatedItem, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping(ITEM_END_POINT_V1+"/runtimeException")
    public Flux<Item> runtimeException() {
        return itemRepository.findAll()
                .concatWith(Mono.error(new RuntimeException("RuntimeException occurred!")));
    }
}
