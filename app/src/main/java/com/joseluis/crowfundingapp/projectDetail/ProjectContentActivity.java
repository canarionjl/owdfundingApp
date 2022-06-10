package com.joseluis.crowfundingapp.projectDetail;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.joseluis.crowfundingapp.R;

public class ProjectContentActivity
        extends AppCompatActivity implements ProjectContentContract.View {

    public static String TAG = ProjectContentActivity.class.getSimpleName();

    private ProjectContentContract.Presenter presenter;

    private Toolbar toolbar;
    private Button favouriteButton;


    //LIFECYCLE ACTIVITY METHODS

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_project_detail);

        configureToolbar();
        configureButtons();

        ProjectContentScreen.configure(this);

      if (savedInstanceState == null) {
           presenter.onStart();
        } else {
          presenter.onRestart();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        presenter.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    //BUTTONS CLICKED
    public void onAddFavouriteButtonClicked(){
        presenter.onAddFavouriteButtonClicked();
    }

    //DATA MANIPULATION

    @Override
    public void onDataUpdated(ProjectContentViewModel viewModel) {
        getSupportActionBar().setTitle(viewModel.title.toUpperCase());
        ((TextView)findViewById(R.id.textViewDescriptionDetailProduct)).setText(viewModel.description);
        ((TextView)findViewById(R.id.textViewDateAndHourDetailProduct)).setText(viewModel.date);
        ImageView imageView = findViewById(R.id.imageViewDetailContent);
        Log.e(TAG,viewModel.imageUrl);
        Glide.with(this).load(viewModel.imageUrl).placeholder(R.drawable.ic_baseline_error_24).into(imageView);

        updateFavouriteButtonState(viewModel);

    }

    @Override
    public void updateFavouriteButtonState(ProjectContentViewModel viewModel){
        if(!viewModel.invitedUser) {
            if (viewModel.isFavourite) {
                favouriteButton.setText(R.string.FavouriteButton);
            } else {
                favouriteButton.setText(R.string.NotFavouriteButton);
            }
        } else {
            favouriteButton.setClickable(false);
        }
    }



    //VIEW CONFIGURATION

    public void configureToolbar(){
        toolbar = findViewById(R.id.toolbarProjectDetailActivity);
        setSupportActionBar(toolbar);
    }

    public void configureButtons(){
        favouriteButton= findViewById(R.id.buttonAddFavouriteItem);
        favouriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAddFavouriteButtonClicked();
            }
        });

        ((ImageButton)findViewById(R.id.imageButtonCall)).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                configureProjectContactCall();
            }
        });

        ((ImageButton)findViewById(R.id.imageButtonLocation)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onMapButtonClicked();
            }
        });
    }



    @Override
    public void makeCall(String phone){
        if(phone==null){
            Toast.makeText(this,R.string.CallingError,Toast.LENGTH_SHORT).show();
            return;
        }else {
            Intent intentCall = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
            startActivity(intentCall);
        }
    }

    @Override
    public void showMap (String latitude, String longitude, String projectName){
        Intent intent = new Intent (Intent.ACTION_VIEW);
        intent.setData(Uri.parse("geo:<"+latitude+">,<"+longitude+">?q=<"+latitude+">,<"+longitude+">(Proyecto: "+projectName+")"));
        if(intent.resolveActivity(getPackageManager())!=null) startActivity(intent);
    }

    public void configureProjectContactCall(){
        presenter.configureProjectContactCall();
    }

    @Override
    public void onMapButtonClicked(){
        presenter.onMapButtonClicked();
    }



    //MVP METHODS

    @Override
    public void injectPresenter(ProjectContentContract.Presenter presenter) {
        this.presenter = presenter;
    }
}