package shirtish.worldnews.firebase;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

import shirtish.worldnews.Constants;

public class FirebaseAuthenticate {
    private FirebaseAuth firebaseAuth;

    private final Activity activity;
    private final Context context;

    private GoogleSignInClient googleSignInClient;

    public FirebaseAuthenticate(Activity activity, Context context) {
        this.activity = activity;
        this.context = context;

        setFirebaseAuthAndCheckIfUserIsSignedIn();
        integratingGoogleSignInAndSetGoogleSignInClient();
    }

    public boolean isLoggedIn() {
        return firebaseAuth.getCurrentUser() != null;
    }

    private void setFirebaseAuthAndCheckIfUserIsSignedIn() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public FirebaseAuth getFirebaseAuth() {
        return firebaseAuth;
    }

    private void integratingGoogleSignInAndSetGoogleSignInClient() {
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(Constants.DEFAULT_WEB_CLIENT_ID)
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(context, googleSignInOptions);
    }

    public void signIn() {
        if (googleSignInClient != null) {
            Intent signInIntent = googleSignInClient.getSignInIntent();
            activity.startActivityForResult(signInIntent, Constants.REQUEST_SIGN_IN);
        }
    }

    public void signOut() {
        firebaseAuth.signOut();
    }
}
