import java.awt.*;import javax.swing.*;
public class ConverterLambda extends JFrame{
 private final JTextField amount=new JTextField(10);private final JComboBox<String> target=new JComboBox<>(new String[]{"USD","EUR"});private final JLabel result=new JLabel(" ");
 private static double rate(String c){return c.equals("USD")?0.012:0.011;}
 public ConverterLambda(){super("Currency Converter");setLayout(new FlowLayout());JButton b=new JButton("Convert");add(new JLabel("INR"));add(amount);add(target);add(b);add(result);
 // TODO 1 completed: one lambda listener registered for both triggers
 b.addActionListener(e->convert());amount.addActionListener(e->convert());
 setDefaultCloseOperation(EXIT_ON_CLOSE);pack();}
 private void convert(){
 // TODO 2 completed
 try{double v=Double.parseDouble(amount.getText().trim());if(v<0)throw new NumberFormatException();String s=String.format("%.2f",v*rate((String)target.getSelectedItem()));result.setText(s);JOptionPane.showMessageDialog(this,s+" "+target.getSelectedItem());}
 catch(NumberFormatException e){result.setText("Invalid amount");JOptionPane.showMessageDialog(this,"Invalid amount","Input Error",JOptionPane.ERROR_MESSAGE);}
 }
 public static void main(String[]x){SwingUtilities.invokeLater(()->new ConverterLambda().setVisible(true));}}
