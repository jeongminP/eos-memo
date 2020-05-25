package com.example.memoapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.memoapp.R;
import com.example.memoapp.adapter.MemoAdapter;
import com.example.memoapp.data.Memo;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Memo> memoList = new ArrayList<>();
    private MemoAdapter memoAdapter = new MemoAdapter(memoList);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUI();

        addMemo(1, "첫 메모", "안녕", "2020. 05. 18");
        addMemo(2, "두번째 메모", "좋아요", "2020. 05. 18");
        addMemo(3, "333", "hello", "2020. 05. 18");
        addMemo(4, "new memo", "new text", "2020. 05. 18");
    }

    private void setUI() {

        // tool bar
        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        // recycler view
        RecyclerView recyclerView = findViewById(R.id.main_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        // recycler view adapter
        recyclerView.setAdapter(memoAdapter);

    }

    private void addMemo(int id, String title, String text, String date) {

        // add memo
        Memo newMemo = new Memo(id, title, text, date);
        memoList.add(newMemo);

        // update recycler view
        memoAdapter.notifyDataSetChanged();
    }
}
