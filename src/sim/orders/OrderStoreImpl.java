package sim.orders;

import com.google.gson.Gson;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.Optional;
public class OrderStoreImpl implements OrderStore {
  Order[] data;
  int nextIndex = 0;

  /**
   * Loads all orders from storage and returns all orders.
   */
  @Override
  public Order[] loadAllOrders() throws IOException {
    String json = new String(
        Files.readAllBytes(FileSystems.getDefault().getPath("src/sim/orders", "orders.json")));

    data = new Gson().fromJson(json, Order[].class);

    System.out.println("Loaded orders.");
    return data;
  }

  /**
   * Returns the next order in store.
   */
  @Override
  public Optional<Order> getNext() {
    if (data == null || nextIndex >= data.length) {
      return Optional.empty();
    }
    return Optional.of(data[nextIndex++]);
  }
}
