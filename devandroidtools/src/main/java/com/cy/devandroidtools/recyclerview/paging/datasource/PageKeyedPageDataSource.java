package com.cy.devandroidtools.recyclerview.paging.datasource;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import com.cy.devandroidtools.log.CLog;

import java.util.ArrayList;
import java.util.List;

/**
 * PageKeyedPageDataSource
 *  这个加载数据的时候，如果是上拉的时候那么靠最后一条数据返回的key来决定，如果是下拉的话，是根据最顶上的一条数据来决定的
 * 这个用来处理记载聊天记录最方便了。
 *
 *  目前有三种DataSource实现方式,
 * 1、PositionalDataSource
 *   -这个就是根据position来的，主要用来处理固定数量的数据，可以任意获取某段的数据。比如有100条数据，我可以从第20条数据开始获取。
 *   -适用于上拉加载更多的场景, 如京东商城列表浏览, 知乎等
 *
 * 2、ItemKeyedDataSource
 *   -这个在初始化加载了默认的比如10条数据以后，它会先执行loadBefore方法加载初始化的首条数据之前的数据【如果不需要，需要自己在方法里处理】，完事会执行
 *    loadAfter加载初始化的最后一条数据之后的数据
 *    如果接口分页使用的最后一条数据的id之类的那么用这个比较合适
 *   -适用于下一part内容依赖当前某个key来获取, 如获得下一part的专辑列表, 需要传入当前歌手来获取
 *
 * 3、PageKeyedDataSource
 *  -它的数据只会加载一次，来回滑动的话不会再次加载，
 *   适用于有明显上下页的场景, 如支持上一章,下一章跳转可以使用此种方式
 *  -平时如果分页用的是pageNum和pageSize的话用这个比较合适
 *
 *  PageKeyedDataSource<Key, Value> ：适用于目标数据根据页信息请求数据的场景，即Key 字段是页相关的信息。比如请求的数据的参数中包含类似next/previous页数的信息。
 *  ItemKeyedDataSource<Key, Value> ：适用于目标数据的加载依赖特定item的信息， 即Key字段包含的是Item中的信息，比如需要根据第N项的信息加载第N+1项的数据，传参中需要传入第N项的ID时，该场景多出现于论坛类应用评论信息的请求。
 *  PositionalDataSource：适用于目标数据总数固定，通过特定的位置加载数据，这里Key是Integer类型的位置信息，T即Value。 比如从数据库中的1200条开始加在20条数据。
 * ————————————————
 * @author cy
 * @date 2020/5/28.
 */
public class PageKeyedPageDataSource<T> extends PageKeyedDataSource<Integer, T> {
    public static final String TAG = PageKeyedPageDataSource.class.getSimpleName();

    private List<T> dataList ;

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, T> callback) {
        CLog.println("loadInitial size ==="+params.requestedLoadSize);
        //这里的previousPageKey，和nextPageKey决定了前后是否有数据，如果你传个null，那么就表示前边或者后边没有数据了。也就是下边的loadBefore或者LoadAfter不会执行了
        callback.onResult(getDataList(5,params.requestedLoadSize) , 0 , 6);
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, T> callback) {
        //请求上一页数据
        CLog.println("loadBefore size ==="+params.requestedLoadSize);
    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, T> callback) {
        //请求下一页数据
        CLog.println("loadAfter size ==="+params.requestedLoadSize);
        callback.onResult(getDataList(5,params.requestedLoadSize) , params.key+1);

    }

    private List<T> getDataList(int startPosition , int loadSize){
        List<T> list = new ArrayList<>();
        for(int i = 0 ; i < dataList.size() ; i ++){
            if(i >= startPosition && i < loadSize){
                list.add(dataList.get(i));
            }
        }
        return list;
    }
}
