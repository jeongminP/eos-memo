package com.example.memoapp.adapter;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.memoapp.R;

public class MemoViewHolder extends RecyclerView.ViewHolder {

    LinearLayout parent;
    TextView title;
    TextView text;
    TextView date;

    MemoViewHolder(View view) {
        super(view);

        parent = view.findViewById(R.id.item_parent);
        title = view.findViewById(R.id.item_title);
        text = view.findViewById(R.id.item_text);
        date = view.findViewById(R.id.item_date);

    }

}
