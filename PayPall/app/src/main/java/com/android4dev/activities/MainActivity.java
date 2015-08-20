package com.android4dev.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android4dev.fragments.Fragment_Product_Detail;
import com.android4dev.fragments.MarkerDemoActivity;
import com.android4dev.helpers.AppController;
import com.android4dev.models.MyRecyclerView;
import com.android4dev.navigationview.R;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;

/***
 * @author Kidane Yosief
 * @version 08/20/2015
 * @since 1.0
 */
public class MainActivity extends AppCompatActivity{

    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX;

    private static final String CONFIG_CLIENT_ID = "AWfS0gLldWSy6EQsd6VJ-872AGLP0_yHKdpoeM-jSzJeJ1TlCLXxSDdfQUSXWCFpX6qK37UcDz7fjf5d";

    private static final int REQUEST_PAYPAL_PAYMENT = 1;

    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(CONFIG_ENVIRONMENT)
            .clientId(CONFIG_CLIENT_ID)
                    // The following are only used in PayPalFuturePaymentActivity.
            .merchantName("The Appexperts Ltd")
            .merchantPrivacyPolicyUri(Uri.parse("https://www.example.com/privacy"))
            .merchantUserAgreementUri(Uri.parse("https://www.example.com/legal"));


    //Defining Variables
    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private String url = "https://dl.dropboxusercontent.com/u/1559445/ASOS/SampleApi/anyproduct_details.json?catid=";
    ArrayList<String> urlarray;
    String description, brandname;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initializing Toolbar and setting it as the actionbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);
        //Initializing NavigationView
        navigationView = (NavigationView) findViewById(R.id.navigation_view);

        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {


                //Checking if the item is in checked state or not, if not make it in checked state
                if(menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);

                //Closing drawer on item click
                drawerLayout.closeDrawers();

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()){


                    case R.id.Pay:
                        //Toast.makeText(getApplicationContext(),"Inbox Selected",Toast.LENGTH_SHORT).show(
                        showProductsPay();
                        return true;
                    case R.id.Map:
                        //Toast.makeText(getApplicationContext(),"Inbox Selected",Toast.LENGTH_SHORT).show(
                        showProductsMap();
                        return true;

                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.inbox:
                        //Toast.makeText(getApplicationContext(),"Inbox Selected",Toast.LENGTH_SHORT).show(
                        showProducts("catalog01_1000_2623");
                        return true;

                    // For rest of the options we just show a toast on click

                    case R.id.starred:
                        //Toast.makeText(getApplicationContext(),"Stared Selected",Toast.LENGTH_SHORT).show();
                        showProducts("catalog01_1000_6992");
                        return true;
                    case R.id.sent_mail:
                        //Toast.makeText(getApplicationContext(),"Send Selected",Toast.LENGTH_SHORT).show();
                        showProducts("catalog01_1000_6930");
                        return true;
                    case R.id.drafts:
                        //Toast.makeText(getApplicationContext(),"Drafts Selected",Toast.LENGTH_SHORT).show();
                        showProducts("catalog01_1000_12451");
                        return true;
                    case R.id.allmail:
                        //Toast.makeText(getApplicationContext(),"All Mail Selected",Toast.LENGTH_SHORT).show();
                        showProducts("catalog01_1000_4174");
                        return true;
                    case R.id.trash:
                        //Toast.makeText(getApplicationContext(),"Trash Selected",Toast.LENGTH_SHORT).show();
                        showProducts("catalog01_1000_8730");
                        return true;
                    case R.id.spam:
                        showProducts("catalog01_1000_1314");
                        //Toast.makeText(getApplicationContext(),"Spam Selected",Toast.LENGTH_SHORT).show();
                        return true;
                    default:
                        Toast.makeText(getApplicationContext(),"Somethings Wrong",Toast.LENGTH_SHORT).show();
                        return true;

                }
            }
        });

        // Initializing Drawer Layout and ActionBarToggle
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.openDrawer, R.string.closeDrawer){

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank

                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();

    }

    // show fragment with particular category id
    public void showProducts(String catid){
        Fragment fragment = new MyRecyclerView();
        Bundle bd = new Bundle();
        bd.putString("catid", catid);
        fragment.setArguments(bd);
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.commit();
    }

    public void showProductsPay(){

        PayPalPayment thingToBuy = new PayPalPayment(new BigDecimal(1),"USD", "theappexperts.co.uk",
                PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(MainActivity.this, PaymentActivity.class);

        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);

        startActivityForResult(intent, REQUEST_PAYPAL_PAYMENT);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_PAYPAL_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm = data
                        .getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    try {
                        System.out.println("Responseeee"+confirm);
                        Log.i("paymentExample", confirm.toJSONObject().toString());


                        JSONObject jsonObj=new JSONObject(confirm.toJSONObject().toString());

                        String paymentId=jsonObj.getJSONObject("response").getString("id");
                        System.out.println("payment id:-=="+paymentId);
                        Toast.makeText(getApplicationContext(), paymentId, Toast.LENGTH_LONG).show();

                    } catch (JSONException e) {
                        Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("paymentExample", "The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i("paymentExample", "An invalid Payment was submitted. Please see the docs.");
            }
        }


    }

    public void showProductsMap(){

        Fragment fragment = new MarkerDemoActivity();
        Bundle bd = new Bundle();
        fragment.setArguments(bd);
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.commit();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showProduct(int pid){
        //Log.e("pid",pid+"");

        makeJsonReq(pid);


    }

    private void makeJsonReq(int pid) {
        final int pID = pid;
        //Log.e("inside","inside makeJsonReq with "+url+catid);
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET,url+pid,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.d("Volley", response.toString());
                        urlarray = new ArrayList<>();

                        try {
                            brandname = response.getString("Brand");
                            description = response.getString("Description");
                            //urls = new ArrayList<>();
                            JSONArray c = response.getJSONArray("ProductImageUrls");
                            for(int i=0; i < c.length();i++){
                                urlarray.add(c.getString(i));
                            }
                            Log.e("brand", brandname);
                            Log.e("desc", description);
                            Log.e("urls", urlarray.toString());
                        }catch (JSONException e){
                            Log.d("Jsonerror",e.toString());
                        }

                        Bundle bd = new Bundle();
                        Fragment fr = new Fragment_Product_Detail();
                        bd.putString("desc",description);
                        bd.putString("brand",brandname);
                        bd.putStringArrayList("list", urlarray);
                        bd.putInt("pid", pID);
                        Log.i("bundle",bd.toString());
                        fr.setArguments(bd);
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.frame,fr);
                        ft.commit();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error", "Error: " + error.getMessage());
            }
        });
        // Adding request to request queue

        AppController.getInstance().addToRequestQueue(req, "jSon_obj");

    }
}
