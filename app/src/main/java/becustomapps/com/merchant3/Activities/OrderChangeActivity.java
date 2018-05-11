package becustomapps.com.merchant3.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import becustomapps.com.merchant3.Objects.Authcode;
import becustomapps.com.merchant3.Objects.Customer;
import becustomapps.com.merchant3.Objects.OrderCustomer;
import becustomapps.com.merchant3.Objects.OrderProduct;
import becustomapps.com.merchant3.Objects.Product;
import becustomapps.com.merchant3.R;
import becustomapps.com.merchant3.Utilities.DataSource;
import becustomapps.com.merchant3.Utilities.LogOutTimerUtil;
import becustomapps.com.merchant3.Utilities.Utility;

public class OrderChangeActivity extends AppCompatActivity implements LogOutTimerUtil.LogOutListener{

    SharedPreferences prefs;
    DataSource datasource;
    String cust_no;
    OrderCustomer orderCustomer;
    Customer customer;

    ArrayList<OrderProduct> orderProducts;
    HashMap<String, Authcode> productMap = new HashMap<String, Authcode>();

    TextView customerid, customername, customeraddress;
    TextView[] daysOfWeek = new TextView[7];
    TextView[] dates = new TextView[7];

    TextView productid, productname;
    TextView onhand, gamble, gambletype, facing;
    TextView[] prevadj = new TextView[7];
    TextView[] shipadj = new TextView[7];
    TextView[] tabadj = new TextView[7];
    TextView[] avgsold = new TextView[7];
    TextView[] presold = new TextView[7];
    TextView[] currentorder = new TextView[7];
    TextView[] suggestedorder = new TextView[7];
    EditText[] finalorderedit = new EditText[7];
    TextView[] locked = new TextView[7];
    TextView prevAdjTotal, shipAdjTotal, avgSoldTotal, preSoldTotal, currentOrderTotal;

    int editColumn = 0;
    int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_change);

        Button blankButton = (Button)findViewById(R.id.middleButton);
        blankButton.setEnabled(false);

        prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE);

        datasource = new DataSource(this);
        datasource.open();

        Intent intent = getIntent();
        cust_no = intent.getStringExtra("cust_no");

        orderCustomer = datasource.getOrderCustomerById(cust_no);
        customer = datasource.getCustomerById(cust_no);
        if(orderCustomer == null){
            //ToDo error
        }
        if(customer == null){
            //ToDo error
        }

        ActionBar actionbar = getSupportActionBar();
        TextView textview = new TextView(this);
        textview.setText(customer.getCust_name());
        textview.setTextColor(Color.WHITE);
        textview.setTextSize(18);
        textview.setGravity(Gravity.CENTER);
        actionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionbar.setCustomView(textview);

        setupXML();

        String theDays = orderCustomer.getDays() + " ";
        String theDates = orderCustomer.getDates() + " ";
        String orderdate = orderCustomer.getOrderdate().substring(0,4);
        Log.e("ORDERDATE", orderdate);
        for(int x = 0; x < 7; x++){
            daysOfWeek[x].setText(theDays.substring((x*3),(x*3)+2));
            String dateString = theDates.substring((x*6),(x*6)+4);
            if(orderdate.equals(dateString)){
                editColumn = x;
            }
            dateString = dateString.substring(0,2) + "-" + dateString.substring(2);
            //ToDo set real date
            dates[x].setText("");
        }

        ArrayList<OrderProduct> fakeProducts = new ArrayList<OrderProduct>();
        fakeProducts = datasource.getOrderProductsFromAcct(cust_no);
        productMap = datasource.getProductsFromAcct(cust_no);
        orderProducts = new ArrayList<OrderProduct>();

        for(OrderProduct p: fakeProducts){
            //Log.e("NUMBER", p.getProdno());
            if(productMap.containsKey(p.getProdno())){
                //Log.e("CONTAINS", "TRUE");
                orderProducts.add(p);
            }
        }

        Utility.alert(this, orderProducts.size() + "");
        loadData();
    }

    public void priorProduct(View view){
        updateProduct();
        if(index > 0){
            index--;
            loadData();
        }
    }

    public void nextProduct(View view){

        updateProduct();
        if(index < orderProducts.size() - 1){
            index++;
            loadData();
        } else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    this);
            alertDialogBuilder
                    .setMessage("Last record for customer. Press OK, check product values and press Complete")
                    .setCancelable(false)
                    .setNegativeButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    dialog.cancel();
                                }
                            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }

    }

    public void loadData(){
        Log.e("DATA", "LOADING");
        //dummy.requestFocus();
        clearTextBoxes();

        OrderProduct p = orderProducts.get(index);
        String fourDigitId = p.getProdno().substring(2);
        String brandId = p.getCustno();
        //ToDo put brand ID in actionbar label
        //Log.e("BRAND", brandId);
        productid.setText(fourDigitId);
        Authcode thisProduct = productMap.get(p.getProdno());
        productname.setText(thisProduct.getProd_name());
        if(!thisProduct.getOnhand().equals("")){
            onhand.setText("OH " + Integer.parseInt(thisProduct.getOnhand()));
        } else {
            onhand.setText("OH");
        }
        gamble.setText("Gam " + Integer.parseInt(p.getGamble()));
        gambletype.setText(p.getGambletype());
        facing.setText("Fac " + Integer.parseInt(p.getFacing()));

        String PREV_ADJ = p.getPrevOrderAdj() + " ";
        String SHIP_ADJ = p.getShipAdj() + " ";
        //String TAB_ADJ = thisAdjustment.getTabAdj();
        String AVG_SOLD = p.getAvgSold() + " ";
        String PRE_SOLD = p.getPrevWeekSold() + " ";
        String CURRENT_ORDER = p.getCurrentOrder() + " ";
        String SUGGESTED_ORDER = "SUGG";
        String FINAL_ORDER = "FIN";
        String LOCKED = p.getLocked() + " ";
        String productAvailToOrder = p.getAvailableToOrder() + " ";
        String dayavail = p.getDailyProductAvailable() + " ";

        int prevtotal = 0;
        int shiptotal = 0;
        int avgtotal = 0;
        int pretotal = 0;
        int currenttotal = 0;

        for(int x = 0; x < 7; x++){
            prevadj[x].setText(Integer.parseInt(PREV_ADJ.substring((x*5),(x*5)+5)) + "");
            prevtotal += Integer.parseInt(PREV_ADJ.substring((x*5),(x*5)+5));
            shipadj[x].setText(Integer.parseInt(SHIP_ADJ.substring((x*5),(x*5)+5)) + "");
            shiptotal += Integer.parseInt(SHIP_ADJ.substring((x*5),(x*5)+5));
            avgsold[x].setText(Integer.parseInt(AVG_SOLD.substring((x*4),(x*4)+4)) + "");
            avgtotal += Integer.parseInt(AVG_SOLD.substring((x*4),(x*4)+4));
            presold[x].setText(Integer.parseInt(PRE_SOLD.substring((x*4),(x*4)+4)) + "");
            pretotal += Integer.parseInt(PRE_SOLD.substring((x*4),(x*4)+4));
            currentorder[x].setText(Integer.parseInt(CURRENT_ORDER.substring((x*4),(x*4)+4)) + "");
            currenttotal += Integer.parseInt(CURRENT_ORDER.substring((x*4),(x*4)+4));
        }

        prevAdjTotal.setText(prevtotal + "");
        shipAdjTotal.setText(shiptotal + "");
        avgSoldTotal.setText(avgtotal + "");
        preSoldTotal.setText(pretotal + "");
        currentOrderTotal.setText(currenttotal + "");

        tabadj[editColumn].setText("TD");

        locked[editColumn].setText((p.getLocked() + " ").substring(editColumn, editColumn+1));
    }

    public void setupXML(){
        //dummy = (EditText)findViewById(R.id.dummyEditText);

        daysOfWeek[0] = (TextView)findViewById(R.id.dayOfWeek1);
        daysOfWeek[1] = (TextView)findViewById(R.id.dayOfWeek2);
        daysOfWeek[2] = (TextView)findViewById(R.id.dayOfWeek3);
        daysOfWeek[3] = (TextView)findViewById(R.id.dayOfWeek4);
        daysOfWeek[4] = (TextView)findViewById(R.id.dayOfWeek5);
        daysOfWeek[5] = (TextView)findViewById(R.id.dayOfWeek6);
        daysOfWeek[6] = (TextView)findViewById(R.id.dayOfWeek7);

        dates[0] = (TextView)findViewById(R.id.date1);
        dates[1] = (TextView)findViewById(R.id.date2);
        dates[2] = (TextView)findViewById(R.id.date3);
        dates[3] = (TextView)findViewById(R.id.date4);
        dates[4] = (TextView)findViewById(R.id.date5);
        dates[5] = (TextView)findViewById(R.id.date6);
        dates[6] = (TextView)findViewById(R.id.date7);

        productid = (TextView)findViewById(R.id.productID);
        productname = (TextView)findViewById(R.id.productText);

        onhand = (TextView)findViewById(R.id.OH);
        gamble = (TextView)findViewById(R.id.gam);
        gambletype = (TextView)findViewById(R.id.percent);
        facing = (TextView)findViewById(R.id.fac);

        prevadj[0] = (TextView)findViewById(R.id.prevAdj1);
        prevadj[1] = (TextView)findViewById(R.id.prevAdj2);
        prevadj[2] = (TextView)findViewById(R.id.prevAdj3);
        prevadj[3] = (TextView)findViewById(R.id.prevAdj4);
        prevadj[4] = (TextView)findViewById(R.id.prevAdj5);
        prevadj[5] = (TextView)findViewById(R.id.prevAdj6);
        prevadj[6] = (TextView)findViewById(R.id.prevAdj7);

        shipadj[0] = (TextView)findViewById(R.id.shipAdj1);
        shipadj[1] = (TextView)findViewById(R.id.shipAdj2);
        shipadj[2] = (TextView)findViewById(R.id.shipAdj3);
        shipadj[3] = (TextView)findViewById(R.id.shipAdj4);
        shipadj[4] = (TextView)findViewById(R.id.shipAdj5);
        shipadj[5] = (TextView)findViewById(R.id.shipAdj6);
        shipadj[6] = (TextView)findViewById(R.id.shipAdj7);

        tabadj[0] = (TextView)findViewById(R.id.tabAdj1);
        tabadj[1] = (TextView)findViewById(R.id.tabAdj2);
        tabadj[2] = (TextView)findViewById(R.id.tabAdj3);
        tabadj[3] = (TextView)findViewById(R.id.tabAdj4);
        tabadj[4] = (TextView)findViewById(R.id.tabAdj5);
        tabadj[5] = (TextView)findViewById(R.id.tabAdj6);
        tabadj[6] = (TextView)findViewById(R.id.tabAdj7);

        avgsold[0] = (TextView)findViewById(R.id.avgSold1);
        avgsold[1] = (TextView)findViewById(R.id.avgSold2);
        avgsold[2] = (TextView)findViewById(R.id.avgSold3);
        avgsold[3] = (TextView)findViewById(R.id.avgSold4);
        avgsold[4] = (TextView)findViewById(R.id.avgSold5);
        avgsold[5] = (TextView)findViewById(R.id.avgSold6);
        avgsold[6] = (TextView)findViewById(R.id.avgSold7);

        presold[0] = (TextView)findViewById(R.id.preSold1);
        presold[1] = (TextView)findViewById(R.id.preSold2);
        presold[2] = (TextView)findViewById(R.id.preSold3);
        presold[3] = (TextView)findViewById(R.id.preSold4);
        presold[4] = (TextView)findViewById(R.id.preSold5);
        presold[5] = (TextView)findViewById(R.id.preSold6);
        presold[6] = (TextView)findViewById(R.id.preSold7);

        currentorder[0] = (TextView)findViewById(R.id.curOrd1);
        currentorder[1] = (TextView)findViewById(R.id.curOrd2);
        currentorder[2] = (TextView)findViewById(R.id.curOrd3);
        currentorder[3] = (TextView)findViewById(R.id.curOrd4);
        currentorder[4] = (TextView)findViewById(R.id.curOrd5);
        currentorder[5] = (TextView)findViewById(R.id.curOrd6);
        currentorder[6] = (TextView)findViewById(R.id.curOrd7);

        suggestedorder[0] = (TextView)findViewById(R.id.sugOrd1);
        suggestedorder[1] = (TextView)findViewById(R.id.sugOrd2);
        suggestedorder[2] = (TextView)findViewById(R.id.sugOrd3);
        suggestedorder[3] = (TextView)findViewById(R.id.sugOrd4);
        suggestedorder[4] = (TextView)findViewById(R.id.sugOrd5);
        suggestedorder[5] = (TextView)findViewById(R.id.sugOrd6);
        suggestedorder[6] = (TextView)findViewById(R.id.sugOrd7);

        finalorderedit[0] = (EditText)findViewById(R.id.finalOrdEdit1);
        finalorderedit[1] = (EditText)findViewById(R.id.finalOrdEdit2);
        finalorderedit[2] = (EditText)findViewById(R.id.finalOrdEdit3);
        finalorderedit[3] = (EditText)findViewById(R.id.finalOrdEdit4);
        finalorderedit[4] = (EditText)findViewById(R.id.finalOrdEdit5);
        finalorderedit[5] = (EditText)findViewById(R.id.finalOrdEdit6);
        finalorderedit[6] = (EditText)findViewById(R.id.finalOrdEdit7);

        locked[0] = (TextView)findViewById(R.id.locked1);
        locked[1] = (TextView)findViewById(R.id.locked2);
        locked[2] = (TextView)findViewById(R.id.locked3);
        locked[3] = (TextView)findViewById(R.id.locked4);
        locked[4] = (TextView)findViewById(R.id.locked5);
        locked[5] = (TextView)findViewById(R.id.locked6);
        locked[6] = (TextView)findViewById(R.id.locked7);

        prevAdjTotal = (TextView)findViewById(R.id.prevAdjTotal);
        shipAdjTotal = (TextView)findViewById(R.id.shipAdjTotal);
        avgSoldTotal = (TextView)findViewById(R.id.avgSoldTotal);
        preSoldTotal = (TextView)findViewById(R.id.preSoldTotal);
        currentOrderTotal = (TextView)findViewById(R.id.curOrdTotal);

    }

    public void clearTextBoxes(){
        for(int x = 0; x < 7; x++){
            suggestedorder[x].setText("");
            finalorderedit[x].setText("");
        }
    }

    public void updateProduct(){
        OrderProduct p = orderProducts.get(index);
        String finalCount = finalorderedit[0].getText().toString();
        String finalCount2 = finalorderedit[1].getText().toString();
        String finalCount3 = finalorderedit[2].getText().toString();
        String finalCount4 = finalorderedit[3].getText().toString();
        String finalCount5 = finalorderedit[4].getText().toString();
        String finalCount6 = finalorderedit[5].getText().toString();
        String finalCount7 = finalorderedit[6].getText().toString();
        p.setFinalOrder(finalCount);
        p.setFinalOrder2(finalCount2);
        p.setFinalOrder3(finalCount3);
        p.setFinalOrder4(finalCount4);
        p.setFinalOrder5(finalCount5);
        p.setFinalOrder6(finalCount6);
        p.setFinalOrder7(finalCount7);

        Log.e("VALUES", finalCount + "," + finalCount2 + "," + finalCount3 + "," + finalCount4 + "," + finalCount5 +
                "," + finalCount6 + "," + finalCount7);

        //Log.e("EMPTY", p.toString());

        if(!p.isEmpty() && !p.getLocked().equals("YYYYYYY")){
            p.setTouched(1);
            datasource.updateOrderProduct(p);
        }
        //adapter.notifyDataSetChanged();
    }

    public void cancelProduct(View view){

    }

    public void viewAll(View view){

    }

    public void back(View view) {
        Intent intent = new Intent(this, MainMenuActivity.class);
        startActivity(intent);
        finish();
    }

    public void help(View view) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        LogOutTimerUtil.startLogoutTimer(this, this);
        Log.e("TIMER", "OnStart () &&& Starting timer");
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        LogOutTimerUtil.startLogoutTimer(this, this);
        Log.e("TIMER", "User interacting with screen");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //LogOutTimerUtil.stopLogoutTimer();
        Log.e("TIMER", "Being Destroyed");
    }

    @Override
    public void onBackPressed() {
        return;
    }

    @Override
    public void doLogout(boolean foreGround) {
        if(foreGround){
            Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra("force_logout", true);
            startActivity(intent);
        }
        finish();
    }
}
