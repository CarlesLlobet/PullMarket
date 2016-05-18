package xyz.carlesllobet.pullmarket.UI;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import xyz.carlesllobet.pullmarket.DB.UserFunctions;
import xyz.carlesllobet.pullmarket.Domain.Llista;
import xyz.carlesllobet.pullmarket.R;

public class PreuActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageButton confirmar;
    private ImageButton cancelar;
    private TextView preu;

    private Toolbar toolbar;

    private ProgressDialog pDialog;

    private static String KEY_SUCCESS = "success";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_preu);

        preu = (TextView) findViewById(R.id.preu);

        confirmar = (ImageButton) findViewById(R.id.confirmar);
        cancelar = (ImageButton) findViewById(R.id.cancelar);

        confirmar.setOnClickListener(this);
        cancelar.setOnClickListener(this);

        preu.setText(Llista.getInstance().getPreuTotal().toString());

        toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);

        setTitle(R.string.tituloLogin);

    }

    @Override
     public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirmar:
                showProgress(true);

                EnviarCompra AsyncSend = new EnviarCompra();
                AsyncSend.execute();

                showProgress(false);
                Toast.makeText(this, "Compra enviada", Toast.LENGTH_SHORT).show();
                Llista.getInstance().borrarLlista();
                borrarLlista();
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                break;
            case R.id.cancelar:
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                break;
        }
    }

    private void showProgress(final boolean show) {
        if (show) {
            pDialog = new ProgressDialog(PreuActivity.this);
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setMessage("Procesando...");
            pDialog.setCancelable(true);
            pDialog.setMax(100);

            pDialog.setProgress(0);
            pDialog.show();
        } else {
            pDialog.dismiss();
        }
    }

    private boolean borrarLlista(){
        boolean borrada = false;
        File list = new File(android.os.Environment.getExternalStorageDirectory(),File.separator+"beam"+File.separator+"List.csv");
        if (list.exists()){
            borrada = list.delete();
        }
        return borrada;
    }

    public class EnviarCompra extends AsyncTask<String, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... params) {
            UserFunctions userFunction = new UserFunctions();
            if (params.length != 0) return null;

            JSONObject json = userFunction.enviarCompra(getApplicationContext());

            return json;

        }

        @Override
        protected void onPostExecute(JSONObject json) {
            //super.onPostExecute(logged);
            //your stuff
            //you can pass params, launch a new Intent, a Toast...

            // check for login response
            try {
                if (json != null && json.getString(KEY_SUCCESS) != null) {
                    if (json.getString(KEY_SUCCESS).equals("1")) {
                        JSONObject json_user = json.getJSONObject("compra");
                    }
                } else {
                    new AlertDialog.Builder(PreuActivity.this)
                            .setTitle(R.string.error)
                            .setMessage(R.string.loginFail)
                            .setPositiveButton(R.string.btnRetry, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
