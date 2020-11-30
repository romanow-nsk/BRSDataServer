/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package firefighter.desktop;

import firefighter.core.constants.Values;
import firefighter.core.entity.artifacts.Artifact;
import firefighter.core.entity.artifacts.ArtifactList;
import firefighter.core.utils.OwnDateTime;
import retrofit2.Call;

/**
 *
 * @author romanow
 */
public class ArtifactPanel extends BasePanel{
    private PlayerPanel player;
    public ArtifactPanel() {
        initComponents();
        FileType.removeAll();
        for(String ss : Values.ArtifactTypeNames)
            FileType.add(ss);
        ParentType.removeAll();
        ParentType.add("");
        for (String ss : Values.ArtifactParentList){
            String name = Values.EntityFactory.getEntityNameBySimpleClass(ss);
            if (name!=null)
            ParentType.add(ss);
            }
        player = new PlayerPanel();
        player.setBounds(450,370,180,50);
        add(player);
        player.setVisible(false);
        ViewPhoto.setVisible(false);
        ViewVideo.setVisible(false);
        }
    public void initPanel(Client main0){
        super.initPanel(main0);
        }
    private String nameMask="";
    private String fileNameMask="";
    private long date1=0;
    private long date2=0;
    private long size1=0;
    private long size2=0;
    private Artifact selected=null;
    private ArtifactList list = new ArtifactList();
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        ResetDateInterval = new javax.swing.JButton();
        ToOwner = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        Date2 = new javax.swing.JTextField();
        NameMask = new javax.swing.JTextField();
        FileNameMask = new javax.swing.JTextField();
        FSize2 = new javax.swing.JTextField();
        ParentOid = new javax.swing.JTextField();
        ResetNameMask = new javax.swing.JButton();
        ResetFileNameMask = new javax.swing.JButton();
        ResetSize = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        Refresh = new javax.swing.JButton();
        FSize1 = new javax.swing.JTextField();
        FileList = new java.awt.Choice();
        Date1 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        ParentEntity = new javax.swing.JTextField();
        DownLoad = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        ParentType = new java.awt.Choice();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        Name = new javax.swing.JTextField();
        FileName = new javax.swing.JTextField();
        FSize = new javax.swing.JTextField();
        CreateDate = new javax.swing.JTextField();
        NoFile = new javax.swing.JCheckBox();
        jLabel13 = new javax.swing.JLabel();
        FileType = new java.awt.Choice();
        ViewPhoto = new javax.swing.JButton();
        ViewVideo = new javax.swing.JButton();

        setLayout(null);

        ResetDateInterval.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/remove.png"))); // NOI18N
        ResetDateInterval.setBorderPainted(false);
        ResetDateInterval.setContentAreaFilled(false);
        ResetDateInterval.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ResetDateIntervalActionPerformed(evt);
            }
        });
        add(ResetDateInterval);
        ResetDateInterval.setBounds(440, 150, 30, 30);

        ToOwner.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/right.PNG"))); // NOI18N
        ToOwner.setBorderPainted(false);
        ToOwner.setContentAreaFilled(false);
        ToOwner.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ToOwnerActionPerformed(evt);
            }
        });
        add(ToOwner);
        ToOwner.setBounds(490, 310, 40, 40);

        jLabel2.setText("Название (маска)");
        add(jLabel2);
        jLabel2.setBounds(60, 65, 140, 14);

        jLabel4.setText("Тип файла");
        add(jLabel4);
        jLabel4.setBounds(60, 35, 110, 14);

        jLabel3.setText("Имя файла  (маска)");
        add(jLabel3);
        jLabel3.setBounds(60, 95, 120, 14);

        Date2.setText("...");
        Date2.setEnabled(false);
        Date2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Date2MouseClicked(evt);
            }
        });
        add(Date2);
        Date2.setBounds(320, 150, 110, 25);
        add(NameMask);
        NameMask.setBounds(200, 60, 230, 25);
        add(FileNameMask);
        FileNameMask.setBounds(200, 90, 230, 25);
        add(FSize2);
        FSize2.setBounds(320, 120, 110, 25);

        ParentOid.setEnabled(false);
        add(ParentOid);
        ParentOid.setBounds(360, 370, 70, 25);

        ResetNameMask.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/remove.png"))); // NOI18N
        ResetNameMask.setBorderPainted(false);
        ResetNameMask.setContentAreaFilled(false);
        ResetNameMask.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ResetNameMaskActionPerformed(evt);
            }
        });
        add(ResetNameMask);
        ResetNameMask.setBounds(440, 60, 30, 30);

        ResetFileNameMask.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/remove.png"))); // NOI18N
        ResetFileNameMask.setBorderPainted(false);
        ResetFileNameMask.setContentAreaFilled(false);
        ResetFileNameMask.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ResetFileNameMaskActionPerformed(evt);
            }
        });
        add(ResetFileNameMask);
        ResetFileNameMask.setBounds(440, 90, 30, 30);

        ResetSize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/remove.png"))); // NOI18N
        ResetSize.setBorderPainted(false);
        ResetSize.setContentAreaFilled(false);
        ResetSize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ResetSizeActionPerformed(evt);
            }
        });
        add(ResetSize);
        ResetSize.setBounds(440, 120, 30, 30);

        jLabel6.setText("Собственник");
        add(jLabel6);
        jLabel6.setBounds(60, 185, 140, 14);

        Refresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/refresh.png"))); // NOI18N
        Refresh.setBorderPainted(false);
        Refresh.setContentAreaFilled(false);
        Refresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RefreshActionPerformed(evt);
            }
        });
        add(Refresh);
        Refresh.setBounds(20, 30, 30, 30);
        add(FSize1);
        FSize1.setBounds(200, 120, 110, 25);

        FileList.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                FileListItemStateChanged(evt);
            }
        });
        add(FileList);
        FileList.setBounds(60, 220, 500, 20);

        Date1.setText("...");
        Date1.setEnabled(false);
        Date1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Date1MouseClicked(evt);
            }
        });
        add(Date1);
        Date1.setBounds(200, 150, 110, 25);

        jLabel7.setText("Создан  от...до...");
        add(jLabel7);
        jLabel7.setBounds(60, 155, 120, 14);

        ParentEntity.setEnabled(false);
        add(ParentEntity);
        ParentEntity.setBounds(200, 370, 150, 25);

        DownLoad.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/download.png"))); // NOI18N
        DownLoad.setBorderPainted(false);
        DownLoad.setContentAreaFilled(false);
        DownLoad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DownLoadActionPerformed(evt);
            }
        });
        add(DownLoad);
        DownLoad.setBounds(450, 310, 40, 40);
        add(jSeparator1);
        jSeparator1.setBounds(60, 210, 500, 2);
        add(ParentType);
        ParentType.setBounds(200, 180, 190, 20);

        jLabel8.setText("Размер от..до..");
        add(jLabel8);
        jLabel8.setBounds(60, 125, 120, 14);

        jLabel9.setText("Название");
        add(jLabel9);
        jLabel9.setBounds(60, 250, 140, 14);

        jLabel10.setText("Имя файла");
        add(jLabel10);
        jLabel10.setBounds(60, 280, 120, 14);

        jLabel11.setText("Размер ");
        add(jLabel11);
        jLabel11.setBounds(60, 310, 120, 14);

        jLabel12.setText("Дата создания");
        add(jLabel12);
        jLabel12.setBounds(60, 350, 120, 14);

        Name.setEnabled(false);
        add(Name);
        Name.setBounds(200, 250, 360, 25);

        FileName.setEnabled(false);
        add(FileName);
        FileName.setBounds(200, 280, 360, 25);

        FSize.setEnabled(false);
        add(FSize);
        FSize.setBounds(200, 310, 110, 25);

        CreateDate.setEnabled(false);
        add(CreateDate);
        CreateDate.setBounds(200, 340, 110, 25);

        NoFile.setText(" Нет файла");
        NoFile.setEnabled(false);
        add(NoFile);
        NoFile.setBounds(360, 310, 90, 23);

        jLabel13.setText("Собственник (имя,oid)");
        add(jLabel13);
        jLabel13.setBounds(60, 380, 140, 14);
        add(FileType);
        FileType.setBounds(200, 30, 190, 20);

        ViewPhoto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/camera.png"))); // NOI18N
        ViewPhoto.setBorderPainted(false);
        ViewPhoto.setContentAreaFilled(false);
        ViewPhoto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ViewPhotoActionPerformed(evt);
            }
        });
        add(ViewPhoto);
        ViewPhoto.setBounds(450, 370, 40, 30);

        ViewVideo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/video.png"))); // NOI18N
        ViewVideo.setBorderPainted(false);
        ViewVideo.setContentAreaFilled(false);
        ViewVideo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ViewVideoActionPerformed(evt);
            }
        });
        add(ViewVideo);
        ViewVideo.setBounds(450, 370, 40, 30);
    }// </editor-fold>//GEN-END:initComponents

    private void ResetNameMaskActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ResetNameMaskActionPerformed
        nameMask="";
        NameMask.setText("");
    }//GEN-LAST:event_ResetNameMaskActionPerformed

    private void ResetFileNameMaskActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ResetFileNameMaskActionPerformed
        fileNameMask="";
        FileNameMask.setText("");
    }//GEN-LAST:event_ResetFileNameMaskActionPerformed

    private void ResetSizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ResetSizeActionPerformed
        FSize1.setText("");
        FSize2.setText("");
        size1=0;
        size2=0;
    }//GEN-LAST:event_ResetSizeActionPerformed

    private void ResetDateIntervalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ResetDateIntervalActionPerformed
        date1=0;
        Date1.setText("...");
        date2=0;
        Date2.setText("...");
    }//GEN-LAST:event_ResetDateIntervalActionPerformed

    private void Date2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Date2MouseClicked
        if (evt.getClickCount()<2)
            return;
        new CalendarView(new I_Calendar() {
            @Override
            public void onMonth(int daInMonth, int month, int year){ }
            @Override
            public void onDay(int day, int month, int year) {
                OwnDateTime date = new OwnDateTime(day,month,year);
                Date2.setText(date.dateToString());
                date2 = date.timeInMS();
            }
            @Override
            public void onDayRight(int day, int month, int year) { }
        });

    }//GEN-LAST:event_Date2MouseClicked

    private void RefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RefreshActionPerformed
        new APICall<ArtifactList>(main) {
            @Override
            public Call<ArtifactList> apiFun() {
                size1=0;
                String ss= FSize1.getText();
                if (ss.length()!=0){
                    try {
                        size1 = Long.parseLong(ss);
                        } catch(Exception ee){
                            System.out.println("Недопустимое значение "+ss+" установлено 0");
                            }
                    }
                size2=0;
                ss= FSize2.getText();
                if (ss.length()!=0){
                    try {
                        size2 = Long.parseLong(ss);
                        } catch(Exception ee){
                            System.out.println("Недопустимое значение "+ss+" установлено 0");
                            }
                    }
                String zz = "";
                int idx = ParentType.getSelectedIndex();
                if (idx!=0)
                    zz = Values.ArtifactParentList[idx-1];
                return main.service.getArtifactConditionList(main.debugToken,FileType.getSelectedIndex(),zz,
                        NameMask.getText(),FileNameMask.getText(),size1,size2,date1,date2);
                }
            @Override
            public void onSucess(ArtifactList oo) {
                list = oo;
                FileList.removeAll();
                list.sortById();
                for(Artifact ctr : list)
                    FileList.add("["+ctr.getOid()+"] "+ctr.getTitle());
                showSelected();
                }
        };
    }//GEN-LAST:event_RefreshActionPerformed

    private void showSelected(){
        if (list.size()==0){
            selected=null;
            Name.setText("");
            FileName.setText("");
            FSize.setText("");
            CreateDate.setText("");
            ParentEntity.setText("");
            ParentOid.setText("");
            NoFile.setSelected(false);
            return;
            }
        selected=list.get(FileList.getSelectedIndex());
        Name.setText(selected.getName());
        FileName.setText(selected.getOriginalName());
        FSize.setText(""+selected.getFileSize());
        CreateDate.setText(selected.getDate().dateTimeToString());
        String ss = selected.getParentName();
        if (ss.length()==0)
            ParentEntity.setText("");
        else
            ParentEntity.setText(Values.EntityFactory.getEntityNameBySimpleClass(ss));
        ParentOid.setText(""+selected.getParentOid());
        NoFile.setSelected(selected.isFileLost());
        if (selected.type()==Values.ArtifactAudioType){
            player.setVisible(true);
            player.setURL(main.createURLForArtifact(selected));
            }
        else{
            player.setVisible(false);
            }
        ViewPhoto.setVisible(selected.type()==Values.ArtifactImageType);
        ViewVideo.setVisible(selected.type()==Values.ArtifactVideoType);
        }

    private void ToOwnerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ToOwnerActionPerformed
    }//GEN-LAST:event_ToOwnerActionPerformed

    private void Date1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Date1MouseClicked
        if (evt.getClickCount()<2)
            return;
        new CalendarView(new I_Calendar() {
            @Override
            public void onMonth(int daInMonth, int month, int year){ }
            @Override
            public void onDay(int day, int month, int year) {
                OwnDateTime date = new OwnDateTime(day,month,year);
                Date1.setText(date.dateToString());
                date1 = date.timeInMS();
            }
            @Override
            public void onDayRight(int day, int month, int year) { }
        });

    }//GEN-LAST:event_Date1MouseClicked

    private void DownLoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DownLoadActionPerformed
        if (selected==null)
            return;
        main.loadFile(selected);
    }//GEN-LAST:event_DownLoadActionPerformed

    private void FileListItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_FileListItemStateChanged
        showSelected();
    }//GEN-LAST:event_FileListItemStateChanged

    private void ViewPhotoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ViewPhotoActionPerformed
        main.showImageArtifact(selected);
    }//GEN-LAST:event_ViewPhotoActionPerformed

    private void ViewVideoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ViewVideoActionPerformed
        main.showVideoArtifact(selected);
    }//GEN-LAST:event_ViewVideoActionPerformed

    @Override
    public void refresh() {}

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
    private javax.swing.JTextField CreateDate;
    private javax.swing.JTextField Date1;
    private javax.swing.JTextField Date2;
    private javax.swing.JButton DownLoad;
    private javax.swing.JTextField FSize;
    private javax.swing.JTextField FSize1;
    private javax.swing.JTextField FSize2;
    private java.awt.Choice FileList;
    private javax.swing.JTextField FileName;
    private javax.swing.JTextField FileNameMask;
    private java.awt.Choice FileType;
    private javax.swing.JTextField Name;
    private javax.swing.JTextField NameMask;
    private javax.swing.JCheckBox NoFile;
    private javax.swing.JTextField ParentEntity;
    private javax.swing.JTextField ParentOid;
    private java.awt.Choice ParentType;
    private javax.swing.JButton Refresh;
    private javax.swing.JButton ResetDateInterval;
    private javax.swing.JButton ResetFileNameMask;
    private javax.swing.JButton ResetNameMask;
    private javax.swing.JButton ResetSize;
    private javax.swing.JButton ToOwner;
    private javax.swing.JButton ViewPhoto;
    private javax.swing.JButton ViewVideo;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JSeparator jSeparator1;
    // End of variables declaration//GEN-END:variables
}