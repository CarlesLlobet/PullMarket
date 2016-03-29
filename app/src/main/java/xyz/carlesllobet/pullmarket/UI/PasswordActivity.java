package xyz.carlesllobet.pullmarket.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import xyz.carlesllobet.pushmarket.DB.UserFunctions;
import xyz.carlesllobet.pushmarket.R;

/**
 * Created by CarlesLlobet on 10/03/2016.
 */
public class PasswordActivity extends xyz.carlesllobet.pushmarket.UI.BaseActivity implements View.OnClickListener {
    private Button btnChange;

    private EditText oldPass;
    private EditText newPass;
    private EditText repeated;

    private TextView error;

    private UserFunctions userFunctions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userFunctions = new UserFunctions();
        setContentView(R.layout.activity_password);
        oldPass = (EditText) findViewById(R.id.oldPassword);

        newPass = (EditText) findViewById(R.id.newPassword);
        repeated = (EditText) findViewById(R.id.newPassword2);

        error = (TextView) findViewById(R.id.change_error);

        setTitle(R.string.tituloPassword);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnChange = (Button) findViewById(R.id.btnChange);
        btnChange.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnChange:
                String nP = newPass.getText().toString();
                String rP = repeated.getText().toString();
                if (nP.equals(rP)) {
                    userFunctions = new UserFunctions();

                    String oP = oldPass.getText().toString();
                    if (userFunctions.getPass(getApplicationContext()).equals(oP)) {
                        userFunctions.setPass(getApplicationContext(), nP);
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.notifPass), Toast.LENGTH_LONG).show();
                    } else error.setText(R.string.error1);

                    Intent intent = new Intent(PasswordActivity.this, SettingsActivity.class);
                    startActivity(intent);
                    //tancar menu
                } else error.setText(R.string.error2);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_others, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }
}
