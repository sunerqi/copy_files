package com.suncreate.syncfiles.job;

import com.suncreate.syncfiles.utils.ResourceUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Calendar;
import java.text.SimpleDateFormat;

/**
 * @Author: sunhailong
 * @Date: 2019/8/9 下午3:31
 * @Desc: 复制文件夹或文件夹
 */
@Component
public class CopyFiles {
    // 源文件夹
    private String resourceUrl = ResourceUtil.getResourceUrl();
    // 目标文件夹
    private String toUrl = ResourceUtil.getToUrl();

    /**
     * 将文件夹及里面的文件复制到另一文件夹,目录结构不变
     *
     * @throws IOException
     */
    @Scheduled(cron = "0 0 1 * * *")
    public void syncFiles() throws IOException {
        // 创建目标文件夹
        (new File(toUrl)).mkdirs();
        // 获取源文件夹当前下的文件或目录
        File[] file = (new File(resourceUrl)).listFiles();
        for (int i = 0; i < file.length; i++) {
            if (file[i].isFile()) {
                // 复制文件
                copyFile(file[i], new File(toUrl + file[i].getName()));
            }
            if (file[i].isDirectory()) {
                // 复制目录
                String sourceDir = resourceUrl + File.separator + file[i].getName();
                String targetDir = toUrl + File.separator + file[i].getName();
                copyDirectiory(sourceDir, targetDir);
            }
        }
    }

    // 复制文件
    public void copyFile(File sourceFile, File targetFile)
            throws IOException {

        //判断文件时间决定是否复制文件
        if (getModifiedTime(sourceFile)) {
            // 新建文件输入流并对它进行缓冲
            FileInputStream input = new FileInputStream(sourceFile);
            BufferedInputStream inBuff = new BufferedInputStream(input);

            // 新建文件输出流并对它进行缓冲
            FileOutputStream output = new FileOutputStream(targetFile);
            BufferedOutputStream outBuff = new BufferedOutputStream(output);

            // 缓冲数组
            byte[] b = new byte[1024 * 5];
            int len;
            while ((len = inBuff.read(b)) != -1) {
                outBuff.write(b, 0, len);
            }
            // 刷新此缓冲的输出流
            outBuff.flush();

            //关闭流
            inBuff.close();
            outBuff.close();
            output.close();
            input.close();
        }
    }

    // 复制文件夹
    public void copyDirectiory(String sourceDir, String targetDir)
            throws IOException {
        // 新建目标目录
        (new File(targetDir)).mkdirs();
        // 获取源文件夹当前下的文件或目录
        File[] file = (new File(sourceDir)).listFiles();
        for (int i = 0; i < file.length; i++) {
            if (file[i].isFile()) {
                // 源文件
                File sourceFile = file[i];
                // 目标文件
                File targetFile = new
                        File(new File(targetDir).getAbsolutePath()
                        + File.separator + file[i].getName());
                copyFile(sourceFile, targetFile);
            }
            if (file[i].isDirectory()) {
                // 准备复制的源文件夹
                String dir1 = sourceDir + "/" + file[i].getName();
                // 准备复制的目标文件夹
                String dir2 = targetDir + "/" + file[i].getName();
                copyDirectiory(dir1, dir2);
            }
        }
    }

    //判断文件是否是前一天最后修改时间，是-返回true且复制到文件共享区，否-返回false不处理
    public boolean getModifiedTime(File sourceFile) {
        boolean copyFlag = false;

        //文件最后修改时间
        Calendar cal = Calendar.getInstance();
        long time = sourceFile.lastModified();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        cal.setTimeInMillis(time);

//        System.out.println(sourceFile + "修改时间[2] " + formatter.format(cal.getTime()));
//        System.out.println("文件时间戳： " + cal.getTime());

        //昨天的凌晨时间
        Calendar yesterday = Calendar.getInstance();
        yesterday.set(Calendar.HOUR_OF_DAY, -24);//控制时
        yesterday.set(Calendar.MINUTE, 0);//控制分
        yesterday.set(Calendar.SECOND, 0);//控制秒

//        System.out.println(formatter.format(yesterday.getTime()));

        if (cal.after(yesterday) || cal.equals(yesterday)) {
            copyFlag = true;
        }
        return copyFlag;
    }
}
