package sim.shelves;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static sim.shelves.Shelf.toShelf;

/**
 * Shelf Tester.
 */
public class ShelfTest {

    @Test
    public void toShelf_returnsOverflowShelf() throws Exception {
        assertEquals(toShelf(""), Shelf.OVERFLOW);
    }

}
