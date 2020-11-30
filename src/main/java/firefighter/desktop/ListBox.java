package firefighter.desktop;

import firefighter.core.constants.ConstValue;
import firefighter.core.entity.Entity;
import firefighter.core.entity.EntityList;

import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

public class ListBox extends Choice {
    I_ListSelected ok;
    public ListBox(Rectangle rr,ArrayList xx, I_ListSelected back0){
        this(rr,back0);
        removeAll();
        for(Object zz : xx)
            add(((Entity)zz).getTitle());
        }
    public ListBox(boolean ff, Rectangle rr, ArrayList xx, I_ListSelected back0){
        this(rr,back0);
        removeAll();
        for(Object zz : xx)
            add(((ConstValue)zz).title());
            }
    public ListBox(Rectangle rr, I_ListSelected back0){
        setBounds(rr);
        ok = back0;
        addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                ok.onSelect(getSelectedIndex());
                }
            });
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getButton()==3)
                    return;
                ok.onSelect(getSelectedIndex());
            }
        });
        }

}
