package com.muju.note.launcher.callkey.service;

import com.muju.note.launcher.callkey.Constant;
import com.muju.note.launcher.util.ShellUtils;
import com.muju.note.launcher.util.log.LogUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class CallKeyService {

//    private static KeyCodeFileObserver keyCodeFileObserver;

    private static int styCount = 0;

    public static void start()
    {
        Observable.timer((long) (1), TimeUnit.SECONDS, Schedulers.newThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        //开启log，保存到制定的文件内
                        File file =  new File(Constant.KEY_CODE_LOG_FILE_PATH_DIR);
                        if (!file.isDirectory())
                        {
                            LogUtil.d("Log文件夹正在创建");
                            if (!file.mkdir() && styCount < 3)
                            {
                                styCount ++;
                                start();
                            }else {
                                LogUtil.d("Log文件夹创建失败");
                                return;
                            }
                        }
                        LogUtil.d("Log文件夹创建成功");
                        ArrayList<String> commands = new ArrayList<>();
                        commands.add("rm -r " + Constant.KEY_CODE_LOG_FILE_PATH);
                        commands.add("logcat -s xxx:D  -f " +  Constant.KEY_CODE_LOG_FILE_PATH);
                        ShellUtils.execCommand(commands,false);
                        LogUtil.d("Log正在收集");
                    }
                });

//        //开启文件监听
//        keyCodeFileObserver = new KeyCodeFileObserver(Constant.KEY_CODE_LOG_FILE_PATH, FileObserver.MODIFY);
//        keyCodeFileObserver.startWatching();

    }

//    private static void  stop()
//    {
//        keyCodeFileObserver.stopWatching();
//    }




}
