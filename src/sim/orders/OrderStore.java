package sim.orders;

import java.io.IOException;
import java.util.Optional;

public interface OrderStore {
    /** Loads all orders from storage and returns all orders. */
    Order[] loadAllOrders() throws IOException;
    /** Returns the next order in store.
     * @return*/
    Optional<Order> getNext();
}
