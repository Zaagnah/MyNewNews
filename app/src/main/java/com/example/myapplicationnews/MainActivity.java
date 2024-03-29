package com.example.myapplicationnews;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

/*В кратце, я пишу парсер новостей с Яндекс-новости. На данный момент реализовано:
1-Парсинг заголовков новостей
2-Парсинг img заставок.
3-Открывается второе активити с подробностями конкретной новости
Не реализовано:
1- IMG заставки "сползают" на другие новости .
2- Нужно добавить скролл
Ведется работа по устранению недоделок.
*/





public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ParseAdapter adapter;
    private ArrayList<ParseItem> parseItems = new ArrayList<>();
    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recyclerView);
        adapter = new ParseAdapter(parseItems,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        Content content = new Content();
        content.execute();

    }
    private  class  Content extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            progressBar.startAnimation(AnimationUtils.loadAnimation(MainActivity.this,android.R.anim.fade_in));

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressBar.setVisibility(View.GONE);
            progressBar.startAnimation(AnimationUtils.loadAnimation(MainActivity.this,android.R.anim.fade_out));
            adapter.notifyDataSetChanged();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try{
                String url = "https://yandex.ru/news/region/Vladimir";
                Document doc = Jsoup.connect(url)
                        .timeout(1000*7)
                        .get();
                Elements data = doc.select(".page-content__cell");

                int size = data.size();
                for(int i = 0;i<=size;i++){
                    String imgUrl = data.select(".page-content__cell")
                            .select("img")
                            .eq(i)
                            .attr("src");
                    String title = data.select(".story__title")// мб косяк тут.
                            .select("a")
                            .eq(i)
                            .text();
                    String detailUrl = data.select(".story__title")
                            .select("a")
                            .eq(i)
                            .attr("href");// check
                    parseItems.add(new ParseItem(imgUrl,title,detailUrl));
                    Log.d("items","img:" + imgUrl + " . title: " + title);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
