package com.example.admin.evoting;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class SessionExpire extends Activity {

    TextView click;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_expire);
        click = (TextView) findViewById(R.id.textView6);
        click.setPaintFlags(click.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_session_expire, menu);
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
    public void logout(View view)
    {
        Intent intent = new Intent(this, Navigation.class);
        startActivity(intent);
    }
    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(this, Navigation.class);
        startActivity(intent);
    }
}
