package firefighter.desktop;

import firefighter.core.I_Important;
import firefighter.core.I_Notify;

import java.util.concurrent.CopyOnWriteArrayList;

public class AppData {
    private static AppData data=null;
    private AppData(){}
    public static AppData ctx(){
        if (data==null)
            data = new AppData();
        return data;
        }
    private I_Notify notify=null;
    public I_Notify getNotify(){ return notify; }
    public static void notify(String ss){
        if (data!=null && data.notify!=null) data.notify.notify(ss);
    }
    public void setNotify(I_Notify note){ notify=note; }
    private CopyOnWriteArrayList<I_Important> childs = new CopyOnWriteArrayList();
    public I_Important tryToStart(I_Important fr){
        for (I_Important frame : childs){
            if (fr.getClass()==frame.getClass()){
                return frame;
            }
        }
        childs.add(fr);
        return null;
    }
    synchronized public void onClose(I_Important fr){
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                childs.remove(fr);
            }});
    }
    public void notifyEvent(int code, String mes){
        sendEvent(0,true,code,mes);
    }
    public void sendEvent(int code){ sendEvent(code,true,0,"");}
    synchronized public void sendEvent(int code,boolean on, int value, String name){
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                for (I_Important frame : childs){
                    frame.onEvent(code,on,value,name);
                }
            }});
    }
}