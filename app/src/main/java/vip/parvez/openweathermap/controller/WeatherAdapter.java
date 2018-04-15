package vip.parvez.openweathermap.controller;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import vip.parvez.openweathermap.R;
import vip.parvez.openweathermap.model.Weather;
import vip.parvez.openweathermap.services.V;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder> {

    private ArrayList<Weather> weathers;
    private Context context;

    public WeatherAdapter(ArrayList<Weather> weathers, Context context) {
        this.context = context;
        this.weathers = weathers;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.recycler_view_weather_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final Weather weather = weathers.get(i);
        viewHolder.tvDate.setText(weather.getDate() + "");
        viewHolder.tvTime.setText(weather.getTime() + "");
        viewHolder.tvTemp.setText(weather.getTemperature() + "");
        viewHolder.tvMinTemp.setText("Min. Temp: " + weather.getMinTemperature() + "");
        viewHolder.tvMaxTemp.setText("Max. Temp: " + weather.getMaxTemperature() + "");
        viewHolder.tvHumidity.setText(weather.getHumidity() + "");
        try {
            Picasso.with(context)
                    .load(V.ICON_BASE_URL + weather.getWeatherIcon() + V.ICON_FORMAT)
//                                    .resize(200, 200)
                    .into(viewHolder.icon);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return weathers.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTemp, tvMinTemp, tvMaxTemp, tvHumidity, tvDate, tvTime;
        ImageView icon;

        public ViewHolder(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.weather_item_icon);
            tvTemp = itemView.findViewById(R.id.weather_item_temperature);
            tvMinTemp = itemView.findViewById(R.id.weather_item_min_temp);
            tvMaxTemp = itemView.findViewById(R.id.weather_item_max_temp);
            tvHumidity = itemView.findViewById(R.id.weather_item_humidity);
            tvDate = itemView.findViewById(R.id.weather_item_date);
            tvTime = itemView.findViewById(R.id.weather_item_time);
        }
    }
}
