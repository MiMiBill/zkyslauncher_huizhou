package com.muju.note.launcher.app.msg.presenter;

import com.muju.note.launcher.app.msg.contract.MsgContract;
import com.muju.note.launcher.app.msg.db.CustomMessageDao;
import com.muju.note.launcher.base.BasePresenter;
import com.muju.note.launcher.litepal.LitePalDb;
import com.muju.note.launcher.util.log.LogUtil;

import org.litepal.LitePal;
import org.litepal.crud.callback.FindMultiCallback;

import java.util.List;

public class MsgPresenter extends BasePresenter<MsgContract.View> implements MsgContract.Presenter {

    /**
     * 查询通知消息
     */
    @Override
    public void querymsg() {
        LitePalDb.setZkysDb();
        LitePal.order("createTime desc").findAsync(CustomMessageDao.class).listen(new FindMultiCallback<CustomMessageDao>() {
            @Override
            public void onFinish(List<CustomMessageDao> list) {
                if (mView == null) {
                    LogUtil.e("mView为空");
                    return;
                }
                if (list == null || list.size() <= 0) {
                    mView.getMsgNull();
                    return;
                }
                mView.getMsg(list);
            }
        });
    }
}
