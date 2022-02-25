package com.developeralamin.firechat.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.developeralamin.firechat.R;
import com.developeralamin.firechat.activity.ChartActiivty;
import com.developeralamin.firechat.model.ProfileData;
import com.developeralamin.firechat.model.UserData;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private List<UserData> list;
    private Context context;

    public UserAdapter(List<UserData> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.ViewHolder holder, int position) {

        UserData userData = list.get(position);

        holder.prfile_Name.setText(userData.getName());
        holder.prfile_Phone.setText(userData.getNumber());
        holder.prfile_Eamil.setText(userData.getEmail());

        Glide.with(context).load(userData.getProfilepictureurl()).into(holder.profile_image);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChartActiivty.class);
                intent.putExtra("userId", userData.getId());
                intent.putExtra("userName", userData.getName());
                intent.putExtra("userProfile", userData.getProfilepictureurl());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView profile_image;
        TextView prfile_Name, prfile_Phone, prfile_Eamil;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profile_image = itemView.findViewById(R.id.profile_image);
            prfile_Name = itemView.findViewById(R.id.prfile_Name);
            prfile_Phone = itemView.findViewById(R.id.prfile_Phone);
            prfile_Eamil = itemView.findViewById(R.id.prfile_Eamil);
        }
    }
}
