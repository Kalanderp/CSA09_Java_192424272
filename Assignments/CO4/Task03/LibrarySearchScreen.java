import java.awt.*; import javax.swing.*;
public class LibrarySearchScreen extends JFrame {
 private final JTextField query=new JTextField(22);
 private final JComboBox<String> category=new JComboBox<>(new String[]{"All","Title","Author","Subject"});
 private final DefaultListModel<String> model=new DefaultListModel<>();
 private final JList<String> results=new JList<>(model);
 private final JLabel status=new JLabel("Ready");
 public LibrarySearchScreen(){
  super("Campus Library Search"); setLayout(new BorderLayout(8,8));
  JLabel title=new JLabel("Campus Library",SwingConstants.CENTER); title.setFont(title.getFont().deriveFont(Font.BOLD,20f));
  JPanel north=new JPanel(new BorderLayout()); north.add(title,BorderLayout.NORTH);
  JPanel search=new JPanel(new FlowLayout(FlowLayout.LEFT)); search.add(new JLabel("Search:"));search.add(query);search.add(category);
  JButton find=new JButton("Search");search.add(find);north.add(search,BorderLayout.SOUTH);add(north,BorderLayout.NORTH);
  model.addElement("Java Programming – Sample result");model.addElement("Data Structures – Sample result");
  add(new JScrollPane(results),BorderLayout.CENTER);
  JPanel buttons=new JPanel(new FlowLayout()); JButton prev=new JButton("Prev"),next=new JButton("Next"),reserve=new JButton("Reserve");
  buttons.add(prev);buttons.add(next);buttons.add(reserve);
  JPanel south=new JPanel(new BorderLayout());south.add(buttons,BorderLayout.CENTER);south.add(status,BorderLayout.SOUTH);add(south,BorderLayout.SOUTH);
  find.addActionListener(e->status.setText(query.getText().trim().isEmpty()?"Enter a search term.":"Searching "+category.getSelectedItem()+" for "+query.getText().trim()));
  prev.addActionListener(e->status.setText("Previous page selected."));next.addActionListener(e->status.setText("Next page selected."));
  reserve.addActionListener(e->status.setText(results.getSelectedValue()==null?"Select a result first.":"Reservation request: "+results.getSelectedValue()));
  setDefaultCloseOperation(EXIT_ON_CLOSE);setMinimumSize(new Dimension(600,400));setSize(760,520);setLocationRelativeTo(null);
 }
 public static void main(String[] a){SwingUtilities.invokeLater(()->new LibrarySearchScreen().setVisible(true));}
}
