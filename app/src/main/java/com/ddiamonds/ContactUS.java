package com.ddiamonds;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ContactUS extends AppCompatActivity implements OnClickListener {

    ImageView btn_send;
    TextInputEditText ed_name, ed_email, ed_phone, ed_subject, ed_message;
    TextView tv_fb, tv_message, tv_homepage, tv_phone, tv_instagram;
    ImageView img_home;
    AsyncHttpClient client;
    private ProgressDialog progressDialog;
    public static String FACEBOOK_URL = "https://www.facebook.com/pg/D.Diamonds.Jewellery/";
    public static String FACEBOOK_PAGE_ID = "D.Diamonds.Jewellery/";

    static final Integer CALL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_us);

        client = new AsyncHttpClient();
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        img_home = (ImageView) findViewById(R.id.img_home);
        btn_send = (ImageView) findViewById(R.id.btn_send);
        ed_name = (TextInputEditText) findViewById(R.id.ed_name);
        ed_email = (TextInputEditText) findViewById(R.id.ed_email);
        ed_phone = (TextInputEditText) findViewById(R.id.ed_phone);
        ed_subject = (TextInputEditText) findViewById(R.id.ed_subject);
        ed_message = (TextInputEditText) findViewById(R.id.ed_message);

        tv_fb = (TextView) findViewById(R.id.tv_fb);
        tv_message = (TextView) findViewById(R.id.tv_message);
        tv_homepage = (TextView) findViewById(R.id.tv_homepage);
        tv_phone = (TextView) findViewById(R.id.tv_phone_number);
        tv_instagram = (TextView) findViewById(R.id.tv_instagram);
        tv_fb.setOnClickListener(this);
        tv_message.setOnClickListener(this);
        tv_homepage.setOnClickListener(this);
        tv_phone.setOnClickListener(this);
        tv_instagram.setOnClickListener(this);

        img_home.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(ContactUS.this, Home.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });

        btn_send.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                String Name = ed_name.getText().toString();
                String Phone = ed_phone.getText().toString();
                String Email = ed_email.getText().toString();
                String Subject = ed_subject.getText().toString();
                String Message = ed_message.getText().toString();

                boolean ValidEmail = isEmailValid(Email);

                if (Name.equals("") || Phone.equals("") || Email.equals("")
                        || Subject.equals("") || Message.equals("")
                        || !ValidEmail) {

                    if (Name.equals("")) {
                        showAlert(getString(R.string.please_enter_name));
                    } else if (Phone.equals("")) {
                        showAlert(getString(R.string.please_enter_phone));
                    } else if (Email.equals("")) {
                        showAlert(getString(R.string.please_enter_email));
                    } else if (Subject.equals("")) {
                        showAlert(getString(R.string.please_enter_subject));
                    } else if (Message.equals("")) {
                        showAlert(getString(R.string.please_enter_message));
                    } else if (!ValidEmail) {
                        showAlert(getString(R.string.please_enter_valid_email));
                    }
                } else {
                    ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo info = cm.getActiveNetworkInfo();

                    if (info == null) {
                        showAlert(getString(R.string.check_connection));
                    } else if (info != null) {
                        if (!info.isConnected()) {
                            showAlert(getString(R.string.check_connection));
                        }

                        // if positive, fetch the articles in background
                        else {
                            progressDialog = new ProgressDialog(ContactUS.this);
                            progressDialog.setMessage("Loading ...");
                            progressDialog.setIndeterminate(false);
                            progressDialog.setCancelable(false);
                            progressDialog.show();

                            RequestParams params = new RequestParams();

                            params.put("name", Name);
                            params.put("email", Email);
                            params.put("phone", Phone);
                            params.put("subject", Subject);
                            params.put("msg", Message);
                            params.put("username", Constants.App_Username);
                            params.put("password", Constants.App_Pass);

                            client.get(Constants.ContactURL, params, new JsonHttpResponseHandler() {
                                @Override
                                public void onSuccess(org.json.JSONObject arg0) {
                                    progressDialog.dismiss();

                                    String status;
                                    try {
                                        status = arg0.getString("status");


                                        if (status.equals("1")) {
                                            ed_name.setText("");
                                            ed_phone.setText("");
                                            ed_email.setText("");
                                            ed_subject.setText("");
                                            ed_message.setText("");

                                            showAlert(getString(R.string.message_sent_successfully));
                                        } else {
                                            showAlert(getString(R.string.something_wrong));
                                        }

                                    } catch (JSONException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }
                                }

                                public void onFailure(Throwable arg0, org.json.JSONArray arg1) {

                                    progressDialog.dismiss();
                                }

                                ;

                                @Override
                                public void onFailure(Throwable arg0, String arg1) {

                                    progressDialog.dismiss();
                                }

                                ;
                            });

                        }
                    }
                }

            }
        });
    }

    public void showAlert(String Message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(Message).setTitle(R.string.alert_title)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // do things
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    @Override
    public void onClick(View view) {
        Uri uri;
        switch (view.getId()) {
            case R.id.tv_fb:
                Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
                String facebookUrl = getFacebookPageURL(this);
                facebookIntent.setData(Uri.parse(facebookUrl));
                startActivity(facebookIntent);
                break;
            case R.id.tv_message:
                sendEmail();
                break;
            case R.id.tv_homepage:
                uri = Uri.parse("http://" + getResources().getString(R.string.website)); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                break;
            case R.id.tv_phone_number:
                askForPermission(Manifest.permission.CALL_PHONE, CALL);
                break;
            case R.id.tv_instagram:
                uri = Uri.parse("http://instagram.com/" + getResources().getString(R.string.instagram_page));
                Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);
                likeIng.setPackage("com.instagram.android");

                try {
                    startActivity(likeIng);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://instagram.com/" + getResources().getString(R.string.instagram_page))));
                }
                break;
        }
    }

    //method to get the right URL to use in the intent
    public String getFacebookPageURL(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app
                return FACEBOOK_URL;
            } else { //older versions of fb app
                return "fb://" + FACEBOOK_PAGE_ID;
            }
        } catch (PackageManager.NameNotFoundException e) {
            return FACEBOOK_URL; //normal web url
        }
    }

    protected void sendEmail() {
        String[] TO = {getResources().getString(R.string.email)};
        String[] CC = {""};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message goes here");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));

        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(ContactUS.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(ContactUS.this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(ContactUS.this, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(ContactUS.this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(ContactUS.this, new String[]{permission}, requestCode);
            }
        } else {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + getResources().getString(R.string.phone_number)));
            startActivity(callIntent);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {
                case 1:
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + getResources().getString(R.string.phone_number)));
                    startActivity(callIntent);
                    break;
            }
        }
    }
}
