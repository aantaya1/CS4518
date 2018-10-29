package com.aantaya.petwars;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddCatActivity extends AppCompatActivity {

    public static final String EXTRA_IMAGE_ID = "image_id";
    public static final String EXTRA_NAME = "name";
    public static final String EXTRA_DESC = "desc";

    private EditText myEditImageIdView;
    private EditText myEditNameView;
    private EditText myEditDescView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_cat_activity);
        myEditImageIdView = findViewById(R.id.edit_image_id);
        myEditNameView = findViewById(R.id.edit_cat_name);
        myEditDescView = findViewById(R.id.edit_cat_desc);

        final Button button = findViewById(R.id.button_save);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent replyIntent = new Intent();
                if (TextUtils.isEmpty(myEditImageIdView.getText()) || TextUtils.isEmpty(myEditNameView.getText()) ||
                        TextUtils.isEmpty(myEditDescView.getText())) {
                    setResult(RESULT_CANCELED, replyIntent);
                } else {
                    String imageid = myEditImageIdView.getText().toString();
                    String name = myEditNameView.getText().toString();
                    String desc = myEditDescView.getText().toString();

                    replyIntent.putExtra(EXTRA_IMAGE_ID, imageid);
                    replyIntent.putExtra(EXTRA_NAME, name);
                    replyIntent.putExtra(EXTRA_DESC, desc);
                    setResult(RESULT_OK, replyIntent);
                }
                finish();
            }
        });
    }
}
