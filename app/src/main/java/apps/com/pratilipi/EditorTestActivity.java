package apps.com.pratilipi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.Session;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import apps.com.laser_native_editor.Editor;
import apps.com.laser_native_editor.EditorListener;
import apps.com.laser_native_editor.models.EditorTextStyle;
import apps.com.pratilipi.Utilities.Utils;

public class EditorTestActivity extends AppCompatActivity {
    Editor editor;

    // Replace APP_KEY from your APP_KEY
    final static private String APP_KEY = "bm3948zv7a5e70a";
    // Relace APP_SECRET from your APP_SECRET
    final static private String APP_SECRET = "uq6kggc5s9d7uo0";

    private final static String ACCESS_KEY = "bm3948zv7a5e70a";
    private final static String ACCESS_SECRET = "uq6kggc5s9d7uo0";

    private DropboxAPI<AndroidAuthSession> dropbox;
    private final static String FILE_DIR = "/Apps/";
    private final static String DROPBOX_NAME = "Pratilipi";
    //
//    private DropboxAPI<AndroidAuthSession> mDBApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor_test);
        editor = (Editor) findViewById(R.id.editor);
        setUpEditor();

        SharedPreferences prefs = getSharedPreferences(DROPBOX_NAME, 0);
        String accessKey = prefs.getString(ACCESS_KEY,null);
        Log.d("LOGIN",accessKey);

        AndroidAuthSession session;
        AppKeyPair pair = new AppKeyPair(ACCESS_KEY, ACCESS_SECRET);

        if (accessKey != null) {
            // callback method
            session = new AndroidAuthSession(pair);
            dropbox = new DropboxAPI<AndroidAuthSession>(session);
            dropbox.getSession().setOAuth2AccessToken(accessKey);
        }
        else{

            initialize_session();
        }



    }

    private void setUpEditor() {

        findViewById(R.id.action_insert_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.openImagePicker();
            }
        });

        Map<Integer, String> headingTypeface = getHeadingTypeface();
        Map<Integer, String> contentTypeface = getContentface();
        editor.setHeadingTypeface(headingTypeface);
        editor.setContentTypeface(contentTypeface);
        editor.setDividerLayout(R.layout.tmpl_divider_layout);
        editor.setEditorImageLayout(R.layout.tmpl_image_view);
        editor.setListItemLayout(R.layout.tmpl_list_item);

        editor.setEditorListener(new EditorListener() {
            @Override
            public void onTextChanged(EditText editText, Editable text) {

            }

            @Override
            public void onUpload(Bitmap image, String uuid) {
                Toast.makeText(EditorTestActivity.this, uuid, Toast.LENGTH_LONG).show();
                editor.onImageUploadComplete("http://www.videogamesblogger.com/wp-content/uploads/2015/08/metal-gear-solid-5-the-phantom-pain-cheats-640x325.jpg", uuid);
                // editor.onImageUploadFailed(uuid);

            }
        });
        editor.render();  // this method must be called to start the editor
        findViewById(R.id.btnRender).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                Retrieve the content as serialized, you could also say getContentAsHTML();
                */

                boolean isInternetAvailable = Utils.isInternetAvailable(getApplicationContext());
                if (isInternetAvailable) {

                } else {
                    Toast.makeText(getApplicationContext(), "Please turn on internet for syncing data", Toast.LENGTH_LONG).show();
                }

                String text = editor.getContentAsSerialized();
                Log.d("STRINGS ++++++" , text);
                Intent intent = new Intent(getApplicationContext(), RenderTestActivity.class);
                intent.putExtra("content", text);
                startActivity(intent);
            }
        });

        findViewById(R.id.btnSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UploadFileToDropbox upload = new UploadFileToDropbox(EditorTestActivity.this, dropbox,
                        FILE_DIR);
                upload.execute();
            }
        });
    }

    public static void setGhost(Button button) {
        int radius = 4;
        GradientDrawable background = new GradientDrawable();
        background.setShape(GradientDrawable.RECTANGLE);
        background.setStroke(4, Color.WHITE);
        background.setCornerRadius(radius);
        button.setBackgroundDrawable(background);
    }

    private void render() {
        String x = "<h2 id=\"installation\" style=\"font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;color:#c00; margin-top: -80px !important;\">Installation</h2>" +
                "<h3 id=\"requires-html5-doctype\" style=\"font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif; margin-bottom: 8px; margin-right: 0px; margin-left: 0px;\">Requires HTML5 doctype</h3>" +
                "<p style=\"font-size: 14px; color: rgb(104, 116, 127);\">Bootstrap uses certain HTML elements and CSS properties which require HTML5 doctype. Include&nbsp;<code style=\"font-size: 12.6px;\">&lt;!DOCTYPE html&gt;</code>&nbsp;in the beginning of all your projects.</p>" +
                "<img src=\"http://www.scifibloggers.com/wp-content/uploads/TOR_2.jpg\" />" +
                "<h2 id=\"integration\" style=\"font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif; margin-top: -80px !important;\">Integration</h2>" +
                "<p style=\"font-size: 14px; color: rgb(104, 116, 127);\">3rd parties available in django, rails, angular and so on.</p>" +
                "<h3 id=\"django\" style=\"font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif; margin-bottom: 8px; margin-right: 0px; margin-left: 0px;\">Django</h3>" +
                "<p style=\"font-size: 14px; color: rgb(104, 116, 127);\">Handy update for your django admin page.</p>" +
                "<ul style=\"color: rgb(51, 51, 51);\"><li style=\"font-size: 14px; color: rgb(104, 116, 127);\"><a href=\"https://github.com/summernote/django-summernote\" target=\"_blank\">django-summernote</a></li><li style=\"font-size: 14px; color: rgb(104, 116, 127);\"><a href=\"https://pypi.python.org/pypi/django-summernote\" target=\"_blank\">summernote plugin for Django</a></li></ul>" +
                "<h3 id=\"ruby-on-rails\" style=\"font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif; margin-bottom: 8px; margin-right: 0px; margin-left: 0px;\">Ruby On Rails</h3>" +
                "<p style=\"font-size: 14px; color: rgb(104, 116, 127);\">This gem was built to gemify the assets used in Summernote.</p>" +
                "<ul style=\"color: rgb(51, 51, 51);\"><li style=\"font-size: 14px; color: rgb(104, 116, 127);\"><a href=\"https://github.com/summernote/summernote-rails\" target=\"_blank\">summernote-rails</a></li></ul>" +
                "<h3 id=\"angularjs\" style=\"font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif; margin-bottom: 8px; margin-right: 0px; margin-left: 0px;\">AngularJS</h3>" +
                "<p style=\"font-size: 14px; color: rgb(104, 116, 127);\">AngularJS directive to Summernote.</p>" +
                "<ul style=\"color: rgb(51, 51, 51);\"><li style=\"font-size: 14px; color: rgb(104, 116, 127);\"><a href=\"https://github.com/summernote/angular-summernote\">angular-summernote</a></li></ul>" +
                "<h3 id=\"apache-wicket\" style=\"font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif; margin-bottom: 8px; margin-right: 0px; margin-left: 0px;\">Apache Wicket</h3>" +
                "<p style=\"font-size: 14px; color: rgb(104, 116, 127);\">Summernote widget for Wicket Bootstrap.</p>" +
                "<ul style=\"color: rgb(51, 51, 51);\"><li style=\"font-size: 14px; color: rgb(104, 116, 127);\"><a href=\"http://wb-mgrigorov.rhcloud.com/summernote\" target=\"_blank\">demo</a></li><li style=\"font-size: 14px; color: rgb(104, 116, 127);\"><a href=\"https://github.com/l0rdn1kk0n/wicket-bootstrap/tree/4f97ca783f7279ca43f9e2ee790703161f59fa40/bootstrap-extensions/src/main/java/de/agilecoders/wicket/extensions/markup/html/bootstrap/editor\" target=\"_blank\">source code</a></li></ul>" +
                "<h3 id=\"webpack\" style=\"font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif; margin-bottom: 8px; margin-right: 0px; margin-left: 0px;\">Webpack</h3>" +
                "<p style=\"font-size: 14px; color: rgb(104, 116, 127);\">Example about using summernote with webpack.</p>" +
                "<ul style=\"color: rgb(51, 51, 51);\"><li style=\"font-size: 14px; color: rgb(104, 116, 127);\"><a href=\"https://github.com/hackerwins/summernote-webpack-example\" target=\"_blank\">summernote-webpack-example</a></li></ul>" +
                "<h3 id=\"meteor\" style=\"font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif; margin-bottom: 8px; margin-right: 0px; margin-left: 0px;\">Meteor</h3>" +
                "<p style=\"font-size: 14px; color: rgb(104, 116, 127);\">Example about using summernote with meteor.</p>" +
                "<ul style=\"color: rgb(51, 51, 51);\"><li style=\"font-size: 14px; color: rgb(104, 116, 127);\"><a href=\"https://github.com/hackerwins/summernote-meteor-example\" target=\"_blank\">summernote-meteor-example</a></li></ul>" +
                "<p style=\"font-size: 14px; color: rgb(104, 116, 127);\"><br></p>";
        editor.render();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == editor.PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));
                editor.insertImage(bitmap);
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            //Write your code if there's no result
            Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_SHORT).show();
            // editor.RestoreState();
        } else if (requestCode == editor.MAP_MARKER_REQUEST) {
            editor.insertMap(data.getStringExtra("cords"));
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Exit Editor?")
                .setMessage("Are you sure you want to exit the editor?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
//        setGhost((Button) findViewById(R.id.btnRender));
    }

    public Map<Integer, String> getHeadingTypeface() {
        Map<Integer, String> typefaceMap = new HashMap<>();
        typefaceMap.put(Typeface.NORMAL, "fonts/GreycliffCF-Bold.ttf");
        typefaceMap.put(Typeface.BOLD, "fonts/GreycliffCF-Heavy.ttf");
        typefaceMap.put(Typeface.ITALIC, "fonts/GreycliffCF-Heavy.ttf");
        typefaceMap.put(Typeface.BOLD_ITALIC, "fonts/GreycliffCF-Bold.ttf");
        return typefaceMap;
    }

    public Map<Integer, String> getContentface() {
        Map<Integer, String> typefaceMap = new HashMap<>();
        typefaceMap.put(Typeface.NORMAL, "fonts/Lato-Medium.ttf");
        typefaceMap.put(Typeface.BOLD, "fonts/Lato-Bold.ttf");
        typefaceMap.put(Typeface.ITALIC, "fonts/Lato-MediumItalic.ttf");
        typefaceMap.put(Typeface.BOLD_ITALIC, "fonts/Lato-BoldItalic.ttf");
        return typefaceMap;
    }


    /**
     *  Initialize the Session of the Key pair to authenticate with dropbox
     *
     */
    protected void initialize_session(){

        // store app key and secret key
        AppKeyPair appKeys = new AppKeyPair(APP_KEY, APP_SECRET);
        AndroidAuthSession session = new AndroidAuthSession(appKeys);
        //Pass app key pair to the new DropboxAPI object.
        dropbox = new DropboxAPI<AndroidAuthSession>(session);
        // MyActivity below should be your activity class name
        //start session
        dropbox.getSession().startOAuth2Authentication(this);
    }





    protected void onResume() {
        super.onResume();

        if (dropbox.getSession().authenticationSuccessful()) {
            try {
                // Required to complete auth, sets the access token on the session
                dropbox.getSession().finishAuthentication();

                String accessToken = dropbox.getSession().getOAuth2AccessToken();
                Log.d("LOGIN",accessToken);

                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString(ACCESS_KEY,accessToken);


            } catch (IllegalStateException e) {
                Log.i("DbAuthLog", "Error authenticating", e);
            }
        }
    }
}


