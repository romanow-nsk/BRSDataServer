/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package firefighter.desktop;

import com.google.gson.Gson;
import firefighter.core.DBRequest;
import firefighter.core.Utils;
import firefighter.core.constants.Values;
import firefighter.core.constants.ValuesBase;
import firefighter.core.entity.Entity;
import firefighter.core.entity.EntityList;
import firefighter.core.entity.I_Compare;
import firefighter.core.entity.baseentityes.JBoolean;
import firefighter.core.entity.baseentityes.JEmpty;
import firefighter.core.entity.baseentityes.JLong;
import retrofit2.Response;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;


/**
 *
 * @author romanow
 */
public abstract class EntityPanelUni extends EntityBasePanel{
    abstract public void updateRecord();
    EntityList<Entity> data;
    private boolean fullMode;
    private boolean newRecord=false;
    private MainBaseFrame main;
    private PopupList popup;
    private boolean isPopup=false;
    private boolean needUpdate=false;
    private int levelAll;
    private int levelGet;
    private Gson gson = new Gson();
    public boolean isNewRecord() {
        return newRecord; }

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
    public EntityPanelUni(int x0, int y0, EntityList data0, String name0, MainBaseFrame base0, boolean fullMode0,int levelAll0, int levelGet0) {
        initComponents();
        fullMode = fullMode0;
        levelAll = levelAll0;
        levelGet = levelGet0;
        Delete.setVisible(fullMode);
        Update.setVisible(fullMode);
        data = data0 !=null ? data0 : new EntityList();
        entityName = name0;
        main = base0;
        setBounds(x0,y0,600,35);
        entityTitleName = Values.EntityFactory.getEntityNameBySimpleClass(entityName);
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
    public EntityPanelUni() {
        initComponents();
    }
    public boolean isRecordSelected(Entity ent){ return true; }
    public EntityList<Entity> getLazy(){
        return null;
        }
    //-------------------------------- Читать всех ----------------------------------------
    public void getAllEvent(){
        current=null;
        listBox.removeAll();
        data.clear();
        EntityList<Entity> xx = null;
        try {
            long tt = System.currentTimeMillis();
            xx = getLazy();
            if (xx!=null) {
                for (Entity ee : xx){
                    if (isRecordSelected(ee))
                        data.add(ee);
                    }
                }
            else{
                Response<ArrayList<DBRequest>> res7=null;
                res7 = main.service.getEntityList(main.debugToken, entityName,Values.GetAllModeActual,levelAll).execute();
                //System.out.println("time="+(System.currentTimeMillis()-tt)+" мс");
                if (!res7.isSuccessful()) {
                    System.out.println("Ошибка запроса  " + Utils.httpError(res7));
                    return;
                    }
                ArrayList<DBRequest> zz  = res7.body();
                for (DBRequest ee : zz){
                    Entity dd = ee.get(gson);
                    if (isRecordSelected(dd))
                        data.add(dd);
                    }
                }
            } catch (Exception e) {
                System.out.println(e.toString());
                return;
                }
        data.sort(new I_Compare() {
            @Override
            public int compare(Entity one, Entity two) {
                return 0;
            }
        },false);
        for(Entity vv : data){
            listBox.add(vv.getTitle());
            }
        setUpdateState(false);
        getById();
        }
    //-------------------------------- Читать по id ----------------------------------------
    public void getById(){
        if (listBox.getItemCount()==0) return;
        Response<DBRequest> res7 = null;
        try {
            long tt = System.currentTimeMillis();
            Response<Entity> res = apiFunGetById();
            if (res!=null){
                //System.out.println("time="+(System.currentTimeMillis()-tt)+" мс");
                if (!res.isSuccessful()) {
                    System.out.println("Ошибка запроса  " + Utils.httpError(res));
                    return;
                    }
                current = res.body();
                Oid.setText(""+current.getOid());
                showRecord();
                }
            else{
                res7 = main.service.getEntity(main.debugToken, entityName,data.get(listBox().getSelectedIndex()).getOid(),levelGet).execute();
                //System.out.println("time="+(System.currentTimeMillis()-tt)+" мс");
                if (!res7.isSuccessful()) {
                    System.out.println("Ошибка запроса  " + Utils.httpError(res7));
                    return;
                    }
                current = res7.body().get(gson);
                Oid.setText(""+current.getOid());
                showRecord();
                }
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                return;
                }
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

    public void updateAction(KeyEvent evt){
        try {
            if (listBox.getItemCount() == 0) return;
            updateRecord();
            Response<JEmpty> res7 = main.service.updateEntity(main.debugToken,new DBRequest(current,gson)).execute();
            if (!res7.isSuccessful()) {
                main.viewUpdate(evt,false);
                System.out.println("Ошибка запроса  " + Utils.httpError(res7));
                return;
                }
            long id = current.getOid();
            getAllEvent();
            seekToId(id);
            newRecord = false;
            setUpdateState(false);
            main.viewUpdate(evt,true);
        } catch (Exception ex) {
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
                Class cc  = Values.EntityFactory.getClassForSimpleName(entityName);
                if (cc==null){
                    System.out.println("Недопустимый класс  " + entityName);
                    return;
                    }
                Entity ent = (Entity)cc.newInstance();
                Response<JLong> res7 = main.service.addEntity(main.debugToken,new DBRequest(ent,gson),0).execute();
                if (!res7.isSuccessful()) {
                    System.out.println("Ошибка запроса  " + Utils.httpError(res7));
                    return;
                }
                long id = res7.body().getValue();
                getAllEvent();
                seekToId(id);
                setUpdateState(true);
                newRecord = true;
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                return;
                }
            }
        };
    private void AddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddActionPerformed
        new OK(400,300,"Добавить "+entityTitleName+" ?",okAppend);
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
        main.onRightButton(main,Add,BasePanel.PanelOffsetY+this.getY(),evt,"Добавить "+entityName);
    }//GEN-LAST:event_AddMouseClicked

    private void DeleteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_DeleteMouseClicked
        main.onRightButton(main,Delete,BasePanel.PanelOffsetY+this.getY(),evt,"Удалить "+entityName);
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
