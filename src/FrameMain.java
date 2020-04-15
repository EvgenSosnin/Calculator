

import javax.swing.*;
import java.awt.event.*;


public class FrameMain extends JFrame {

    private float EUR = HTMLParser.getEUR_value();
    private String TOTAL = "Promdesign   |   1 EUR = " + EUR + " UAH ";

    //JPanels
    private PanelHead panelHead = new PanelHead();
    private PanelBottom panelBottom = new PanelBottom();
    private PanelBody panelBody = new PanelBody(EUR);


    public FrameMain() {

        //frame settings
        setTitle(TOTAL);
        setUndecorated(true);
        getRootPane().setWindowDecorationStyle(JRootPane.PLAIN_DIALOG);
        setSize(426, 900);
        setLayout(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowClose());

        //add Components
        add(panelHead);
        panelHead.setBounds(0,0,417,178);

        add(panelBottom);
        panelBottom.setBounds(0,830,417,37);

        add(panelBody);
        panelBody.setBounds(0,180,417,800);


        //final settings
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);

        //Start animation
        panelHead.startAnimation();
    }

    private class WindowClose extends WindowAdapter {
        @Override
        public void windowClosing(java.awt.event.WindowEvent windowEvent) {
            PriceType.saveAllPrices(panelBody.getArrPricesTypes());
            panelHead.offAnimation();
            int answer = JOptionPane.showConfirmDialog(null,"Are u sure?","",JOptionPane.YES_NO_OPTION);
            if (answer == JOptionPane.YES_OPTION){
                System.exit(0);
            }else{
                panelHead.startAnimation();
            }

        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new FrameMain();
            }
        });
    }
}
