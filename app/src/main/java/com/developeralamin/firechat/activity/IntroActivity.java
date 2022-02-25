package com.developeralamin.firechat.activity;


import android.os.Bundle;

import com.developeralamin.firechat.R;
import com.google.firebase.database.annotations.Nullable;

import io.github.dreierf.materialintroscreen.MaterialIntroActivity;
import io.github.dreierf.materialintroscreen.SlideFragmentBuilder;

public class IntroActivity extends MaterialIntroActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addSlide(new SlideFragmentBuilder()
                .title("Send Free Message")
                .image(R.drawable.message)
                .buttonsColor(R.color.yellow)
                .backgroundColor(R.color.red)
                .build());

        addSlide(new SlideFragmentBuilder()
                .title("Connect Your Friend")
                .image(R.drawable.chat)
                .buttonsColor(R.color.yellow)
                .backgroundColor(R.color.red)
                .build());

        addSlide(new SlideFragmentBuilder()
                .title("Make Group Chat")
                .image(R.drawable.group)
                .buttonsColor(R.color.yellow)
                .backgroundColor(R.color.red)
                .build());
    }


}