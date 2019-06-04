package com.muju.note.launcher.app.satisfaction;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

/**
 * Created by Administrator on 2018/9/10.
 */

public class SatisfactionDetailResponse {


    /**
     * code : 200
     * data : {"description":"诚邀为此诊疗点打评，更希望您提出宝贵的建议，调查结果将用于医院服务改进依据，我们将致力给您提供更好的医疗服务，感谢您的参与。","id":1,"pageNum":1,"pageSize":20,"problems":[{"id":1,"number":1,"pageNum":1,"pageSize":20,"problemItems":[{"answer":"很满意","id":1,"pageNum":1,"pageSize":20,"problemId":1,"sort":1,"val":5},{"answer":"满意","id":2,"pageNum":1,"pageSize":20,"problemId":1,"sort":2,"val":4},{"answer":"一般","id":3,"pageNum":1,"pageSize":20,"problemId":1,"sort":3,"val":3},{"answer":"不满意","id":4,"pageNum":1,"pageSize":20,"problemId":1,"sort":4,"val":0}],"surveyId":1,"title":"您对医师、护士技术的满意度","type":1},{"id":2,"number":2,"pageNum":1,"pageSize":20,"problemItems":[{"answer":"很满意","id":5,"pageNum":1,"pageSize":20,"problemId":2,"sort":1,"val":5},{"answer":"满意","id":6,"pageNum":1,"pageSize":20,"problemId":2,"sort":2,"val":4},{"answer":"一般","id":7,"pageNum":1,"pageSize":20,"problemId":2,"sort":3,"val":3},{"answer":"不满意","id":8,"pageNum":1,"pageSize":20,"problemId":2,"sort":4,"val":0}],"surveyId":1,"title":"您对医院卫生的满意度","type":1},{"id":3,"number":3,"pageNum":1,"pageSize":20,"problemItems":[{"answer":"很满意","id":9,"pageNum":1,"pageSize":20,"problemId":3,"sort":1,"val":5},{"answer":"满意","id":10,"pageNum":1,"pageSize":20,"problemId":3,"sort":2,"val":4},{"answer":"一般","id":11,"pageNum":1,"pageSize":20,"problemId":3,"sort":3,"val":3},{"answer":"不满意","id":12,"pageNum":1,"pageSize":20,"problemId":3,"sort":4,"val":0}],"surveyId":1,"title":"您对护士向您介绍的目前饮食注意问题的满意度","type":1},{"id":4,"number":4,"pageNum":1,"pageSize":20,"problemItems":[{"answer":"很满意","id":13,"pageNum":1,"pageSize":20,"problemId":4,"sort":1,"val":5},{"answer":"满意","id":14,"pageNum":1,"pageSize":20,"problemId":4,"sort":2,"val":4},{"answer":"一般","id":15,"pageNum":1,"pageSize":20,"problemId":4,"sort":3,"val":3},{"answer":"不满意","id":16,"pageNum":1,"pageSize":20,"problemId":4,"sort":4,"val":0}],"surveyId":1,"title":"护士与您交流您的疾病的健康指导满意度","type":1},{"id":5,"number":5,"pageNum":1,"pageSize":20,"problemItems":[{"answer":"很满意","id":17,"pageNum":1,"pageSize":20,"problemId":5,"sort":1,"val":5},{"answer":"满意","id":18,"pageNum":1,"pageSize":20,"problemId":5,"sort":2,"val":4},{"answer":"一般","id":19,"pageNum":1,"pageSize":20,"problemId":5,"sort":3,"val":3},{"answer":"不满意","id":20,"pageNum":1,"pageSize":20,"problemId":5,"sort":4,"val":0}],"surveyId":1,"title":"您对医务人员能否及时解答您提出的病情治疗等问题的满意度","type":1},{"id":6,"number":6,"pageNum":1,"pageSize":20,"problemItems":[{"answer":"很满意","id":21,"pageNum":1,"pageSize":20,"problemId":6,"sort":1,"val":5},{"answer":"满意","id":22,"pageNum":1,"pageSize":20,"problemId":6,"sort":2,"val":4},{"answer":"一般","id":23,"pageNum":1,"pageSize":20,"problemId":6,"sort":3,"val":3},{"answer":"不满意","id":24,"pageNum":1,"pageSize":20,"problemId":6,"sort":4,"val":0}],"surveyId":1,"title":"您对门诊挂号、缴费、入院以及缴费这些手续的满意度","type":1},{"id":7,"number":7,"pageNum":1,"pageSize":20,"problemItems":[{"answer":"王小波","id":25,"pageNum":1,"pageSize":20,"problemId":7,"sort":1,"val":0},{"answer":"周丹","id":26,"pageNum":1,"pageSize":20,"problemId":7,"sort":2,"val":0},{"answer":"小红","id":27,"pageNum":1,"pageSize":20,"problemId":7,"sort":3,"val":0},{"answer":"小南","id":28,"pageNum":1,"pageSize":20,"problemId":7,"sort":4,"val":0}],"surveyId":1,"title":"您最满意的护士","type":1},{"id":8,"number":8,"pageNum":1,"pageSize":20,"problemItems":[{"answer":"100","id":29,"pageNum":1,"pageSize":20,"problemId":8,"sort":1,"val":100},{"answer":"85","id":30,"pageNum":1,"pageSize":20,"problemId":8,"sort":2,"val":85},{"answer":"70","id":31,"pageNum":1,"pageSize":20,"problemId":8,"sort":3,"val":70},{"answer":"60","id":32,"pageNum":1,"pageSize":20,"problemId":8,"sort":4,"val":60}],"surveyId":1,"title":"您对护士打分","type":1},{"id":9,"number":9,"pageNum":1,"pageSize":20,"problemItems":[],"surveyId":1,"title":"您对我们医院的建议","type":2}],"teamId":57,"title":"深圳市人民医院满意度调查"}
     * msg : 操作成功！
     */

    private int code;
    private DataBean data;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class DataBean {
        /**
         * description : 诚邀为此诊疗点打评，更希望您提出宝贵的建议，调查结果将用于医院服务改进依据，我们将致力给您提供更好的医疗服务，感谢您的参与。
         * id : 1
         * pageNum : 1
         * pageSize : 20
         * problems : [{"id":1,"number":1,"pageNum":1,"pageSize":20,"problemItems":[{"answer":"很满意","id":1,"pageNum":1,"pageSize":20,"problemId":1,"sort":1,"val":5},{"answer":"满意","id":2,"pageNum":1,"pageSize":20,"problemId":1,"sort":2,"val":4},{"answer":"一般","id":3,"pageNum":1,"pageSize":20,"problemId":1,"sort":3,"val":3},{"answer":"不满意","id":4,"pageNum":1,"pageSize":20,"problemId":1,"sort":4,"val":0}],"surveyId":1,"title":"您对医师、护士技术的满意度","type":1},{"id":2,"number":2,"pageNum":1,"pageSize":20,"problemItems":[{"answer":"很满意","id":5,"pageNum":1,"pageSize":20,"problemId":2,"sort":1,"val":5},{"answer":"满意","id":6,"pageNum":1,"pageSize":20,"problemId":2,"sort":2,"val":4},{"answer":"一般","id":7,"pageNum":1,"pageSize":20,"problemId":2,"sort":3,"val":3},{"answer":"不满意","id":8,"pageNum":1,"pageSize":20,"problemId":2,"sort":4,"val":0}],"surveyId":1,"title":"您对医院卫生的满意度","type":1},{"id":3,"number":3,"pageNum":1,"pageSize":20,"problemItems":[{"answer":"很满意","id":9,"pageNum":1,"pageSize":20,"problemId":3,"sort":1,"val":5},{"answer":"满意","id":10,"pageNum":1,"pageSize":20,"problemId":3,"sort":2,"val":4},{"answer":"一般","id":11,"pageNum":1,"pageSize":20,"problemId":3,"sort":3,"val":3},{"answer":"不满意","id":12,"pageNum":1,"pageSize":20,"problemId":3,"sort":4,"val":0}],"surveyId":1,"title":"您对护士向您介绍的目前饮食注意问题的满意度","type":1},{"id":4,"number":4,"pageNum":1,"pageSize":20,"problemItems":[{"answer":"很满意","id":13,"pageNum":1,"pageSize":20,"problemId":4,"sort":1,"val":5},{"answer":"满意","id":14,"pageNum":1,"pageSize":20,"problemId":4,"sort":2,"val":4},{"answer":"一般","id":15,"pageNum":1,"pageSize":20,"problemId":4,"sort":3,"val":3},{"answer":"不满意","id":16,"pageNum":1,"pageSize":20,"problemId":4,"sort":4,"val":0}],"surveyId":1,"title":"护士与您交流您的疾病的健康指导满意度","type":1},{"id":5,"number":5,"pageNum":1,"pageSize":20,"problemItems":[{"answer":"很满意","id":17,"pageNum":1,"pageSize":20,"problemId":5,"sort":1,"val":5},{"answer":"满意","id":18,"pageNum":1,"pageSize":20,"problemId":5,"sort":2,"val":4},{"answer":"一般","id":19,"pageNum":1,"pageSize":20,"problemId":5,"sort":3,"val":3},{"answer":"不满意","id":20,"pageNum":1,"pageSize":20,"problemId":5,"sort":4,"val":0}],"surveyId":1,"title":"您对医务人员能否及时解答您提出的病情治疗等问题的满意度","type":1},{"id":6,"number":6,"pageNum":1,"pageSize":20,"problemItems":[{"answer":"很满意","id":21,"pageNum":1,"pageSize":20,"problemId":6,"sort":1,"val":5},{"answer":"满意","id":22,"pageNum":1,"pageSize":20,"problemId":6,"sort":2,"val":4},{"answer":"一般","id":23,"pageNum":1,"pageSize":20,"problemId":6,"sort":3,"val":3},{"answer":"不满意","id":24,"pageNum":1,"pageSize":20,"problemId":6,"sort":4,"val":0}],"surveyId":1,"title":"您对门诊挂号、缴费、入院以及缴费这些手续的满意度","type":1},{"id":7,"number":7,"pageNum":1,"pageSize":20,"problemItems":[{"answer":"王小波","id":25,"pageNum":1,"pageSize":20,"problemId":7,"sort":1,"val":0},{"answer":"周丹","id":26,"pageNum":1,"pageSize":20,"problemId":7,"sort":2,"val":0},{"answer":"小红","id":27,"pageNum":1,"pageSize":20,"problemId":7,"sort":3,"val":0},{"answer":"小南","id":28,"pageNum":1,"pageSize":20,"problemId":7,"sort":4,"val":0}],"surveyId":1,"title":"您最满意的护士","type":1},{"id":8,"number":8,"pageNum":1,"pageSize":20,"problemItems":[{"answer":"100","id":29,"pageNum":1,"pageSize":20,"problemId":8,"sort":1,"val":100},{"answer":"85","id":30,"pageNum":1,"pageSize":20,"problemId":8,"sort":2,"val":85},{"answer":"70","id":31,"pageNum":1,"pageSize":20,"problemId":8,"sort":3,"val":70},{"answer":"60","id":32,"pageNum":1,"pageSize":20,"problemId":8,"sort":4,"val":60}],"surveyId":1,"title":"您对护士打分","type":1},{"id":9,"number":9,"pageNum":1,"pageSize":20,"problemItems":[],"surveyId":1,"title":"您对我们医院的建议","type":2}]
         * teamId : 57
         * title : 深圳市人民医院满意度调查
         */

        private String description;
        private int id;
        private int pageNum;
        private int pageSize;
        private int teamId;
        private String title;
        private List<ProblemsBean> problems;

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getPageNum() {
            return pageNum;
        }

        public void setPageNum(int pageNum) {
            this.pageNum = pageNum;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getTeamId() {
            return teamId;
        }

        public void setTeamId(int teamId) {
            this.teamId = teamId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<ProblemsBean> getProblems() {
            return problems;
        }

        public void setProblems(List<ProblemsBean> problems) {
            this.problems = problems;
        }


        public static class ProblemsBean implements MultiItemEntity {
            /**
             * id : 1
             * number : 1
             * pageNum : 1
             * pageSize : 20
             * problemItems : [{"answer":"很满意","id":1,"pageNum":1,"pageSize":20,"problemId":1,"sort":1,"val":5},{"answer":"满意","id":2,"pageNum":1,"pageSize":20,"problemId":1,"sort":2,"val":4},{"answer":"一般","id":3,"pageNum":1,"pageSize":20,"problemId":1,"sort":3,"val":3},{"answer":"不满意","id":4,"pageNum":1,"pageSize":20,"problemId":1,"sort":4,"val":0}]
             * surveyId : 1
             * title : 您对医师、护士技术的满意度
             * type : 1 (1：单选；2 填空；3 多选)
             */

            private int id;
            private int number;
            private int pageNum;
            private int pageSize;
            private int surveyId;
            private String title;
            private int type;
            private List<ProblemItemsBean> problemItems;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getNumber() {
                return number;
            }

            public void setNumber(int number) {
                this.number = number;
            }

            public int getPageNum() {
                return pageNum;
            }

            public void setPageNum(int pageNum) {
                this.pageNum = pageNum;
            }

            public int getPageSize() {
                return pageSize;
            }

            public void setPageSize(int pageSize) {
                this.pageSize = pageSize;
            }

            public int getSurveyId() {
                return surveyId;
            }

            public void setSurveyId(int surveyId) {
                this.surveyId = surveyId;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public List<ProblemItemsBean> getProblemItems() {
                return problemItems;
            }

            public void setProblemItems(List<ProblemItemsBean> problemItems) {
                this.problemItems = problemItems;
            }

            @Override
            public int getItemType() {
                return type;
            }

            public static class ProblemItemsBean {
                /**
                 * answer : 很满意
                 * id : 1
                 * pageNum : 1
                 * pageSize : 20
                 * problemId : 1
                 * sort : 1
                 * val : 5
                 */

                private String answer;
                private int id;
                private int pageNum;
                private int pageSize;
                private int problemId;
                private int sort;
                private int val;

                public String getAnswer() {
                    return answer;
                }

                public void setAnswer(String answer) {
                    this.answer = answer;
                }

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public int getPageNum() {
                    return pageNum;
                }

                public void setPageNum(int pageNum) {
                    this.pageNum = pageNum;
                }

                public int getPageSize() {
                    return pageSize;
                }

                public void setPageSize(int pageSize) {
                    this.pageSize = pageSize;
                }

                public int getProblemId() {
                    return problemId;
                }

                public void setProblemId(int problemId) {
                    this.problemId = problemId;
                }

                public int getSort() {
                    return sort;
                }

                public void setSort(int sort) {
                    this.sort = sort;
                }

                public int getVal() {
                    return val;
                }

                public void setVal(int val) {
                    this.val = val;
                }
            }
        }
    }
}
