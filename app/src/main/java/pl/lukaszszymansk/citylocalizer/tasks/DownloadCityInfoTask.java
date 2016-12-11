package pl.lukaszszymansk.citylocalizer.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Locale;

import pl.lukaszszymansk.citylocalizer.R;
import pl.lukaszszymansk.citylocalizer.activities.CityInfoActivity;
import pl.lukaszszymansk.citylocalizer.core.Settings;
import pl.lukaszszymansk.citylocalizer.models.Geocode;
import pl.lukaszszymansk.citylocalizer.models.GeocodingResult;


public class DownloadCityInfoTask extends AsyncTask<Void, Void, Geocode> {

    private Context context;
    private String cityName;
    private ProgressDialog progress;

    public DownloadCityInfoTask(Context context, String cityName) {
        this.context = context;
        this.cityName = cityName;

        initProgressBar();
    }

    @Override
    protected Geocode doInBackground(Void... params) {

        String address = String.format(Settings.GEOCODE_URL, cityName.replaceAll(" ","%20"));
        HttpClient httpClient = new DefaultHttpClient();
        StringBuffer buffer = new StringBuffer();

        try {
            HttpGet httpGet = new HttpGet(address);
            httpGet.setHeader("Accept-Language", Locale.getDefault().toString());

            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();

            BufferedReader bf = new BufferedReader(new InputStreamReader((entity.getContent()),"UTF-8"));

            String line = "";
            while((line = bf.readLine())!=null){
                buffer.append(line);
            }

            bf.close();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        String jsonOutput = buffer.toString();

        Gson gson = new GsonBuilder().create();

        return gson.fromJson(jsonOutput, Geocode.class);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        progress.show();
    }

    @Override
    protected void onPostExecute(Geocode result) {

        progress.dismiss();

        if(result != null) {
            String status = result.getStatus();

            if (status.equals("OK")) {
                List<GeocodingResult> geocodingResults = result.getResultsForCities();

                if(geocodingResults == null || geocodingResults.size() == 0)
                    Toast.makeText(context, R.string.geocode_no_city_results, Toast.LENGTH_SHORT).show();
                else {
                    context.startActivity(CityInfoActivity.getStartIntent(context, geocodingResults));
                }

            }
            else if(status.equals("ZERO_RESULTS"))
                Toast.makeText(context, R.string.geocode_no_results, Toast.LENGTH_SHORT).show();
            else if(status.equals("OVER_QUERY_LIMIT"))
                Toast.makeText(context, R.string.geocode_over_query_limit, Toast.LENGTH_SHORT).show();
            else if(status.equals("REQUEST_DENIED"))
                Toast.makeText(context, R.string.geocode_request_denied, Toast.LENGTH_SHORT).show();
            else if(status.equals("INVALID_REQUEST"))
                Toast.makeText(context, R.string.geocode_invalid_request, Toast.LENGTH_SHORT).show();
            else if(status.equals("UNKNOWN_ERROR"))
                Toast.makeText(context, R.string.geocode_unknown_error, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, R.string.error_internet_connection, Toast.LENGTH_SHORT).show();
        }
    }


    private void initProgressBar() {

        progress = new ProgressDialog(context);

        WindowManager.LayoutParams wlmp = progress.getWindow().getAttributes();
        wlmp.gravity = Gravity.CENTER_HORIZONTAL;
        progress.getWindow().setAttributes(wlmp);

        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setCancelable(false);
        progress.setMessage(context.getString(R.string.please_wait));

    }
}
