package sim;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import sim.orders.Order;
import sim.orders.OrderProcessor;
import sim.orders.OrderStore;
import sim.orders.OrderStoreImpl;

public class SimulationRunner {
  private static LinkedList<Order> kitchenQueue = new LinkedList<>();
  public static void main(String[] args) throws IOException {
    System.out.println("\n\n***** Started simulator. *****\n\n");

    OrderStore store = new OrderStoreImpl();
    store.loadAllOrders();

    Runnable orderProcessorRunner = new OrderProcessor(2, store, kitchenQueue);
    Thread orderProcessor = new Thread(orderProcessorRunner);
    orderProcessor.start();

    System.out.println("\n\n***** Simulator ended. *****\n\n");
  }
}
