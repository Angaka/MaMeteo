package com.projects.crow.mameteo.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.projects.crow.mameteo.BuildConfig;
import com.projects.crow.mameteo.R;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

/**
 * Created by Venom on 21/09/2017.
 */

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setDescription(getString(R.string.app_description))
                .addItem(new Element().setTitle("Version " + BuildConfig.VERSION_NAME))
                .addWebsite("https://www.flaticon.com/authors/robin-kylander", "Thanks to Robin Kylander for icons. Click here for details")
                .addWebsite("https://darksky.net/poweredby/", "Powered by Dark Sky")
                .create();
        setContentView(aboutPage);
    }
}
