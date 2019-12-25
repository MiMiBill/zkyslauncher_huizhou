package com.muju.note.launcher.app.video.bean;

import com.muju.note.launcher.app.video.db.VideoInfoDao;

import java.util.ArrayList;

/**
 * Created by long yun
 * on 2019/12/19
 */
public class UpdateVideoCountBean {

//    {
//        "data": {
//        "stopVideoIds": [
//        0
//		],
//        "videos": [{
//            "actor": "string",
//                    "cid": "string",
//                    "columnId": "string",
//                    "columnName": "string",
//                    "createTime": "2019-12-19T03:02:53.895Z",
//                    "customTag": "string",
//                    "description": "string",
//                    "director": "string",
//                    "duration": 0,
//                    "editTime": "string",
//                    "fee": 0,
//                    "id": 0,
//                    "imgUrl": "string",
//                    "isClassify": 0,
//                    "isNew": 0,
//                    "isRecommend": 0,
//                    "keywords": "string",
//                    "logoUrl": "string",
//                    "name": "string",
//                    "number": 0,
//                    "onwayTime": "string",
//                    "region": "string",
//                    "score": "string",
//                    "screenUrl": "string",
//                    "source": 0,
//                    "status": 0,
//                    "tid": "string",
//                    "typeId": "string",
//                    "updateTime": "2019-12-19T03:02:53.896Z",
//                    "videoType": 0,
//                    "watchCount": 0
//        }]
//    },
//        "message": "string",
//            "messageId": 0,
//            "successful": true,
//            "timestamp": 0
//    }

    private String message;
    private boolean successful;
    private long timestamp;
    private int messageId;
    private long data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public long getData() {
        return data;
    }

    public void setData(long data) {
        this.data = data;
    }
}
