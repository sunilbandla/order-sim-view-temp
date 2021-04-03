package sim.orders;

import java.io.IOException;
import java.util.Optional;

public interface OrderStore {
  /** Loads all orders from storage and returns all orders. */
  Order[] loadAllOrders() throws IOException;
  /**
   * Returns the next order in store.
   */
  Optional<Order> getNext();
  /** Checks if there are new orders. */
  boolean hasNext();
}
