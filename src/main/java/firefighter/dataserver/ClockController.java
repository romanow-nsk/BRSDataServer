package firefighter.dataserver;

import firefighter.core.API.RestAPIBase;
import firefighter.core.UniException;
import firefighter.core.Utils;
import firefighter.core.constants.Values;
import firefighter.core.entity.*;
import firefighter.core.entity.artifacts.Artifact;
import firefighter.core.entity.artifacts.ArtifactTypes;
import firefighter.core.entity.subjectarea.*;
import firefighter.core.entity.subjectarea.events.EventFacade;
import firefighter.core.entity.subjectarea.events.EventSystem;
import firefighter.core.mongo.DBQueryList;
import firefighter.core.mongo.I_DBQuery;
import firefighter.core.utils.OwnDateTime;
import firefighter.core.utils.Pair;
import spark.utils.IOUtils;

import java.io.*;
import java.util.ArrayList;

public class ClockController<T extends DataServer> extends Thread{
    protected T db;
    protected boolean shutdown=false;
    private int dataLoop=0;
    private int dataLongLoop=0;
    private int failureLoop=0;
    private int otherDataLoop=0;
    private int fileScanLoop=0;
    private int mainServerLoop=0;
    private OwnDateTime lastDay = new OwnDateTime(false);
    public void resetCash() throws UniException {
        }
    public ClockController(T db0){
        db = db0;
        lastDay.onlyDate();
        start();
        }
    public void run(){
        while (!shutdown){
            try {
                Thread.sleep(1000);                 // Часики 1 сек
                } catch (InterruptedException e) {}
            try {
                clockCycle();
                } catch (Exception e) {
                    System.out.println("Внутреннее API:"+Utils.createFatalMessage(e));
                    }
            }
        }
    public void shutdown(){
        this.interrupt();
        shutdown=true;
        }
    public void clockCycle(){
        WorkSettings ws;
        try {
            ws = db.common.getWorkSettings();
            } catch (UniException e) {
                return;
                }
            }
    //-----------------------------------------------------------------------------------------------------------
    private final static int hourX=19;
    private final static int minuteX=45;
    //-----------------------------------------------------------------------------------------------------------
    //------------------------------------------------------------------------------------------------------------------
    public static void main(String a[]){
    }
}
