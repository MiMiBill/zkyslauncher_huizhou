package com.muju.note.launcher.app.video.bean;

public class VideoDownLoadBean {

    /**
     {
     "successful": true,
     "messageId": 200,
     "message": "操作成功!",
     "timestamp": 1576727053902,
     "data": {
     "id": 4,
     "url": "http://qiniupaddb.zgzkys.com/201912191120.7z",
     "tableName": "video",
     "remark": "影视资源",
     "count": 29545,
     "version": 1,
     "createTime": "2019-12-19 03:21:17"
     }
     }
     */
    private String message;
    private String messageId;
    private String timestamp;
    private boolean successful;
    private VideoDownLoadBean.VideoDownLoadData data;


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    public VideoDownLoadData getData() {
        return data;
    }

    public void setData(VideoDownLoadData data) {
        this.data = data;
    }

    public static class  VideoDownLoadData{

//        "id": 4,
//                "url": "http://qiniupaddb.zgzkys.com/201912191120.7z",
//                "tableName": "video",
//                "remark": "影视资源",
//                "count": 29545,
//                "version": 1,
//                "createTime": "2019-12-19 03:21:17"

        private int  id;
        private String url;
        private String tableName;
        private String remark;
        private int count;
        private String version;
        private String createTime;


        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getTableName() {
            return tableName;
        }

        public void setTableName(String tableName) {
            this.tableName = tableName;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }
    }


}
