package xyz.carlesllobet.pullmarket.UI;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import xyz.carlesllobet.pushmarket.DB.UserFunctions;
import xyz.carlesllobet.pushmarket.Domain.NFC.Util.NFCHammer;
import xyz.carlesllobet.pushmarket.R;

/**
 * Created by CarlesLlobet on 26/01/2016.
 */
public class HomeActivity extends BaseActivity implements View.OnClickListener{

    private UserFunctions uf;

    private Toolbar tb;

    private FloatingActionButton afegir,llista,barcode;

    private Boolean fabOpen;

    NfcAdapter mAdapter;
    PendingIntent mPendingIntent;
    IntentFilter writeTagFilters[];
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setTitle(R.string.tituloHome);

        afegir = (FloatingActionButton) findViewById (R.id.fab);
        llista = (FloatingActionButton) findViewById (R.id.fab1);
        barcode = (FloatingActionButton) findViewById (R.id.fab2);

        llista.setVisibility(View.INVISIBLE);
        barcode.setVisibility(View.INVISIBLE);
        fabOpen = false;

        uf = new UserFunctions();

        tb = (Toolbar) findViewById(R.id.tool_bar);

        afegir.setOnClickListener(this);
        llista.setOnClickListener(this);
        barcode.setOnClickListener(this);

        //Escoltar NFC
        mAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mAdapter == null) {
            Log.d("NFC", "Your device does not support NFC");
            return;
        }
        mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
                getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                if (fabOpen){
                    llista.setVisibility(View.INVISIBLE);
                    barcode.setVisibility(View.INVISIBLE);
                    fabOpen = false;
                } else {
                    llista.setVisibility(View.VISIBLE);
                    barcode.setVisibility(View.VISIBLE);
                    fabOpen = true;
                }
                break;
            case R.id.fab1:
                startActivity(new Intent(getApplicationContext(), ProductsActivity.class));
                break;
            case R.id.fab2:
                startActivity(new Intent(getApplicationContext(), BarcodeActivity.class));
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkMenuItem(0);

        llista.setVisibility(View.INVISIBLE);
        barcode.setVisibility(View.INVISIBLE);
        fabOpen = false;

        //Reescoltar NFC
        if (mAdapter != null) {
            if (!mAdapter.isEnabled()) {
                Log.d("NFC", "Your NFC is not enabled");
                return;
            }
            mAdapter.enableForegroundDispatch(this, mPendingIntent, null, null);
        }
    }

    @Override
    protected void onPause(){
        super.onPause();
        if (mAdapter != null) {
            mAdapter.disableForegroundDispatch(this);
        }
    }

    @Override
    protected void onNewIntent(final Intent intent) {
        showProgress(true);
        super.onNewIntent(intent);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                setIntent(intent);
                resolveIntent(intent);
            }
        }, 0);

    }

    private void resolveIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            getTagInfo(intent);
        }
    }

    private void getTagInfo(Intent intent) {
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        String[] techList = tag.getTechList();
        for (int i = 0; i < techList.length; i++) {
            if (techList[i].equals(MifareClassic.class.getName())) {
                MifareClassic mifareClassicTag = MifareClassic.get(tag);
                switch (mifareClassicTag.getType()) {
                    case MifareClassic.TYPE_CLASSIC:
                        MifareClassic mfc = MifareClassic.get(tag);
                        // resolveIntentClassic(mfc);
                        boolean result = NFCHammer.ReadClassic1kValue(this, mfc);
                        if(result){
                            showProgress(false);
                            //TRACTAR RESULT
                        }else{
                            showProgress(false);
                            Log.d("NFC", "Tap The card again!!!");
                            //Toast.makeText(this, "Tap The card again!!!", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case MifareClassic.TYPE_PLUS:
                        Log.d("NFC", "This Tag is Mifare Classic Plus. We will Add this type in next version");
                        break;
                    case MifareClassic.TYPE_PRO:
                        Log.d("NFC", "This Tag is Mifare Classic Pro. We will Add this type in next version");
                        break;
                }
            } else if (techList[i].equals(MifareUltralight.class.getName())) {
                MifareUltralight mifareUlTag = MifareUltralight.get(tag);
                switch (mifareUlTag.getType()) {
                    case MifareUltralight.TYPE_ULTRALIGHT:
                        boolean result1 = NFCHammer.readUltraLightValue(this, tag);
                        if(result1){
                            showProgress(false);
                            //TRACTAR RESULT1.
                        }
                        else{
                            showProgress(false);
                            Log.d("NFC", "Tap The card again!!!");
                        }
                        break;
                    case MifareUltralight.TYPE_ULTRALIGHT_C:


                        boolean result = NFCHammer.ReadULCValue(this, tag);
                        if(result){
                            showProgress(false);
                            //TRACTAR RESULT
                        }
                        else{
                            showProgress(false);
                            Log.d("NFC","Tap The card again!!!");
                        }
                        break;
                }
            } else if (techList[i].equals(IsoDep.class.getName())) {
                // info[1] = "IsoDep";
                @SuppressWarnings("unused")
                IsoDep isoDepTag = IsoDep.get(tag);
			/*	CommonTask
				.createToast(
						"This Tag is IsoDep tag. We will Add this type in next version",
						this, Color.GREEN);*/
                // info[0] += "IsoDep \n";
            } else if (techList[i].equals(Ndef.class.getName())) {
                Ndef.get(tag);
				/*CommonTask
				.createToast(
						"This Tag is NDEF Tag. We will Add this type in next version",
						this, Color.GREEN);*/
            } else if (techList[i].equals(NdefFormatable.class.getName())) {
                @SuppressWarnings("unused")
                NdefFormatable ndefFormatableTag = NdefFormatable.get(tag);
			/*	CommonTask
				.createToast(
						"This Tag is NDEF formatable Tag. We will Add this type in next version",
						this, Color.GREEN);*/
            }
        }
    }

    private void showProgress(final boolean show) {
        if (show){
            pDialog = new ProgressDialog(HomeActivity.this);
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setMessage("Procesando...");
            pDialog.setCancelable(true);
            pDialog.setMax(100);

            pDialog.setProgress(0);
            pDialog.show();
        }

        else {
            pDialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        if (fabOpen){
            llista.setVisibility(View.INVISIBLE);
            barcode.setVisibility(View.INVISIBLE);
            fabOpen = false;
        } else {
            finish();
        }
    }
}
