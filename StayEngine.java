import java.time.LocalDate;
import java.util.*;

public class StayEngine {
    private final List<Room> rooms = new ArrayList<>();
    private final List<Booking> bookings = new ArrayList<>();

    public StayEngine() {
        rooms.add(new Room(101,"Eco Single",1800,1,95));
        rooms.add(new Room(102,"Smart Single",2200,1,88));
        rooms.add(new Room(201,"Comfort Double",3200,2,82));
        rooms.add(new Room(202,"Garden Double",3600,2,92));
        rooms.add(new Room(301,"Family Suite",5200,4,76));
        rooms.add(new Room(302,"Premium Suite",6800,4,70));
        rooms.add(new Room(401,"Work+Stay Studio",4400,2,86));
        rooms.add(new Room(402,"Quiet Retreat",3900,2,97));
    }

    public List<Room> getRooms(){ return rooms; }
    public List<Booking> getBookings(){ return bookings; }

    public List<Room> recommend(int guests, String preference, double budget) {
        List<Room> result = new ArrayList<>();
        for(Room r: rooms) {
            if(!r.isAvailable() || r.getCapacity()<guests || r.getPricePerNight()>budget) continue;
            boolean match = preference.equals("Any")
                || (preference.equals("Eco-friendly") && r.getEcoScore()>=90)
                || (preference.equals("Quiet") && r.getType().contains("Quiet"))
                || (preference.equals("Work") && r.getType().contains("Work"))
                || (preference.equals("Family") && r.getCapacity()>=4);
            if(match) result.add(r);
        }
        return result;
    }

    public Booking book(String name, Room room, LocalDate in, LocalDate out,
                        int guests, String preference) {
        if(name.trim().isEmpty() || room==null || !room.isAvailable()
                || !out.isAfter(in) || guests<1 || guests>room.getCapacity()) return null;
        Booking b = new Booking(name,room,in,out,guests,preference);
        bookings.add(b); room.setAvailable(false); return b;
    }

    public boolean cancelLatest() {
        if(bookings.isEmpty()) return false;
        Booking b=bookings.remove(bookings.size()-1);
        b.getRoom().setAvailable(true); return true;
    }

    public double occupancyPercent() {
        long used=rooms.stream().filter(r->!r.isAvailable()).count();
        return used*100.0/rooms.size();
    }
}
