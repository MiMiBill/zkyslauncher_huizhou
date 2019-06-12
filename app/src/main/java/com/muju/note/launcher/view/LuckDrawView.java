package com.muju.note.launcher.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.muju.note.launcher.R;
import com.muju.note.launcher.app.userinfo.bean.LuckDrawBean;
import com.muju.note.launcher.app.video.dialog.LoginDialog;
import com.muju.note.launcher.util.user.UserUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class LuckDrawView extends FrameLayout {

    private Context context;
    private List<LuckDrawBean> list=new ArrayList<>();

    private int luckIndex=0; // 上次中奖的下标
    private int luckingIndex=0; // 抽奖中的index
    private TextView tvStart;
    private RelativeLayout rlStart;

    private long startTime;
    private long endTime;
    private LoginDialog loginDialog;

    public LuckDrawView(@NonNull Context context) {
        super(context);
        this.context=context;
        init(context);
    }

    public LuckDrawView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        init(context);
    }

    public LuckDrawView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
        init(context);
    }


    private void init(final Context context){
        list=new ArrayList<>();
        View view=LayoutInflater.from(context).inflate(R.layout.view_luckdraw,null);
        initData("杯子", (ImageView) view.findViewById(R.id.iv_one));
        initData("谢谢参与", (ImageView) view.findViewById(R.id.iv_two));
        initData("项链", (ImageView) view.findViewById(R.id.iv_three));
        initData("戒指", (ImageView) view.findViewById(R.id.iv_four));
        initData("谢谢参与", (ImageView) view.findViewById(R.id.iv_five));
        initData("鱼", (ImageView) view.findViewById(R.id.iv_six));
        initData("谢谢参与", (ImageView) view.findViewById(R.id.iv_seven));
        initData("鸭子", (ImageView) view.findViewById(R.id.iv_eight));
        initData("平板", (ImageView) view.findViewById(R.id.iv_nine));
        initData("谢谢参与", (ImageView) view.findViewById(R.id.iv_ten));
        initData("温度计", (ImageView) view.findViewById(R.id.iv_eleven));
        initData("谢谢参与", (ImageView) view.findViewById(R.id.iv_twelve));

        tvStart=view.findViewById(R.id.tv_start);
        rlStart=view.findViewById(R.id.rl_start);
        rlStart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (UserUtil.getUserBean() != null){
                    if(UserUtil.getUserBean().getIntegral()>=10){
                        start();
                        if(startListener!=null){
                            startListener.start();
                        }
                    }else {
                        Toast.makeText(context,"积分不够,无法抽奖,快去做任务转去积分吧",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    showLoginDialog(context);
                }
            }
        });

        addView(view);
    }

    private void showLoginDialog(Context context) {
        loginDialog = new LoginDialog(context, R.style.DialogFullscreen, new LoginDialog
                .OnLoginListener() {
            @Override
            public void onSuccess() {
                loginDialog.dismiss();
            }

            @Override
            public void onFail() {

            }
        });
        loginDialog.show();
    }


    private void initData(String tag, ImageView imageView){
        LuckDrawBean bean=new LuckDrawBean();
        bean.setTag(tag);
        bean.setImg(imageView);
        list.add(bean);
    }

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0x01:
                    if(luckDrawListener!=null){
                        luckDrawListener.start();
                    }
                    lucking();
                    break;

                case 0x02:
                    luckStoping();
                    break;

                case 0x03:
                    handler.removeMessages(0x02);
                    if(luckDrawListener!=null){
                        luckDrawListener.stop();
                    }
                    tvStart.setText("开始");
                    rlStart.setEnabled(true);
                    break;
            }
        }
    };

    /**
     *  抽奖中
     */
    private void lucking(){
        for (int i=0;i<list.size();i++){
            if(i==luckingIndex){
                list.get(i).getImg().setVisibility(VISIBLE);
            }else {
                list.get(i).getImg().setVisibility(GONE);
            }
        }
        luckingIndex++;
        if(luckingIndex>=list.size()){
            luckingIndex=0;
        }
        startTime=startTime+200;
        if(startTime>=900){
            startTime=900;
        }

        handler.sendEmptyMessageDelayed(0x01,1000-startTime);
    }


    /**
     *  开始抽奖
     */
    public void start(){
        luckingIndex=luckIndex;
        tvStart.setText("抽奖中");
        rlStart.setEnabled(false);
        startTime=0;
        handler.sendEmptyMessageDelayed(0x01,100);
    }

    /**
     *  停止抽奖
     * @param index
     */
    public void stop(int index){
        luckIndex=index;
        if(Math.abs(luckIndex-luckingIndex)<=5){
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    timeStop();
                }
            },1700);
        }else {
            timeStop();
        }

    }

    private void timeStop(){
        endTime=900;
        handler.removeMessages(0x01);
        handler.sendEmptyMessageDelayed(0x02,100);
    }


    /**
     *  停止抽奖中
     */
    private void luckStoping(){

        if(luckingIndex==luckIndex){
            stopUi();
            handler.sendEmptyMessageDelayed(0x03,1000);
            return;
        }
        stopUi();
        handler.sendEmptyMessageDelayed(0x02,1000-endTime);
    }

    private void stopUi(){
        for (int i=0;i<list.size();i++){
            if(i==luckingIndex){
                list.get(i).getImg().setVisibility(VISIBLE);
            }else {
                list.get(i).getImg().setVisibility(GONE);
            }
        }
        luckingIndex++;
        if(luckingIndex>=list.size()){
            luckingIndex=0;
        }
        endTime=endTime-100;
        if(endTime<=0){
            endTime=0;
        }
    }


    public interface OnLuckDrawListener{
        public void stop();
        public void start();
    }

    private OnLuckDrawListener luckDrawListener;

    /**
     *  回调
     * @param luckDrawListener
     */
    public void setOnLuckDrawListener(OnLuckDrawListener luckDrawListener){
        this.luckDrawListener=luckDrawListener;
    }


    private OnStartListener startListener;

    /**
     *  回调
     * @param
     */
    public void setOnLuckStartListener(OnStartListener startListener){
        this.startListener=startListener;
    }

    public interface OnStartListener{
        public void start();
    }
}
