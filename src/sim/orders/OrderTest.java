package sim.orders;

import org.junit.Test;
import sim.shelves.Shelf;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Order Tester.
 */
public class OrderTest {

    private final static String ID = "id";
    private final static String NAME = "name";
    private final static String TEMP_COLD = "cold";

    @Test
    public void getOrderValue_zeroShelfLife_returnsZeroValue() {
        Order order = new Order(ID, NAME, TEMP_COLD, 0, 1);
        assertEquals(order.getOrderValue(Shelf.COLD), 0);
    }

    @Test
    public void getOrderValue_positiveShelfLife_zeroDecayRate_returnsNegativeValue() {
        Order order = new Order(ID, NAME, TEMP_COLD, 1, 0);
        assertTrue(order.getOrderValue(Shelf.COLD) < 0);
    }

}
