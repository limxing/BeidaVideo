package me.leefeng.beida.main.menu;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import me.leefeng.beida.R;

/**
 * Created by FengTing on 2017/5/17.
 * https://www.github.com/limxing
 */

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuItemHolder> {
    private List<MenuItem> list;
    MenuItenClickListener listener;

    public MenuAdapter(List<MenuItem> list, MenuItenClickListener listener) {
        this.list = list;
this.listener=listener;
    }

    @Override
    public MenuItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new MenuItemHolder(View.inflate(parent.getContext(), R.layout.menu_item, null));
    }

    @Override
    public void onBindViewHolder(MenuItemHolder holder, final int position) {
        holder.tv.setText(list.get(position).getName());
        holder.iv.setImageResource(list.get(position).getIcon());
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.onMenuItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class MenuItemHolder extends RecyclerView.ViewHolder {
        TextView tv;
        ImageView iv;
        View view;

        public MenuItemHolder(View itemView) {
            super(itemView);
            view = itemView;
            tv = (TextView) itemView.findViewById(R.id.menu_item_name);
            iv = (ImageView) itemView.findViewById(R.id.menu_item_iv);
        }
    }

    public interface MenuItenClickListener {
        void onMenuItemClick(int position);
    }
}
