package com.muju.note.launcher.app.hostipal.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.github.barteksc.pdfviewer.util.FitPolicy;
import com.muju.note.launcher.R;
import com.muju.note.launcher.base.BaseFragment;
import com.muju.note.launcher.base.LauncherApplication;
import com.muju.note.launcher.util.sdcard.SdcardConfig;

import java.io.File;

import butterknife.BindView;

/**
 *  医院宣教PDF播放
 */
public class HospitalMissionPdfFragment extends BaseFragment implements OnPageChangeListener, OnLoadCompleteListener,
        OnPageErrorListener {

    @BindView(R.id.pdf_pdfview)
    PDFView pdfPdfview;
    @BindView(R.id.ll_back)
    LinearLayout llBack;

    public static final String MISSION_PDF_PATH="mission_pdf_path";

    public static HospitalMissionPdfFragment newInstance(String path) {
        Bundle args = new Bundle();
        args.putString(MISSION_PDF_PATH, path);
        HospitalMissionPdfFragment fragment = new HospitalMissionPdfFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_his_mission_pdf;
    }

    @Override
    public void initData() {
        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop();
            }
        });

        File file=new File(SdcardConfig.RESOURCE_FOLDER,getArguments().getString(MISSION_PDF_PATH).hashCode()+".pdf");
        if(!file.exists()){
            showToast("文件路径不存在，请稍后重试");
            return;
        }
        pdfPdfview.fromFile(file)
                .defaultPage(0)
                .onPageChange(this)
                .enableAnnotationRendering(true)
                .onLoad(this)
                .scrollHandle(new DefaultScrollHandle(LauncherApplication.getContext()))
                .spacing(10) // in dp
                .onPageError(this)
                .pageFitPolicy(FitPolicy.BOTH)
                .load();
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void showError(String msg) {

    }

    @Override
    public void loadComplete(int nbPages) {

    }

    @Override
    public void onPageChanged(int page, int pageCount) {

    }

    @Override
    public void onPageError(int page, Throwable t) {

    }
}
