/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package firefighter.desktop;

import firefighter.core.API.RestAPICommon;
import firefighter.core.DBRequest;
import firefighter.core.ServerState;
import firefighter.core.UniException;
import firefighter.core.Utils;
import firefighter.core.constants.ConstValue;
import firefighter.core.constants.TableItem;
import firefighter.core.constants.Values;
import firefighter.core.constants.ValuesBase;
import firefighter.core.entity.Entity;
import firefighter.core.entity.EntityList;
import firefighter.core.entity.artifacts.Artifact;
import firefighter.core.entity.base.BugMessage;
import firefighter.core.entity.base.StringList;
import firefighter.core.entity.baseentityes.JBoolean;
import firefighter.core.entity.baseentityes.JEmpty;
import firefighter.core.entity.baseentityes.JString;
import firefighter.core.ftp.AsyncTaskBack;
import firefighter.core.ftp.ClientFileWriter;
import firefighter.core.utils.FileNameExt;
import firefighter.dataserver.DBExample;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Response;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author romanow
 */
public class ServerPanel extends BasePanel{
    private final static int StateLoopDelay=10;
    private boolean isShown=false;
    private boolean onFront=false;
    private boolean isDown=false;
    private Client client;
    private boolean onBusy=false;
    private Thread stateLoopThread;
    private EntityPanel bugPanel;
    private EntityList<BugMessage> bugList = new EntityList<>();
    private ServerState serverState=new ServerState();
    private EntityList<?> records = new EntityList<>();
    private ArrayList<TableItem> classes;
    private String entityClassName="";
    private ArrayList<ConstValue> operList;
    private StringList folder=new StringList();
    private String folderName="";
    public ServerPanel() {
        initComponents();
        EntityNames.removeAll();
        classes = Values.EntityFactory.classList();
        for(TableItem ss : classes){
            EntityNames.add(ss.name+" ("+ss.clazz.getSimpleName()+")");
            }
        for(int i=0;i<=10;i++)
            Level.add(""+i);
        Mode.add("Актуальные");
        Mode.add("Все");
        Mode.add("Удаленные");
        TestNumber.add("200 Нормальное завершение");
        TestNumber.add("500 NullPointerException");
        TestNumber.add("404 Not Found");
        TestNumber.add("400 Request error - имя параметра");
        TestNumber.add("400 Request error - значение параметра");
        }
    private void setMonitorState(boolean state){
        Monitor.setSelected(state);
        isShown=state;
        if (state)
            stateLoopThread.interrupt();
        }
    private Runnable stateLoop = new Runnable() {
        @Override
        public void run() {
            while (!isDown){
                if (isShown && onFront)
                    java.awt.EventQueue.invokeLater(new Runnable() {
                        public void run() {
                            refresh();
                        }
                    });
                try {
                    Thread.sleep(1000*StateLoopDelay);
                    } catch (InterruptedException e) {}
            }
        }
    };
    public void initPanel(Client main0){
        super.initPanel(main0);
        client = main0;
        operList = main.constList.getValuesList("DBOperation");
        Operation.removeAll();
        for(ConstValue ss:operList)
            Operation.add(ss.title());
        ArtifactTypes.removeAll();
        for(String ss : Values.ArtifactTypeNames)
            ArtifactTypes.add(ss);
        ServerIPPort.setText(""+client.getServerIP()+" : "+client.getServerPort());
        //----------------------------------------------------------------------------------------------
        bugPanel = new EntityPanel(20,570,bugList,"BugMessage",main,true,false){
            @Override
            public Response apiFunGetAll() throws IOException {
                return  main.service.getBugList(main.debugToken, Values.GetAllModeActual).execute();
            }
            @Override
            public Response apiFunGetById() throws IOException {
                return  main.service.getBug(main.debugToken,data.get(listBox().getSelectedIndex()).getOid()).execute();
            }
            @Override
            public Response apiFunAdd() throws IOException {
                return null;
            }
            @Override
            public Response apiFunUpdate() throws IOException {
                return null;
            }
            @Override
            public void showRecord() {
                System.out.println("-------------------------------------------------------"+current.toString());
            }
            @Override
            public void updateRecord() {}
            };
        add(bugPanel);
        stateLoopThread = new Thread(stateLoop);
        stateLoopThread.start();
        }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jCheckBox1 = new javax.swing.JCheckBox();
        Import = new javax.swing.JButton();
        ServerLog = new javax.swing.JButton();
        Reload = new javax.swing.JButton();
        Export = new javax.swing.JButton();
        TargetDB = new javax.swing.JButton();
        UploadServer = new javax.swing.JButton();
        UploadMK = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        LogSize = new javax.swing.JTextField();
        Shutdown = new javax.swing.JButton();
        ClearTable1 = new javax.swing.JButton();
        ClearDB = new javax.swing.JButton();
        EntityNames = new java.awt.Choice();
        Password = new javax.swing.JPasswordField();
        jLabel3 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        TCount = new javax.swing.JTextField();
        TMin = new javax.swing.JTextField();
        TMax = new javax.swing.JTextField();
        TMid = new javax.swing.JTextField();
        jSeparator2 = new javax.swing.JSeparator();
        PID = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        Level = new java.awt.Choice();
        Mode = new java.awt.Choice();
        jLabel11 = new javax.swing.JLabel();
        RefreshEntityList = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        ReleaseNum = new javax.swing.JTextField();
        FTP = new javax.swing.JCheckBox();
        Execute = new javax.swing.JButton();
        CommandLine = new javax.swing.JTextField();
        TestNumber = new java.awt.Choice();
        OperationButton = new javax.swing.JButton();
        TestCall = new javax.swing.JButton();
        Operation = new java.awt.Choice();
        SeccionCount = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        ServerLocked = new javax.swing.JCheckBox();
        RequestCount = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        LockServer = new javax.swing.JButton();
        CashMode = new javax.swing.JCheckBox();
        TotalCount = new javax.swing.JTextField();
        CashPercent = new javax.swing.JLabel();
        CashCount = new javax.swing.JTextField();
        Monitor = new javax.swing.JCheckBox();
        ExportAtrifacts = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JSeparator();
        Record = new java.awt.Choice();
        RemoveRecord = new javax.swing.JButton();
        EditRecord = new javax.swing.JButton();
        ServerIPPort = new javax.swing.JLabel();
        LogFileReopen = new javax.swing.JButton();
        LogFolder = new javax.swing.JButton();
        ArtifactFolder = new javax.swing.JButton();
        ArtifactTypes = new java.awt.Choice();
        FolderList = new java.awt.Choice();
        DownLoadFile = new javax.swing.JButton();
        ImportArtifact = new javax.swing.JButton();
        XLSX = new javax.swing.JCheckBox();
        BlockSize = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        MB = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();

        jButton1.setText("jButton1");

        jButton2.setText("jButton2");

        jCheckBox1.setText("jCheckBox1");

        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentHidden(java.awt.event.ComponentEvent evt) {
                formComponentHidden(evt);
            }
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });
        setLayout(null);

        Import.setText("Импорт БД");
        Import.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ImportActionPerformed(evt);
            }
        });
        add(Import);
        Import.setBounds(20, 460, 120, 23);

        ServerLog.setText("Лог сервера");
        ServerLog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ServerLogActionPerformed(evt);
            }
        });
        add(ServerLog);
        ServerLog.setBounds(20, 110, 120, 23);

        Reload.setText("Рестарт");
        Reload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ReloadActionPerformed(evt);
            }
        });
        add(Reload);
        Reload.setBounds(20, 340, 120, 23);

        Export.setText("Экспорт БД ");
        Export.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ExportActionPerformed(evt);
            }
        });
        add(Export);
        Export.setBounds(20, 140, 120, 23);

        TargetDB.setText("Тестовая БД");
        TargetDB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TargetDBActionPerformed(evt);
            }
        });
        add(TargetDB);
        TargetDB.setBounds(20, 430, 120, 23);

        UploadServer.setText("Обновить сервер");
        UploadServer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UploadServerActionPerformed(evt);
            }
        });
        add(UploadServer);
        UploadServer.setBounds(20, 260, 120, 23);

        UploadMK.setText("Обновить МК");
        UploadMK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UploadMKActionPerformed(evt);
            }
        });
        add(UploadMK);
        UploadMK.setBounds(20, 310, 120, 23);

        jLabel1.setText("Память (Мб)");
        add(jLabel1);
        jLabel1.setBounds(60, 290, 80, 14);

        LogSize.setText("50");
        add(LogSize);
        LogSize.setBounds(150, 110, 40, 25);

        Shutdown.setText("Завершение");
        Shutdown.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ShutdownActionPerformed(evt);
            }
        });
        add(Shutdown);
        Shutdown.setBounds(20, 370, 120, 23);

        ClearTable1.setText("Очистить");
        ClearTable1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ClearTable1ActionPerformed(evt);
            }
        });
        add(ClearTable1);
        ClearTable1.setBounds(500, 400, 120, 23);

        ClearDB.setText("Очистка БД");
        ClearDB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ClearDBActionPerformed(evt);
            }
        });
        add(ClearDB);
        ClearDB.setBounds(20, 400, 120, 23);
        add(EntityNames);
        EntityNames.setBounds(220, 400, 260, 30);

        Password.setText("pi31415926");
        add(Password);
        Password.setBounds(20, 230, 120, 25);

        jLabel3.setText("Записей в блоке");
        add(jLabel3);
        jLabel3.setBounds(20, 170, 120, 14);
        add(jSeparator1);
        jSeparator1.setBounds(10, 247, 230, 0);

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel4.setText("Задержки (мс)");
        add(jLabel4);
        jLabel4.setBounds(260, 40, 130, 14);

        jLabel5.setText("PID");
        add(jLabel5);
        jLabel5.setBounds(270, 205, 60, 14);

        jLabel6.setText("Мин.");
        add(jLabel6);
        jLabel6.setBounds(270, 75, 70, 14);

        jLabel7.setText("Макс.");
        add(jLabel7);
        jLabel7.setBounds(270, 105, 70, 14);

        jLabel8.setText("Среднее");
        add(jLabel8);
        jLabel8.setBounds(270, 135, 70, 14);

        TCount.setEnabled(false);
        add(TCount);
        TCount.setBounds(330, 160, 70, 25);

        TMin.setEnabled(false);
        add(TMin);
        TMin.setBounds(330, 70, 70, 25);

        TMax.setEnabled(false);
        add(TMax);
        TMax.setBounds(330, 100, 70, 25);

        TMid.setEnabled(false);
        add(TMid);
        TMid.setBounds(330, 130, 70, 25);
        add(jSeparator2);
        jSeparator2.setBounds(270, 190, 140, 2);

        PID.setEnabled(false);
        add(PID);
        PID.setBounds(330, 200, 70, 25);

        jLabel9.setText("Кол-во");
        add(jLabel9);
        jLabel9.setBounds(270, 165, 60, 14);

        jLabel10.setText("Таблица");
        add(jLabel10);
        jLabel10.setBounds(160, 405, 60, 14);
        add(Level);
        Level.setBounds(220, 435, 60, 20);
        add(Mode);
        Mode.setBounds(300, 435, 130, 20);

        jLabel11.setText("Уровень");
        add(jLabel11);
        jLabel11.setBounds(160, 435, 60, 14);

        RefreshEntityList.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/refresh.png"))); // NOI18N
        RefreshEntityList.setBorderPainted(false);
        RefreshEntityList.setContentAreaFilled(false);
        RefreshEntityList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RefreshEntityListActionPerformed(evt);
            }
        });
        add(RefreshEntityList);
        RefreshEntityList.setBounds(440, 430, 40, 30);

        jLabel12.setText("Запросов");
        add(jLabel12);
        jLabel12.setBounds(270, 295, 60, 14);

        ReleaseNum.setEnabled(false);
        add(ReleaseNum);
        ReleaseNum.setBounds(330, 230, 70, 25);

        FTP.setText("через TCP");
        add(FTP);
        FTP.setBounds(150, 260, 90, 23);

        Execute.setText("Ком.строка");
        Execute.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ExecuteActionPerformed(evt);
            }
        });
        add(Execute);
        Execute.setBounds(20, 520, 120, 23);

        CommandLine.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                CommandLineKeyPressed(evt);
            }
        });
        add(CommandLine);
        CommandLine.setBounds(160, 490, 460, 25);
        add(TestNumber);
        TestNumber.setBounds(160, 520, 290, 20);

        OperationButton.setText("Операция");
        OperationButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OperationButtonActionPerformed(evt);
            }
        });
        add(OperationButton);
        OperationButton.setBounds(500, 370, 120, 23);

        TestCall.setText("Тест");
        TestCall.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TestCallActionPerformed(evt);
            }
        });
        add(TestCall);
        TestCall.setBounds(460, 520, 60, 23);
        add(Operation);
        Operation.setBounds(220, 370, 260, 20);

        SeccionCount.setEnabled(false);
        add(SeccionCount);
        SeccionCount.setBounds(330, 260, 70, 25);

        jLabel13.setText("Сборка");
        add(jLabel13);
        jLabel13.setBounds(270, 235, 60, 14);

        ServerLocked.setText("Блокировка");
        ServerLocked.setEnabled(false);
        add(ServerLocked);
        ServerLocked.setBounds(150, 340, 90, 23);

        RequestCount.setEnabled(false);
        add(RequestCount);
        RequestCount.setBounds(330, 290, 70, 25);

        jLabel14.setText("Сессий");
        add(jLabel14);
        jLabel14.setBounds(270, 265, 60, 14);

        LockServer.setText("<-");
        LockServer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LockServerActionPerformed(evt);
            }
        });
        add(LockServer);
        LockServer.setBounds(270, 340, 50, 23);

        CashMode.setText("Кэширование сервера");
        CashMode.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                CashModeItemStateChanged(evt);
            }
        });
        add(CashMode);
        CashMode.setBounds(410, 70, 160, 23);

        TotalCount.setEditable(false);
        TotalCount.setEnabled(false);
        add(TotalCount);
        TotalCount.setBounds(410, 130, 60, 25);

        CashPercent.setText("% кэширования");
        add(CashPercent);
        CashPercent.setBounds(480, 110, 90, 14);

        CashCount.setEditable(false);
        CashCount.setEnabled(false);
        add(CashCount);
        CashCount.setBounds(410, 100, 60, 25);

        Monitor.setText("Мониторинг состояния");
        Monitor.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                MonitorItemStateChanged(evt);
            }
        });
        add(Monitor);
        Monitor.setBounds(20, 40, 190, 23);

        ExportAtrifacts.setText("Экспорт файлов");
        ExportAtrifacts.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ExportAtrifactsActionPerformed(evt);
            }
        });
        add(ExportAtrifacts);
        ExportAtrifacts.setBounds(20, 190, 120, 23);
        add(jSeparator3);
        jSeparator3.setBounds(10, 220, 240, 10);
        add(Record);
        Record.setBounds(160, 465, 460, 20);

        RemoveRecord.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/remove.png"))); // NOI18N
        RemoveRecord.setBorderPainted(false);
        RemoveRecord.setContentAreaFilled(false);
        RemoveRecord.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RemoveRecordActionPerformed(evt);
            }
        });
        add(RemoveRecord);
        RemoveRecord.setBounds(660, 465, 30, 30);

        EditRecord.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/no_problem.png"))); // NOI18N
        EditRecord.setBorderPainted(false);
        EditRecord.setContentAreaFilled(false);
        EditRecord.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EditRecordActionPerformed(evt);
            }
        });
        add(EditRecord);
        EditRecord.setBounds(620, 460, 40, 40);

        ServerIPPort.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        ServerIPPort.setText("...");
        add(ServerIPPort);
        ServerIPPort.setBounds(30, 10, 320, 20);

        LogFileReopen.setText("Обновить log");
        LogFileReopen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LogFileReopenActionPerformed(evt);
            }
        });
        add(LogFileReopen);
        LogFileReopen.setBounds(410, 230, 120, 23);

        LogFolder.setText("Список логов");
        LogFolder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LogFolderActionPerformed(evt);
            }
        });
        add(LogFolder);
        LogFolder.setBounds(410, 260, 120, 23);

        ArtifactFolder.setText("Список файлов");
        ArtifactFolder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ArtifactFolderActionPerformed(evt);
            }
        });
        add(ArtifactFolder);
        ArtifactFolder.setBounds(410, 290, 120, 23);
        add(ArtifactTypes);
        ArtifactTypes.setBounds(540, 290, 110, 20);
        add(FolderList);
        FolderList.setBounds(330, 340, 360, 20);

        DownLoadFile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/download.png"))); // NOI18N
        DownLoadFile.setBorderPainted(false);
        DownLoadFile.setContentAreaFilled(false);
        DownLoadFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DownLoadFileActionPerformed(evt);
            }
        });
        add(DownLoadFile);
        DownLoadFile.setBounds(650, 280, 40, 30);

        ImportArtifact.setText("Импорт файлов");
        ImportArtifact.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ImportArtifactActionPerformed(evt);
            }
        });
        add(ImportArtifact);
        ImportArtifact.setBounds(20, 490, 120, 23);

        XLSX.setText("xlsx");
        add(XLSX);
        XLSX.setBounds(150, 140, 45, 23);

        BlockSize.setText("0");
        add(BlockSize);
        BlockSize.setBounds(150, 170, 60, 25);

        jLabel15.setText("строк");
        add(jLabel15);
        jLabel15.setBounds(200, 110, 50, 14);

        MB.setText("0");
        add(MB);
        MB.setBounds(150, 290, 60, 25);

        jLabel2.setText("пароль операции ");
        add(jLabel2);
        jLabel2.setBounds(150, 230, 110, 14);
    }// </editor-fold>//GEN-END:initComponents

    private void ServerLogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ServerLogActionPerformed
        final int logSize = Integer.parseInt(LogSize.getText());
        new APICall<StringList>(main){
            @Override
            public Call<StringList> apiFun() {
                return main.service.getConsoleLog(main.debugToken,logSize);
                }
            @Override
            public void onSucess(StringList oo) {
                String ss = "лог сервера ="+logSize+" ["+oo.size()+"]-------------------------------------------\n";
                System.out.println(ss);
                for(String zz : oo)
                    System.out.println(zz);
                }
            };
    }//GEN-LAST:event_ServerLogActionPerformed

    private void ReloadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ReloadActionPerformed
        new OK(200, 200, "Рестарт сервера", new I_Button() {
            @Override
            public void onPush() {
                setMonitorState(false);
                restartServer();
            }
        });
    }//GEN-LAST:event_ReloadActionPerformed

    private void ExportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ExportActionPerformed
        new APICall<Artifact>(main){
            @Override
            public Call<Artifact> apiFun() {
                return main.service.exportDBxlsx(main.debugToken,XLSX.isSelected(),Integer.parseInt(BlockSize.getText()));
                }
            @Override
            public void onSucess(Artifact oo) {
                main.loadFileAndDelete(oo);
                }
            };
    }//GEN-LAST:event_ExportActionPerformed

    private void restartServer(){
        new APICall<JEmpty>(main){
            @Override
            public Call<JEmpty> apiFun() {
                return main.service.rebootServer(main.debugToken,Password.getText());
            }
            @Override
            public void onSucess(JEmpty oo) {
                client.logOff();
                }
            };
        }
    private void shutdownServer(){
        new APICall<JString>(main){
            @Override
            public Call<JString> apiFun() {
                return main.service.shutdown(main.debugToken,Password.getText());
                }
            @Override
            public void onSucess(JString oo) {
                client.logOff();
                }
            };
        }
    
    private void ImportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ImportActionPerformed
        FileNameExt fname = main.getInputFileName("Импорт БД","mongo*.xls*",null);
        final MultipartBody.Part body = RestAPICommon.createMultipartBody(fname);
        new APICall<Artifact>(main){
            @Override
            public Call<Artifact> apiFun() {
                return main.service.upload(main.debugToken,"DB-import",fname.fileName(),body);
                }
            @Override
            public void onSucess(final Artifact oo) {
                new APICall<JString>(main){
                    @Override
                    public Call<JString> apiFun() {
                        return main.service.importDBxls(main.debugToken,Password.getText(),oo.getOid());
                        }
                    @Override
                    public void onSucess(JString oo) {
                        setMonitorState(false);
                        System.out.print(oo);
                        System.out.println("Рестарт сервера после импорта");
                        restartServer();
                        }
                    };
                }
            };
    }//GEN-LAST:event_ImportActionPerformed

    private void ShutdownActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ShutdownActionPerformed
        new OK(200, 200, "Завершение работы сервера", new I_Button() {
            @Override
            public void onPush() {
                setMonitorState(false);
                shutdownServer();
            }
        });
    }//GEN-LAST:event_ShutdownActionPerformed

    private void ClearTable1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ClearTable1ActionPerformed
        String name = classes.get(EntityNames.getSelectedIndex()).name;
        new OK(200, 200, "Очистить  таблицу "+name, new I_Button() {
            @Override
            public void onPush() {
                new APICall<JString>(main){
                    @Override
                    public Call<JString> apiFun() {
                        return main.service.clearTable(main.debugToken,name,Password.getText());
                    }
                    @Override
                    public void onSucess(JString oo) {
                        System.out.println(oo.getValue());
                    }
                };
            }
        });
    }//GEN-LAST:event_ClearTable1ActionPerformed

    private void uploadMKByAPI(){
        FileNameExt fname = main.getInputFileName("Выгрузить МК", ValuesBase.env().apkName(),null);
        final MultipartBody.Part body2 = RestAPICommon.createMultipartBody(fname);
        new APICall<JEmpty>(main){
            @Override
            public Call<JEmpty> apiFun() {
                return main.service.uploadByName(main.debugToken,ValuesBase.env().apkName(),body2,false);
            }
            @Override
            public void onSucess(JEmpty oo) {
                System.out.println("Файл выгружен");
            }
        };
        }

    private void UploadMKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UploadMKActionPerformed
        new OK(200, 200, "Выгрузить МК", new I_Button() {
            @Override
            public void onPush() {
                if (FTP.isSelected())
                    uploadMKByFTP();
                else
                    uploadMKByAPI();
            }
        });
    }//GEN-LAST:event_UploadMKActionPerformed

    private void deploy(){
        new APICall<JString>(main){
            @Override
            public Call<JString> apiFun() {
                return main.service.deployMB(main.debugToken,Password.getText(),Integer.parseInt(MB.getText()));
            }
            @Override
            public void onSucess(JString oo) {
                System.out.println(oo.getValue());
                main.delayInGUI(3,new Runnable(){
                    @Override
                    public void run() {
                        client.logOff();
                        }
                    });
                }
            };
        }

    private void uploadMKByFTP(){
        FileNameExt fname = main.getInputFileName("Выгрузить МК",ValuesBase.env().apkName(),null);
        new ClientFileWriter(fname.fullName(), ValuesBase.env().apkName(), client.getServerIP(), Integer.parseInt(client.getServerPort()), Password.getText(), new AsyncTaskBack() {
            @Override
            public void runInGUI(Runnable run) {
                java.awt.EventQueue.invokeLater(run);
            }
            @Override
            public void onError(String mes) {
                System.out.println(mes);
            }
            @Override
            public void onMessage(String mes) {
                System.out.println(mes);
            }
            @Override
            public void onFinish(boolean result) {
                System.out.println("Файл " +ValuesBase.env().apkName()+ " выгружен");
                }
            });
        }

    private void uploadServerByAPI(){
        FileNameExt fname = main.getInputFileName("Выгрузить сервер",ValuesBase.env().serverName(),null);
        final MultipartBody.Part body2 = RestAPICommon.createMultipartBody(fname);
        new APICall<JEmpty>(main){
            @Override
            public Call<JEmpty> apiFun() {
                return main.service.uploadByName(main.debugToken,ValuesBase.env().serverName(),body2,false);
            }
            @Override
            public void onSucess(JEmpty oo) {
                System.out.println("Файл " +ValuesBase.env().serverName()+ " выгружен");
                deploy();
                }
            };
        }

    private void uploadServerByFTP(){
        FileNameExt fname = main.getInputFileName("Выгрузить сервер",ValuesBase.env().serverName(),null);
        new ClientFileWriter(fname.fullName(), ValuesBase.env().serverName(), client.getServerIP(), Integer.parseInt(client.getServerPort()), Password.getText(), new AsyncTaskBack() {
            @Override
            public void runInGUI(Runnable run) {
                java.awt.EventQueue.invokeLater(run);
                }
            @Override
            public void onError(String mes) {
                System.out.println(mes);
                }
            @Override
            public void onMessage(String mes) {
                System.out.println(mes);
                }
            @Override
            public void onFinish(boolean result) {
                System.out.println("Файл " +ValuesBase.env().serverName()+ " выгружен");
                deploy();
                }
            });
        }

    private void UploadServerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UploadServerActionPerformed
        new OK(200, 200, "Выгрузить сервер", new I_Button() {
            @Override
            public void onPush() {
                setMonitorState(false);
                if (FTP.isSelected())
                    uploadServerByFTP();
                else
                    uploadServerByAPI();

            }
        });
    }//GEN-LAST:event_UploadServerActionPerformed

    public void refreshEntityList(){
        entityClassName = classes.get(EntityNames.getSelectedIndex()).clazz.getSimpleName();
        records = main.getList(entityClassName,Mode.getSelectedIndex(),Level.getSelectedIndex());
        Record.removeAll();
        records.sortById();
        for(Entity ent : records){
            String title="";
            try {
                title = ent.getTitle();
                } catch (Exception ee){ }
            Record.add("[" + ent.getOid() + "] " + title);
            }
        }
    
    private void RefreshEntityListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RefreshEntityListActionPerformed
        refreshEntityList();
    }//GEN-LAST:event_RefreshEntityListActionPerformed

    private void ClearDBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ClearDBActionPerformed
        new OK(200, 200, "Очистить БД", new I_Button() {
            @Override
            public void onPush() {
                new APICall<JString>(main){
                    @Override
                    public Call<JString> apiFun() {
                        return main.service.clearDB(main.debugToken,Password.getText());
                        }
                    @Override
                    public void onSucess(JString oo) {
                        System.out.println(oo.getValue());
                        main.delayInGUI(3,new Runnable(){
                            @Override
                            public void run() {
                                client.logOff();
                            }
                        });
                    }
                };

            }
        });
    }//GEN-LAST:event_ClearDBActionPerformed

    private void TargetDBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TargetDBActionPerformed
        new OK(200, 200, "Тестовая БД", new I_Button() {
            @Override
            public void onPush() {
                new DBExample().createAll(main.service,Password.getText());
                client.logOff();
                }
            });
    }//GEN-LAST:event_TargetDBActionPerformed
    private void execute(){
        new OK(200, 200, "Командная строка", new I_Button() {
            @Override
            public void onPush() {
                new APICall<JString>(main){
                    @Override
                    public Call<JString> apiFun() {
                        return main.service.execute(main.debugToken,Password.getText(),CommandLine.getText());
                        }
                    @Override
                    public void onSucess(JString oo) {
                        System.out.println(oo.getValue());
                    }
                };
            }
        });
    }
    private void ExecuteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ExecuteActionPerformed
        execute();
    }//GEN-LAST:event_ExecuteActionPerformed

    private void CommandLineKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_CommandLineKeyPressed
        if(evt.getKeyCode()!=10) return;
        execute();
    }//GEN-LAST:event_CommandLineKeyPressed

    private void OperationButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OperationButtonActionPerformed
        new OK(200, 200, "Операция над БД: "+Operation.getSelectedItem(), new I_Button() {
            @Override
            public void onPush() {
                new APICall<JString>(main){
                    @Override
                    public Call<JString> apiFun() {
                        int value = operList.get(Operation.getSelectedIndex()).value();
                        return main.service.prepareDB(main.debugToken,value,Password.getText());
                        }
                    @Override
                    public void onSucess(JString oo) {
                        System.out.println(oo.getValue());
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                int count = Values.BackgroundOperationMaxDelay/Values.HTTPTimeOut*2;
                                longPolling(count);
                                }
                            }).start();
                        }
                    };
                }
            });
    }//GEN-LAST:event_OperationButtonActionPerformed

    private void TestCallActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TestCallActionPerformed
                new APICall<JString>(main){
                    @Override
                    public Call<JString> apiFun() {
                        return main.service.testCall(main.debugToken,TestNumber.getSelectedIndex(),"abcd");
                        }
                    @Override
                    public void onSucess(JString oo) {
                        System.out.println(oo.getValue());
                        }
                    };

    }//GEN-LAST:event_TestCallActionPerformed


    private void longPolling(final int count){
        new APICall<JString>(main){
            @Override
            public Call<JString> apiFun() {
                return main.service.longPolling(main.debugToken,Password.getText());
            }
            @Override
            public void onSucess(JString oo) {
                if (oo.getValue().length()!=0 || count<=0)
                    System.out.println("Отложенный ответ:\n"+oo);
                else{
                    System.out.println("...");
                    longPolling(count-1);
                    }
            }
        };
    }
    private void LockServerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LockServerActionPerformed
        boolean locked = serverState.isLocked();
        new OK(200, 200, (locked ? "Деб" : "Б")+"локировать сервер", new I_Button() {
            @Override
            public void onPush() {
                new APICall<JEmpty>(main){
                    @Override
                    public Call<JEmpty> apiFun() {
                        return main.service.lock(main.debugToken,Password.getText(),!locked);
                    }
                    @Override
                    public void onSucess(JEmpty oo) {
                        refresh();
                    }
                };
            }
        });
    }//GEN-LAST:event_LockServerActionPerformed

    private void CashModeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_CashModeItemStateChanged
        if (onBusy) return;
        new APICall<JEmpty>(main){
            @Override
            public Call<JEmpty> apiFun() {
                return main.service.setCashMode(main.debugToken,CashMode.isSelected(),Password.getText());
                }
            @Override
            public void onSucess(JEmpty oo) {}
        };
    }//GEN-LAST:event_CashModeItemStateChanged

    private void formComponentHidden(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentHidden
        onFront=false;
    }//GEN-LAST:event_formComponentHidden

    private void formComponentShown(java.awt.event.ComponentEvent evt) {                                    
        onFront=true;
    }                                  

    private void MonitorItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_MonitorItemStateChanged
        setMonitorState(Monitor.isSelected());
    }//GEN-LAST:event_MonitorItemStateChanged

    private EntityList<Artifact> loadList = new EntityList<>();
    private String loadDirectory="";
    private int cntTotal=0;
    private int cntLost=0;
    private int cntError=0;
    private int cntSuccess=0;
    private int cntPresent=0;
    private int loadIdx=0;
    private void loadOne(){
        while(true){
            if (loadIdx==cntTotal){
                System.out.println("Всего "+cntTotal+"\nНет на сервере "+cntLost+"\nОшибка загрузки "+cntError+"\nЗагружен ранее "+cntPresent+"\nЗагружено "+cntSuccess);
                return;
                }
            Artifact art = loadList.get(loadIdx);
            String fname = art.createArtifactServerPath();
            if (art.isFileLost()){
                System.out.println("Файл на сервере отсутствует "+ fname);
                cntLost++;
                loadIdx++;
                continue;
                }
            String path = loadDirectory+"/"+art.type()+"_"+art.directoryName();
            String fullName = loadDirectory+"/"+fname;
            File dir = new File(path);
            if (!dir.exists()){
                if (!dir.mkdir()){
                    System.out.println("Не удается создать каталог "+ path);
                    cntError++;
                    loadIdx++;
                    continue;
                    }
                }
            if (!dir.isDirectory()){
                System.out.println("Это не  каталог "+ path);
                cntError++;
                loadIdx++;
                continue;
                }
            File file = new File(fullName);
            if (file.exists()){
                if (file.length()==art.getFileSize()){
                    //System.out.println("Файл уже скачан "+ fname);
                    cntPresent++;
                    loadIdx++;
                    continue;
                    }
                else
                    System.out.println("Не совпадает размер, загрузка "+ fname+" "+file.length()+"/"+art.getFileSize());
                }
            main.loadFile(art, fullName, new I_DownLoad() {
                @Override
                public void onSuccess() {
                    System.out.println("Файл загружен "+ fname);
                    cntSuccess++;
                    loadIdx++;
                    loadOne();
                    }
                @Override
                public void onError(String mes) {
                    System.out.println("Ошибка загрузки "+ fname+"\n"+mes);
                    cntError++;
                    loadIdx++;
                    loadOne();
                    }
                });
            return;
            }
        }
    //--------------------------------------------------------------------------------------------
    private boolean uploadByName(String clientName, String ext, String serverName){
        try {
            MultipartBody.Part body2 = RestAPICommon.createMultipartBody(clientName,ext);
            Response<JEmpty> call3 = main.service.uploadByName(main.debugToken,serverName,body2,false).execute();
            if (!call3.isSuccessful()){
                 System.out.println("Ошибка выгрузки файла  "+ Utils.httpError(call3));
                 return false;
                 }
            System.out.println("Файл выгружен: "+serverName);
            return true;
            } catch (Exception e) {
                System.out.println("Ошибка сервера: "+e.toString());
                return false;
                }
        }
    private void updateArtifactField(String fld, DBRequest request){
        new APICall<JEmpty>(main){
            @Override
            public Call<JEmpty> apiFun() {
                return main.service.updateEntityField(main.debugToken,fld,request);
                }
            @Override
            public void onSucess(JEmpty oo) {}
            };
        }
    private void saveOne(){
        while(true){
            if (loadIdx==cntTotal){
                System.out.println("Всего "+cntTotal+"\nНет на клиенте "+cntLost+"\nОшибка выгрузки "+cntError+"\nЕсть на сервере "+cntPresent+"\nВыгружено "+cntSuccess);
                return;
                }
            Artifact art = loadList.get(loadIdx);
            String fname = art.createArtifactServerPath();
            if (!art.isFileLost()){
                cntPresent++;
                loadIdx++;
                continue;
                }
            String path = loadDirectory+"/"+art.type()+"_"+art.directoryName();
            String fullName = loadDirectory+fname;
            fullName = fullName.replace("\\","/");
            File dir = new File(path);
            if (!dir.exists()){
                cntLost++;
                loadIdx++;
                continue;
                }
            if (!dir.isDirectory()) {
                cntLost++;
                loadIdx++;
                continue;
                }
            File file = new File(fullName);
            if (!file.exists()){
                System.out.println("Нет на клиенте "+ fname+" "+file.length()+"/"+art.getFileSize());
                loadIdx++;
                cntLost++;
                continue;
                }
            if (uploadByName(fullName,art.getOriginalExt(),fname)){
                cntSuccess++;
                art.setFileLost(false);
                art.setFileSize(file.length());
                DBRequest request = new DBRequest(art,main.gson);
                updateArtifactField("fileLost",request);
                updateArtifactField("fileSize",request);
                }
            else
                cntError++;
            loadIdx++;
            }
    }
    private void ExportAtrifactsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ExportAtrifactsActionPerformed
        new OK(200, 200, "Экспорт артефактов", new I_Button() {
            @Override
            public void onPush() {
                FileNameExt ff = main.getOutputFileName("Каталог экпорта","aaa","aaa");
                loadDirectory = ff.getPath();
                new APICall<ArrayList<DBRequest>>(main){
                    @Override
                    public Call<ArrayList<DBRequest>> apiFun() {
                        return main.service.getEntityList(main.debugToken,"Artifact",Values.GetAllModeActual,0);
                        }
                    @Override
                    public void onSucess(ArrayList<DBRequest> oo) {
                        try {
                            loadList.load(oo);
                            cntTotal = loadList.size();
                            cntError = cntSuccess = cntLost = cntPresent = 0;
                            loadIdx=0;
                            loadOne();
                            } catch (UniException e) {  Utils.printFatalMessage(e); }
                        }
                    };
            }
        });
    }//GEN-LAST:event_ExportAtrifactsActionPerformed

    private void RemoveRecordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RemoveRecordActionPerformed
        if (records.size()==0)
            return;
        final Entity entity = records.get(Record.getSelectedIndex());
        new OK(200, 200, "Удалить: [" + entity.getOid() + "] " + entity.getTitle(), new I_Button() {
            @Override
            public void onPush() {
                new APICall<JBoolean>(main){
                    @Override
                    public Call<JBoolean> apiFun() {
                        return main.service.removeEntity(main.debugToken,entityClassName,entity.getOid());
                        }
                    @Override
                    public void onSucess(JBoolean oo) {
                        refreshEntityList();
                    }
                };
            }
        });
    }//GEN-LAST:event_RemoveRecordActionPerformed

    private void EditRecordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EditRecordActionPerformed
        if (records.size()==0)
            return;
        final Entity entity = records.get(Record.getSelectedIndex());
        new EntityEditPanel(main,entity);
    }//GEN-LAST:event_EditRecordActionPerformed

    private void LogFileReopenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LogFileReopenActionPerformed
        new OK(200, 200, "Создать новый log-файл", new I_Button() {
            @Override
            public void onPush() {
                new APICall<JEmpty>(main){
                    @Override
                    public Call<JEmpty> apiFun() {
                        return main.service.reopenLogFile(main.debugToken,Password.getText());
                    }
                    @Override
                    public void onSucess(JEmpty dd) {
                    }
                };
            }
        });
    }//GEN-LAST:event_LogFileReopenActionPerformed


    private void loadFileList(final String dir){
        new APICall<StringList>(main){
            @Override
            public Call<StringList> apiFun() {
                return main.service.getFolder(main.debugToken,Password.getText(),dir);
            }
            @Override
            public void onSucess(StringList oo) {
                folder = oo;
                folderName = dir;
                FolderList.removeAll();
                System.out.println(dir+": файлов: "+oo.size());
                for(String ss : oo){
                    FolderList.add(ss);
                    //System.out.println(ss);
                    }
            }
        };
        }

    private void LogFolderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LogFolderActionPerformed
        loadFileList("log");
    }//GEN-LAST:event_LogFolderActionPerformed

    private void ArtifactFolderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ArtifactFolderActionPerformed
        if (ArtifactTypes.getSelectedIndex()==0)
            return;
        int idx = ArtifactTypes.getSelectedIndex();
        String zz = ""+idx+"_"+Values.ArtifactDirNames[idx];
        loadFileList(zz);
    }//GEN-LAST:event_ArtifactFolderActionPerformed

    private void DownLoadFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DownLoadFileActionPerformed
        if (folder.size()==0)
            return;
        String fname = folder.get(FolderList.getSelectedIndex());
        main.loadFile(folderName,fname);
    }//GEN-LAST:event_DownLoadFileActionPerformed

    private void ImportArtifactActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ImportArtifactActionPerformed
        new OK(200, 200, "Импорт артефактов", new I_Button() {
            @Override
            public void onPush() {
                FileNameExt ff = main.getOutputFileName("Каталог импорта","aaa","aaa");
                loadDirectory = ff.getPath();
                new APICall<ArrayList<DBRequest>>(main){
                    @Override
                    public Call<ArrayList<DBRequest>> apiFun() {
                        return main.service.getEntityList(main.debugToken,"Artifact",Values.GetAllModeActual,0);
                    }
                    @Override
                    public void onSucess(ArrayList<DBRequest> oo) {
                        try {
                            loadList.load(oo);
                            cntTotal = loadList.size();
                            cntError = cntSuccess = cntLost = cntPresent = 0;
                            loadIdx=0;
                            saveOne();
                        } catch (UniException e) {  Utils.printFatalMessage(e); }
                    }
                };
            }
        });
    }//GEN-LAST:event_ImportArtifactActionPerformed

    private void showState(){
        onBusy=true;
        TMid.setText(""+serverState.getTimeMiddle());
        TMin.setText(""+serverState.getTimeMin());
        TMax.setText(""+serverState.getTimeMax());
        TCount.setText(""+serverState.getTimeCount());
        PID.setText(""+serverState.getPID());
        ReleaseNum.setText(""+serverState.getReleaseNumber());
        SeccionCount.setText(""+serverState.getSessionCount());
        ServerLocked.setSelected(serverState.isLocked());
        RequestCount.setText(""+serverState.getRequestNum());
        CashMode.setSelected(serverState.isСashEnabled());
        if (!serverState.isСashEnabled() || serverState.getTotalGetCount()==0) {
            TotalCount.setText("");
            CashCount.setText("");
            CashPercent.setText("");
            }
        else {
            TotalCount.setText(""+serverState.getTotalGetCount());
            CashCount.setText(""+serverState.getCashGetCount());
            CashPercent.setText("" + serverState.getCashGetCount() * 100 / serverState.getTotalGetCount()+" %");
            }
        onBusy=false;
        }
    @Override
    public void refresh() {
        new APICall<ServerState>(main){
            @Override
            public Call<ServerState> apiFun() {
                return main.service.serverState(main.debugToken);
                }
            @Override
            public void onSucess(ServerState oo) {
                serverState = oo;
                showState();
                }
            };
        }

    @Override
    public void eventPanel(int code, int par1, long par2, String par3) {
        if (code==EventRefreshSettings){
            refresh();
            main.sendEventPanel(EventRefreshSettingsDone,0,0,"");
            }
    }

    @Override
    public void shutDown() {
        isDown=true;
        stateLoopThread.interrupt();
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ArtifactFolder;
    private java.awt.Choice ArtifactTypes;
    private javax.swing.JTextField BlockSize;
    private javax.swing.JTextField CashCount;
    private javax.swing.JCheckBox CashMode;
    private javax.swing.JLabel CashPercent;
    private javax.swing.JButton ClearDB;
    private javax.swing.JButton ClearTable1;
    private javax.swing.JTextField CommandLine;
    private javax.swing.JButton DownLoadFile;
    private javax.swing.JButton EditRecord;
    private java.awt.Choice EntityNames;
    private javax.swing.JButton Execute;
    private javax.swing.JButton Export;
    private javax.swing.JButton ExportAtrifacts;
    private javax.swing.JCheckBox FTP;
    private java.awt.Choice FolderList;
    private javax.swing.JButton Import;
    private javax.swing.JButton ImportArtifact;
    private java.awt.Choice Level;
    private javax.swing.JButton LockServer;
    private javax.swing.JButton LogFileReopen;
    private javax.swing.JButton LogFolder;
    private javax.swing.JTextField LogSize;
    private javax.swing.JTextField MB;
    private java.awt.Choice Mode;
    private javax.swing.JCheckBox Monitor;
    private java.awt.Choice Operation;
    private javax.swing.JButton OperationButton;
    private javax.swing.JTextField PID;
    private javax.swing.JPasswordField Password;
    private java.awt.Choice Record;
    private javax.swing.JButton RefreshEntityList;
    private javax.swing.JTextField ReleaseNum;
    private javax.swing.JButton Reload;
    private javax.swing.JButton RemoveRecord;
    private javax.swing.JTextField RequestCount;
    private javax.swing.JTextField SeccionCount;
    private javax.swing.JLabel ServerIPPort;
    private javax.swing.JCheckBox ServerLocked;
    private javax.swing.JButton ServerLog;
    private javax.swing.JButton Shutdown;
    private javax.swing.JTextField TCount;
    private javax.swing.JTextField TMax;
    private javax.swing.JTextField TMid;
    private javax.swing.JTextField TMin;
    private javax.swing.JButton TargetDB;
    private javax.swing.JButton TestCall;
    private java.awt.Choice TestNumber;
    private javax.swing.JTextField TotalCount;
    private javax.swing.JButton UploadMK;
    private javax.swing.JButton UploadServer;
    private javax.swing.JCheckBox XLSX;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    // End of variables declaration//GEN-END:variables
}
