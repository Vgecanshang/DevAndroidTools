package com.cy.devandroidtools.recyclerview.paging;

import android.app.Activity;
import android.view.View;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cy.devandroidtools.log.CLog;
import com.cy.devandroidtools.recyclerview.base.ViewHolder;


/**
 * 分页管理器(基于Paging:https://developer.android.google.cn/topic/libraries/architecture/paging)
 *
 * @author cy
 * @date 2020/5/29.
 */
public class ListPagedManager<T> {
    public static final String TAG = ListPagedManager.class.getSimpleName();

    /**
     * 初始加载数量
     */
    public static final int PARAMS_INITIAL_LOAD_SIZE = 10;
    /**
     * 往下滑动加载数量
     */
    public static final int PARAMS_RELOAD_SIZE = 10;
    /**
     * 提前多少数据开始继续加载数据
     * 如果是上滑的话对应的就是离最后个item还有2个位置的时候开始记载更多数据。下拉一样的道理
     */
    public static final int PARAMS_PREFETCH_DISTANCE = 2;

    /**
     * LayoutManager -grid
     */
    public static final String LAYOUT_MANAGER_GRIDLAYOUT = "grid";
    /**
     * LayoutManager -linear
     */
    public static final String LAYOUT_MANAGER_LINEARLAYOUT = "linear";

    private Activity mActivity;
    private RecyclerView mRecyclerView;
    private PagerManagerListener<T> pagerListener;
    private String managerType = LAYOUT_MANAGER_LINEARLAYOUT;
    private PagerListAdapter<T> pagerListAdapter;
    private DiffUtil.ItemCallback<T> diffCallback;

    public ListPagedManager(Activity activity, RecyclerView recyclerView, PagerManagerListener refreshCallBack , DiffUtil.ItemCallback<T> diffCallback) {
        this.mActivity = activity;
        this.mRecyclerView = recyclerView;
        this.pagerListener = refreshCallBack;
        this.diffCallback = diffCallback;
        initRecyclerView();
    }
    public ListPagedManager(Activity activity, RecyclerView recyclerView, PagerManagerListener refreshCallBack , String managerType , DiffUtil.ItemCallback<T> diffCallback) {
        this.mActivity = activity;
        this.mRecyclerView = recyclerView;
        this.pagerListener = refreshCallBack;
        this.managerType = managerType;
        this.diffCallback = diffCallback;
        initRecyclerView();
    }

    private void initRecyclerView() {
        PagedList.Config config = new PagedList.Config.Builder()
                .setPageSize(PARAMS_RELOAD_SIZE)
                .setEnablePlaceholders(false)//当item为null时，是否使用PlaceHolder展示
                .setInitialLoadSizeHint(PARAMS_INITIAL_LOAD_SIZE)
                .setPrefetchDistance(PARAMS_PREFETCH_DISTANCE)
                .build();

        //LiveData 可以放在 ViewModel 层处理
        LiveData<PagedList<T>> pagerLiveData = new LivePagedListBuilder<>(new DataSourceFactory<Integer, T>(), config).build();
        pagerLiveData.observe( (FragmentActivity)mActivity , ts -> {
            CLog.d(TAG , "ListPagedManager pagerLiveData observe ts="+ts.toString());
            pagerListAdapter.submitList(ts);//调用PagedListAdapter中的方法绑定，刷新UI
        });
        pagerListAdapter = new PagerListAdapter<T>(diffCallback , pagerListener);
        if(managerType.equals(LAYOUT_MANAGER_GRIDLAYOUT)){
            final GridLayoutManager manager = new GridLayoutManager(getActivity(), 1);
            mRecyclerView.setLayoutManager(manager);
        }else {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
        mRecyclerView.setAdapter(pagerListAdapter);
    }

    public interface PagerManagerListener<T> {
        /**
         * 获取列表条目布局
         * @return resource
         */
        int getItemLayoutId();

        /**
         * 列表条目数据填充
         * @param holder
         * @param entity
         * @param position
         */
        void convert(ViewHolder holder, T entity, int position);

        /**
         * 列表条目点击事件
         * @param view
         * @param position
         * @param data
         */
        void onItemClick(View view, int position, T data);
    }

    private Activity getActivity() {
        return mActivity;
    }
}
