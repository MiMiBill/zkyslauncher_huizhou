package com.muju.note.launcher.app.hostipal.ui;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.muju.note.launcher.R;
import com.muju.note.launcher.app.hostipal.adapter.DepartmentAdapter;
import com.muju.note.launcher.app.hostipal.adapter.PathologyAdapter;
import com.muju.note.launcher.app.hostipal.contract.EncyHosContract;
import com.muju.note.launcher.app.hostipal.db.InfoDao;
import com.muju.note.launcher.app.hostipal.db.InfomationDao;
import com.muju.note.launcher.app.hostipal.popupwindow.SearchPopupWindow;
import com.muju.note.launcher.app.hostipal.presenter.EncyPresenter;
import com.muju.note.launcher.base.BaseFragment;
import com.muju.note.launcher.util.SoftKeyboardUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

//医疗百科
public class HospitalEncyFragment extends BaseFragment<EncyPresenter> implements EncyHosContract.View {
    private static final String TAG = "EncyFragment";
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
    private List<InfomationDao> infomationBeans = new ArrayList<>();
    private int pageNum = 0;
    private int itenId = 0;
    private int type = 0;
    private SearchPopupWindow popupWindow;

    @Override
    public int getLayout() {
        return R.layout.fragment_encyclopedias;
    }

    @Override
    public void initData() {
        departmentAdapter = new DepartmentAdapter(infoBeans, getActivity());
        item1.setAdapter(departmentAdapter);

        pathologyAdapter = new PathologyAdapter(infomationBeans, getActivity());
        mItem2.setAdapter(pathologyAdapter);

        mPresenter.queryEncyCloumn();
        mPresenter.queryTopEncyClopedia();

        initListener();
    }


    @OnClick({R.id.lly_patient, R.id.btn_up, R.id.btn_next, R.id.lly_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.lly_patient:
                llyItem.setVisibility(View.GONE);
                break;
            case R.id.btn_up:
                if (pageNum == 0) {
                    showToast("已经是第一页了");
                    return;
                }
                pageNum--;
                type = 1;
                mPresenter.queryEncyClopediapage(itenId, pageNum, type);
                break;
            case R.id.btn_next:
                pageNum++;
                if (infomationBeans.size() < 12) {
                    showToast("已经是最后一页了");
                    return;
                }
                mPresenter.queryEncyClopediapage(itenId, pageNum, type);
                break;
            case R.id.lly_back:
                pop();
                break;
        }
    }

    @Override
    public void getInfoNull() {
        llyFrmlayout.setVisibility(View.GONE);
        llyProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void getInfo(List<InfoDao> infoDaoList) {
        infoBeans.clear();
        infoBeans.addAll(infoDaoList);
        departmentAdapter.setNewData(infoDaoList);
    }

    @Override
    public void getInfoMation(List<InfomationDao> infomationDaoList) {
        llyProgress.setVisibility(View.GONE);
        pathologyAdapter.setNewData(infomationDaoList);
    }

    @Override
    public void getInfoMationPage(List<InfomationDao> list) {
        llyProgress.setVisibility(View.GONE);
        infomationBeans.clear();
        infomationBeans.addAll(list);

        pathologyAdapter.setNewData(list);
        llyItem.setVisibility(View.VISIBLE);
    }


    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        hideSoftInput();
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }

    @Override
    public void setTopInfomation(InfomationDao infomation) {
        llyProgress.setVisibility(View.GONE);
        setUi(infomation);
    }

    @Override
    public void setInfomationById(InfomationDao infomation) {
        llyProgress.setVisibility(View.GONE);
        setUi(infomation);
        popupWindow.dismiss();
    }

    @Override
    public void setInfomationByTitle(InfomationDao infomation) {
        llyProgress.setVisibility(View.GONE);
        setUi(infomation);
    }

    @Override
    public void search(List<InfomationDao> searchList) {
        if (searchList.size() > 0) {
            showPop(searchList);
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
                pageNum = 0;
                type = 0;
                itenId = infoBeans.get(position).getColumnId();
                mPresenter.queryEncyClopediapage(itenId, pageNum, type);
            }
        });

        mItem2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                InfomationDao bean = infomationBeans.get(position);
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
                mPresenter.querySearch(keyWord);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void showPop(final List<InfomationDao> searchList) {
        SoftKeyboardUtil.hideSoftKeyboard(getActivity());
        popupWindow = new SearchPopupWindow(getActivity(), searchList);
        popupWindow.showAsDropDown(edtSearch);
        popupWindow.setOnClickListener(new SearchPopupWindow.OnClickListener() {
            @Override
            public void onClick(int position) {
                int searchId = searchList.get(position).getId();
                mPresenter.queryEncyClopediaById(searchId);
            }
        });
    }

    @Override
    public void initPresenter() {
        mPresenter = new EncyPresenter();
    }

    @Override
    public void showError(String msg) {

    }


    private void setUi(InfomationDao bean) {
        llyFrmlayout.setVisibility(View.VISIBLE);
        if (!"".equals(bean.getTitle())) {
            tvTitle.setText(bean.getTitle());
        } else {
            tvTitle.setVisibility(View.GONE);
        }
        tvAuthor.setText(bean.getAuthor() + "   " + bean.getSource() + "   " + bean.getClickCount
                () + "次点击");
        if (bean.getSummary() != null && (!"".equals(bean.getSummary().trim()))) {
            tvSummary.setText(bean.getSummary());
            tvSummary.setVisibility(View.VISIBLE);
            tvSummaryDe.setVisibility(View.VISIBLE);
        } else {
            tvSummary.setVisibility(View.GONE);
            tvSummaryDe.setVisibility(View.GONE);
        }
        if (bean.getCause() != null && (!"".equals(bean.getCause().trim()))) {
            tvCause.setText(bean.getCause());
            tvCause.setVisibility(View.VISIBLE);
            tvCauseDe.setVisibility(View.VISIBLE);
        } else {
            tvCause.setVisibility(View.GONE);
            tvCauseDe.setVisibility(View.GONE);
        }
        if (bean.getCheck() != null && (!"".equals(bean.getCheck().trim()))) {
            tvCheck.setText(bean.getCheck());
            tvCheck.setVisibility(View.VISIBLE);
            tvCheckDe.setVisibility(View.VISIBLE);
        } else {
            tvCheck.setVisibility(View.GONE);
            tvCheckDe.setVisibility(View.GONE);
        }
        if (bean.getClinicalManifestation() != null && (!"".equals(bean.getClinicalManifestation().trim()))) {
            tvClinicalManifestation.setText(bean.getClinicalManifestation());
            tvClinicalManifestation.setVisibility(View.VISIBLE);
            tvClinicalManifestationDe.setVisibility(View.VISIBLE);
        } else {
            tvClinicalManifestation.setVisibility(View.GONE);
            tvClinicalManifestationDe.setVisibility(View.GONE);
        }
        if (bean.getComplicatingDisease() != null && (!"".equals(bean.getComplicatingDisease().trim()))) {
            tvComplicatingdisease.setText(bean.getComplicatingDisease());
            tvComplicatingdisease.setVisibility(View.VISIBLE);
            tvComplicatingdiseaseDe.setVisibility(View.VISIBLE);
        } else {
            tvComplicatingdisease.setVisibility(View.GONE);
            tvComplicatingdiseaseDe.setVisibility(View.GONE);
        }
        if (bean.getAntidiastole() != null && (!"".equals(bean.getAntidiastole().trim()))) {
            tvAntidiastole.setText(bean.getAntidiastole());
            tvAntidiastole.setVisibility(View.VISIBLE);
            tvAntidiastoleDe.setVisibility(View.VISIBLE);
        } else {
            tvAntidiastole.setVisibility(View.GONE);
            tvAntidiastoleDe.setVisibility(View.GONE);
        }
        if (bean.getDassification() != null && (!"".equals(bean.getDassification().trim()))) {
            tvDassification.setText(bean.getDassification());
            tvDassification.setVisibility(View.VISIBLE);
            tvDassificationDe.setVisibility(View.VISIBLE);
        } else {
            tvDassification.setVisibility(View.GONE);
            tvDassificationDe.setVisibility(View.GONE);
        }
        if (bean.getDiacrsis() != null && (!"".equals(bean.getDiacrsis().trim()))) {
            tvDiacrisis.setText(bean.getDiacrsis());
            tvDiacrisis.setVisibility(View.VISIBLE);
            tvDiacrisisDe.setVisibility(View.VISIBLE);
        } else {
            tvDiacrisis.setVisibility(View.GONE);
            tvDiacrisisDe.setVisibility(View.GONE);
        }
        if (bean.getDietCare() != null && (!"".equals(bean.getDietCare().trim()))) {
            tvDietcare.setText(bean.getDietCare());
            tvDietcare.setVisibility(View.VISIBLE);
            tvDietcareDe.setVisibility(View.VISIBLE);
        } else {
            tvDietcare.setVisibility(View.GONE);
            tvDietcareDe.setVisibility(View.GONE);
        }
        if (bean.getCure() != null && (!"".equals(bean.getCure().trim()))) {
            tvCure.setText(bean.getCure());
            tvCure.setVisibility(View.VISIBLE);
            tvCureDe.setVisibility(View.VISIBLE);
        } else {
            tvCure.setVisibility(View.GONE);
            tvCureDe.setVisibility(View.GONE);
        }
        if (bean.getPrognosis() != null && (!"".equals(bean.getPrognosis().trim()))) {
            tvPrognosis.setText(bean.getPrognosis());
            tvPrognosis.setVisibility(View.VISIBLE);
            tvPrognosisDe.setVisibility(View.VISIBLE);
        } else {
            tvPrognosis.setVisibility(View.GONE);
            tvPrognosisDe.setVisibility(View.GONE);
        }
        if (bean.getProphylaxis() != null && (!"".equals(bean.getProphylaxis().trim()))) {
            tvProphylaxis.setText(bean.getProphylaxis());
            tvProphylaxis.setVisibility(View.VISIBLE);
            tvProphylaxisDe.setVisibility(View.VISIBLE);
        } else {
            tvProphylaxis.setVisibility(View.GONE);
            tvProphylaxisDe.setVisibility(View.GONE);
        }
    }
}
