package sim.shelves;

import sim.orders.Order;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

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
        while (!areAllOrdersReceived.get()) {
            try {
                Order newOrder = kitchenQueue.take();
                System.out.println("Cooked " + newOrder);
                Shelf newOrderShelf = Shelf.toShelf(newOrder.getTemp());
                shelfQueues.get(newOrderShelf).put(newOrder);
                System.out.printf("Added %s to %s shelf.%n", newOrder, newOrderShelf);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
