package xyz.carlesllobet.pullmarket.UI;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import xyz.carlesllobet.pullmarket.DB.UserFunctions;
import xyz.carlesllobet.pullmarket.Domain.Llista;
import xyz.carlesllobet.pullmarket.Domain.Product;
import xyz.carlesllobet.pullmarket.Domain.RecyclerItemClickListener;
import xyz.carlesllobet.pullmarket.R;

/**
 * Created by CarlesLlobet on 26/01/2016.
 */
public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    // A File object containing the path to the transferred files
    private File folder;
    // Incoming Intent
    private Intent mIntent;

    private ImageButton confirmar,cancelar;

    private TextView ajuda;

    private LinearLayout botons;

    NfcAdapter mAdapter;
    ProgressDialog pDialog;

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayout;

    public static final String MIME_TEXT_PLAIN = "text/plain";
    public static final String TAG = "NfcDemo";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setTitle(R.string.tituloHome);

        mRecyclerView = (RecyclerView) findViewById(R.id.mRecyclerView);

        mLinearLayout = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(mLinearLayout);

        final HomeAdapter adapter = new HomeAdapter();

        mRecyclerView.setAdapter(adapter);

        botons = (LinearLayout) findViewById(R.id.botons);
        ajuda = (TextView) findViewById(R.id.textView10);

        if (Llista.getInstance().getAllProducts().isEmpty()){
            botons.setVisibility(View.INVISIBLE);
            ajuda.setVisibility(View.VISIBLE);
        }
        else{
            botons.setVisibility(View.VISIBLE);
            ajuda.setVisibility(View.INVISIBLE);
        }

        confirmar = (ImageButton) findViewById(R.id.confirmar);
        cancelar = (ImageButton) findViewById(R.id.cancelar);

        confirmar.setOnClickListener(this);
        cancelar.setOnClickListener(this);

        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Llista list = Llista.getInstance();
                        list.borraUn(position);
                        borrarLlista();
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    }
                })
        );

        //Escoltar NFC
        mAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mAdapter == null) {
            Toast.makeText(this, "Your device does not support NFC", Toast.LENGTH_LONG).show();
            return;
        }

        if (!mAdapter.isEnabled()) {
            Toast.makeText(this, "NFC is disabled", Toast.LENGTH_LONG).show();
        }

        Llista list = Llista.getInstance();
        if (list.getAllProducts().isEmpty()) {
            llegirProductes();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirmar:
                startActivity(new Intent(getApplicationContext(), PreuActivity.class));
                break;
            case R.id.cancelar:
                Llista.getInstance().borrarLlista();
                borrarLlista();
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        //Reescoltar NFC
        if (mAdapter != null) {
            if (!mAdapter.isEnabled()) {
                Log.d("NFC", "Your NFC is not enabled");
                return;
            }
            setupForegroundDispatch(this, mAdapter);
        }
    }

    @Override
    protected void onPause() {
        if (mAdapter != null) {
            stopForegroundDispatch(this, mAdapter);
        }
        super.onPause();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        /**
         * This method gets called, when a new Intent gets associated with the current activity instance.
         * Instead of creating a new activity, onNewIntent will be called. For more information have a look
         * at the documentation.
         *
         * In our case this method gets called, when the user attaches a Tag to the device.
         */
        llegirProductes();
    }

    private void showProgress(final boolean show) {
        if (show) {
            pDialog = new ProgressDialog(HomeActivity.this);
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

    public static void setupForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        final Intent intent = new Intent(activity.getApplicationContext(), activity.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        final PendingIntent pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(), 0, intent, 0);

        IntentFilter[] filters = new IntentFilter[1];
        String[][] techList = new String[][]{};

        // Notice that this is the same filter as in our manifest.
        filters[0] = new IntentFilter();
        filters[0].addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filters[0].addCategory(Intent.CATEGORY_DEFAULT);
        try {
            filters[0].addDataType(MIME_TEXT_PLAIN);
        } catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("Check your mime type.");
        }

        adapter.enableForegroundDispatch(activity, pendingIntent, filters, techList);
    }

    /**
     * @param activity The corresponding Activity requesting to stop the foreground dispatch.
     * @param adapter  The {@link NfcAdapter} used for the foreground dispatch.
     */
    public static void stopForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        adapter.disableForegroundDispatch(activity);
    }


    @Override
    public void onBackPressed() {
            finish();
    }



    public void llegirProductes(){
        Log.d("List","Llegint productes");
        // Get the Intent action
        mIntent = getIntent();
        String action = mIntent.getAction();
        /*
         * For ACTION_VIEW, the Activity is being asked to display data.
         * Get the URI.
         */

        if (TextUtils.equals(action, Intent.ACTION_VIEW)) {
            // Get the URI from the Intent
            Uri beamUri = mIntent.getData();
            /*
             * Test for the type of URI, by getting its scheme value
             */
            if (TextUtils.equals(beamUri.getScheme(), "file")) {
                folder = new File(beamUri.getPath());
            } /*else if (TextUtils.equals(
                    beamUri.getScheme(), "content")) {
                mParentPath = handleContentUri(beamUri);
            }*/
        }

        folder = new File(android.os.Environment.getExternalStorageDirectory(),File.separator+"beam"+File.separator+"List.csv");
        if (folder.exists()){
            Log.d("List:","folder exists");
            if (folder.canRead()) {
                try {
                    FileInputStream fis = new FileInputStream(folder);
                    InputStreamReader isr = new InputStreamReader(fis);
                    BufferedReader bufferedReader = new BufferedReader(isr);
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    try {
                        while ((line = bufferedReader.readLine()) != null) {
                            sb.append(line);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                        String[] productes = (sb.toString()).split(",");
                        UserFunctions userFunctions = new UserFunctions();
                        userFunctions.setComprador(getApplicationContext(), productes[0]);
                        for (int i = 1; i < productes.length; ++i) {
                            Product nou = userFunctions.getProduct(getApplicationContext(), Long.valueOf(productes[i]));
                            if (nou == null) {
                                userFunctions.updateAllProducts(getApplicationContext());
                            } else {
                                Llista list = Llista.getInstance();
                                list.addProduct(nou);
                            }
                        }
                        recreate();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
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
}
