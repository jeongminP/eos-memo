package com.example.memoapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memoapp.R;
import com.example.memoapp.data.Memo;

import java.util.ArrayList;

public class MemoAdapter extends RecyclerView.Adapter<MemoViewHolder> {

    private ArrayList<Memo> memoList;

    public MemoAdapter(ArrayList<Memo> memoList) {
        this.memoList = memoList;
    }

    @NonNull
    @Override
    public MemoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);

        return new MemoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemoViewHolder holder, int position) {

        Memo memo = memoList.get(position);

        holder.title.setText(memo.getTitle());
        holder.text.setText(memo.getText());
        holder.date.setText(memo.getDate());

    }

    @Override
    public int getItemCount() {
        return memoList.size();
    }
}
