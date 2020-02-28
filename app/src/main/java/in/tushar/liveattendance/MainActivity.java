package in.tushar.liveattendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    public static String TAG = "----Main Activity";
    TextView signInText, emailText , passwordText , registerNowText , signInWithText , googleText , facebookText , twitterText , githubText;
    EditText userEmail, userPassword;
    Button signInbtn;
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Log.e(TAG,"Current User : "+currentUser );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userEmail = findViewById(R.id.emailAddress);
        userPassword = findViewById(R.id.password);
        mAuth = FirebaseAuth.getInstance();
        signInbtn = findViewById(R.id.signInbtn);
        signInbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInbtn.setScaleX((float)0.8);
                signInbtn.setScaleY((float)0.8);
                if(userEmail.getText().toString().isEmpty()){
                    userEmail.setError("Please Enter email or name");
                    userEmail.requestFocus();
                    return;
                }else if(userPassword.getText().toString().isEmpty()){
                    userPassword.setError("Enter password");
                    userPassword.requestFocus();
                    return;
                }else if(!userEmail.getText().toString().isEmpty() || !userPassword.getText().toString().isEmpty()){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            signInbtn.setScaleX((float)1.0);
                            signInbtn.setScaleY((float)1.0);
                            Log.e(TAG,"Details : "+userEmail.getText().toString()+" : "+userPassword.getText().toString());
                            createUser(userEmail.getText().toString().trim(),userPassword.getText().toString().trim());
                        }
                    },250);
                }
            }
        });
    }
    public void createUser(final String email , final String password){
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Log.e(TAG," createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Log.e(TAG,"After Created get user : "+user);
                            signInExistingUser(email,password);
                        }else{
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    public void signInExistingUser(final String email , final String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Log.d(TAG, "exist user : "+user);
                            getCurrentUser(email,password);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    public void getCurrentUser(String email , String password){
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null){
            String name = user.getDisplayName();
            String userEmail = user.getEmail();
            Uri photoUri = user.getPhotoUrl();
            boolean emailVerified = user.isEmailVerified();
            String uid = user.getUid();
            Log.e(TAG,"User Details : "+name+userEmail+emailVerified+uid+photoUri);
        }
    }
}
