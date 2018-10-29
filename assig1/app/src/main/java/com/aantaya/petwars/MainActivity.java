package com.aantaya.petwars;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.aantaya.petwars.Entities.CatEntity;
import com.aantaya.petwars.ViewModels.CatViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private CatViewModel catViewModel;
    public static final int NEW_WORD_ACTIVITY_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        catViewModel = ViewModelProviders.of(this).get(CatViewModel.class);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final CatListAdapter adapter = new CatListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        catViewModel.getMyAllCats().observe(this, new Observer<List<CatEntity>>() {
            @Override
            public void onChanged(@Nullable final List<CatEntity> words) {
                // Update the cached copy of the words in the adapter.
                adapter.setCats(words);
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddCatActivity.class);
                startActivityForResult(intent, NEW_WORD_ACTIVITY_REQUEST_CODE);
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_WORD_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Long imageid = 10L;
            String name = data.getStringExtra(AddCatActivity.EXTRA_NAME);
            String desc = data.getStringExtra(AddCatActivity.EXTRA_DESC);

            CatEntity cat = new CatEntity(imageid, 0, name, desc);
            catViewModel.insert(cat);
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG).show();
        }
    }
}