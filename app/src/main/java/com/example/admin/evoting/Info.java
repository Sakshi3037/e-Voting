package com.example.admin.evoting;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Text;

import java.io.IOException;

public class Info extends Activity {

    WebView descrp;
    String voterId, partyName;
    String Ip;
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        descrp = (WebView) findViewById(R.id.webview);
        Intent intent = getIntent();
        descrp.loadUrl(intent.getStringExtra("Description"));
        voterId = intent.getStringExtra("VoterId");
        partyName = intent.getStringExtra("Party");
        ActionBar mActionBar = getActionBar();
        mActionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#4fb3a2")));
        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Ip = sp.getString("IP", null);
        descrp.getSettings().setJavaScriptEnabled(true);
        descrp.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("http")) {
                    view.loadUrl(url);
                    return true;
                } else {
                    return super.shouldOverrideUrlLoading(view, url);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        else if(id == R.id.action_vote)
        {
            String userVoted = null;
            final Intent intent = new Intent(getApplicationContext(), Twitter.class);
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet("http://"+ Ip + "/php/sendVoteStatus.php?Voter_id=" + voterId);
            HttpResponse httpResponse;
            try {
                httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                userVoted = EntityUtils.toString(httpEntity, "UTF-8");
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(userVoted.contains("yes"))
            {
                intent.putExtra("status", "Already Voted!!");
                startActivity(intent);
            }
            else
            {
                AlertDialog alertDialog = new AlertDialog.Builder(Info.this).create();
                alertDialog.setTitle("Confirm");
                alertDialog.setMessage("Are you sure you want to vote for this party?");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                DefaultHttpClient httpClient = new DefaultHttpClient();
                                HttpGet httpGet = new HttpGet("http://" + Ip + "/php/setVoteStatus.php?Voter_id=" + voterId
                                        + "&Party=" + partyName);
                                HttpResponse httpResponse;
                                try {
                                    httpResponse = httpClient.execute(httpGet);
                                    HttpEntity httpEntity = httpResponse.getEntity();
                                    EntityUtils.toString(httpEntity, "UTF-8");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                intent.putExtra("status", "Successfully Voted!!");
                                startActivity(intent);
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
                alertDialog.show();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
