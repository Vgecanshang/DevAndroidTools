package com.cy.devandroidtools.file.callback;



import com.cy.devandroidtools.file.bean.Directory;
import com.cy.devandroidtools.file.bean.ImageFile;

import java.util.List;


public interface FilterResultCallback<T extends ImageFile> {
    void onResult(List<Directory<T>> directories);
}
