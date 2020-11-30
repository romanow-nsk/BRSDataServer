package firefighter.desktop.view;

import javax.swing.*;
import java.awt.*;

public class MESContext {
    public final TextArea MES;
    public final JFrame logFrame;
    public final JTextField MESShort;
    public MESContext(TextArea mes0, JFrame frame, JTextField ff) {
        MES = mes0;
        logFrame = frame;
        MESShort = ff;
    }
}
