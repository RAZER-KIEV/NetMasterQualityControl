package ua.kiev.netmaster.netmasterqualitycontrol.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.Gson;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import ua.kiev.netmaster.netmasterqualitycontrol.R;
import ua.kiev.netmaster.netmasterqualitycontrol.domain.Employee;
import ua.kiev.netmaster.netmasterqualitycontrol.domain.MyDownTask;
import ua.kiev.netmaster.netmasterqualitycontrol.fragments.CreateRegisterDialog;
import ua.kiev.netmaster.netmasterqualitycontrol.loger.L;

import static android.Manifest.permission.READ_CONTACTS;

//import ua.kiev.netmaster.agro.R;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity implements CreateRegisterDialog.RegisterDialogCommunicator {
    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private final int REQUEST_READ_CONTACTS = 0;
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private MyDownTask mAuthTask = null;
    // UI references.
    private AutoCompleteTextView loginView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private String result="", access, res;
    private String login, password;
    public static final String LOG = "myLogs";
    private LoginButton loginButton;
    private Profile profile;
    private MyApplication myApplication;


    //FB
    private CallbackManager callbackManager;
    private FacebookCallback<LoginResult> facebookCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
           // AccessToken accessToken = loginResult.getAccessToken();
           myApplication.setFbProfile(profile = Profile.getCurrentProfile());
            if(profile!=null){
                L.l("LoginActivity. FacebookCallback : onSuccess() " + profile.getFirstName() + " " + profile.getLastName());
                loginWithFb();
            }
        }

        @Override
        public void onCancel() {
            Log.d(LoginActivity.LOG, "LoginActivity. FacebookCallback : onCancel() ");
        }

        @Override
        public void onError(FacebookException error) {
            Log.d(LoginActivity.LOG, "LoginActivity. FacebookCallback : onError() ");
        }
    };

    private void loginWithFb(){
        while (!authenticate(profile.getFirstName(), profile.getFirstName() + profile.getId())) {
            registerDialogData(profile.getFirstName(), profile.getFirstName() + profile.getId());
        }
        goToMainActivity();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        L.l("onCreate()", this);
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        myApplication = (MyApplication) getApplication();
        setContentView(R.layout.activity_login);
        // Set up the login form.
        CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
        callbackManager = CallbackManager.Factory.create();
       // mTokenTracker.startTracking();
       // mProfileTracker.startTracking();
        initViews();
        profile=Profile.getCurrentProfile();
        if(profile!=null) {
            L.l("onCreate() profile.toString() = " + profile.toString(), this);
            loginWithFb();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStart() {
        super.onStart();
        loginButton.setReadPermissions("public_profile");
        loginButton.registerCallback(callbackManager, facebookCallback);
    }

    private void initViews() {
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions();
        loginView = (AutoCompleteTextView) findViewById(R.id.email);
        //populateAutoComplete();
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                        attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();

            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
        Log.d(LOG, "LoginActivity. onPostResume()");
       /* if(profile!=null){
            myApplication.setFbProfile(profile);
            loginWithFb();
        }*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        showProgress(false);
        access = null;
        Log.d(LOG, "LoginActivity. onResume()");
    }

   /* private void populateAutoComplete() {
        Log.d(LOG, "LoginActivity. populateAutoComplete()");
        if (!mayRequestContacts()) {
            return;
        }
        getLoaderManager().initLoader(0, null, this);
    }*/

    private boolean mayRequestContacts() {
        Log.d(LOG, "LoginActivity. mayRequestContacts()");
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(loginView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    /*@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.d(LOG,"LoginActivity. onRequestPermissionsResult()");
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }*/


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin(){
        Log.d(LOG,"LoginActivity. attemptLogin()");


        // Reset errors.
        loginView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.// TODO: 22.12.2015
        login = loginView.getText().toString();
            myApplication.setLogin(login);
        password = mPasswordView.getText().toString();
            myApplication.setPassword(password);

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }
        // Check for a valid login address.
        if (TextUtils.isEmpty(login)) {
            loginView.setError(getString(R.string.error_field_required));
            focusView = loginView;
            cancel = true;
        }
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
          if(authenticate(login, password))goToMainActivity();
            else {
              Toast.makeText(this,"Authentication Error!",Toast.LENGTH_SHORT).show();
              new CreateRegisterDialog().show(getFragmentManager(), "RegisterDialog");
              //onResume();
          }
        }
    }


    private boolean authenticate(String login,String password){
        Log.d(LOG, "LoginActivity. authenticate()");
        showProgress(true);
        Map<String, String> params=new HashMap<>();
        params.put(getString(R.string.login),login);
        params.put(getString(R.string.password),password);
        mAuthTask = new MyDownTask(params,this);
        try {
            result = mAuthTask.execute().get().trim();
            if(result.equals(getString(R.string.server_unreachable))){
                L.t(result,this);
                L.l(result,this);
                return false;
            }
            Log.d(LOG,"LoginActivity. authenticate(). result= "+result);
        } catch (InterruptedException|ExecutionException e) {
            e.printStackTrace();
        }
        int spaceIndex = result.indexOf(" ");
        if(spaceIndex>=0) {
            res = result.substring(spaceIndex).trim();
            Log.d(LOG,"LoginActivity. authenticate(). res= "+res);
            access = result.substring(0, spaceIndex).trim();
            Log.d(LOG,"LoginActivity. authenticate(). access= "+access);
        }
        if (access!=null&&access.equals("Auth_Success")) {
            L.l("access!=null&&access.equals(\"Auth_Success\")", this);
            Employee me1 = myApplication.getGson().fromJson(res, Employee.class);
            L.l("me1 = " + me1, this);
            myApplication.setMe(myApplication.getGson().fromJson(res, Employee.class));
            L.l("me: "+ myApplication.getMe());
            L.l("LoginActivity. trying start MainActivity");
            return true;
        }else {
            return false;
        }
    }

    private void goToMainActivity(){
        Intent intentSuccess = new Intent(this, MainActivity.class);
        startActivity(intentSuccess);
    }

    private boolean isPasswordValid(String password) {
        Log.d(LOG, "LoginActivity. isPasswordValid()");
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        Log.d(LOG, "LoginActivity. showProgress()");
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /*@Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        Log.d(LOG, "LoginActivity. onCreateLoader()");
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }*/

   /* @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        Log.d(LOG,"LoginActivity. onLoadFinished()");
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }
        addEmailsToAutoComplete(emails);
    }*/

    /*@Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        Log.d(LOG, "LoginActivity. onLoaderReset()");
    }*/

   /* private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        Log.d(LOG, "LoginActivity. addEmailsToAutoComplete()");
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        loginView.setAdapter(adapter);
    }*/

    @Override
    public void registerDialogData(String login, String password) {
        Map<String,String> params = new HashMap<>();
        params.put(getString(R.string.urlTail), getString(R.string.addEmpl));
        params.put(getString(R.string.login),login);
        params.put(getString(R.string.password), password);
        try {
            String res = new MyDownTask(params,this).execute().get();
            Toast.makeText(this,"User created: " +res, Toast.LENGTH_LONG).show();
            if (res.equals(getString(R.string.server_unreachable))){
                L.t(res,this);
            } else if(Long.parseLong(res)>0) attemptLogin();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addEmplToMyNetwork(Long emplId) {
        //not Implemented;
    }


    /*private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }*/

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(LOG,"LoginActivity. onPause()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(LOG, "LoginActivity. onDestroy()");
    }
}

