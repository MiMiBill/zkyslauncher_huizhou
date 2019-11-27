package com.muju.note.launcher.app.video.bean;

/**
 * Created by long yun
 * on 2019/11/23
 */
public class WeiXinTask {

//    {
//        "data": {
//        "code": "string",
//                "taskUrl": "string"
//    },
//        "message": "string",
//            "messageId": 0,
//            "successful": true,
//            "timestamp": 0
//    }

    private String message;
    private String messageId;
    private String timestamp;
    private boolean successful;
    private WeiXinTaskData data;

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

    public WeiXinTaskData getData() {
        return data;
    }

    public void setData(WeiXinTaskData data) {
        this.data = data;
    }

    public static class WeiXinTaskData{
        private String code;
        private String taskUrl;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getTaskUrl() {
            return taskUrl;
        }

        public void setTaskUrl(String taskUrl) {
            this.taskUrl = taskUrl;
        }
    }


}
