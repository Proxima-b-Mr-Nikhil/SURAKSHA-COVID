package com.nikhilece.ad_suraksha;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private Context mContext;
    private List<Upload> mUploads;
    public static  final int MSG_TYPE_RIGHT = 1;

    ImageAdapter(Context context, List<Upload> uploads) {
        mContext = context;
        mUploads = uploads;
    }
    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_item_left, parent, false);

        return new ImageViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull final ImageViewHolder holder, int i) {
        Upload uploadCurrent = mUploads.get(i);
        holder.textViewName.setText(uploadCurrent.getName());


    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    class ImageViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;


        ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.show_message);


        }
    }
}
