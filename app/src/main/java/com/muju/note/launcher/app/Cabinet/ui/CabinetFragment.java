package com.muju.note.launcher.app.Cabinet.ui;

import android.Manifest;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.muju.note.launcher.R;
import com.muju.note.launcher.app.Cabinet.bean.CabinetBean;
import com.muju.note.launcher.app.Cabinet.bleLock.BluetoothLeService;
import com.muju.note.launcher.app.Cabinet.bleLock.utils.HexUtils;
import com.muju.note.launcher.app.Cabinet.bleLock.utils.SampleGattAttributes;
import com.muju.note.launcher.app.Cabinet.contract.CabinetContract;
import com.muju.note.launcher.app.Cabinet.event.ReturnBedEvent;
import com.muju.note.launcher.app.Cabinet.presenter.CabinetPresenter;
import com.muju.note.launcher.app.luckdraw.ui.LuckDrawFragment;
import com.muju.note.launcher.app.video.event.VideoNoLockEvent;
import com.muju.note.launcher.base.BaseFragment;
import com.muju.note.launcher.base.LauncherApplication;
import com.muju.note.launcher.litepal.LitePalDb;
import com.muju.note.launcher.service.db.PadConfigSubDao;
import com.muju.note.launcher.util.DateUtil;
import com.muju.note.launcher.util.app.MobileInfoUtil;
import com.muju.note.launcher.util.log.LogUtil;
import com.muju.note.launcher.util.qr.QrCodeUtils;
import com.muju.note.launcher.util.sdcard.SdcardConfig;
import com.muju.note.launcher.view.MyVideoView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePal;
import org.litepal.crud.callback.FindMultiCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.OnClick;
import me.leefeng.promptlibrary.PromptDialog;

/**
 * 屏安柜
 */
public class CabinetFragment extends BaseFragment<CabinetPresenter> implements CabinetContract.View {

    private static final String BLE_MAC = "50:33:8B:71:E5:A4";
    private static final String BLE_NAME = "wwt";
    private static BluetoothDevice curluetoothDevice;

    private static byte[] token = new byte[4];
    private static byte CHIP_TYPE;
    private static byte DEV_TYPE;
    private static byte[] gettoken = {0x06, 0x01, 0x01, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
    byte[] sendDataBytes = null;
    private boolean isAuto = false;
    private int count = 0;
    private PromptDialog promptDialog;
    private boolean isBLELockOpened;



    //以上蓝牙相关
    private static final String TAG = "CabinetFragment";
    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.tv_use)
    TextView tvUse;
    @BindView(R.id.iv_code)
    ImageView ivCode;
    @BindView(R.id.lly_not_login)
    LinearLayout llyNotLogin;
    @BindView(R.id.lly_nocabnet)
    LinearLayout llyNoCabinet;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.lly_login)
    LinearLayout llyLogin;
    @BindView(R.id.lly_prise)
    LinearLayout llyPrise;
    @BindView(R.id.lly_orser)
    LinearLayout llyOrser;
    @BindView(R.id.tv_start)
    TextView tvStart;
    @BindView(R.id.tv_end)
    TextView tvEnd;
    @BindView(R.id.tv_rent_time)
    TextView tvRentTime;
    @BindView(R.id.btn_unlock)
    Button btnUnlock;
    @BindView(R.id.btn_lock)
    Button btnLock;
    @BindView(R.id.rel_titlebar)
    RelativeLayout relTitlebar;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.video_view)
    MyVideoView videoView;
    private boolean isOrder = false;
    private CabinetBean.DataBean dataBean;
    private Handler myhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    myhandler.removeMessages(1);
                    if (isOrder) {
                        setTime();
                    }
                    break;
            }
        }
    };

    @Override
    public int getLayout() {
        return R.layout.fragment_cabinet;
    }

    @Override
    public void initData() {
        tvTitle.setText("屏安柜");
        promptDialog = new PromptDialog(getActivity());
        relTitlebar.setBackgroundColor(getResources().getColor(R.color.white));
        EventBus.getDefault().register(this);
        mPresenter.getCabnetOrder();
        playVideo();
        initBleLock();

    }

    //************************蓝牙相关***********************************//

    /**
     * BLE通讯广播
     */
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case SampleGattAttributes.ACTION_GATT_CONNECTED:
                    //链接
//                    deviceStatus.setText("连接状态：已连接");
                    BluetoothLeService.getInstance().isBLELockConnected = true;
                    break;
                case SampleGattAttributes.ACTION_GATT_DISCONNECTED:
                    //断开
                    BluetoothLeService.getInstance().isBLELockConnected = false;
                    promptDialog.showWarn("设备已断开连接!");
//                    progressDialog.dismiss();
//                    deviceStatus.setText("连接状态：已断开");
//                    count = 0;
//                    openCount.setText("开锁次数：" +count);
                    break;
                case SampleGattAttributes.ACTION_GATT_SERVICES_DISCOVERED:
                    //发现服务
                    promptDialog.showLoading("设备已经连接,正初始化设备...");
                    handler.sendEmptyMessageDelayed(0, 2000);
                    break;
                case SampleGattAttributes.ACTION_BLE_REAL_DATA:
                    parseData(intent.getStringExtra("data"));
                    break;
                case BluetoothAdapter.ACTION_STATE_CHANGED:
//                    Log.e(LockManageActivity.class.getSimpleName(),"state_changed");
                    break;
            }
        }
    };



    /**
     * 解析锁反馈的指令
     *
     * @param value 反馈数据
     */
    private void parseData(String value) {
        byte[] values = HexUtils.hexStringToBytes(value);
        byte[] x = new byte[16];
        System.arraycopy(values, 0, x, 0, 16);
        byte[] decrypt = BluetoothLeService.Decrypt(x, SampleGattAttributes.key);
        String decryptString = HexUtils.bytesToHexString(decrypt).toUpperCase();
        if (decryptString.startsWith("0602")) {//token
            if (decrypt != null && decrypt.length == 16) {
                if (decrypt[0] == 0x06 && decrypt[1] == 0x02) {
                    token[0] = decrypt[3];
                    token[1] = decrypt[4];
                    token[2] = decrypt[5];
                    token[3] = decrypt[6];
                    CHIP_TYPE = decrypt[7];
                    DEV_TYPE = decrypt[10];
                    promptDialog.showSuccess("初始化设备成功!");
//                    deviceVersion.setText("当前版本："+Integer.parseInt(decryptString.substring(16, 18), 16) + "." + Integer.parseInt(decryptString.substring(18, 20), 16));
//                    handler.sendEmptyMessageDelayed(1, 1000);//去获取电量
                }
            }
//            handler.sendEmptyMessage(1);
        } else if (decryptString.startsWith("0202")) {//电量
//            progressDialog.dismiss();
//            if (decryptString.startsWith("020201ff")) {
//                deviceCz.setText("获取电量失败");
//            } else {
//                String battery = decryptString.substring(6, 8);
//                deviceBattery.setText("当前电量：" + Integer.parseInt(battery, 16));
//            }
        } else if (decryptString.startsWith("0502")) {//开锁
            if (decryptString.startsWith("05020101")) {
//                deviceCz.setText("开锁失败");
                isBLELockOpened = false;
                promptDialog.showWarnAlert("打开柜子失败,请重试！",null);
            } else {
                count++;
                isBLELockOpened = true;
                promptDialog.showSuccess("柜子已打开!");
//                deviceCz.setText("开锁成功");
//                openCount.setText("开锁次数：" +count);
            }
        } else if (decryptString.startsWith("050F")) {//锁状态
            if (decryptString.startsWith("050F0101")) {
//                deviceCz.setText("当前操作：锁已关闭");
                isBLELockOpened = false;
            } else {
//                deviceCz.setText("当前操作：锁已开启");
                isBLELockOpened = true;
            }
        } else if (decryptString.startsWith("050D")) {//复位
            if (decryptString.startsWith("050D0101")) {
//                deviceCz.setText("当前操作：复位失败");
            } else {
//                deviceCz.setText("当前操作：复位成功");
            }
        }else if (decryptString.startsWith("0508")){//上锁
            if (decryptString.startsWith("05080101")){
//                deviceCz.setText("当前操作：上锁失败");
            }else {
//                deviceCz.setText("当前操作：上锁成功");
                if (isAuto){
                    handler.sendEmptyMessageDelayed(2,1000);
                }

            }
        }else if (decryptString.startsWith("0505")){
            if (decryptString.startsWith("05050101")){
//                deviceCz.setText("当前操作：修改密码失败");
            }else {
//                deviceCz.setText("当前操作：修改密码成功");
            }
        }else if (decryptString.startsWith("CB0503")){
            BluetoothLeService.getInstance().writeCharacteristic(new byte[]{0x05, 0x04, 0x06,SampleGattAttributes.password[0],SampleGattAttributes.password[1],SampleGattAttributes.password[2],SampleGattAttributes.password[3],SampleGattAttributes.password[4],SampleGattAttributes.password[5], token[0], token[1], token[2], token[3], 0x00, 0x00, 0x00});
        }
    }



    private void openBleLock()
    {
        new Thread(){
            @Override
            public void run() {
                super.run();
                for (int i = 0 ;i < 1; i++)
                {
                    sendDataBytes = new byte[]{0x05, 0x01, 0x06, SampleGattAttributes.password[0],SampleGattAttributes.password[1],SampleGattAttributes.password[2],SampleGattAttributes.password[3],SampleGattAttributes.password[4],SampleGattAttributes.password[5],   token[0], token[1], token[2], token[3], 0x00, 0x00, 0x00};
                    boolean isSuccess =  BluetoothLeService.getInstance().writeCharacteristic(sendDataBytes);
                    LogUtil.d("ble 写入：" + isSuccess);
                    if (isBLELockOpened)
                    {
                        break;
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        }.start();
    }



    private void getBelLockState()
    {
        sendDataBytes = new byte[]{0x05, 0x0E, 0x01, 0X01, token[0], token[1], token[2], token[3], 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
        BluetoothLeService.getInstance().writeCharacteristic(sendDataBytes);
    }

//    @Override
//    public void onClick(View v) {
//        sendDataBytes = null;
//        switch (v.getId()) {
//            case R.id.bt_open://开锁
//
//
//                new Thread(){
//                    @Override
//                    public void run() {
//                        super.run();
//                        for (int i = 0 ;i < 3;i++)
//                        {
//                            sendDataBytes = new byte[]{0x05, 0x01, 0x06, SampleGattAttributes.password[0],SampleGattAttributes.password[1],SampleGattAttributes.password[2],SampleGattAttributes.password[3],SampleGattAttributes.password[4],SampleGattAttributes.password[5],   token[0], token[1], token[2], token[3], 0x00, 0x00, 0x00};
//                            App.getInstance().getBluetoothLeService().writeCharacteristic(sendDataBytes);
//                            try {
//                                Thread.sleep(1000);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//                }.start();
//
//
//                break;
//            case R.id.bt_status://获取锁状态
//                sendDataBytes = new byte[]{0x05, 0x0E, 0x01, 0X01, token[0], token[1], token[2], token[3], 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
//                App.getInstance().getBluetoothLeService().writeCharacteristic(sendDataBytes);
//
//                break;
//            case R.id.bt_close://复位
//                sendDataBytes = new byte[]{0x05, 0x0c, 0x01, 0x01, token[0], token[1], token[2], token[3], 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
//                App.getInstance().getBluetoothLeService().writeCharacteristic(sendDataBytes);
//
//                break;
//            case R.id.bt_update_password://修改密码
//                App.getInstance().getBluetoothLeService().writeCharacteristic(new byte[]{0x05, 0x03, 0x06,SampleGattAttributes.password[0],SampleGattAttributes.password[1],SampleGattAttributes.password[2],SampleGattAttributes.password[3],SampleGattAttributes.password[4],SampleGattAttributes.password[5],  token[0], token[1], token[2], token[3], 0x00, 0x00, 0x00});
//
//                break;
//        }
//    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    BluetoothLeService.getInstance().writeCharacteristic(gettoken);
                    break;
                case 1://获取电量
                    byte[] batteryBytes = {0x02, 0x01, 0x01, 0x01, token[0], token[1], token[2], token[3], 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
                    BluetoothLeService.getInstance().writeCharacteristic(batteryBytes);
                    break;
                case 2://开锁
                    sendDataBytes = new byte[]{0x05, 0x01, 0x06, SampleGattAttributes.password[0], SampleGattAttributes.password[1], SampleGattAttributes.password[2], SampleGattAttributes.password[3], SampleGattAttributes.password[4], SampleGattAttributes.password[5], token[0], token[1], token[2], token[3], 0x00, 0x00, 0x00};
                    BluetoothLeService.getInstance().writeCharacteristic(sendDataBytes);
                    break;
            }
        }
    };


    //------------------------------------


    @Override
    public void onDestroy() {
        super.onDestroy();
        //取消监听
        getActivity().unregisterReceiver(broadcastReceiver);
    }


    private void initBleLock()
    {


        //监听广播
        getActivity().registerReceiver(broadcastReceiver, SampleGattAttributes.makeGattUpdateIntentFilter());
        //开柜按钮
        btnUnlock.setEnabled(true);
        btnUnlock.setBackgroundColor(getResources().getColor(R.color.color_38B4E9));
        //搜索设备

        if (BluetoothLeService.getInstance().initBluetooth()) {
            LogUtil.d("初始化蓝牙成功");
            BluetoothAdapter bluetoothAdapter = BluetoothLeService.getInstance().getmBluetoothAdapter();
            if (bluetoothAdapter!=null&&!bluetoothAdapter.isEnabled()){
                Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBT, 1);
            }
            requestPermission(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},101);

            if (curluetoothDevice == null)
            {
                //没有设备，开始扫描
                promptDialog.showLoading("正在连接设备，请稍后...");
                startScanDevice();
            }else {
                //连接
                if (BluetoothLeService.getInstance().isBLELockConnected)
                {
                    //设备已经连接
                }else {
                    promptDialog.showLoading("正在连接设备，请稍后...");
                    BluetoothLeService.getInstance().connect(curluetoothDevice.getAddress());
                }

            }

        }else {
            LogUtil.d("初始化蓝牙失败");
            promptDialog.showWarn("初始化蓝牙失败...");
        }

    }


    private int REQUEST_CODE_PERMISSION = 0x00099;

    /**
     * 请求权限
     *
     * @param permissions 请求的权限
     * @param requestCode 请求权限的请求码
     */
    public void requestPermission(String[] permissions, int requestCode) {
        this.REQUEST_CODE_PERMISSION = requestCode;
        if (checkPermissions(permissions)) {
            permissionSuccess(REQUEST_CODE_PERMISSION);
        } else {
            List<String> needPermissions = getDeniedPermissions(permissions);
            ActivityCompat.requestPermissions(getActivity(), needPermissions.toArray(new String[needPermissions.size()]), REQUEST_CODE_PERMISSION);
        }
    }

    /**
     * 获取权限成功
     *
     * @param requestCode
     */
    public void permissionSuccess(int requestCode) {
        Log.d(TAG, "获取权限成功=" + requestCode);

    }

    /**
     * 检测所有的权限是否都已授权
     *
     * @param permissions
     * @return
     */
    private boolean checkPermissions(String[] permissions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(getActivity(), permission) !=
                    PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取权限集中需要申请权限的列表
     *
     * @param permissions
     * @return
     */
    private List<String> getDeniedPermissions(String[] permissions) {
        List<String> needRequestPermissionList = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(getActivity(), permission) !=
                    PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permission)) {
                needRequestPermissionList.add(permission);
            }
        }
        return needRequestPermissionList;
    }


     private BluetoothAdapter bluetoothAdapter;

    private void startScanDevice() {
        BluetoothLeService bluetoothLeService = BluetoothLeService.getInstance();

        bluetoothAdapter = bluetoothLeService.getmBluetoothAdapter();

        bluetoothAdapter.startLeScan(new UUID[]{SampleGattAttributes.bltServerUUID},leScanCallback);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                bluetoothAdapter.stopLeScan(leScanCallback);
                if (curluetoothDevice == null)
                {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            promptDialog.showWarn("没有找到合适的蓝牙设备...");
                        }
                    });
                }
            }
        },5000);


    }

    private BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            if (BLE_NAME.equalsIgnoreCase(device.getName()) && curluetoothDevice == null)
            {
                LogUtil.d("BLE MAC：" + device.getAddress());
                //指定的蓝牙
                if (BLE_MAC.equalsIgnoreCase(device.getAddress()))
                {
                    curluetoothDevice = device;
                    bluetoothAdapter.stopLeScan(leScanCallback);
                    bluetoothAdapter.cancelDiscovery();
                    //连接
                    BluetoothLeService.getInstance().connect(curluetoothDevice.getAddress());
                }
            }

        }
    };


    //************************以上蓝牙相关***********************************//

    @Override
    public void initPresenter() {
        mPresenter = new CabinetPresenter();
    }

    @Override
    public void showError(String msg) {

    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        myhandler.removeMessages(111);
        if (videoView != null) {
            videoView.stopPlayback();
        }
        EventBus.getDefault().post(new VideoNoLockEvent(true));
//        EventBus.getDefault().unregister(this);
    }

    @OnClick(R.id.ll_back)
    public void onViewClicked() {
        pop();
    }


    @OnClick({R.id.lly_prise, R.id.lly_orser, R.id.btn_unlock, R.id.btn_lock, R.id.iv_cabinet_play})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.lly_prise:
                start(new LuckDrawFragment());
                break;
            case R.id.lly_orser:
                if (isOrder) {
                    start(CabinetOrderFragment.newInstance(dataBean));
                }
                break;
            case R.id.btn_unlock:

                if (curluetoothDevice ==null || !BluetoothLeService.getInstance().isBLELockConnected)
                {
                    initBleLock();
                }else {
                    promptDialog.showLoading("正在开柜，请稍后...");
                    //蓝牙开锁
                    openBleLock();
                }

//                if (isOrder) {
//                    start(UnlockFragment.newInstance(dataBean));
//                }
                break;
            case R.id.btn_lock:
                if (isOrder) {
                    start(ReturnBedFragment.newInstance(dataBean));
                }
                break;
            case R.id.iv_cabinet_play:
                playVideo();
                break;
        }
    }

    //播放视频
    private void playVideo() {
        LitePalDb.setZkysDb();
        LitePal.where("type =?", "openVideo").limit(1).findAsync(PadConfigSubDao
                .class).listen(new FindMultiCallback<PadConfigSubDao>() {
            @Override
            public void onFinish(List<PadConfigSubDao> list) {
                if (list != null && list.size() > 0) {
                    PadConfigSubDao subDao = list.get(0);
                    if (subDao != null) {
                        String path = subDao.getContent();
                        String videoPath = SdcardConfig.RESOURCE_FOLDER + path.hashCode() + ".mp4";
                        Uri uri = Uri.parse(videoPath);
                        videoView.setVideoURI(uri);
                        videoView.setVisibility(View.VISIBLE);
                        videoView.start();
                        EventBus.getDefault().post(new VideoNoLockEvent(false));
                        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                videoView.stopPlayback();
                                videoView.setVisibility(View.GONE);
                            }
                        });
                    }
                }
            }
        });
    }


    //推送成功或者订单页面归还成功,重新拉取订单
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ReturnBedEvent event) {
        mPresenter.getCabnetOrder();
    }


    @Override
    public void getOrder(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            if (jsonObject.optInt("code") == 200) {
                Gson gson = new Gson();
                CabinetBean cabinetBean = gson.fromJson(data, CabinetBean.class);
                dataBean = cabinetBean.getData();
                if (dataBean.getHasLock() == 1) {  //有柜子有订单
                    if (dataBean.getId() > 0) {
                        isOrder = true;
                        llyNotLogin.setVisibility(View.GONE);
                        llyLogin.setVisibility(View.VISIBLE);
                        btnLock.setEnabled(true);
                        btnUnlock.setEnabled(true);
                        btnUnlock.setBackgroundColor(getResources().getColor(R.color.color_38B4E9));
                        btnLock.setBackgroundColor(getResources().getColor(R.color.color_FF6339));
                        tvEnd.setText(dataBean.getExpireTime());
                        tvStart.setText(dataBean.getLeaseTime());
                        tvRentTime.setText(dataBean.getNum() + "天");
                        setTime();
                    } else { //有柜子无订单
                        noOrder(0);
                    }
                } else { //无柜子
                    noOrder(1);
                }
            } else { //无柜子
                noOrder(1);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    //设置归还倒计时
    private void setTime() {
        myhandler.sendEmptyMessageDelayed(111, 1000 * 60);
        long currentTimeMillis = System.currentTimeMillis() / 1000;
        long lastTime = DateUtil.formartTime(dataBean.getExpireTime());
        tvTime.setText(DateUtil.getTime((int) (lastTime - currentTimeMillis)));
    }

    //无订单
    private void noOrder(int type) {
        isOrder = false;
        llyLogin.setVisibility(View.GONE);
        if (type == 0) {
            llyNoCabinet.setVisibility(View.GONE);
            llyNotLogin.setVisibility(View.VISIBLE);
            String code = String.format("https://xiao.zgzkys.com/qrcode?DevName=%s", MobileInfoUtil.getIMEI(getContext()));
            ivCode.setImageBitmap(QrCodeUtils.generateOriginalBitmap(code, 102, 102));
        } else {
            llyNotLogin.setVisibility(View.GONE);
            llyNoCabinet.setVisibility(View.VISIBLE);
        }
        tvStart.setText("暂无信息");
        tvEnd.setText("暂无信息");
        tvRentTime.setText("暂无信息");
        btnLock.setEnabled(false);
//        btnUnlock.setEnabled(false);
//        btnUnlock.setBackgroundColor(getResources().getColor(R.color.black_gray1));
        btnLock.setBackgroundColor(getResources().getColor(R.color.black_gray1));
    }

    @Override
    public void unLock(String data) {

    }

    @Override
    public void padNoOrder() {
        noOrder(1);
    }

    @Override
    public void unLockFail() {

    }

    @Override
    public void returnBedFail() {

    }

    @Override
    public void reTurnBed(String data) {

    }


    @Override
    public void findByDid(String data) {

    }

}
