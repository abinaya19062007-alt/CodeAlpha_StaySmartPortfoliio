import java.io.FileWriter;
import java.io.IOException;

public class ReportExporter {
    public static String export(StayEngine engine) {
        String file="staysmart_bookings.csv";
        try(FileWriter out=new FileWriter(file)) {
            out.write("Guest,Room,Type,CheckIn,CheckOut,Nights,Guests,Preference,Total,EcoScore\n");
            for(Booking b:engine.getBookings()) {
                out.write(b.getGuestName().replace(","," ") + "," +
                    b.getRoom().getNumber()+","+b.getRoom().getType()+","+
                    b.getCheckIn()+","+b.getCheckOut()+","+b.getNights()+","+
                    b.getGuests()+","+b.getPreference()+","+
                    String.format("%.2f",b.getTotal())+","+b.getRoom().getEcoScore()+"\n");
            }
            return file;
        } catch(IOException e) { return "Export failed: "+e.getMessage(); }
    }
}
