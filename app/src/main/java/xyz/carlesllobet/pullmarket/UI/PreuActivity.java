package xyz.carlesllobet.pullmarket.UI;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import xyz.carlesllobet.pullmarket.Domain.Llista;
import xyz.carlesllobet.pullmarket.R;

public class PreuActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageButton confirmar;
    private ImageButton cancelar;
    private TextView preu;

    private Toolbar toolbar;

    private ProgressDialog pDialog;

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
                //Enviar compra a la DB
                showProgress(false);
                Toast.makeText(this, "Compra enviada", Toast.LENGTH_LONG).show();
                Llista.getInstance().borrarLlista();
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                break;
            case R.id.cancelar:
                Llista.getInstance().borrarLlista();
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
}
