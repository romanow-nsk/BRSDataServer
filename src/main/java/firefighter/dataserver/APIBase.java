package firefighter.dataserver;

import firefighter.core.ServerState;
import firefighter.core.UniException;
import firefighter.core.Utils;
import firefighter.core.constants.Values;
import firefighter.core.constants.ValuesBase;
import firefighter.core.entity.base.BugMessage;
import firefighter.core.mongo.RequestStatistic;
import spark.Request;
import spark.Response;
import spark.Route;

public class APIBase<T extends DataServer> {
    protected T db;
    public APIBase(T db0){
        db = db0;
    }
    public void set(T db0) {
        db = db0;
    }
    protected abstract class RouteWrap implements Route {
        private boolean testToken=true;
        public RouteWrap(){}
        public RouteWrap(boolean testTokenMode){
            testToken = testTokenMode;
        }
        public abstract Object _handle(Request req, Response res, RequestStatistic statistic) throws Exception;
        @Override
        public Object handle(Request req, Response res){
            ServerState state = db.getServerState();
            try {
                long tt = db.canDo(req,res,testToken);
                if(tt==0) return res.body();
                state.incRequestNum();                    //++ операций
                RequestStatistic statistic = new RequestStatistic();
                statistic.startTime = tt;
                Object out = _handle(req,res, statistic);
                state.decRequestNum();
                return out==null ? res.body() : db.toJSON(out, req, statistic);
            } catch (Exception ee){
                state.decRequestNum();
                String mes = Utils.createFatalMessage(ee)+"\n"+db.traceRequest(req);
                System.out.println(mes);
                db.sendBug("Сервер",mes);
                try {
                    db.mongoDB.add(new BugMessage(mes));
                    } catch (UniException e) {}
                db.createHTTPError(res, Values.HTTPException, mes);
                return res.body();
            }
        }
    }

}
