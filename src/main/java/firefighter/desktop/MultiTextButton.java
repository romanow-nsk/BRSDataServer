package firefighter.desktop;

import javax.swing.*;
import java.awt.*;

public class MultiTextButton extends JButton {
    public MultiTextButton(){
        setLayout(new BorderLayout());
        }
    public void setText(String text){
        int idx=text.indexOf(" ");
        if (idx==-1)
            super.setText(text);
        else{
            add(BorderLayout.NORTH,new JLabel(text.substring(0,idx)));
            add(BorderLayout.SOUTH,new JLabel(text.substring(idx+1)));
            }
        }
}
