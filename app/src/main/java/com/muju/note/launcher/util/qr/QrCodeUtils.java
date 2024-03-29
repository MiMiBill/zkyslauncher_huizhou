package com.muju.note.launcher.util.qr;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Base64;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/7/2.
 */

public class QrCodeUtils {

    /**
     * 生成二维码
     *
     * @param content
     * @param width
     * @param height
     * @return
     */
    public static Bitmap generateBitmap(String content, int width, int height) {
        String encodeToString = Base64.encodeToString(content.getBytes(), Base64.DEFAULT);
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, String> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        try {
            BitMatrix encode = qrCodeWriter.encode(encodeToString, BarcodeFormat.QR_CODE, width, height, hints);
            int[] pixels = new int[width * height];
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (encode.get(j, i)) {
                        pixels[i * width + j] = 0x00000000;
                    } else {
                        pixels[i * width + j] = 0xffffffff;
                    }
                }
            }
            return Bitmap.createBitmap(pixels, 0, width, width, height, Bitmap.Config.RGB_565);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap generateOriginalBitmap(String content, int width, int height) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, String> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        try {
            BitMatrix encode = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints);
            int[] pixels = new int[width * height];
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (encode.get(j, i)) {
                        pixels[i * width + j] = 0x00000000;
                    } else {
                        pixels[i * width + j] = 0xffffffff;
                    }
                }
            }
            return Bitmap.createBitmap(pixels, 0, width, width, height, Bitmap.Config.RGB_565);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 给二维码加logo
     *
     * @param qrBitmap
     * @param logoBitmap
     * @return
     */
    public static Bitmap addLogo(Bitmap qrBitmap, Bitmap logoBitmap) {
//        int qrBitmapWidth = qrBitmap.getWidth();
//        int qrBitmapHeight = qrBitmap.getHeight();
//        int logoBitmapWidth = logoBitmap.getWidth();
//        int logoBitmapHeight = logoBitmap.getHeight();
//        Bitmap blankBitmap = Bitmap.createBitmap(qrBitmapWidth, qrBitmapHeight, Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(blankBitmap);
//        canvas.drawBitmap(qrBitmap, 0, 0, null);
//        canvas.save(Canvas.ALL_SAVE_FLAG);
//        float scaleSize = 1.0f;
//        while ((logoBitmapWidth / scaleSize) > (qrBitmapWidth / 5) || (logoBitmapHeight / scaleSize) > (qrBitmapHeight / 5)) {
//            scaleSize *= 2;
//        }
//        float sx = 1.0f / scaleSize;
//        canvas.scale(sx, sx, qrBitmapWidth / 2, qrBitmapHeight / 2);
//        canvas.drawBitmap(logoBitmap, (qrBitmapWidth - logoBitmapWidth) / 2, (qrBitmapHeight - logoBitmapHeight) / 2, null);
//        canvas.restore();
//        return blankBitmap;
        return null;
    }
}
