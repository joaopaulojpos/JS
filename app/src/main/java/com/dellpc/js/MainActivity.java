package com.dellpc.js;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.dellpc.js.apiclima.ClimaapiService;
import com.dellpc.js.models.Clima;
import com.dellpc.js.models.ClimaResposta;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "CLIMEX";

    private Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
        enviar.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NotificationActivity.class);
                intent.putExtra("mensagem", msg.getText().toString());
                int id = (int) (Math.random()*1000);
                PendingIntent pi = PendingIntent.getActivity(getBaseContext(), id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                Notification notification = new Notification.Builder(getBaseContext())
                        .setContentTitle("De: Jaime APP")
                        .setContentText(msg.getText()).setSmallIcon(R.drawable.notifi)
                        .setContentIntent(pi).build();
                NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                notification.flags |= Notification.FLAG_AUTO_CANCEL;

                notificationManager.notify(id, notification);
            }
        });
        */

        retrofit = new Retrofit.Builder()
                .baseUrl("https://api.hgbrasil.com/weather/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        obterDados();
    }

    public void obterDados(){
        ClimaapiService service = retrofit.create(ClimaapiService.class);
        Call<ClimaResposta> climaRespostaCall = service.obterListaClimas();

        climaRespostaCall.enqueue(new Callback<ClimaResposta>() {
            @Override
            public void onResponse(Call<ClimaResposta> call, Response<ClimaResposta> response) {
                if (response.isSuccessful()){

                    ClimaResposta climaResposta = response.body();
                    Clima listaClima = climaResposta.getResults();

                    String data = listaClima.getDate();
                    String situacao = listaClima.getCurrently();
                    String descricao = listaClima.getDescription();
                    String condicao_tempo = listaClima.getCondition_slug();

                    if ( listaClima.getCurrently().equals("noite")){
                        Log.i(TAG, " Clima " + listaClima.getDescription());
                      //  Log.i(TAG, " Data " + listaClima.getDate());
                        Log.i(TAG, " Situação " + listaClima.getCurrently());
                        Log.i(TAG, "Clima " + listaClima.getCondition_slug());
                        Log.i(TAG, "ENTROU NO IF");
                    }
                    gerarNotificacao(listaClima.getCondition_slug());

                    /**
                    for (int i = 0; i < listaClima.size(); i++){
                        Clima c = listaClima.get(i);
                        Log.i(TAG, " Clima: " + c.getDescription());
                    }
                     */


                }else {
                    Log.e(TAG," onResponse " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<ClimaResposta> call, Throwable t) {
                Log.e(TAG, " onFailure " + t.getMessage());
            }
        });
    }

    /**
     * Método de notificação
     *
     */
    public void gerarNotificacao(String condicao){

        String condicao_tempo = condicao;

        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        PendingIntent p = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setTicker("Ticker Texto");
        builder.setContentTitle("Aviso de Clima - Jaime App");
        builder.setSmallIcon(R.mipmap.ic_launcher);

        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.notifi));
        builder.setContentIntent(p);

        NotificationCompat.InboxStyle style = new NotificationCompat.InboxStyle();
        if (condicao.equals("cloudly_night") || condicao.equals("cloudly_day")){
            String [] descs = new String[]{" OPS, a previsão hoje é de Tempo","Parcialmente Nublado Previna-se, ", " e leve consigo um guarda chuva"};
            for(int i = 0; i < descs.length; i++){
                style.addLine(descs[i]);
            }
        } else if (condicao.equals("cloud")) {
            String[] descs = new String[]{" Olá, a previsão hoje é de Tempo Nublado ", "Se eu você, levaria um guarda chuva"};
            for (int i = 0; i < descs.length; i++) {
                style.addLine(descs[i]);
            }
        }else if (condicao.equals("storm")){
            String[] descs = new String[]{" Olá, a previsão hoje é de Tempestades então", " prepare sua capa ou seu guarda chuva e", " tome muito cuidado !"};
            for (int i = 0; i < descs.length; i++) {
                style.addLine(descs[i]);
            }
        }else if (condicao.equals("rain")){
            String[] descs = new String[]{" Olá, a previsão hoje é de muita chuva, tome muito cuidado", "por onde anda de preferência fique em casa. "};
            for (int i = 0; i < descs.length; i++) {
                style.addLine(descs[i]);
            }
        } else if (condicao.equals("clear_night") || condicao.equals("clear_day")){
            String[] descs = new String[]{" Olá, a previsão hoje é de Céu de limpo ", "então aproveita e vai explorar os lugares ", "conheçer coisas novas, Divirta-se. "};
            for (int i = 0; i < descs.length; i++) {
                style.addLine(descs[i]);
            }
        }
        builder.setStyle(style);

        Notification n = builder.build();
        n.flags = Notification.FLAG_AUTO_CANCEL;
        nm.notify(R.drawable.notifi, n);

        try{
            Uri som = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone toque = RingtoneManager.getRingtone(this, som);
            toque.play();
        }
        catch(Exception e){}
    }
}
