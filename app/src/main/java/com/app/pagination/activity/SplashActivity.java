package com.app.pagination.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.app.pagination.R;
import com.app.pagination.model.Genre;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ravi on 07-12-2017.
 */
public class SplashActivity extends AppCompatActivity {
    List genreList = new ArrayList<>();
    int statusCode;
    Context context;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        context = getApplicationContext();
        new GetMovieGenresData().execute();
        /*int SPLASH_TIME = 1500;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                    Intent intentLaunchLoginActivity = new Intent(
                            SplashActivity.this, MainActivity.class);
                    startActivity(intentLaunchLoginActivity);
                    finish();
            }
        }, SPLASH_TIME);*/
    }

    public class GetMovieGenresData extends AsyncTask<Void, Void, Integer>{


        @Override
        protected Integer doInBackground(Void... voids) {

            String urlToGetGenres ="https://api.themoviedb.org/3/genre/movie/list?api_key=2c0bb3ae2ba96a469724d0c25bd4844e&language=en-US";
            HttpURLConnection httpURLConnection;
            Integer result = 0;

            try{
                URL url = new URL(urlToGetGenres);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                statusCode = httpURLConnection.getResponseCode();
                if (statusCode == 200){
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while((line = bufferedReader.readLine()) !=null){
                        stringBuilder.append(line);
                    }
                     ParseJSONData(stringBuilder.toString());
                    result = 1;
                } else {
                    result = 0;
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if(statusCode == 200){
            Intent intentLaunchLoginActivity = new Intent(
                    context, MainActivity.class);
            startActivity(intentLaunchLoginActivity);
            finish();}
            else
                Toast.makeText(context, "Check Your Network Connection",Toast.LENGTH_SHORT).show();

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        private void ParseJSONData(String s){

            try{
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.optJSONArray("genres");

                for (int i = 0; i<jsonArray.length(); i++){
                    JSONObject jsonObjectGenre = jsonArray.getJSONObject(i);
                        Genre genre = new Genre();
                        genre.setmStringGenreId(jsonObjectGenre.optString("id"));
                        genre.setmStringGenreName(jsonObjectGenre.optString("name"));
                        genreList.add(genre);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
