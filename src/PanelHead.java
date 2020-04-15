import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class PanelHead extends JPanel {
    private volatile boolean isAnimationRun = false;
    private JLabel labCalculator;
    private JLabel labBackground;
    private JLabel labLogo;
    private Circle circle;

    public PanelHead() {
        setLayout(null);
        labBackground = getImg("background.png");
        circle = new Circle();
        labLogo = getImg("PDlogo.png");
        labCalculator = getImg("headLabel.png");

        labLogo.setBounds(280,60,60,59);
        labCalculator.setBounds(40,70,185,35);
        labCalculator.setVisible(false);

        add(labLogo);
        add(circle);
        add(labCalculator);
        add(labBackground);

        circle.paintCircle(-112,90,112,112);
        circle.setBounds(0,0,417,178);
        labBackground.setBounds(0,0,417,178);
        this.setSize(417,178);
        this.setVisible(true);


    }

    private JLabel getImg(String path){
        JLabel lab = new JLabel();
        try{
            URL url = getClass().getResource(path);
            lab.setIcon(new ImageIcon(url));
        }catch(Exception e){
            e.printStackTrace();
        }
        return lab;
    }
    public void startAnimation(){
        Thread thread = new Thread(() -> {
            while(isAnimationRun){ }
            isAnimationRun = true;

            int move = -100,grow = 1;
            while(move < 250){
                circle.paintCircle(move,30,grow/3,grow/3);
                try{
                    Thread.sleep(5);
                }catch(InterruptedException e){
                }
                move++;
                grow++;
            }
            labCalculator.setVisible(true);
            labLogo.setVisible(true);
            isAnimationRun = false;
        });
        thread.start();
    }
    public void offAnimation(){
        Thread thread = new Thread(() -> {
            while(isAnimationRun){ }
            isAnimationRun = true;

            labCalculator.setVisible(false);
            labLogo.setVisible(false);

            int increment = circle.w;
            int x = circle.x;
            int y = circle.y;
            while (increment > 1) {
                increment -= 1;
                if(increment%2 == 0){
                    x +=1;
                    y +=1;
                }
                circle.paintCircle(x, y, increment, increment);
                try{
                    Thread.sleep(15);
                }catch (InterruptedException exp){
                    exp.printStackTrace();
                }
            }
            isAnimationRun = false;
        });
        thread.start();
    }
    private static class Circle extends JComponent{
        private Color color = Color.white;
        private int h = 0,w = 0,x = 113,y = 109;
        public void paintCircle(int x, int y , int w, int h){
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
            this.repaint();
        }
        @Override
        protected void paintComponent(Graphics g) {
            g.setColor(color);
            g.fillOval(x,y,w,h);
        }
    }
}
