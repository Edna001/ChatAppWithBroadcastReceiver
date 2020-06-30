package com.ednadev.allo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.ednadev.allo.view.MziaFragment;
import com.ednadev.allo.view.ZezvaFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadFragment(R.id.mziaContainer, new MziaFragment());
        loadFragment(R.id.zezvaContainer, new ZezvaFragment());
    }

    private void loadFragment(int container, Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(container, fragment)
                .commit();
    }
}