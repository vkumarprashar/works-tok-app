package com.eurasian.workstokapp.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.eurasian.workstokapp.R;
import com.eurasian.workstokapp.models.Earnings;

import java.util.List;

public class WithdrawAdapter extends BaseAdapter {

    List<Earnings> list;
    Activity activity;
    public WithdrawAdapter(Activity activity, List<Earnings> list) {
        this.activity = activity;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Earnings getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(activity.getApplicationContext()).inflate(R.layout.list_view_withdraw, null, false);
        Earnings earnings1 = getItem(position);
        TextView clientName = view.findViewById(R.id.customerNameWithdraw);
        clientName.setText(earnings1.getClientName());
        TextView rating = view.findViewById(R.id.rating);
        rating.setText("" + earnings1.getRequestrating());

        ImageView imageView = view.findViewById(R.id.ratingImg);
        switch (earnings1.getRequestrating()) {
            case 1 : imageView.setBackgroundResource(R.drawable.ic_baseline_star_rate_24_red);
                rating.setTextColor(Color.RED);
            break;
            case 2 : imageView.setBackgroundResource(R.drawable.ic_baseline_star_rate_24_orange);
                                rating.setTextColor(Color.rgb(231, 149, 75));
                break;
            case 3 : imageView.setBackgroundResource(R.drawable.ic_baseline_star_rate_24_yellow);
                rating.setTextColor(Color.rgb(246, 218, 25));
                break;
            case 4 : imageView.setBackgroundResource(R.drawable.ic_baseline_star_rate_24_light_green);
                rating.setTextColor(Color.rgb(170, 219, 131));
                break;
            case 5 : imageView.setBackgroundResource(R.drawable.ic_baseline_star_rate_24_green);
                rating.setTextColor(Color.rgb(81, 157, 8));
                break;
        }
        TextView dateOfWork = view.findViewById(R.id.dateOfWork);
        dateOfWork.setText(earnings1.getCompletedWorkTime());
        TextView earning = view.findViewById(R.id.earning);
        earning.setText(String.format("%.0f", earnings1.getEarning()) + " RUB");

        return view;
    }
}
