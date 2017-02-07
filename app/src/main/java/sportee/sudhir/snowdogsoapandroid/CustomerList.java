package sportee.sudhir.snowdogsoapandroid;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class CustomerList extends AppCompatActivity{

    private ProgressDialog pDialog;
    ArrayList<HashMap<String, String>> contactList;
    LinearLayout linearLayout;
    TextView text1;

    ListView list_contact;

    private static final String NAMESPACE = "urn:Magento";
    private static final String URL = "http://magenjet.com/doorstep/index.php/api/v2_soap/index/";

    private final static String TAG = CustomerList.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_list);

        linearLayout=(LinearLayout)findViewById(R.id.linear1);
        text1=(TextView)findViewById(R.id.lineartext1);


        list_contact=(ListView)findViewById(R.id.list1);
        contactList = new ArrayList<>();


                new AsynCall().execute();
    }

        private class AsynCall extends AsyncTask<Void, Void, Void> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                // Showing progress dialog
                pDialog = new ProgressDialog(CustomerList.this);
                pDialog.setMessage("Please wait...");
                pDialog.setCancelable(false);
                pDialog.show();

            }
            @Override
            protected Void doInBackground(Void... arg0) {

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

                    //making call to get list of customers

                    String sessionId = result.toString();

                    request = new SoapObject(NAMESPACE, "customerCustomerList");

                    request.addProperty("sessionId", sessionId);

                    env.setOutputSoapObject(request);

                    androidHttpTransport.call("", env);

                    SoapObject  resultRequestSOAP =   (SoapObject) env.getResponse();



                    for (int i = 0; i < resultRequestSOAP.getPropertyCount(); i++) {
                        Object property = resultRequestSOAP.getProperty(i);
                        if (property instanceof SoapObject) {
                            SoapObject countryObj = (SoapObject) property;
                            String email = countryObj.getProperty("email").toString();
                            String firstname = countryObj.getProperty("firstname").toString();
                            String lastname = countryObj.getProperty("lastname").toString();
                            String customer_id = countryObj.getProperty("customer_id").toString();


                            HashMap<String, String> contact = new HashMap<>();

                            contact.put("email", email);
                            contact.put("firstname", firstname);
                            contact.put("lastname", lastname);
                            contact.put("customer_id", customer_id);

                          contactList.add(contact);
                        }
                    }

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
            protected void onPostExecute(Void response) {
                super.onPostExecute(response);
                if (pDialog.isShowing())
                    pDialog.dismiss();


                ListAdapter adapter =new SimpleAdapter(CustomerList.this,contactList, R.layout.list_item,
                        new String[]{"email","firstname", "lastname", "customer_id"}, new int[]{R.id.text_email,R.id.text_fname, R.id.text_lname, R.id.text_cid});
                            list_contact.setAdapter(adapter);
           }

    }
}
