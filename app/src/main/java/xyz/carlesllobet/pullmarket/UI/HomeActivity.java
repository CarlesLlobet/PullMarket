package xyz.carlesllobet.pullmarket.UI;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

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

    private UserFunctions uf;

    private ImageButton confirmar,cancelar;

    private LinearLayout botons;

    NfcAdapter mAdapter;
    PendingIntent mPendingIntent;
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

        uf = new UserFunctions();

        botons = (LinearLayout) findViewById(R.id.botons);

        botons.setVisibility(View.INVISIBLE);

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

        handleIntent(getIntent());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirmar:
                startActivity(new Intent(getApplicationContext(), PreuActivity.class));
                break;
            case R.id.cancelar:
                Llista.getInstance().borrarLlista();
                botons.setVisibility(View.INVISIBLE);
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
        handleIntent(intent);
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

    private void handleIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {

            String type = intent.getType();
            if (MIME_TEXT_PLAIN.equals(type)) {

                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                new NdefReaderTask().execute(tag);

            } else {
                Log.d(TAG, "Wrong mime type: " + type);
            }
        } else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {

            // In case we would still use the Tech Discovered Intent
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            String[] techList = tag.getTechList();
            String searchedTech = Ndef.class.getName();

            for (String tech : techList) {
                if (searchedTech.equals(tech)) {
                    new NdefReaderTask().execute(tag);
                    break;
                }
            }
        }
    }


    @Override
    public void onBackPressed() {
            finish();
    }

    private class NdefReaderTask extends AsyncTask<Tag, Void, String> {

        @Override
        protected String doInBackground(Tag... params) {
            Tag tag = params[0];

            Ndef ndef = Ndef.get(tag);
            if (ndef == null) {
                // NDEF is not supported by this Tag.
                return null;
            }

            NdefMessage ndefMessage = ndef.getCachedNdefMessage();

            NdefRecord[] records = ndefMessage.getRecords();
            for (NdefRecord ndefRecord : records) {
                if (ndefRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN && Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT)) {
                    try {
                        return readText(ndefRecord);
                    } catch (UnsupportedEncodingException e) {
                        Log.e(TAG, "Unsupported Encoding", e);
                    }
                }
            }

            return null;
        }

        private String readText(NdefRecord record) throws UnsupportedEncodingException {
        /*
         * See NFC forum specification for "Text Record Type Definition" at 3.2.1
         *
         * http://www.nfc-forum.org/specs/
         *
         * bit_7 defines encoding
         * bit_6 reserved for future use, must be 0
         * bit_5..0 length of IANA language code
         */

            byte[] payload = record.getPayload();

            // Get the Text Encoding
            String textEncoding = null;
            if ((payload[0] & 128)==0){
                textEncoding = "UTF-8";
            }else{
                textEncoding = "UTF-16";
            }

            // Get the Language Code
            int languageCodeLength = payload[0] & 0063;

            // String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");
            // e.g. "en"

            // Get the Text
            return new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                // Do something with the result here
                UserFunctions userFunctions = new UserFunctions();
                String[] productes = result.split(",");
                for (int i = 0; i < productes.length; ++i) {
                    Product nou = userFunctions.getProduct(getApplicationContext(), Long.valueOf(productes[i]));
                    if (nou == null) {
                        userFunctions.updateAllProducts(getApplicationContext());
                    } else {
                        Llista list = Llista.getInstance();
                        list.addProduct(nou);
                    }
                }
                if (!Llista.getInstance().getAllProducts().isEmpty()) botons.setVisibility(View.VISIBLE);
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            }
        }
    }
}
