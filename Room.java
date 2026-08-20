public class Room {
    private final int number;
    private final String type;
    private final double pricePerNight;
    private final int capacity;
    private final int ecoScore;
    private boolean available = true;

    public Room(int number, String type, double pricePerNight, int capacity, int ecoScore) {
        this.number = number; this.type = type; this.pricePerNight = pricePerNight;
        this.capacity = capacity; this.ecoScore = ecoScore;
    }
    public int getNumber(){ return number; }
    public String getType(){ return type; }
    public double getPricePerNight(){ return pricePerNight; }
    public int getCapacity(){ return capacity; }
    public int getEcoScore(){ return ecoScore; }
    public boolean isAvailable(){ return available; }
    public void setAvailable(boolean value){ available = value; }
}
