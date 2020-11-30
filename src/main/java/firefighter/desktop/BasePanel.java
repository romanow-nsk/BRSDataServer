/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package firefighter.desktop;

import firefighter.core.entity.Entity;
import firefighter.core.entity.EntityList;
import retrofit2.Response;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 *
 * @author romanow
 */
public abstract class BasePanel extends JPanel implements I_PanelEvent{
    public final static int EventPLMOn=1;             //
    public final static int EventPLMOff=2;             //
    public final static int EventRefreshSettings=4;      // Обновить настройки
    public final static int EventRefreshSettingsDone=5;  // Настройки обновлены
    public final static int PanelOffsetY=60;
    public final static int PanelW=800;
    public final static int PanelH=700;
    protected Client main;
    protected boolean editMode=true;
    public boolean isMainMode(){ return true; }
    public boolean isESSMode(){ return true; }
    /**
     * Creates new form BasePanel
     */
    public BasePanel() {
        initComponents();
    }
    public void initPanel(Client main0){
        main = main0;
        setBounds(0,0,PanelW,PanelH);
        }
    public void setChoice(Choice cc, EntityList<Entity> ss){
        cc.removeAll();
        for (Entity ee : ss)
            cc.add(ee.getTitle());
        }
    public String httpError(Response res) throws IOException {
        return  res.message()+" ("+res.code()+") "+res.errorBody().string();
        }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
