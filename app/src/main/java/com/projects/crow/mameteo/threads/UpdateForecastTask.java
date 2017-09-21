package com.projects.crow.mameteo.threads;

import android.content.Context;
import android.os.AsyncTask;

import com.projects.crow.mameteo.networks.DarkSkyHelper;
import com.projects.crow.mameteo.networks.models.Forecast;
import com.projects.crow.mameteo.utils.services.LocationService;

/**
 * Created by Venom on 20/09/2017.
 */
public class UpdateForecastTask extends AsyncTask<Void, Void, Forecast> {

    private Context mContext;
    private double mLatitude;
    private double mLongitude;

    private OnUpdateForecastTaskListener mListener;

    public UpdateForecastTask(Context context, double latitude, double longitude) {
        mContext = context;
        mLatitude = latitude;
        mLongitude = longitude;
    }

    public void setListener(OnUpdateForecastTaskListener listener) {
        mListener = listener;
    }

    @Override
    protected Forecast doInBackground(Void... voids) {
        DarkSkyHelper db = DarkSkyHelper.getInstance();

        if (!LocationService.isLocationEnabled(mContext))
            return db.getLastForecast(mContext);
        return db.getForecast(mContext, mLatitude, mLongitude);
    }

    @Override
    protected void onPostExecute(Forecast forecast) {
        mListener.onPostExecute(mContext, forecast);
    }

    public interface OnUpdateForecastTaskListener {
        void onPostExecute(Context context, Forecast forecast);
    }
}
