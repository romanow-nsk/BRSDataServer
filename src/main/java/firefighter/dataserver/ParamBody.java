package firefighter.dataserver;

import com.google.gson.Gson;
import firefighter.core.constants.Values;
import firefighter.core.constants.ValuesBase;
import firefighter.core.mongo.DAO;
import firefighter.core.entity.Entity;
import spark.Request;
import spark.Response;

import java.io.IOException;

public class ParamBody {
    private boolean valid=false;
    private DAO  value=null;
    public DAO getValue(){ return  value; }
    public boolean isValid(){ return valid; }
    public long getOid(){ return ((Entity)value).getOid(); }
    public ParamBody(Request req, Response res, Class clazz) throws IOException {
        try{
            Gson gson = new Gson();
            value = (DAO)gson.fromJson(req.body(),clazz);
            valid = value!=null;
            } catch(Exception ee){
                DataServer.funCreateHTTPError(res, Values.HTTPRequestError, "Ошибка формата класса: "+clazz.getSimpleName());
                }
    }
}
