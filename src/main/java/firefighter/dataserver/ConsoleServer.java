package firefighter.dataserver;

import firefighter.core.API.RestAPIBase;
import firefighter.core.I_EmptyEvent;
import firefighter.core.I_String;
import firefighter.core.LogStream;
import firefighter.core.ServerState;
import firefighter.core.constants.Values;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.PrintStream;
import java.util.concurrent.TimeUnit;

public class ConsoleServer {
    protected I_DBTarget dbTarget;
    protected Class apiFace;
    private int lineCount=0;
    private String gblEncoding="";
    private boolean utf8;
    private DataServer dataServer = new DataServer();
    private I_ServerState serverBack = new I_ServerState() {
        @Override
        public void onStateChanged(ServerState serverState) {
            java.awt.EventQueue.invokeLater(new Runnable() {
                public void run() {
                    System.out.println("Asterisk: "+serverState.toString());
                }
            });
        }
    };
    //---------------------------------------------------------------------
    private I_EmptyEvent asteriskBack = new I_EmptyEvent() {
        @Override
        public void onEvent() {
                System.out.println(""+dataServer.getServerState().getLastMailNumber());
        }
    };
    public ConsoleServer(){
        Values.init();
        dbTarget = new DBExample();
        apiFace = RestAPIBase.class;
        }
    public ConsoleServer(I_DBTarget target, Class apiFace0){
        apiFace = apiFace0;
        dbTarget = target;
        }
    private int port;
    public void setTarget(){
        Retrofit retrofit=null;
        RestAPIBase service=null;
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(Values.HTTPTimeOut, TimeUnit.SECONDS)
                .connectTimeout(Values.HTTPTimeOut, TimeUnit.SECONDS)
                .build();
        retrofit = new Retrofit.Builder()
                .baseUrl("http://localhost:"+port)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = (RestAPIBase) retrofit.create(apiFace);
        dbTarget.createAll(service, Values.DebugTokenPass);
        }
    public void startServer(int port0,String init){
        port = port0;
        dataServer.startServer(port, Values.MongoDBType36, serverBack,(init.equals("target")));
        gblEncoding = System.getProperty("file.encoding");
        utf8 = gblEncoding.equals("UTF-8");
        asteriskBack.onEvent();
        final LogStream log = new LogStream(utf8, new I_String() {
            @Override
            public void onEvent(String ss) {
                dataServer.addToLog(ss);
                }
            });
        if (init.equals("target")){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(10);
                        } catch (InterruptedException e) {}
                    setTarget();
                    System.setOut(new PrintStream(log));
                    System.setErr(new PrintStream(log));
                    }
                }).start();
            }
        else {
            System.setOut(new PrintStream(log));
            System.setErr(new PrintStream(log));
            }
        }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                //----------------------------------------------------------------------------------
                String port = args[0];
                String init = "";
                if (args.length==0)
                    port = "4567";
                if (args.length>=1)
                    init = args[1];
                System.out.println("Порт="+port);
                ConsoleServer server = new ConsoleServer();
                server.startServer(Integer.parseInt(port),init);
                }
            });
        }
    }
