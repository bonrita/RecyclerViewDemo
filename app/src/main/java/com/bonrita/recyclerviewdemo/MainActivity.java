package com.bonrita.recyclerviewdemo;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements RecyclerAdapter.AdapterClickListener {
    // https://www.youtube.com/watch?v=ovmWgYxOCug&list=PLk7v1Z2rk4hjHrGKo9GqOtLs1e2bglHHA&index=5

    private static final String SOURCE_URL = "https://teamtreehouse.com/matthew.json";

    private RecyclerView recyclerView;
    private RecyclerAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private List<ListItem> listItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.listItems = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.recyler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        loadRemoteData(new VolleyCallback() {
            @Override
            public void onSuccess(List<ListItem> result, RecyclerAdapter.AdapterClickListener listener) {
                adapter = new RecyclerAdapter(result, getApplicationContext());
                adapter.setAdapterClickListener(listener);
                recyclerView.setAdapter(adapter);
            }
        }, this);

    }

    @Override
    public void itemClicked(View v, int position) {
        // Get a list item at the current position.
        ListItem item = this.listItems.get(position);
        Toast.makeText(getApplicationContext(), "Clicked on '" + item.getHeading() + "'", Toast.LENGTH_LONG).show();
    }

    /**
     * Load remote data.
     * 
     * @param callback
     * @param listener
     */
    private void loadRemoteData(final VolleyCallback callback, final RecyclerAdapter.AdapterClickListener listener) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading data ....");
        progressDialog.show();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, SOURCE_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
                try {
                    JSONArray badges = response.getJSONArray("badges");

                    for (int i = 0; i < badges.length(); i++) {
                        JSONObject item = badges.getJSONObject(i);
                        // System.out.println(item.get("name"));

                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.ENGLISH);
                        Date newDate = format.parse(item.getString("earned_date"));

                        SimpleDateFormat newFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
                        // System.out.println(newFormat.format(newDate));

                        ListItem listItem = new ListItem(item.getString("name"), newFormat.format(newDate), item.getString("icon_url"));
                        listItems.add(listItem);
                        // "2013-09-12T00:35:56.000Z",
                    }

                    callback.onSuccess(listItems, listener);

                } catch (JSONException e) {
                    progressDialog.dismiss();
                    e.printStackTrace();
                } catch (ParseException e) {
                    progressDialog.dismiss();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        // Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        // Adding our request to the queue
        requestQueue.add(jsonObjectRequest);
    }

    /**
     * More info about using callback instances.
     * http://stackoverflow.com/questions/28120029/how-can-i-return-value-from-function-onresponse-of-volley
     */
    public interface VolleyCallback {
        void onSuccess(List<ListItem> result, RecyclerAdapter.AdapterClickListener listener);
    }

}
