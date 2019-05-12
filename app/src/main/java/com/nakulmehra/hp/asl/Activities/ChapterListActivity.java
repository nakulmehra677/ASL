package com.nakulmehra.hp.asl.Activities;

import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.support.v7.widget.Toolbar;
import android.widget.Switch;
import android.widget.Toast;

import com.nakulmehra.hp.asl.Adapters.ChapterListItemAdapter;
import com.nakulmehra.hp.asl.Fragments.FeedbackFragment;
import com.nakulmehra.hp.asl.Interfaces.OnChapterListChangedListener;
import com.nakulmehra.hp.asl.Interfaces.OnChapterSearchListChangedListener;
import com.nakulmehra.hp.asl.Interfaces.OnLastChapterChangedListener;
import com.nakulmehra.hp.asl.Interfaces.OnShareMessageChangedListener;
import com.nakulmehra.hp.asl.Managers.ChapterListManager;
import com.nakulmehra.hp.asl.Managers.DatabaseManager;
import com.nakulmehra.hp.asl.Models.ChapterListItem;
import com.nakulmehra.hp.asl.R;

import java.util.ArrayList;
import java.util.List;

public class ChapterListActivity extends BaseActictivity implements NavigationView.OnNavigationItemSelectedListener {

    private ProgressBar progressBar, scrollProgressBar;
    private ChapterListItemAdapter mAdapter;
    private SwipeRefreshLayout mySwipeRefreshLayout;
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private LinearLayoutManager mLayoutManager;
    private static Bundle mBundleRecyclerViewState;
    private Switch nightModeSwitch;
    private final String KEY_RECYCLER_STATE = "recycler_state";
    private SwitchCompat drawerSwitch;

    private ChapterListManager chapterListManager;
    List<ChapterListItem> chapterList = new ArrayList<>();
    String lastChapter = "", lastFetechedChapter = "";
    private boolean newLastChapterFeteched = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
            setTheme(R.style.darkAppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chlist);

        progressBar = findViewById(R.id.indeterminateBar);
        scrollProgressBar = findViewById(R.id.scrollProgressbar);
        recyclerView = findViewById(R.id.my_recycler_view);
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.parent_layout);
        navigationView = findViewById(R.id.nav_view);
        mySwipeRefreshLayout = findViewById(R.id.swiperefresh);
        drawerSwitch = findViewById(R.id.night_mode_switch);

        setSupportActionBar(toolbar);
        recyclerView.setHasFixedSize(true);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        // Setting up the recyclerView //
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        chapterListManager = new ChapterListManager(this);
        mAdapter = new ChapterListItemAdapter(chapterList, ChapterListActivity.this);
        recyclerView.setAdapter(mAdapter);
        getLastChapterName();

        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {

                    @Override
                    public void onRefresh() {
                        chapterList.clear();
                        lastChapter = "";
                        lastFetechedChapter = "";
                        mAdapter.notifyDataSetChanged();
                        getLastChapterName();
                    }
                }
        );

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (!recyclerView.canScrollVertically(1)) {
                    if (!lastChapter.equals(lastFetechedChapter) && newLastChapterFeteched) {
                        scrollProgressBar.setVisibility(View.VISIBLE);
                        newLastChapterFeteched = false;
                        getData();
                    }
                }
            }
        });

        drawerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Log.i("TAGG", "Night mode ON");
                } else {
                    Log.i("TAGG", "Night mode OFF");
                }
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });
    }

    public void getData() {
        chapterListManager.getChapterListDetails(onChapterListChangedListener(), lastFetechedChapter);
    }

    public void getLastChapterName() {
        chapterListManager.getLastChapterDetails(onLastChapterChangedListener());
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.i("TAGG", "ChapterListActivity onPause()");

        // save RecyclerView state
        mBundleRecyclerViewState = new Bundle();
        Parcelable listState = recyclerView.getLayoutManager().onSaveInstanceState();
        mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, listState);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.i("TAGG", "ChapterListActivity onResume()");

        // restore RecyclerView state
        if (mBundleRecyclerViewState != null) {
            Parcelable listState = mBundleRecyclerViewState.getParcelable(KEY_RECYCLER_STATE);
            recyclerView.getLayoutManager().onRestoreInstanceState(listState);
        }
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.chapter_list_menu, menu);
        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) item.getActionView();
        chapterList.clear();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                progressBar.setVisibility(View.VISIBLE);
                chapterListManager.searchChapter(onChapterSearchListChangedListener(), s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                mySwipeRefreshLayout.setEnabled(false);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
        }
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.share_app:
                DatabaseManager databaseManager = new DatabaseManager(this);
                databaseManager.getShareMessage(onShareMessageChangedListener());
                break;

            case R.id.give_feedback:
                DialogFragment newFragment = FeedbackFragment.newInstance(ChapterListActivity.this);
                newFragment.show(getSupportFragmentManager(), "dialog");
                break;

            case R.id.about_asl:
                startActivity(new Intent(this, AboutASLActivity.class));
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private OnShareMessageChangedListener onShareMessageChangedListener() {
        return new OnShareMessageChangedListener() {
            @Override
            public void onShareMessageChanged(String shareMessage) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, shareMessage);
                intent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(intent, "Share with"));
            }
        };
    }

    private OnLastChapterChangedListener onLastChapterChangedListener() {
        return new OnLastChapterChangedListener() {
            @Override
            public void onLastChapterChanged(String lastChapterName) {
                lastChapter = lastChapterName;
                getData();
            }
        };
    }

    private OnChapterSearchListChangedListener<List<ChapterListItem>> onChapterSearchListChangedListener() {
        return new OnChapterSearchListChangedListener<List<ChapterListItem>>() {
            @Override
            public void onChapterSearchListChanged(List<ChapterListItem> list) {
                chapterList.addAll(list);
                newLastChapterFeteched = true;
                mAdapter.notifyDataSetChanged();

                progressBar.setVisibility(View.GONE);
                scrollProgressBar.setVisibility(View.GONE);
                mySwipeRefreshLayout.setRefreshing(false);
            }
        };
    }

    private OnChapterListChangedListener<List<ChapterListItem>> onChapterListChangedListener() {
        return new OnChapterListChangedListener<List<ChapterListItem>>() {
            @Override
            public void onChapterListChanged(List<ChapterListItem> list, String lastChapterName) {
                lastFetechedChapter = lastChapterName;
                chapterList.addAll(list);
                newLastChapterFeteched = true;
                mAdapter.notifyDataSetChanged();

                progressBar.setVisibility(View.GONE);
                scrollProgressBar.setVisibility(View.GONE);
                mySwipeRefreshLayout.setRefreshing(false);
            }
        };
    }
}