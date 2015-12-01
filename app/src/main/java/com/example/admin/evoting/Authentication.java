package com.example.admin.evoting;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import java.io.IOException;
import java.util.Random;

public class Authentication extends Activity {
    GmailSender sender;
    String userId;
    String VoterId = null;
    String userEmail = null;
    int i1;
    TextView textView;
    @SuppressLint("NewApi")
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        ActionBar mActionBar = getActionBar();
        mActionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#4fb3a2")));
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setHomeButtonEnabled(true);
        sender = new GmailSender("pawanjot.punarkriti@gmail.com", "googlepunarkriti");
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.
                Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        textView = (TextView) findViewById(R.id.textView9);
        final Intent intent = getIntent();
        VoterId = intent.getStringExtra("VoterId");
        DefaultHttpClient httpClient = new DefaultHttpClient();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String Ip = sp.getString("IP", null);
        HttpGet httpGet = new HttpGet("http://" + Ip + "/php/getMail.php?Voter_id=" + VoterId);
        HttpResponse httpResponse;
        try {
            httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            userEmail = EntityUtils.toString(httpEntity, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(userEmail == null)
        {
            AlertDialog alertDialog = new AlertDialog.Builder(Authentication.this).create();
            alertDialog.setTitle("Alert");
            alertDialog.setMessage("Server is down, try again later..");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }});
        }
        try {
             new MyAsyncClass().execute(userEmail);
             } catch (Exception ex) {
             Toast.makeText(Authentication.this, ex.toString(), Toast.LENGTH_SHORT).show();
             }
    }

    public void resend(View view)
    {
        try {
            new MyAsyncClass().execute(userEmail);
        } catch (Exception ex) {
            Toast.makeText(Authentication.this, ex.toString(), Toast.LENGTH_SHORT).show();
        }
    }
    public void proceed(View view)
    {
        EditText password = (EditText) findViewById(R.id.editText);
        if(password.getText() == null)
        {

        }
        else
        {
            int input = Integer.parseInt(password.getText().toString());
            if(input == i1)
            {
                Intent intent = new Intent(this, User_info.class);
                intent.putExtra("VoterId", VoterId);
                startActivity(intent);
            }
            else
            {
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                TextView textView = (TextView) findViewById(R.id.textView5);
                textView.setVisibility(View.VISIBLE);
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_authentication, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        else if(id == android.R.id.home)
        {
            startActivity(new Intent(this, Navigation.class));
        }

        return super.onOptionsItemSelected(item);
    }
    class MyAsyncClass extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... Params) {
            try {
                Random r = new Random();
                i1 = r.nextInt(99999 - 11111 + 1) + 11111;
                String body = "Hello\n Your OTP for voter card verification is " + i1;
                sender.sendMail("Voter card verification", body, "pawanjot.punarkriti@gmail.com", Params[0]);
            }
            catch (Exception ex) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }
    }

    @Override
    public void onBackPressed()
    {
        startActivity(new Intent(this, Navigation.class));
    }
}

