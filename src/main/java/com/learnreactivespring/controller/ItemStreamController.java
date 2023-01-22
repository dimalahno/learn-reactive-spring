package com.learnreactivespring.controller;

import com.learnreactivespring.document.ItemCapped;
import com.learnreactivespring.repository.ItemCappedReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import static com.learnreactivespring.constants.ItemConstants.ITEM_STREAM_END_POINT;
import static org.springframework.http.MediaType.APPLICATION_STREAM_JSON_VALUE;

@RestController
@Slf4j
public class ItemStreamController {

    final ItemCappedReactiveRepository repository;

    public ItemStreamController(ItemCappedReactiveRepository repository) {
        this.repository = repository;
    }

    @GetMapping(value = ITEM_STREAM_END_POINT, produces = APPLICATION_STREAM_JSON_VALUE)
    public Flux<ItemCapped> getItemsStream() {
        return repository.findItemsBy();
    }

}
