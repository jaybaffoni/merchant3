package becustomapps.com.merchant3.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.Normalizer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import becustomapps.com.merchant3.Activities.FormActivity;
import becustomapps.com.merchant3.Objects.Authcode;
import becustomapps.com.merchant3.Objects.Shipment;
import becustomapps.com.merchant3.R;

public class AuthcodeAdapter extends RecyclerView.Adapter<AuthcodeAdapter.MyViewHolder> {

    private List<Authcode> authcodes;
    private int total = 0;
    private FormActivity activity;
    boolean[] opened;
    Map<String, Shipment> shipmentMap = new HashMap<String, Shipment>();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        int entryCount = 0;
        int adjustmentCount = 0;
        int id;
        public TextView cust_no, prod_no, prod_name, index, shipdata;
        public EditText onhand, notsold, markdown, mdretail;
        public Button adjust;
        public EditText charge, short_, damaged, cripple, transfer, recall;
        public LinearLayout expandableLayout;

        public MyViewHolder(View view) {
            super(view);

            cust_no = (TextView)view.findViewById(R.id.cust_no);
            prod_no = (TextView)view.findViewById(R.id.prod_no);
            prod_name = (TextView)view.findViewById(R.id.prod_name);
            index = (TextView)view.findViewById(R.id.index);
            shipdata = (TextView)view.findViewById(R.id.shipment);

            onhand = (EditText)view.findViewById(R.id.onhandEntry);
            notsold = (EditText)view.findViewById(R.id.notsoldEntry);
            markdown = (EditText)view.findViewById(R.id.markdownEntry);
            mdretail = (EditText)view.findViewById(R.id.mdretailEntry);
            adjust = (Button)view.findViewById(R.id.adjustButton);

            expandableLayout = (LinearLayout)view.findViewById(R.id.expandableAdjustments);

            charge = (EditText)view.findViewById(R.id.chargeEntry);
            short_ = (EditText)view.findViewById(R.id.shortEntry);
            damaged = (EditText)view.findViewById(R.id.damagedEntry);
            cripple = (EditText)view.findViewById(R.id.crippleEntry);
            transfer = (EditText)view.findViewById(R.id.transferEntry);
            recall = (EditText)view.findViewById(R.id.recallEntry);

            mdretail.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(5,2)});
        }
    }


    public AuthcodeAdapter(List<Authcode> acs, HashMap<String, Shipment> shipmentMap, FormActivity activity) {
        this.authcodes = acs;
        total = authcodes.size();
        this.activity  = activity;
        this.shipmentMap = shipmentMap;
        opened = new boolean[total];
        for(boolean b: opened){
            b = false;
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.form_item, parent, false);
        MyViewHolder mvh = new MyViewHolder(itemView);
        EntryTextWatcher etw = new EntryTextWatcher(mvh);
        mvh.onhand.addTextChangedListener(etw);
        mvh.notsold.addTextChangedListener(etw);
        mvh.markdown.addTextChangedListener(etw);
        mvh.mdretail.addTextChangedListener(etw);

        AdjustmentTextWatcher atw = new AdjustmentTextWatcher(mvh);
        mvh.charge.addTextChangedListener(atw);
        mvh.short_.addTextChangedListener(atw);
        mvh.damaged.addTextChangedListener(atw);
        mvh.cripple.addTextChangedListener(atw);
        mvh.transfer.addTextChangedListener(atw);
        mvh.recall.addTextChangedListener(atw);

        return mvh;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.id = position;
        holder.entryCount = 0;
        holder.adjustmentCount = 0;

        if(opened[position]){
            holder.expandableLayout.setVisibility(View.VISIBLE);
        } else {
            holder.expandableLayout.setVisibility(View.GONE);
        }

        Authcode ac = authcodes.get(position);
        holder.cust_no.setText(ac.getCust_no());
        holder.prod_no.setText(ac.getProd_no());
        holder.prod_name.setText(ac.getProd_name());
        holder.index.setText((position + 1) + " of " + total);

        if(shipmentMap.containsKey(ac.getProd_no())){
            holder.shipdata.setText(shipmentMap.get(ac.getProd_no()).toString());
        } else {
            holder.shipdata.setText("");
        }

        holder.onhand.setText(ac.getOnhand());
        holder.notsold.setText(ac.getNotsold());
        holder.markdown.setText(ac.getMarkdown());
        holder.mdretail.setText(ac.getMdretail());

        holder.charge.setText(ac.getCharge());
        holder.short_.setText(ac.getShort_());
        holder.damaged.setText(ac.getDamaged());
        holder.cripple.setText(ac.getCripple());
        holder.transfer.setText(ac.getTransfer());
        holder.recall.setText(ac.getRecall());

        holder.onhand.setTag("onhand");

        holder.adjust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.expandableLayout.getVisibility() == View.GONE){
                    holder.expandableLayout.setVisibility(View.VISIBLE);
                    holder.charge.requestFocus();
                    opened[position] = true;
                } else {
                    holder.expandableLayout.setVisibility(View.GONE);
                    holder.mdretail.requestFocus();
                    opened[position] = false;
                }
            }
        });
        holder.mdretail.setOnEditorActionListener(new EditText.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_NEXT){
                    if(holder.expandableLayout.getVisibility() == View.GONE){
                        int pos = position + 1;
                        if(pos >= authcodes.size()){
                            return true;
                        }
                        activity.scroll(pos);
                        holder.onhand.requestFocus();
                    }
                }
                return false;
            }
        });

        holder.recall.setOnEditorActionListener(new EditText.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_NEXT){
                    int pos = position + 1;
                    if(pos >= authcodes.size()){
                        return true;
                    }
                    activity.scroll(pos);
                    holder.onhand.requestFocus();
                }
                return false;
            }
        });

        /*if(activity.recyclerHasFocus()){
            Log.e("FOCUS", "here");
            holder.onhand.requestFocus();
        }*/

    }

    private class EntryTextWatcher implements TextWatcher {
        MyViewHolder vh;

        public EntryTextWatcher(MyViewHolder e) {
            vh = e;
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        public void afterTextChanged(Editable s) {
            if(vh.entryCount < 3){
                vh.entryCount++;
            } else {
                activity.updateRow(vh.id,
                        vh.onhand.getText().toString(),
                        vh.notsold.getText().toString(),
                        vh.markdown.getText().toString(),
                        vh.mdretail.getText().toString()
                );

            }
        }
    }

    private class AdjustmentTextWatcher implements TextWatcher {
        MyViewHolder vh;

        public AdjustmentTextWatcher(MyViewHolder e) {
            vh = e;
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        public void afterTextChanged(Editable s) {
            if(vh.adjustmentCount < 5){
                vh.adjustmentCount++;
            } else {
                activity.updateAdjust(vh.id,
                        vh.charge.getText().toString(),
                        vh.short_.getText().toString(),
                        vh.damaged.getText().toString(),
                        vh.cripple.getText().toString(),
                        vh.transfer.getText().toString(),
                        vh.recall.getText().toString()
                );
                vh.adjust.setText(authcodes.get(vh.id).getAdjustmentCount() + "");
            }

        }
    }

    public class DecimalDigitsInputFilter implements InputFilter {

        Pattern mPattern;

        public DecimalDigitsInputFilter(int digitsBeforeZero,int digitsAfterZero) {
            mPattern=Pattern.compile("[0-9]{0," + (digitsBeforeZero-1) + "}+((\\.[0-9]{0," + (digitsAfterZero-1) + "})?)||(\\.)?");
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            Matcher matcher=mPattern.matcher(dest);
            if(!matcher.matches())
                return "";
            return null;
        }

    }

    @Override
    public int getItemCount() {
        return authcodes.size();
    }
}