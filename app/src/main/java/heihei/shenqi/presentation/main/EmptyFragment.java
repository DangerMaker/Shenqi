package heihei.shenqi.presentation.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.zip.Inflater;

import heihei.shenqi.R;

/**
 * Created by Administrator on 2016/8/1.
 */
public class EmptyFragment extends Fragment {
    TextView textView;
    String string;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_empty, null);
        textView = (TextView) view.findViewById(R.id.textView);
        textView.setText(string);
        return view;
    }

    public void setTextView(String str) {
         this.string = str;
    }
}
