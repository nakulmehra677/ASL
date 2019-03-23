package com.example.hp.asl.Managers;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hp.asl.Activities.ChapterDetailActivity;
import com.example.hp.asl.Activities.UploadChapterActivity;
import com.example.hp.asl.Interfaces.OnChapterChangedListener;
import com.example.hp.asl.Interfaces.OnChapterExamplesChangedListener;
import com.example.hp.asl.Interfaces.OnChapterListChangedListener;
import com.example.hp.asl.Interfaces.OnChapterRulesChangedListener;
import com.example.hp.asl.Interfaces.OnChapterUploadListener;
import com.example.hp.asl.Models.ChapterDetail;
import com.example.hp.asl.Models.ChapterExamples;
import com.example.hp.asl.Models.ChapterListItem;
import com.example.hp.asl.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DatabaseManager {

    private Context context;

    DatabaseManager(Context context) {
        this.context = context;
    }

    void getChapterList(final OnChapterListChangedListener<List<ChapterListItem>> listener) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<ChapterListItem> list = new ArrayList<>();
                for (DataSnapshot d : dataSnapshot.getChildren())
                    list.add(d.getValue(ChapterListItem.class));

                listener.onChapterListChanged(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("ERROR", "getChapterList(), onCancelled", new Exception(databaseError.getMessage()));
            }
        });
    }

    protected void getChapter(String name, final OnChapterChangedListener listener) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(name);

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
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(name).child("rules");

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
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(name).child("examples");

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
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

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
}
