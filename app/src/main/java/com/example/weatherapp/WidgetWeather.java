
package com.example.weatherapp;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;

import com.airbnb.lottie.utils.Utils;
import com.example.weatherapp.api.weather.WeatherApi;
import com.example.weatherapp.api.weather.WeatherApiManager;
import com.example.weatherapp.models.CurrentWeatherInfo;
import com.example.weatherapp.models.WeatherDescription;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Implementation of App Widget functionality.
 */
public class WidgetWeather extends AppWidgetProvider {

    static void updateAppWidget(final Context context, AppWidgetManager appWidgetManager,
                                final int appWidgetId) {

        // Construct the RemoteViews object
        final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_weather);
        WeatherApiManager api = new WeatherApiManager();
        views.setTextViewText(R.id.appwidget_text,"17.34");
        api.getWeatherByCityName("beirut").enqueue(new Callback<CurrentWeatherInfo>() {
            @Override
            public void onResponse(Call<CurrentWeatherInfo> call, Response<CurrentWeatherInfo> response) {
                if (response.isSuccessful()) {
                    CurrentWeatherInfo currentWeatherInfo = response.body();
                    if (currentWeatherInfo != null) {

                        String currentTemp = String.valueOf(currentWeatherInfo.getTempInfo().getTemp());
                        views.setTextViewText(R.id.appwidget_text,currentTemp);



                        if (currentWeatherInfo.getWeatherDescriptions() != null && !currentWeatherInfo.getWeatherDescriptions().isEmpty()) {
                            WeatherDescription currentWeatherDescription = currentWeatherInfo.getWeatherDescriptions().get(0);

                            String weatherDescription = currentWeatherDescription.getDescription();

                            String iconUrl = "http://openweathermap.org/img/w/" + currentWeatherDescription.getIcon() + ".png";

                            Target mTarget = new Target() {
                                @Override
                                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                    views.setImageViewBitmap(R.id.appwidget_image,bitmap);
                                }

                                @Override
                                public void onBitmapFailed(Drawable errorDrawable) {

                                }

                                @Override
                                public void onPrepareLoad(Drawable placeHolderDrawable) {

                                }
                            };
                            Picasso.with(context).load(iconUrl).into(mTarget);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<CurrentWeatherInfo> call, Throwable t) {
            }
        });



        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);

    }
    private void showCurrentWeather(CurrentWeatherInfo info) {

    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

