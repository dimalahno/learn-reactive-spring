package com.learnreactivespring.repository;

import com.learnreactivespring.document.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@DataMongoTest
@ExtendWith(SpringExtension.class)
public class ItemReactiveRepositoryTest {

    static {
        System.setProperty("spring.mongodb.embedded.version","5.0.0");
    }

    @Autowired
    ItemReactiveRepository repository;

    List<Item> itemList = Arrays.asList(new Item(null, "Samsung TV", 400.0),
            new Item(null, "LG TV", 420.0),
            new Item(null, "Apple Watch", 299.99),
            new Item(null, "Beats Headphones", 149.99),
            new Item("ABC", "Bose Headphones", 149.99));

    @BeforeEach
    public void setUp() {
        repository.deleteAll()
                .thenMany(Flux.fromIterable(itemList))
                .flatMap(repository::save)
                .doOnNext(item -> System.out.println("Inserted Item is: " + item))
                .blockLast();
    }

    @Test
    public void getAllItems() {
        StepVerifier.create(repository.findAll())
                .expectSubscription()
                .expectNextCount(5)
                .verifyComplete();
    }

    @Test
    public void getItemById() {
        StepVerifier.create(repository.findById("ABC"))
                .expectSubscription()
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    public void getItemById1() {
        StepVerifier.create(repository.findById("ABC"))
                .expectSubscription()
                .expectNextMatches(item -> item.getDescription().equals("Bose Headphones"))
                .verifyComplete();
    }

    @Test
    public void findItemByDescription() {
        StepVerifier.create(repository.findByDescription("Bose Headphones"))
                .expectSubscription()
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    public void saveItem() {
        Item item = new Item(null, "Google Home Mini", 30.00);
        Mono<Item> savedItem = repository.save(item);
        StepVerifier.create(savedItem)
                .expectSubscription()
                .expectNextMatches(item1 -> Objects.nonNull(item1) &&
                        Objects.equals(item1.getDescription(), "Google Home Mini"))
                .verifyComplete();
    }

    @Test
    public void updateItem() {
        var newPrice = 520.00;
        Mono<Item> updatedItems = repository.findByDescription("LG TV")
                .map(item -> {
                    item.setPrice(newPrice);
                    return item;
                })
                .flatMap(item -> repository.save(item));

        StepVerifier.create(updatedItems)
                .expectSubscription()
                .expectNextMatches(item -> item.getPrice() == 520.00)
                .verifyComplete();
    }

    @Test
    public void deleteItem() {
        var deletedItem = repository.findById("ABC")
                .map(Item::getId)
                .flatMap(id -> repository.deleteById(id));

        StepVerifier.create(deletedItem)
                .expectSubscription()
                .verifyComplete();

        StepVerifier.create(repository.findAll().log())
                .expectSubscription()
                .expectNextCount(4)
                .verifyComplete();
    }

    @Test
    public void deleteItemByDescription() {
        var deletedItem = repository.findByDescription("LG TV")
                .flatMap(item -> repository.delete(item));

        StepVerifier.create(deletedItem)
                .expectSubscription()
                .verifyComplete();

        StepVerifier.create(repository.findAll().log())
                .expectSubscription()
                .expectNextCount(4)
                .verifyComplete();
    }
}
