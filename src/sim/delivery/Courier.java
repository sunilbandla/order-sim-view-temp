package sim.delivery;

import sim.orders.Order;
import sim.shelves.Shelf;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import static sim.shelves.Shelf.OVERFLOW;

/**
 * A courier that will take 2-6 seconds to deliver an order.
 */
public class Courier implements Runnable {
    private final String id;
    private final Shelf shelf;
    private final Map<Shelf, BlockingQueue<Order>> shelfQueues;

    public Courier(String id, Shelf shelf, Map<Shelf, BlockingQueue<Order>> shelfQueues) {
        this.id = id;
        this.shelf = shelf;
        this.shelfQueues = shelfQueues;
    }

    @Override
    public void run() {
        try {
            // Courier arrives between 2 - 6 seconds later.
            Thread.sleep((long) (Math.floor(Math.random() * 5 + 2) * 1000));
            boolean isDelivered = deliverOrderFromShelf(shelfQueues, shelf, id);
            if (!isDelivered) {
                // Check if order was moved to overflow shelf.
                deliverOrderFromShelf(shelfQueues, OVERFLOW, id);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private boolean deliverOrderFromShelf(Map<Shelf, BlockingQueue<Order>> shelfQueues, Shelf shelf, String id) {
        BlockingQueue<Order> queue = shelfQueues.get(shelf);
        // Remove stale orders.
        AtomicBoolean isCurrentOrderRemoved = new AtomicBoolean(false);
        queue.removeIf(order -> {
            if (order.getOrderValue(shelf) == 0) {
                if (order.getId().equals(id)) {
                    isCurrentOrderRemoved.set(true);
                }
                System.out.printf("Removing stale order %s from shelf %s.%n", order.getId(), shelf);
                return true;
            }
            return false;
        });
        if (isCurrentOrderRemoved.get()) {
            return true;
        }
        boolean isDelivered = queue.removeIf(order -> order.getId().equals(id));
        if (isDelivered) {
            System.out.printf("Delivered order %s from shelf %s.%n", id, shelf);
        } else {
            System.out.printf("Order %s was not found in shelf %s.%n", id, shelf);
        }
        return isDelivered;
    }
}
