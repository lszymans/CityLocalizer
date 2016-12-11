package pl.lukaszszymansk.citylocalizer.activities;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import pl.lukaszszymansk.citylocalizer.R;

public class InfoMessageActivity extends AppCompatActivity {

    private static final String EXTRA_INFO_MESSAGE = "EXTRA_INFO_MESSAGE";

    public static Intent getStartIntent(Context context, String message) {
        Intent intent = new Intent(context, InfoMessageActivity.class);
        intent.putExtra(EXTRA_INFO_MESSAGE, message);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_message);

        String message = getIntent().getExtras().getString(EXTRA_INFO_MESSAGE);
        TextView tvMessage = (TextView)findViewById(R.id.tvMessage);
        tvMessage.setText(message);

        findViewById(R.id.btnOk).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
