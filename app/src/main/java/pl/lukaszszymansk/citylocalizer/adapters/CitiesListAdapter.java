package pl.lukaszszymansk.citylocalizer.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import pl.lukaszszymansk.citylocalizer.R;
import pl.lukaszszymansk.citylocalizer.db.tables.City;

public class CitiesListAdapter extends ArrayAdapter<City> {

    protected List<City> cities;
    protected LayoutInflater inflater;

    public CitiesListAdapter(Context context) {
        super(context, R.layout.list_item_city);

        cities = new ArrayList<City>();
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if (cities != null)
            return cities.size();

        return 0;
    }

    @Override
    public City getItem(int position) {
        return cities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void add(City object) {
        cities.add(0, object);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if(convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_city, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.tvCityName = (TextView) convertView.findViewById(R.id.tvCityName);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final City city = getItem(position);
        viewHolder.tvCityName.setText(city.getName());

        convertView.setBackgroundColor(position % 2 == 0 ? Color.WHITE : Color.GRAY);

        return convertView;
    }

    public List<City> getCities() {
        return cities;
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
    }

    //---------------------------------------------------------

    private static class ViewHolder {
        TextView tvCityName;
    }
}
