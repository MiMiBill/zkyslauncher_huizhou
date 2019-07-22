package com.muju.note.launcher.app.hostipal.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.muju.note.launcher.R;
import com.muju.note.launcher.app.hostipal.adapter.MattersAttentionAdapter;
import com.muju.note.launcher.app.hostipal.bean.MattersAttentionBean;
import com.muju.note.launcher.base.BaseFragment;
import com.muju.note.launcher.base.LauncherApplication;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 注意事项
 */
public class MattersAttentionFragment extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rv_matters_attention)
    RecyclerView rvMattersAttention;

    private List<MattersAttentionBean> mattersAttentionBeanList;
    private MattersAttentionAdapter mattersAttentionAdapter;

    @Override
    public int getLayout() {
        return R.layout.fragment_matters_attention;
    }

    @Override
    public void initData() {

        llBack.setOnClickListener(this);

        mattersAttentionBeanList=new ArrayList<>();
        mattersAttentionAdapter=new MattersAttentionAdapter(R.layout.item_rv_matters_attention,mattersAttentionBeanList);
        rvMattersAttention.setLayoutManager(new LinearLayoutManager(LauncherApplication.getContext(),LinearLayoutManager.VERTICAL,false));
        rvMattersAttention.setAdapter(mattersAttentionAdapter);

        addData();
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void showError(String msg) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_back:
                pop();
                break;
        }
    }

    private void addData(){
        MattersAttentionBean bean=new MattersAttentionBean(1,"手术前、手术中和手术后","医生会给患者使用抗生素。使用静脉抗生素的最好时机是从手术前就开始，手术中可以继续输注（手术中必须有静脉通道）。一般手术后使用3天静脉药，而后改用3~5天口服药，根据伤口大小和污染程度进行调整。");
        mattersAttentionBeanList.add(bean);
        MattersAttentionBean bean1=new MattersAttentionBean(2,"手术后隔天一定要到医院换药","肌腱损伤手术，术后半个月拆线，术后3~4周去石膏。拆线之前，一般换药2~3次，每次换药后，都要将石膏固定好。如果石膏很不服贴或者已经断裂，就需要更换。单根肌腱损伤，石膏外固定时间为三周，多发肌腱损伤，一般满四周才去石膏。");
        mattersAttentionBeanList.add(bean1);
        MattersAttentionBean bean2=new MattersAttentionBean(3,"手术后麻醉恢复","从注射麻醉药到手术后麻醉恢复，最长可达到十几个小时，其标志就是伤口开始疼痛，而后肌肉可以收缩了。如果手术后超过十五小时没有恢复知觉，应该引起注意。");
        mattersAttentionBeanList.add(bean2);
        MattersAttentionBean bean3=new MattersAttentionBean(4,"手部肌腱修复后都会用石膏保护","手部肌腱修复后都会用石膏保护。凡是被石膏固定的部位，绝对不可以活动！没有被石膏包被的部位，鼓励活动。 \n" +
                "屈侧肌腱损伤，手指被石膏固定在屈曲位；背侧肌腱损伤，手指被固定在伸直位，可以使断裂的肌腱在无张力的状态下自行愈合。 \n" +
                "严禁屈伸手指去感觉肌腱是否已经接上！严禁自行拆去石膏！");
        mattersAttentionBeanList.add(bean3);
        mattersAttentionAdapter.notifyDataSetChanged();
    }
}
