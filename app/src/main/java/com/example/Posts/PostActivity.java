package com.example.Posts;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hugme.MainActivity;
import com.example.hugme.R;
import com.example.parser.Parser;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author: Qianxuan Lin, Xie Hu, Xitong Liu
 */

public class PostActivity extends AppCompatActivity implements TimelineAdapter.OnPostListener {
    // field initial
    Button signOutButton;
    GoogleSignInClient gsc;
    GoogleSignInOptions gso;
    String personalEmail;

    // -------------------- Author: Xitong Liu -------------------- //
    private static final String TAG = "YOUR-TAG-NAME";
    List<PostAll> allPosts = new ArrayList<>();    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Objects.requireNonNull(getSupportActionBar()).setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.toolbar_title_layout);

        setContentView(R.layout.activity_post);

        // --- Retrieve data from firebase and show in recyclerView --- //
        RecyclerView rvTimeline = (RecyclerView) findViewById(R.id.timeline);
        // DATA
        // Get all the data in real time
        DatabaseReference posts = FirebaseDatabase.getInstance().getReference().child("post");
        rvTimeline.setHasFixedSize(true);
        rvTimeline.setLayoutManager(new LinearLayoutManager(this));


        // ADAPTER
        TimelineAdapter timelineAdapter = new TimelineAdapter(getApplication(), allPosts, this);
        rvTimeline.setAdapter(timelineAdapter);

        posts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot post : snapshot.getChildren()) {
                    PostAll pa = new PostAll(post.getKey(), post.getValue(PostEntity.class));
                    allPosts.add(pa);
                }
                timelineAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // Set visibility for different users
        Button findVet = (Button) findViewById(R.id.Button_logout);
        Button createPost = (Button) findViewById(R.id.createPost);
        if (MainActivity.visibility == 1) {
            findVet.setVisibility(View.INVISIBLE);
            createPost.setVisibility(View.VISIBLE);
        }
        else if (MainActivity.visibility == 0) {
            findVet.setVisibility(View.VISIBLE);
            createPost.setVisibility(View.INVISIBLE);
        }
        // -------------------- End of Xitong Liu's part -------------------- //


        //-------------------------------search part--------------------------------
        // --- Author: Qianxuan Lin, Xitong Liu --- //
        Button searchBtn = (Button) findViewById(R.id.searchButton);
        EditText searchText = (EditText)  findViewById(R.id.searchPost);
        List<PostAll> resultPosts = new ArrayList<>(); //store search result
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputSearch =searchText.getText().toString().replaceAll("\\s+","");
                resultPosts.clear(); // Clear previous array on second click
                boolean check = false;
                // Catch all exceptions without exiting
                try {
                    check = Parser.parsing(inputSearch);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(check) {
                    for (int i = 0; i < allPosts.size(); i++) {
                        String breed = Parser.getBreed(inputSearch);
                        String species = Parser.getSpecies(inputSearch);
                        // If both conditions for search is satisfied
                        if (!(Objects.equals(breed, "") || Objects.equals(species, ""))) {
                            if (Objects.equals(allPosts.get(i).getPe().getPetBreed().replaceAll("\\s+",""), breed)
                                    && Objects.equals(allPosts.get(i).getPe().getPetSpecies().replaceAll("\\s+",""), species)) {
                                resultPosts.add(allPosts.get(i));
                            }
                        }
                        // If either condition is satisfied
                        else if (Objects.equals(allPosts.get(i).getPe().getPetBreed().replaceAll("\\s+",""), breed)) {
                            resultPosts.add(allPosts.get(i));
                        }
                        else if(Objects.equals(allPosts.get(i).getPe().getPetSpecies().replaceAll("\\s+",""), species)) {
                            resultPosts.add(allPosts.get(i));
                        }
                    }
                    // Set adapter to searched posts
                    TimelineAdapter timelineAdapterSearch = new TimelineAdapter(getApplication(), resultPosts, PostActivity.this);
                    rvTimeline.setAdapter(timelineAdapterSearch);

                } else {
                    Toast.makeText(getApplicationContext(), "Invalid Search Input.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        //-------------------------------search part end--------------------------------
        // -------------------- End of Qianxuan Lin, Xitong Liu's part -------------------- //

        // initial the gso and gsc
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);
        signOutButton = findViewById(R.id.Button_logout);
        GoogleSignInAccount googleAccount = GoogleSignIn.getLastSignedInAccount(this);
        // check the account
        if(googleAccount!=null){
            // assign google email to personalEmail
            personalEmail = googleAccount.getEmail();
        }
        // signOut click event
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
    }

    public void createPost(View v) {
        // jump to AddPetPost
        Intent intent = new Intent(getApplicationContext(), AddPetPost.class);
        startActivity(intent);
    }

    //signOut function
    public void signOut(){
        gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                finish();
                startActivity(new Intent(PostActivity.this,MainActivity.class));
            }
        });
    }
    // -------------------- Author: Xitong Liu -------------------- //
    @Override
    public void postListener(int position, List<PostAll> posts) {
        Log.d(TAG, "postListener: clicked");
        // transfer data
        Intent intent = new Intent(getApplicationContext(), SinglePostActivity.class);
        intent.putExtra("Clicked Post", posts.get(position));
        intent.putExtra("Personal Email", personalEmail);
        startActivity(intent);
    }
    // -------------------- End of Xitong Liu's part -------------------- //
}