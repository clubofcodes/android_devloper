package classes;

import android.content.Context;
import android.content.Intent;
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

import java.util.ArrayList;

public class OfllineDataAdapter extends RecyclerView.Adapter<OfllineDataAdapter.MyViewHolder> {
    private Context context;
    ArrayList<String> userList;

    public OfllineDataAdapter(Context context, ArrayList<String> list) {
        this.context = context;
        this.userList = list;
    }

    @NonNull
    @Override
    public OfllineDataAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        return new OfllineDataAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OfllineDataAdapter.MyViewHolder holder, final int position) {
        holder.offlineEmailTxt.setText(String.valueOf(userList.get(position)));
        holder.dataViewLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailClick = new Intent(context, DataDisplayActivity.class);
                emailClick.putExtra("username",String.valueOf(userList.get(position)));
                context.startActivity(emailClick);
                ((AppCompatActivity)context).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtNameList, offlineEmailTxt;
        LinearLayout dataViewLinearLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNameList = itemView.findViewById(R.id.txtNameList);
            offlineEmailTxt = itemView.findViewById(R.id.offlineEmailTxt);
            LinearLayout layout = itemView.findViewById(R.id.emailPhoneLayout);
            layout.setVisibility(View.GONE);
            txtNameList.setVisibility(View.GONE);
            dataViewLinearLayout = itemView.findViewById(R.id.dataViewLinearLayout);
        }

    }
}
