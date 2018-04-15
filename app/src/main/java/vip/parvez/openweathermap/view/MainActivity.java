package vip.parvez.openweathermap.view;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vip.parvez.openweathermap.R;
import vip.parvez.openweathermap.controller.JsonArray;
import vip.parvez.openweathermap.controller.WeatherAdapter;
import vip.parvez.openweathermap.model.Weather;
import vip.parvez.openweathermap.services.MyServices;
import vip.parvez.openweathermap.services.RetrofitAPI;
import vip.parvez.openweathermap.services.RetrofitClient;
import vip.parvez.openweathermap.services.V;

public class MainActivity extends AppCompatActivity {
    RetrofitAPI retrofitAPI;
    Spinner spinner;
    ProgressDialog progressDialog;
    ImageView ivIcon;
    TextView tvTemperature, tvMinTemperature, tvMaxTemperature;
    TextView tvPressure, tvHumidity, tvRain, tvWindSpeed, tvWindDegree, tvWeatherStatus;
    RecyclerView recyclerView;
    WeatherAdapter weatherAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        setSpinner();

    }

    private void init() {
        ivIcon = findViewById(R.id.weather_icon);
        tvTemperature = findViewById(R.id.temperature);
        tvMaxTemperature = findViewById(R.id.max_temp);
        tvMinTemperature = findViewById(R.id.min_temp);
        tvHumidity = findViewById(R.id.humidity);
        tvPressure = findViewById(R.id.pressure);
        tvRain = findViewById(R.id.rain);
        tvWindSpeed = findViewById(R.id.wind_speed);
        tvWindDegree = findViewById(R.id.wind_degree);
        tvWeatherStatus = findViewById(R.id.weather_status);
        recyclerView.findViewById(R.id.recycle_view_temp);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    private void setSpinner() {
        spinner = (Spinner) findViewById(R.id.city_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.city_name, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String cityName = spinner.getSelectedItem().toString();
                getWeather(cityName);
                MyServices.log(cityName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                MyServices.log("Dhaka,BD");
                getWeather("Dhaka,BD");
            }
        });
    }


    private void getWeather(String cityName) {
        if (MyServices.checkConnectivity(this)) {
            progressDialog = MyServices.progressDialog(this, "Loading...");
            progressDialog.show();
            retrofitAPI = RetrofitClient.getClient(V.BASE_URL).create(RetrofitAPI.class);
            Call<ResponseBody> callback = retrofitAPI.getWeather(cityName, "json", V.UNITS, V.APP_ID);
            callback.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    try {
                        //get all json object data
                        JSONObject data = new JSONObject(response.body().string());
                        //MyServices.log("ALL DATA: " + data.toString());

                        //status code
                        String code = data.getString("cod");
                        //MyServices.log("CODE: " + code);

                        if (code == "200") {

                            //message
                            //String message = data.getString("message");
                            //MyServices.log("MESSAGE: " + message);

                            //total report count
                            //String cnt = data.getString("cnt");
                            //MyServices.log("COUNT: " + cnt);

                            //city info
                            //JSONObject city = data.getJSONObject("city");
                            //MyServices.log("CITY: " + city);

                            //LAT LON
                            //JSONObject coord = city.getJSONObject("coord");
                            //MyServices.log("LAT LON: " + coord);
                            //MyServices.log("LAT: " + coord.getString("lat"));
                            //MyServices.log("LON: " + coord.getString("lon"));

                            //get reports
                            JSONArray forecastList = data.getJSONArray("list");//.getJSONObject(0);
                            MyServices.log("FORECASTLIST: " + forecastList);


                            ArrayList<Weather> weatherList = JsonArray.toWeather(forecastList);
                            Weather currentWeather = weatherList.get(0);
                            tvTemperature.setText(currentWeather.getTemperature() + "");
                            tvMinTemperature.setText(currentWeather.getMinTemperature() + "");
                            tvMaxTemperature.setText(currentWeather.getMaxTemperature() + "");
                            tvWeatherStatus.setText(currentWeather.getWeatherDescription() + "");
                            tvHumidity.setText(currentWeather.getHumidity() + "");
                            tvPressure.setText(currentWeather.getPressure() + "");
                            tvRain.setText(currentWeather.getRain() + "");
                            tvWindSpeed.setText(currentWeather.getWindSpeed() + "");
                            tvWindDegree.setText(currentWeather.getWindDeg() + "");
                            try {
                                Picasso.with(getApplicationContext())
                                        .load(V.ICON_BASE_URL + currentWeather.getWeatherIcon() + V.ICON_FORMAT)
//                                    .resize(200, 200)
                                        .into(ivIcon);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (weatherList.size() > 0) {
                                weatherAdapter = new WeatherAdapter(weatherList, getApplicationContext());
                                recyclerView.setAdapter(weatherAdapter);
                            }
//                            MyServices.log("WEATHER 0: " + weatherList.get(0).toString());
//                            String value = "";
//                            for (Weather weather : weatherList) {
//                                value += "Date: " + weather.getDate() + "\n";
//                                value += "Date Txt: " + weather.getDtTxt() + "\n";
//                                value += "Time: " + weather.getTime() + "\n";
//                                value += "Temperature: " + weather.getTemperature() + "\n";
//                                value += "Icon: " + V.ICON_BASE_URL + weather.getWeatherIcon() + V.ICON_FORMAT + "\n";
//                                value += "=====================\n";
//                            }
                        } else {
                            MyServices.alertDialog(getApplicationContext(), "Connection. Please try again after sometime.");

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    progressDialog.dismiss();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    progressDialog.dismiss();
                    MyServices.alertDialog(getApplicationContext(), "Network failed. Please try again after sometime.");
                }
            });
        } else {
            MyServices.alertDialog(this, "Internet Connectivity failed. Please connect with your internet.");
        }
    }
}
