package com.example.studentprofile;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import  android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class ArticleListVolly extends AppCompatActivity implements OnClickListener {

    String domain_name;

    private RequestQueue requestQueue;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    private List<ArticleItem> articleItems;

    private ProgressBar progressBar;

    private Button btnSumbit;

    private TextView addTitle;
    private TextView addContent;
    private TextView addDescription;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_list);

        requestQueue = Volley.newRequestQueue(this);
        this.domain_name =this.getResources().getString(R.string.domain_name)+this.getResources().getString(R.string.articles);

        recyclerView = (RecyclerView) findViewById(R.id.article_list);
        recyclerView.setHasFixedSize(true);
        articleItems = new ArrayList<>();

        loadRecyclerViewData();

        btnSumbit = (Button) findViewById(R.id.btnSumit);

        addTitle = (TextView) findViewById(R.id.add_title);
        addContent = (TextView) findViewById(R.id.add_content);
        addDescription = (TextView) findViewById(R.id.add_description);

        btnSumbit.setOnClickListener(this);
    }

    private void loadRecyclerViewData(){
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        progressBar.setVisibility(View.VISIBLE);

//        to fetch the data We nead new string request from the server
//        We use StringRequest from the volley
//        param 1: the type of  HTTP Request GET or POST
//        param 2: the URL - domain_name
//        param 3: the Listner (Response and Error Listner

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                domain_name,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.INVISIBLE);
                        try {
//                            JSONObject jsonObject = new JSONObject(response);
//                            JSONArray array = jsonObject.getJSONArray("article");
                            JSONArray array = new JSONArray(response);


                            for(int i=0; i<array.length(); i++){
                                JSONObject articleObj = (JSONObject) array.get(i);
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
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

//        Now we have a request to excute this request we need Request Queue object
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        We add the request to our request queue
        requestQueue.add(stringRequest);
    }

    @Override
    public void onClick(View v) {
        if(v==btnSumbit){

            JSONObject postparams = new JSONObject();
            try {
                postparams.put("title", addTitle.getText());
                postparams.put("content", addContent.getText());
                postparams.put("description", addDescription.getText());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    domain_name,
                    postparams,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }
            );

            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                    DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            ));

//            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(jsonObjectRequest);

            articleItems = new ArrayList<>();
            addTitle.setText("");
            addContent.setText("");
            addDescription.setText("");

            loadRecyclerViewData();
        }
    }
}

//public class MyRetryPolicyWithoutRetry implements RetryPolicy
//{
//    @Override
//    public int getCurrentTimeout()
//    {
//        return CONNECTION_TIME_OUT; /200000/
//    }
//
//    @Override
//    public int getCurrentRetryCount()
//    {
//        return 0;
//    }
//
//    @Override
//    public void retry(VolleyError error) throws VolleyError
//    {
//        throw(error);
//    }
//}
