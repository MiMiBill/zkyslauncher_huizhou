package com.muju.note.launcher.app.startUp.ui;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.muju.note.launcher.MainActivity;
import com.muju.note.launcher.R;
import com.muju.note.launcher.app.hostipal.service.MienService;
import com.muju.note.launcher.app.hostipal.service.MissionService;
import com.muju.note.launcher.app.startUp.adapter.ActivationCheckAdapter;
import com.muju.note.launcher.app.startUp.event.StartCheckDataEvent;
import com.muju.note.launcher.app.video.service.VideoService;
import com.muju.note.launcher.base.BaseFragment;
import com.muju.note.launcher.base.LauncherApplication;
import com.muju.note.launcher.service.Department.DepartmentInfoService;
import com.muju.note.launcher.service.encyclope.EncyclopeService;
import com.muju.note.launcher.service.homemenu.HomeMenuService;
import com.muju.note.launcher.service.orderfood.OrderFoodService;
import com.muju.note.launcher.util.log.LogUtil;
import com.muju.note.launcher.util.rx.RxUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 初始化数据界面
 */
public class StartCheckDataFragment extends BaseFragment {

    private static final String TAG = StartCheckDataFragment.class.getSimpleName();
    @BindView(R.id.rv_check)
    RecyclerView rvCheck;

    private List<String> list;
    private ActivationCheckAdapter adapter;

    private Disposable disposable;
    private int progress = 1;
    private int count = 1;

    @Override
    public int getLayout() {
        return R.layout.fragment_start_check;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    handler.removeMessages(1);
                    count++;
                    progress = 2 * count;
                    if (progress > 99) {
                        progress = 99;
                    }
//                    LogFactory.l().i("progress==="+progress);
                    list.set(list.size() - 1, "正在存储..." + progress + "%");
                    adapter.notifyDataSetChanged();
                    rvCheck.scrollToPosition(list.size() - 1);
                    handler.sendEmptyMessageDelayed(1, 1000);
                    break;
            }
        }
    };

    @Override
    public void initData() {
        EventBus.getDefault().register(this);
        list = new ArrayList<>();
        adapter = new ActivationCheckAdapter(R.layout.rv_item_activation_check_msg, list);
        rvCheck.setLayoutManager(new LinearLayoutManager(LauncherApplication.getContext(), LinearLayoutManager.VERTICAL, false));
        rvCheck.setAdapter(adapter);

        VideoService.getInstance().startColumns();
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void showError(String msg) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateMsg(StartCheckDataEvent event) {
        switch (event.getStatus()) {
            case VIDEO_COLUMN_START:
                list.add("正在初始化影视分类数据，请稍候...");
                break;
            case VIDEO_COLUMN_HTTP_START:
                list.add("正在获取后台影视分类数据，请稍候...");
                break;
            case VIDEO_COLUMN_DB_START:
                list.add("正在加载影视分类数据，请稍候...");
                break;
            case VIDEO_COLUMN_SUCCESS:
                list.add("影视分类数据初始化成功！");
                VideoService.getInstance().startVideoTop();
                break;
            case VIDEO_COLUMN_HTTP_FAIL:
                list.add("影视分类后台数据获取失败！1分钟后重新初始化");
                reStartColumn(1);
                break;
            case VIDEO_COLUMN_HTTP_DATA_NULL:
                list.add("影视分类后台数据获取为空！请稍后重试或联系管理人员检查！");
                break;
            case VIDEO_TOP_START:
                list.add("正在初始化影视推荐数据，请稍候...");
                break;
            case VIDEO_TOP_HTTP_START:
                list.add("正在获取后台影视推荐数据，请稍候...");
                break;
            case VIDEO_TOP_DB_START:
                list.add("正在加载影视推荐数据，请稍候...");
                break;
            case VIDEO_TOP_SUCCESS:
                list.add("影视推荐数据初始化成功！");
                VideoService.getInstance().startVideoInfo();
                break;
            case VIDEO_TOP_HTTP_FAIL:
                list.add("影视推荐后台数据获取失败！1分钟后重新初始化");
                reStartVideoTop(1);
                break;
            case VIDEO_TOP_HTTP_DATA_NULL:
                list.add("影视推荐后台数据获取为空！请稍后重试或联系管理人员检查");
                break;

            case VIDEO_INFO_START:
                list.add("正在初始化影视详情数据，请稍候...");
                break;
            case VIDEO_INFO_HTTP_START:
                list.add("正在获取后台影视详情数据，请稍候...");
                break;
            case VIDEO_INFO_HTTP_DATA_NULL:
                list.add("影视详情后台数据获取为空！请稍后重试或联系管理人员检查");
                break;
            case VIDEO_INFO_HTTP_FAIL:
                list.add("影视详情后台数据获取失败！1分钟后重新初始化");
                reStartVideoInfo(1);
                break;
            case VIDEO_DATA_SAVE:
                list.add("影视详情数据正在存储...");
                handler.sendEmptyMessage(1);
                break;
            case VIDEO_DATA_SAVE_FAIL:
                list.add("影视详情数据存储失败!1分钟后重新初始化");
                reStartVideoInfo(1);
                resetProgress();
                break;
            case VIDEO_INFO_SUCCESS:
                list.set(list.size() - 1, "正在存储...100%");
                list.add("影视详情数据初始化成功！");
                MienService.getInstance().startMien();
                resetProgress();
                break;
            case VIDEO_INFO_DOWNLOAD_START:
                list.add("开始下载影视详情数据，请稍候...");
                list.add("正在下载中：0%");
                break;
            case VIDEO_INFO_DOWNLOAD_FAIL:
                list.add("影视详情数据下载失败！1分钟后重新初始化");
                reStartVideoInfo(1);
                break;
            case VIDEO_INFO_DOWNLOAD_PROGRESS:
                list.set(list.size() - 1, "正在下载中：" + event.getProgress() + "%");
                break;
            case VIDEO_INFO_CARSH:
                list.add("遇到异常，请重启设备或联系管理人员：" + event.getCarsh().getMessage());
                break;
            case VIDEO_INFO_DB_START:
                list.add("开始加载影视详情数据，请稍候...");
                list.add("正在加载中：0/0");
                break;
            case VIDEO_INFO_DB_PROGRESS:
                list.set(list.size() - 1, "正在加载中：" + event.getDbProgress());
                break;

            case HOSPITAL_MIEN_START:
                list.add("正在初始化医院风采数据，请稍候...");
                break;
            case HOSPITAL_MIEN_HTTP_START:
                list.add("正在获取后台医院风采数据，请稍候...");
                break;
            case HOSPITAL_MIEN_DB_START:
                list.add("正在加载医院风采数据，请稍候...");
                break;
            case HOSPITAL_MIEN_SUCCESS:
                list.add("医院风采数据初始化成功！");
                EncyclopeService.getInstance().startEncy();
                break;
            case HOSPITAL_MIEN_HTTP_FAIL:
                list.add("医院风采后台数据获取失败！1分钟后重新初始化");
                reStartMien(1);
                break;

            case HOSPITAL_ENCY_START:
                list.add("正在初始化医疗百科数据，请稍候...");
                break;
            case HOSPITAL_ENCY_HTTP_START:
                list.add("正在获取后台医疗百科数据，请稍候...");
                break;
            case HOSPITAL_ENCY_SUCCESS:
                list.set(list.size() - 1, "正在存储...100%");
                list.add("医疗百科数据初始化成功！");
                MissionService.getInstance().startMiss();
                resetProgress();
                break;
            case ENCY_DATA_SAVE:
                list.add("医疗百科详情数据正在存储...");
                handler.sendEmptyMessageDelayed(1, 1000);
                break;
            case ENCY_DATA_SAVE_FAIL:
                list.add("医疗百科数据存储失败!1分钟后重新初始化");
                reStartEncy(1);
                resetProgress();
                break;
            case ENCY_KS_DATA_SAVE:
                list.add("医疗百科科室正在存储...");
                break;
            case ENCY_KS_DATA_SAVE_FAIL:
                list.add("医疗百科科室数据存储失败!1分钟后重新初始化");
                reStartEncy(1);
                break;
            case HOSPITAL_ENCY_HTTP_FAIL:
                list.add("医疗百科后台数据获取失败！1分钟后重新初始化");
                reStartEncy(1);
                break;
            case HOSPITAL_ENCY_UNZIP_START:
                list.add("正在解压缩，请稍候...");
                break;
            case HOSPITAL_ENCY_DOWNLOAD_START:
                list.add("后台访问成功，开始下载，请稍候...");
                list.add("正在下载中：0%");
                break;
            case HOSPITAL_ENCY_DOWNLOAD_PROGRESS:
                list.set(list.size() - 1, "正在下载中：" + event.getProgress() + "%");
                break;
            case HOSPITAL_ENCY_DOWNLOAD_FAIL:
                list.add("下载失败! 1分钟后重试");
                reStartEncy(1);
                break;
            case HOSPITAL_ENCY_FIRST_DB_START:
                list.add("解压完成，正在加载医疗百科科室数据，请稍候...");
                break;
            case HOSPITAL_ENCY_FIRST_DB_END:
                list.add("科室加载完成!");
                break;
            case HOSPITAL_ENCY_TWO_DB_START:
                list.add("正在加载详情数据，请稍候...");
                list.add("正在加载中：0/0");
                break;
            case HOSPITAL_ENCY_TWO_DB_PROGRESS:
                list.set(list.size() - 1, "正在加载中：" + event.getDbProgress());
                break;
            case HOSPITAL_MISS_START:
                list.add("正在初始化医院宣教数据，请稍候...");
                break;
            case HOSPITAL_MISS_HTTP_START:
                list.add("正在获取后台医院宣教数据，请稍候...");
                break;
            case HOSPITAL_MISS_DB_START:
                list.add("正在加载医院宣教数据，请稍候...");
                break;
            case HOSPITAL_MISS_SUCCESS:
                list.add("医院风采数据初始化成功！数据正在后台下载，下载成功后可正常使用!");
                HomeMenuService.getInstance().startMenu();
                break;
            case HOSPITAL_MISS_HTTP_FAIL:
                list.add("医院宣教后台数据获取失败！1分钟后重新初始化");
                reStartMiss(1);
                break;

            case HOME_MENU_START:
                list.add("正在初始化首页模块数据，请稍候...");
                break;
            case HOME_MENU_HTTP_START:
                list.add("正在获取后台首页模块数据，请稍候...");
                break;
            case HOME_MENU_DB_START:
                list.add("正在加载首页模块数据，请稍候...");
                break;
            case HOME_MENU_SUCCESS:
                list.add("首页模块数据初始化成功！");
                HomeMenuService.getInstance().startRebootMenu();
                break;
            case HOME_MENU_HTTP_FAIL:
                list.add("首页模块后台数据获取失败！1分钟后重新初始化");
                reHomeMenu(1);
                break;
            case HOME_MENU_HTTP_DATA_NULL:
                list.add("首页模块后台数据获取为空！请稍后重试或联系管理人员检查");
                break;

            case HOME_MENU_REBOOT_START:
                list.add("正在更新首页模块数据，请稍候...");
                break;
            case HOME_MENU_REBOOT_HTTP_START:
                list.add("正在获取后台首页模块数据，请稍候...");
                break;
            case HOME_MENU_REBOOT_DB_START:
                list.add("正在加载首页模块数据，请稍候...");
                break;
            case HOME_MENU_REBOOT_SUCCESS:
                list.add("首页模块数据更新成功！");
//                getActivity().startActivity(new Intent(getActivity(), MainActivity.class));
//                getActivity().finish();
                OrderFoodService.getInstance().startfoodMenu();
                break;
            case HOME_MENU_REBOOT_HTTP_FAIL:
                list.add("首页模块后台数据获取失败！1分钟后重新更新");
                reRebootHomeMenu(1);
                break;
            case HOME_MENU_REBOOT_HTTP_DATA_NULL:
                list.add("首页模块后台数据获取为空！请稍后重试或联系管理人员检查");
                break;
            case FOOD_MENU_START:
                list.add("正在初始化点餐数据，请稍候...");
                break;
            case FOOD_MENU_HTTP_START:
                list.add("正在获取后台点餐数据，请稍候...");
                break;
            case FOOD_MENU_DB_START:
                list.add("正在加载点餐数据，请稍候...");
                break;
            case FOOD_MENU_SUCCESS:
                list.add("点餐数据初始化成功！");
                OrderFoodService.getInstance().startRebootFoodMenu();
                break;
            case FOOD_MENU_REBOOT_START:
                list.add("正在更新点餐数据，请稍候...");
                break;
            case FOOD_MENU_REBOOT_SUCCESS:
                list.add("点餐数据更新成功，请稍候...");
                DepartmentInfoService.getInstance().startDepartment();//
                break;
            case FOOD_MENU_REBOOT_HTTP_START:
                list.add("正在获取后台点餐数据，请稍候...");
                break;
            case FOOD_MENU_REBOOT_DB_START:
                list.add("正在加载点餐数据，请稍候...");
                break;
            case FOOD_MENU_HTTP_FAIL:
                 list.add("点餐后台数据获取失败！1分钟后重新初始化");
                 reFoodMenu(1);
                break;
            case FOOD_MENU_REBOOT_HTTP_FAIL:
                list.add("点餐后台数据获取失败！1分钟后重新更新");
                reRebootFoodMenu(1);
                break;
            case FOOD_MENU_REBOOT_HTTP_DATA_NULL:
                list.add("点餐数据获取为空！请稍后重试或联系管理人员检查");
                break;

                //新加的
            case DEPARTMENT_START:
                list.add("正在初始化科室数据，请稍候...");
                break;
            case DEPARTMENT_HTTP_START:
                list.add("正在获取科室数据，请稍候...");
                break;
            case DEPARTMENT_DB_START:
                list.add("正在加载科室数据，请稍候...");
                break;
            case DEPARTMENT_SUCCESS:
                list.add("据初始化科室数成功！");
                getActivity().startActivity(new Intent(getActivity(), MainActivity.class));
                getActivity().finish();
                break;
            case DEPARTMENT_HTTP_FAIL:
                list.add("科室数据获取失败！1分钟后重新初始化");
                reDepartmentInfo(1);
                break;

        }
        adapter.notifyDataSetChanged();
        rvCheck.scrollToPosition(list.size() - 1);
    }

    //重置进度
    private void resetProgress() {
        handler.removeMessages(1);
        count = 1;
        progress = 1;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 重新查询影视分类数据
     */
    private void reStartColumn(int min) {
        RxUtil.closeDisposable(disposable);
        disposable = Observable.timer(min, TimeUnit.MINUTES)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {

                        LogUtil.d("=======reStartColumn=====");

                        VideoService.getInstance().startColumns();
                    }
                });
    }

    /**
     * 重新获取首页推荐数据
     *
     * @param min
     */
    private void reStartVideoTop(int min) {
        RxUtil.closeDisposable(disposable);
        disposable = Observable.timer(min, TimeUnit.MINUTES)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        LogUtil.d("=======reStartVideoTop=====");
                        VideoService.getInstance().startVideoTop();
                    }
                });
    }

    /**
     * 重新影视详情数据
     *
     * @param min
     */
    private void reStartVideoInfo(int min) {
        RxUtil.closeDisposable(disposable);
        disposable = Observable.timer(min, TimeUnit.MINUTES)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {

                        LogUtil.d("=======reStartVideoInfo=====");
                        VideoService.getInstance().startVideoInfo();
                    }
                });
    }

    /**
     * 重新获取医院风采数据
     */
    private void reStartMien(int min) {
        RxUtil.closeDisposable(disposable);
        disposable = Observable.timer(min, TimeUnit.MINUTES)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {

                        LogUtil.d("=======reStartMien=====");
                        MienService.getInstance().startMien();
                    }
                });
    }


    /**
     * 重新获取医疗百科数据
     *
     * @param min
     */
    private void reStartEncy(int min) {
        RxUtil.closeDisposable(disposable);
        disposable = Observable.timer(min, TimeUnit.MINUTES)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {

                        LogUtil.d("=======reStartEncy=====");
                        EncyclopeService.getInstance().startEncy();
                    }
                });
    }

    private void reStartMiss(int min) {
        RxUtil.closeDisposable(disposable);
        disposable = Observable.timer(min, TimeUnit.MINUTES)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {

                        LogUtil.d("=======reStartMiss=====");
                        MissionService.getInstance().startMiss();
                    }
                });
    }

    private void reHomeMenu(int min) {
        RxUtil.closeDisposable(disposable);
        disposable = Observable.timer(min, TimeUnit.MINUTES)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        LogUtil.d("=======reHomeMenu=====");
                        HomeMenuService.getInstance().startMenu();
                    }
                });
    }

    private void reFoodMenu(int min) {
        RxUtil.closeDisposable(disposable);
        disposable = Observable.timer(min, TimeUnit.MINUTES)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {

                        LogUtil.d("=======reFoodMenu=====");
                        OrderFoodService.getInstance().startfoodMenu();
                    }
                });
    }


    private void reRebootHomeMenu(int min) {
        RxUtil.closeDisposable(disposable);
        disposable = Observable.timer(min, TimeUnit.MINUTES)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        LogUtil.d("=======reRebootHomeMenu=====");
                        HomeMenuService.getInstance().startRebootMenu();
                    }
                });
    }

    private void reDepartmentInfo(int min) {
        RxUtil.closeDisposable(disposable);
        disposable = Observable.timer(min, TimeUnit.MINUTES)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        LogUtil.d("=======reDepartmentInfo=====");
                        DepartmentInfoService.getInstance().startDepartment();
                    }
                });
    }


    private void reRebootFoodMenu(int min) {
        RxUtil.closeDisposable(disposable);
        disposable = Observable.timer(min, TimeUnit.MINUTES)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {

                        LogUtil.d("=======reRebootFoodMenu=====");
                        OrderFoodService.getInstance().startRebootFoodMenu();
                    }
                });
    }
}
