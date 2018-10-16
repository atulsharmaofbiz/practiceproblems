package com.example.problem3;

import com.google.common.io.Files;
import java.io.*;
import java.util.UUID;

public class FileUtil {

    private static final int BUFFER_SIZE=4096;

    public int copyLargeWithOffset(InputStream input,byte[] buffer)throws IOException{
        return input.read(buffer);
    }

    public void copyLarge(File src,File tgt) throws FileNotFoundException,IOException {
        int offset=0;
        if(!src.exists())
            throw new FileNotFoundException();
        if(tgt.exists()){
            tgt.delete();
        }
        File tempFile = getTempFile();
        long size=src.length();
        BufferedOutputStream os = null;
        BufferedInputStream is = null;
        try {
            os = new BufferedOutputStream(new FileOutputStream(tempFile));
            is = new BufferedInputStream(new FileInputStream(src));
            byte[] buffer = new byte[BUFFER_SIZE];
            long cnt = 1;
            while (cnt > 0) {
                    cnt = copyLargeWithOffset(is, buffer);
                    os.write(buffer);
                    offset += cnt;
            }
            os.flush();
            tempFile.renameTo(tgt);
        }finally {
            if(os!=null){
                os.close();is.close();
            }
        }

    }




    private static File getTempFile() {
        String tempDir=System.getenv("TMPDIR");
        return new File(tempDir+"/"+ UUID.randomUUID());
    }


    public static void main(String[] args) {
        FileUtil fileUtil = new FileUtil();
        File src = new File(args[0]);
        File tgt = new File(args[1]);
        try {
            fileUtil.copyLarge(src,tgt);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
