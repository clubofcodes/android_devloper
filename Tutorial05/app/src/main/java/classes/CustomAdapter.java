package classes;
//*******************"Tutorial 10 (CustomAdapter for listItem)"*******************
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rku.tutorial05.R;

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

            TextView txtName =(TextView) view.findViewById(R.id.listTxtName);
            TextView txtEmail =(TextView) view.findViewById(R.id.listTxtEmail);
            LinearLayout emailPhoneLayout = (LinearLayout) view.findViewById(R.id.emailPhoneLayout);

            try {
                JSONObject object = jsonArray.getJSONObject(i);
                txtName.setText("Name: " + object.getString("name"));
                txtEmail.setText("Email: " + object.getString("email"));
                txtName.setTextColor(Color.RED);
                txtEmail.setTextColor(Color.RED);
                txtEmail.setVisibility(View.VISIBLE);
                emailPhoneLayout.setVisibility(View.GONE);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return view;
    }
    //*******************"Tutorial 10 (onlineUsers List)"*******************
}
