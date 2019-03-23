package com.example.hp.asl.Activities;

import android.annotation.SuppressLint;
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
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ProgressBar;

import com.example.hp.asl.Adapters.ChapterListItemAdapter;
import com.example.hp.asl.Interfaces.OnChapterListChangedListener;
import com.example.hp.asl.Managers.ChapterListManager;
import com.example.hp.asl.Models.ChapterListItem;
import com.example.hp.asl.R;
import com.example.hp.asl.broadcast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChapterListActivity extends AppCompatActivity {

    ChapterListManager chapterListManager;
    com.example.hp.asl.broadcast broadcast = new broadcast();
    private static ProgressBar progressBar;
    private static ChapterListItemAdapter mAdapter;
    SwipeRefreshLayout mySwipeRefreshLayout;
    private RecyclerView recyclerView;

    private boolean isScrolling = false;
    private int currentItems, totalItems, scrolledOutItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chlist);

        progressBar = findViewById(R.id.indeterminateBar);
        recyclerView = findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);

        // Setting up the recyclerView //
        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChapterListActivity.this, UploadChapterActivity.class));
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

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                    isScrolling = true;
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItems = mLayoutManager.getChildCount();
                totalItems = mLayoutManager.getItemCount();
                scrolledOutItems = ((LinearLayoutManager) mLayoutManager).findFirstVisibleItemPosition();

                if (isScrolling && (currentItems + scrolledOutItems == totalItems)) {
                    isScrolling = false;
                    getData();
                }
            }
        });
    }

    public void getData() {
        chapterListManager = new ChapterListManager(this);
        chapterListManager.getChapterListDetails(onChapterListChangedListener());
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(broadcast, filter);
        getData();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(broadcast);
    }

    private OnChapterListChangedListener<List<ChapterListItem>> onChapterListChangedListener() {
        return new OnChapterListChangedListener<List<ChapterListItem>>() {
            @Override
            public void onChapterListChanged(List<ChapterListItem> list) {

                mAdapter = new ChapterListItemAdapter(list, ChapterListActivity.this);
                mAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(mAdapter);
                progressBar.setVisibility(View.INVISIBLE);
            }
        };
    }
}