package firefighter.desktop;

import firefighter.core.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CallBackWithGet<T> implements Callback<T> {
    private EntityPanel panel;
    public CallBackWithGet(EntityPanel pp){
        panel = pp;
        }
    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.isSuccessful())
            panel.getById();
        else
            System.out.println("Ошибка:  "+ Utils.httpError(response));
    }
    @Override
    public void onFailure(Call<T> call, Throwable t) {
        System.out.println("Ошибка: "+t.toString());
    }
}
