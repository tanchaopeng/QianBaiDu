package youmo.qianbaidu;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

/**
 * Created by tanch on 2016/1/18.
 */
public class YmBaseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<String> Data;

    private IItemCilck Iclick;

    public interface IItemCilck
    {
        void OnClick(View v, int i);
    }
    public void SetOnClick(IItemCilck iic)
    {
        Iclick=iic;
    }

    public YmBaseAdapter(List<String> data)
    {
        this.Data=data;
    }

    private  class YmBaseHolder_defluat extends RecyclerView.ViewHolder {
        public TextView ItemName;

        public YmBaseHolder_defluat(View v) {
            super(v);
            ItemName=(TextView)v.findViewById(R.id.submenu_name);
            ItemName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Iclick.OnClick(v,getAdapterPosition());
                }
            });
        }
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.ymbase_defluat, parent, false);
        return new YmBaseHolder_defluat(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((YmBaseHolder_defluat)holder).ItemName.setText(Data.get(position));
    }

    @Override
    public int getItemCount() {
        return Data.size();
    }
}
