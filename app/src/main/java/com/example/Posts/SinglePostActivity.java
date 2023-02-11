package com.example.Posts;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.Posts.PostEntity;
import com.example.hugme.MainActivity;
import com.example.hugme.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
/**
 * @author: Ting Tang, Hu Xie, Xitong Liu
 */
public class SinglePostActivity extends AppCompatActivity {
    int likeNumber=0;
    private DatabaseReference key;
    FirebaseDatabase rootNode;
    //for specific user email storage in this class
    String personalEmail;
    List<String> userEmailList = new ArrayList<>();

    // ---------- Author: Xitong Liu ---------- //
    // --- Intent put extra object --- //
    private static final String TAG = "YOUR-TAG-NAME";
    PostAll postAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_post);
        // change action bar colour
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().
                getColor(R.color.navy)));

        if (getIntent().hasExtra("Clicked Post")) {
            postAll = getIntent().getParcelableExtra("Clicked Post");
            Log.d(TAG, "onCreate: ");
        }
        // ---------- End of Xitong Liu's part ---------- //

        // get the specific user Email
        Intent intent = getIntent();
        personalEmail = intent.getStringExtra("Personal Email");
        if(personalEmail==null){
            personalEmail="Admin";
        }

        // postID is the unique id for every post, it is same as the node value of this post
        String postUID = postAll.getKey();

        // make the delete button only visible for admin
        Button deleteButton = (Button) findViewById(R.id.bt_delete);
        if (MainActivity.visibility == 1) {
            deleteButton.setVisibility(View.VISIBLE);
        }
        else if (MainActivity.visibility == 0) {
            deleteButton.setVisibility(View.INVISIBLE);
        }


        // images

        // find the imageSlider
        ImageSlider imageSlider = (ImageSlider) findViewById(R.id.image_slider);
        // create an array for storing images in SlideModel
        ArrayList<SlideModel> images = new ArrayList<>();
        // create an array adapter
        ArrayAdapter listOfImagesAdaptor = new ArrayAdapter(this,
                android.R.layout.simple_expandable_list_item_1, images);

        // get images of this pet from firebase
        DatabaseReference imageReference = FirebaseDatabase.getInstance().getReference().
                child("post").child(postUID).child("imageList");
        imageReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                images.clear();
                for (DataSnapshot ss: snapshot.getChildren()) {
                    images.add(new SlideModel(ss.getValue().toString(),null,null));
                }
                listOfImagesAdaptor.notifyDataSetChanged();
                imageSlider.setImageList(images, ScaleTypes.CENTER_CROP);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        // pet info

        // find all related the TextView
        TextView petID = (TextView) findViewById(R.id.tv_petID);
        TextView petAge = (TextView) findViewById(R.id.tv_petAge);
        TextView petSpecies = (TextView) findViewById(R.id.tv_petSpecies);
        TextView petBreed = (TextView) findViewById(R.id.tv_petBreed);
        TextView petGender = (TextView) findViewById(R.id.tv_petGender);

        // get information of this pet from firebase
        DatabaseReference petInfoReference = FirebaseDatabase.getInstance().getReference().
                child("post").child(postUID);
        petInfoReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                petID.setText(snapshot.child("petID").getValue().toString());
                petAge.setText(snapshot.child("petAge").getValue().toString());
                petSpecies.setText(snapshot.child("petSpecies").getValue().toString());
                petBreed.setText(snapshot.child("petBreed").getValue().toString());
                petGender.setText(snapshot.child("petGender").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        // comments

        // create an array for storing comments
        ArrayList<String> comments = new ArrayList<>();
        // find the ListView
        ListView listOfComments = (ListView) findViewById(R.id.lv_comments);
        // create an array adapter
        ArrayAdapter listOfCommentsAdaptor = new ArrayAdapter(this,
                android.R.layout.simple_expandable_list_item_1, comments);
        listOfComments.setAdapter(listOfCommentsAdaptor);

        // get comments for this pet's post from firebase
        DatabaseReference commentReference = FirebaseDatabase.getInstance().getReference().
                child("post").child(postUID).child("commentList");
        commentReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                comments.clear();
                for (DataSnapshot ss: snapshot.getChildren()) {
                    comments.add(ss.getValue().toString());
                }
                listOfCommentsAdaptor.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        // add new comments
        Button replyButton = (Button) findViewById(R.id.bt_sendComment);
        replyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText commentInput = (EditText) findViewById(R.id.et_comment);
                String currentComment = commentInput.getText().toString();
                if (!currentComment.isEmpty()) {
                    comments.add(commentInput.getText().toString());
                    commentReference.setValue(comments); // add this comments into firebase
                    listOfCommentsAdaptor.notifyDataSetChanged();
                    commentInput.setText(""); // clear the EditText
                    // throw a toast to tell the user that their comment has been sent
                    Toast toast = Toast.makeText(getApplicationContext(), "Comment sent!",
                            Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });


        //========================================================================
        // get uid list and like_total from firebase
        //========================================================================
        // create list for userEmail and initial field int likeTotal
        TextView petLike = (TextView)findViewById(R.id.tv_likeNumber);
        // userEmailReference initialization
        DatabaseReference userEmailReference = FirebaseDatabase.getInstance().getReference().
                child("post").child(postUID).child("usrEmailList");
        userEmailReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Traverse and assign value into userEmailList
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    userEmailList.add(dataSnapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        // initial likeTotalReference for the field likeTotal
        DatabaseReference likeTotalReference = FirebaseDatabase.getInstance().getReference().
                child("post").child(postUID).child("likeTotal");
        likeTotalReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                petLike.setText(snapshot.getValue().toString());
                likeNumber=Integer.parseInt(snapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // like_Button
        Button likeButton = (Button)findViewById(R.id.bt_like);
        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userEmailList==null){
                    userEmailList.add(personalEmail);
                    // upload userEmail to firebase
                    userEmailReference.setValue(userEmailList);
                    likeNumber=likeNumber+1;
                    petLike.setText(String.valueOf(likeNumber));
                    likeTotalReference.setValue(likeNumber);
                }else{
                    boolean flag=false;
                    for (int i = 0; i < userEmailList.size(); i++) {
                        if(userEmailList.get(i).equals(personalEmail)){
                            flag=true;
                            Toast.makeText(SinglePostActivity.this,"You have already like this pet!",Toast.LENGTH_LONG).show();
                        }
                    }
                    if(!flag){
                        userEmailList.add(personalEmail);
                        userEmailReference.setValue(userEmailList);
                        likeNumber=likeNumber+1;
                        petLike.setText(String.valueOf(likeNumber));
                        likeTotalReference.setValue(likeNumber);
                    }
                }
            }
        });
        // get the specific key value
        key = FirebaseDatabase.getInstance().getReference().child("post").child(postUID);


        // delete event
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                key.setValue(null);
            }
        });
    }
}