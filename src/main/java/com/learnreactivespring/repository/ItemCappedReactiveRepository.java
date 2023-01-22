package com.learnreactivespring.repository;

import com.learnreactivespring.document.Item;
import com.learnreactivespring.document.ItemCapped;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ItemCappedReactiveRepository extends ReactiveMongoRepository<Item, String> {

    @Tailable
    Flux<ItemCapped> findItemsBy();
}
