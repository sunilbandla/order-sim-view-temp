package sim.delivery;

import sim.orders.Order;
import sim.shelves.Shelf;

import java.util.Map;
import java.util.concurrent.BlockingQueue;

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
            Thread.sleep((long) (Math.floor(Math.random() * 5 + 2) * 1000));
            BlockingQueue<Order> queue = shelfQueues.get(shelf);
            boolean isDelivered = queue.removeIf(order -> order.getId().equals(id));
            if (isDelivered) {
                System.out.printf("Delivered order %s from shelf %s.%n", id, shelf);
            } else {
                System.out.printf("Order %s was not found in shelf %s.%n", id, shelf);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
