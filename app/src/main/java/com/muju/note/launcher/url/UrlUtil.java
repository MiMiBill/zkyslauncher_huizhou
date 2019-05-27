package com.muju.note.launcher.url;

public class UrlUtil {

    //    public static final String HOST_DEFAULT = "http://pad.zgzkys.com";
    public static final String HOST_DEFAULT = "http://test.pad.zgzkys.com";
//    public static final String HOST_DEFAULT = "http://192.168.1.114:8086";
//    public static final String HOST_DEFAULT = "http://192.168.1.200:8086";

    public static String getHost(){
        return HOST_DEFAULT;
    }

    /**
     *   获取平板配置信息
     * @return
     */
    public static String getPadConfigsNew(){
        return getHost()+"/padConfig/getConfig";
    }

    /**
     *   医院风采
     * @return
     */
    public static String getHospitalInfo() {
        return getHost() + "/hospitalMien/list/%s";
    }

    /**
     *  获取视频本地化信息
     * @return
     */
    public static String getVideoDownLoadUrl(){
        return getHost()+"/video/downloadUrl";
    }

    /**
     *  获取每日影视更新列表
     * @return
     */
    public static String getVideoUpdate(){
        return getHost()+"/video/getLately";
    }

    /**
     *  获取影视类型及所有标签
     * @return
     */
    public static String getVideoColumnsTags(){
        return getHost()+"/video/columnTags";
    }

    /**
     *  根据条件查询视频
     * @return
     */
    public static String getSerchVideo(){
        return getHost()+"/video/search";
    }

}
