package youmo.qianbaidu.Sub;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

import Core.SubItemModel;
import youmo.qianbaidu.R;

/**
 * Created by tanch on 2016/1/18.
 */
public class SubItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<SubItemModel> Data;

    private IItemCilck Iclick;

    public interface IItemCilck
    {
        void OnClick(View v, int i);
    }
    public void SetOnClick(IItemCilck iic)
    {
        Iclick=iic;
    }

    public SubItemAdapter(List<SubItemModel> data)
    {
        this.Data=data;
    }

    private  class SubItemHolder_defluat extends RecyclerView.ViewHolder {
        public TextView ItemName;

        public SubItemHolder_defluat(View v) {
            super(v);
            ItemName=(TextView)v.findViewById(R.id.title);
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
        return new SubItemHolder_defluat(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((SubItemHolder_defluat)holder).ItemName.setText(Data.get(position).title);
    }

    @Override
    public int getItemCount() {
        return Data.size();
    }
}
