package sportee.sudhir.snowdogsoapandroid;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Calendar;

public class ModifyUrlActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText titleText;
    private Button updateBtn, deleteBtn;
    PendingIntent contentIntent;


    private long _id;

    private DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Modify Record");
        setContentView(R.layout.add_modify_record);

        dbManager = new DBManager(this);
        dbManager.open();

        titleText = (EditText) findViewById(R.id.subject_edittext);


        updateBtn = (Button) findViewById(R.id.btn_update);
        deleteBtn = (Button) findViewById(R.id.btn_delete);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String name = intent.getStringExtra("title");

        _id = Long.parseLong(id);

        titleText.setText(name);


        updateBtn.setOnClickListener(this);
        deleteBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_update:
                String title = titleText.getText().toString();
                dbManager.update(_id, title);

                Calendar calendar = Calendar.getInstance();

                calendar.set(Calendar.MONTH, 02);
                calendar.set(Calendar.YEAR, 2017);
                calendar.set(Calendar.DAY_OF_MONTH, 07);

                calendar.set(Calendar.HOUR_OF_DAY, 20);
                calendar.set(Calendar.MINUTE, 48);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.AM_PM,Calendar.PM);

                Intent myIntent = new Intent(ModifyUrlActivity.this, MyReceiver.class);
                contentIntent = PendingIntent.getBroadcast(ModifyUrlActivity.this, 0, myIntent,0);

                AlarmManager am = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
                long recurring = (5 * 60000);
                am.setRepeating(AlarmManager.RTC, Calendar.getInstance().getTimeInMillis(), recurring, contentIntent);

                this.returnHome();
                break;

            case R.id.btn_delete:
                dbManager.delete(_id);
                this.returnHome();
                break;
        }
    }

    public void returnHome() {
        Intent home_intent = new Intent(getApplicationContext(), UrlListActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(home_intent);


    }
}
