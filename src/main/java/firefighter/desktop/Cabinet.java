/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package firefighter.desktop;

import com.google.gson.Gson;
import firefighter.core.API.RestAPICommon;
import firefighter.core.DBRequest;
import firefighter.core.ServerState;
import firefighter.core.Utils;
import firefighter.core.constants.Values;
import firefighter.core.entity.Entity;
import firefighter.core.entity.EntityList;
import firefighter.core.entity.artifacts.Artifact;
import firefighter.core.entity.artifacts.ArtifactList;
import firefighter.core.entity.base.BugMessage;
import firefighter.core.entity.base.StringList;
import firefighter.core.entity.baseentityes.JEmpty;
import firefighter.core.entity.notifications.NTMessage;
import firefighter.core.entity.users.Account;
import firefighter.core.entity.users.Person;
import firefighter.core.entity.users.User;
import firefighter.core.export.ExcelX;
import firefighter.core.ftp.AsyncTaskBack;
import firefighter.core.ftp.ClientFileReader;
import firefighter.core.jdbc.JDBCFactory;
import firefighter.core.utils.City;
import firefighter.core.utils.FileNameExt;
import firefighter.core.utils.Street;
import firefighter.dataserver.DBExample;
import firefighter.dataserver.I_DBTarget;
import firefighter.dataserver.I_ServerState;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;


/**
 *
 * @author romanow
 */
public class Cabinet extends MainBaseFrame{
    protected int x00 = 10;
    protected int y00 = 475;
    protected boolean onBusy=false;
    private EntityList<Street> streetList = new EntityList<>();
    private EntityList<City> cityList = new EntityList<>();
    private EntityList<Person> personList = new EntityList<>();
    private ArtifactList artifactList = new ArtifactList();
    private EntityList<BugMessage> bugList = new EntityList<>();
    private EntityList<NTMessage> notifList = new EntityList<>();
    private EntityList<User> userAList = new EntityList<>();
    //-------------------------------------------------------------------------
    public I_DBTarget createDBExample(){ return new DBExample(); }
    public void onServerState(ServerState serverState){}
    public void onDataServerOnOff(){}
    public void onStart(){}
    //-------------------------------------------------------------------------
    public class CabinetEntityPanel {
        private JPanel panel;
        private JButton GetAll = new JButton();
        private JButton GetById = new JButton();
        private JButton DeleteById = new JButton();
        private String name;
        private String className;
        private Gson gson = new Gson();
        protected Choice listBox = new Choice();
        EntityList<Entity> data;
        //------------------- Вложенный класс для панелек
        public void getAllEvent(){
            new APICall<ArrayList<DBRequest>>(Cabinet.this){
                @Override
                public Call<ArrayList<DBRequest>> apiFun() {
                    return service.getEntityList(debugToken,className,Mode.getSelectedIndex(),Level.getSelectedIndex());
                    }
                @Override
                public void onSucess(ArrayList<DBRequest> oo) {
                    try {
                        listBox.removeAll();
                        data.clear();
                        for (DBRequest xx : oo) {
                            Entity ent = xx.get(gson);
                            data.add(ent);
                            listBox.add(ent.getTitle());
                            }
                        System.out.println(data);
                    }catch (Exception ee){ System.out.println(ee.toString()); }
                }
            };
        }
        public String getName() {
            return name; }
        public void setVisible(boolean bb){
            panel.setVisible(bb);
            }
        public CabinetEntityPanel(int x0, int y0, EntityList data0, String name0) {
            data = data0;
            className = name0;
            name = Values.EntityFactory.getEntityNameBySimpleClass(className);
            panel = new JPanel();
            panel.setLayout(null);
            panel.setBounds(x0,y0,410,50);
            panel.add(GetAll);
            panel.add(listBox);
            panel.add(GetById);
            panel.setVisible(false);
            GetAll.setText(name);
            GetAll.setBounds(0, 0, 100, 25);
            listBox.setBounds(0, 30, 270, 20);
            GetById.setBounds(110, 0, 25, 25);
            GetById.setText("?");
            GetAll.setVisible(true);
            listBox.setVisible(true);
            setVisible(false);
            GetById.setVisible(true);
            GetAll.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    getAllEvent();
                }
            });
            GetById.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (listBox.getItemCount() == 0) return;
                    new APICall<DBRequest>(Cabinet.this){
                        @Override
                        public Call<DBRequest> apiFun() {
                            return service.getEntity(debugToken,className,data.get(listBox.getSelectedIndex()).getOid(),Level.getSelectedIndex());
                            }
                        @Override
                        public void onSucess(DBRequest xx) {
                            try {
                                Entity ent = xx.get(gson);
                                System.out.println("id = "+data.get(listBox.getSelectedIndex()).getOid()+ "===========================================\n"+ent+"\n");
                                }catch (Exception ee){ System.out.println(ee.toString()); }
                            }
                        };
                    }
                });
            }
        public void initPanel(){
            getContentPane().add(panel);
            }
        }
    //------------------------------ Конкретные панельки --------------------------------
    private CabinetEntityPanel[] createPanelList() {
        CabinetEntityPanel userPanel = new CabinetEntityPanel(x00, y00, userAList, "User");
        CabinetEntityPanel artPanel = new CabinetEntityPanel(x00, y00, artifactList, "Artifact");
        CabinetEntityPanel streetPanel = new CabinetEntityPanel(x00, y00, streetList, "Street");
        CabinetEntityPanel cityPanel = new CabinetEntityPanel(x00, y00, cityList, "City");
        CabinetEntityPanel bugPanel = new CabinetEntityPanel(x00, y00, bugList, "BugMessage");
        CabinetEntityPanel PersonPanel = new CabinetEntityPanel(x00, y00, personList, "Person");
        CabinetEntityPanel panelList[] = {userPanel, artPanel, streetPanel, cityPanel, bugPanel, PersonPanel};
        return panelList;
        }
    private CabinetEntityPanel[] panelList;
    private CabinetEntityPanel currentPanel;
    //------------------------------------------------------------------------------------
    private void funcButton(){
        if (service==null) {
            System.out.println("Клиент не подключен к серверу данных");
            return;
            }
        switch(APIFun.getSelectedIndex()) {
        case 0: apiKeepAlive(); break;
        case 1: apiLogin();  break;
        case 2: apiUploadFile();  break;
        case 3: apiLoadFile(Long.parseLong(Idddd.getText())); break;
        case 4:
            Artifact art2 = artifactList.get(panelList[1].listBox.getSelectedIndex());
            loadFile(art2);
            break;
        case 5: apiLoadApkLocal(); break;
        case 6: apiLoginBody(); break;
        case 7: apiDeleteById(); break;
        case 8: apiUndeleteById(); break;
        case 9: System.out.println("Имя файла-артефакта:"+artifactList.get(panelList[1].listBox.getSelectedIndex()).createArtifactServerPath());
                break;
        case 10: apiReadConsoleLog(); break;
        case 11: apiPing(); break;
        case 12: apiClearTable(); break;
        }
    }
    //------------------------------------------------------------------------------------------------------------------
    public Cabinet(){
        super();
        if (!tryToStart()) return;
        initComponents();
        Values.init();
        setTitle("Сервер данных БРС 2.0");
        panelList = createPanelList();
        currentPanel = panelList[0];
        onStart();
        setMES(MES);
        //gblEncoding = System.getProperty("file.encoding");
        //utf8 = gblEncoding.equals("UTF-8");
        this.setBounds(100, 100, 890, 650);
        APIFun.add("KeepAlive");
        APIFun.add("Логин");
        APIFun.add("Выгрузить файл");
        APIFun.add("Загрузить файл по id");
        APIFun.add("Загрузить файл из списка");
        APIFun.add("Загрузить МК локально");
        APIFun.add("Авторизация (body)");
        APIFun.add("Удалить по id");
        APIFun.add("Восстановить по id");
        APIFun.add("Имя файла-артефакта");
        APIFun.add("Читать лог консоли");
        APIFun.add("Пинг-понг");
        APIFun.add("Очистить таблицу");
        //System.out.print("АБВГДабвгде12345");
        ClientIP.add("localhost");
        ClientIP.add("217.71.138.9");
        ClientIP.add("217.71.138.8");
        ClientIP.add("217.71.138.5");
        Port.add("4567");
        Port.add("4569");
        Port.add("4571");
        Port.add("5001");
        JDBCFactory factory = new JDBCFactory();
        for (String ss : factory.getNameList())
            MongoDB.add(ss);
        //---------------------------------------------------------------------
        for(int i=0;i<=10;i++)
            Level.add(""+i);
        Mode.add("Актуальные");
        Mode.add("Все");
        Mode.add("Удаленные");
        ServerPort.add("4567");
        ServerPort.add("4569");
        ServerPort.add("4571");
        ServerPort.add("5001");
        Object olist[] = Values.EntityFactory.nameList().toArray();
        for(int i=0;i<olist.length;i++){
            EntityClasses.add((String)olist[i]);
            }
        Panels.removeAll();
        for(CabinetEntityPanel pp : panelList){
            //System.out.println(pp.name);
            pp.initPanel();
            Panels.add(pp.name);
            }
        currentPanel.setVisible(true);
        }
    //------------------------------------------------------------------------------------------------------------------
    Callback<Artifact> artifactCallback = new ArtifactCallBack();
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        MES = new java.awt.TextArea();
        DataServerOn = new javax.swing.JCheckBox();
        Idddd = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        APIFun = new java.awt.Choice();
        Phone = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        Password = new javax.swing.JTextField();
        JSONTrace = new javax.swing.JCheckBox();
        ExportXLS = new javax.swing.JButton();
        ImportXLS = new javax.swing.JButton();
        ClientON = new javax.swing.JCheckBox();
        ClientIP = new java.awt.Choice();
        EntityClasses = new java.awt.Choice();
        jLabel6 = new javax.swing.JLabel();
        Port = new java.awt.Choice();
        ClearDB = new javax.swing.JButton();
        Level = new java.awt.Choice();
        jLabel9 = new javax.swing.JLabel();
        Mode = new java.awt.Choice();
        ServerPort = new java.awt.Choice();
        TargetDB = new javax.swing.JButton();
        StartClient = new javax.swing.JButton();
        Panels = new java.awt.Choice();
        jLabel7 = new javax.swing.JLabel();
        MongoDB = new java.awt.Choice();
        ESSLabel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        getContentPane().setLayout(null);
        getContentPane().add(MES);
        MES.setBounds(290, 20, 560, 560);

        DataServerOn.setText("Лок. сервер данных");
        DataServerOn.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                DataServerOnItemStateChanged(evt);
            }
        });
        getContentPane().add(DataServerOn);
        DataServerOn.setBounds(10, 20, 140, 23);

        Idddd.setText("1");
        getContentPane().add(Idddd);
        Idddd.setBounds(180, 320, 90, 25);

        jButton2.setText("API");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton2);
        jButton2.setBounds(50, 260, 60, 23);
        getContentPane().add(APIFun);
        APIFun.setBounds(120, 260, 150, 20);

        Phone.setText("9139449081");
        getContentPane().add(Phone);
        Phone.setBounds(180, 350, 90, 25);

        jLabel2.setText("Phone");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(120, 360, 40, 14);

        jLabel3.setText("Password");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(120, 390, 46, 14);

        Password.setText("1234");
        getContentPane().add(Password);
        Password.setBounds(180, 380, 90, 25);

        JSONTrace.setText("Трассировка  HTTP-запроса");
        JSONTrace.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                JSONTraceItemStateChanged(evt);
            }
        });
        getContentPane().add(JSONTrace);
        JSONTrace.setBounds(10, 170, 190, 23);

        ExportXLS.setText("Экспорт xls");
        ExportXLS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ExportXLSActionPerformed(evt);
            }
        });
        getContentPane().add(ExportXLS);
        ExportXLS.setBounds(10, 230, 100, 23);

        ImportXLS.setText("Импорт xls");
        ImportXLS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ImportXLSActionPerformed(evt);
            }
        });
        getContentPane().add(ImportXLS);
        ImportXLS.setBounds(10, 200, 100, 23);

        ClientON.setText("Клиент");
        ClientON.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ClientONItemStateChanged(evt);
            }
        });
        getContentPane().add(ClientON);
        ClientON.setBounds(10, 50, 63, 23);
        getContentPane().add(ClientIP);
        ClientIP.setBounds(120, 50, 150, 20);
        getContentPane().add(EntityClasses);
        EntityClasses.setBounds(120, 290, 150, 30);

        jLabel6.setText("Таблица");
        getContentPane().add(jLabel6);
        jLabel6.setBounds(10, 450, 60, 14);
        getContentPane().add(Port);
        Port.setBounds(120, 80, 150, 20);

        ClearDB.setText("Очистка БД");
        ClearDB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ClearDBActionPerformed(evt);
            }
        });
        getContentPane().add(ClearDB);
        ClearDB.setBounds(120, 200, 100, 23);
        getContentPane().add(Level);
        Level.setBounds(80, 420, 90, 20);

        jLabel9.setText("Id");
        getContentPane().add(jLabel9);
        jLabel9.setBounds(120, 330, 10, 14);
        getContentPane().add(Mode);
        Mode.setBounds(180, 420, 90, 20);
        getContentPane().add(ServerPort);
        ServerPort.setBounds(180, 20, 90, 20);

        TargetDB.setText("Целевая БД");
        TargetDB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TargetDBActionPerformed(evt);
            }
        });
        getContentPane().add(TargetDB);
        TargetDB.setBounds(120, 230, 100, 23);

        StartClient.setText("Клиент");
        StartClient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                StartClientActionPerformed(evt);
            }
        });
        getContentPane().add(StartClient);
        StartClient.setBounds(200, 170, 73, 23);

        Panels.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                PanelsItemStateChanged(evt);
            }
        });
        getContentPane().add(Panels);
        Panels.setBounds(80, 450, 190, 20);

        jLabel7.setText("Уровень");
        getContentPane().add(jLabel7);
        jLabel7.setBounds(10, 420, 60, 14);
        getContentPane().add(MongoDB);
        MongoDB.setBounds(160, 110, 110, 20);

        ESSLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/brs.png"))); // NOI18N
        ESSLabel.setBorderPainted(false);
        ESSLabel.setContentAreaFilled(false);
        getContentPane().add(ESSLabel);
        ESSLabel.setBounds(10, 80, 40, 40);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void  apiPing(){
        try {
            Response<JEmpty> res = service.ping().execute();
            if (!res.isSuccessful()){
                System.out.println(Utils.httpError(res));
                }
            else{
                }
            } catch (IOException e) { System.out.println(e.getMessage()); }
        }
    private void showAnswer(Response<?> res){
        if (res.isSuccessful())
            System.out.println(res.body());
        else
            System.out.println(res.message());
        }

    private I_ServerState serverBack = new I_ServerState() {
        @Override
        public void onStateChanged(final ServerState serverState) {
            java.awt.EventQueue.invokeLater(new Runnable() {
                public void run() {
                    onBusy = true;
                    //System.out.println("?????????????????"+serverState.isServerRun());
                    DataServerOn.setSelected(serverState.isServerRun());
                    onServerState(serverState);
                    onBusy = false;
                }
            });

          }
        };

    private void DataServerOnItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_DataServerOnItemStateChanged
        if (onBusy) return;
        if (DataServerOn.isSelected()){
            dataServer.startServer(Integer.parseInt(ServerPort.getSelectedItem()), MongoDB.getSelectedIndex(), serverBack,true);
            onDataServerOnOff();
            }
        else{
            dataServer.shutdown();
            }
        serverOn = DataServerOn.isSelected();
    }//GEN-LAST:event_DataServerOnItemStateChanged
    //--------------------- Загрузка файла - общая часть
    private boolean putResponse(Response res8){
        if (!res8.isSuccessful()){
            System.out.println(Utils.httpError(res8));
            return false;
            }
        else{
            System.out.println(res8.body());
            return true;
            }
        }
    private void apiLogin(){
        try {
            Response<User> res5 = service.login(Phone.getText(),Password.getText()).execute();
            if (res5.isSuccessful())
                debugToken = res5.body().getSessionToken();
            System.out.println("Авторизация  "+res5.body());
            System.out.println("Новый токен:" +debugToken);
            } catch (Exception ee){ System.out.println(ee.getMessage()); }
        }
    private void apiLoginBody(){
        try {
            Account acc = new Account("",Phone.getText(),Password.getText());
            Response<User> res5 = service.login(acc).execute();
            if (res5.isSuccessful())
                debugToken = res5.body().getSessionToken();
            System.out.println("Авторизация  "+res5.body());
            System.out.println("Новый токен:" +debugToken);
            System.out.println("Фотография:" +res5.body().getPhoto().getRef().createArtifactServerPath());
            } catch (Exception ee){ System.out.println(ee.getMessage()); }
        }

    private void apiUploadFile(){
        FileNameExt fname = getInputFileName("Выгрузить файл","*",null);
        MultipartBody.Part body = RestAPICommon.createMultipartBody(fname);
        Call<Artifact> call = service.upload(debugToken,"TestFile",fname.fileName(),body);
        call.enqueue(artifactCallback);
        }
    private void apiLoadFile(long id){
        Response<Artifact> art = null;
        try {
            art = service.getArtifactById(debugToken,id,Level.getSelectedIndex()).execute();
            if (!art.isSuccessful()){
                System.out.println(art.message());
                return;
                }
            Artifact art2 = art.body();
            loadFile(art2);
            } catch (IOException e) { System.out.println(e.getMessage()); }
        }
    private void apiLoadApkLocal(){
        ClientFileReader reader = new ClientFileReader("d:", "FireFighter.apk", ClientIP.getSelectedItem(),
                Integer.parseInt(Port.getSelectedItem()),new AsyncTaskBack() {
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
                System.out.println("Клиент "+mes);
                }
            @Override
            public void onFinish(boolean result) {
                System.out.println("Завершено");
                }
            });
        }
    public void apiClearDB(){
        service.clearDB(debugToken,Password.getText());
        Password.setText("");
        }
    private void apiClearTable(){
        try {
            String ss = service.clearTable(debugToken,EntityClasses.getSelectedItem(),Password.getText()).execute().body().getValue();
            System.out.println(ss);
            } catch (IOException e) {
                System.out.println(e.toString());
                }
        Password.setText("");
        }
    public void apiTargetDB() {
        createDBExample().createAll(service,Password.getText());
        Password.setText("");
        }
    private void apiDeleteById(){
        try {
            long id = Long.parseLong(Idddd.getText());
            String fullName = EntityClasses.getSelectedItem();
            boolean bb = service.deleteById(debugToken,fullName,id).execute().body().value();
            } catch (Exception ex) { System.out.println(ex.getMessage()); }
            }
    private void apiUndeleteById(){
        try {
            long id = Long.parseLong(Idddd.getText());
            String fullName = EntityClasses.getSelectedItem();
            boolean bb = service.undeleteById(debugToken,fullName,id).execute().body().value();
        } catch (Exception ex) { System.out.println(ex.getMessage()); }
    }
    private void apiKeepAlive(){
        try {
            int count  = service.keepalive(debugToken).execute().body().getValue();
            } catch (Exception ex) { System.out.println(ex.getMessage()); }
        }
    private void apiReadConsoleLog(){
        try {
            Response<StringList> log = service.getConsoleLog(debugToken,30).execute();
            if (!log.isSuccessful()){
                System.out.println(log.message());
                return;
                }
            System.out.println("------------------------------ Чтение консоли(+)\n");
            System.out.print(log.body().toString());
            System.out.println("------------------------------ Чтение консоли(-)\n");
            } catch (IOException ex) { System.out.println(ex.getMessage()); }
        }
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        funcButton();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void JSONTraceItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_JSONTraceItemStateChanged
        dataServer.setObjectTrace(JSONTrace.isSelected());
    }//GEN-LAST:event_JSONTraceItemStateChanged

    private void ExportXLSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ExportXLSActionPerformed
        try {
            FileNameExt fspec = getOutputFileName("Экспорт xls","","mongo.xls");
            ExcelX xls = new ExcelX();
            dataServer.exportToExcel(xls);
            xls.save(fspec);
            System.out.println("Экспорт в файл "+fspec.fileName());
            } catch (Exception e) { System.out.println(e.toString()); }
    }//GEN-LAST:event_ExportXLSActionPerformed

    private void ImportXLSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ImportXLSActionPerformed
        try {
            FileNameExt fspec = getInputFileName("Импорт xls","xls",null);
            ExcelX xls = new ExcelX();
            dataServer.clearDB();
            xls.load(fspec,dataServer.mongoDB());
    } catch (Exception e) { System.out.println(e.toString()); }
        // TODO add your handling code here:
    }//GEN-LAST:event_ImportXLSActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        dataServer.shutdown();
    }//GEN-LAST:event_formWindowClosing

    private void ClientONItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ClientONItemStateChanged
        if (ClientON.isSelected()){
            if (!startClient(ClientIP.getSelectedItem(),Port.getSelectedItem()))
                ClientON.setSelected(false);
                }
        else{
            service=null;
            }
    }//GEN-LAST:event_ClientONItemStateChanged

    private void ClearDBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ClearDBActionPerformed
        apiClearDB();
    }//GEN-LAST:event_ClearDBActionPerformed

    private void TargetDBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TargetDBActionPerformed
        apiTargetDB();
    }//GEN-LAST:event_TargetDBActionPerformed

    private boolean bb1=false;
    private void StartClientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_StartClientActionPerformed
        Runtime r =Runtime.getRuntime();
        Process p =null;
        String cmd = "java -cp FireFighterDataserver.jar firefighter.desktop.Client";
        try {
            p=r.exec(cmd);
            /*
            p.waitFor();
            InputStreamReader br=new InputStreamReader(p.getInputStream(),"UTF-8");
            String ss="";
            while(true) {
            while(true) {
                int nn=br.read();
                if (nn==-1) break;
                char z=(char)nn;
                ss+=z;
                }
            System.out.println(ss);
            */
            }
            catch(Exception e){ System.out.println("Клиент не стартанул\n"+e+"\n"); }
    }//GEN-LAST:event_StartClientActionPerformed

    private void PanelsItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_PanelsItemStateChanged
        int idx = Panels.getSelectedIndex();
        currentPanel.setVisible(false);
        currentPanel = panelList[idx];
        currentPanel.setVisible(true);
    }//GEN-LAST:event_PanelsItemStateChanged

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Cabinet.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Cabinet.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Cabinet.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Cabinet.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Cabinet().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private java.awt.Choice APIFun;
    private javax.swing.JButton ClearDB;
    private java.awt.Choice ClientIP;
    private javax.swing.JCheckBox ClientON;
    private javax.swing.JCheckBox DataServerOn;
    private javax.swing.JButton ESSLabel;
    private java.awt.Choice EntityClasses;
    private javax.swing.JButton ExportXLS;
    private javax.swing.JTextField Idddd;
    private javax.swing.JButton ImportXLS;
    private javax.swing.JCheckBox JSONTrace;
    private java.awt.Choice Level;
    private java.awt.TextArea MES;
    private java.awt.Choice Mode;
    private java.awt.Choice MongoDB;
    private java.awt.Choice Panels;
    private javax.swing.JTextField Password;
    private javax.swing.JTextField Phone;
    private java.awt.Choice Port;
    private java.awt.Choice ServerPort;
    private javax.swing.JButton StartClient;
    private javax.swing.JButton TargetDB;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    // End of variables declaration//GEN-END:variables
}
