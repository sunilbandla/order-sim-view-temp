package sim.orders;

import sim.shelves.Shelf;

import java.time.Instant;

/**
 * Contains order details like ID, life, etc..
 */
public class Order {
    private String id;
    private String name;
    private String temp;
    private long shelfLife;
    private double decayRate;
    private long shelvedAtMillis;

    public Order(String id, String name, String temp, int shelfLife, int decayRate) {
        this.id = id;
        this.name = name;
        this.temp = temp;
        this.shelfLife = shelfLife;
        this.decayRate = decayRate;
    }

    public String getTemp() {
        return temp;
    }

    public String getId() {
        return id;
    }

    public void setShelvedAtMillis(long shelvedAtMillis) {
        this.shelvedAtMillis = shelvedAtMillis;
    }

    public double getOrderValue(Shelf shelf) {
        long age = Instant.now().toEpochMilli() - shelvedAtMillis;
        return Math.floor((shelfLife - age - decayRate * age * shelf.getDecayModifier()) / shelfLife);
    }

    @Override
    public String toString() {
        return "Order {"
                + "id='" + id + '\'' + ", name='" + name + '\'' + ", temp='" + temp + '\''
                + ", shelfLife=" + shelfLife + ", decayRate=" + decayRate + ", shelvedAtMillis=" + shelvedAtMillis + '}';
    }
}
