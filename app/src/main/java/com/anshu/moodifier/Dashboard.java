package com.anshu.moodifier;

import static android.app.PendingIntent.getActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Dashboard extends AppCompatActivity {
TextView greeting,compliment,jokeOut;
SharedPreferences sharedPreferences;
Button moodifyAgain;
ImageButton nextJokeBtn;
String genderStr;
String[] jokes;
int jokeIndex=0;
    @SuppressLint({"SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        greeting=findViewById(R.id.greeting);
        compliment=findViewById(R.id.compliment);
        jokeOut=findViewById(R.id.jokeShow);
        moodifyAgain=findViewById(R.id.moodifyBtn);
        moodifyAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayCompliment();
                jokeOut.setText(jokes[++jokeIndex]);
            }
        });
        nextJokeBtn=findViewById(R.id.nextJokeBtn);
        nextJokeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(jokeIndex!=29) {
                    jokeOut.setText(jokes[++jokeIndex]);
                }
                else
                {
                    Toast.makeText(Dashboard.this,"Loading...",Toast.LENGTH_LONG).show();
                    loadJokes();
                }
            }
        });
        sharedPreferences=getSharedPreferences("Moodifier", Context.MODE_PRIVATE);
        String name=sharedPreferences.getString("Name","XYZ");
        genderStr=sharedPreferences.getString("Gender","<Select Gender>");
        greeting.setText("Hello "+name+",\nWelcome to Moodifier");
        Animation blink= AnimationUtils.loadAnimation(Dashboard.this,R.anim.blink);
        displayCompliment();
        compliment.startAnimation(blink);

        loadJokes();
        Animation blink2= AnimationUtils.loadAnimation(Dashboard.this,R.anim.blink2);
        jokeOut.startAnimation(blink2);
    }
    @SuppressLint("SetTextI18n")
    void displayCompliment()
    {
        String complimentStr=getCompliment(genderStr);
        compliment.setText("You are very "+complimentStr+"!");
    }
    String getCompliment(String genderStr)
    {
        String[] complimentsFemale={"cute","pretty","beautiful","gorgeous","sweet","smart","lovely",
                "glamorous","likable","charming","attractive","adorable","stunning","alluring"};
        String[] complimentsMale={"handsome","cute","intelligent","daring","strong",
                "powerful","wise","talented","caring","courageous","adorable","attractive","charismatic"};
        if(genderStr.equals("Male"))
        {
            int index= (int) ((Math.random() * (complimentsMale.length-1)));
            return complimentsMale[index];
        }
        else if(genderStr.equals("Female"))
        {
            int index= (int) ((Math.random() * (complimentsFemale.length-1)));
            return complimentsFemale[index];
        }
    return "cute";
    }
    public void  loadJokes()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestQueue requestQueue = Volley.newRequestQueue(Dashboard.this);
                JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET,
                        "https://api.api-ninjas.com/v1/jokes?limit=30", null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                             jokeOut.setText(response.getJSONObject(0).getString("joke"));
                             jokeOut.clearAnimation();
                             jokes= new String[30];
                            for(int i=0;i<30;i++) {
                                JSONObject obj = response.getJSONObject(i);
                                jokes[i]=obj.getString("joke");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        //Log.d("myapp", "Something went wrong");
                        Toast.makeText(Dashboard.this,"Something went wrong",Toast.LENGTH_LONG).show();
                    }
                }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap headers=new HashMap();
                        headers.put("X-Api-Key","OPakK7lmBBhCx+Lakh1IGQ==14OypK9nRf0bFDPG");
                        return headers;
                    }
                };
                requestQueue.add(jsonObjectRequest);
            }
        }).start();

    }
}