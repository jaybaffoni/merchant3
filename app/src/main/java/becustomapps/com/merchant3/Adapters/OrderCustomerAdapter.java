package becustomapps.com.merchant3.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import becustomapps.com.merchant3.Objects.Customer;
import becustomapps.com.merchant3.Objects.OrderCustomer;
import becustomapps.com.merchant3.R;
import becustomapps.com.merchant3.Utilities.DataSource;

/**
 * Created by Jay on 4/10/2018.
 */

public class OrderCustomerAdapter extends ArrayAdapter<OrderCustomer> {
    Context context;
    List<OrderCustomer> orderCustomers;
    HashMap<String, Customer> customerHashMap = new HashMap<String, Customer>();
    DataSource datasource;

    public OrderCustomerAdapter(Context context, List<OrderCustomer> orderCustomers, HashMap<String, Customer> customerHashMap) {
        super(context, 0);
        this.context = context;
        this.orderCustomers = orderCustomers;
        this.customerHashMap = customerHashMap;
        datasource = new DataSource(context);
        datasource.open();
    }

    @Override
    public int getCount() {
        return orderCustomers.size();
    }

    private class ViewHolder {
        TextView cust_no;
        TextView cust_name;
        TextView cust_address;
        ImageView completed;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        OrderCustomer orderCustomer = orderCustomers.get(position);
        Customer customer = customerHashMap.get(orderCustomer.getAcct());
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.customer_item, parent, false);
            holder = new ViewHolder();

            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        holder.cust_no = (TextView)convertView.findViewById(R.id.cust_no);
        holder.cust_name = (TextView)convertView.findViewById(R.id.cust_name);
        holder.cust_address = (TextView)convertView.findViewById(R.id.cust_address);
        holder.completed = (ImageView)convertView.findViewById(R.id.image_check);

        holder.cust_no.setText(customer.getCust_no());
        holder.cust_name.setText(customer.getCust_name());
        holder.cust_address.setText(customer.getCust_address());

        if(orderCustomer.getEnd().equals("")){
            holder.completed.setVisibility(View.INVISIBLE);
        } else {
            holder.completed.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

}