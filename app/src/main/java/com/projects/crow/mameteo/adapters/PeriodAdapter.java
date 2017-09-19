package com.projects.crow.mameteo.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.projects.crow.mameteo.R;
import com.projects.crow.mameteo.database.models.Datum;
import com.projects.crow.mameteo.utils.DateUtils;
import com.projects.crow.mameteo.utils.MaMeteoUtils;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Venom on 13/09/2017.
 */

public class PeriodAdapter extends RecyclerView.Adapter<PeriodAdapter.PeriodViewHolder> {

    private static final String TAG = "PeriodAdapter";
    
    private Context mContext;
    private List<Datum> mDatas;
    private String mPeriod;

    private HashMap<String, Integer> mLayout;

    protected class PeriodViewHolder extends RecyclerView.ViewHolder {

        private ImageView mIvIcon;

        private TextView mTvDay;

        private TextView mTvTemperature;
        private TextView mTvHour;
        private TextView mTvWindSpeed;
        private TextView mTvHumidity;

        private PeriodViewHolder(View itemView) {
            super(itemView);
            mIvIcon = itemView.findViewById(R.id.image_view_icon);
            mTvTemperature = itemView.findViewById(R.id.text_view_temperature);
            switch (mPeriod) {
                case MaMeteoUtils.DAILY:
                    mTvDay = itemView.findViewById(R.id.text_view_day);
                    break;
                case MaMeteoUtils.HOURLY:
                    mTvHour = itemView.findViewById(R.id.text_view_hour);
                    mTvWindSpeed = itemView.findViewById(R.id.text_view_windspeed);
                    mTvHumidity = itemView.findViewById(R.id.text_view_humidity);
                    break;
            }
        }
    }

    public PeriodAdapter(Context context, List<Datum> datas, String period) {
        mContext = context;
        mDatas = datas;
        mPeriod = period;

        mLayout = new HashMap<>();
        mLayout.put(MaMeteoUtils.DAILY, R.layout.list_item_daily);
        mLayout.put(MaMeteoUtils.HOURLY, R.layout.list_item_hourly);
    }

    @Override
    public PeriodAdapter.PeriodViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(mLayout.get(mPeriod), parent, false);
        return new PeriodViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PeriodAdapter.PeriodViewHolder holder, int position) {
        Datum data = mDatas.get(position);

        holder.mIvIcon.setImageResource(MaMeteoUtils.getIconByName(data.getIcon()));
        switch (mPeriod) {
            case MaMeteoUtils.DAILY:
                holder.mTvTemperature.setText(MaMeteoUtils.formatToCelsius(data.getTemperatureMax()));
                holder.mTvDay.setText(DateUtils.convertTimestampInDateFormat(data.getTime(), "EEEE"));
                break;
            case MaMeteoUtils.HOURLY:
                holder.mTvTemperature.setText(MaMeteoUtils.formatToCelsius(data.getTemperature()));
                holder.mTvHour.setText(DateUtils.convertTimestampInDateFormat(data.getTime(), "HH:mm"));
                holder.mTvWindSpeed.setText(MaMeteoUtils.windspeedFormat(data.getWindSpeed()));
                holder.mTvHumidity.setText(MaMeteoUtils.percentageFormat(data.getHumidity()));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }


    public void updateDatas(List<Datum> datas) {
        mDatas = datas;
        notifyDataSetChanged();
    }
}
