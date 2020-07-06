package com.cy.devandroidtools.file.bean;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;


public class ImageFile implements Parcelable {
    private long id;
    private String name;//文件名
    private String path;//文件路径
    private long size;   //byte
    private String bucketId;  //Directory ID
    private String bucketName;  //Directory Name
    private long date;  //Added Date
    private boolean isSelected;
    private int state = 0;//1上传成功  0 正在上传  2  上传失败
    private String ossKey;//判断文件是否重复的唯一判断,且是阿里云的唯一地址

    private int orientation;   //0, 90, 180, 270
    private Uri uri;
    public ImageFile(File file) {
        this.name = file.getName();
        this.path = file.getAbsolutePath();
        this.size = file.length();
    }

    public ImageFile() {
    }

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(path);
        dest.writeLong(size);
        dest.writeString(bucketId);
        dest.writeString(bucketName);
        dest.writeLong(date);
        dest.writeByte((byte) (isSelected ? 1 : 0));
        dest.writeInt(state);
        dest.writeString(ossKey);
        dest.writeInt(orientation);
        dest.writeParcelable(uri,flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ImageFile> CREATOR = new Creator<ImageFile>() {
        @Override
        public ImageFile[] newArray(int size) {
            return new ImageFile[size];
        }

        @Override
        public ImageFile createFromParcel(Parcel in) {
            ImageFile file = new ImageFile();
            file.id = in.readLong();
            file.name = in.readString();
            file.path = in.readString();
            file.size = in.readLong();
            file.bucketId = in.readString();
            file.bucketName = in.readString();
            file.date = in.readLong();
            file.isSelected = in.readByte() != 0;
            file.state = in.readInt();
            file.ossKey = in.readString();
            file.setOrientation(in.readInt());
            file.setUri(in.readParcelable(Uri.class.getClassLoader()));
            return file;
        }
    };

    @Override
    public String toString() {
        return "ImageFile{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", size=" + size +
                ", bucketId='" + bucketId + '\'' +
                ", bucketName='" + bucketName + '\'' +
                ", date=" + date +
                ", isSelected=" + isSelected +
                ", state=" + state +
                ", ossKey='" + ossKey + '\'' +
                ", orientation='" + orientation + '\'' +
                '}';
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getBucketId() {
        return bucketId;
    }

    public void setBucketId(String bucketId) {
        this.bucketId = bucketId;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getOssKey() {
        return ossKey;
    }

    public void setOssKey(String ossKey) {
        this.ossKey = ossKey;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ImageFile)) return false;

        ImageFile file = (ImageFile) o;
        return this.path.equals(file.path);
    }

    @Override
    public int hashCode() {
        return path.hashCode();
    }
}
