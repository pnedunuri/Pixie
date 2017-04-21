package com.pixieproto.bluephish.pixieproto;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.pixieproto.bluephish.bottomnavlibrary.BottomNavigationItem;
import com.pixieproto.bluephish.bottomnavlibrary.BottomNavigationView;
import com.pixieproto.bluephish.bottomnavlibrary.OnBottomNavigationItemClickListener;

public class PixieProto extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pixie_proto);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigation);

        int[] image = {R.drawable.ic_home_color_primary_24dp, R.drawable.ic_image_color_primary_24dp,
                R.drawable.ic_camera_color_primary_24dp, R.drawable.ic_more_color_primary_24dp};
        int whiteColor = ContextCompat.getColor(this, R.color.white);

        if (bottomNavigationView != null) {
            bottomNavigationView.isWithText(false);
            // bottomNavigationView.activateTabletMode();
            bottomNavigationView.isColoredBackground(true);
            bottomNavigationView.setTextActiveSize(getResources().getDimension(R.dimen.text_active));
            bottomNavigationView.setTextInactiveSize(getResources().getDimension(R.dimen.text_inactive));
            bottomNavigationView.setItemActiveColorWithoutColoredBackground(whiteColor);
            bottomNavigationView.setFont(Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/Noh_bold.ttf"));
        }

        // setup nav bar
        BottomNavigationItem bottomNavigationItem = new BottomNavigationItem("Home", whiteColor, image[0]);
        BottomNavigationItem bottomNavigationItem1 = new BottomNavigationItem("Gallery", whiteColor, image[1]);
        BottomNavigationItem bottomNavigationItem2 = new BottomNavigationItem("Camera", whiteColor, image[2]);
        BottomNavigationItem bottomNavigationItem3 = new BottomNavigationItem("More", whiteColor, image[3]);

        bottomNavigationView.addTab(bottomNavigationItem);
        bottomNavigationView.addTab(bottomNavigationItem1);
        bottomNavigationView.addTab(bottomNavigationItem2);
        bottomNavigationView.addTab(bottomNavigationItem3);

        bottomNavigationView.setOnBottomNavigationItemClickListener(new OnBottomNavigationItemClickListener() {
            @Override
            public void onNavigationItemClick(int index) {
                switch (index) {
                    case 0:
                        // home button clicked
                        break;
                    case 1:
                        // gallery button clicked
                        break;
                    case 2:
                        // camera button clicked
                        Intent intent = new Intent(PixieProto.this, CameraActivity.class);
                        startActivity(intent);
                        break;
                    case 3:
                        // more button clicked
                        break;
                }
            }
        });

        verifyPermissions(Manifest.permission.INTERNET);
        verifyPermissions(Manifest.permission.ACCESS_NETWORK_STATE);
    }


    /**
     * Checks if the app has permission to write to device storage
     * <p>
     * If the app does not has permission then the user will be prompted to grant permissions
     */
    public void verifyPermissions(String... permissionStr) {
        // Check if we have write permission
        for (int i = 0; i < permissionStr.length; i++) {
            int permission = ActivityCompat.checkSelfPermission(this, permissionStr[i]);

            if (permission != PackageManager.PERMISSION_GRANTED) {
                // We don't have permission so prompt the user
                ActivityCompat.requestPermissions(this, new String[]{permissionStr[i]}, i);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {

        }
    }
}