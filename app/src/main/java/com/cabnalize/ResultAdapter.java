package com.cabnalize;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.neno0o.ubersdk.Endpoints.Models.Prices.Price;
import java.util.List;

/**
 * Created by Sumanta.Longjam on 19-08-2016.
 */
public class ResultAdapter extends RecyclerView.Adapter implements View.OnClickListener {

    private Context context;
    private final int VIEW_ROW = 1;
    private final int VIEW_HEADER = 0;
    private List<Object> entityList;

    public ResultAdapter(Context context, List<Object> propertyList) {
        this.context = context;
        this.entityList = propertyList;
    }

    @Override
    public int getItemViewType(int position) {
        return entityList.get(position) instanceof HeaderEntity ? VIEW_HEADER : VIEW_ROW;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        if (viewType == VIEW_ROW) {
            View view = LayoutInflater.from(context).inflate(R.layout.cab_row, parent, false);
            viewHolder = new RowHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.header_row, parent, false);
            viewHolder = new HeaderViewHolder(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Object entity = entityList.get(position);
        if (holder instanceof RowHolder) {
            if(entity != null) {
                if(entity instanceof OlaEntity) {
                    OlaEntity olaEntity = (OlaEntity) entity;
                    ((RowHolder) holder).imageView.setImageResource(R.mipmap.ola_icon);
                    ((RowHolder) holder).displayNameTV.setText(olaEntity.getDisplayName());
                    String estimate = "Rs "+olaEntity.getLowEstimate()+" - " + olaEntity.getHighEstimate();
                    ((RowHolder) holder).estimatePriceTV.setText(estimate);
                }
                else if (entity instanceof Price) {
                    Price price = (Price)entity;
                    ((RowHolder) holder).imageView.setImageResource(R.mipmap.uber_icon);
                    ((RowHolder) holder).displayNameTV.setText(price.getDisplayName());
                    ((RowHolder) holder).estimatePriceTV.setText(price.getEstimate());
                    ((RowHolder) holder).surgeTV.setText(""+price.getSurgeMultiplier());
                }
            }
        } else {
            HeaderEntity headerEntity = (HeaderEntity) entity;
            if(headerEntity.isUber()) {
                ((HeaderViewHolder) holder).companyIcon.setImageResource(R.mipmap.uber_icon);
                ((HeaderViewHolder) holder).companyName.setText("UBER");
            } else {
                ((HeaderViewHolder) holder).companyIcon.setImageResource(R.mipmap.ola_icon);
                ((HeaderViewHolder) holder).companyName.setText("OLA");
            }
        }
    }

    @Override
    public int getItemCount() {
        return entityList.size();
    }

    @Override
    public void onClick(View v) {

    }

    public static class RowHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView displayNameTV, estimatePriceTV, surgeTV;
        public RowHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.imageView);
            displayNameTV = (TextView) view.findViewById(R.id.displayNameTV);
            estimatePriceTV = (TextView) view.findViewById(R.id.estimatePriceTV);
            surgeTV = (TextView) view.findViewById(R.id.surgeTV);
        }
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        public ImageView companyIcon;
        public TextView companyName;
        public HeaderViewHolder(View v) {
            super(v);
            companyIcon = (ImageView) v.findViewById(R.id.companyIcon);
            companyName = (TextView) v.findViewById(R.id.companyName);
        }
    }
}
