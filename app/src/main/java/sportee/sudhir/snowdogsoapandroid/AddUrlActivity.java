package sportee.sudhir.snowdogsoapandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddUrlActivity extends AppCompatActivity  implements View.OnClickListener{
    private Button addTodoBtn;
    private EditText subjectEditText;


    private DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Add Record");
        setContentView(R.layout.activity_add_record);

        subjectEditText = (EditText) findViewById(R.id.subject_edittext);

        addTodoBtn = (Button) findViewById(R.id.add_record);

        dbManager = new DBManager(this);
        dbManager.open();
        addTodoBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.add_record:

                final String name = subjectEditText.getText().toString();


                dbManager.insert(name);

                Intent main = new Intent(AddUrlActivity.this, UrlListActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                startActivity(main);
                break;
        }

    }
}
