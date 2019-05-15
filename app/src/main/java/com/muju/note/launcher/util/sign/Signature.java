package com.muju.note.launcher.util.sign;


import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

/**
 * 后台给的签名类
 *
 * @author suhua
 * @date 2018年8月11日
 */
public class Signature {

    public static final String PAD_SIGN = "SIGN";

    private static final String hexDigits[] = {"0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    public static String getSign(Map<String, String> map, String key) {

        ArrayList<String> list = new ArrayList<String>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (entry.getValue() != "") {
                list.add(entry.getKey() + "=" + entry.getValue().trim() + "&");
            }
        }
        int size = list.size();
        String[] arrayToSort = list.toArray(new String[size]);
        Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append(arrayToSort[i]);
        }
        String result = sb.toString();

        result += "key=" + key;
        result = MD5Encode(result).toUpperCase();
        return result;
    }


    /**
     * 数据返回签名算法
     */
    public static String getReturnDataSign(String data, String key) {
        data += "key=" + key;
        data = MD5Encode(data).toUpperCase();
        return data;
    }


    public static String MD5Encode(String origin) {
        String resultString = null;
        try {
            resultString = origin;
            MessageDigest md = MessageDigest.getInstance("MD5");
            resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultString;
    }

    private static String byteArrayToHexString(byte b[]) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++)
            resultSb.append(byteToHexString(b[i]));

        return resultSb.toString();
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0)
            n += 256;
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }



//    public static void main(String[] args) {
//
//        System.out.println("ODk4NjAxMTcwNTMxMDExMDI0Miw4NjAwOTUxNTM4NjI0MDM=20190321".hashCode());
//        //7E4DC42D3D9E306B5E95A99C85227772
//        //pageNum=1&pageSize=20&teamId=57&key=13333333333
//        //BA8C5BD7F7EFD55113F57E10D6D93199
//        Map<String, String> map = new HashMap<>();
//
//        map.put("pageNum", "1");
//        map.put("pageSize", "20");
//        map.put("teamId", "57");
//        System.out.println(getSign(map, "13333333333"));
//
//    }
}
