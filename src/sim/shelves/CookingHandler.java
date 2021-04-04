package sim.shelves;

import sim.delivery.Courier;
import sim.orders.Order;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static sim.shelves.Shelf.*;

/** Handles cooking and shelving of orders. */
public class CookingHandler implements Runnable {
    private final BlockingQueue<Order> kitchenQueue;
    private final AtomicBoolean areAllOrdersReceived;
    private final Map<Shelf, BlockingQueue<Order>> shelfQueues;
    private final ExecutorService courierService;

    public CookingHandler(BlockingQueue<Order> kitchenQueue, AtomicBoolean areAllOrdersReceived,
                          Map<Shelf, BlockingQueue<Order>> shelfQueues, ExecutorService courierService) {
        this.kitchenQueue = kitchenQueue;
        this.areAllOrdersReceived = areAllOrdersReceived;
        this.shelfQueues = shelfQueues;
        this.courierService = courierService;
    }

    @Override
    public void run() {
        while (!areAllOrdersReceived.get()) {
            try {
                Order newOrder = kitchenQueue.poll(5, TimeUnit.SECONDS);
                if (newOrder == null) {
                    break;
                }
                System.out.println("Cooked " + newOrder);
                Shelf newOrderShelf = toShelf(newOrder.getTemp());
                if (shelfQueues.get(newOrderShelf).size() == newOrderShelf.getCapacity()) {
                    handleOverflow();
                    newOrderShelf = OVERFLOW;
                }
                newOrder.setShelvedAtMillis(Instant.now().toEpochMilli());
                shelfQueues.get(newOrderShelf).put(newOrder);
                System.out.printf("Added %s to %s shelf.%n", newOrder, newOrderShelf);
                courierService.submit(new Courier(newOrder.getId(), toShelf(newOrder.getTemp()), shelfQueues));
                System.out.printf("Dispatched courier for order %s from shelf %s.%n", newOrder.getId(), newOrderShelf);
                logShelfStats(shelfQueues);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("*** Done cooking all orders.");
    }

    /**
     * Check if overflow order can be moved to other shelves. If not, drop an order from it.
     */
    private void handleOverflow() {
        BlockingQueue<Order> overflowQueue = shelfQueues.get(OVERFLOW);
        if (!overflowQueue.isEmpty()) {
            // Items need to be either moved or discarded.
            boolean wereOverflowItemsMoved = overflowQueue.removeIf(x -> {
                Shelf currentShelf = toShelf(x.getTemp());
                boolean canCurrentOrderBeMoved = shelfQueues.get(currentShelf).size() < currentShelf.getCapacity();
                if (canCurrentOrderBeMoved) {
                    try {
                        shelfQueues.get(currentShelf).put(x);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.printf("Moved %s to %s shelf.%n", x, currentShelf);
                }
                return canCurrentOrderBeMoved;
            });
            if (!wereOverflowItemsMoved) {
                // Items need to be discarded.
                Order removedOrder = overflowQueue.remove();
                System.out.printf("Removed %s from OVERFLOW shelf.%n", removedOrder);
            }
        }
    }
}
