package com.robosoft.flickrapp.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.ShareApi;
import com.facebook.share.model.ShareLinkContent;
import com.robosoft.flickrapp.R;
import com.robosoft.flickrapp.utills.Constants;

import java.util.Arrays;
import java.util.List;

/**
 * Created by rahul on 3/6/16.
 */
public class FacebookShareActivity extends AppCompatActivity implements Constants.FbSharing{

    private CallbackManager mCallbackManager;
    private String mShareUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.activity_facebook_share);
        Bundle bundle = getIntent().getExtras();
        mShareUrl = bundle.getString(FB_SHARE_KEY);

        mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        List<String> permissionNeeds = Arrays.asList(PERMISSION);

        LoginManager loginManager = LoginManager.getInstance();
        loginManager.logInWithPublishPermissions(this, permissionNeeds);

        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                shareLink();
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private  void shareLink(){

        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse(mShareUrl))
                .build();

        ShareApi.share(content, null);
        Toast.makeText(this , getResources().getString(R.string.fb_share_msg) ,Toast.LENGTH_SHORT).show();
    }
}
