import java.awt.*; import java.awt.event.*; import java.util.*;
public class DrawPad extends Frame {
 private final java.util.List<Point> points=new ArrayList<>();
 private final Label status=new Label("Clicks: 0");
 public DrawPad(){
  super("Draw Pad");add(status,BorderLayout.SOUTH);
  addMouseListener(new MouseAdapter(){
   // TODO 1 completed
   @Override public void mouseClicked(MouseEvent e){points.add(e.getPoint());status.setText("Clicks: "+points.size());repaint();}
  });
  addWindowListener(new WindowAdapter(){@Override public void windowClosing(WindowEvent e){dispose();}});
  setSize(400,300);
 }
 @Override public void paint(Graphics g){
  super.paint(g);
  // TODO 2 completed
  for(Point p:points) g.fillOval(p.x-3,p.y-3,6,6);
 }
 public static void main(String[] a){new DrawPad().setVisible(true);}
}
