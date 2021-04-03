package sim;

import java.io.IOException;
import sim.orders.OrderStore;
import sim.orders.OrderStoreImpl;

public class SimulationRunner {
  public static void main(String[] args) throws IOException {
    System.out.println("\n\n***** Started simulator. *****\n\n");

    OrderStore store = new OrderStoreImpl();
    store.loadAllOrders();

    System.out.println("\n\n***** Simulator ended. *****\n\n");
  }
}
