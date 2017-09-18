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

import java.util.List;

/**
 * Created by Venom on 13/09/2017.
 */

public class PeriodAdapter extends RecyclerView.Adapter<PeriodAdapter.DailyViewHolder> {

    private static final String TAG = "PeriodAdapter";
    
    private Context mContext;
    private List<Datum> mDatas;
    private static String mPeriod;

    protected class DailyViewHolder extends RecyclerView.ViewHolder {

        private ImageView mIvIcon;
        private TextView mTvTemperature;
        private TextView mTvHour;

        private DailyViewHolder(View itemView) {
            super(itemView);
            mIvIcon = itemView.findViewById(R.id.image_view_icon);
            mTvTemperature = itemView.findViewById(R.id.text_view_temperature);
            mTvHour = itemView.findViewById(R.id.text_view_hour);
        }
    }

    public PeriodAdapter(Context context, List<Datum> datas, String period) {
        mContext = context;
        mDatas = datas;
        mPeriod = period;
    }

    @Override
    public PeriodAdapter.DailyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_daily, parent, false);
        return new DailyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PeriodAdapter.DailyViewHolder holder, int position) {
        Datum data = mDatas.get(position);

        holder.mIvIcon.setImageResource(MaMeteoUtils.getIconByName(mContext, data.getIcon()));
        holder.mTvTemperature.setText(MaMeteoUtils.fahrenheitToCelsius(data.getTemperature()));
        holder.mTvHour.setText(DateUtils.convertTimestampInHour(data.getTime()));

        switch (mPeriod) {
            case MaMeteoUtils.HOURLY:
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
