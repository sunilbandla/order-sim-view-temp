package sim.orders;

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

    public double getDecayRate() {
        return decayRate;
    }

    public void setDecayRate(double decayRate) {
        this.decayRate = decayRate;
    }

    public long getShelfLife() {
        return shelfLife;
    }

    public void setShelfLife(long shelfLife) {
        this.shelfLife = shelfLife;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setShelvedAtMillis(long shelvedAtMillis) {
        this.shelvedAtMillis = shelvedAtMillis;
    }

    @Override
    public String toString() {
        return "Order {"
                + "id='" + id + '\'' + ", name='" + name + '\'' + ", temp='" + temp + '\''
                + ", shelfLife=" + shelfLife + ", decayRate=" + decayRate + ", shelvedAtMillis=" + shelvedAtMillis + '}';
    }
}
