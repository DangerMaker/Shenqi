package heihei.shenqi.presentation.luluhei;

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
public class TasksAdapter extends BaseRecyclerAdapter<Task> {

    public TasksAdapter(Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_tasks,null);
        holder = new taskHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Task task = getItem(position);
        taskHolder taskHolder = (taskHolder) holder;
        taskHolder.simpleDraweeView.setImageURI(task.getImg());
        taskHolder.simpleDraweeView.setAspectRatio(1.66f);
        taskHolder.textView.setText(task.getTitle());

        if (position == mItems.size() - 1) {
            if(mListener != null)
                mListener.onListEnded();
        }
    }

    protected class taskHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img)
        SimpleDraweeView simpleDraweeView;
        @BindView(R.id.title)
        TextView textView;

        public taskHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private ListAdapterListener mListener;

    public void setListener(ListAdapterListener mListener) {
        this.mListener = mListener;
    }

    public interface ListAdapterListener {
        void onListEnded();
    }
}