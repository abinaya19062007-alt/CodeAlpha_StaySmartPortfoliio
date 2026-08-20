import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Booking {
    private final String guestName;
    private final Room room;
    private final LocalDate checkIn, checkOut;
    private final int guests;
    private final String preference;
    private final double total;

    public Booking(String guestName, Room room, LocalDate checkIn, LocalDate checkOut,
                   int guests, String preference) {
        this.guestName = guestName; this.room = room; this.checkIn = checkIn;
        this.checkOut = checkOut; this.guests = guests; this.preference = preference;
        this.total = getNights() * room.getPricePerNight();
    }
    public String getGuestName(){ return guestName; }
    public Room getRoom(){ return room; }
    public LocalDate getCheckIn(){ return checkIn; }
    public LocalDate getCheckOut(){ return checkOut; }
    public int getGuests(){ return guests; }
    public String getPreference(){ return preference; }
    public double getTotal(){ return total; }
    public long getNights(){ return Math.max(1, ChronoUnit.DAYS.between(checkIn, checkOut)); }
}
