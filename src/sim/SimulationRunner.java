package sim;

import sim.orders.Order;
import sim.orders.OrderProcessor;
import sim.orders.OrderStore;
import sim.orders.OrderStoreImpl;
import sim.shelves.CookingHandler;
import sim.shelves.Shelf;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static sim.shelves.Shelf.*;

/**
 * Orchestrates receiving, cooking and delivering orders.
 */
public class SimulationRunner {
    private static final ExecutorService courierService = Executors.newFixedThreadPool(10);
    private static final BlockingQueue<Order> kitchenQueue = new LinkedBlockingQueue<>();
    private static final Map<Shelf, BlockingQueue<Order>> shelfQueues = new HashMap<>();
    private static final AtomicBoolean areAllOrdersReceived = new AtomicBoolean(false);

    public static void main(String[] args) throws IOException, InterruptedException {
        int ordersPerSecond = getOrdersPerSecondFromInput(args);
        System.out.println("\n\n***** Started simulator. *****\n\n");

        // Initialize.
        shelfQueues.put(HOT, new LinkedBlockingQueue<>(HOT.getCapacity()));
        shelfQueues.put(COLD, new LinkedBlockingQueue<>(COLD.getCapacity()));
        shelfQueues.put(FROZEN, new LinkedBlockingQueue<>(FROZEN.getCapacity()));
        shelfQueues.put(OVERFLOW, new LinkedBlockingQueue<>(OVERFLOW.getCapacity()));

        OrderStore store = new OrderStoreImpl();
        store.loadAllOrders();

        Runnable orderProcessorRunner = new OrderProcessor(ordersPerSecond, store, kitchenQueue, areAllOrdersReceived);
        Thread orderProcessor = new Thread(orderProcessorRunner);
        orderProcessor.start();

        Runnable cookingHandlerRunner = new CookingHandler(kitchenQueue, areAllOrdersReceived, shelfQueues, courierService);
        Thread cookingHandler = new Thread(cookingHandlerRunner);
        cookingHandler.start();

        orderProcessor.join();
        cookingHandler.join();

        // Wait for all couriers to be done.
        courierService.shutdown();
        try {
            courierService.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("*** Stopped courier service.");

        logShelfStats(shelfQueues);
        System.out.println("\n\n***** Simulator ended. *****\n\n");
    }

    private static int getOrdersPerSecondFromInput(String[] args) {
        if (args.length > 0) {
            try {
                return Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.out.println("Invalid order rate. " + args[0] + " must be an integer.");
            }
        }
        return 2;
    }
}
