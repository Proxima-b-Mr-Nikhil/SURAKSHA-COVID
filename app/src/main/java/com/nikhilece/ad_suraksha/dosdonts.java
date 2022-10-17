package com.nikhilece.ad_suraksha;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class dosdonts extends AppCompatActivity {
    private TextView textView;
    private PDFView pdfView;
    ProgressBar progressBar;
    private FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference mref=database.getReference("dosdonts");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sympdf);
        pdfView=(PDFView)findViewById(R.id.pdfview);
        textView=(TextView)findViewById(R.id.textView);
        progressBar=(ProgressBar)findViewById(R.id.progressBar) ;


        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String Value=dataSnapshot.getValue(String.class);
                textView.setText(Value);
                Toast.makeText(dosdonts.this,"please wait...", Toast.LENGTH_LONG).show();
                String url=textView.getText().toString();
                new RetrivePdfStream().execute(url);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(dosdonts.this,"failed to load", Toast.LENGTH_LONG).show();

            }
        });}
    class RetrivePdfStream extends AsyncTask<String, Void, InputStream> {

        @Override
        protected void onPostExecute(InputStream inputStream) {

            pdfView.fromStream(inputStream).onLoad(new OnLoadCompleteListener() {

                @Override
                public void loadComplete(int nbPages) {
                    progressBar.setVisibility(View.GONE);
                }
            }).load();

        }
        @Override
        protected InputStream doInBackground(String... strings) {
            InputStream inputStream=null;
            try{
                URL url=new URL(strings[0]);
                HttpURLConnection urlConnection=(HttpURLConnection)url.openConnection();
                if(urlConnection.getResponseCode()==200)
                {

                    inputStream=new BufferedInputStream(urlConnection.getInputStream());
                }
            }catch (IOException e){
                return null;
            }
            return inputStream;


        }

    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        startActivity ( new Intent(dosdonts.this, home.class));
        Animatoo.animateSlideRight(dosdonts.this);
    }
}



