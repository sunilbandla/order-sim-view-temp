package sim.shelves;

import sim.orders.Order;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static sim.shelves.Shelf.OVERFLOW;

public class CookingHandler implements Runnable {
    private final BlockingQueue<Order> kitchenQueue;
    private final AtomicBoolean areAllOrdersReceived;
    private final Map<Shelf, BlockingQueue<Order>> shelfQueues;

    public CookingHandler(BlockingQueue<Order> kitchenQueue, AtomicBoolean areAllOrdersReceived, Map<Shelf, BlockingQueue<Order>> shelfQueues) {
        this.kitchenQueue = kitchenQueue;
        this.areAllOrdersReceived = areAllOrdersReceived;
        this.shelfQueues = shelfQueues;
    }

    @Override
    public void run() {
        do {
            try {
                Order newOrder = kitchenQueue.poll(5, TimeUnit.SECONDS);
                if (newOrder == null) {
                    System.out.println("*** Done cooking all orders.");
                    return;
                }
                System.out.println("Cooked " + newOrder);
                Shelf newOrderShelf = Shelf.toShelf(newOrder.getTemp());
                if (shelfQueues.get(newOrderShelf).size() == newOrderShelf.getCapacity()) {
                    handleOverflow();
                    newOrderShelf = OVERFLOW;
                }
                newOrder.setShelvedAtMillis(Instant.now().toEpochMilli());
                shelfQueues.get(newOrderShelf).put(newOrder);
                System.out.printf("Added %s to %s shelf.%n", newOrder, newOrderShelf);
                logShelfStats();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (!areAllOrdersReceived.get());
    }


    private void logShelfStats() {
        System.out.println("\nShelf stats:");
        shelfQueues.keySet().forEach(shelf ->
                System.out.printf("%s - %d%n", shelf, shelfQueues.get(shelf).size()));
        System.out.println();
    }

    /**
     * Check if overflow order can be moved to other shelves. If not, drop an order from it.
     */
    private void handleOverflow() {
        BlockingQueue<Order> overflowQueue = shelfQueues.get(OVERFLOW);
        if (!overflowQueue.isEmpty()) {
            // Items need to be either moved or discarded.
            boolean wereOverflowItemsMoved = overflowQueue.removeIf(x -> {
                Shelf currentShelf = Shelf.toShelf(x.getTemp());
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
