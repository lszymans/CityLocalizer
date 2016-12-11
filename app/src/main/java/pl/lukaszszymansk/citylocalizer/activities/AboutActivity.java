package pl.lukaszszymansk.citylocalizer.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import pl.lukaszszymansk.citylocalizer.R;
import pl.lukaszszymansk.citylocalizer.core.Settings;
import pl.lukaszszymansk.citylocalizer.services.UtilsService;

public class AboutActivity extends AppCompatActivity {

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, AboutActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String versionText = UtilsService.getAppVersion(this);
        versionText = String.format("%s: %s", getResources().getString(R.string.app_version), versionText);
        TextView tvAppVersion = (TextView) findViewById(R.id.tvAppVersion);
        tvAppVersion.setText(versionText);

        TextView tvOpenSourceLicences = (TextView) findViewById(R.id.tvOpenSourceLicences);
        tvOpenSourceLicences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(WebPageActivity.getStartIntent(AboutActivity.this,
                        Settings.LICENCES_URL, getString(R.string.app_licences), null));
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
