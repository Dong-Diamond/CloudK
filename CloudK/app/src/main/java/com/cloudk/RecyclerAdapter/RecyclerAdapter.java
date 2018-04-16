package com.cloudk.RecyclerAdapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cloudk.Attributes;
import com.cloudk.R;

import java.util.List;

/**
 * Created by dong on 2018/2/2.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{

    private List<Attributes> dataList;

    public RecyclerAdapter(List<Attributes> dataList)
    {
        this.dataList = dataList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.attr_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Attributes attributes = dataList.get(position);
        holder.title.setText(attributes.getAttributeName()+":"+attributes.getId());
        holder.content.setText(attributes.getAgreementId()
                +" "+attributes.getAttributeInterval()
                +" "+attributes.getAttributeKey()
                +" "+attributes.getCreateDate()
                +" "+attributes.getUpdateDate());

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView content;
        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.attr_title);
            content = (TextView) itemView.findViewById(R.id.attr_content);
        }
    }
}
