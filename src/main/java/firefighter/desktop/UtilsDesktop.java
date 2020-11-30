package firefighter.desktop;

import javax.swing.*;

public class UtilsDesktop {
    public static void setLabelText(JLabel label, String name, int size){
        String out="<html>";
        while(true) {
            int idx = name.lastIndexOf(" ",size);
            int idx2 = name.indexOf("$");
            if (idx2!=-1 && idx2 < idx) idx=idx2;
            if (idx == -1){
                out += "&nbsp; &nbsp;" + name + "</html>";
                break;
                }
            out+=  "&nbsp; &nbsp;" + name.substring(0, idx)+"<br>";
            name = name.substring(idx + 1);
            }
        label.setText(out);
        }
    public static void setButtonText(JButton label, String name, int size){
        if (name.length()<=size) {
            label.setText("<html>&nbsp; &nbsp;"+name+"</html>");
            return;
            }
        int idx = name.substring(0,size).lastIndexOf(" ");
        String name2;
        if (idx==-1)
            name2 = "<html>&nbsp; &nbsp;"+name+"</html>";
        else
            name2 = "<html> &nbsp; &nbsp;"+name.substring(0,idx)+"<br> &nbsp; &nbsp;"+name.substring(idx+1)+"</html>";
        label.setText(name2);
        }
}
