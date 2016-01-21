package youmo.qianbaidu;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.Collator;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import Core.MenuModel;

/**
 * Created by tanch on 2016/1/18.
 */
public class MenuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final List<MenuModel> Data;

    private Sub_Cilck sc;

    public interface Sub_Cilck
    {
       void OnClick(View v,int i);
    }

    public void SetSub_Cilck(Sub_Cilck sc)
    {
        this.sc=sc;
    }

    public MenuAdapter(List<MenuModel> data)
    {
        this.Data=data;
    }

    private   class MenuHolder_defluat extends RecyclerView.ViewHolder {
        public TextView Sub_Name;

        public MenuHolder_defluat(View v) {
            super(v);
            Sub_Name=(TextView)v.findViewById(R.id.submenu_name);
            Sub_Name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sc.OnClick(v,getAdapterPosition());
                }
            });
        }
    }

    private class MenuHolder_title extends RecyclerView.ViewHolder {
        public TextView Menu_Name;
        public TextView Sub_Name;

        public MenuHolder_title(View v) {
            super(v);
            Menu_Name=(TextView)v.findViewById(R.id.menu_name);
            Sub_Name=(TextView)v.findViewById(R.id.submenu_name);
            Menu_Name.setVisibility(View.VISIBLE);
            Sub_Name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sc.OnClick(v,getAdapterPosition());
                }
            });
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_holder_default, parent, false);
        switch (viewType)
        {
            case 0:
                return new MenuHolder_defluat(v);
            case 1:
                return new MenuHolder_title(v);
            default:
                return new MenuHolder_defluat(v);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof MenuHolder_title )
        {
            ((MenuHolder_title)holder).Menu_Name.setText(Data.get(position).Name);
            ((MenuHolder_title)holder).Sub_Name.setText(Data.get(position).SubName);
        }
        else
        {
            ((MenuHolder_defluat)holder).Sub_Name.setText(Data.get(position).SubName);
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (position==0)
            return 1;
        MenuModel mm1=Data.get(position);
        MenuModel mm2=Data.get(position-1);
        if (mm1.Name.equals(mm2.Name))
            return 0;
        return 1;
    }

    @Override
    public int getItemCount() {
        return Data.size();
    }
}
