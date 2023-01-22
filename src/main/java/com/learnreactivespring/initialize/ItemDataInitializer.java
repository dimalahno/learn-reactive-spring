package com.learnreactivespring.initialize;

import com.learnreactivespring.document.Item;
import com.learnreactivespring.document.ItemCapped;
import com.learnreactivespring.repository.ItemCappedReactiveRepository;
import com.learnreactivespring.repository.ItemReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
@Profile("!test")
public class ItemDataInitializer implements CommandLineRunner {

    final ItemReactiveRepository itemRepository;
    final ItemCappedReactiveRepository itemCappedReactiveRepository;

    public ItemDataInitializer(ItemReactiveRepository itemRepository,
                               ItemCappedReactiveRepository itemCappedReactiveRepository) {
        this.itemRepository = itemRepository;
        this.itemCappedReactiveRepository = itemCappedReactiveRepository;
    }

    @Override
    public void run(String... args) {
        initialDataSetUp();
        dataSetUpForCappedCollection();
    }

    public void dataSetUpForCappedCollection() {
        Flux<ItemCapped> itemCappedFlux = Flux.interval(Duration.ofSeconds(1))
                .map(i -> new ItemCapped(null, "RandomItem " + i, (100.00 + i)));

        itemCappedReactiveRepository.insert(itemCappedFlux)
                .subscribe(itemCapped -> log.info("Inserted Item is : {}", itemCapped));
    }

    private List<Item> data() {
        return Arrays.asList(
                new Item(null, "Samsung TV", 400.0),
                new Item(null, "LG TV", 420.0),
                new Item(null, "Apple Watch", 299.99),
                new Item("ABC", "Beats Headphones", 149.99)
        );
    }

    private void initialDataSetUp() {
        itemRepository.deleteAll()
                .thenMany(Flux.fromIterable(data()))
                .flatMap(itemRepository::save)
                .thenMany(itemRepository.findAll())
                .subscribe(item -> log.info("Item inserted from CommandLineRunner : {}", item));
    }
}
