package io.quarkuscoffeeshop.counter.infrastructure;

import io.quarkuscoffeeshop.counter.domain.commands.PlaceOrderCommand;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

@ApplicationScoped
public class KafkaService {

    @Inject
    OrderService orderService;

    @Incoming("orders")
    @Transactional
    public void orderIn(final PlaceOrderCommand placeOrderCommand) {
        orderService.onPlaceOrderCommand(placeOrderCommand);
    }
}
