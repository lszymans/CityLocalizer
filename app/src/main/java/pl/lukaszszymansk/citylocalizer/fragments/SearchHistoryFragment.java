package pl.lukaszszymansk.citylocalizer.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import pl.lukaszszymansk.citylocalizer.R;
import pl.lukaszszymansk.citylocalizer.adapters.SearchHistoryListAdapter;
import pl.lukaszszymansk.citylocalizer.services.UtilsService;
import pl.lukaszszymansk.citylocalizer.tasks.DownloadCityInfoTask;


public class SearchHistoryFragment extends Fragment {

    private SearchHistoryListAdapter historyListAdapter;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if(msg.what == 1) {
                updateView();
            }
        }
    };

    public static SearchHistoryFragment newInstance() {
        SearchHistoryFragment fragment = new SearchHistoryFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_history, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        historyListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        historyListAdapter = new SearchHistoryListAdapter(getActivity(), handler);

        ListView lvCities = (ListView) getActivity().findViewById(R.id.lvCities);
        lvCities.setAdapter(historyListAdapter);
        lvCities.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UtilsService.closeKeyboard(getActivity());
                new DownloadCityInfoTask(getActivity(), historyListAdapter.getItem(position).getName()).execute();
            }
        });

        updateView();
    }

    private void updateView() {
        TextView tvEmptyList = (TextView) getActivity().findViewById(R.id.tvEmptyList);
        ListView lvCities = (ListView) getActivity().findViewById(R.id.lvCities);

        if(historyListAdapter != null && historyListAdapter.getCount() > 0) {
            lvCities.setVisibility(View.VISIBLE);
            tvEmptyList.setVisibility(View.GONE);
        } else {
            lvCities.setVisibility(View.GONE);
            tvEmptyList.setVisibility(View.VISIBLE);
        }
    }
}
