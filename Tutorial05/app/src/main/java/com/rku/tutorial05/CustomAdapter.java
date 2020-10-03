package com.rku.tutorial05;
//*******************"Tutorial 10 (CustomAdapter for listItem)"*******************
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CustomAdapter extends BaseAdapter {
    private Context context;
    private JSONArray jsonArray;

    public CustomAdapter(Context context, JSONArray jsonArray) {
        this.context = context;
        this.jsonArray = jsonArray;
    }

    @Override
    public int getCount() {return jsonArray.length(); }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.list_item,viewGroup,false);

            TextView txtTitle =(TextView) view.findViewById(R.id.listTxtTitle);
            TextView txtEmail =(TextView) view.findViewById(R.id.listTxtEmail);

            try {
                JSONObject object = jsonArray.getJSONObject(i);
                txtTitle.setText(object.getString("name"));
                txtEmail.setText(object.getString("email"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return view;
    }
    //*******************"Tutorial 10 (onlineUsers List)"*******************
}
