package com.cy.devandroidtools.bean;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.cy.devandroidtools.BR;

/**
 * class
 *
 * @author cy
 * @date 2020/5/28.
 */
public class ImageBean extends BaseObservable {
    private int id;
    private String name;
    private String url;
    private String uri;

    public ImageBean(int id, String name, String url, String uri) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.uri = uri;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
