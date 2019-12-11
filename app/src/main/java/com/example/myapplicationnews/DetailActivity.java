package com.example.myapplicationnews;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class DetailActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView titleTextView, detailTextView;
    private String detailString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        imageView = findViewById(R.id.imageView);
        titleTextView = findViewById(R.id.textView);
        detailTextView = findViewById(R.id.detailTextView);

        titleTextView.setText(getIntent().getStringExtra("title"));
        Picasso.get().load(getIntent().getStringExtra("image")).into(imageView);
        Content content = new Content();
        content.execute();
    }

    private  class  Content extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            detailTextView.setText(detailString);

        }



        @Override
        protected Void doInBackground(Void... voids) {
            try{
                String baseUrl = "https://yandex.ru";
                String detailUrl = getIntent().getStringExtra("detailUrl");
                String url = baseUrl + detailUrl;

                Document doc = Jsoup.connect(url)
                        //.userAgent("Chrome/13.0.782.112 ")
                        //.referrer("https://yandex.ru/")
                        //.timeout(1000*7)
                       // .ignoreHttpErrors(true)
                        .get();

                Elements data = doc.select(".doc__content");

                detailString = data.select(".doc__text")
                        .text();


            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
