import java.awt.*;import java.sql.*;import java.util.Vector;import javax.swing.*;import javax.swing.table.DefaultTableModel;
public class EventRegistrationApp extends JFrame{
 private final JTextField sid=new JTextField(9),name=new JTextField(12);
 private final JComboBox<String> event=new JComboBox<>(new String[]{"Cultural Fest","Music Night","Tech Expo"});
 private final JLabel status=new JLabel("Ready");
 private final DefaultTableModel model=new DefaultTableModel(new Object[]{"ID","Student ID","Name","Event"},0);
 private final JTable table=new JTable(model);private final RegistrationService service=new RegistrationService();
 public EventRegistrationApp(){super("Campus Event Registration");setLayout(new BorderLayout(6,6));
 JPanel form=new JPanel(new FlowLayout());form.add(new JLabel("Student ID"));form.add(sid);form.add(new JLabel("Name"));form.add(name);form.add(event);
 JButton register=new JButton("Register"),refresh=new JButton("Refresh");form.add(register);form.add(refresh);add(form,BorderLayout.NORTH);add(new JScrollPane(table),BorderLayout.CENTER);add(status,BorderLayout.SOUTH);
 JMenuBar mb=new JMenuBar();JMenu file=new JMenu("File");JMenuItem export=new JMenuItem("Export CSV"),exit=new JMenuItem("Exit");file.add(export);file.add(exit);mb.add(file);setJMenuBar(mb);
 register.addActionListener(e->registerAsync());refresh.addActionListener(e->loadAsync());exit.addActionListener(e->dispose());export.addActionListener(e->exportCsv());
 setDefaultCloseOperation(EXIT_ON_CLOSE);setSize(820,480);setLocationRelativeTo(null);loadAsync();}
 private void registerAsync(){String s=sid.getText().trim(),n=name.getText().trim(),ev=(String)event.getSelectedItem();if(s.isEmpty()||n.isEmpty()){status.setText("Student ID and name are required.");return;}
 status.setText("Registering...");new SwingWorker<Void,Void>(){protected Void doInBackground()throws Exception{service.register(s,n,ev);return null;}
 protected void done(){try{get();status.setText("Registration saved.");loadAsync();}catch(Exception x){status.setText("Registration failed: "+root(x));}}}.execute();}
 private void loadAsync(){status.setText("Loading...");new SwingWorker<Vector<Object[]>,Void>(){protected Vector<Object[]> doInBackground()throws Exception{return service.firstPage(100);}
 protected void done(){try{model.setRowCount(0);for(Object[] r:get())model.addRow(r);status.setText("Loaded first 100 registrations.");}catch(Exception x){status.setText("Load failed: "+root(x));}}}.execute();}
 private String root(Exception x){Throwable t=x;while(t.getCause()!=null)t=t.getCause();return t.getMessage();}
 private void exportCsv(){JFileChooser ch=new JFileChooser();if(ch.showSaveDialog(this)!=JFileChooser.APPROVE_OPTION)return;try(java.io.PrintWriter p=new java.io.PrintWriter(ch.getSelectedFile())){p.println("id,student_id,name,event");for(int i=0;i<model.getRowCount();i++)p.println(model.getValueAt(i,0)+","+model.getValueAt(i,1)+","+model.getValueAt(i,2)+","+model.getValueAt(i,3));status.setText("CSV exported.");}catch(Exception e){status.setText("Export failed: "+e.getMessage());}}
 public static void main(String[]a){SwingUtilities.invokeLater(()->new EventRegistrationApp().setVisible(true));}}
class RegistrationService{private final RegistrationDao dao=new RegistrationDao();void register(String s,String n,String e)throws SQLException{if(s.trim().isEmpty()||n.trim().isEmpty())throw new IllegalArgumentException("Required fields missing.");dao.insert(s,n,e);}Vector<Object[]> firstPage(int limit)throws SQLException{return dao.fetch(limit,0);}}
class RegistrationDao{
 private Connection connect()throws SQLException{String u=System.getenv("EVENT_DB_URL"),n=System.getenv("EVENT_DB_USER"),p=System.getenv("EVENT_DB_PASSWORD");if(u==null||n==null||p==null)throw new SQLException("Set EVENT_DB_URL, EVENT_DB_USER and EVENT_DB_PASSWORD.");return DriverManager.getConnection(u,n,p);}
 void insert(String s,String n,String e)throws SQLException{try(Connection c=connect();PreparedStatement p=c.prepareStatement("INSERT INTO registrations(student_id,student_name,event_name) VALUES(?,?,?)")){p.setString(1,s);p.setString(2,n);p.setString(3,e);p.executeUpdate();}}
 Vector<Object[]> fetch(int limit,int offset)throws SQLException{Vector<Object[]> rows=new Vector<>();try(Connection c=connect();PreparedStatement p=c.prepareStatement("SELECT id,student_id,student_name,event_name FROM registrations ORDER BY id DESC LIMIT ? OFFSET ?")){p.setInt(1,limit);p.setInt(2,offset);try(ResultSet r=p.executeQuery()){while(r.next())rows.add(new Object[]{r.getLong(1),r.getString(2),r.getString(3),r.getString(4)});}}return rows;}
}
