package firefighter.dataserver;

import com.google.gson.Gson;
import firefighter.core.API.RestAPIBase;
import firefighter.core.ServerState;
import firefighter.core.UniException;
import firefighter.core.Utils;
import firefighter.core.constants.ConstList;
import firefighter.core.constants.Values;
import firefighter.core.constants.ValuesBase;
import firefighter.core.entity.Entity;
import firefighter.core.entity.base.BugMessage;
import firefighter.core.entity.baseentityes.JString;
import firefighter.core.entity.subjectarea.WorkSettings;
import firefighter.core.entity.users.User;
import firefighter.core.export.ExcelX;
import firefighter.core.jdbc.JDBCFactory;
import firefighter.core.mongo.*;
import firefighter.core.utils.OwnDateTime;
import firefighter.core.utils.Pair;
import firefighter.core.utils.StringFIFO;
import firefighter.desktop.APICallServer;
import firefighter.ftp.ServerFileAcceptor;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import spark.Request;
import spark.Response;
import spark.Route;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

// AJAX посылает post, а браузер - get
public class DataServer {
    //-------------------- Модель БД сервера --------------------------
    private I_ServerState masterBack=null;          // Обработчик событий ServerState
    I_MongoDB mongoDB = new MongoDB36();
    APIUser users = null;
    public APIArtifact files = null;
    public APICommon common=null;
    public APINotification notify=null;
    public APIAdmin admin = null;
    ConstList constList = new ConstList();          // Константы из ValuesBase
    private String dataServerFileDir="";            // Корневой каталог артефактов сервера
    int port;                                       // Номер порта
    StringFIFO consoleLog = new StringFIFO(Values.ConsoleLogSize);
    private ServerFileAcceptor deployer=null;       // Приемник обновления через TCP
    SessionController sessions = null;              // Контроллер сессий
    private String debugToken = "";                 // Дежурный токен
    boolean objectTrace=false;                      // Трассировка содержимого запросов/ответов в лог
    private boolean shutDown = false;               //
    protected boolean isRun=false;
    private boolean mainServer=false;               // Из настроек - главный сервер
    private BufferedWriter logFile=null;            //
    private OwnDateTime logFileCreateDate;
    private OwnDateTime logFileWriteDate;
    protected ClockController clock=null;
    public DataServer(){}
    public I_MongoDB mongoDB(){ return mongoDB; }
    public APICommon common(){ return common; }
    //-------------------------------------------------------------------------
    private void getAnswer(Process p){
        new Thread(){
            public void run() {
                try {
                    InputStreamReader br = new InputStreamReader(p.getInputStream(), "UTF-8");
                    while (!shutDown) {
                        int nn = br.read();
                        if (nn == -1) break;
                        System.out.print((char) nn);
                        }
                    br.close();
                    } catch (Exception e) { System.out.println("Mongo error " + e.toString()); }
                }
            }.start();
        }
    /*
    public void startMongo(){
        Runtime r =Runtime.getRuntime();
        Process p =null;
        try {
            p = r.exec(set.mongoStartCmd());
            p.waitFor();
            getAnswer(p);
            } catch(Exception ee){ System.out.println("Mongo is not started "+ee.toString());}
        }
    */

    public void setMIMETypes(){
        //for(int i=0;i<Values.ArtifactExt.length;i++)
        //    spark.Spark.staticFiles.registerMimeType(Values.ArtifactExt[i],Values.ArtifactMime[i]);
        }
    public String dataServerFileDir(){
        String dir =  dataServerFileDir+"/"+port;
        File path = new File(dir);
        if (!path.exists())
            path.mkdir();
        return dir;
        }
    public String rootServerFileDir(){
        String dir =  dataServerFileDir;
        File path = new File(dir);
        if (!path.exists())
            path.mkdir();
        return dir;
    }
    public I_ServerState serverBack = new I_ServerState() {        // Перехвать обратного вызова с установкой собственых
        @Override
        public void onStateChanged(ServerState serverState) {
            serverState.setServerRun(isRun);
            if (masterBack!=null)
                masterBack.onStateChanged(serverState);
            }
    };

    public boolean startServer(int port0, int mongoType, I_ServerState ss,boolean force){
        masterBack = ss;
        port = port0;
        System.out.println(System.getProperty("user.dir"));
        System.out.println(System.getProperty("os.name"));
        System.out.println("PID="+ Utils.getPID());
        mongoDB = new JDBCFactory().getDriverByIndex(mongoType);
        return restartServer(force);
        }
     public boolean restartServer(boolean force){
        constList.createConstList();
            try {
                if (!mongoDB.openDB(port)){
                    System.out.println("Mongo is not open");
                    return false;
                    }
                } catch (UniException e) {
                System.out.println("Mongo is not open "+e.toString());
                return false;
                }
        setMIMETypes();
        spark.Spark.port(port);
        spark.Spark.staticFiles.location("/public");                            // Обязательно
        spark.Spark.notFound((req,res)->{
            StringBuffer ff = new StringBuffer();
            InputStreamReader in = new InputStreamReader(new FileInputStream(dataServerFileDir()+"/index.html"));
            int cc;
            while((cc=in.read())!=-1){
                ff.append((char)cc);
                }
            in.close();
            return ff.toString();
            //res.redirect("/index.html"); return null;
            });
        //spark.Spark.notFound((req,res)->{res.redirect("/index.html"); return null; });
        spark.Spark.staticFiles.externalLocation("/public/static");     // Не понятно
        spark.Spark.staticFileLocation("/public");                            // Не понятно
        spark.Spark.externalStaticFileLocation("/public");              // Не понятно
        //https://gist.github.com/zikani03/7c82b34fbbc9a6187e9a CORS для Spark
        spark.Spark.before("/*", (request,response)->{
            response.header("Access-Control-Allow-Methods", "GET,PUT,POST,DELETE,OPTIONS");
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Allow-Headers", "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin,");
            response.header("Access-Control-Allow-Credentials", "true");
            });
         spark.Spark.options("/*",
                 (request, response) -> {
                     String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
                     if (accessControlRequestHeaders != null) {
                         response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
                        }
                     String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
                     if (accessControlRequestMethod != null) {
                         response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
                        }
                     return "OK";
                 });
         /*
        spark.Spark.options("/*", (request,response)-> {
                    // - старая рекомедация для стороннего доступа от web
                    String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
                    if (accessControlRequestHeaders != null) {
                        response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
                        }
                    String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
                    if (accessControlRequestMethod != null) {
                        response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
                        }

            response.header("Access-Control-Allow-Methods", "GET,PUT,POST,DELETE,OPTIONS");
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Allow-Headers", "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin,");
            response.header("Access-Control-Allow-Credentials", "true");
            response.header("Content-Type","application/json");
            return "OK";
            });
        */
        //------------------ Обработка запросов на прямое чтение файлов из каталога артефактов экземпляра сервера
        spark.Spark.get("/file/*", new Route() {
                    @Override
                    public Object handle(Request request, Response response) throws Exception {
                        System.out.println("Файл:"+request.splat()[0]);
                        HttpServletResponse res = response.raw();
                        OutputStream out = res.getOutputStream();
                        String fname = dataServerFileDir()+"/"+request.splat()[0];
                        File ff = new File(fname);
                        if (!ff.exists()){
                            res.sendError(Values.HTTPNotFound,"File "+fname+" не найден");
                            response.body("");
                            return null;
                            }
                        FileInputStream in = new FileInputStream(ff);
                        long sz = ff.length();
                        response.raw().setContentLengthLong(sz);
                        while (sz--!=0)
                            out.write(in.read());
                        in.close();
                        out.close();
                        return null;
                        }
                });
        //-------------------------------------------------------------------------------------------------
        /*
        spark.Spark.before("/file/*", new Filter() {
            @Override
            public void handle(Request request, Response response) throws Exception {
                System.out.println("Файл:"+request.splat()[0]);
                HttpServletResponse res = response.raw();
                OutputStream out = res.getOutputStream();
                String fname = dataServerFileDir()+"/"+request.splat()[0];
                File ff = new File(fname);
                if (!ff.exists()){
                    res.sendError(Values.HTTPNotFound,"File "+fname+" не найден");
                    response.body("");
                    return;
                    }
                FileInputStream in = new FileInputStream(ff);
                long sz = ff.length();
                response.raw().setContentLengthLong(sz);
                while (sz--!=0)
                    out.write(in.read());
                in.close();
                out.close();
                }
            });
         */
        //-------------------------------------------------------------------------------------
        common = new APICommon(this);
        WorkSettings ws = new WorkSettings();
        try {
            ws = common.getWorkSettings();
            dataServerFileDir = ws.isDataServerFileDirDefault() ? System.getProperty("user.dir") : ws.getDataServerFileDir();
            } catch (Exception ee){
                System.out.println("WorkSettings is not read: "+ee.toString());
                if (!force) return false;
                try {
                    mongoDB.clearTable("WorkSettings");
                    mongoDB.add(new WorkSettings());
                    } catch (UniException ex){
                        Utils.printFatalMessage(ex);
                        }
        }
        //--------------------------------------------------------------------------------
         sessions = new SessionController(this);
         deployer = new ServerFileAcceptor(this,dataServerFileDir(),port);
         clock = new ClockController(this);
        //---------------------------------------------------------------------------------
        users = new APIUser(this);
        files = new APIArtifact(this);
        notify = new APINotification(this);
        admin = new APIAdmin(this);
        debugToken = sessions.createContext(0,ValuesBase.env().superUser());          // Вечный ключ для отладки
        isRun=true;
        try {
            changeServerState(new I_ChangeRecord() {
                @Override
                public boolean changeRecord(Entity ent) {
                    ServerState state =(ServerState)ent;
                    state.setAsrteiskDialOn(false);
                    state.setAsteriskMailOn(false);
                    state.init();
                    return true;
                    }
                });
            } catch (UniException e) { System.out.println("StartServer: "+e.toString());}
        openLogFile();
        return true;
        }
    public void addToLog(String ss){
        if (!isRun)
            return;
        consoleLog.add(ss);
        writeToLogFile(ss);
        }
    public void shutdown(){
        if (!isRun) return;
        shutDown=true;
        closeLogFile();
        spark.Spark.stop();
        spark.Spark.awaitStop();
        mongoDB.closeDB();
        if (deployer!=null)
            deployer.shutdown();
        if (sessions!=null)
            sessions.shutdown();
        isRun=false;
        serverBack.onStateChanged(common.getServerStateRight());
        }
    public void setObjectTrace(boolean objectTrace) {
        this.objectTrace = objectTrace;
        }
    public String toJSON(Object ent, Request req, RequestStatistic statistic){
        String ss = req.queryString();
        ss = "<-----"+req.ip()+req.pathInfo()+" "+req.requestMethod()+" "+(ss == null ? "" : ss);
        if (statistic.startTime!=-1){
            long dd = System.currentTimeMillis()-statistic.startTime;
            ss += " time="+(dd)+" мс";
            common.addTimeStamp(dd);
        }
        ss += " объектов "+statistic.entityCount+" ("+statistic.recurseCount+")";
        System.out.println(ss);
        try {
            String out = new Gson().toJson(ent);
            if (objectTrace)
                System.out.println("--->"+ent.getClass().getSimpleName()+":"+out);
            return out;
        } catch (Exception ee){
            System.out.println("Ошибка JSON: "+ee.toString());
            return "{}";
        }
    }
    public String  traceRequest(Request req){
        String ss = "<----"+req.ip()+req.pathInfo()+" "+req.requestMethod()+" "+req.queryString()+"\n";
        Set<String> rr = req.headers();
        for (String zz : rr)
            ss += "header:"+zz+"="+req.headers(zz)+"\n";
        Map<String,String> qq = req.cookies();
        for (String zz : qq.keySet())
            ss += "qookie:"+zz+"="+qq.get(zz)+"\n";
        if (req.body().length()!=0)
            ss += req.body()+"\n";
        System.out.print(ss);
        return ss;
        }
    public long canDo(Request req, Response res) throws IOException {
        return canDo(req, res,true);
    }
    public long canDo(Request req, Response res,boolean testToken) throws IOException {
        if (!mongoDB.isOpen()){
            createHTTPError(res,Values.HTTPServiceUnavailable, "Database not open");
            return 0;
            }
        //res.header("Access-Control-Allow-Origin","*");
        //res.header("Access-Control-Allow-Methods", "GET");
        //res.type("application/json");
        //String ss = req.queryString();
        //System.out.println("<-----"+req.ip()+req.pathInfo()+" "+req.requestMethod()+" "+(ss == null ? "" : ss));
        if (objectTrace)
            traceRequest(req);
        boolean bb =  !testToken ? true : getSession(req,res)!=null;
        return bb ? System.currentTimeMillis() : 0;
        }
    //------------------- КЛючи сессий --------------------------------------------
    public boolean isAdmin(Request req,Response res) throws IOException {
        UserContext uu = getSession(req,res,false);
        if (uu==null)
            return false;
        int type = uu.getUser().getTypeId();
        if (type==Values.UserAdminType || type==Values.UserSuperAdminType)
                return true;
        return false;
        }
    public boolean isPrivilegedUser(Request req,Response res) throws IOException {
        UserContext uu = getSession(req,res,false);
        if (uu==null)
            return false;
        int type = uu.getUser().getTypeId();
        if (type==Values.UserAdminType || type==Values.UserSuperAdminType)
            return true;
        return false;
    }
    public boolean isSomeUser(Request req,Response res,User user) throws IOException {
        UserContext uu = getSession(req,res,false);
        if (uu==null)
            return false;
        int type = uu.getUser().getTypeId();
        if (type==Values.UserAdminType || type==Values.UserSuperAdminType)
            return true;
        return uu.getUser().getOid() == user.getOid();
        }
    public UserContext getSession(Request req,Response res) throws IOException {
        return getSession(req,res,true);
        }
    public UserContext getSession(Request req,Response res,boolean answer) throws IOException {
        String val = req.headers(Values.SessionHeaderName);
        if (val==null){
            if (answer)
                createHTTPError(res,Values.HTTPAuthorization, "SessionToken not send");
            return null;
            }
        UserContext ctx = sessions.getContext(val);
        if (ctx==null){
            if (answer)
                createHTTPError(res,Values.HTTPAuthorization, "SessionToken не найден (too old)");
            return null;
            }
        ctx.wasCalled();            // Отметить время обращения
        return ctx;
        }
    public String createSessionToken(User user){
        return sessions.createContext(Values.SessionSilenceTime,user);
        }
    public String getDebugToken(){ return  debugToken; }
    public void closeSession(String key){ sessions.removeContext(key);}
    //-------------------------------------------------------------------------------
    /*
    public void loadSettings(){
        try {
            set = (Settings)load(Settings.class);
            } catch (UniException e) {
                System.out.println("Настройки не загружены, сброс с исходные:" +e.toString());
                set = new Settings();
                saveSettings();
                }
        }
    public void saveSettings(){
        try {
            save(set);
            } catch (UniException e) { System.out.println("Настройки не сохранены:" +e.toString()); }
        }
    */
    public void save(Object entity) throws UniException {
        try {
            Gson gson = new Gson();
            OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(entity.getClass().getSimpleName()+".json"), "UTF-8");
            gson.toJson(entity,out);
            out.flush();
            out.close();
            } catch (Exception ee){ throw UniException.io(ee); }
        }
    public Object load(Class entity) throws UniException {
        try {
            Gson gson = new Gson();
            InputStreamReader out = new InputStreamReader(new FileInputStream(entity.getSimpleName()+".json"), "UTF-8");
            Object ent = new Gson().fromJson(out,entity);
            out.close();
            return ent;
            } catch (Exception ee){ throw UniException.io(ee); }
        }
    public boolean exportToExcel(ExcelX ex) {
        return admin.exportToExcel(ex);
        }
    public String clearDB() throws UniException {
        return mongoDB.clearDB();
        }
    public ServerState getServerState(){
        return common.getServerState();
        }
    public void changeServerState(I_ChangeRecord todo) throws UniException {
        common.changeServerState(todo);
        serverBack.onStateChanged(getServerState());
        }

    public void delayInGUI(final int sec,final Runnable code){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000*sec);
                    java.awt.EventQueue.invokeLater(code);
                } catch (InterruptedException e) {}
            }
        }).start();
    }
    //----------------------------------------------------------------------------------------------------------------
    public void createHTTPError(Response res, int code, String mes){
        funCreateHTTPError(res,code,mes);
        }
    //----------------------------------------------------------------------------------------------------------------
    public static void  funCreateHTTPError(Response res, int code, String mes){
        res.status(code);
        res.raw().setCharacterEncoding("utf-8");
        res.body(mes);
        System.out.println("HTTP: "+code+" "+mes);
        }
    public boolean isTokenValid(String token){
        return sessions.isTokenValid(token);
        }
    //---------------------------------------------------------- Поддержка log-файла -----------------
    synchronized public void openLogFile(){
        if (logFile!=null){
            closeLogFile();
            }
        logFileCreateDate = new OwnDateTime();
        logFileWriteDate =  new OwnDateTime();
        logFileWriteDate.onlyDate();
        String fname = dataServerFileDir()+"/log";
        File dir = new File(fname);
        if (!dir.exists()){
            dir.mkdir();
            }
        fname+="/"+logFileCreateDate.toString2()+".log";
        try {
            logFile = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fname),"Windows-1251"));
            } catch (Exception e) {}
        }
    synchronized public void closeLogFile(){
        if (logFile!=null){
            try {
                logFile.flush();
                logFile.close();
                } catch (IOException e) {}
            logFile=null;
            }
        }
    synchronized public void writeToLogFile(String ss){
        if (logFile==null)
            return;
        OwnDateTime dd = new OwnDateTime();
        dd.onlyDate();
        if (!dd.equals(logFileWriteDate)){
            closeLogFile();
            openLogFile();
            }
        try {
            logFile.write(ss);
            logFile.newLine();
            } catch (IOException e) {}
        }
    public void sendBug(String module,Exception ex){
        try {
            String err = Utils.createFatalMessage(ex);
            System.out.println(err);
            mongoDB.add(new BugMessage(module+":\n"+err));
            notify.createBRSEvent(Values.EventSystem,Values.ELError,"Программная ошибка",err);
            } catch (UniException e) {
                System.out.println(Utils.createFatalMessage(e));
                }
        }
    public void sendBug(String module,String err){
        try {
            System.out.println(err);
            mongoDB.add(new BugMessage(module+":\n"+err));
            notify.createBRSEvent(Values.EventSystem,Values.ELError,"Программная ошибка",err);
            } catch (UniException e) {
                System.out.println(Utils.createFatalMessage(e));
                }
    }
    public boolean isMainServer() {
        return mainServer;
        }
    //---------------------------------------------------------------------------------------------------------
    public Pair<RestAPIBase,String> startOneClient(String ip, String port) throws Exception {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(Values.HTTPTimeOut, TimeUnit.SECONDS)
                .connectTimeout(Values.HTTPTimeOut, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://"+ip+":"+port)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        RestAPIBase service = (RestAPIBase)retrofit.create(RestAPIBase.class);
        JString ss = new APICallServer<JString>(){
            @Override
            public Call<JString> apiFun() {
                return service.debugToken(Values.DebugTokenPass);
            }
        }.call(service);
        return new Pair<>(service,ss.getValue());
        }


    public static void main(String argv[]){
        new DataServer();
        }
}
