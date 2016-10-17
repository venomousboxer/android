package com.hasgeek.funnel.session;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.hasgeek.funnel.data.SessionService;
import com.hasgeek.funnel.helpers.BaseActivity;
import com.hasgeek.funnel.R;
import com.hasgeek.funnel.data.DataManager;
import com.hasgeek.funnel.model.Proposal;
import com.hasgeek.funnel.model.Session;

import io.realm.RealmChangeListener;
import io.realm.RealmModel;

public class SessionActivity extends BaseActivity {

    public static final String EXTRA_SESSION_ID = "session_id";
    Session session;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);

        Intent intent = getIntent();
        final String sessionId = intent.getStringExtra(EXTRA_SESSION_ID);


        session = SessionService.getSessionById_Hot(getRealm(), sessionId);

        if(session==null) {
            finish();
            toast("No session with that ID found");
        }

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.session_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Feedback submitted \uD83D\uDC4C", Snackbar.LENGTH_LONG).show();
            }
        });

        initViews();

        session.addChangeListener(new RealmChangeListener<RealmModel>() {
            @Override
            public void onChange(RealmModel element) {
                initViews();
            }
        });

    }

    void initViews() {

        getSupportActionBar().setTitle(session.getTitle());

        getSupportActionBar().setSubtitle("By "+session.getSpeaker());



        TextView descriptionTv = (TextView) findViewById(R.id.activity_session_description);

        TextView speakerBioTv = (TextView) findViewById(R.id.activity_session_speaker_bio);

        descriptionTv.setText(session.getDescriptionText());
        speakerBioTv.setText(session.getSpeakerBioText());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // I do not want this...
                // Home as up button is to navigate to Home-Activity not previous activity
                super.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
