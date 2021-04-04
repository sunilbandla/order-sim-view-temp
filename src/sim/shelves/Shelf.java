package sim.shelves;

import sim.orders.Order;

import java.util.Map;
import java.util.concurrent.BlockingQueue;

/**
 * Contains properties of shelves.
 */
public enum Shelf {
    HOT("hot", 10),
    COLD("cold", 10),
    FROZEN("frozen", 10),
    OVERFLOW("", 15),
    ;

    private final String temperature;
    private final int capacity;

    Shelf(String temperature, int capacity) {
        this.temperature = temperature;
        this.capacity = capacity;
    }

    static Shelf toShelf(String temperature) {
        switch (temperature) {
            case "hot":
                return HOT;
            case "cold":
                return COLD;
            case "frozen":
                return FROZEN;
            default:
                return OVERFLOW;
        }
    }

    public static void logShelfStats(Map<Shelf, BlockingQueue<Order>> shelfQueues) {
        System.out.print("Shelves: ");
        shelfQueues.keySet().forEach(shelf ->
                System.out.printf("%s - %d; ", shelf, shelfQueues.get(shelf).size()));
        System.out.println();
    }

    public int getCapacity() {
        return capacity;
    }

}
