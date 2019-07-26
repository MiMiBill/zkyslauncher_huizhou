package com.muju.note.launcher.app.video.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.muju.note.launcher.R;
import com.muju.note.launcher.app.home.db.AdvertsCodeDao;
import com.muju.note.launcher.app.video.adapter.VideoPriceAdapter;
import com.muju.note.launcher.app.video.bean.PayBean;
import com.muju.note.launcher.app.video.bean.PriceBean;
import com.muju.note.launcher.base.LauncherApplication;
import com.muju.note.launcher.litepal.LitePalDb;
import com.muju.note.launcher.url.UrlUtil;
import com.muju.note.launcher.util.DateUtil;
import com.muju.note.launcher.util.app.MobileInfoUtil;
import com.muju.note.launcher.util.qr.QrCodeUtils;

import org.json.JSONObject;
import org.litepal.LitePal;
import org.litepal.crud.callback.FindMultiCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
//支付页面
public class NewVideoPayDialog extends Dialog {
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.btn_pay)
    Button btnPay;
    @BindView(R.id.lly_pay_rent)
    LinearLayout llyPayRent;
    @BindView(R.id.iv_code)
    ImageView ivCode;
    @BindView(R.id.lly_change)
    LinearLayout llyChange;
    @BindView(R.id.lly_task_code)
    LinearLayout llyTaskCode;
    @BindView(R.id.tv_get_code)
    TextView tvGetCode;
    @BindView(R.id.lly_task_code_user)
    LinearLayout llyTaskCodeUser;
    @BindView(R.id.btn_code)
    Button btnCode;
    @BindView(R.id.lly_pay_task)
    LinearLayout llyPayTask;
    @BindView(R.id.iv_pay_code)
    ImageView ivPayCode;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.lly_pay_wechat)
    LinearLayout llyPayWechat;
    @BindView(R.id.lly_pay)
    LinearLayout llyPay;
    @BindView(R.id.iv_type)
    ImageView ivType;
    @BindView(R.id.lly_cabinet)
    LinearLayout llyCabinet;
    @BindView(R.id.lly_pay_xcx)
    LinearLayout llyPayXcx;
    @BindView(R.id.lly_task)
    LinearLayout llyTask;
    @BindView(R.id.iv_close)
    ImageView ivClose;
    @BindView(R.id.iv_xcx_code)
    ImageView ivXcxCode;
    @BindView(R.id.tv_price_pay)
    TextView tvPricePay;
    @BindView(R.id.rel_dialog)
    RelativeLayout relDialog;
    private int type = 0;
    private Context context;
    private int selectIndexId=-1;
    private int selectPosition=0;
    private double payPrice=0.0;
    private View.OnClickListener listener;
    private List<PriceBean.DataBean> priceList=new ArrayList<>();
    private VideoPriceAdapter videoPriceAdapter;

    public NewVideoPayDialog(Context context, int themeResId, int type,List<PriceBean.DataBean> priceList,View.OnClickListener listener) {
        super(context, themeResId);
        this.type = type;
        this.context = context;
        this.priceList=priceList;
        this.listener=listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_pay_video);
        ButterKnife.bind(this);
        if(priceList.size()>0){
            initRecyclerview();
            initPrice(0);
        }else {
            payByXcx();
        }


        ivClose.setOnClickListener(listener);
        btnCode.setOnClickListener(listener);
    }


    private void initPrice(int position) {
        llyPayXcx.setVisibility(View.GONE);
        llyPayTask.setVisibility(View.GONE);
        llyPayWechat.setVisibility(View.GONE);
        llyPayRent.setVisibility(View.VISIBLE);
        if(type==0){
            llyTask.setVisibility(View.VISIBLE);
        }
        llyCabinet.setVisibility(View.VISIBLE);
        PriceBean.DataBean dataBean = priceList.get(position);
        selectIndexId=dataBean.getId();
        payPrice=dataBean.getPrice();
        long endTime=0;
        long timeMillis = System.currentTimeMillis()/1000;
        if(dataBean.getUnit()==1){
            endTime=timeMillis+dataBean.getAmount()*60*60;
        }else {
            endTime=timeMillis+dataBean.getAmount()*60*60*24;
        }
        tvTime.setText("购买后到期的日期为"+DateUtil.formartTimeToDate(endTime));

        String price=dataBean.getPrice()+"";
        setPrice(price);

    }


    //设置价格
    private void setPrice(String price) {
        String total="应付金额: ￥"+ price+"元";
        SpannableStringBuilder sb = new SpannableStringBuilder(total);
        sb.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.color_585858)), 0, 5,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sb.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.color_FF3000)), 5,
                total.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sb.setSpan(new RelativeSizeSpan(1.4f), 5, total.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvPricePay.setText(sb);
    }

    private void initRecyclerview() {
        videoPriceAdapter = new VideoPriceAdapter(context,priceList);
        priceList.get(0).setCheck(true);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(LauncherApplication.getContext(), 3);
        recyclerview.setLayoutManager(gridLayoutManager);
        recyclerview.setAdapter(videoPriceAdapter);
        videoPriceAdapter.setOnItemClickListener(new VideoPriceAdapter.OnClickListener() {
            @Override
            public void onItemClick(int position) {
                selectPosition=position;
                for (int i = 0; i < priceList.size(); i++) {
                    if(i==position){
                        priceList.get(i).setCheck(true);
                    }else {
                        priceList.get(i).setCheck(false);
                    }
                }

                initPrice(position);
                videoPriceAdapter.notifyPrice(priceList);
            }
        });
    }



    @OnClick({R.id.btn_pay,R.id.lly_cabinet,R.id.lly_task,R.id.lly_change})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_pay:
                getOrderCreate();
                break;
            case R.id.lly_cabinet:
                payByXcx();
                break;
            case R.id.lly_task:
                getWxType();
                break;
            case R.id.lly_change:
                changePayType();
                break;
        }
    }


    //获取公众号type
    private void getWxType() {
        OkGo.<String>get(UrlUtil.getWxType())
                .tag(UrlUtil.getWxType())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body());
                            if (jsonObject.optInt("code") == 200) {
                                if(jsonObject.optInt("data")==1){
                                    doTaskVideo(1);
                                }else {
                                    doTaskVideo(2);
                                }
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void changePayType() {
        initPrice(selectPosition);
    }


    //小程序支付
    private void payByXcx() {
        llyPayWechat.setVisibility(View.GONE);
        llyPayRent.setVisibility(View.GONE);
        llyCabinet.setVisibility(View.GONE);
        llyTask.setVisibility(View.GONE);
        ivType.setImageResource(R.mipmap.pay_iv_pay);
        llyPayTask.setVisibility(View.GONE);
        llyPayXcx.setVisibility(View.VISIBLE);
        String code = String.format("https://xiao.zgzkys.com/qrcode?DevName=%s",
                MobileInfoUtil.getIMEI(getContext()));
        ivXcxCode.setImageBitmap(QrCodeUtils.generateOriginalBitmap(code, 234, 234));
    }


    //做任务
    private void doTaskVideo(final int type) {
        llyPayWechat.setVisibility(View.GONE);
        llyPayRent.setVisibility(View.GONE);
        llyCabinet.setVisibility(View.GONE);
        llyTask.setVisibility(View.GONE);
        llyPayXcx.setVisibility(View.GONE);
        ivType.setImageResource(R.mipmap.pay_iv_task);
        llyPayTask.setVisibility(View.VISIBLE);
        if(type==1){
            llyTaskCode.setVisibility(View.VISIBLE);
            llyTaskCodeUser.setVisibility(View.GONE);
        }else {
            llyTaskCode.setVisibility(View.GONE);
            llyTaskCodeUser.setVisibility(View.VISIBLE);
        }
        LitePalDb.setZkysDb();
        LitePal.where("taskType =? and wxType =?","1",type+"").limit(1).findAsync(AdvertsCodeDao.class).listen(new FindMultiCallback<AdvertsCodeDao>() {
            @Override
            public void onFinish(List<AdvertsCodeDao> list) {
                if(list!=null && list.size()>0){
                    AdvertsCodeDao codeDao = list.get(0);
                    if (codeDao != null) {
                        String url = codeDao.getTaskUrl();
                        if(type==1){
                            ivCode.setImageBitmap(QrCodeUtils.generateOriginalBitmap(url, 234, 234));
                        }else {
                            Glide.with(context).load(url).into(ivCode);
                            tvGetCode.setText("关注后回复"+codeDao.getPubCode()+"获取验证码");
                        }

                    }
                }
            }
        });

    }


    //创建订单
    private void getOrderCreate() {
        OkGo.<String>post(UrlUtil.orderCreate())
                .tag(UrlUtil.orderCreate())
                .params("comboId",selectIndexId)
                .params("imei",MobileInfoUtil.getIMEI(LauncherApplication.getContext()))
                .params("payType",3)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body());
                            if (jsonObject.optInt("code") == 200) {
                                Gson gson=new Gson();
                                llyPayWechat.setVisibility(View.VISIBLE);
                                llyPayRent.setVisibility(View.GONE);
                                llyCabinet.setVisibility(View.GONE);
                                llyTask.setVisibility(View.GONE);
                                ivType.setImageResource(R.mipmap.pay_iv_pay);
                                llyPayTask.setVisibility(View.GONE);
                                llyPayXcx.setVisibility(View.GONE);
                                tvPrice.setText("¥"+payPrice+"元");
                                PayBean payBean = gson.fromJson(response.body(), PayBean.class);
                                if(payBean!=null && payBean.getData()!=null && payBean.getData().getQrCode()!=null){
                                    if(!payBean.getData().getQrCode().equals(""))
                                    setPayCode(payBean.getData().getQrCode());
                                }
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
    }



    public void setPayCode(String code){
        ivPayCode.setImageBitmap(QrCodeUtils.generateOriginalBitmap(code, 234, 234));
    }

}
