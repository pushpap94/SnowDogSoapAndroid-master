package sportee.sudhir.snowdogsoapandroid;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.provider.DocumentsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.support.annotation.UiThread;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Main3Activity extends AppCompatActivity {

    private LinearLayout container;
    private Button click;
    private ArrayList<Customer> contactList;
    private ProgressDialog loading;

    private static final String NAMESPACE = "urn:Magento";
    private static final String URL = "http://magenjet.com/doorstep/index.php/api/v2_soap/index/";

    private final static String TAG = Main3Activity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        container = (LinearLayout) findViewById(R.id.bankslayout);
        click = (Button) findViewById(R.id.button1);
        contactList = new ArrayList<>();

        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AsynCall().execute();

            }
        });

    }

        private class AsynCall extends AsyncTask<Void, Void, SoapObject> {
            @Override
            protected SoapObject doInBackground(Void... params) {

                try {

                    SoapSerializationEnvelope env = new SoapSerializationEnvelope(
                            SoapEnvelope.VER11);

                    env.dotNet = false;
                    env.xsd = SoapSerializationEnvelope.XSD;
                    env.enc = SoapSerializationEnvelope.ENC;

                    SoapObject request = new SoapObject(NAMESPACE, "login");

                    request.addProperty("username", "zuanandroid");
                    request.addProperty("apiKey", "123456");

                    env.setOutputSoapObject(request);

                    HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

                    androidHttpTransport.call("", env);
                    Object result = env.getResponse();

                    Log.d("sessionId", result.toString());

                    //making call to get list of customers

                    String sessionId = result.toString();

                    request = new SoapObject(NAMESPACE, "customerCustomerList");

                    request.addProperty("sessionId", sessionId);

                    env.setOutputSoapObject(request);

                    androidHttpTransport.call("", env);

                    result = env.getResponse();

//                    SoapObject response = (SoapObject) env.bodyIn;

                    Log.d("Customer List", result.toString());


                            setDataToLayout((SoapObject) result);






                }
                catch (HttpResponseException e1) {
                    e1.printStackTrace();
                } catch (SoapFault soapFault) {
                    soapFault.printStackTrace();
                } catch (XmlPullParserException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }



                return null;
            }

            @Override
            protected void onPostExecute(SoapObject response) {
                super.onPostExecute(response);
//                contactList.clear();
//                for (int i = 0; i < result.getPropertyCount(); i++) {
//                    PropertyInfo pi = new PropertyInfo();
//                    result.getPropertyInfo(i, pi);
//                    Object property = result.getProperty(i);
//
//                    if (pi.name.equals("customerlist") && property instanceof SoapObject) {
//                        SoapObject transDetail = (SoapObject) property;
//
//                        String fname = transDetail.getPrimitivePropertyAsString("firstname");
//                        String lname = transDetail.getPrimitivePropertyAsString("lastname");
//
//                        Log.d(TAG, "Firstname: " + fname);
//                        Log.d(TAG, "LastName: " + lname);
//
//                        Customer entity = new Customer();
//                        entity.setFirstname(fname);
//                        entity.setLastname(lname);
//
//                        contactList.add(entity);
//
//                    }
//                }
//
//                setDataToLayout();
            }


            public void setDataToLayout(SoapObject result) {

                contactList.clear();
                for (int i = 0; i < result.getPropertyCount(); i++) {
                    PropertyInfo pi = new PropertyInfo();
                    result.getPropertyInfo(i, pi);
                    Object property = result.getProperty(i);

                    if (pi.name.equals(" ") && property instanceof SoapObject) {
                        SoapObject transDetail = (SoapObject) property;

                        String fname = transDetail.getPrimitivePropertyAsString("firstname");
                        String lname = transDetail.getPrimitivePropertyAsString("lastname");

                        Log.d(TAG, "Firstname: " + fname);
                        Log.d(TAG, "LastName: " + lname);

                        Customer entity = new Customer();
                        entity.setFirstname(fname);
                        entity.setLastname(lname);

                        contactList.add(entity);

                    }
                }

                LayoutInflater inflater = (LayoutInflater) getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                container.removeAllViews();

                for (int i = 0; i < contactList.size(); i++) {

                    View child = inflater.inflate(R.layout.list_item,null,false);

                    TextView text1 = (TextView) findViewById(R.id.text_fname);
                    TextView text2 = (TextView) findViewById(R.id.text_lname);

                    text1.setText(contactList.get(i).getFirstname());
                    text2.setText(contactList.get(i).getLastname());

                    container.addView(child);


                }

            }

    }
}
