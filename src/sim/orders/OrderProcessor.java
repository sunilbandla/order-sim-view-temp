package sim.orders;

import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class OrderProcessor implements Runnable {
  private final int ordersPerSecond;
  private final OrderStore orderStore;
  private final BlockingQueue<Order> kitchenQueue;
  private final AtomicBoolean areAllOrdersReceived;

  public OrderProcessor(
          int ordersPerSecond, OrderStore orderStore, BlockingQueue<Order> kitchenQueue, AtomicBoolean areAllOrdersReceived) {
    this.ordersPerSecond = ordersPerSecond;
    this.orderStore = orderStore;
    this.kitchenQueue = kitchenQueue;
    this.areAllOrdersReceived = areAllOrdersReceived;
  }

  @Override
  public void run() {
    while (!areAllOrdersReceived.get()) {
      System.out.println("Receiving orders...");
      for (int i = 0; i < ordersPerSecond; i++) {
        if (orderStore.hasNext()) {
          Order nextOrder = orderStore.getNext().get();
          System.out.println("Received " + nextOrder);
          kitchenQueue.add(nextOrder);
        } else {
          System.out.println("*** Done receiving all orders.");
          areAllOrdersReceived.set(true);
          break;
        }
      }
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}
