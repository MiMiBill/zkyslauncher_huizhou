package com.muju.note.launcher.service.download;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okserver.OkDownload;
import com.lzy.okserver.download.DownloadListener;
import com.lzy.okserver.download.DownloadTask;
import com.muju.note.launcher.util.log.LogUtil;
import com.muju.note.launcher.util.sdcard.SdcardConfig;

import java.io.File;

/**
 *  下载模块
 */
public class DownLoadService {

    private static final String TAG="DownLoadService";

    public static DownLoadService downLoadService=null;
    public static DownLoadService getInstance(){
        if(downLoadService==null){
            downLoadService=new DownLoadService();
        }
        return downLoadService;
    }

    /**
     *  下载后的名称为hasecode格式
     * @param url
     * @param type
     */
    public void downLoadHaseCode(final String url, String type){
        LogUtil.d(TAG,"下载文件："+url+type);
        File file=new File(SdcardConfig.RESOURCE_FOLDER,url.hashCode()+type);
        if(file.exists()){
            LogUtil.i(TAG,"文件路径已存在，无需下载："+file.getName());
            return;
        }
        OkDownload.getInstance().setFolder(SdcardConfig.RESOURCE_FOLDER);
        GetRequest<File> request = OkGo.<File>get(url);
        DownloadTask task=OkDownload.request(url,request)
                .fileName(url.hashCode()+type)
                .save()
                .register(new DownloadListener(url) {
                    @Override
                    public void onStart(Progress progress) {

                    }

                    @Override
                    public void onProgress(Progress progress) {
                    }

                    @Override
                    public void onError(Progress progress) {
                        LogUtil.d(TAG,"下载失败："+progress.exception.getMessage());
                    }

                    @Override
                    public void onFinish(File file, Progress progress) {
                        LogUtil.d(TAG,"下载成功："+progress.filePath);
                    }

                    @Override
                    public void onRemove(Progress progress) {

                    }
                });
        task.start();
    }

}
