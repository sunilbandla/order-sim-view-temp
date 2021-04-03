package sim.orders;

import java.util.LinkedList;

public class OrderProcessor implements Runnable {
  private final int ordersPerSecond;
  private final OrderStore orderStore;
  private final LinkedList<Order> kitchenQueue;

  public OrderProcessor(
      int ordersPerSecond, OrderStore orderStore, LinkedList<Order> kitchenQueue) {
    this.ordersPerSecond = ordersPerSecond;
    this.orderStore = orderStore;
    this.kitchenQueue = kitchenQueue;
  }

  @Override
  public void run() {
    boolean canExit = false;
    while (!canExit) {
      System.out.println("Receiving orders...");
      for (int i = 0; i < ordersPerSecond; i++) {
        if (orderStore.hasNext()) {
          Order nextOrder = orderStore.getNext().get();
          System.out.println("Received order: " + nextOrder);
          kitchenQueue.add(nextOrder);
        } else {
          System.out.println("*** Done receiving all orders.");
          canExit = true;
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
