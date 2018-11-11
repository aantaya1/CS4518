package com.example.owner.petwars2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class VoteActivity extends AppCompatActivity {

    private TextView voteCatName;
    private ImageView voteCatImage;
    private Button voteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);

        voteCatName = findViewById(R.id.vote_cat_name);
        voteCatImage = findViewById(R.id.vote_image);
        voteButton = findViewById(R.id.vote_button);

        voteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Trigger intent to call upvote on cat
                //Then generate new view for new cat
            }
        });
    }
}

