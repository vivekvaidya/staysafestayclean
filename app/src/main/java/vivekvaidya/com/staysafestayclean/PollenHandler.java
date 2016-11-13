package vivekvaidya.com.staysafestayclean;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class PollenHandler extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public Bitmap mIcon11;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        new DownloadImageTask((ImageView) findViewById(R.id.IMAGEID)).execute("http://10.67.229.19:3000/week.png");
        final ZoomableImageView touch = (ZoomableImageView)findViewById(R.id.IMAGEID);
        touch.setImageBitmap(mIcon11);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "sup", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                touch.setOnTouchListener(new View.OnTouchListener() {

                    public boolean onTouch(View view, MotionEvent event) {
                        Intent myIntent = new Intent(PollenHandler.this, ScrollingActivity.class);
                        PollenHandler.this.startActivity(myIntent);
                        int[] viewCoords = new int[2];
                        view.getLocationOnScreen(viewCoords);
                        int touchX = (int) event.getX();
                        int touchY = (int) event.getY();
                        int imageX = touchX - viewCoords[0]; // viewCoords[0] is the X coordinate
                        int imageY = touchY - viewCoords[1];
                        Log.d("X & Y", imageX + " " + imageY);
                        imageX = imageX/30;
                        imageY = imageY/30; // viewCoords[1] is the y coordinate
                        String str_url = "http://10.67.229.19:3000/data/" + imageX + "/" + imageY;
                        try {
                            //do not run until only one output gets pushed to NODE server.
                            URL url = new URL("http://10.67.229.19:3000/"+imageX+"/"+imageY);
                            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
                            httpCon.setDoOutput(true);
                            httpCon.setRequestMethod("PUT");
                            OutputStreamWriter out = new OutputStreamWriter(
                                    httpCon.getOutputStream());
                            out.write("Resource content");
                            out.close();
                            httpCon.getInputStream();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return true;
                    }
                });
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


    }


    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {

            bmImage.setImageBitmap(result);
        }
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Intent myIntent = new Intent(PollenHandler.this, MainActivity.class);
            PollenHandler.this.startActivity(myIntent);
        } else if (id == R.id.nav_gallery) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_slideshow) {
            Intent myIntent = new Intent(PollenHandler.this, MainActivity.class);
            PollenHandler.this.startActivity(myIntent);
        } else if (id == R.id.nav_manage) {
            Intent myIntent = new Intent(PollenHandler.this, LoginActivity.class);
            PollenHandler.this.startActivity(myIntent);

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
