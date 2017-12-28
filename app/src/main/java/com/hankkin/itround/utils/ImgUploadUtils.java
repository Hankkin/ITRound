package com.hankkin.itround.utils;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.SaveCallback;
import com.hankkin.itround.manage.UserManager;

import java.util.List;

/**
 * Created by huanghaijie on 2017/12/26.
 */

public class ImgUploadUtils {



    private static ImgUploadUtils instance = new ImgUploadUtils();
    public static ImgUploadUtils getInstance() {
        if (instance == null) {
            instance = new ImgUploadUtils();
        }
        return instance;
    }

    private int index = 0;

    public void uploadImg(final List<AVFile> files, final UploadImgCallBack callBack){
        files.get(index)
                .saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if (index == files.size()-1){
                            callBack.uploadSuc(files);
                        }
                        else {
                            index++;
                            uploadImg(files,callBack);
                        }
                    }
                });
    }

    public interface UploadImgCallBack{
        void uploadSuc(List<AVFile> files);
    }

}
