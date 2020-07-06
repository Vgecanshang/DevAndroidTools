package com.cy.devandroidtools.view.nodata;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cy.devandroidtools.R;

public class CommonLoadingDataPage extends LinearLayout {
	
	LinearLayout mLayout;
	ImageView mIvIcon;
	TextView mTvReason;
	private Context mContext;
    private int res;
	 public CommonLoadingDataPage(Context context) {
	        this(context, null);

	    }

	    public CommonLoadingDataPage(Context context, AttributeSet attrs) {
	        this(context, attrs, 0);

	    }

	    public CommonLoadingDataPage(Context context, AttributeSet attrs, int defStyleAttr) {
	        super(context, attrs, defStyleAttr);
	        this.mContext = context;
	        View view = LayoutInflater.from(context).inflate(R.layout.loading_nodata_page, this, true);
	        mLayout = findViewById(R.id.customer_no_data_layout);
	        mIvIcon = findViewById(R.id.customer_no_data_iv_icon);
	        mTvReason = findViewById(R.id.customer_no_data_tv_reason);
	    }


	    public void setNoDataIcon(int res) {
	        this.res = res;
	        mIvIcon.setImageResource(res);
	    }

	    public void setNoDataReason(int res) {
	        mTvReason.setText(mContext.getResources().getString(res));
	    }

	    public void setNoDataReason(String res) {
	        mTvReason.setText(res);
	    }

	    public void setLayoutClickLoadData(OnClickListener onClickListener) {
	        mLayout.setOnClickListener(onClickListener);
	    }

	    public int getNoDataIcon() {
	        return res;
	    }


	    /**
	     *网络异常空
	     */
	    public void setNetError(){
	        setNoDataIcon(R.drawable.icon_no_data);
	        setNoDataReason(R.string.str_load_data_net_error);
	    }

	    /**
	     * 数据空
	     */
	    public void setDataEmpty(){
	        setNoDataIcon(R.drawable.icon_no_data);
			if (TextUtils.isEmpty(mTvReason.getText())) {
				setNoDataReason(R.string.str_load_data_nothing);
			}
	    }
}
