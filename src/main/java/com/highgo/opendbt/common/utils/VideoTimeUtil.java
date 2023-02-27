package com.highgo.opendbt.common.utils;


import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.info.MultimediaInfo;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * @Description: 视频处理工具类
 * @Title: TimeUtil
 * @Package com.highgo.opendbt.utils
 * @Author:
 * @Copyright 版权归HIGHGO企业所有
 * @CreateTime: 2022/8/25 13:44
 */
public class VideoTimeUtil {
    private static final Logger logger = LoggerFactory.getLogger(VideoTimeUtil.class);

    /**
     * 视频时长
     *
     * @param fileUrl
     * @return String[] 0=秒时长，1=展示时长（格式如 01:00:00）
     */
    public static String[] parseDuration(String fileUrl) {
        String[] length = new String[2];
        try {
            // 构造方法 接受File对象
            MultimediaObject instance = new MultimediaObject(new File(fileUrl));
            MultimediaInfo result = instance.getInfo();
            Long ls = result.getDuration() / 1000;
            length[0] = String.valueOf(ls);
            Integer hour = (int) (ls / 3600);
            Integer minute = (int) (ls % 3600) / 60;
            Integer second = (int) (ls - hour * 3600 - minute * 60);
            String hr = hour.toString();
            String mi = minute.toString();
            String se = second.toString();
            if (hr.length() < 2) {
                hr = "0" + hr;
            }
            if (mi.length() < 2) {
                mi = "0" + mi;
            }
            if (se.length() < 2) {
                se = "0" + se;
            }

            String noHour = "00";
            if (noHour.equals(hr)) {
                length[1] = mi + ":" + se;
            } else {
                length[1] = hr + ":" + mi + ":" + se;
            }


        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return length;
    }

    /**
     * 截取视频第六帧的图片
     *
     * @param filePath 视频路径
     * @param dir      文件存放的根目录
     * @return 图片的相对路径 例：pic/1.png
     * @throws FrameGrabber.Exception
     */
    public static String videoImage(String filePath, String dir) throws FrameGrabber.Exception {
        String pngPath = "";
        FFmpegFrameGrabber ff = FFmpegFrameGrabber.createDefault(filePath);

        ff.start();
        int ffLength = ff.getLengthInFrames();
        Frame f;
        int i = 0;
        while (i < ffLength) {
            f = ff.grabImage();
            //截取第6帧
            if ((i > 5) && (f.image != null)) {
                //生成图片的相对路径 例如：pic/uuid.png
                pngPath = getPngPath();
                //执行截图并放入指定位置
                doExecuteFrame(f, dir + pngPath);
                break;
            }
            i++;
        }
        ff.stop();

        return dir + pngPath;
    }

    /**
     * 生成图片的相对路径
     *
     * @return 图片的相对路径 例：pic/1.png
     */
    private static String getPngPath() {
        return "pic" + File.separator + getUUID() + ".png";
    }


    /**
     * 生成唯一的uuid
     *
     * @return uuid
     */
    private static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }


    /**
     * 截取缩略图
     *
     * @param f                       Frame
     * @param targerFilePath:封面图片存放路径
     */
    private static void doExecuteFrame(Frame f, String targerFilePath) {
        String imagemat = "png";
        if (null == f || null == f.image) {
            return;
        }
        Java2DFrameConverter converter = new Java2DFrameConverter();
        BufferedImage bi = converter.getBufferedImage(f);
        File output = new File(targerFilePath);
        if (!output.exists()) {
            output.mkdirs();
        }
        try {
            ImageIO.write(bi, imagemat, output);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }
}

