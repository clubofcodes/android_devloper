package classes;
//*******************"Tutorial 10, 12(RecycleView Adapter class for Online data)"*******************
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.rku.tutorial05.DataDisplayActivity;
import com.rku.tutorial05.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OnlineDataAdapter extends RecyclerView.Adapter<OnlineDataAdapter.MyViewHolder> {
    private Context context;
    JSONArray jsonArray;

    public OnlineDataAdapter(Context context, JSONArray jsonArray) {
        this.jsonArray = jsonArray;
        this.context = context;
    }

    @NonNull
    @Override
    public OnlineDataAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OnlineDataAdapter.MyViewHolder holder, final int position) {
        try {
            JSONObject jsonObject = jsonArray.getJSONObject(position);
            holder.txtName.setText("Name: " + jsonObject.getString("name"));
            holder.txtEmail.setText("Email: \n" + jsonObject.getString("email"));
            holder.txtPhone.setText("Phone: \n" + jsonObject.getString("phone"));
            holder.txtName.setTextColor(Color.RED);
            holder.txtEmail.setTextColor(Color.RED);
            holder.txtPhone.setTextColor(Color.RED);
            holder.dataViewLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DataDisplayActivity.class);
                    intent.putExtra("userPosition",position);
                    intent.putExtra("temp",4);
                    context.startActivity(intent);
                    ((AppCompatActivity)context).finish();
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return jsonArray.length();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtName,txtEmail,txtPhone,offlineEmailTxt;
        LinearLayout dataViewLinearLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtNameList);
            txtEmail = itemView.findViewById(R.id.txtEmail);
            txtPhone = itemView.findViewById(R.id.txtPhone);
            offlineEmailTxt = itemView.findViewById(R.id.offlineEmailTxt);
            offlineEmailTxt.setVisibility(View.GONE);
            dataViewLinearLayout = itemView.findViewById(R.id.dataViewLinearLayout);
        }
    }
    //*******************"Tutorial 10, 12"*******************
}
