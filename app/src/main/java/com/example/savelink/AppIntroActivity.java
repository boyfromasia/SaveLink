package com.example.savelink;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.github.paolorotolo.appintro.model.SliderPage;

public class AppIntroActivity extends AppIntro {
    MainActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SliderPage sliderPage1 = new SliderPage();
        sliderPage1.setTitle("");
        sliderPage1.setImageDrawable(R.drawable.first_slide);
        sliderPage1.setDescription("Hello Dear User!");
        sliderPage1.setDescColor(Color.WHITE);
        sliderPage1.setBgColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        addSlide(AppIntroFragment.newInstance(sliderPage1));

        SliderPage sliderPage2 = new SliderPage();
        sliderPage2.setTitle("Add Folder");
        sliderPage2.setTitleColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
        sliderPage2.setImageDrawable(R.drawable.second_slide);
        sliderPage2.setDescription("In the left is a Folder with password" + "\n" + "In the right is a Folder without password");
        sliderPage2.setDescColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
        sliderPage2.setBgColor(ContextCompat.getColor(getApplicationContext(), R.color.background));
        addSlide(AppIntroFragment.newInstance(sliderPage2));

        SliderPage sliderPage3 = new SliderPage();
        sliderPage3.setTitle("Swipe");
        sliderPage3.setTitleColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
        sliderPage3.setImageDrawable(R.drawable.swipe);
        sliderPage3.setDescription("Swipe left to rename" + "\n" + "Swipe right to delete");
        sliderPage3.setDescColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
        sliderPage3.setBgColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        addSlide(AppIntroFragment.newInstance(sliderPage3));

        SliderPage sliderPage4 = new SliderPage();
        sliderPage4.setTitle("Copy link");
        sliderPage4.setTitleColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
        sliderPage4.setImageDrawable(R.drawable.longpress);
        sliderPage4.setDescription("Use long press to copy link");
        sliderPage4.setDescColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
        sliderPage4.setBgColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        addSlide(AppIntroFragment.newInstance(sliderPage4));

        SliderPage sliderPage5 = new SliderPage();
        sliderPage5.setTitle("Caution");
        sliderPage5.setTitleColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
        sliderPage5.setImageDrawable(R.drawable.caution);
        sliderPage5.setDescription("You can't change your password if you lose it");
        sliderPage5.setDescColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
        sliderPage5.setBgColor(ContextCompat.getColor(getApplicationContext(), R.color.background));
        addSlide(AppIntroFragment.newInstance(sliderPage5));
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        finish();
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        finish();
    }
}
