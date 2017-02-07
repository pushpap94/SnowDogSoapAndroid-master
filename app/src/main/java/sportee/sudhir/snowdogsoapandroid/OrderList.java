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

public class OrderList extends AppCompatActivity {

    private ProgressDialog pDialog;
    private static final String NAMESPACE = "urn:Magento";
    private static final String URL = "http://magenjet.com/doorstep/index.php/api/v2_soap/index/";
    ArrayList<HashMap<String, String>> orderList;
    ListView order_list;

    LinearLayout linearLayout;
    TextView text2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        order_list=(ListView)findViewById(R.id.list2);

        linearLayout=(LinearLayout)findViewById(R.id.linear2);
        text2=(TextView)findViewById(R.id.lineartext2);

        orderList=new ArrayList<>();

        new AsynCall().execute();
    }

    private class AsynCall  extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(OrderList.this);
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

//                Log.d("sessionId", result.toString());

                //making call to get list of customers

                String sessionId = result.toString();

                request = new SoapObject(NAMESPACE, "salesOrderList");

                request.addProperty("sessionId", sessionId);

                env.setOutputSoapObject(request);

                androidHttpTransport.call("", env);

                SoapObject  resultRequest =   (SoapObject) env.getResponse();

//                System.out.println("Response : "+resultRequest.toString());



                for (int i = 0; i < resultRequest.getPropertyCount(); i++) {
                    Object property = resultRequest.getProperty(i);
                    if (property instanceof SoapObject) {
                        SoapObject countryObj = (SoapObject) property;

                        String customer_firstname = countryObj.getProperty("customer_firstname").toString();

                        String order_id = countryObj.getProperty("order_id").toString();

                        String status = countryObj.getProperty("status").toString();

                        String total_qty_ordered = countryObj.getProperty("total_qty_ordered").toString();

                        String base_shipping_amount = countryObj.getProperty("base_shipping_amount").toString();





                        HashMap<String, String> contact = new HashMap<>();

                        contact.put("customer_firstname", customer_firstname);
                        contact.put("order_id", order_id);
                        contact.put("status", status);
                        contact.put("total_qty_ordered", total_qty_ordered);

                        contact.put("base_shipping_amount", base_shipping_amount);



                        orderList.add(contact);
                    }
                }

            } catch (HttpResponseException e) {
                e.printStackTrace();
            } catch (SoapFault soapFault) {
                soapFault.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void response) {
            super.onPostExecute(response);
            if (pDialog.isShowing())
                pDialog.dismiss();

//            System.out.println("Contact" + orderList);

            ListAdapter adapter =new SimpleAdapter(OrderList.this,orderList,R.layout.list_order,
                    new String[]{"customer_firstname","order_id","status","total_qty_ordered",
                            "base_shipping_amount"},
                    new int[]{R.id.text_name,R.id.text_orderid,R.id.text_status, R.id.text_qty,
                            R.id.text_shamount});
            order_list.setAdapter(adapter);

        }

    }
}
