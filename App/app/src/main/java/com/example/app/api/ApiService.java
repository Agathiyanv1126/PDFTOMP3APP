package com.example.app.api;

import com.example.app.response.Bus;
import com.example.app.response.BusResponse;
import com.example.app.response.DeleteResponse;
import com.example.app.response.LoginResponse;
import com.example.app.response.ProfileById;
import com.example.app.response.ScheduleResponse;
import com.example.app.response.SignupResponse;
import com.example.app.response.ViewScheduleResponse;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {

    @POST("user/create")
    Call<SignupResponse> createUser(@Body Map<String, String> body);

    @POST("user/studentlogin")
    Call<LoginResponse> loginUser(@Body Map<String, String> body);

    @GET("user/users/{id}")
    Call<ProfileById> getUserById(@Path("id") String userId);

    @POST("bus/create")
    Call<BusResponse> createBus(@Body Map<String, String> body);

    @GET("bus/getAllBus")
    Call<List<Bus>> getAllBuses();

    @POST("bus/scheduleBus")
    Call<ScheduleResponse> scheduleBus(@Body Map<String, String> requestBody);

    @GET("bus/getschedule")
    Call<ViewScheduleResponse> getSchedule();

    @DELETE("bus/deleteschedule/{id}")
    Call<DeleteResponse> deleteSchedule(@Path("id") String scheduleId);
}
