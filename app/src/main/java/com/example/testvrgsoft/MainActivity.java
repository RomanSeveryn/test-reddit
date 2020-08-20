package com.example.testvrgsoft;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ExampleAdapter.OnItemClickListener {
    public static final String EXTRA_URL = "imageUrl";
    public static final String EXTRA_NAME = "nameAuthor";
    public static final String EXTRA_COMMENT = "comment";

    private RecyclerView recyclerView;
    private ExampleAdapter exampleAdapter;
    private ArrayList<ExampleItem> exampleList;
    private RequestQueue requestQueue;

    private NestedScrollView nestedScrollView;
    private ProgressBar progressBar;

    int limit = 3;
    String after = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nestedScrollView = findViewById(R.id.nestedScrollView);
        progressBar = findViewById(R.id.progressBar);

        recyclerView = findViewById(R.id.recycleView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    progressBar.setVisibility(View.VISIBLE);

                    parseJson();
                }
            }
        });


        exampleList = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);
        parseJson();
    }

    private void parseJson() {
        String url = "https://www.reddit.com//top.json?limit=" + limit + "&after=" + after;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONObject("data").getJSONArray("children");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject child = jsonArray.getJSONObject(i).getJSONObject("data");

                                String name = child.getString("author");
                                int comment = child.getInt("num_comments");
                                String image = child.getString("thumbnail");
                                exampleList.add(new ExampleItem(image, name, comment));
                            }

                            after = response.getJSONObject("data").getString("after");

                            exampleAdapter = new ExampleAdapter(MainActivity.this, exampleList);
                            recyclerView.setAdapter(exampleAdapter);
                            exampleAdapter.setOnItemClickListener(MainActivity.this);

                            progressBar.setVisibility(View.GONE);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(request);
    }

    //Transfer post to Detail activity - start
    @Override
    public void onItemClick(int position) {
        Intent detailIntent = new Intent(this, DetailActivity.class);
        ExampleItem clickItem = exampleList.get(position);
        detailIntent.putExtra(EXTRA_URL, clickItem.getImageViewUrl());
        detailIntent.putExtra(EXTRA_NAME, clickItem.getNameAuthor());
        detailIntent.putExtra(EXTRA_COMMENT, clickItem.getComment());
        startActivity(detailIntent);
    }
    //Transfer post to Detail activity - end
}