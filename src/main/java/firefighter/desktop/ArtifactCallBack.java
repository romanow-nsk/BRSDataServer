package firefighter.desktop;

import firefighter.core.Utils;
import firefighter.core.entity.artifacts.Artifact;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class ArtifactCallBack implements Callback<Artifact> {
    public void onSuccess(Response<Artifact> response){
        System.out.println("Асинхронный запрос записи файла "+response.body()); }
    @Override
    public void onResponse(Call<Artifact> call, Response<Artifact> response) {
        if (response.isSuccessful()){
            onSuccess(response); }
        else{
            System.out.println("Асинхронный запрос записи файла  "+ Utils.httpError(response));
            }}
    @Override
    public void onFailure(Call<Artifact> call, Throwable t) {
        System.out.println(t.toString());
        }
    }
