package io.quarkuscoffeeshop.counter.infrastructure;

import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import io.quarkuscoffeeshop.counter.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@QuarkusMain
public class MockerService implements QuarkusApplication {

    final Logger logger = LoggerFactory.getLogger(MockerService.class);

    @Inject
    OrderService orderService;

    private boolean running = true;

    private Runnable mockOrders = () -> {

        logger.debug("Mocker now running");

        while (running) {
            try {
                Thread.sleep(3000);
                int orders = new Random().nextInt(33);
                String loyaltyMemberId = null;
                if(orders %3 == 0){
                    loyaltyMemberId = UUID.randomUUID().toString();
                }
                if (orders %2 == 0) {
                    PlaceOrderCommand placeOrderCommand = new PlaceOrderCommand(
                            UUID.randomUUID().toString(),
                            "WEB",
                            loyaltyMemberId,
                            new ArrayList<LineItem>() {{
                                add(new LineItem(Item.COFFEE_BLACK, "Paul"));
                            }},
                            null);
                    OrderEvent orderEvent = Order.process(placeOrderCommand);
                    orderService.onPlaceOrderCommand(placeOrderCommand);
                }else{
                    PlaceOrderCommand placeOrderCommand = new PlaceOrderCommand(UUID.randomUUID().toString(),
                            "WEB",
                            loyaltyMemberId,
                            new ArrayList<LineItem>() {{
                                add(new LineItem(Item.COFFEE_BLACK, "Paul"));
                            }},
                            new ArrayList<LineItem>() {{
                                add(new LineItem(Item.CAKEPOP, "John"));
                            }});
                    OrderEvent orderEvent = Order.process(placeOrderCommand);
                    orderService.onPlaceOrderCommand(placeOrderCommand);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public int run(String... args) throws Exception {
        this.running = true;
        logger.info("starting");
        mockOrders.run();
        return 10;
    }
}
