package com.example.memoapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.memoapp.R;
import com.example.memoapp.Requests;
import com.example.memoapp.adapter.MemoAdapter;
import com.example.memoapp.data.Memo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Memo> memoList = new ArrayList<>();
    private MemoAdapter memoAdapter = new MemoAdapter(memoList);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUI();

//        test();
        loadMemo();

    }

    private void setUI() {

        // tool bar
        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        /* add button */
        FloatingActionButton fab = findViewById(R.id.main_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMemo();
            }
        });

        // recycler view
        RecyclerView recyclerView = findViewById(R.id.main_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        // recycler view adapter
        recyclerView.setAdapter(memoAdapter);

    }

    private void loadMemo() {

        // empty list
        memoList.clear();

        new Thread() {
            @Override
            public void run() {

                try {

                    // request to server
                    String getMemo = Requests.getMemo();

                    if(getMemo == null) return;

                    // parse json array
                    JSONArray jsonArray = new JSONArray(getMemo);

                    for(int i = 0; i < jsonArray.length(); i++) {

                        // parse json object
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        int id = jsonObject.getInt("id");
                        String title = jsonObject.getString("title");
                        String text = jsonObject.getString("text");
                        String date = jsonObject.getString("date");

                        // add memo to list
                        Memo newMemo = new Memo(id, title, text, date);
                        memoList.add(newMemo);

                    }

                    // main thread
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            // update recycler view
                            memoAdapter.notifyDataSetChanged();

                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }.start();

    }

    private void addMemo() {

        new Thread() {
            @Override
            public void run() {

                Requests.addMemo("자동 추가", (memoList.size() + 1) + "", "2020-05-26");

                loadMemo();

            }
        }.start();

    }

}
