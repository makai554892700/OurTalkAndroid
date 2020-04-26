package www.mys.com.ourtalkandroid.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mayousheng.www.initview.ViewUtils;


public abstract class BaseFragment extends Fragment {

    private View rootView;

    protected abstract int getLayoutId();

    protected abstract void initView(View view);

    public View getRootView() {
        return rootView;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container
            , @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(getLayoutId(), container, false);
        ViewUtils.initAllView(BaseFragment.class, this, rootView);
        initView(rootView);
        return rootView;
    }
}
