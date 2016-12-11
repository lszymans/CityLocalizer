package pl.lukaszszymansk.citylocalizer.adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.List;

import pl.lukaszszymansk.citylocalizer.R;
import pl.lukaszszymansk.citylocalizer.activities.MainActivity;
import pl.lukaszszymansk.citylocalizer.db.tables.City;

public class SearchHistoryListAdapter extends CitiesListAdapter {

    private Handler handler;

    public SearchHistoryListAdapter(Context context, Handler handler) {
        super(context);
        this.handler = handler;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if(convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_city, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.tvCityName = (TextView) convertView.findViewById(R.id.tvCityName);
            viewHolder.ivDeleteItem = (ImageView) convertView.findViewById(R.id.ivDeleteItem);
            viewHolder.ivDeleteItem.setVisibility(View.VISIBLE);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final City city = getItem(position);
        viewHolder.tvCityName.setText(city.getName());

        viewHolder.ivDeleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ((MainActivity) getContext()).getHelper().getCityDao().delete(city);
                    notifyDataSetChanged();
                } catch (SQLException e) {
                    Toast.makeText(getContext(), R.string.error_database_query, Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });

        convertView.setBackgroundColor(position % 2 == 0 ? Color.WHITE : Color.GRAY);

        return convertView;
    }

    @Override
    public void notifyDataSetChanged() {
        List<City> cities = null;

        try {
            Dao<City, Long> cityDao = ((MainActivity) getContext()).getHelper().getCityDao();
            QueryBuilder<City, Long> queryBuilder = cityDao.queryBuilder();
            cities = queryBuilder.orderBy("id", false).query();
            handler.obtainMessage(1).sendToTarget();
        } catch (SQLException e) {
            Toast.makeText(getContext(), R.string.error_database_query, Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        setCities(cities);
        super.notifyDataSetChanged();
    }

    //---------------------------------------------------------

    private static class ViewHolder {
        TextView tvCityName;
        ImageView ivDeleteItem;
    }
}
