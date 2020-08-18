package com.example.studentprofile;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ArticleList extends AppCompatActivity {

    String domain_name;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    private List<ArticleItem> articleItems;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_list);

        this.domain_name =this.getResources().getString(R.string.domain_name);

        Intent DataIntent = getIntent(); //receiving the intent

        JSONArray obj = new JSONArray();
        try {
            obj = new JSONArray(DataIntent.getStringExtra("displayLogin"));
            //the received response from server was JSON serialized coverting it back to JSON Array
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            Log.i("Json", "SignIN: "+((JSONObject)((JSONObject)obj.get(0)).get("fields")).get("key"));
        } catch (JSONException e) {

            e.printStackTrace();
        }

        AsyncRequest P=new AsyncRequest("GET",this);        //AsyncRequest object to snrd request
        P.setUrl(domain_name+this.getResources().getString(R.string.articles));                                    //url to which to send request
        P.start();
        try
        {
            P.join();                                                           //procced after process P is completed
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        Log.i("FORM:---", "onCreate: "+P.getResponseBody());

        recyclerView = (RecyclerView) findViewById(R.id.article_list);
        recyclerView.setHasFixedSize(true);
        articleItems = new ArrayList<>();



        JSONArray articleArray = new JSONArray();
        try {
            articleArray = new JSONArray(P.getResponseBody());

            for(int i=0; i<articleArray.length(); i++){
                JSONObject articleObj = (JSONObject) articleArray.get(i);
                ArticleItem item = new ArticleItem(
                        articleObj.getString("id"),
                        articleObj.getString("title"),
                        articleObj.getString("content"),
                        articleObj.getString("description")
                );
                articleItems.add(item);
            }
            adapter = new ArticleAdapter(articleItems, getApplicationContext());
            recyclerView.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }







//        TextView articles = (TextView) findViewById(R.id.articles);
//
//        articles.setText(P.getResponseBody());

    }
}
