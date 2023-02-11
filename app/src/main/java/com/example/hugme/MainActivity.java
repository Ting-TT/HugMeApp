package com.example.hugme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Posts.PostActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.Objects;

/**
 * @author: Ting Tang, Xie Hu, Xitong Liu
 */

public class MainActivity extends AppCompatActivity {
    // Visibility in the Post Activity for different users
    public static int visibility = 0;
    //create field of google sign option&client
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    ImageView googleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Objects.requireNonNull(getSupportActionBar()).hide();

        // gso and gsc initialization
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions
                .DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        gsc = GoogleSignIn.getClient(this,gso);

        googleButton = (ImageView) findViewById(R.id.googleSignIn);
        //googleButton click event
        googleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sigIn();
            }
        });
    }

    public void admin(View v) {
        EditText adminCode = (EditText) findViewById(R.id.adminCode);
        Button adminSignIn = (Button) findViewById(R.id.adminSignIn);
        TextView description1 = (TextView) findViewById(R.id.description1);
        TextView description2 = (TextView) findViewById(R.id.description2);
        TextView description3 = (TextView) findViewById(R.id.description3);

        if (adminCode.getVisibility() == View.INVISIBLE) {
            adminCode.setVisibility(View.VISIBLE);
            adminSignIn.setVisibility(View.VISIBLE);
            description1.setVisibility(View.INVISIBLE);
            description2.setVisibility(View.INVISIBLE);
            description3.setVisibility(View.INVISIBLE);
        }
        else {
            hideAdmin();
        }
    }

    public void adminSignIn(View v) {
        EditText adminCode = (EditText) findViewById(R.id.adminCode);
        Intent intent = new Intent(getApplicationContext(), PostActivity.class);
        if (adminCode.getText().toString().equals("1234567")) {
            visibility = 1;
            gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    finish();
                }
            });
            startActivity(intent);
        }
        else
            Toast.makeText(getApplicationContext(), "Invalid Admin Code",
                    Toast.LENGTH_SHORT).show();
    }


    public void locationButton(View v) {
        Intent intent = new Intent(MainActivity.this, LocationActivity.class);
        startActivity(intent);
    }

    private void hideAdmin() {
        EditText adminCode = (EditText) findViewById(R.id.adminCode);
        Button adminSignIn = (Button) findViewById(R.id.adminSignIn);
        TextView description1 = (TextView) findViewById(R.id.description1);
        TextView description2 = (TextView) findViewById(R.id.description2);
        TextView description3 = (TextView) findViewById(R.id.description3);
        TextView textForContact = (TextView) findViewById(R.id.tv_emergency);
        TextView vet = (TextView) findViewById(R.id.tv_vet);
        TextView contact = (TextView) findViewById(R.id.tv_contact);
        TextView address = (TextView) findViewById(R.id.tv_address);
        adminCode.setVisibility(View.INVISIBLE);
        adminSignIn.setVisibility(View.INVISIBLE);
        description1.setVisibility(View.VISIBLE);
        description2.setVisibility(View.VISIBLE);
        description3.setVisibility(View.VISIBLE);
        textForContact.setVisibility(View.VISIBLE);
        vet.setVisibility(View.VISIBLE);
        contact.setVisibility(View.VISIBLE);
        address.setVisibility(View.VISIBLE);
    }

    // sigIn function
    void sigIn(){
        Intent signInIntent = gsc.getSignInIntent();
        startActivityForResult(signInIntent,1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1000){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            if (task == null) {
            }
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                navigateToPostActivity();
            } catch (ApiException e) {
                e.printStackTrace();
            }
        }
    }

    void navigateToPostActivity(){
        finish();
        Intent intent = new Intent(MainActivity.this, PostActivity.class);
        startActivity(intent);
    }
}