import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/** Assignment 4 CO4: event-driven GUI designs and runnable Swing/AWT examples. */
public class Assignment4Suite {
    public static void main(String[] args) {
        System.out.println("ASSIGNMENT 4 – CO4 | Event-Driven GUI Design");
        System.out.println("Q1 service validation: " + FeeService.validate("192424272", "1250.00"));
        System.out.println("Q1 invalid amount: " + FeeService.validate("192424272", "-10"));
        System.out.println("Q2 cached departments: " + HospitalData.doctorMap().keySet());
        System.out.println("Q3 layout design: BorderLayout root; FlowLayout header/footer; CENTER results.");
        System.out.println("Q4 paint model: stored Point objects are redrawn during paint().");
        System.out.println("Q5 converter test: 100 INR to USD at 0.012 = " + CurrencyMath.convert(100, "USD"));
        System.out.println("Q6 JDBC: MySQL UI source compiled; runtime requires Connector/J and configured database.");
        System.out.println("Core validation checks completed.");
    }
}

class FeeService {
    static String validate(String reg, String amountText) {
        if (reg == null || !reg.trim().matches("[0-9]{6,12}")) return "Invalid registration number";
        try {
            double amount = Double.parseDouble(amountText.trim());
            if (!Double.isFinite(amount) || amount <= 0) return "Amount must be positive";
            return "VALID: payment of ₹" + String.format(Locale.US, "%.2f", amount);
        } catch (RuntimeException ex) { return "Enter a valid amount"; }
    }
    void recordPayment(String reg, double amount) throws SQLException {
        String url = System.getenv().getOrDefault("DB_URL", "jdbc:mysql://localhost:3306/simats");
        try (Connection c = DriverManager.getConnection(url,
                System.getenv().getOrDefault("DB_USER", "root"), System.getenv().getOrDefault("DB_PASSWORD", ""));
             PreparedStatement ps = c.prepareStatement("INSERT INTO fees(reg_no, amount) VALUES (?, ?)")) {
            ps.setString(1, reg); ps.setDouble(2, amount); ps.executeUpdate();
        }
    }
}

class FeeWindow extends Frame {
    private final TextField reg = new TextField(16), amount = new TextField(12);
    private final Label message = new Label("Enter payment details");
    private final FeeService service = new FeeService();
    FeeWindow() {
        super("Student Fee Payment"); setLayout(new GridLayout(4, 2, 8, 8));
        add(new Label("Registration number")); add(reg); add(new Label("Amount (INR)")); add(amount);
        Button pay = new Button("Pay"), clear = new Button("Clear"), receipt = new Button("Receipt");
        pay.addActionListener(e -> pay()); clear.addActionListener(e -> {reg.setText(""); amount.setText(""); message.setText("Cleared");});
        receipt.addActionListener(e -> message.setText("Receipt is available after successful payment."));
        add(pay); add(clear); add(receipt); add(message);
        addWindowListener(new WindowAdapter(){@Override public void windowClosing(WindowEvent e){dispose();}});
        setSize(420, 190);
    }
    private void pay() {
        String check = FeeService.validate(reg.getText(), amount.getText());
        if (!check.startsWith("VALID:")) { message.setText(check); return; }
        double value = Double.parseDouble(amount.getText().trim());
        message.setText("Processing payment...");
        new SwingWorker<Void,Void>() {
            protected Void doInBackground() throws Exception { service.recordPayment(reg.getText().trim(), value); return null; }
            protected void done() { try { get(); message.setText("Payment successful. Receipt ready."); }
                catch(Exception ex) { message.setText("Payment failed: " + ex.getCause().getMessage()); } }
        }.execute();
    }
}

class Doctor { final String name; Doctor(String n){name=n;} public String toString(){return name;} }
class HospitalData {
    static Map<String,List<Doctor>> doctorMap() {
        Map<String,List<Doctor>> m = new LinkedHashMap<>();
        m.put("Cardiology", Arrays.asList(new Doctor("Dr. Rao"),new Doctor("Dr. Mehta")));
        m.put("Orthopaedics", Arrays.asList(new Doctor("Dr. Kumar"),new Doctor("Dr. Iyer")));
        m.put("Dermatology", Arrays.asList(new Doctor("Dr. Shah")));
        return Collections.unmodifiableMap(m);
    }
}
class AppointmentDesk extends JFrame {
    private final JTextField patient = new JTextField(12);
    private final JComboBox<String> department = new JComboBox<>(new String[]{"Cardiology","Orthopaedics","Dermatology"});
    private final JComboBox<Doctor> doctor = new JComboBox<>();
    private final JComboBox<String> slot = new JComboBox<>(new String[]{"09:00","10:00","11:00","14:00"});
    private final JLabel status = new JLabel("Ready"); private boolean booking = false;
    private final Map<String,List<Doctor>> doctors = HospitalData.doctorMap();
    AppointmentDesk(){super("Hospital Outpatient Appointment Desk"); setLayout(new GridLayout(5,2,8,8));
        add(new JLabel("Patient ID"));add(patient);add(new JLabel("Department"));add(department);
        add(new JLabel("Doctor"));add(doctor);add(new JLabel("Time slot"));add(slot);
        JButton book=new JButton("Book");add(book);add(status);
        department.addActionListener(e->refreshDoctors()); book.addActionListener(e->book()); refreshDoctors();
        addWindowListener(new WindowAdapter(){@Override public void windowClosing(WindowEvent e){if(booking){int a=JOptionPane.showConfirmDialog(AppointmentDesk.this,"Booking in progress. Close?","Confirm",JOptionPane.YES_NO_OPTION);if(a!=JOptionPane.YES_OPTION)return;}dispose();}});
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);pack();setLocationRelativeTo(null);
    }
    private void refreshDoctors(){doctor.removeAllItems();for(Doctor d:doctors.getOrDefault((String)department.getSelectedItem(),Collections.emptyList()))doctor.addItem(d);}
    private void book(){if(patient.getText().trim().isEmpty()||doctor.getSelectedItem()==null){status.setText("Enter patient ID and select a doctor");return;}booking=true;status.setText("Appointment confirmed for "+patient.getText().trim());booking=false;}
}

class LibrarySearchScreen extends JFrame {
    private final JTextField query=new JTextField(24); private final JComboBox<String> category=new JComboBox<>(new String[]{"All","Books","Journals","Theses"});
    private final DefaultListModel<String> model=new DefaultListModel<>(); private final JList<String> results=new JList<>(model); private final JLabel status=new JLabel("Ready");
    LibrarySearchScreen(){super("Campus Library Search");setLayout(new BorderLayout(8,8));
        JPanel top=new JPanel(new FlowLayout(FlowLayout.LEFT));top.add(new JLabel("Search"));top.add(query);top.add(category);JButton search=new JButton("Search");top.add(search);
        add(top,BorderLayout.NORTH);add(new JScrollPane(results),BorderLayout.CENTER);
        JPanel bottom=new JPanel(new BorderLayout()); JPanel actions=new JPanel(new FlowLayout(FlowLayout.CENTER));for(String s:new String[]{"Prev","Next","Reserve"})actions.add(new JButton(s));bottom.add(actions,BorderLayout.CENTER);bottom.add(status,BorderLayout.SOUTH);add(bottom,BorderLayout.SOUTH);
        search.addActionListener(e->{model.clear();if(!query.getText().trim().isEmpty()){model.addElement("Java Programming – Library Catalogue");model.addElement("Event-Driven Systems – Reference Shelf");}status.setText(model.size()+" result(s)");});
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);setSize(720,480);setLocationRelativeTo(null);
    }
}

class DrawPad extends Frame {
    private final List<Point> points=new ArrayList<>(); private final Label status=new Label("Clicks: 0");
    DrawPad(){super("Draw Pad");setLayout(new BorderLayout());add(status,BorderLayout.SOUTH);
        addMouseListener(new MouseAdapter(){@Override public void mouseClicked(MouseEvent e){points.add(e.getPoint());status.setText("Clicks: "+points.size());repaint();}});
        addWindowListener(new WindowAdapter(){@Override public void windowClosing(WindowEvent e){dispose();}});setSize(400,300);}
    @Override public void paint(Graphics g){super.paint(g);for(Point p:points)g.fillOval(p.x-3,p.y-3,6,6);}
    static BufferedImage renderToImage(int width,int height,List<Point> pts){BufferedImage image=new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);Graphics g=image.getGraphics();for(Point p:pts)g.fillOval(p.x-3,p.y-3,6,6);g.dispose();return image;}
}

class CurrencyMath {
    static double rate(String currency){switch(currency){case "USD":return 0.012;case "EUR":return 0.011;case "GBP":return 0.0095;default:return 1.0;}}
    static double convert(double inr,String target){if(!Double.isFinite(inr)||inr<0)throw new IllegalArgumentException("Amount must be non-negative");return inr*rate(target);}
}
class Converter extends JFrame {
    private final JTextField amount=new JTextField(10); private final JComboBox<String> target=new JComboBox<>(new String[]{"USD","EUR","GBP"}); private final JLabel result=new JLabel("Result appears here");
    Converter(){super("Currency Converter");setLayout(new FlowLayout());JButton convert=new JButton("Convert");add(new JLabel("INR"));add(amount);add(target);add(convert);add(result);
        ActionListener action=e->convert();convert.addActionListener(action);amount.addActionListener(action);
        addWindowListener(new WindowAdapter(){@Override public void windowClosing(WindowEvent e){dispose();}});setDefaultCloseOperation(DISPOSE_ON_CLOSE);pack();}
    private void convert(){try{double n=Double.parseDouble(amount.getText().trim());double out=CurrencyMath.convert(n,(String)target.getSelectedItem());String msg=String.format(Locale.US,"%.2f %s",out,target.getSelectedItem());result.setText(msg);JOptionPane.showMessageDialog(this,msg,"Conversion",JOptionPane.INFORMATION_MESSAGE);}catch(RuntimeException ex){JOptionPane.showMessageDialog(this,"Enter a valid amount","Invalid amount",JOptionPane.ERROR_MESSAGE);}}
}

class EventRegistrationApp extends JFrame {
    private final JTextField studentId=new JTextField(10),studentName=new JTextField(12);
    private final JComboBox<String> event=new JComboBox<>(new String[]{"Music Night","Tech Fest","Dance Competition"});
    private final DefaultTableModel model=new DefaultTableModel(new String[]{"Student ID","Name","Event"},0);
    private final JTable table=new JTable(model); private final JLabel status=new JLabel("Ready");
    private final JComboBox<String> eventFilter=new JComboBox<>(new String[]{"All","Music Night","Tech Fest","Dance Competition"});
    private int pageOffset=0;
    private final String url=System.getenv().getOrDefault("DB_URL","jdbc:mysql://localhost:3306/simats");
    EventRegistrationApp(){super("Campus Event Registration");setLayout(new BorderLayout(8,8));
        JPanel north=new JPanel(new BorderLayout()); JPanel form=new JPanel(new FlowLayout(FlowLayout.LEFT));form.add(new JLabel("Student ID"));form.add(studentId);form.add(new JLabel("Name"));form.add(studentName);form.add(event);JButton register=new JButton("Register");form.add(register);north.add(form,BorderLayout.NORTH); JPanel filters=new JPanel(new FlowLayout(FlowLayout.LEFT));filters.add(new JLabel("Filter event"));filters.add(eventFilter);JButton applyFilter=new JButton("Apply Filter"),previous=new JButton("Previous"),next=new JButton("Next");filters.add(applyFilter);filters.add(previous);filters.add(next);north.add(filters,BorderLayout.SOUTH);add(north,BorderLayout.NORTH);
        add(new JScrollPane(table),BorderLayout.CENTER);add(status,BorderLayout.SOUTH);
        JMenuBar bar=new JMenuBar();JMenu file=new JMenu("File");JMenuItem refresh=new JMenuItem("Refresh"),export=new JMenuItem("Export CSV"),exit=new JMenuItem("Exit");file.add(refresh);file.add(export);file.addSeparator();file.add(exit);bar.add(file);JMenu help=new JMenu("Help");help.add(new JMenuItem("About"));bar.add(help);setJMenuBar(bar);
        register.addActionListener(e->register());refresh.addActionListener(e->{pageOffset=0;loadPage(pageOffset);});applyFilter.addActionListener(e->{pageOffset=0;loadPage(pageOffset);});previous.addActionListener(e->{pageOffset=Math.max(0,pageOffset-100);loadPage(pageOffset);});next.addActionListener(e->{pageOffset+=100;loadPage(pageOffset);});export.addActionListener(e->exportCsv());exit.addActionListener(e->dispose());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);setSize(800,500);setLocationRelativeTo(null);loadPage(0);
    }
    private Connection connect() throws SQLException{return DriverManager.getConnection(url,System.getenv().getOrDefault("DB_USER","root"),System.getenv().getOrDefault("DB_PASSWORD",""));}
    private void register(){String id=studentId.getText().trim(),name=studentName.getText().trim(),ev=(String)event.getSelectedItem();if(!id.matches("[0-9]{6,12}")||name.isEmpty()){status.setText("Invalid input: check student ID and name");return;}
        status.setText("Saving registration...");new SwingWorker<Void,Void>(){protected Void doInBackground()throws Exception{try(Connection c=connect();PreparedStatement p=c.prepareStatement("INSERT INTO registrations(student_id,student_name,event_name) VALUES(?,?,?)")){p.setString(1,id);p.setString(2,name);p.setString(3,ev);p.executeUpdate();}return null;}
            protected void done(){try{get();status.setText("Registration successful");loadPage(0);}catch(Exception ex){Throwable t=ex.getCause();status.setText(t!=null&&t.getMessage()!=null&&t.getMessage().contains("Duplicate")?"Duplicate registration rejected":"Database error: "+(t==null?ex.getMessage():t.getMessage()));}}}.execute();}
    private void loadPage(int offset){String selected=(String)eventFilter.getSelectedItem();new SwingWorker<List<Object[]>,Void>(){protected List<Object[]> doInBackground()throws Exception{List<Object[]> rows=new ArrayList<>();String sql="SELECT student_id,student_name,event_name FROM registrations "+("All".equals(selected)?"":"WHERE event_name=? ")+"ORDER BY id LIMIT 100 OFFSET ?";try(Connection c=connect();PreparedStatement p=c.prepareStatement(sql)){int index=1;if(!"All".equals(selected))p.setString(index++,selected);p.setInt(index,offset);try(ResultSet r=p.executeQuery()){while(r.next())rows.add(new Object[]{r.getString(1),r.getString(2),r.getString(3)});}}return rows;}
        protected void done(){try{model.setRowCount(0);for(Object[] row:get())model.addRow(row);status.setText("Loaded page (up to 100 records)");}catch(Exception ex){status.setText("Load failed: configure MySQL and Connector/J");}}}.execute();}
    private void exportCsv(){try{JFileChooser chooser=new JFileChooser();if(chooser.showSaveDialog(this)!=JFileChooser.APPROVE_OPTION)return;try(PrintWriter out=new PrintWriter(chooser.getSelectedFile())){for(int c=0;c<model.getColumnCount();c++){if(c>0)out.print(',');out.print(model.getColumnName(c));}out.println();for(int r=0;r<model.getRowCount();r++){for(int c=0;c<model.getColumnCount();c++){if(c>0)out.print(',');out.print('"'+String.valueOf(model.getValueAt(r,c)).replace("\"","\"\"")+'"');}out.println();}}status.setText("CSV exported");}catch(IOException ex){status.setText("Export failed: "+ex.getMessage());}}
}

class GuiLauncher {
    public static void main(String[] args) {
        String target = args.length == 0 ? "Converter" : args[0];
        SwingUtilities.invokeLater(() -> {
            switch (target) {
                case "FeeWindow": new FeeWindow().setVisible(true); break;
                case "AppointmentDesk": new AppointmentDesk().setVisible(true); break;
                case "LibrarySearchScreen": new LibrarySearchScreen().setVisible(true); break;
                case "DrawPad": new DrawPad().setVisible(true); break;
                case "Converter": new Converter().setVisible(true); break;
                case "EventRegistrationApp": new EventRegistrationApp().setVisible(true); break;
                default: throw new IllegalArgumentException("Unknown GUI: " + target);
            }
        });
    }
}
