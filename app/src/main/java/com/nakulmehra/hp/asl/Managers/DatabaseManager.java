package com.nakulmehra.hp.asl.Managers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.Query;
import com.nakulmehra.hp.asl.Interfaces.OnChapterChangedListener;
import com.nakulmehra.hp.asl.Interfaces.OnChapterExamplesChangedListener;
import com.nakulmehra.hp.asl.Interfaces.OnChapterListChangedListener;
import com.nakulmehra.hp.asl.Interfaces.OnChapterRulesChangedListener;
import com.nakulmehra.hp.asl.Interfaces.OnChapterSearchListChangedListener;
import com.nakulmehra.hp.asl.Interfaces.OnChapterUploadListener;
import com.nakulmehra.hp.asl.Interfaces.OnLastChapterChangedListener;
import com.nakulmehra.hp.asl.Interfaces.OnShareMessageChangedListener;
import com.nakulmehra.hp.asl.Models.ChapterDetail;
import com.nakulmehra.hp.asl.Models.ChapterExamples;
import com.nakulmehra.hp.asl.Models.ChapterListItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {

    private Context context;

    public DatabaseManager(Context context) {
        this.context = context;
    }

    void getChapterList(final OnChapterListChangedListener<List<ChapterListItem>> listener, final String lastChapter) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Chapters");
        Query query;
        if (lastChapter.isEmpty())
            query = databaseReference.orderByKey().limitToFirst(20);
        else
            query = databaseReference.orderByKey().startAt(lastChapter).limitToFirst(20);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<ChapterListItem> list = new ArrayList<>();
                for (DataSnapshot d : dataSnapshot.getChildren())
                    list.add(d.getValue(ChapterListItem.class));

                String newLastChapterName = list.get(list.size() - 1).getName();
                list.remove(list.size() - 1);
                listener.onChapterListChanged(list, newLastChapterName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("ERROR", "getChapterList(), onCancelled", new Exception(databaseError.getMessage()));
            }
        });
    }

    void getLastChapter(final OnLastChapterChangedListener listener) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Chapters");
        Query getLastKey = databaseReference.orderByKey().limitToLast(1);

        getLastKey.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String chapterName = "";
                for (DataSnapshot d : dataSnapshot.getChildren())
                    chapterName = d.getValue(ChapterListItem.class).getName();

                listener.onLastChapterChanged(chapterName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    void search(final OnChapterSearchListChangedListener<List<ChapterListItem>> listener, String searchChapter) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Chapters");
        Query query;

        query = databaseReference.orderByKey()
                .startAt(searchChapter)
                .endAt(searchChapter + "\uf8ff");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<ChapterListItem> list = new ArrayList<>();
                if (dataSnapshot.exists())
                    for (DataSnapshot d : dataSnapshot.getChildren())
                        list.add(d.getValue(ChapterListItem.class));

                listener.onChapterSearchListChanged(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("ERROR", "getChapterList(), onCancelled", new Exception(databaseError.getMessage()));
            }
        });
    }

    protected void getChapter(String name, final OnChapterChangedListener listener) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Chapters").child(name);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    ChapterDetail chapterDetail = dataSnapshot.getValue(ChapterDetail.class);
                    listener.onChapterChanged(chapterDetail);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("ERROR", "getChapter(), onCancelled", new Exception(databaseError.getMessage()));
            }
        });
    }

    protected void getChapterRules(String name, final OnChapterRulesChangedListener<List<String>> listener) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Chapters").child(name).child("rules");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> list = new ArrayList<>();
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    list.add(d.getValue(String.class));
                }
                listener.onChapterRuleChanged(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("ERROR", "getChapterRules(), onCancelled", new Exception(databaseError.getMessage()));
            }
        });
    }

    protected void getChapterExamples(String name, final OnChapterExamplesChangedListener<List<ChapterExamples>> listener) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Chapters").child(name).child("examples");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<ChapterExamples> list = new ArrayList<>();
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    list.add(d.getValue(ChapterExamples.class));
                }
                listener.onChapterExampleChanged(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("ERROR", "getChapterExamples(), onCancelled", new Exception(databaseError.getMessage()));
            }
        });

    }

    void uploadChapter(ChapterDetail chapterDetail, final OnChapterUploadListener listener) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Chapters");

        databaseReference.child(chapterDetail.getName()).setValue(chapterDetail).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                    listener.onChapterUploaded(true);
                else
                    listener.onChapterUploaded(false);
            }
        });
    }

    public void sendFeedback(String feedbackMessage) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Feedbacks");
        String key = databaseReference.push().getKey();
        databaseReference.child(key).setValue(feedbackMessage);
    }

    public void getShareMessage(final OnShareMessageChangedListener listener) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Share Message");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String shareMessage = dataSnapshot.getValue(String.class);
                listener.onShareMessageChanged(shareMessage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
