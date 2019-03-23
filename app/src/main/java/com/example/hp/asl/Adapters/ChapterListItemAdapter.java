package com.example.hp.asl.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hp.asl.Activities.ChapterDetailActivity;
import com.example.hp.asl.Models.ChapterListItem;
import com.example.hp.asl.R;

import java.util.List;

public class ChapterListItemAdapter extends RecyclerView.Adapter<ChapterListItemAdapter.MyViewHolder> {
        private List<ChapterListItem> chapter;
        private Context context;

        public static class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView identification, name;

            MyViewHolder(View view) {
                super(view);
                name = view.findViewById(R.id.chapter_name);
                identification = view.findViewById(R.id.identification);
            }
        }

        public ChapterListItemAdapter(List<ChapterListItem> chapter, Context context) {
            this.chapter = chapter;
            this.context = context;
        }

        @NonNull
        @Override
        public ChapterListItemAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
            ChapterListItem chapterListItem = chapter.get(position);
            holder.name.setText(chapterListItem.getName());
            holder.identification.setText(chapterListItem.getIdentification());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ChapterDetailActivity.class);
                    intent.putExtra("position", chapter.get(position).getName());
                    context.startActivity(intent);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    return true;
                }
            });
        }

        @Override
        public int getItemCount() {
            return chapter.size();
        }
    }

