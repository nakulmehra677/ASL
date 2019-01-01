package com.example.hp.asl;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class chlist extends AppCompatActivity {

    broadcast broadcast = new broadcast();

    private static ProgressBar progressBar;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    private static List<post> chapter = new ArrayList<>();
    private static MyAdapter mAdapter;

    private FloatingActionButton fab;

    private static DatabaseReference databaseReference;

    SwipeRefreshLayout mySwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chlist);

        progressBar = findViewById(R.id.indeterminateBar);

        mRecyclerView = findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        mAdapter = new MyAdapter(chapter, this);
        mRecyclerView.setAdapter(mAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                startActivity(new Intent(chlist.this, add.class));
            }
        });

        mySwipeRefreshLayout = findViewById(R.id.swiperefresh);
        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {

                    @Override
                    public void onRefresh() {
                        getData();
                        mySwipeRefreshLayout.setRefreshing(false);
                    }
                }
        );
    }

    static void getData() {
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                progressBar.setVisibility(View.VISIBLE);

                chapter.clear();
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    post p = d.getValue(post.class);
                    chapter.add(p);
                }
                progressBar.setVisibility(View.GONE);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        databaseReference.addListenerForSingleValueEvent(postListener);
    }

    @Override
    protected void onStart() {
        getData();
        super.onStart();

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(broadcast, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(broadcast);
    }
}

class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    List<post> chapter;
    private Context context;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView identification, chapter_name;

        public MyViewHolder(View view) {
            super(view);
            chapter_name = view.findViewById(R.id.chapter_name);
            identification = view.findViewById(R.id.identification);
        }
    }

    public MyAdapter(List<post> chapter, Context context) {

        this.chapter = chapter;
        this.context = context;
    }

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        post post = chapter.get(position);
        holder.chapter_name.setText(post.getChapter_name());
        holder.identification.setText(post.getIdentification());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, chapterDetail.class);
                intent.putExtra("position", chapter.get(position).getChapter_name());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return chapter.size();
    }
}

class post {

    private String chapter_name, identification;

    public post() {
    }

    public post(String chapter_name, String identification) {
        this.chapter_name = chapter_name;
        this.identification = identification;
    }

    public String getChapter_name() {
        return chapter_name;
    }

    public String getIdentification() {
        return identification;
    }

    public void setChapter_name(String chapter_name) {
        this.chapter_name = chapter_name;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }
}