package vip.parvez.openweathermap.services;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitAPI {

    /**
     *
     * @param cityName dhaka,bd
     * @param mode json
     * @param units temperature in Celsius use units=metric
     * @param appid
     * @return
     */
    @GET("/data/2.5/forecast?")
    Call<ResponseBody> getWeather(@Query("q") String cityName, @Query("mode") String mode,
                                  @Query("units") String units, @Query("appid") String appid);
}
