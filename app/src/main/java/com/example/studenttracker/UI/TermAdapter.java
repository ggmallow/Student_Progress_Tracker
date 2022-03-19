package com.example.studenttracker.UI;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studenttracker.Models.Course;
import com.example.studenttracker.Models.Term;
import com.example.studenttracker.R;

import java.util.ArrayList;

public class TermAdapter extends RecyclerView.Adapter<TermAdapter.MyViewHolder>{

    private ArrayList<Term> allTermsList;
    private OnTermListener mOnTermListener;

    public int checkedPosition = -1;


    public TermAdapter(ArrayList<Term> allTermsList, OnTermListener onTermListener) {
        this.allTermsList = allTermsList;
        this.mOnTermListener = onTermListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView termView;
        OnTermListener  onTermListener;

        public MyViewHolder(final View itemView, OnTermListener onTermListener) {
            super(itemView);
            termView = itemView.findViewById(R.id.termView);
            this.onTermListener = onTermListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onTermListener.onTermClick(getAdapterPosition());
        }

    }


    public interface OnTermListener{
        void onTermClick(int position);

    }



    @NonNull
    @Override
    public TermAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View termView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_term, parent, false);
        return new MyViewHolder(termView, mOnTermListener);
    }

    @Override
    public void onBindViewHolder(@NonNull TermAdapter.MyViewHolder holder, int position) {
        String termName = allTermsList.get(position).getTitle();
        holder.termView.setText(termName);

        holder.termView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkedPosition = holder.getAdapterPosition();
                notifyDataSetChanged();

            }
        } );

        if (checkedPosition == position) {
            holder.termView.setBackgroundColor(Color.parseColor("#FF018786"));
            mOnTermListener.onTermClick(holder.getAdapterPosition());


        } else {
            holder.termView.setBackgroundColor(Color.parseColor("#FFFFFF"));

        }

    }

    @Override
    public int getItemCount() {
        return allTermsList.size();
    }


}
