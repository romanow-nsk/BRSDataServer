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
import firefighter.core.entity.artifacts.Artifact;
import firefighter.core.entity.artifacts.ReportFile;
import firefighter.core.entity.baseentityes.JBoolean;
import firefighter.core.entity.baseentityes.JEmpty;
import firefighter.core.entity.baseentityes.JInt;
import firefighter.core.entity.notifications.NTList;
import firefighter.core.entity.notifications.NTMessage;
import firefighter.core.entity.users.User;
import retrofit2.Response;

import java.io.IOException;

/**
 *
 * @author romanow
 */
public class ReportsPanelBase extends BasePanel{
    private EntityBasePanel reportsPanel;
    private NTList notificationList = new NTList();
    private volatile boolean logOn=false;
    private boolean working=false;
    private String userToken="";
    private User user = new User();
    private NTMessage ntMessage;
    private EntityList<ReportFile> reportList = new EntityList<>();
    private String reports[] = {};

    public ReportsPanelBase() {
        initComponents();
    }
    public void initPanel(Client main0){
        super.initPanel(main0);
        ParamList.setVisible(false);
        ReportList.removeAll();
        for(String ss : reports)
            ReportList.add(ss);
        ReportType.removeAll();
        for(int i = 0; i< Values.ReportTypes.length; i++)
            ReportType.add(Values.ReportTypes[i]);
        Reports.removeAll();
        Reports.add("Все");
        for(String ss : Values.Reports)
            Reports.add(ss);
        reportsPanel = new EntityPanelUni(10,20,reportList,"ReportFile",main,true,1,1){
            @Override
            public boolean isRecordSelected(Entity ent) {
                int idx = Reports.getSelectedIndex();
                return idx==0 || idx-1 == ((ReportFile)ent).getReportType();
                }
            @Override
            public void showRecord() {
                ReportFile st = (ReportFile) current;
                ReportName.setText(st.toShortString());
                Archive.setSelected(st.isArchive());
                }
            @Override
            public void updateRecord() {
                }
            };
        add(reportsPanel);
        }
    @Override
    public void refresh() {
        working=true;
        //reportsPanel.getAllEvent();
        //defectSheetPanel.getAllEvent();
        }

        /**
         * This method is called from within the constructor to initialize the form.
         * WARNING: Do NOT modify this code. The content of this method is always
         * regenerated by the Form Editor.
         */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        list1 = new java.awt.List();
        textField1 = new java.awt.TextField();
        ReportName = new javax.swing.JTextField();
        LoadReport = new javax.swing.JButton();
        NotifyList = new java.awt.Choice();
        ReportType = new java.awt.Choice();
        jLabel1 = new javax.swing.JLabel();
        Login = new javax.swing.JCheckBox();
        RemoveNotify = new javax.swing.JButton();
        Header = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        Message = new javax.swing.JTextArea();
        Artifact = new javax.swing.JTextField();
        LoadArtifact = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        Full = new javax.swing.JCheckBox();
        Reports = new java.awt.Choice();
        Archive = new javax.swing.JCheckBox();
        jLabel31 = new javax.swing.JLabel();
        ReportList = new java.awt.Choice();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        CreateReport = new javax.swing.JButton();
        ParamList = new java.awt.Choice();

        textField1.setText("textField1");

        setLayout(null);
        add(ReportName);
        ReportName.setBounds(150, 60, 470, 25);

        LoadReport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/download.png"))); // NOI18N
        LoadReport.setBorderPainted(false);
        LoadReport.setContentAreaFilled(false);
        LoadReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LoadReportActionPerformed(evt);
            }
        });
        add(LoadReport);
        LoadReport.setBounds(630, 50, 40, 40);

        NotifyList.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                NotifyListItemStateChanged(evt);
            }
        });
        add(NotifyList);
        NotifyList.setBounds(10, 190, 520, 20);
        add(ReportType);
        ReportType.setBounds(10, 120, 130, 20);

        jLabel1.setText("Уведомления Нач.ТО");
        add(jLabel1);
        jLabel1.setBounds(10, 170, 150, 14);

        Login.setText("Включить уведомления");
        Login.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                LoginItemStateChanged(evt);
            }
        });
        add(Login);
        Login.setBounds(10, 210, 170, 23);

        RemoveNotify.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/remove.png"))); // NOI18N
        RemoveNotify.setBorderPainted(false);
        RemoveNotify.setContentAreaFilled(false);
        RemoveNotify.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RemoveNotifyActionPerformed(evt);
            }
        });
        add(RemoveNotify);
        RemoveNotify.setBounds(550, 190, 30, 30);

        Header.setEnabled(false);
        add(Header);
        Header.setBounds(10, 240, 510, 25);

        Message.setColumns(20);
        Message.setRows(5);
        Message.setEnabled(false);
        jScrollPane1.setViewportView(Message);

        add(jScrollPane1);
        jScrollPane1.setBounds(10, 270, 510, 50);

        Artifact.setEnabled(false);
        add(Artifact);
        Artifact.setBounds(10, 330, 470, 25);

        LoadArtifact.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/download.png"))); // NOI18N
        LoadArtifact.setBorderPainted(false);
        LoadArtifact.setContentAreaFilled(false);
        LoadArtifact.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LoadArtifactActionPerformed(evt);
            }
        });
        add(LoadArtifact);
        LoadArtifact.setBounds(500, 330, 30, 30);
        add(jSeparator1);
        jSeparator1.setBounds(10, 370, 550, 10);
        add(jSeparator2);
        jSeparator2.setBounds(20, 162, 550, 10);

        Full.setText("полный");
        add(Full);
        Full.setBounds(420, 90, 80, 23);

        Reports.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ReportsItemStateChanged(evt);
            }
        });
        add(Reports);
        Reports.setBounds(10, 60, 130, 20);

        Archive.setText("архивный");
        Archive.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ArchiveItemStateChanged(evt);
            }
        });
        add(Archive);
        Archive.setBounds(310, 90, 120, 23);

        jLabel31.setText("Дата регламента");
        add(jLabel31);
        jLabel31.setBounds(10, 250, 110, 14);

        ReportList.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ReportListItemStateChanged(evt);
            }
        });
        add(ReportList);
        ReportList.setBounds(150, 120, 150, 20);

        jLabel2.setText("Отчет");
        add(jLabel2);
        jLabel2.setBounds(150, 100, 90, 14);

        jLabel3.setText("Файл");
        add(jLabel3);
        jLabel3.setBounds(10, 100, 90, 14);

        CreateReport.setText("Создать");
        CreateReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CreateReportActionPerformed(evt);
            }
        });
        add(CreateReport);
        CreateReport.setBounds(310, 120, 100, 23);
        add(ParamList);
        ParamList.setBounds(430, 120, 190, 20);
    }// </editor-fold>//GEN-END:initComponents

    private void LoadReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LoadReportActionPerformed
        Artifact art = ((ReportFile)reportsPanel.current).getArtifact().getRef();
        main.loadFile(art);
    }//GEN-LAST:event_LoadReportActionPerformed


    private void NotifyListItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_NotifyListItemStateChanged
        if (NotifyList.getItemCount()==0 || !logOn)
            return;
        refreshNotify();
    }//GEN-LAST:event_NotifyListItemStateChanged

    private void procNotifications(){
        try {
            Response<EntityList<NTMessage>> ss = main.service.getNotificationUserList(userToken,0,Values.UserSuperAdminType,Values.NSSend).execute();
            if (!ss.isSuccessful()) {
                System.out.println("Ошибка запроса  " + Utils.httpError(ss));
            } else {
                EntityList<NTMessage> list = ss.body();
                boolean changed = false;
                for (NTMessage note : list) {
                    changed |= notificationList.add(note);
                    note.setState(Values.NSReceived);
                    Response<JEmpty> rr = main.service.setNotificationState(userToken,note.getOid(),Values.NSReceived).execute();
                    if (!ss.isSuccessful()) {
                        System.out.println("Ошибка запроса  " + Utils.httpError(rr));
                        }
                    }
                if (changed){
                    refreshNotifyList();
                    main.panelToFront(this);
                    }
                }
            } catch (Exception ee) {
            System.out.println("Ошибка запроса  " + ee.toString());
            }
        }

    public void refreshNotifyList(){
        NotifyList.removeAll();
        for(NTMessage mes : notificationList.getData()){
            NotifyList.add(mes.getTitle());
            }
        refreshNotify();
        }
    public void refreshNotify() {
        if (NotifyList.getItemCount()==0)
            return;
        int idx = NotifyList.getSelectedIndex();
        ntMessage = notificationList.getData().get(idx);
        Header.setText(ntMessage.getTitle()+": "+ntMessage.getHeader());
        Message.setText(ntMessage.getMessage());
        Artifact art = ntMessage.getArtifact().getRef();
        boolean bb = art==null || art.getOid()==0;
        LoadArtifact.setVisible(!bb);
        Artifact.setVisible(!bb);
        if (!bb)
            Artifact.setText(art.getTitle());
        }

    private Thread keepAliveThread = null;
    Runnable keepAlive = new Runnable() {
        @Override
        public void run() {
            while(logOn){
                try {
                    Thread.sleep(Values.CKeepALiveTime*1000);
                    } catch (InterruptedException e) {}
                if (!logOn)
                    break;
                try {
                    Response<JInt> ss = main.service.getNotificationCount(userToken).execute();
                    if (!ss.isSuccessful()) {
                        System.out.println("Ошибка запроса  " + Utils.httpError(ss));
                    } else {
                        if (ss.body().getValue()!=0)
                            procNotifications();
                        }
                    } catch (Exception ee) {
                        System.out.println("Ошибка запроса  " + ee.toString());
                        }
                }
            }
        };

    private void LoginItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_LoginItemStateChanged
        if (!working)
            return;
        if (Login.isSelected()) {
            try {
                Response<User> ss = main.service.login("89131111111", "1234").execute();
                if (!ss.isSuccessful()) {
                    System.out.println("Ошибка запроса  " + Utils.httpError(ss));
                } else {
                    System.out.println(ss.body().toString());
                    user = ss.body();
                    userToken = user.getSessionToken();
                    System.out.println(userToken);
                    logOn=true;
                    keepAliveThread = new Thread(keepAlive);
                    keepAliveThread.start();
                    Response<EntityList<NTMessage>> zz = main.service.getNotificationUserList(userToken,0,Values.UserSuperAdminType,Values.NSReceived).execute();
                    if (!ss.isSuccessful()) {
                        System.out.println("Ошибка запроса  " + Utils.httpError(ss));
                    } else {
                        EntityList<NTMessage> list = zz.body();
                        for (NTMessage note : list) {
                            final NTMessage note1 = note;
                            notificationList.add(note);
                            }
                        refreshNotifyList();
                        }
                    }
            } catch (Exception ee) {
                System.out.println("Ошибка запроса  " + ee.toString());
                }
            }
        else{
            try {
                Response<JEmpty> ss = main.service.logoff(userToken).execute();
                if (!ss.isSuccessful()) {
                    System.out.println("Ошибка запроса  " + Utils.httpError(ss));
                    }
                else {
                    logOn = false;
                    keepAliveThread.interrupt();
                    keepAliveThread=null;
                    }
                } catch (Exception ee) {
                    System.out.println("Ошибка запроса  " + ee.toString());
                    }
            }
        }//GEN-LAST:event_LoginItemStateChanged

    private void RemoveNotifyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RemoveNotifyActionPerformed
        try {
            if (NotifyList.getSelectedIndex()==0) return;
            long oid = notificationList.getData().get(NotifyList.getSelectedIndex()).getOid();
            Response<JBoolean> ss = main.service.removeNotification(userToken,oid).execute();
            if (!ss.isSuccessful()) {
                System.out.println("Ошибка запроса  " + Utils.httpError(ss));
                return;
                }
            notificationList.remove(oid);
            refreshNotifyList();
            } catch (IOException e) {
                System.out.println("Ошибка запроса  " + e.toString());
                }
    }//GEN-LAST:event_RemoveNotifyActionPerformed

    private void LoadArtifactActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LoadArtifactActionPerformed
        main.loadFile(ntMessage.getArtifact().getRef());
    }//GEN-LAST:event_LoadArtifactActionPerformed

    private void ArchiveItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ArchiveItemStateChanged
        ReportFile st = (ReportFile) reportsPanel.current;
        if (st==null) return;
        st.setArchive(Archive.isSelected());
        reportsPanel.updateRecord();
    }//GEN-LAST:event_ArchiveItemStateChanged

    private void ReportsItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ReportsItemStateChanged
        reportsPanel.getAllEvent();
    }//GEN-LAST:event_ReportsItemStateChanged


    private void ReportListItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ReportListItemStateChanged
        ParamList.setVisible(false);
        ParamList.removeAll();
        }//GEN-LAST:event_ReportListItemStateChanged

    private void CreateReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CreateReportActionPerformed
        new OK(200, 200, "Отчет "+ReportList.getSelectedItem(), new I_Button() {
            @Override
            public void onPush() {
                switch(ReportList.getSelectedIndex()){
                }
            }
        });
    }//GEN-LAST:event_CreateReportActionPerformed

    @Override
    public void eventPanel(int code, int par1, long par2, String par3) {
        if (code==EventRefreshSettings){
            refresh();
            main.sendEventPanel(EventRefreshSettingsDone,0,0,"");
            }
    }

    @Override
    public void shutDown() {

    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox Archive;
    private javax.swing.JTextField Artifact;
    private javax.swing.JButton CreateReport;
    private javax.swing.JCheckBox Full;
    private javax.swing.JTextField Header;
    private javax.swing.JButton LoadArtifact;
    private javax.swing.JButton LoadReport;
    private javax.swing.JCheckBox Login;
    private javax.swing.JTextArea Message;
    private java.awt.Choice NotifyList;
    private java.awt.Choice ParamList;
    private javax.swing.JButton RemoveNotify;
    private java.awt.Choice ReportList;
    private javax.swing.JTextField ReportName;
    private java.awt.Choice ReportType;
    private java.awt.Choice Reports;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private java.awt.List list1;
    private java.awt.TextField textField1;
    // End of variables declaration//GEN-END:variables
}
