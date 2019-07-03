package com.muju.note.launcher.app.hostipal.ui;

import android.support.v4.app.Fragment;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.muju.note.launcher.R;
import com.muju.note.launcher.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import q.rorbin.verticaltablayout.VerticalTabLayout;
import q.rorbin.verticaltablayout.adapter.SimpleTabAdapter;
import q.rorbin.verticaltablayout.widget.ITabView;
import q.rorbin.verticaltablayout.widget.TabView;

public class ChemotherapyFragment extends BaseFragment {
    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rel_titlebar)
    RelativeLayout relTitlebar;
    @BindView(R.id.vertical_tab)
    VerticalTabLayout mVerticalTabLayout;
    @BindView(R.id.container)
    FrameLayout container;
    List<String> tabsData = new ArrayList<>();
    List<Fragment> fragments = new ArrayList<>();
    @Override
    public int getLayout() {
        return R.layout.fragment_chemotherapy;
    }

    @Override
    public void initData() {
        tvTitle.setText("化验报告");
        relTitlebar.setBackgroundColor(getResources().getColor(R.color.white));

        tabsData.add("尿液分析");
        tabsData.add("口腔检查");
        tabsData.add("心电图");
        tabsData.add("上腹彩超(肝/胆/脾)");
        tabsData.add("胸部正位DR");
        tabsData.add("血常规");
        tabsData.add("眼科检查");
        for (int i = 0; i < 7; i++){
            fragments.add(new ChemotherFragment());
        }
        initTabs();
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void showError(String msg) {

    }

    /**
     * 初始化tab：结合fragment的方式
     */
    private void initTabs() {
        mVerticalTabLayout.setupWithFragment(getChildFragmentManager(), R.id.container, fragments, new SimpleTabAdapter() {
            @Override
            public int getCount() {
                return tabsData.size();
            }

            @Override
            public ITabView.TabTitle getTitle(int position) {
                return new TabView.TabTitle.Builder()
                        .setTextSize(15)
                        .setTextColor(getResources().getColor(R.color.chemotherapy_title_view_selected), getResources().getColor(R.color.chemotherapy_title_view_unselected))
                        .setContent(tabsData.get(position)).build();
            }

        });
        mVerticalTabLayout.addOnTabSelectedListener(new VerticalTabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabView tab, int position) {
//                currentFragmentIndex = position;
                tab.setBackgroundColor(getResources().getColor(android.R.color.white));
                setTabsUnSelectedBackgroundColor();
            }

            @Override
            public void onTabReselected(TabView tab, int position) {

            }
        });
        mVerticalTabLayout.getTabAt(0).setBackgroundColor(getResources().getColor(android.R.color.white));
    }

    private void setTabsUnSelectedBackgroundColor() {
        for (int i = 0; i < mVerticalTabLayout.getTabCount(); i++) {
            if (mVerticalTabLayout.getSelectedTabPosition() != i) {
                TabView tab = mVerticalTabLayout.getTabAt(i);
                tab.setBackgroundColor(getResources().getColor(R.color.default_bg));
            }
        }
    }



    @OnClick(R.id.ll_back)
    public void onViewClicked() {
        pop();
    }
}
