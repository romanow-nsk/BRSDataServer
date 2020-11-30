package firefighter.dataserver;

import firefighter.core.UniException;
import firefighter.core.constants.Values;
import firefighter.core.constants.ValuesBase;
import firefighter.core.entity.Entity;
import firefighter.core.entity.EntityLink;
import firefighter.core.entity.EntityList;
import firefighter.core.entity.artifacts.Artifact;
import firefighter.core.entity.base.BugMessage;
import firefighter.core.entity.baseentityes.JEmpty;
import firefighter.core.entity.baseentityes.JLong;
import firefighter.core.entity.users.Account;
import firefighter.core.entity.users.User;
import firefighter.core.mongo.RequestStatistic;
import spark.Request;
import spark.Response;
import spark.Spark;

import java.util.ArrayList;

public class APIUser extends APIBase{
    public boolean addUser(User tt) throws UniException {
        if (!db.mongoDB.isOpen())
            return false;
        db.mongoDB.add(tt);
        return true;
        }
    public APIUser(DataServer db0) {
        super(db0);
        //------------------------------------------------- Таблица API ------------------------------------------------
        // localhost:4567/user/login?phone=9139449081
        spark.Spark.get("/api/user/login/phone",apiUserLogin);
        spark.Spark.post("/api/user/login",apiUserLoginBody);
        spark.Spark.get("/api/user/logoff",apiLogoff);
        //------------------------- стандарт -------------------------
        spark.Spark.get("/api/user/list",apiUserList);
        spark.Spark.get("/api/user/get",apiGetUserById);
        spark.Spark.post("/api/user/add",apiAddUser);
        spark.Spark.post("/api/user/update",apiUserUpdate);
        spark.Spark.get("/api/user/account/get",apiUserAccount);
        }
    //-------------------------------------------------------------------------------------
    public void sendSecurityMessage(String message, Request req) throws UniException {
        String ss = req.queryString();
        ss = "<-----"+req.ip()+req.pathInfo()+" "+req.requestMethod()+" "+(ss == null ? "" : ss);
        System.out.println(ss);
        db.mongoDB.add(new BugMessage(message+" "+ss));
        }
    public boolean isReadOnly(Request req, Response res) throws Exception{
        UserContext ctx = db.getSession(req,res);
        User user = ctx.getUser();
        if (user.getTypeId()== Values.UserGuestType){
            db.createHTTPError(res,Values.HTTPRequestError, "Отсутсвуют права на выполнение операций");
            return true;
        }
        return false;
        }
    public boolean isOnlyForSuperAdmin(Request req, Response res) throws Exception{
        ParamString pass = new ParamString(req, res, "pass");
        if (!pass.isValid()) return false;
        if (!pass.getValue().equals(Values.DebugTokenPass)) {
            sendSecurityMessage("Illegal debug pass", req);
            db.createHTTPError(res,Values.HTTPAuthorization, "Недопустимый пароль операции");
            return false;
        }
        UserContext ctx = db.getSession(req,res);
        User user = ctx.getUser();
        if (user.getTypeId()!=Values.UserSuperAdminType){
            db.createHTTPError(res,Values.HTTPRequestError, "Операция только для суперадминистратора");
            return false;
        }
        return true;
    }

    //-------------------------------------------------------------------------------------
    RouteWrap apiLogoff = new RouteWrap() {
        @Override
        public Object _handle(Request req, Response res, RequestStatistic statistic) throws Exception {
            String val = req.headers(Values.SessionHeaderName);
            if (val==null) return new JEmpty();
            db.closeSession(val);
            res.header("Access-Control-Allow-Origin","*");
            return new JEmpty();
            }};
    RouteWrap apiUserLoginBody = new RouteWrap(false) {
        @Override
        public Object _handle(Request req, Response res, RequestStatistic statistic) throws Exception {
            ParamBody pp = new ParamBody(req,res,Account.class);
            if (!pp.isValid()) return null;
            User uu = procLogin((Account) pp.getValue(),res);
            return uu;
            }};
    RouteWrap apiAddUser = new RouteWrap() {
        @Override
        public Object _handle(Request req, Response res, RequestStatistic statistic) throws Exception {
            if (!db.isPrivilegedUser(req,res)){
                db.createHTTPError(res,Values.HTTPNotFound, "Нет прав для выполнения операции");
                return null;
                }
            ParamBody qq = new ParamBody(req,res,User.class);
            if (!qq.isValid()) return null;
            User pp =(User)qq.getValue();
            Account account = pp.getAccount();
            db.mongoDB.add(account);
            pp.getAccountData().setOid(account.getOid());
            db.mongoDB.add(pp);
            return new JLong(pp.getOid());
        }};
    RouteWrap apiUserUpdate = new RouteWrap() {
        @Override
        public Object _handle(Request req, Response res, RequestStatistic statistic) throws Exception {
            ParamBody qq = new ParamBody(req,res, User.class);
            if (!qq.isValid())
                return null;
            User uu = (User)qq.getValue();
            if (!db.isSomeUser(req,res,uu)){
                db.createHTTPError(res,Values.HTTPNotFound, "Нет прав для изменения");
                return null;
                }
            if (uu.getAccountData().getRef()!=null)
                db.mongoDB.update(uu.getAccountData().getRef());
            return db.common.updateEntityHTTP(req,res, User.class);
        }};
    RouteWrap apiUserLogin = new RouteWrap(false) {
        @Override
        public Object _handle(Request req, Response res, RequestStatistic statistic) throws Exception {
            ParamString phone = new ParamString(req,res,"phone");
            if (!phone.isValid()) return null;
            ParamString pass = new ParamString(req,res,"pass");
            if (!pass.isValid()) return null;
            User out = procLogin(new Account(phone.getValue(),phone.getValue(),pass.getValue()),res);
            return out;
            }};
    public User getUserById(Request req, Response res) throws Exception {
        User uu = (User)db.common.getEntityByIdHTTP(req,res,User.class);
        if (uu==null)
            return null;
        return uu;
        }
    RouteWrap apiGetUserById = new RouteWrap() {
        @Override
        public Object _handle(Request req, Response res, RequestStatistic statistic) throws Exception {
            User uu = getUserById(req,res);
            if (uu==null)
                return null;
            if (!db.isSomeUser(req,res,uu)){
                uu.setAccount(new Account());
                uu.setSessionToken("");
                }
            else{
                Account account = new Account();
                db.mongoDB.getById(account,uu.getAccountData().getOid());
                uu.getAccountData().setOidRef(account);
                }
            return uu;
            }};
    RouteWrap apiUserList = new RouteWrap() {
        @Override
        public Object _handle(Request req, Response res, RequestStatistic statistic) throws Exception {
            EntityList out = db.common.getEntityListHTTP(req,res,User.class);
            if (out==null)
                return null;
                for(Object ent : out){                          // Очистить Account
                    User user = (User)ent;
                    if (!db.isPrivilegedUser(req,res)){
                        user.setAccount(new Account());
                        user.setSessionToken("");
                        }
                    else{
                        if (user.getAccountData().getOid()!=0){
                            Account account = new Account();
                            db.mongoDB.getById(account,user.getAccountData().getOid());
                            user.getAccountData().setOidRef(account);
                            }
                        }
                }
            return out;
            }};
    private final static int loginNext=0;
    private final static int loginBad=-1;
    private final static int loginOK=1;
    private int procLogin (User dbUser, Account user,Response res, boolean su) throws Exception{
        boolean ph = user.loginPhoneValid();
        Account account=new Account();
        if (!su){      // НЕ SU
            if (!db.mongoDB.getById(account,dbUser.getAccountData().getOid()))
                return loginNext;
        }
        else{
            account = dbUser.getAccount();          // Для SU
        }
        boolean accountFound=false;
        if (ph){
            if (account.getLoginPhone().equals(user.getLoginPhone()))
                accountFound=true;
        }
        else{                               // 638
            if (account.getLogin().equals(user.getLogin()))
                accountFound=true;
        }
        if (!accountFound)
            return loginNext;
        if (!account.getPassword().equals(user.getPassword())){
            db.createHTTPError(res,Values.HTTPNotFound, "Недопустимый пароль");         // ПРОБЛЕМА С КИРИЛЛИЦЕЙ  - в запросе UTF8
            return loginBad;
        }
        if (db.getServerState().isLocked() && dbUser.getTypeId()!=Values.UserSuperAdminType){
            db.createHTTPError(res,Values.HTTPAuthorization, "Заблокировано суперадмином");
            return loginBad;
        }
        EntityLink<Artifact> art = dbUser.getPhoto();
        if (art.getOid()!=0){
            art.setRef(new Artifact());
            if (!db.mongoDB.getById(art.getRef(),art.getOid())){
                art.setOid(0);
            }
        }
        return loginOK;
    }
    private User procLogin(Account user,Response res) throws Exception {
        boolean success=false;
        int done = procLogin(ValuesBase.env().superUser(),user,res,true);
        if (done==loginBad)
            return null;
        if (done==loginOK){
            User out = new User();
            out.setTypeId(Values.UserSuperAdminType);
            String token = db.createSessionToken(out);
            out.setSessionToken(token);
            res.raw().addHeader(Values.SessionHeaderName,token);
            return out;
        }
        ArrayList<Entity> xx = db.mongoDB.getAll(new User());
        for(Entity vv : xx){
            User uu = (User)vv;
            done = procLogin((User)vv,user,res,false);
            if (done==loginBad)
                return null;
            if (done==loginOK){
                String token = db.createSessionToken(uu);
                uu.setSessionToken(token);
                res.raw().addHeader(Values.SessionHeaderName,token);
                return uu;
                }
            }
        db.createHTTPError(res,Values.HTTPNotFound, "Недопустимый логин");         // ПРОБЛЕМА С КИРИЛЛИЦЕЙ  - в запросе UTF8
        return null;
        }
    RouteWrap apiUserAccount = new RouteWrap() {
        @Override
        public Object _handle(Request req, Response res, RequestStatistic statistic) throws Exception {
            User user = db.getSession(req,res).getUser();
            if (user.getTypeId()==Values.UserSuperAdminType)
                return Values.superUser.getAccount();
            Account account = new Account();
            if (!db.mongoDB.getById(account,user.getAccountData().getOid())){
                db.createHTTPError(res,Values.HTTPAuthorization, "Недоступны данные аккаунта");
                return null;
                }
            return account;
            }};
}
