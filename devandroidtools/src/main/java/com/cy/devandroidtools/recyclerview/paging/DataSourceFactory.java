package com.cy.devandroidtools.recyclerview.paging;

import androidx.annotation.NonNull;
import androidx.paging.DataSource;

import com.cy.devandroidtools.recyclerview.paging.datasource.PageKeyedPageDataSource;

/**
 * 数据工厂
 *
 * @author cy
 * @date 2020/5/28.
 */
public class DataSourceFactory<K, V> extends DataSource.Factory<K, V> {
    @NonNull
    @Override
    public DataSource create() {
        return new PageKeyedPageDataSource();
    }
}

