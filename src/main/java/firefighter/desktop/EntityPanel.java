/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package firefighter.desktop;

import firefighter.core.Utils;
import firefighter.core.constants.Values;
import firefighter.core.constants.ValuesBase;
import firefighter.core.entity.Entity;
import firefighter.core.entity.EntityList;
import firefighter.core.entity.I_Compare;
import firefighter.core.entity.baseentityes.JBoolean;
import firefighter.core.entity.baseentityes.JLong;
import retrofit2.Response;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;


/**
 *
 * @author romanow
 */
public abstract class EntityPanel extends EntityBasePanel {
    EntityList<Entity> data;
    boolean fullMode;
    boolean newRecord=false;
    private boolean sorted=true;
    MainBaseFrame main;
    PopupList popup;
    boolean isPopup=false;
    private boolean needUpdate=false;
    public void setUpdateState(boolean ss){
        needUpdate = ss;
        Update.setVisible(ss);
        Add.setVisible(!ss);
        Delete.setVisible(!ss);
        }

    @Override
    public EntityList<Entity> getData() {
        return data; }
    public Choice listBox(){ return listBox; }
    public JButton Add(){ return Add; }
    public EntityPanel(int x0, int y0, EntityList data0, String name0, MainBaseFrame base0, boolean fullMode0) {
        this(x0,y0,data0,name0,base0,fullMode0,true);
        }
    public EntityPanel(int x0, int y0, EntityList data0, String name0, MainBaseFrame base0, boolean fullMode0,boolean sorted0) {
        initComponents();
        sorted = sorted0;
        fullMode = fullMode0;
        Delete.setVisible(fullMode);
        Update.setVisible(fullMode);
        data = data0 !=null ? data0 : new EntityList();
        entityName = name0;
        entityTitleName = Values.EntityFactory.getEntityNameBySimpleClass(entityName);
        main = base0;
        setBounds(x0,y0,600,35);
        Title.setText(entityTitleName);
        setUpdateState(false);
        setVisible(true);
        popup = new PopupList(main, this, 200, 0, 40, 25, data, new I_ListSelectedFull() {
            @Override
            public void onSelect(long idx, String name) {
                listBox.select((int)idx);
                getById();
                popup.setVisible(false);
                listBox.setBounds(200,0,280,25);
                }
            @Override
            public void onCancel() {
                popup.setVisible(false);
                listBox.setBounds(200,0,280,25);
                }
            });
        popup.setVisible(false);
        add(popup);
        setVisible(true);
    }
    public EntityPanel() {
        initComponents();
    }
    public boolean isRecordSelected(Entity ent){ return true; }
    public EntityList<Entity> getLazy(){
        return new EntityList<>();
        }
    //-------------------------------- Читать всех ----------------------------------------
    public void getAllEvent(){
        current=null;
        listBox.removeAll();
        data.clear();
        Response res7=null;
        EntityList<Entity> zz;
        try {
            long tt = System.currentTimeMillis();
            res7 = apiFunGetAll();
            //System.out.println("time="+(System.currentTimeMillis()-tt)+" мс");
        } catch (IOException ex) {
                System.out.println(ex.getMessage());
                return; }
            if (res7==null)
                zz = getLazy();
            else{
                if (!res7.isSuccessful()) {
                    String ss = Utils.httpError(res7);
                    System.out.println("Ошибка запроса  " + ss);
                    return;
                    }
                zz  = (EntityList)res7.body();
                }
        for (Entity ee : zz)
            if (isRecordSelected(ee))
                data.add(ee);
        if (sorted)
            data.sort(new I_Compare() {
                @Override
                public int compare(Entity one, Entity two) {
                    return 0;
                }
            },false);
        for(Entity xx : data){
            listBox.add(xx.getTitle());
            }
        setUpdateState(false);
        getById();
        }
    //-------------------------------- Читать по id ----------------------------------------
    public void getById(){
        if (listBox.getItemCount()==0) return;
        Response res7 = null;
        try {
            long tt = System.currentTimeMillis();
            res7 = apiFunGetById();
            //System.out.println("time="+(System.currentTimeMillis()-tt)+" мс");
            if (!res7.isSuccessful()) {
                System.out.println("Ошибка запроса  " + Utils.httpError(res7));
                return;
                }
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
                return;
                }
        current = (Entity) res7.body();
        Oid.setText(""+current.getOid());
        //System.out.println("id = "+data.get(listBox.getSelectedIndex()).getOid()+
        //        "===========================================\n"+res7.body());
        showRecord();
        }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton3 = new javax.swing.JButton();
        GetAll = new javax.swing.JButton();
        listBox = new java.awt.Choice();
        Update = new javax.swing.JButton();
        Add = new javax.swing.JButton();
        Delete = new javax.swing.JButton();
        Title = new javax.swing.JLabel();
        Oid = new javax.swing.JTextField();

        jButton3.setText("jButton3");

        setLayout(null);

        GetAll.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/refresh.png"))); // NOI18N
        GetAll.setBorderPainted(false);
        GetAll.setContentAreaFilled(false);
        GetAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GetAllActionPerformed(evt);
            }
        });
        add(GetAll);
        GetAll.setBounds(100, 0, 30, 30);

        listBox.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                listBoxMouseClicked(evt);
            }
        });
        listBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                listBoxItemStateChanged(evt);
            }
        });
        add(listBox);
        listBox.setBounds(200, 3, 280, 25);

        Update.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/save.png"))); // NOI18N
        Update.setBorderPainted(false);
        Update.setContentAreaFilled(false);
        Update.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UpdateActionPerformed(evt);
            }
        });
        add(Update);
        Update.setBounds(570, 0, 30, 30);

        Add.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/add.png"))); // NOI18N
        Add.setBorderPainted(false);
        Add.setContentAreaFilled(false);
        Add.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                AddMouseClicked(evt);
            }
        });
        Add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddActionPerformed(evt);
            }
        });
        add(Add);
        Add.setBounds(490, 0, 30, 30);

        Delete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/remove.png"))); // NOI18N
        Delete.setBorderPainted(false);
        Delete.setContentAreaFilled(false);
        Delete.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                DeleteMouseClicked(evt);
            }
        });
        Delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DeleteActionPerformed(evt);
            }
        });
        add(Delete);
        Delete.setBounds(530, 0, 30, 30);

        Title.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        Title.setText("Наименование");
        add(Title);
        Title.setBounds(0, 0, 100, 30);
        add(Oid);
        Oid.setBounds(140, 0, 50, 25);
    }// </editor-fold>//GEN-END:initComponents

    private void GetAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GetAllActionPerformed
        getAllEvent();
        getById();
    }//GEN-LAST:event_GetAllActionPerformed
    @Override
    public void updateAction(KeyEvent evt){
        try {
            if (listBox.getItemCount() == 0) return;
            updateRecord();
            Response<String> res7 = apiFunUpdate();
            if (!res7.isSuccessful()) {
                main.viewUpdate(evt,false);
                System.out.println("Ошибка запроса  " + Utils.httpError(res7));
                return;
                }
            //System.out.println("Изменен "+res7.body());
            long id = current.getOid();
            getAllEvent();
            seekToId(id);
            newRecord = false;
            setUpdateState(false);
            main.viewUpdate(evt,true);
        } catch (IOException ex) {
            main.viewUpdate(evt,false);
            System.out.println(ex.getMessage());
            return;
            }
        }
    private void UpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UpdateActionPerformed
        updateAction(null);
    }//GEN-LAST:event_UpdateActionPerformed

    private void listBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_listBoxItemStateChanged
        getById();
    }//GEN-LAST:event_listBoxItemStateChanged

    public void seekToId(long id){
        for(int idx=0;idx<data.size();idx++){
            if (data.get(idx).getOid()==id){
                listBox.select(idx);
                getById();
                break;
                }
            }
        }
    public void getById(long oid){
        int idx = data.getIdxById(oid);
        if (idx!=-1)
        listBox.select(idx);
        getById();
    }
    private I_Button okAppend = new I_Button() {
        @Override
        public void onPush() {
            try {
                Response<JLong> res7 = apiFunAdd();
                if (!res7.isSuccessful()) {
                    System.out.println("Ошибка запроса  " + Utils.httpError(res7));
                    return;
                }
                long id = res7.body().getValue();
                //System.out.println("Добавлен id="+id);
                getAllEvent();
                seekToId(id);
                setUpdateState(true);
                newRecord = true;
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
                return;
                }
            }
        };
    private void AddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddActionPerformed
        new OK(400,300,"Добавить \'"+ entityTitleName+"\' ?",okAppend);
    }//GEN-LAST:event_AddActionPerformed

    private I_Button okDelete = new I_Button() {
        @Override
        public void onPush() {
            try {
                Response<JBoolean> res = main.service.removeEntity(main.debugToken,entityName,data.get(listBox().getSelectedIndex()).getOid()).execute();
                if (!res.isSuccessful()) {
                    System.out.println("Ошибка запроса  " + Utils.httpError(res));
                    return;
                }
                if (res.body().value()){
                    getAllEvent();
                }
                else
                    System.out.println("Ошибка удаления");
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
                return;
                }
            }
        };
    private void DeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DeleteActionPerformed
        //-------------------------------- Удалить ----------------------------------------
        if (listBox.getItemCount() == 0) return;
        new OK(400,300,"Удалить: "+current.getTitle()+" ?",okDelete);
        }//GEN-LAST:event_DeleteActionPerformed

    private void listBoxMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listBoxMouseClicked
        if (evt.getButton()==3){
            if (!isPopup){
                popup.setVisible(true);
                if (data.size()==0)
                    getAllEvent();
                listBox.setBounds(240,0,240,25);
                }
            else{
                popup.setVisible(false);
                listBox.setBounds(200,0,280,25);
                }
            isPopup=!isPopup;
        }
    }//GEN-LAST:event_listBoxMouseClicked


    private void AddMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_AddMouseClicked
        main.onRightButton(main,Add,BasePanel.PanelOffsetY+this.getY(),evt,"Добавить \'"+entityTitleName+"\'");
    }//GEN-LAST:event_AddMouseClicked

    private void DeleteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_DeleteMouseClicked
        main.onRightButton(main,Delete,BasePanel.PanelOffsetY+this.getY(),evt,"Удалить \'"+entityTitleName+"\'");
    }//GEN-LAST:event_DeleteMouseClicked

    public JButton getAddButton(){
        return Add; }
    public JButton getDeleteButton(){
        return Delete; }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Add;
    private javax.swing.JButton Delete;
    private javax.swing.JButton GetAll;
    private javax.swing.JTextField Oid;
    private javax.swing.JLabel Title;
    private javax.swing.JButton Update;
    private javax.swing.JButton jButton3;
    private java.awt.Choice listBox;
    // End of variables declaration//GEN-END:variables
}
