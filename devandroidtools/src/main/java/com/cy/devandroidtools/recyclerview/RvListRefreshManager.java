package com.cy.devandroidtools.recyclerview;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.cy.devandroidtools.R;
import com.cy.devandroidtools.recyclerview.adapter.CommonAdapter;
import com.cy.devandroidtools.recyclerview.base.ViewHolder;
import com.cy.devandroidtools.recyclerview.interfaces.OnItemClickListener;
import com.cy.devandroidtools.recyclerview.interfaces.OnLoadMoreListener;
import com.cy.devandroidtools.recyclerview.interfaces.OnNetWorkErrorListener;
import com.cy.devandroidtools.recyclerview.interfaces.OnRefreshListener;
import com.cy.devandroidtools.recyclerview.paging.ListPagedManager;
import com.cy.devandroidtools.recyclerview.paging.PagerListAdapter;
import com.cy.devandroidtools.view.nodata.CommonLoadingDataPage;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据列表分页管理类
 *
 * @author cy
 * @date 2020/5/18.
 */
public class RvListRefreshManager<T> {

    public static final String LAYOUTMANAGER_GRIDLAYOUT = "grid";
    public static final String LAYOUTMANAGER_LINEARLAYOUT = "linear";


    public interface IRvListRefreshManager<T> {
        /**
         * 获取列表条目布局
         */
        int getItemLayoutId();

        /**
         * 加载列表数据请求
         */
        void loadData(int currentPage, int pageSize);

        /**
         * 列表条目数据填充
         */
        void convert(ViewHolder holder, T entity, int position);

        /**
         * 列表条目点击事件
         */
        void onItemClick(View view, int position, T data);

    }

    /**
     * 加载列表数据
     */
    public void loadData() {
        mRefreshCallBack.loadData(mCurrentPage, REQUEST_COUNT);
    }

    /**
     * 加载首页数据
     */
    public void loadFirstPageData() {
        mCurrentPage = 1;
        mRecyclerView.refresh();

    }

    /**
     * 获取列表数据
     */
    public List<T> getDataList() {
        return mList;
    }

    private IRvListRefreshManager mRefreshCallBack;

    private CommonLoadingDataPage mLayoutNoData;// 无数据展示控件
    private String mNoDataReason = "";
    private int mNoDataImgResId;

    private Activity mActivity;
    private LRecyclerView mRecyclerView;
    private LRecyclerViewAdapter mAdapter;
    private List<T> mList = new ArrayList<>();

    //共多少页
    private int totalPage;
    //每页展示多少条数据
    private final int REQUEST_COUNT = 20;
    //当前页数
    private int mCurrentPage = 1;
    private String managerType = null;
    public RvListRefreshManager(Activity activity, LRecyclerView recyclerView, IRvListRefreshManager refreshCallBack , String managerType) {
        this.mActivity = activity;
        this.mRecyclerView = recyclerView;
        this.mRefreshCallBack = refreshCallBack;
        this.managerType = managerType;
        initRecyclerView();
    }

    public static final int INITIALLOADSIZE = 40;//初始加载数量
    public static final int RELOADSIZE = 10;//往下滑动加载数量
    private void init(FragmentActivity activity){
        PagedList.Config config = new PagedList.Config.Builder()
                .setPageSize(RELOADSIZE)//分页加载量
                .setEnablePlaceholders(false)//当item为null时，是否使用PlaceHolder展示
                .setInitialLoadSizeHint(INITIALLOADSIZE)//预加载的数量
                .setPrefetchDistance(8) //提前多少数据开始继续加载数据。如果是上滑的话对应的就是离最后个item还有2个位置的时候开始记载更多数据。下拉一样的道理
                .build();

        ////可以放在ViewModel层处理
        dataList = new LivePagedListBuilder(new DataSourceFactory(), config).build();

        dataList.observe(activity, new Observer<PagedList<T>>() {
            @Override
            public void onChanged(PagedList<T> ts) {

            }
        });
        PagerListAdapter pagerListAdapter = new PagerListAdapter(new DiffUtil.ItemCallback() {
            @Override
            public boolean areItemsTheSame(@NonNull Object oldItem, @NonNull Object newItem) {
                return false;
            }

            @Override
            public boolean areContentsTheSame(@NonNull Object oldItem, @NonNull Object newItem) {
                return false;
            }
        }, new ListPagedManager.PagerManagerListener() {
            @Override
            public int getItemLayoutId() {
                return 0;
            }

            @Override
            public void convert(ViewHolder holder, Object entity, int position) {

            }

            @Override
            public void onItemClick(View view, int position, Object data) {

            }
        });
        mRecyclerView.setAdapter(pagerListAdapter);

    }
    private LiveData<PagedList<T>> dataList;
    class DataSourceFactory extends DataSource.Factory<Integer,T>{

        @NonNull
        @Override
        public DataSource create() {
            return null;
        }
    }


    /**
     * 设置无数据时的布局
     *
     * @param noDataLayout 无数据控件
     * @param noDataReason 无数据提示文字
     */
    public void setNoDataLayout(CommonLoadingDataPage noDataLayout, String noDataReason) {
        setNoDataLayout(noDataLayout, noDataReason, R.drawable.icon_no_data);
    }

    /**
     * 设置无数据时的布局
     *
     * @param noDataLayout   无数据控件
     * @param noDataReason   无数据提示文字
     * @param noDataImgResId 无数据提示图片
     */
    public void setNoDataLayout(CommonLoadingDataPage noDataLayout, String noDataReason, int noDataImgResId) {
        this.mLayoutNoData = noDataLayout;
        this.mNoDataReason = noDataReason;
        this.mNoDataImgResId = noDataImgResId;
        // 无数据 、 请求失败 重新加载
        noDataLayout.setOnClickListener(v -> loadFirstPageData());
    }

    private Activity getActivity() {
        return mActivity;
    }

    private void initRecyclerView() {
        mAdapter = new LRecyclerViewAdapter(setBaseAdapter());


        mRecyclerView.setAdapter(mAdapter);
        if(managerType.equals(LAYOUTMANAGER_GRIDLAYOUT)){
            final GridLayoutManager manager = new GridLayoutManager(getActivity(), 2);
            mRecyclerView.setLayoutManager(manager);

            manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return mAdapter.isFooter(position)|| mAdapter.isHeader(position)|| mAdapter.isRefreshHeader(position)? manager.getSpanCount():1;
                }
            });
        }else {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }




        mRecyclerView.setOnRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh() {
                mCurrentPage = 1;
                loadData();
            }
        });

        mRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {

            @Override
            public void onLoadMore() {
                // TODO Auto-generated method stub
                if (mCurrentPage < totalPage) {
                    mCurrentPage++;
                    loadData();
                } else {
                    mRecyclerView.setNoMore(true);
                }
            }
        });




        mAdapter.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {
                // TODO Auto-generated method stub
                mRefreshCallBack.onItemClick(view, position, mList.get(position));
            }
        });





    }

    private CommonAdapter setBaseAdapter() {
        return new CommonAdapter<T>(mActivity, mRefreshCallBack.getItemLayoutId(), mList) {
            @Override
            protected void convert(ViewHolder holder, T entity, int position) {
                mRefreshCallBack.convert(holder, entity, position);
            }

        };
    }

    public void addHeader(View v) {
        if (v == null) {
            return;
        }
        if (mCurrentPage == 1) {
            mAdapter.removeHeaderView();
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            v.setLayoutParams(lp);
            mAdapter.addHeaderView(v);
        }
    }

    /**
     * 加载数据成功
     */
    public void loadDataSuccess(int total, List<T> dataList) {
        totalPage = total;
        if (mCurrentPage == 1) {
            mList.clear();
        }
        mList.addAll(dataList);

        mRecyclerView.refreshComplete(REQUEST_COUNT);

        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }

        if (mLayoutNoData != null) {
            mRecyclerView.setEmptyView(mLayoutNoData);
            mLayoutNoData.setNoDataReason(mNoDataReason);
            mLayoutNoData.setNoDataIcon(mNoDataImgResId);
        }
    }

    /**
     * 加载列表数据失败
     */
    public void loadDataFailure() {
        //获取网络数据失败
        mRecyclerView.refreshComplete(REQUEST_COUNT);
        mAdapter.notifyDataSetChanged();
        if (mLayoutNoData != null) {
            mRecyclerView.setEmptyView(mLayoutNoData);
            mLayoutNoData.setNetError();
        }


        mRecyclerView.setOnNetWorkErrorListener(new OnNetWorkErrorListener() {

            @Override
            public void reload() {
                mCurrentPage--;
                loadData();
            }
        });

    }

    public void notifyItemRemoved(T bean) {
        mAdapter.notifyItemRemoved(mList.indexOf(bean) + 2);
        mList.remove(bean);
    }

    public void notifyDataSetChanged() {
        mAdapter.notifyDataSetChanged();
    }

    public void notifyItemChanged(int position) {
        mAdapter.notifyItemChanged(position);
    }

    public LRecyclerViewAdapter getLAdapter() {
        return mAdapter;
    }


    public void setOnUserRefreshListener(UserRefreshListener listener){
        if(mRecyclerView != null){
            mRecyclerView.setUserRefreshListener(listener);
        }

    }

    /** 用户下拉刷新*/
    public interface UserRefreshListener {
        void onUserRefresh();
    }


}
