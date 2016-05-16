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

import xyz.carlesllobet.pullmarket.Domain.Llista;
import xyz.carlesllobet.pullmarket.Domain.Product;
import xyz.carlesllobet.pullmarket.R;
import xyz.carlesllobet.pullmarket.UI.HomeActivity;

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
    private static String compra_tag = "registerPurchase";

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

    public String getComprador(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String res = preferences.getString("comprador", "");
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

    public void setComprador(Context context, String comp) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("comprador", comp);
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

    public JSONObject enviarCompra(Context context) {
        // Building Parameters
        ArrayList<Product> productes = Llista.getInstance().getAllProducts();
        String res = productes.get(0).getId().toString();
        Double preu = productes.get(0).getPreu();
        for (int i = 1; i < productes.size(); ++i){
            res += ",";
            res += productes.get(i).getId().toString();
            preu += productes.get(i).getPreu();
        }

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", compra_tag));
        params.add(new BasicNameValuePair("usuari", "admin@admin.com")); //getComprador(context)
        params.add(new BasicNameValuePair("caixer", getEmail(context)));
        params.add(new BasicNameValuePair("productes", res));
        params.add(new BasicNameValuePair("preu_total", preu.toString()));

        Log.d("usuari:","admin@admin.com");
        Log.d("caixer:",getEmail(context));
        Log.d("preu_total:",preu.toString());
        Log.d("productes:",res);

        JSONObject json = jsonParser.getJSONFromUrl(webserviceURL, params);

        return json;
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
            Uri llet = Uri.parse("android.resource://xyz.carlesllobet.pullmarket/" + R.mipmap.llet);
            Uri xampu = Uri.parse("android.resource://xyz.carlesllobet.pullmarket/" + R.mipmap.xampu);
            Uri dentifric = Uri.parse("android.resource://xyz.carlesllobet.pullmarket/" + R.mipmap.dentifric);
            Uri patates = Uri.parse("android.resource://xyz.carlesllobet.pullmarket/" + R.mipmap.patates);
            Uri cafe = Uri.parse("android.resource://xyz.carlesllobet.pullmarket/" + R.mipmap.cafe);
            addProduct(context,5411786006905L, "Llet Puleva", "Sense lactosa", 1.14, 2, llet);
            addProduct(context, 8413831003300L, "Xampú H&S", "Anticaspa", 5.99, 32, xampu);
            addProduct(context, 3057067222903L, "Cafè Marcilla", "Barreja", 2.43, 2, cafe);
            addProduct(context,8435173009116L, "Pasta Colgate", "Anticàries", 1.19, 33, dentifric);
            addProduct(context,8427626000900L, "Patates Xip Lay's", "Gourmet cruixents", 2.01, 1, patates);


            //Guardem que s'han posat els valors inicials
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("TestValues", "inserted");
            editor.commit();
            return false;
        } else return true;
    }
}
