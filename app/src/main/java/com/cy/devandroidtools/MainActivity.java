package com.cy.devandroidtools;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Bundle;
import android.widget.ImageView;

import com.cy.devandroidtools.bean.ImageBean;
import com.cy.devandroidtools.databinding.ActivityMainBinding;
import com.cy.devandroidtools.file.FileFilter;
import com.cy.devandroidtools.file.bean.Directory;
import com.cy.devandroidtools.file.bean.ImageFile;
import com.cy.devandroidtools.file.callback.FilterResultCallback;
import com.cy.devandroidtools.img.loader.GlideImageLoader;
import com.cy.devandroidtools.permissions.PermissionUtil;
import com.cy.devandroidtools.toast.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    /**
     * 相册目录
     */
    private List<Directory<ImageFile>> dicList = new ArrayList<>();
    /**
     * 某个目录下的文件数组
     */
    private ArrayList<ImageFile> imageLists = new ArrayList<>();

    private ActivityMainBinding mainBinding ;
    private ImageBean imageBean ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(mainBinding.getRoot());
        imageBean = new ImageBean(1 , "第一张图片" , "" , "");
        mainBinding.setImage(imageBean);
        PermissionUtil.requestPermissionsWithTip("我们需要的你给予读写权限，以便获取相册展示..",MainActivity.this, isGranted ->{
            if(isGranted){
                ToastUtil.showToast(MainActivity.this , "获取权限成功");
//                loadImages();
            }
        } , Manifest.permission.WRITE_EXTERNAL_STORAGE);
//        GlideImageLoader.displayImage(mainBinding.iv , "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1587056301299&di=421baf3f56de08f2fd95a4e2969be9e5&imgtype=0&src=http%3A%2F%2Fimages.enet.com.cn%2Fegames%2Farticleimage%2F201204%2F20120416054140892.jpg");
    }

    /**
     * 加载本地图片
     */
    private void loadImages(){
        FileFilter.getImages(MainActivity.this, directories -> {
            imageLists.clear();
            dicList.clear();
            dicList.addAll(directories);
            for (Directory<ImageFile> directory : dicList) {
                if (imageLists.size() == 0) {
                    imageLists.addAll(directory.getFiles());
                    directory.setState(1);
                    imageBean.setName("LoadImageS");
                    GlideImageLoader.displayImage(mainBinding.iv, imageLists.get(16).getUri());
                    break;
                }
            }
        });
    }
}
