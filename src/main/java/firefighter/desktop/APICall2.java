package firefighter.desktop;

import firefighter.core.UniException;
import firefighter.core.Utils;
import firefighter.core.constants.Values;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;

public abstract class APICall2<T> {
    public abstract Call<T> apiFun();
    public APICall2(){}
    public T call(MainBaseFrame base)throws UniException {
        String mes;
        Response<T> res;
        long tt;
        try {
            tt = System.currentTimeMillis();
            res = apiFun().execute();
            } catch (Exception ex) {
                throw UniException.bug(ex);
                }
            if (!res.isSuccessful()){
                if (res.code()== Values.HTTPAuthorization){
                    mes =  "Сеанс закрыт " + Utils.httpError(res);
                    System.out.println(mes);
                    base.logOff();
                    throw UniException.io(mes);
                    }
                try {
                    mes = "Ошибка " + res.message() + " (" + res.code() + ") " + res.errorBody().string();
                    } catch (IOException ex){ mes=ex.toString(); }
                System.out.println(mes);
                throw UniException.io(mes);
                }
            //System.out.println("time="+(System.currentTimeMillis()-tt)+" мс");
            return res.body();
            }
    }

