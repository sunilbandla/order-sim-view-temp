package sim.shelves;

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

    public int getCapacity() {
        return capacity;
    }
}
