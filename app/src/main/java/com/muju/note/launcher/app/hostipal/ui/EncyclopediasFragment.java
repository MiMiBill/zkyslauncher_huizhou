package com.muju.note.launcher.app.hostipal.ui;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.muju.note.launcher.R;
import com.muju.note.launcher.app.activeApp.entity.ResourceEntity;
import com.muju.note.launcher.app.hostipal.adapter.DepartmentAdapter;
import com.muju.note.launcher.app.hostipal.adapter.PathologyAdapter;
import com.muju.note.launcher.app.hostipal.bean.GetDownloadBean;
import com.muju.note.launcher.app.hostipal.contract.EncyContract;
import com.muju.note.launcher.app.hostipal.db.InfoDao;
import com.muju.note.launcher.app.hostipal.db.InfomationDao;
import com.muju.note.launcher.app.hostipal.popupwindow.SearchPopupWindow;
import com.muju.note.launcher.app.hostipal.presenter.EncyclopsediasPresenter;
import com.muju.note.launcher.app.hostipal.util.DBManager;
import com.muju.note.launcher.base.BaseFragment;
import com.muju.note.launcher.listener.OnZipSuccessListener;
import com.muju.note.launcher.service.http.ServiceHttp;
import com.muju.note.launcher.util.Constants;
import com.muju.note.launcher.util.SoftKeyboardUtil;
import com.muju.note.launcher.util.log.LogFactory;
import com.muju.note.launcher.util.log.LogUtil;
import com.muju.note.launcher.util.sp.SPUtil;
import com.muju.note.launcher.util.zip.ZipUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

//医疗百科
public class EncyclopediasFragment extends BaseFragment<EncyclopsediasPresenter> implements
        EncyContract.View {
    private static final String TAG = "EncyclopediasFragment";
    public static EncyclopediasFragment encyclopediasFragment = null;
    @BindView(R.id.lly_back)
    LinearLayout llyBack;
    @BindView(R.id.edt_search)
    EditText edtSearch;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_author)
    TextView tvAuthor;
    @BindView(R.id.tv_summary_de)
    TextView tvSummaryDe;
    @BindView(R.id.tv_summary)
    TextView tvSummary;
    @BindView(R.id.tv_cause_de)
    TextView tvCauseDe;
    @BindView(R.id.tv_cause)
    TextView tvCause;
    @BindView(R.id.tv_dassification_de)
    TextView tvDassificationDe;
    @BindView(R.id.tv_dassification)
    TextView tvDassification;
    @BindView(R.id.tv_clinicalManifestation_de)
    TextView tvClinicalManifestationDe;
    @BindView(R.id.tv_clinicalManifestation)
    TextView tvClinicalManifestation;
    @BindView(R.id.tv_check_de)
    TextView tvCheckDe;
    @BindView(R.id.tv_check)
    TextView tvCheck;
    @BindView(R.id.tv_diacrisis_de)
    TextView tvDiacrisisDe;
    @BindView(R.id.tv_diacrisis)
    TextView tvDiacrisis;
    @BindView(R.id.tv_antidiastole_de)
    TextView tvAntidiastoleDe;
    @BindView(R.id.tv_antidiastole)
    TextView tvAntidiastole;
    @BindView(R.id.tv_cure_de)
    TextView tvCureDe;
    @BindView(R.id.tv_cure)
    TextView tvCure;
    @BindView(R.id.tv_prognosis_de)
    TextView tvPrognosisDe;
    @BindView(R.id.tv_prognosis)
    TextView tvPrognosis;
    @BindView(R.id.tv_prophylaxis_de)
    TextView tvProphylaxisDe;
    @BindView(R.id.tv_prophylaxis)
    TextView tvProphylaxis;
    @BindView(R.id.tv_complicatingdisease_de)
    TextView tvComplicatingdiseaseDe;
    @BindView(R.id.tv_complicatingdisease)
    TextView tvComplicatingdisease;
    @BindView(R.id.tv_dietcare_de)
    TextView tvDietcareDe;
    @BindView(R.id.tv_dietcare)
    TextView tvDietcare;
    @BindView(R.id.lly_frmlayout)
    LinearLayout llyFrmlayout;
    @BindView(R.id.item1)
    ListView item1;
    @BindView(R.id.item2)
    ListView mItem2;
    @BindView(R.id.lly_list)
    LinearLayout llyList;
    @BindView(R.id.progress)
    ProgressBar progress;
    @BindView(R.id.lly_progress)
    LinearLayout llyProgress;
    Unbinder unbinder;
    @BindView(R.id.lly_patient)
    LinearLayout llyPatient;
    @BindView(R.id.btn_up)
    Button btnUp;
    @BindView(R.id.btn_next)
    Button btnNext;
    @BindView(R.id.lly_item)
    LinearLayout llyItem;
    private DepartmentAdapter departmentAdapter;
    private PathologyAdapter pathologyAdapter;
    private List<InfoDao> infoBeans = new ArrayList<>();
    private DBManager manager;
    private SQLiteDatabase sqLiteDatabase;
    private List<InfomationDao> infomationBeans = new ArrayList<>();
    private String zipFileString;
    private String outPathString;
    private String outPathName;
    private String dbName;
    private int pageNum=0;
    private int itenId=0;
    private ArrayList<InfomationDao> searchList = new ArrayList<>();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    List<InfoDao> infoBeans = (List<InfoDao>) msg.obj;
                    setData(infoBeans);
                    llyList.setVisibility(View.VISIBLE);
                    llyProgress.setVisibility(View.GONE);
                    llyFrmlayout.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };

    private void setData(List<InfoDao> infoBeans) {
//        if(departmentAdapter ==null){
        departmentAdapter = new DepartmentAdapter(infoBeans, getActivity());
//        }
//        if(pathologyAdapter ==null){
        pathologyAdapter = new PathologyAdapter(infomationBeans, getActivity());
//        }
        item1.setAdapter(departmentAdapter);
        mItem2.setAdapter(pathologyAdapter);
    }

    public static EncyclopediasFragment getInstance() {
        if (encyclopediasFragment == null) {
            encyclopediasFragment = new EncyclopediasFragment();
        }
        return encyclopediasFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogFactory.l().i("onCreate");
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_encyclopedias;
    }

    @Override
    public void initData() {
        outPathName = "medical.db";
        outPathString = Environment.getExternalStorageDirectory().getAbsolutePath() + File
                .separator + "zkysdb/" + outPathName;
        File file = new File(outPathString);
        if (!file.exists()) {
            try {
                llyList.setVisibility(View.GONE);
                llyFrmlayout.setVisibility(View.GONE);
                llyProgress.setVisibility(View.VISIBLE);
                mPresenter.getDownLoadUrl();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            new LoadDataTask().run();
        }
        initListener();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ResourceEntity entity) {
        if (entity.getType() == ResourceEntity.ENCY_ZIP) {
            zipFileString = Environment.getExternalStorageDirectory().getAbsolutePath() + File
                    .separator + "zkys/resource/" + dbName;
            ZipUtils.UnZipFolder(zipFileString, outPathString, new OnZipSuccessListener() {
                @Override
                public void OnZipSuccess() {
                    LogUtil.i("setOnZipSuccessListener");
                    new LoadDataTask().run();
                }
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }



    @OnClick({R.id.lly_patient, R.id.btn_up, R.id.btn_next,R.id.lly_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.lly_patient:
                llyItem.setVisibility(View.GONE);
                break;
            case R.id.btn_up:
                if(pageNum==0){
                    showToast("已经是第一页了");
                    return;
                }
                pageNum--;
                infomationBeans.clear();
                infomationBeans = manager.query(sqLiteDatabase,itenId,pageNum);
                pathologyAdapter.setNewData(infomationBeans);
                pathologyAdapter.notifyDataSetChanged();
                break;
            case R.id.btn_next:
                pageNum++;
                infomationBeans.clear();
                infomationBeans = manager.query(sqLiteDatabase,itenId,pageNum);
                if(infomationBeans.size()<11){
                    showToast("已经是最后一页了");
                    return;
                }
                pathologyAdapter.setNewData(infomationBeans);
                pathologyAdapter.notifyDataSetChanged();
                break;
            case R.id.lly_back:
                pop();
                break;
        }
    }

    private class LoadDataTask implements Runnable {
        @Override
        public void run() {
            manager = new DBManager(getActivity());
            sqLiteDatabase = manager.DBManager();

            String[] columns = new String[]{"name", "id"};
            infoBeans = manager.queryNameList(sqLiteDatabase, columns, null, null);
            LogUtil.i("infoBeans==" + infoBeans.size());

            InfomationDao infomationBean = manager.queryTop(sqLiteDatabase);
            setUi(infomationBean);

            Message mess = new Message();
            mess.obj = infoBeans;
            mess.what = 1;
            handler.sendMessage(mess);
        }
    }

    private void initListener() {
        item1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < infoBeans.size(); i++) {
                    InfoDao infoBean = infoBeans.get(i);
                    if (position == i) {
                        infoBean.setCheck(true);
                    } else {
                        infoBean.setCheck(false);
                    }
                }
                pageNum=0;
                itenId=infoBeans.get(position).getId();
                departmentAdapter.setNewData(infoBeans);
                infomationBeans.clear();
                infomationBeans = manager.query(sqLiteDatabase,itenId);
                pathologyAdapter.setNewData(infomationBeans);
                pathologyAdapter.notifyDataSetChanged();
                llyItem.setVisibility(View.VISIBLE);
            }
        });

        mItem2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                InfomationDao bean = infomationBeans.get(position);
                pathologyAdapter.notifyDataSetChanged();
                //通过监听器
                llyItem.setVisibility(View.GONE);
                setUi(bean);
            }
        });

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String keyWord = s.toString();
                searchList.clear();
                searchList = manager.querySearch(sqLiteDatabase, keyWord);
                LogUtil.i("搜索结果: " + searchList.size());
                if (searchList.size() > 0) {
                    showPop(searchList);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void showPop(final ArrayList<InfomationDao> searchList) {
        SoftKeyboardUtil.hideSoftKeyboard(getActivity());
        final SearchPopupWindow popupWindow = new SearchPopupWindow(getActivity(), searchList);
        popupWindow.showAsDropDown(edtSearch);
        popupWindow.setOnClickListener(new SearchPopupWindow.OnClickListener() {
            @Override
            public void onClick(int position) {
                String title = searchList.get(position).getTitle();
                InfomationDao bean = manager.queryTitle(sqLiteDatabase, title);
                setUi(bean);
                popupWindow.dismiss();
            }
        });
    }

    @Override
    public void initPresenter() {
        mPresenter = new EncyclopsediasPresenter();
    }

    @Override
    public void showError(String msg) {

    }


    @Override
    public void getDownLoadUrl(GetDownloadBean getDownloadBean) {
        String path = getDownloadBean.getPath();
        dbName = path.substring(path.lastIndexOf("/") + 1);
        if (!dbName.equals(SPUtil.getString(Constants.ENCY_ZIP))) {
            SPUtil.putString(Constants.ENCY_ZIP, dbName);
        }
        ServiceHttp.getInstance().downloadFile(new ResourceEntity(ResourceEntity
                .ENCY_ZIP, path, dbName));
    }

    private void setUi(InfomationDao bean) {
        tvTitle.setText(bean.getTitle());
        tvAuthor.setText(bean.getAuthor() + "   " + bean.getSource() + "   " + bean.getClickCount
                () + "次点击");
        if (!"".equals(bean.getSummary().trim())) {
            tvSummary.setText(bean.getSummary());
            tvSummary.setVisibility(View.VISIBLE);
            tvSummaryDe.setVisibility(View.VISIBLE);
        }
        if (!"".equals(bean.getCause().trim())) {
            tvCause.setText(bean.getCause());
            tvCause.setVisibility(View.VISIBLE);
            tvCauseDe.setVisibility(View.VISIBLE);
        }
        if (!"".equals(bean.getCheck().trim())) {
            tvCheck.setText(bean.getCheck());
            tvCheck.setVisibility(View.VISIBLE);
            tvCheckDe.setVisibility(View.VISIBLE);
        }
        if (!"".equals(bean.getClinicalManifestation().trim())) {
            tvClinicalManifestation.setText(bean.getClinicalManifestation());
            tvClinicalManifestation.setVisibility(View.VISIBLE);
            tvClinicalManifestationDe.setVisibility(View.VISIBLE);
        }
        if (!"".equals(bean.getComplicatingDisease().trim())) {
            tvComplicatingdisease.setText(bean.getComplicatingDisease());
            tvComplicatingdisease.setVisibility(View.VISIBLE);
            tvComplicatingdiseaseDe.setVisibility(View.VISIBLE);
        }
        if (!"".equals(bean.getAntidiastole().trim())) {
            tvAntidiastole.setText(bean.getAntidiastole());
            tvAntidiastole.setVisibility(View.VISIBLE);
            tvAntidiastoleDe.setVisibility(View.VISIBLE);
        }
        if (!"".equals(bean.getDassification().trim())) {
            tvDassification.setText(bean.getDassification());
            tvDassification.setVisibility(View.VISIBLE);
            tvDassificationDe.setVisibility(View.VISIBLE);
        }
        if (!"".equals(bean.getDiacrsis().trim())) {
            tvDiacrisis.setText(bean.getDiacrsis());
            tvDiacrisis.setVisibility(View.VISIBLE);
            tvDiacrisisDe.setVisibility(View.VISIBLE);
        }
        if (!"".equals(bean.getDietCare().trim())) {
            tvDietcare.setText(bean.getDietCare());
            tvDietcare.setVisibility(View.VISIBLE);
            tvDietcareDe.setVisibility(View.VISIBLE);
        }
        if (!"".equals(bean.getCure().trim())) {
            tvCure.setText(bean.getCure());
            tvCure.setVisibility(View.VISIBLE);
            tvCureDe.setVisibility(View.VISIBLE);
        }
        if (!"".equals(bean.getPrognosis().trim())) {
            tvPrognosis.setText(bean.getPrognosis());
            tvPrognosis.setVisibility(View.VISIBLE);
            tvPrognosisDe.setVisibility(View.VISIBLE);
        }
        if (!"".equals(bean.getProphylaxis().trim())) {
            tvProphylaxis.setText(bean.getProphylaxis());
            tvProphylaxis.setVisibility(View.VISIBLE);
            tvProphylaxisDe.setVisibility(View.VISIBLE);
        }
    }
}
