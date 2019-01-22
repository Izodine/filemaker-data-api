package com.joselopezrosario.androidfm_sample;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.joselopezrosario.androidfm.Fm;
import com.joselopezrosario.androidfm.FmData;
import com.joselopezrosario.androidfm.FmRecord;
import com.joselopezrosario.androidfm.FmRequest;
import com.joselopezrosario.androidfm.FmResponse;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AssetRecyclerViewAdapter assetAdapter;
    private RecyclerView assetRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        assetRecyclerView = findViewById(R.id.asset_recyclerview);
        assetRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        assetAdapter = new AssetRecyclerViewAdapter();
        assetRecyclerView.setAdapter(assetAdapter);

        FloatingActionButton fab = findViewById(R.id.fab);
        final TextView resultsCounter = findViewById(R.id.results_count_textview);

        final Handler handler = new Handler();

        new Thread(new Runnable() {
            @Override
            public void run() {
                FmData data = NetworkUtil.getAllRecords();

                final int size = data.size();

                final List<FmRecord> records = new ArrayList<>();

                for (int i = 0; i < size; i++) {
                    records.add(data.getRecord(i));
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        assetAdapter.updateData(records);
                        resultsCounter.setText(String.format("Showing %d Results.", size));
                    }
                });


            }
        }).start();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Action goes here
            }
        });

    }
}
