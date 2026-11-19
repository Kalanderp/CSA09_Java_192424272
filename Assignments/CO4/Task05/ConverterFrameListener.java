import java.awt.*;import java.awt.event.*;import javax.swing.*;
public class ConverterFrameListener extends JFrame implements ActionListener{
 JTextField amount=new JTextField(10);JComboBox<String> target=new JComboBox<>(new String[]{"USD","EUR"});JLabel result=new JLabel(" ");
 public ConverterFrameListener(){super("Converter – Frame Listener");setLayout(new FlowLayout());JButton b=new JButton("Convert");add(new JLabel("INR"));add(amount);add(target);add(b);add(result);b.addActionListener(this);amount.addActionListener(this);addWindowListener(new WindowAdapter(){public void windowClosing(WindowEvent e){dispose();}});setDefaultCloseOperation(DISPOSE_ON_CLOSE);pack();}
 public void actionPerformed(ActionEvent e){convert();}
 private void convert(){try{double v=Double.parseDouble(amount.getText().trim());if(v<0)throw new NumberFormatException();double r="USD".equals(target.getSelectedItem())?0.012:0.011;String s=String.format("%.2f",v*r);result.setText(s);JOptionPane.showMessageDialog(this,s);}catch(NumberFormatException x){result.setText("Invalid amount");JOptionPane.showMessageDialog(this,"Invalid amount");}}
 public static void main(String[]x){SwingUtilities.invokeLater(()->new ConverterFrameListener().setVisible(true));}}
