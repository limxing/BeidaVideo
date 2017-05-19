package me.leefeng.beida.notice;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import me.leefeng.beida.R;
import me.leefeng.beida.dbmodel.NoticeMessage;
import me.leefeng.library.utils.LogUtils;

/**
 * Created by limxing on 2017/5/18.
 */

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.NoticeItenHolder> {
    private List<NoticeMessage> list;

    public NoticeAdapter(List<NoticeMessage> list) {
        this.list = list;
    }

    @Override
    public NoticeItenHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NoticeItenHolder(View.inflate(parent.getContext(), R.layout.notice_item, null));
    }

    @Override
    public void onBindViewHolder(NoticeItenHolder holder, int position) {
        NoticeMessage message = list.get(position);
        holder.time.setText(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date(message.getTime())));
        holder.title.setText(message.getTitle());
        holder.des.setText(message.getDescription());
        if (message.isRead()){
            holder.read.setVisibility(View.INVISIBLE);
        }else{
            holder.read.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class NoticeItenHolder extends RecyclerView.ViewHolder {

        TextView time;
        TextView title;
        TextView des;
        TextView read;

        public NoticeItenHolder(View itemView) {
            super(itemView);
            time = (TextView) itemView.findViewById(R.id.notice_item_time);
            title = (TextView) itemView.findViewById(R.id.notice_item_title);
            des = (TextView) itemView.findViewById(R.id.notice_item_des);
            read = (TextView) itemView.findViewById(R.id.notice_item_read);
        }
    }
}
