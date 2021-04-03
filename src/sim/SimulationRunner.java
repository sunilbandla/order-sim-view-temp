package sim;

import sim.orders.OrderStore;
import sim.orders.OrderStoreImpl;

import java.io.IOException;

public class SimulationRunner {
    public static void main(String[] args) throws IOException {
        System.out.println("\n\n***** Started simulator. *****\n\n");

        OrderStore store = new OrderStoreImpl();
        store.loadAllOrders();

        System.out.println("\n\n***** Simulator ended. *****\n\n");
    }
}
