package www.mys.com.ourtalkandroid.base;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.mayousheng.www.initview.ViewDesc;

import www.mys.com.ourtalkandroid.R;
import www.mys.com.ourtalkandroid.utils.LogUtils;

public abstract class BaseAppbarLoadingActivity extends BaseLoadingActivity {

    @ViewDesc(viewId = R.id.toolbar)
    public Toolbar toolbar;
    @ViewDesc(viewId = R.id.appbar_left)
    public ImageView appBarLeftIcon;
    @ViewDesc(viewId = R.id.appbar_right)
    public ImageView appBarRightIcon;
    @ViewDesc(viewId = R.id.appbar_right2)
    public ImageView appBarRightIcon2;
    @ViewDesc(viewId = R.id.appbar_title)
    public TextView appBarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        long start = System.currentTimeMillis();
        super.onCreate(savedInstanceState);
        if (getLeftIcon() != -1) {
            appBarLeftIcon.setImageResource(getLeftIcon());
            appBarLeftIcon.setOnClickListener(onLeftIconClick());
        } else {
            appBarLeftIcon.setVisibility(View.GONE);
        }
        if (getRightIcon() != -1) {
            appBarRightIcon.setImageResource(getRightIcon());
            appBarRightIcon.setOnClickListener(onRightIconClick());
        } else {
            appBarRightIcon.setVisibility(View.GONE);
        }
        if (getRightIcon2() != -1) {
            appBarRightIcon2.setImageResource(getRightIcon2());
            appBarRightIcon2.setOnClickListener(onRightIconClick2());
        } else {
            appBarRightIcon2.setVisibility(View.GONE);
        }
        appBarTitle.setText(getAppBarTitle());
        setSupportActionBar(toolbar);
    }

    public int getRightIcon() {
        return -1;
    }


    public int getRightIcon2() {
        return -1;
    }

    public View.OnClickListener onRightIconClick() {
        return v -> {
        };
    }

    public View.OnClickListener onRightIconClick2() {
        return v -> {
        };
    }

    public int getLeftIcon() {
        return -1;
    }

    public View.OnClickListener onLeftIconClick() {
        return v -> {
            finish();
        };
    }

    public abstract String getAppBarTitle();

}
