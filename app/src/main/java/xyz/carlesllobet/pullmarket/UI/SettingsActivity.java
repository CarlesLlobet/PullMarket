package xyz.carlesllobet.pullmarket.UI;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.Locale;

import xyz.carlesllobet.pullmarket.DB.UserFunctions;
import xyz.carlesllobet.pullmarket.R;

/**
 * Created by JEDI on 17/08/2015.
 */
public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnChangePassword;
    private Button btnLogout;

    private UserFunctions userFunctions;

    private String name;
    private String lastName;
    private String email;
    private String sex;
    private String country;
    private String city;
    private String age;

    private TextView nombre;
    private TextView apellido;
    private TextView correo;
    private TextView edad;
    private TextView pais;
    private TextView ciudad;

    private RadioButton sexo;

    private boolean clickable;

    private MenuItem language;

    private String lang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        clickable = true;

        userFunctions = new UserFunctions();

        setContentView(R.layout.activity_settings);

        setTitle(R.string.tituloSettings);

        nombre = (TextView) findViewById(R.id.nombre);
        apellido = (TextView) findViewById(R.id.apellidos);
        correo = (TextView) findViewById(R.id.email);
        edad = (TextView) findViewById(R.id.edad);
        pais = (TextView) findViewById(R.id.pais);
        ciudad = (TextView) findViewById(R.id.ciudad);


        name = userFunctions.getName(getApplicationContext());
        lastName = userFunctions.getLastName(getApplicationContext());
        age = userFunctions.getAge(getApplicationContext());
        email = userFunctions.getEmail(getApplicationContext());
        country = userFunctions.getCountry(getApplicationContext());
        city = userFunctions.getCity(getApplicationContext());
        sex = userFunctions.getSex(getApplicationContext());

        if (sex.equals("Male")) {
            sexo = (RadioButton) findViewById(R.id.radioMale);
            sexo.setChecked(true);
        } else if (sex.equals("Female")) {
            sexo = (RadioButton) findViewById(R.id.radioFemale);
            sexo.setChecked(true);
        }

        nombre.setText(name);
        apellido.setText(lastName);
        correo.setText(email);
        edad.setText(age);
        pais.setText(country);
        ciudad.setText(city);

        btnChangePassword = (Button) findViewById(R.id.btnChangePassword);
        btnChangePassword.setOnClickListener(this);

        btnLogout = (Button) findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (clickable) {
            switch (v.getId()) {
                case R.id.btnChangePassword:
                    startActivity(new Intent(getApplicationContext(), PasswordActivity.class));
                    break;
                case R.id.btnLogout:
                    UserFunctions userFunctions = new UserFunctions();
                    userFunctions.logoutUser(getApplicationContext());
                    Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    //tancar menu
                    break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        language = menu.findItem(R.id.action_language);
        String aux = Locale.getDefault().toString();
        if (aux.equals("ca")) language.setIcon(R.mipmap.catalonia);
        if (aux.equals("en")) language.setIcon(R.mipmap.uk);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_spa:
                Locale locale = new Locale("es");
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                getApplicationContext().getResources().updateConfiguration(config, null);
                language.setIcon(R.mipmap.spain);
                lang = "es";
                userFunctions.setLang(getApplicationContext(), lang);
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                finish();
                break;
            case R.id.action_cat:
                Locale locale2 = new Locale("ca");
                Locale.setDefault(locale2);
                Configuration config2 = new Configuration();
                config2.locale = locale2;
                getApplicationContext().getResources().updateConfiguration(config2, null);
                language.setIcon(R.mipmap.catalonia);
                lang = "ca";
                userFunctions.setLang(getApplicationContext(), lang);
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                finish();
                break;
            case R.id.action_en:
                Locale locale3 = new Locale("en");
                Locale.setDefault(locale3);
                Configuration config3 = new Configuration();
                config3.locale = locale3;
                getApplicationContext().getResources().updateConfiguration(config3, null);
                language.setIcon(R.mipmap.uk);
                lang = "en";
                userFunctions.setLang(getApplicationContext(), lang);
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // refresh your views here
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

}