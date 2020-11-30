package firefighter.desktop;

import firefighter.core.constants.Values;
import firefighter.core.entity.EntityNamed;
import firefighter.core.entity.Entity;
import firefighter.core.entity.EntityList;
import retrofit2.Call;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PopupList extends JTextField {
    public  PopupList(final MainBaseFrame main, final Component parent, final int x0, final int y0, int dx, int dy, final EntityList<?> data, final I_ListSelectedFull back){
        setBounds(x0,y0,dx,dy);
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                if (evt.getKeyChar()==27)
                    back.onCancel();
                String ss = getText().toLowerCase()+evt.getKeyChar();
                if (ss.length()<2)
                    return;
                JPopupMenu menu = new JPopupMenu();
                int i=0;
                int ii=0;
                for (Entity ent : data){
                    final int jj=i;
                    if (ent.getTitle().toLowerCase().indexOf(ss)!=-1){
                        JMenuItem item = new JMenuItem();
                        item.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                back.onSelect(jj,"");
                                setText("");
                            }
                        });
                        item.setText(ent.getTitle());
                        menu.add(item);
                        ii++;
                        }
                    i++;
                    }
                if (ii==0 || ii> Values.PopupListMaxSize)
                    return;
                menu.show(parent,x0, y0+50);
                   }
        });
        }
    public  PopupList(final MainBaseFrame main, final Component parent, final int x0, final int y0, int dx, int dy, final String entityName, final I_ListSelectedFull back){
        setBounds(x0,y0,dx,dy);
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                if (evt.getKeyChar()==27)
                    back.onCancel();
                String ss = getText().toLowerCase()+evt.getKeyChar();
                if (ss.length()<2)
                    return;
                JPopupMenu menu = new JPopupMenu();
                new APICall<EntityList<EntityNamed>>((Client)main){
                    @Override
                    public Call<EntityList<EntityNamed>> apiFun() {
                        return main.service.getNamesByPattern(main.debugToken,entityName,ss);
                        }
                    @Override
                    public void onSucess(final EntityList<EntityNamed> oo){
                        if (oo.size()==0)
                            return;
                        for(int i=0;i<oo.size();i++){
                            EntityNamed ent = oo.get(i);
                            final int ii=i;
                            JMenuItem item = new JMenuItem();
                            item.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {        // Возвращает OID
                                    back.onSelect(oo.get(ii).getOid(),oo.get(ii).getName());
                                    setText("");
                                    }
                                });
                            item.setText(ent.getTitle());
                            menu.add(item);
                            }
                        menu.show(parent,x0, y0+50);
                        }
                    };
            }
        });
    }
}
