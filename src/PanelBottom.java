import javax.swing.*;
import java.net.URL;

public class PanelBottom extends JPanel {
    public PanelBottom(){
            setSize(417,35);
            JLabel lab = new JLabel();
            try{
                URL url = getClass().getResource("bottombubble.png");
                lab.setIcon(new ImageIcon(url));
            }catch(Exception e){
                e.printStackTrace();
            }
            add(lab);
    }
}
