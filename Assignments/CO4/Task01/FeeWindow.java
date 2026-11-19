import java.awt.*; import java.awt.event.*; import javax.swing.*;
public class FeeWindow extends JFrame {
 private final JTextField regNo=new JTextField(14), amount=new JTextField(10);
 private final JLabel status=new JLabel("Enter payment details.");
 private final FeeService service=new FeeService();
 public FeeWindow(){
  super("Student Fee Payment"); setLayout(new BorderLayout(8,8));
  JPanel form=new JPanel(new FlowLayout(FlowLayout.LEFT));
  form.add(new JLabel("Register No:")); form.add(regNo); form.add(new JLabel("Amount:")); form.add(amount);
  JButton pay=new JButton("Pay"), clear=new JButton("Clear"), receipt=new JButton("Receipt");
  JPanel actions=new JPanel(); actions.add(pay); actions.add(clear); actions.add(receipt);
  add(form,BorderLayout.NORTH); add(actions,BorderLayout.CENTER); add(status,BorderLayout.SOUTH);
  pay.addActionListener(e->pay()); clear.addActionListener(e->{regNo.setText("");amount.setText("");status.setText("Fields cleared.");});
  receipt.addActionListener(e->JOptionPane.showMessageDialog(this,status.getText(),"Receipt",JOptionPane.INFORMATION_MESSAGE));
  setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
  addWindowListener(new WindowAdapter(){@Override public void windowClosing(WindowEvent e){
   if(JOptionPane.showConfirmDialog(FeeWindow.this,"Close payment window?","Confirm",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION) dispose();
  }});
  pack(); setLocationRelativeTo(null);
 }
 private void pay(){try{status.setText(service.payFee(regNo.getText(),amount.getText()));}
  catch(Exception ex){status.setText("Payment failed: "+ex.getMessage());}}
 public static void main(String[] a){SwingUtilities.invokeLater(()->new FeeWindow().setVisible(true));}
}
