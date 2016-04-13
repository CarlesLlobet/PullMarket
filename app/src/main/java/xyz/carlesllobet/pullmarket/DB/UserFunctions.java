package xyz.carlesllobet.pullmarket.DB;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import xyz.carlesllobet.pullmarket.Domain.Product;
import xyz.carlesllobet.pullmarket.R;

public class UserFunctions {

    private JSONParser jsonParser;

    // Testing in localhost using wamp or xampp
    // use http://10.0.2.2/ to connect to your localhost ie http://localhost/

    //private static String loginURL = "http://192.168.69.18/servei_web";
    //private static String registerURL = "http://192.168.69.18/servei_web";
    private static String webserviceURL = "http://pushmarket.carlesllobet.xyz/webservice/index.php";

    private static String login_tag = "login";
    private static String register_tag = "register";
    private static String products_tag = "products";
    private static String password_tag = "password";

    private SharedPreferences preferences;

    // constructor
    public UserFunctions() {
        jsonParser = new JSONParser();
    }

    //USERS

    public JSONObject loginUser(String email, String password) {
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", login_tag));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("password", password));

        JSONObject json = jsonParser.getJSONFromUrl(webserviceURL, params);

        return json;
    }

    public JSONObject registerUser(String email, String nombre, String cognoms, String edad, String password,
                                   String sexe, String pais, String ciutat) {
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", register_tag));
        params.add(new BasicNameValuePair("nombre", nombre));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("cognoms", cognoms));
        params.add(new BasicNameValuePair("edad", edad));
        params.add(new BasicNameValuePair("password", password));
        params.add(new BasicNameValuePair("sexe", sexe));
        params.add(new BasicNameValuePair("pais", pais));
        params.add(new BasicNameValuePair("ciutat", ciutat));

        // getting JSON Object
        JSONObject json = jsonParser.getJSONFromUrl(webserviceURL, params);

        return json;
    }



    public String getName (Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String res = preferences.getString("name", "");
        return res;
    }

    public String getPass (Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String res = preferences.getString("password", "");
        return res;
    }

    public String getLang (Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String res = preferences.getString("language", "");
        Log.d("recuperat", res);
        return res;
    }

    public String getEmail(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String res = preferences.getString("email", "");
        return res;
    }

    public String getPassword(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String res = preferences.getString("password", "");
        return res;
    }

    public String getLastName(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String res = preferences.getString("lastName", "");
        return res;
    }

    public String getAge(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String res = preferences.getString("edad", "");
        return res;
    }

    public String getSex(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String res = preferences.getString("sexe", "");
        return res;
    }

    public String getCountry(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String res = preferences.getString("pais", "");
        return res;
    }

    public String getCity(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String res = preferences.getString("ciutat", "");
        return res;
    }

    public void setLang(Context context, String lang) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("language", lang);
        Log.d("guardat", lang);
    }

    public void setPass (Context context, String password){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("password", password);

        //SET PASSWORD TO WEBSERVICE DB
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", password_tag));
        params.add(new BasicNameValuePair("password", password));

        // getting JSON Object
        jsonParser.getJSONFromUrl(webserviceURL, params);
    }

    /**
     * Function get Login status
     */
    public boolean isUserLoggedIn(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (preferences.contains("email")) return true;
        return false;
    }

    /**
     * Function to logout user
     * Reset Database
     */
    public void logoutUser(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();

        editor.remove("email");
        editor.commit();
    }


    //PRODUCTS

    public void updateAllProducts(Context context) {
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", products_tag));
        // getting JSON Object
        JSONObject json = jsonParser.getJSONFromUrl(webserviceURL, params);

        DatabaseHandler db = new DatabaseHandler(context);
        db.resetTable();
        //Insert all products of the json
    }

    public ArrayList<Product> getAllProducts(Context context) {
        DatabaseHandler db = new DatabaseHandler(context);
        ArrayList<Product> products = db.getAllProducts();
        return products;
    }

    public Product getProduct(Context context, Long id) {
        DatabaseHandler db = new DatabaseHandler(context);
        Product product = db.getProduct(id);
        return product;
    }

    public String getProductName(Context context, Integer id) {
        DatabaseHandler db = new DatabaseHandler(context);
        String res = db.getProductName(id);
        return res;
    }

    public boolean addProduct(Context context, Long id, String nom, String descripcio, Double preu, Integer sector, Uri foto){
        DatabaseHandler db = new DatabaseHandler(context);
        boolean res = db.addProduct(id,nom,descripcio,sector,preu,foto);
        return res;
    }

    public Boolean checkTestValues(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (preferences.getString("TestValues","notExists").equals("notExists")) {
            //Fiquem valors inicials
            Uri vi = Uri.parse("android.resource://xyz.carlesllobet.pushmarket/" + R.drawable.vi);
            Uri tabac = Uri.parse("android.resource://xyz.carlesllobet.pushmarket/" + R.drawable.tabac);
            Uri paper = Uri.parse("android.resource://xyz.carlesllobet.pushmarket/" + R.drawable.paper);
            Uri diari = Uri.parse("android.resource://xyz.carlesllobet.pushmarket/" + R.drawable.diari);
            Uri letibalm = Uri.parse("android.resource://xyz.carlesllobet.pushmarket/" + R.drawable.letibalm);
            Uri cafe = Uri.parse("android.resource://xyz.carlesllobet.pushmarket/" + R.drawable.cafe);
            addProduct(context,5411786006905L, "Flandria Original", "Fumar Mata", 3.35, 2, tabac);
            addProduct(context, 8413831003300L, "Xènius Penedès", "12.5% vol, 75cl", 8.90, 32, vi);
            addProduct(context, 3057067222903L, "Papel de Liar OCB", "300 unidades", 2.25, 2, paper);
            addProduct(context,8435173009116L, "Cápsulas café", "Capsulas ecologicas", 4.99, 33, cafe);
            addProduct(context,8427626000900L, "El Mundo", "Noticias de deportes", 1.10, 1, diari);
            addProduct(context,84196866L, "Letibalm", "Crema labial", 4.30, 5, letibalm);

            //Guardem que s'han posat els valors inicials
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("TestValues", "inserted");
            editor.commit();
            return false;
        } else return true;
    }
}
