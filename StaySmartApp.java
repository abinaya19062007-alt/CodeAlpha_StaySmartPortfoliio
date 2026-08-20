import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class StaySmartApp extends JFrame {
    private final StayEngine engine=new StayEngine();
    private final Color TEAL=new Color(0,121,107), DARK=new Color(30,55,58);
    private final Color CREAM=new Color(250,248,242), ORANGE=new Color(239,126,60);
    private JLabel occupancyLabel,availableLabel,bookingLabel;
    private JTextArea logArea;
    private JComboBox<String> roomBox,preferenceBox;
    private JTextField guestField;
    private JSpinner guestSpinner,budgetSpinner;

    public StaySmartApp(){
        setTitle("StaySmart | Intelligent Stay Planner");
        setSize(1120,700); setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); buildUI(); refreshRoomChoices();
    }

    private void buildUI(){
        JPanel root=new JPanel(new BorderLayout()); root.setBackground(CREAM);

        JPanel header=new JPanel(new BorderLayout()); header.setBackground(TEAL);
        header.setBorder(new EmptyBorder(18,25,18,25));
        JLabel title=new JLabel("STAYSMART");
        title.setForeground(Color.WHITE); title.setFont(new Font("Arial",Font.BOLD,27));
        JLabel sub=new JLabel("  Intelligent Hotel Reservation & Stay Planner");
        sub.setForeground(new Color(220,250,245)); sub.setFont(new Font("Arial",Font.PLAIN,13));
        JPanel brand=new JPanel(new GridLayout(2,1)); brand.setOpaque(false);
        brand.add(title); brand.add(sub);
        JLabel badge=new JLabel("  SMART MATCH  "); badge.setOpaque(true);
        badge.setBackground(new Color(255,224,178)); badge.setForeground(DARK);
        header.add(brand,BorderLayout.WEST); header.add(badge,BorderLayout.EAST);
        root.add(header,BorderLayout.NORTH);

        JPanel stats=new JPanel(new GridLayout(1,3,15,15)); stats.setBackground(CREAM);
        stats.setBorder(new EmptyBorder(18,20,10,20));
        occupancyLabel=valueLabel("0%"); availableLabel=valueLabel("8"); bookingLabel=valueLabel("0");
        stats.add(card("HOTEL OCCUPANCY",occupancyLabel,ORANGE));
        stats.add(card("AVAILABLE ROOMS",availableLabel,TEAL));
        stats.add(card("ACTIVE BOOKINGS",bookingLabel,DARK));

        JPanel center=new JPanel(new GridLayout(1,2,18,18)); center.setBackground(CREAM);
        center.setBorder(new EmptyBorder(8,20,15,20));

        JPanel left=new JPanel(new BorderLayout()); left.setBackground(Color.WHITE);
        JLabel lt=new JLabel("  ROOM DISCOVERY"); lt.setFont(new Font("Arial",Font.BOLD,18));
        lt.setForeground(DARK); lt.setBorder(new EmptyBorder(12,5,12,5));
        String[] cols={"Room","Type","₹/Night","Capacity","Eco"};
        Object[][] data=new Object[engine.getRooms().size()][5];
        for(int i=0;i<engine.getRooms().size();i++){
            Room r=engine.getRooms().get(i); data[i][0]=r.getNumber(); data[i][1]=r.getType();
            data[i][2]="₹"+String.format("%.0f",r.getPricePerNight());
            data[i][3]=r.getCapacity(); data[i][4]=r.getEcoScore()+"/100";
        }
        JTable table=new JTable(data,cols); table.setRowHeight(31);
        table.getTableHeader().setBackground(new Color(225,242,238));
        table.getTableHeader().setForeground(DARK);
        left.add(lt,BorderLayout.NORTH); left.add(new JScrollPane(table),BorderLayout.CENTER);

        JPanel right=new JPanel(new BorderLayout()); right.setBackground(Color.WHITE);
        JLabel rt=new JLabel("  PLAN YOUR STAY"); rt.setFont(new Font("Arial",Font.BOLD,18));
        rt.setForeground(DARK); rt.setBorder(new EmptyBorder(12,5,12,5));
        JPanel form=new JPanel(new GridLayout(6,2,10,10)); form.setBackground(Color.WHITE);
        form.setBorder(new EmptyBorder(15,18,10,18));
        guestField=new JTextField();
        guestSpinner=new JSpinner(new SpinnerNumberModel(2,1,6,1));
        preferenceBox=new JComboBox<>(new String[]{"Any","Eco-friendly","Quiet","Work","Family"});
        budgetSpinner=new JSpinner(new SpinnerNumberModel(5000.0,1000.0,20000.0,500.0));
        roomBox=new JComboBox<>();
        JButton recommend=new JButton("FIND SMART MATCH"),book=new JButton("CONFIRM BOOKING");
        styleButton(recommend,ORANGE,Color.WHITE); styleButton(book,TEAL,Color.WHITE);
        form.add(new JLabel("Guest name")); form.add(guestField);
        form.add(new JLabel("Guests")); form.add(guestSpinner);
        form.add(new JLabel("Preference")); form.add(preferenceBox);
        form.add(new JLabel("Max ₹/night")); form.add(budgetSpinner);
        form.add(new JLabel("Recommended room")); form.add(roomBox);
        form.add(recommend); form.add(book);
        right.add(rt,BorderLayout.NORTH); right.add(form,BorderLayout.CENTER);
        logArea=new JTextArea(7,30); logArea.setEditable(false);
        logArea.setText("STAYSMART ACTIVITY\n------------------\nChoose your preferences and find a smart room match.");
        right.add(new JScrollPane(logArea),BorderLayout.SOUTH);
        center.add(left); center.add(right);

        JPanel middle=new JPanel(new BorderLayout()); middle.setBackground(CREAM);
        middle.add(stats,BorderLayout.NORTH); middle.add(center,BorderLayout.CENTER);

        JPanel footer=new JPanel(new BorderLayout()); footer.setBackground(DARK);
        footer.setBorder(new EmptyBorder(9,20,9,20));
        JLabel ft=new JLabel("StaySmart • Reservation simulation • Eco-aware matching");
        ft.setForeground(Color.WHITE);
        JButton export=new JButton("Export CSV"),cancel=new JButton("Cancel Latest");
        styleButton(export,Color.WHITE,DARK); styleButton(cancel,new Color(255,235,225),ORANGE);
        JPanel actions=new JPanel(); actions.setOpaque(false); actions.add(export); actions.add(cancel);
        footer.add(ft,BorderLayout.WEST); footer.add(actions,BorderLayout.EAST);

        root.add(middle,BorderLayout.CENTER); root.add(footer,BorderLayout.SOUTH); setContentPane(root);

        recommend.addActionListener(e->findMatches());
        book.addActionListener(e->confirmBooking());
        export.addActionListener(e->JOptionPane.showMessageDialog(this,"Report: "+ReportExporter.export(engine)));
        cancel.addActionListener(e->{if(engine.cancelLatest()){logArea.append("\nBooking cancelled.");updateStats();refreshRoomChoices();}else JOptionPane.showMessageDialog(this,"No active booking.");});
    }

    private void findMatches(){
        int guests=(Integer)guestSpinner.getValue(); String pref=preferenceBox.getSelectedItem().toString();
        double budget=(Double)budgetSpinner.getValue(); List<Room> matches=engine.recommend(guests,pref,budget);
        roomBox.removeAllItems();
        for(Room r:matches) roomBox.addItem(r.getNumber()+" - "+r.getType());
        logArea.append(matches.isEmpty()?"\nNo room matches selected preferences.":"\nSmart match found: "+matches.get(0).getNumber()+" | Eco "+matches.get(0).getEcoScore()+"/100");
    }

    private void confirmBooking(){
        if(roomBox.getSelectedItem()==null){JOptionPane.showMessageDialog(this,"Find a room match first.");return;}
        int no=Integer.parseInt(roomBox.getSelectedItem().toString().split(" - ")[0]); Room chosen=null;
        for(Room r:engine.getRooms()) if(r.getNumber()==no) chosen=r;
        Booking b=engine.book(guestField.getText(),chosen,LocalDate.now(),LocalDate.now().plusDays(1),
                (Integer)guestSpinner.getValue(),preferenceBox.getSelectedItem().toString());
        if(b==null){JOptionPane.showMessageDialog(this,"Check guest name, room availability and capacity.");return;}
        logArea.append("\nBOOKED: Room "+chosen.getNumber()+" | ₹"+String.format("%.2f",b.getTotal())+
                " | Eco "+chosen.getEcoScore()+"/100");
        updateStats(); refreshRoomChoices();
        JOptionPane.showMessageDialog(this,"Booking confirmed!\nRoom "+chosen.getNumber()+
                "\nTotal: ₹"+String.format("%.2f",b.getTotal())+"\nEco Score: "+chosen.getEcoScore()+"/100");
    }

    private void refreshRoomChoices(){
        if(roomBox==null)return; roomBox.removeAllItems();
        for(Room r:engine.getRooms()) if(r.isAvailable()) roomBox.addItem(r.getNumber()+" - "+r.getType());
    }
    private void updateStats(){
        occupancyLabel.setText(String.format("%.0f%%",engine.occupancyPercent()));
        availableLabel.setText(String.valueOf(engine.getRooms().stream().filter(Room::isAvailable).count()));
        bookingLabel.setText(String.valueOf(engine.getBookings().size()));
    }
    private JLabel valueLabel(String s){ JLabel l=new JLabel(s); l.setFont(new Font("Arial",Font.BOLD,22)); l.setForeground(DARK); return l; }
    private JPanel card(String title,JLabel value,Color accent){
        JPanel p=new JPanel(new BorderLayout()); p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(accent,2),new EmptyBorder(10,12,10,12)));
        JLabel t=new JLabel(title); t.setForeground(accent); t.setFont(new Font("Arial",Font.BOLD,11));
        p.add(t,BorderLayout.NORTH); p.add(value,BorderLayout.CENTER); return p;
    }
    private void styleButton(JButton b,Color bg,Color fg){
        b.setBackground(bg); b.setForeground(fg); b.setFocusPainted(false);
        b.setFont(new Font("Arial",Font.BOLD,11)); b.setBorder(BorderFactory.createEmptyBorder(8,12,8,12));
    }
    public static void main(String[] args){ SwingUtilities.invokeLater(()->new StaySmartApp().setVisible(true)); }
}
