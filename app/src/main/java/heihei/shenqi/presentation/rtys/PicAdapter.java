package heihei.shenqi.presentation.rtys;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;
import heihei.frame.base.BaseRecyclerAdapter;
import heihei.shenqi.R;
import heihei.shenqi.data.Task;


/**
 * User: lyjq(1752095474)
 * Date: 2016-04-26
 */
public class PicAdapter extends BaseRecyclerAdapter<Task> {

    public PicAdapter(Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_rtys,null);
        holder = new taskHolder(view,onItemClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Task task = getItem(position);
        taskHolder taskHolder = (taskHolder) holder;
        taskHolder.simpleDraweeView.setImageURI(task.getImg());
        taskHolder.simpleDraweeView.setAspectRatio(0.67f);
        taskHolder.textView.setText(task.getTitle());

        if (position == mItems.size() - 1) {
            if(mListener != null)
                mListener.onListEnded();
        }
    }

    protected class taskHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.img)
        SimpleDraweeView simpleDraweeView;
        @BindView(R.id.title)
        TextView textView;
        private OnItemClickListener onItemClickListener;

        public taskHolder(View itemView,OnItemClickListener onItemClickListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.onItemClickListener = onItemClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(onItemClickListener != null){
                onItemClickListener.onItemClick(v,getPosition());
            }
        }
    }

    private ListAdapterListener mListener;

    public void setListener(ListAdapterListener mListener) {
        this.mListener = mListener;
    }

    public interface ListAdapterListener {
        void onListEnded();
    }

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        public void onItemClick(View view, int postion);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.onItemClickListener = listener;
    }
}
