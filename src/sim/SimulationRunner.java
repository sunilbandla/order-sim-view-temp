package sim;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import sim.orders.Order;
import sim.orders.OrderProcessor;
import sim.orders.OrderStore;
import sim.orders.OrderStoreImpl;
import sim.shelves.CookingHandler;
import sim.shelves.Shelf;

import static sim.shelves.Shelf.*;

public class SimulationRunner {
  private static BlockingQueue<Order> kitchenQueue = new LinkedBlockingQueue<>();
  private static Map<Shelf, BlockingQueue<Order>> shelfQueues = new HashMap<>();
  private static AtomicBoolean areAllOrdersReceived = new AtomicBoolean(false);
  public static void main(String[] args) throws IOException, InterruptedException {
    System.out.println("\n\n***** Started simulator. *****\n\n");

    // Initialize.
    shelfQueues.put(HOT, new LinkedBlockingQueue<>(HOT.getCapacity()));
    shelfQueues.put(COLD, new LinkedBlockingQueue<>(COLD.getCapacity()));
    shelfQueues.put(FROZEN, new LinkedBlockingQueue<>(FROZEN.getCapacity()));
    shelfQueues.put(OVERFLOW, new LinkedBlockingQueue<>(OVERFLOW.getCapacity()));

    OrderStore store = new OrderStoreImpl();
    store.loadAllOrders();

    Runnable orderProcessorRunner = new OrderProcessor(2, store, kitchenQueue, areAllOrdersReceived);
    Thread orderProcessor = new Thread(orderProcessorRunner);
    orderProcessor.start();

    Runnable cookingHandlerRunner = new CookingHandler(kitchenQueue, areAllOrdersReceived, shelfQueues);
    Thread cookingHandler = new Thread(cookingHandlerRunner);
    cookingHandler.start();

    orderProcessor.join();
    cookingHandler.join();
    System.out.println("\n\n***** Simulator ended. *****\n\n");
  }
}
