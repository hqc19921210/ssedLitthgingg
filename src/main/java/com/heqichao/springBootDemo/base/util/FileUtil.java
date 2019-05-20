package com.heqichao.springBootDemo.base.util;

import com.heqichao.springBootDemo.base.param.RequestContext;

import java.io.File;
import java.util.Date;

/**
 * Created by heqichao on 2018-12-9.
 */
public class FileUtil {
    /**
     * 临时创建的下载目录文件
     * @param fileName
     * @return
     */
    public static File createTempDownloadFile(String fileName){
        // 获取临时目录
        String filePath = RequestContext.getContext().getSession().getServletContext().getRealPath("/download/");
        File rootDir =new File(filePath);
        if(!rootDir.exists()){
            rootDir.mkdirs();
        }
        // 在临时目录下面创建临时子目录
        File dir = new File(filePath+ System.currentTimeMillis());
        if(!dir.exists()){
            dir.mkdir();
        }
        return new File(dir.getAbsolutePath()+File.separator+fileName);
    }

    /**
     * 删除 文件
     * @param file
     */
    public static void deleteFile(File file){
        try {
            if(file!=null ){
                file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
