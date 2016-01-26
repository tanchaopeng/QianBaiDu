package youmo.qianbaidu.Sub;

import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
    private int ImageWidth;
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
        this.ImageWidth=0;
    }
    public SubItemAdapter(List<SubItemModel> data,int width)
    {
        this.Data=data;
        this.ImageWidth=width;
    }

    private  class SubItemHolder_defluat extends RecyclerView.ViewHolder {
        public TextView ItemName;

        public SubItemHolder_defluat(View v) {
            super(v);
            ItemName=(TextView)v.findViewById(R.id.textView_image_itemTitle);
            ItemName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Iclick.OnClick(v,getAdapterPosition());
                }
            });
        }
    }
    private  class SubItemHolder_video extends RecyclerView.ViewHolder {
        public TextView textView;
        public ImageView imageView;

        public SubItemHolder_video(View v) {
            super(v);
            textView=(TextView)v.findViewById(R.id.textView_video_title);
            imageView=(ImageView)v.findViewById(R.id.imageView_video_smallIamge);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Iclick.OnClick(v,getAdapterPosition());
                }
            });
           // int h=(int)((float)ImageWidth/0.75);
            //imageView.setLayoutParams(new RecyclerView.LayoutParams(ImageWidth,ImageWidth));
        }
    }

    @Override
    public int getItemViewType(int position) {
       return Data.get(position).imageUrl!=null? 1:0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v=null;
        switch (viewType)
        {
            case 0://小说，图片
                v= LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_image_item, parent, false);
                return new SubItemHolder_defluat(v);
            case 1://视频
                v= LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_video_list, parent, false);
                return new SubItemHolder_video(v);
            default:
                v= LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_image_item, parent, false);
                return new SubItemHolder_defluat(v);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SubItemHolder_defluat)
        {
            ((SubItemHolder_defluat)holder).ItemName.setText(Data.get(position).title);
        }
        else
        {
            ((SubItemHolder_video)holder).textView.setText(Data.get(position).title);
        }
    }

    @Override
    public int getItemCount() {
        return Data.size();
    }
}
