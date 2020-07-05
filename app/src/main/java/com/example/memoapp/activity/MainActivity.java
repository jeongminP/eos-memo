package com.example.memoapp.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.memoapp.R;
import com.example.memoapp.Requests;
import com.example.memoapp.adapter.MemoAdapter;
import com.example.memoapp.data.Memo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    static final int CLICK_DELETE = 0;
    static final int CLICK_EDIT = 1;
    static final int CLICK_DIALOG_ADD = 2;
    static final int CLICK_DIALOG_EDIT = 3;

    private ArrayList<Memo> memoList = new ArrayList<>();
    private MemoAdapter memoAdapter = new MemoAdapter(this, memoList);

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

                showMemoDialog(CLICK_DIALOG_ADD, -1);

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

    private Memo findMemo(int id) {

        for(Memo memo : memoList) {

            if(memo.getId() == id) return memo;

        }

        return null;

    }

    private void showMemoDialog(int action, final int id) {

        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog, null);

        TextView nameTextView = view.findViewById(R.id.dialog_name);
        final EditText titleEditText = view.findViewById(R.id.dialog_title);
        final EditText textEditText = view.findViewById(R.id.dialog_text);
        Button okButton = view.findViewById(R.id.dialog_ok);
        Button cancelButton = view.findViewById(R.id.dialog_cancel);

        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        if(action == CLICK_DIALOG_ADD) {   // add

            nameTextView.setText("Add Memo");

            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String title = titleEditText.getText().toString();
                    String text = textEditText.getText().toString();
                    String date = new SimpleDateFormat("yyyy. MM. dd. HH:mm", Locale.getDefault()).format(new Date());

                    addMemo(title, text, date);

                    dialog.dismiss();

                }
            });

        } else if(action == CLICK_DIALOG_EDIT) {    // edit

            nameTextView.setText("Edit Memo");

            try {

                Memo memo = findMemo(id);

                titleEditText.setText(memo.getTitle());
                textEditText.setText(memo.getText());

            } catch (Exception e) {
                e.printStackTrace();
                return;
            }

            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String title = titleEditText.getText().toString();
                    String text = textEditText.getText().toString();
                    String date = new SimpleDateFormat("yyyy. MM. dd. HH:mm", Locale.getDefault()).format(new Date());

                    editMemo(id, title, text, date);

                    dialog.dismiss();

                }
            });

        }

        dialog.show();

    }

    private void addMemo(final String title, final String text, final String date) {

        new Thread() {
            @Override
            public void run() {

                Requests.addMemo(title, text, date);

                loadMemo();

            }
        }.start();

    }

    public void clickedMemo(int action, int id) {

        if(action == CLICK_DELETE) deleteMemo(id);
        else if(action == CLICK_EDIT) showMemoDialog(CLICK_DIALOG_EDIT, id);

    }

    public void deleteMemo(final int id) {

        new Thread() {
            @Override
            public void run() {

                Requests.deleteMemo(id);

                loadMemo();

            }
        }.start();

    }

    public void editMemo(final int id, final String title, final String text, final String date) {

        new Thread() {
            @Override
            public void run() {

                Requests.editMemo(id, title, text, date);

                loadMemo();

            }
        }.start();

    }

}
