package firefighter.dataserver;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import firefighter.core.DBRequest;
import firefighter.core.ServerState;
import firefighter.core.UniException;
import firefighter.core.constants.ConstList;
import firefighter.core.constants.Values;
import firefighter.core.entity.*;
import firefighter.core.entity.artifacts.Artifact;
import firefighter.core.entity.base.BugMessage;
import firefighter.core.entity.base.HelpFile;
import firefighter.core.entity.base.StringList;
import firefighter.core.entity.base.WorkSettingsBase;
import firefighter.core.entity.baseentityes.*;
import firefighter.core.entity.subjectarea.WorkSettings;
import firefighter.core.entity.users.User;
import firefighter.core.mongo.DAO;
import firefighter.core.mongo.I_ChangeRecord;
import firefighter.core.mongo.RequestStatistic;
import spark.Request;
import spark.Response;
import spark.Spark;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class APICommon extends APIBase{
    private WorkSettings workSettings = null;
    private ServerState serverState = null;     // Ленивый ServerState
    public boolean addUser(User tt) throws UniException {
        if (!db.mongoDB.isOpen())
            return false;
        db.mongoDB.add(tt);
        return true;
        }
    public APICommon(DataServer db0) {
        super(db0);
        //------------------------------------------------- Таблица API ------------------------------------------------
        // localhost:4567/user/login?phone=9139449081
        Spark.get("/api/entity/list", routeEntityList);
        Spark.get("/api/entity/list/last", routeEntityListLast);
        Spark.post("/api/entity/add", routeEntityAdd);
        Spark.get("/api/entity/get", routeEntityGet);
        Spark.post("/api/entity/update", routeEntityUpdate);
        Spark.post("/api/entity/update/field", routeEntityUpdateField);
        Spark.post("/api/entity/update/object/field", routeEntityUpdateField);
        Spark.post("/api/entity/remove", routeEntityRemove);
        Spark.get("/api/entity/number", routeEntityNumber);
        Spark.get("/api/entity/get/withpaths", routeEntityGet);
        Spark.get("/api/entity/list/withpaths", routeEntityList);
        //----------------------------------------------------------------------------------------------------------
        spark.Spark.get("/api/debug/ping", apiPing);
        spark.Spark.get("/api/keepalive", apiKeepAlive);
        spark.Spark.post("/api/bug/add", apiSendBug);
        spark.Spark.get("/api/bug/list", apiBugList);
        spark.Spark.get("/api/bug/get", apiGetBug);
        spark.Spark.get("/api/version", apiVersion);
        spark.Spark.get("/api/serverstate", apiServerState);
        spark.Spark.get("/api/worksettings", apiWorkSettings);
        spark.Spark.post("/api/worksettings/update", apiWorkSettingsUpdate);
        spark.Spark.post("/api/entity/delete", apiDeleteById);
        spark.Spark.post("/api/entity/undelete", apiUndeleteById);
        spark.Spark.get("/api/debug/token", apiDebugToken);
        spark.Spark.get("/api/debug/consolelog", routeGetConsoleLog);
        spark.Spark.get("/api/const/all", apiConstAll);
        spark.Spark.get("/api/const/bygroups", apiConstByGroups);
        spark.Spark.get("/api/names/get",routeNamesByPattern);
        spark.Spark.get("/api/helpfile/list",apiHelpFileList);
        Spark.post("/api/entity/artifactlist/add",routeArtifactToList);
        Spark.post("/api/entity/artifactlist/remove",routeArtifactFromList);
        Spark.post("/api/entity/artifact/replace",routeArtifactReplace);
        }
    //----------------------------- образцы 4 операций --------------------------------------------
    RouteWrap routeEntityAdd = new RouteWrap() {
        @Override
        public Object _handle(Request req, Response res, RequestStatistic statistic) throws Exception {
            ParamInt plevel = new ParamInt(req,res,"level",0);
            int level = plevel.isValid() ?  plevel.getValue() : 0;
            ParamBody qq = new ParamBody(req, res, DBRequest.class);
            if (!qq.isValid()) return null;
            DBRequest dbReq = (DBRequest)qq.getValue();
            long oid = db.mongoDB.add(dbReq.get(new Gson()),level);
            return new JLong(oid);
        }};
    RouteWrap routeEntityList = new RouteWrap() {
        @Override
        public Object _handle(Request req, Response res, RequestStatistic statistic) throws Exception {
            ParamInt fl = new ParamInt(req,res,"mode", Values.GetAllModeActual);
            ParamInt plevel = new ParamInt(req,res,"level",0);
            ParamString cname = new ParamString(req,res,"classname","");
            String className = cname.isValid() ?  cname.getValue() : "";
            Class cc = Values.EntityFactory.getClassForSimpleName(className);
            if (cc==null){
                db.createHTTPError(res,Values.HTTPRequestError, "Недопустимый класс сущности "+className);
                return null;
            }
            ParamString paths = new ParamString(req,res,"paths","");
            String pathsList = paths.isValid() ?  paths.getValue() : "";
            EntityList<Entity> xx;
            xx = (EntityList<Entity>)db.mongoDB.getAll((Entity) cc.newInstance(),fl.getValue(),plevel.getValue(),pathsList,statistic);
            ArrayList<DBRequest> out = new ArrayList<>();
            Gson gson = new Gson();
            for(Entity ent : xx)
                out.add(new DBRequest(ent,gson));
            return out;
        }};
    RouteWrap routeEntityListLast = new RouteWrap() {
        @Override
        public Object _handle(Request req, Response res, RequestStatistic statistic) throws Exception {
            ParamInt number = new ParamInt(req,res,"number");
            if (!number.isValid())
                return null;
            ParamInt plevel = new ParamInt(req,res,"level",0);
            ParamString cname = new ParamString(req,res,"classname","");
            String className = cname.isValid() ?  cname.getValue() : "";
            Class cc = Values.EntityFactory.getClassForSimpleName(className);
            if (cc==null){
                db.createHTTPError(res,Values.HTTPRequestError, "Недопустимый класс сущности "+className);
                return null;
                }
            ParamString paths = new ParamString(req,res,"paths","");
            String pathsList = paths.isValid() ?  paths.getValue() : "";
            Entity entity = (Entity) cc.newInstance();
            long lastId = db.mongoDB.lastOid(entity);
            lastId -=number.getValue();
            if (lastId<0) lastId=0;
            EntityList<Entity> xx;
            BasicDBObject query = new BasicDBObject(new BasicDBObject("oid", new BasicDBObject("$gte", lastId)));
            xx = (EntityList<Entity>)db.mongoDB.getAllByQuery(entity,query,plevel.getValue(),pathsList,statistic);
            ArrayList<DBRequest> out = new ArrayList<>();
            Gson gson = new Gson();
            int size = xx.size()-1;
            for(int i=0;i<size;i++)
                out.add(new DBRequest(xx.get(i),gson));
            return out;
        }};
    RouteWrap routeEntityUpdate = new RouteWrap() {      // Нельзя менять ТИП КИ
        @Override
        public Object _handle(Request req, Response res, RequestStatistic statistic) throws Exception {
            if (db.users.isReadOnly(req,res)) return null;
            ParamInt plevel = new ParamInt(req,res,"level",0);
            int level = plevel.isValid() ?  plevel.getValue() : 0;
            ParamBody qq = new ParamBody(req, res, DBRequest.class);
            if (!qq.isValid()) return null;
            DBRequest dbReq = (DBRequest)qq.getValue();
            Entity ent = dbReq.get(new Gson());
            db.mongoDB.update(ent,level);
            return new JEmpty();
        }};
    RouteWrap routeEntityGet = new RouteWrap() {
        @Override
        public Object _handle(Request req, Response res, RequestStatistic statistic) throws Exception {
            ParamLong id = new ParamLong(req,res,"id");
            if (!id.isValid()) return null;
            ParamInt plevel = new ParamInt(req,res,"level",0);
            int level = plevel.isValid() ?  plevel.getValue() : 0;
            ParamString cname = new ParamString(req,res,"classname","");
            String className = cname.isValid() ?  cname.getValue() : "";
            Class cc = Values.EntityFactory.getClassForSimpleName(className);
            if (cc==null){
                db.createHTTPError(res,Values.HTTPRequestError, "Недопустимый класс сущности "+className);
                return null;
                }
            ParamString paths = new ParamString(req,res,"paths","");
            String pathsList = paths.isValid() ?  paths.getValue() : "";
            Entity uu = (Entity) cc.newInstance();
            if (!db.mongoDB.getById(uu,id.getValue(),level,pathsList)){
                db.createHTTPError(res,Values.HTTPNotFound, className+" не найден id="+id.getValue());
                return null;
            }
            return new DBRequest(uu, new Gson());
            }
    };
    RouteWrap routeEntityRemove = new RouteWrap() {
        @Override
        public Object _handle(Request req, Response res, RequestStatistic statistic) throws Exception {
            if (db.users.isReadOnly(req,res)) return null;
            ParamLong id = new ParamLong(req,res,"id");
            if (!id.isValid()) return null;
            ParamString cname = new ParamString(req,res,"classname","");
            String className = cname.isValid() ?  cname.getValue() : "";
            Class cc = Values.EntityFactory.getClassForSimpleName(className);
            if (cc==null){
                db.createHTTPError(res,Values.HTTPRequestError, "Недопустимый класс сущности "+className);
                return null;
                }
            Entity uu = (Entity) cc.newInstance();
            boolean bb = db.mongoDB.delete(uu,id.getValue(),Values.DeleteMode);
            return new JBoolean(bb);
            }
        };
    public int getEntityNumber(String className) throws Exception {
        Class cc = Values.EntityFactory.getClassForSimpleName(className);
        if (cc==null)
            return -1;
        Entity uu = (Entity) cc.newInstance();
        BasicDBObject query = new BasicDBObject();
        query.put("valid", true);
        int num = db.mongoDB.getCountByQuery(uu,query);
        return num;
        }
    RouteWrap routeEntityNumber = new RouteWrap() {
        @Override
        public Object _handle(Request req, Response res, RequestStatistic statistic) throws Exception {
            ParamString cname = new ParamString(req,res,"classname","");
            String className = cname.isValid() ?  cname.getValue() : "";
            int num = getEntityNumber(className);
            if (num==-1){
                db.createHTTPError(res,Values.HTTPRequestError, "Недопустимый класс сущности "+className);
                return null;
                }
            return new JInt(num);
            }
        };
    //------------------------------------------------------------------------------------- 639
    RouteWrap routeEntityUpdateField = new RouteWrap() {      // Нельзя менять ТИП КИ
        @Override
        public Object _handle(Request req, Response res, RequestStatistic statistic) throws Exception {
            if (db.users.isReadOnly(req,res)) return null;
            ParamString pname = new ParamString(req,res,"name");
            if (!pname.isValid())
                return null;
            ParamString pref = new ParamString(req,res,"prefix","");
            if (!pref.isValid())
                return null;
            ParamBody qq = new ParamBody(req, res, DBRequest.class);
            if (!qq.isValid()) return null;
            DBRequest dbReq = (DBRequest)qq.getValue();
            Entity ent = dbReq.get(new Gson());
            String prefix = pref.getValue();
            if (prefix.length()==0){                                                // Явный префикс отсутствует
                String key = ent.getClass().getSimpleName()+"."+pname.getValue();   // Найти в таблице префиксов
                String zz = Values.PrefixMap.get(key);
                if (zz!=null)
                    prefix = zz+"_";
            }
            if (!db.mongoDB.updateField(ent,pname.getValue(),prefix)){
                db.createHTTPError(res,Values.HTTPNotFound, "Не найден объект "+ent.getClass().getSimpleName()+"["+ent.getOid()+"]");
                return null;
            }
            return new JEmpty();
        }};
    //-----------------------------------------------------------------------------------------------------------------
    RouteWrap routeNamesByPattern = new RouteWrap() {
        @Override
        public Object _handle(Request req, Response res, RequestStatistic statistic) throws Exception {
            ParamString entity = new ParamString(req, res, "entity");
            if (!entity.isValid()) return null;
            ParamString pattern = new ParamString(req, res, "pattern");
            if (!pattern.isValid()) return null;
            Class zz = Values.EntityFactory.get(entity.getValue());
            if (zz==null){
                db.createHTTPError(res,Values.HTTPRequestError, "Недопустимый класс сущности "+entity.getValue());
                return null;
                }
            Entity cc = (Entity) zz.newInstance();
            EntityList<EntityNamed> out = db.mongoDB.getListForPattern(cc,pattern.getValue());
            return out;
            }
        };


    RouteWrap apiPing = new RouteWrap() {
        @Override
        public Object _handle(Request req, Response res, RequestStatistic statistic) throws Exception {
            //db.createHTTPError(res,Values.HTTPRequestError, "Это ответ по-русски");
            return new JEmpty();
        }};
    RouteWrap apiWorkSettingsUpdate = new RouteWrap() {
        @Override
        public Object _handle(Request req, Response res, RequestStatistic statistic) throws Exception {
            if (db.users.isReadOnly(req,res)) return null;
            ParamBody qq = new ParamBody(req,res, WorkSettings.class);
            if (!qq.isValid())
                return null;
            synchronized (this){        // Синхронизация при обновлении объектов
                Entity ent = (Entity) qq.getValue();
                db.mongoDB.update(ent);
                System.out.println("Обновлены рабочие настройки");
                workSettings = (WorkSettings)ent;
                }
            return new JEmpty();
        }};

    RouteWrap apiConstAll = new RouteWrap() {
        @Override
        public Object _handle(Request req, Response res, RequestStatistic statistic) throws Exception {
            return db.constList;
        }};
    RouteWrap apiConstByGroups = new RouteWrap() {
        @Override
        public Object _handle(Request req, Response res, RequestStatistic statistic) throws Exception {
            ArrayList<ConstList> out = db.constList.getByGroups();
            return out;
        }};
    RouteWrap apiDebugToken= new RouteWrap(false) {
        @Override
        public Object _handle(Request req, Response res, RequestStatistic statistic) throws Exception {
            System.out.println("Отладочный токен сервера "+db.getDebugToken());
            return new JString(db.getDebugToken());
        }};
    RouteWrap apiVersion = new RouteWrap() {
        @Override
        public Object _handle(Request req, Response res, RequestStatistic statistic) throws Exception {
            WorkSettingsBase ws = getWorkSettings();
            if (ws==null) return null;
            System.out.println("Текущая версия клиента "+ws.getMKVersion());
            return new JString(ws.getMKVersion());
        }};
    public synchronized WorkSettings getWorkSettings() throws UniException {
        if (workSettings!=null)
            return workSettings;
        ArrayList<Entity> list = db.mongoDB.getAll(new WorkSettings(),Values.GetAllModeActual,1);
        if (list.size()==0){
            workSettings = new WorkSettings();
            db.mongoDB.clearTable("WorkSettings");
            db.mongoDB.add(workSettings);
            System.out.println("Созданы рабочие настройки");
            return workSettings;
            }
        else{
            System.out.println("Прочитаны рабочие настройки");
            workSettings = (WorkSettings) list.get(0);
            return workSettings;
            }

        }

    public synchronized void changeServerState(I_ChangeRecord todo) throws UniException {
        if (serverState==null)
            serverState = getServerState();
        if (todo.changeRecord(serverState))
            db.mongoDB.update(serverState);
            }
    public synchronized void addTimeStamp(long clock) {
        if (serverState==null)
        serverState = getServerState();
        serverState.addTimeStamp(clock);
        }

    public synchronized ServerState getServerStateRight() {
        return serverState==null ? new ServerState() : serverState;
        }
    public synchronized ServerState getServerState() {
        if (serverState==null) {
            serverState = new ServerState();
            serverState.setPid();
            try {
                ArrayList<Entity> list = db.mongoDB.getAll(new ServerState(), Values.GetAllModeActual, 1);
                if (list.size() == 0) {
                    db.mongoDB.add(serverState);
                    System.out.println("Созданы параметры сервера");
                } else {
                    serverState = (ServerState) list.get(0);
                    serverState.setPid();
                    }
                } catch (UniException ee){}
            }
        return serverState;
        }
    RouteWrap apiWorkSettings = new RouteWrap() {
        @Override
        public Object _handle(Request req, Response res, RequestStatistic statistic) throws Exception {
            WorkSettings ws = getWorkSettings();
            return ws;
        }};
    RouteWrap apiServerState = new RouteWrap() {
        @Override
        public Object _handle(Request req, Response res, RequestStatistic statistic) throws Exception {
            ServerState ws = getServerState();
            ws.setСashEnabled(db.mongoDB.isCashOn());
            ws.setTotalGetCount(db.mongoDB.getTotalGetCount());
            ws.setCashGetCount(db.mongoDB.getCashGetCount());       // Добавить, ТЕКУЩЕЕ
            return ws;
        }};
    RouteWrap apiSendBug = new RouteWrap() {
        @Override
        public Object _handle(Request req, Response res, RequestStatistic statistic) throws Exception {
            if (db.users.isReadOnly(req,res)) return null;
            Entity ent = addEntityHTTP(req,res,BugMessage.class);
            if (ent==null) return null;
            System.out.println("Передан баг: "+ent.toString());
            return new JLong(ent.getOid());
            }};
    public Object keepAlive(Request req, Response res,boolean timeStamp) throws Exception {
        String val = req.headers(Values.SessionHeaderName);
        if (val==null)
            return new JInt(0);       // Вернуть количество уведомлений, не прочитанных
        UserContext ctx = db.sessions.getContext(val);
        if (ctx==null){
            System.out.println("KeepAlive: no user context");
            return new JInt(0);
            }
        User uu = ctx.getUser();
        if (uu==null){
            System.out.println("KeepAlive: no user");
            return new JInt(0);
        }
        int count=0;
        int type = uu.getTypeId();
        count = db.notify.getNotificationCount(type,Values.NSSend,uu.getOid());
        System.out.println("KeepAlive: user "+uu.getTitle()+" ["+count+"]");
        if (timeStamp)
            ctx.wasCalled();                // Отметка времени !!!!!!!!!!!!!!!!
        return new JInt(count);
        }
    RouteWrap apiKeepAlive = new RouteWrap(false) {
        @Override
        public Object _handle(Request req, Response res, RequestStatistic statistic) throws Exception {
            return keepAlive(req,res,true);
            }};
    RouteWrap apiBugList = new RouteWrap() {
        @Override
        public Object _handle(Request req, Response res, RequestStatistic statistic) throws Exception {
            EntityList out = getEntityListHTTP(req,res, BugMessage.class);
            return out;
        }};
    RouteWrap routeBugGet = new RouteWrap() {
        @Override
        public Object _handle(Request req, Response res, RequestStatistic statistic) throws Exception {
            BugMessage out = (BugMessage) getEntityByIdHTTP(req,res,BugMessage.class);
            if (out==null)
                return null;
            if (out.getUserId().getOid()!=0) {
                if (!getEntityById(req,res, Artifact.class,out.getUserId(),0))
                    return null;
                }
            return out;
        }
    };

    RouteWrap apiDeleteById = new RouteWrap() {
        @Override
        public Object _handle(Request req, Response res, RequestStatistic statistic) throws Exception {
            if (db.users.isReadOnly(req,res)) return null;
            return deleteById(req,res,Values.DeleteMode);
            }};
    RouteWrap apiUndeleteById = new RouteWrap() {
        @Override
        public Object _handle(Request req, Response res, RequestStatistic statistic) throws Exception {
            if (db.users.isReadOnly(req,res)) return null;
            return deleteById(req,res,Values.UndeleteMode);
        }};

    public Object deleteById(Request req, Response res, boolean mode) throws Exception {
        ParamString entity = new ParamString(req,res,"entity");
        if (!entity.isValid()) return null;
        ParamLong id = new ParamLong(req,res,"id");
        if (!id.isValid()) return null;
        Entity cc=null;
        try {
            //-------------- По имени сущности ---------------------------------------------------------------------
            //cc = (Entity) Class.forName(entity.getValue()).newInstance();
            Class zz = Values.EntityFactory.get(entity.getValue());
            if (zz==null){
                db.createHTTPError(res,Values.HTTPRequestError, "Недопустимый класс сущности "+entity.getValue());
                }
            cc = (Entity) zz.newInstance();
            boolean bb = db.mongoDB.delete(cc,id.getValue(),mode);
            System.out.println((mode ? "Восстановлен" : "Удален")+" id="+id.getValue()+" "+bb);
            return new JBoolean(bb);
            } catch(Exception ee){
                db.createHTTPError(res,Values.HTTPRequestError, "Не могу создать объект "+entity+" "+ee.toString());
                return null;
                }
        }
    RouteWrap routeGetConsoleLog = new RouteWrap() {
        @Override
        public Object _handle(Request req, Response res, RequestStatistic statistic) throws Exception {
            ParamInt count = new ParamInt(req,res,"count");
            if (!count.isValid()) return null;
            StringList list =  db.consoleLog.getStrings(count.getValue());
            return list;
        }};
    //------------------- Общий код для операций list/get/add/update----------------------------
    public boolean getEntityListById(Request req, Response res, Class proto, EntityLinkList links) throws Exception {
        return getEntityListById(req,res,proto,links,0);
        }
    public boolean getEntityListById(Request req, Response res, Class proto, EntityLinkList links,int level) throws Exception {
        for(int i=0;i<links.size();i++){
            if (!db.common.getEntityById(req,res, proto,(EntityLink) links.get(i),level))
                return false;
            }
        return true;
        }
    public boolean getEntityById(Request req, Response res, Class proto, EntityLink link) throws Exception {
        return getEntityById(req,res,proto,link,0);
        }
    public boolean getEntityById(Request req, Response res, Class proto, EntityLink link, int level) throws Exception {
        Entity ent = getEntityById(req,res,proto,link.getOid(),level);
        if (ent==null)
            return false;
        link.setRef(ent);
        return true;
        }
    public int getLevel(Request req,Response res) throws Exception {
        ParamInt plevel = new ParamInt(req,res,"level",0);
        int level = plevel.isValid() ?  plevel.getValue() : 0;
        return level;
        }
    public Entity getEntityByIdHTTP(Request req, Response res, Class proto) throws Exception {
        ParamLong id = new ParamLong(req,res,"id");
        if (!id.isValid()) return null;
        ParamInt plevel = new ParamInt(req,res,"level",0);
        int level = plevel.isValid() ?  plevel.getValue() : 0;
        return getEntityById(req,res,proto, id.getValue(),level);
        }
    public Entity getEntityByIdHTTP(Request req, Response res, Class proto,int level) throws Exception {
        ParamLong id = new ParamLong(req,res,"id");
        if (!id.isValid()) return null;
        return getEntityById(req,res,proto, id.getValue(),level);
    }
    public Entity getEntityById(Request req, Response res, Class proto, long id) throws Exception {
        return getEntityById(req,res,proto,id,0);
        }
    public Entity getEntityByIdWithLevel(Request req, Response res, Class proto, long id) throws Exception {
        ParamInt plevel = new ParamInt(req,res,"level",0);
        int level = plevel.isValid() ?  plevel.getValue() : 0;
        return getEntityById(req,res,proto,id,level);
        }
    public Entity getEntityById(Request req, Response res, Class proto, int level) throws Exception {
        ParamLong id = new ParamLong(req,res,"id");
        if (!id.isValid()) return null;
        Entity uu = (Entity) proto.newInstance();
        if (!db.mongoDB.getById(uu,id.getValue(),level)){
            db.createHTTPError(res,Values.HTTPNotFound, proto.getSimpleName()+" не найден id="+id);
            return null;
            }
        return uu;
        }
    public Entity getEntityById(Request req, Response res, Class proto, long id,int level) throws Exception {
        Entity uu = (Entity) proto.newInstance();
        if (!db.mongoDB.getById(uu,id,level)){
            db.createHTTPError(res,Values.HTTPNotFound, proto.getSimpleName()+" не найден id="+id);
            return null;
            }
        return uu;
        }
    public Entity addEntityHTTP(Request req, Response res, Class proto) throws Exception {
        return addEntityHTTP(req,res,proto,null);
        }
    public Entity addEntityHTTP(Request req, Response res, Class proto,BeforeAction before) throws Exception {
        ParamInt plevel = new ParamInt(req,res,"level",0);
        int level = plevel.isValid() ?  plevel.getValue() : 0;
        ParamBody qq = new ParamBody(req, res, proto);
        if (!qq.isValid()) return null;
        Entity ent = (Entity) qq.getValue();
        if (before!=null)
            before.daAction(ent);
        db.mongoDB.add(ent,level);
        return ent;
        }
    public EntityList getEntityListHTTP(Request req, Response res, Class proto) throws Exception {
        ParamInt fl = new ParamInt(req,res,"mode",Values.GetAllModeActual);
        ParamInt plevel = new ParamInt(req,res,"level",0);
        int level = plevel.isValid() ?  plevel.getValue() : 0;
        EntityList<Entity> xx = (EntityList<Entity>)db.mongoDB.getAll((Entity) proto.newInstance(),fl.getValue(),level);
        return xx;
        }
    public JEmpty updateEntityHTTP(Request req, Response res, Class proto) throws Exception {
        return updateEntityHTTP(req,res,proto,null);
        }
    public JEmpty updateEntityHTTP(Request req, Response res, Class proto, BeforeAction beforeUpdate) throws Exception {
        long tt = System.currentTimeMillis();
        ParamInt plevel = new ParamInt(req,res,"level",0);
        int level = plevel.isValid() ?  plevel.getValue() : 0;
        ParamBody qq = new ParamBody(req,res, proto);
        if (!qq.isValid())
            return null;
        if (getEntityById(req,res,proto,qq.getOid(),0)==null)
            return null;
        Entity ent = (Entity) qq.getValue();
        if (beforeUpdate!=null)
            beforeUpdate.daAction(ent);
        db.mongoDB.update(ent,level);
        return new JEmpty();
        }
    RouteWrap apiGetBug = new RouteWrap() {
        @Override
        public Object _handle(Request req, Response res, RequestStatistic statistic) throws Exception {
            BugMessage out = (BugMessage) db.common.getEntityByIdHTTP(req,res,BugMessage.class);
            if (out==null)
                return null;
            if (out.getUserId().getOid()!=0) {
                if (!db.common.getEntityById(req,res, User.class,out.getUserId(),0))
                    return null;
                }
            return out;
            }
        };
    //-------------------------------------------------------------------------------------------
    RouteWrap apiHelpFileList = new RouteWrap(false) {
        @Override
        public Object _handle(Request req, Response res, RequestStatistic statistic) throws Exception {
            ParamString question = new ParamString(req,res,"question");
            if (!question.isValid()) return null;
            String qList = question.getValue();
            EntityList<Entity> helps = db.mongoDB.getAll(new HelpFile(),Values.GetAllModeActual,1);
            EntityList<Entity> out = new EntityList<>();
            for (Entity ent : helps){
                HelpFile helpFile = (HelpFile)ent;
                if (helpFile.isAllTagsPresent(qList))
                    out.add(ent);
                }
            return out;
        }};
    //---------------------------------------------------------------------------------------------
    class ArtifactResult{
        final Entity ent;
        final Artifact art;
        final Field field;
        public ArtifactResult(Entity ent, Artifact art, Field field) {
            this.ent = ent;
            this.art = art;
            this.field = field;
        }
    }
    private ArtifactResult common(Request req, Response res, boolean linkList) throws Exception{
        ParamLong id = new ParamLong(req,res,"id");
        if (!id.isValid())
            return null;
        ParamLong artid = new ParamLong(req,res,"artifactid");
        if (!artid.isValid())
            return null;
        ParamString className = new ParamString(req,res,"classname");
        if (!className.isValid())
            return null;
        ParamString fieldName = new ParamString(req,res,"fieldname");
        if (!fieldName.isValid())
            return null;
        Class cc = Values.EntityFactory.getClassForSimpleName(className.getValue());
        if (cc==null){
            db.createHTTPError(res,Values.HTTPRequestError, "Недопустимый класс сущности "+className);
            return null;
        }
        Entity uu = (Entity) cc.newInstance();
        if (!db.mongoDB.getById(uu,id.getValue(),1)){
            db.createHTTPError(res,Values.HTTPNotFound, className+" не найден id="+id);
            return null;
        }
        Field field = uu.getField(fieldName.getValue(), linkList ? DAO.dbLinkList : DAO.dbLink);
        if (field==null){
            db.createHTTPError(res,Values.HTTPNotFound, fieldName+" не найдено");
            return null;
        }
        Artifact art = new Artifact();
        if (!db.mongoDB.getById(art,artid.getValue())){
            db.createHTTPError(res,Values.HTTPNotFound, "Артефакт не найден id="+id);
            return null;
        }
        return new ArtifactResult(uu,art,field);
    }
    RouteWrap routeArtifactToList = new RouteWrap() {
        @Override
        public Object _handle(Request req, Response res, RequestStatistic statistic) throws Exception {
            if (db.users.isReadOnly(req,res)) return null;
            ArtifactResult pair = common(req,res,true);
            if (pair==null)
                return null;
            Artifact art = pair.art;
            Entity ent = pair.ent;
            art.setParent(ent);
            db.mongoDB.update(art);
            EntityLinkList list = (EntityLinkList)pair.field.get(ent);
            list.add(art.getOid());
            db.mongoDB.update(ent);
            return new JEmpty();
        }};
    RouteWrap routeArtifactFromList = new RouteWrap() {
        @Override
        public Object _handle(Request req, Response res, RequestStatistic statistic) throws Exception {
            ArtifactResult pair = common(req,res,true);
            if (pair==null)
                return null;
            Artifact art = pair.art;
            Entity ent = pair.ent;
            art.setParent(ent);
            db.mongoDB.update(art);
            EntityLinkList list = (EntityLinkList)pair.field.get(ent);
            if (!list.removeById(art.getOid())) {
                db.createHTTPError(res,Values.HTTPNotFound, "Артефакт отсутствует в списке id="+art.getOid());
                return null;
            }
            db.mongoDB.update(ent);
            db.files.deleteArtifactFile(art);
            db.mongoDB.remove(art);
            return new JEmpty();
        }};
    RouteWrap routeArtifactReplace = new RouteWrap() {
        @Override
        public Object _handle(Request req, Response res, RequestStatistic statistic) throws Exception {
            if (db.users.isReadOnly(req,res)) return null;
            ArtifactResult pair = common(req,res,false);
            if (pair==null)
                return null;
            Artifact art = pair.art;
            Entity ent = pair.ent;
            EntityLink list = (EntityLink)pair.field.get(ent);
            if (list.getOid()!=0){
                System.out.println("Replace artifact "+list.getOid()+"->"+art.getOid());
                Artifact old = new Artifact();
                db.mongoDB.getById(old,list.getOid());
                db.files.deleteArtifactFile(old);
                db.mongoDB.remove(old);             // Удалить старый артефакт и файл
                }
            art.setParent(ent);
            db.mongoDB.update(art);
            list.setOid(art.getOid());
            db.mongoDB.update(ent);
            return new JEmpty();
        }};

}
