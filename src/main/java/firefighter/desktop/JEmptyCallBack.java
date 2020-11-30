package firefighter.desktop;

import firefighter.core.Utils;
import firefighter.core.entity.baseentityes.JEmpty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JEmptyCallBack implements Callback<JEmpty> {
    public void onSuccess(Response<JEmpty> response){
        System.out.println("ОK: Асинхронный ответ "+response.body()); }
    @Override
    public void onResponse(Call<JEmpty> call, Response<JEmpty> response) {
        if (response.isSuccessful()){
            onSuccess(response);
        }
        else{
            System.out.println("Ошибка: Асинхронный ответ  "+Utils.httpError(response));
        }
    }
    @Override
    public void onFailure(Call<JEmpty> call, Throwable t) {
        System.out.println(t.toString());
    }
}