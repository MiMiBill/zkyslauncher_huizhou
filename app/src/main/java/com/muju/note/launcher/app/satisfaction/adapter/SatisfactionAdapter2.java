package com.muju.note.launcher.app.satisfaction.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.util.MultiTypeDelegate;
import com.muju.note.launcher.R;
import com.muju.note.launcher.app.home.ui.HomeFragment;
import com.muju.note.launcher.app.satisfaction.response.SatisfactionDetailResponse;
import com.muju.note.launcher.util.toast.FancyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SatisfactionAdapter2 extends BaseQuickAdapter<SatisfactionDetailResponse.DataBean.ProblemsBean, BaseViewHolder> {
    //(1：单选；2 填空；3 多选)
    final int SATISFACTION_RADIO = 1, SATISFACTION_EDIT = 2, SATISFACTION_CHECK = 3;
    List<AnswerEntity> answerEntities = new ArrayList<>();
    Context context;

    public SatisfactionAdapter2(Context context, @Nullable List<SatisfactionDetailResponse.DataBean.ProblemsBean> data) {
        super(data);
        this.context = context;
        for (int i = 0; i < data.size(); i++) {
            answerEntities.add(new AnswerEntity(data.get(i).getType(), data.get(i).getId(), i + getHeaderLayoutCount()));
        }

        setMultiTypeDelegate(new MultiTypeDelegate<SatisfactionDetailResponse.DataBean.ProblemsBean>() {
            @Override
            protected int getItemType(SatisfactionDetailResponse.DataBean.ProblemsBean entity) {
                //根据你的实体类来判断布局类型
                return entity.getType();
            }
        });
        getMultiTypeDelegate()
                .registerItemType(SATISFACTION_RADIO, R.layout.item_satisfaction_radio2)
                .registerItemType(SATISFACTION_EDIT, R.layout.item_satisfaction_edit)
                .registerItemType(SATISFACTION_CHECK, R.layout.item_satisfaction_check);
    }

    @Override
    protected void convert(final BaseViewHolder helper, SatisfactionDetailResponse.DataBean.ProblemsBean item) {
        // LogUtil.e("SatisfactionAdapter", "answerEntities.size()" + answerEntities.size());
        //if (answerEntities.size() == 0) return;
        int position = helper.getLayoutPosition() - getHeaderLayoutCount();
        final List<SatisfactionDetailResponse.DataBean.ProblemsBean.ProblemItemsBean> problemItems = item.getProblemItems();
        final AnswerEntity answerEntity = answerEntities.get(position);
        //   LogUtil.e("position=" + position, answerEntity.toString());
//        AutoUtils.auto(helper.itemView);
        helper.setText(R.id.title, item.getNumber() + "、" + item.getTitle());
        switch (helper.getItemViewType()) {
            case SATISFACTION_RADIO:
                RecyclerView radioList = helper.getView(R.id.radio_group);
                radioList.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                RadioAdapter radioAdapter = new RadioAdapter(problemItems, position);
                radioList.setAdapter(radioAdapter);
                break;
            case SATISFACTION_EDIT:
                EditText editText = helper.getView(R.id.check_list);
                if (editText.getTag() != null && editText.getTag() instanceof TextWatcher) {
                    editText.removeTextChangedListener((TextWatcher) editText.getTag());
                }
                editText.setText(answerEntity.getAnswer());
                TextWatcher textWatcher = new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        answerEntity.setAnswer(s.toString());
                    }
                };
                editText.addTextChangedListener(textWatcher);
                editText.setTag(textWatcher);
                break;
            case SATISFACTION_CHECK:

                RecyclerView recyclerView = helper.getView(R.id.check_list);
                recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));

                recyclerView.setAdapter(new RecyclerView.Adapter() {
                    @NonNull
                    @Override
                    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        return new RecyclerView.ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_satisfaction_check_layout, null)) {
                            @Override
                            public String toString() {
                                return super.toString();
                            }
                        };
                    }

                    @Override
                    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
                        CheckBox checkBox = holder.itemView.findViewById(R.id.checkbox);
                        checkBox.setText(problemItems.get(position).getAnswer());
                        checkBox.setChecked(answerEntity.getCheckMap().keySet().contains(position));
                        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if (isChecked) {
                                    answerEntity.getCheckMap().put(position, problemItems.get(position).getVal() + "");
                                } else {
                                    answerEntity.getCheckMap().remove(position);
                                }
                            }
                        });

                    }

                    @Override
                    public int getItemCount() {
                        return problemItems.size();
                    }
                });
                break;
        }
    }

    class AnswerEntity {
        int type;
        int problemId;
        int parentPosition;
        int childPosition = -1;
        String answer;

        Map<Integer, String> checkMap = new HashMap<>();

        public Map<Integer, String> getCheckMap() {
            return checkMap;
        }

        public void setCheckMap(Map<Integer, String> checkMap) {
            this.checkMap = checkMap;
        }


        public AnswerEntity(int type, int problemId, int parentPosition) {
            this.type = type;
            this.problemId = problemId;
            this.parentPosition = parentPosition;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getProblemId() {
            return problemId;
        }

        public void setProblemId(int problemId) {
            this.problemId = problemId;
        }

        public int getParentPosition() {
            return parentPosition;
        }

        public void setParentPosition(int parentPosition) {
            this.parentPosition = parentPosition;
        }

        public int getChildPosition() {
            return childPosition;
        }

        public void setChildPosition(int childPosition) {
            this.childPosition = childPosition;
        }

        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }

        @Override
        public String toString() {
            return "type=" + type + ",problemId=" + problemId + ",parentPosition=" + parentPosition + ",childPosition=" + childPosition + ",answer=" + answer;
        }


        public JSONObject toResult() {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("problemId", problemId);
                // jsonObject.put("hospitalizationId", LauncherApplication.getInstance().getPatient().getTabbId());
                if (type == SATISFACTION_CHECK) {
                    Set<String> mapValuesSet = new HashSet<String>(checkMap.values());
                    String[] checkResult = new String[mapValuesSet.size()];
                    mapValuesSet.toArray(checkResult);
                    jsonObject.put("answer", Arrays.toString(checkResult));
                } else {
                    jsonObject.put("answer", answer);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


            return jsonObject;
        }
    }

    class RadioAdapter extends BaseQuickAdapter<SatisfactionDetailResponse.DataBean.ProblemsBean.ProblemItemsBean, BaseViewHolder> {
        int parentPosition;

        public RadioAdapter(@Nullable List<SatisfactionDetailResponse.DataBean.ProblemsBean.ProblemItemsBean> data, int parentPosition) {
            super(R.layout.item_satisfaction_radio_layout, data);
            this.parentPosition = parentPosition;
        }

        @Override
        protected void convert(final BaseViewHolder helper, final SatisfactionDetailResponse.DataBean.ProblemsBean.ProblemItemsBean item) {
            RadioButton radioButton = helper.getView(R.id.radiobutton);
            radioButton.setText(item.getAnswer());
            radioButton.setChecked(answerEntities.get(parentPosition).getChildPosition() == helper.getLayoutPosition());
            radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        answerEntities.get(parentPosition).setChildPosition(helper.getLayoutPosition());
                        answerEntities.get(parentPosition).setAnswer(item.getVal() + "");
                        notifyDataSetChanged();
                    }
                }
            });

        }
    }

    public JSONObject getSatisfaResult() {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < answerEntities.size(); i++) {
            AnswerEntity entity = answerEntities.get(i);
            if (entity.getType() == SATISFACTION_CHECK) {
                if (entity.getCheckMap().size() == 0) {
                    FancyToast.makeText(context, "第" + (i + 1) + "项还未确认结果！", FancyToast.LENGTH_SHORT).show();
                    getRecyclerView().scrollToPosition(i);
                    return null;
                }
            } else {
                if (TextUtils.isEmpty(entity.getAnswer())) {
                    FancyToast.makeText(context, "第" + (i + 1) + "项还未确认结果！", FancyToast.LENGTH_SHORT).show();
                    getRecyclerView().scrollToPosition(i);
                    return null;
                }
            }
            jsonArray.put(entity.toResult());

        }
        try {
            jsonObject.put("answerList", jsonArray);
            //@TODO 临时去掉
//            LauncherApplication.getInstance().getPatient().getTabbId()
//            jsonObject.put("hospitalizationId", LauncherApplication.getInstance().getPatient().getTabbId());
            if(HomeFragment.entity==null){
                jsonObject.put("hospitalizationId", "0");
            }else {
                jsonObject.put("hospitalizationId", HomeFragment.entity.getId());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

}
