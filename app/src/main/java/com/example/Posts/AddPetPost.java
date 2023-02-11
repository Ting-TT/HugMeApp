package com.example.Posts;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hugme.R;
import com.example.hugme.UriTool;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: Xie Hu, Ting Tang
 */
public class AddPetPost extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView textView;
    Button pick;

    // create uri list and set adapter for listview
    ArrayList<Uri> uri = new ArrayList<>();
    RecyclerAdapter adapter;

    //Realtime Storeage Fireauth initialization
    private StorageReference storageRef;
    private StorageReference storageReference;
    private DatabaseReference reference;
    private FirebaseAuth instance;
    private FirebaseStorage storage;

    // initial the field
    private static final int Read_Permission = 101;
    private Button btPost;
    private EditText etAddPetID;
    private EditText et_add_petBreed;
    private EditText et_add_petSpecies;
    private EditText et_add_petAge;
    private EditText et_add_petGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pet_post);
        // change action bar colour
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().
                getColor(R.color.navy)));

        // find id for fields
        textView = findViewById(R.id.totalPhotos) ;
        recyclerView=findViewById(R.id.recyclerView_Gallery_Images);
        pick = findViewById(R.id.pick);
        adapter = new RecyclerAdapter(uri);
        recyclerView.setLayoutManager(new GridLayoutManager(AddPetPost.this, 3));
        recyclerView.setAdapter(adapter) ;
        if (ContextCompat.checkSelfPermission(AddPetPost.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(AddPetPost.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},Read_Permission);
        }

        // pick click event
        pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                //set image type
                intent.setType("image/*");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                }
                intent.setAction(Intent.ACTION_GET_CONTENT) ;
                startActivityForResult(Intent.createChooser(intent, "Select Picture"),
                        1);
            }
        });


        // find id for fields
        btPost = findViewById(R.id.bt_post);
        etAddPetID = findViewById(R.id.et_add_petID);
        et_add_petBreed = findViewById(R.id.et_add_petBreed);
        et_add_petSpecies = findViewById(R.id.et_add_petSpecies);
        et_add_petAge = findViewById(R.id.et_add_petAge);
        et_add_petGender = findViewById(R.id.et_add_petGender);

       // Realtime Storeage Fireauth initialization
        reference = FirebaseDatabase.getInstance().getReference();
        instance = FirebaseAuth.getInstance();

        //A reference to the storeage bucket that was created
        storage = FirebaseStorage.getInstance("gs://my-project-eb665.appspot.com");
        storageRef = storage.getReference();
        // post button click event
        btPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check if the uri is empty
                if(uri.size()>0){
                    // call doUpload function
                    doUpload();
                }
                // jump to PostActivity
                Intent intent = new Intent(getApplicationContext(), PostActivity.class);
                startActivity(intent);
            }
        });
    }

    // create new String List
    List<String> imageList = new ArrayList<>();
    List<String> stringList = new ArrayList<>();
    List<String> emailList = new ArrayList<>();
    // initial the likeNumber
    int likeNumber=0;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == Activity.RESULT_OK){
            if(data.getClipData() != null) {
                int x = data.getClipData().getItemCount();
                for (int i = 0; i < x; i++) {
                    uri.add(data.getClipData().getItemAt(i).getUri());
                }
            } else if (data.getData() != null) {
                uri.add(data.getData());
            }
            adapter.notifyDataSetChanged();
            textView.setText("Photos ("+uri.size()+")");
        }
    }

    //Upload multiple images recursively
    private int tempInt = 0;
    // doUpload function for upload image form local
    private void doUpload() {
        // create file object and assign
        Uri file = Uri.fromFile(new File(UriTool.getFileAbsolutePath(AddPetPost.this,uri.get(tempInt))));
        // initial the storageReference
        storageReference = storageRef.child("images/" + file.getLastPathSegment());
        UploadTask uploadTask = storageReference.putFile(file);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //Query the URL of the image after uploading
                storageRef.child("images/"+file.getLastPathSegment()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri tempUri) {

                        imageList.add(tempUri.toString());
                        tempInt++;

                        // Upload post data by combining image data
                        if (tempInt==uri.size()){
                            // assign value into postEntity object by constructor
                            PostEntity postEntity = new PostEntity(etAddPetID.getText().toString(), et_add_petBreed.getText().toString(),
                                    et_add_petSpecies.getText().toString(), et_add_petAge.getText().toString()
                                    , et_add_petGender.getText().toString(), imageList, stringList, emailList,likeNumber
                            );
                            // upload post into firebase
                            DatabaseReference post = reference.child("post").push();
                            post.setValue(postEntity);
                            // Toast message
                            Toast.makeText(AddPetPost.this, "Successsfuly", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        // all doUpload function
                        doUpload();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                    }
                });
            }
        });
    }
}






