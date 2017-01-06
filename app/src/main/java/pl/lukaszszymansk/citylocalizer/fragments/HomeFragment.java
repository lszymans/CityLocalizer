package pl.lukaszszymansk.citylocalizer.fragments;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

import pl.lukaszszymansk.citylocalizer.R;
import pl.lukaszszymansk.citylocalizer.activities.MainActivity;
import pl.lukaszszymansk.citylocalizer.adapters.CitiesListAdapter;
import pl.lukaszszymansk.citylocalizer.db.tables.City;
import pl.lukaszszymansk.citylocalizer.helpers.SharedPreferencesHelper;
import pl.lukaszszymansk.citylocalizer.services.UtilsService;
import pl.lukaszszymansk.citylocalizer.tasks.DownloadCityInfoTask;

public class HomeFragment extends Fragment {

    private static final String EXTRA_CITY_LIST_ADAPTER = "EXTRA_CITY_LIST_ADAPTER";

    private CitiesListAdapter citiesListAdapter;

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        AdView mAdView = (AdView) getActivity().findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)        // all emulators
                .addTestDevice("D035E85259C4783BE50FF984618518B2")  // my test phone
                .build();
        mAdView.loadAd(adRequest);

        citiesListAdapter = new CitiesListAdapter(getActivity());
        if(savedInstanceState != null)
            citiesListAdapter.setCities((List<City>) savedInstanceState.getSerializable(EXTRA_CITY_LIST_ADAPTER));

        ListView lvCities = (ListView) getActivity().findViewById(R.id.lvCities);
        lvCities.setAdapter(citiesListAdapter);
        lvCities.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UtilsService.closeKeyboard(getActivity());
                new DownloadCityInfoTask(getActivity(), citiesListAdapter.getItem(position).getName()).execute();
            }
        });

        updateView();

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final EditText editText = new EditText(getActivity());
                editText.setId(android.R.id.text1);
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity())
                        .setTitle(getString(R.string.enter_city))
                        .setView(editText)
                        .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                String newItem = editText.getText().toString().trim();
                                City city = new City(editText.getText().toString().trim());
                                updateSearchHistory(city);
                                if (newItem != null && !newItem.isEmpty()) {
                                    citiesListAdapter.add(city);
                                }
                                updateView();
                            }
                        })
                        .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                updateView();
                            }
                        });
                AlertDialog dialog = dialogBuilder.create();
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                dialog.show();
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putSerializable(EXTRA_CITY_LIST_ADAPTER, (Serializable) citiesListAdapter.getCities());
        super.onSaveInstanceState(savedInstanceState);
    }

    private void updateView() {
        TextView tvEmptyCitiesList = (TextView) getActivity().findViewById(R.id.tvEmptyCitiesList);
        ListView lvCities = (ListView) getActivity().findViewById(R.id.lvCities);

        if(citiesListAdapter.getCount() > 0) {
            lvCities.setVisibility(View.VISIBLE);
            tvEmptyCitiesList.setVisibility(View.GONE);
        } else {
            lvCities.setVisibility(View.GONE);
            tvEmptyCitiesList.setVisibility(View.VISIBLE);
        }
    }

    private void updateSearchHistory(City city) {
        boolean storeHistory = SharedPreferencesHelper.getInstance().ifStoreHistory();

        if (storeHistory) {
            try {
                ((MainActivity) getActivity()).getHelper().getCityDao().create(city);
            } catch (SQLException e) {
                Toast.makeText(getContext(), R.string.error_database_query, Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }
}
