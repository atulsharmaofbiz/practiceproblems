package com.example.problem3;

import org.apache.commons.io.FileUtils;
import java.io.*;
import java.nio.file.Paths;
import java.util.*;

public class FileUtil {

    private static final int BUFFER_SIZE=4096;

    public static final int CHUNK_SIZE = BUFFER_SIZE*1024;

    public static String getFileNameWithoutExt(String fullName){
        String fName = getFileName(fullName);
        String fNameWithoutExt = fName.substring(0,fName.lastIndexOf("."));
        return fNameWithoutExt;
    }

    public static String getFileName(String fullName){
        String[] vals = fullName.split("/");
        String fName=vals[vals.length-1];
        return fName;
    }

    public static String getChunkFilePath(String baseDir,String fileUrl,int chunkNum){
        String fNameWithoutExt = getFileNameWithoutExt(fileUrl);
        return String.format("%s/%s/chunk_%s",baseDir,fNameWithoutExt,chunkNum);
    }

    public static void createDir(String path){
        new File(path).mkdirs();
    }

    public static boolean fileExists(String filePath){
        File file = new File(filePath);
        return file.exists();
    }

    public static String getChunkDirPath(String baseDir,String fileUrl){
        String fNameWithoutExt = getFileNameWithoutExt(fileUrl);
        return String.format("%s/%s",baseDir,fNameWithoutExt);
    }

    public static byte[] readChunk(int chunkNum,String filePath) throws IOException{
        BufferedInputStream inputStream=null;
        try {
            inputStream = new BufferedInputStream(new FileInputStream(new File(filePath)));
            byte[] buffer = new byte[CHUNK_SIZE];
            long offset = (long) chunkNum*(long)CHUNK_SIZE;
            inputStream.skip(offset);
            int cnt=inputStream.read(buffer);
            return Arrays.copyOf(buffer,cnt);
        }finally {
            if(inputStream!=null)
                inputStream.close();
        }
    }


    public static void combineChunks(String fileChunkDir,String tgtFilePath) throws IOException {
        File file = new File(fileChunkDir);
        File[] files=file.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return (name.contains("chunk_"));
            }
        });
        List<File> fileList = Arrays.asList(files);
        fileList.sort((x,y)->{
            return
                    Integer.parseInt(x.getName().replace("chunk_", "")) -
                            Integer.parseInt(y.getName().replace("chunk_", ""));
            }
        );
        File tgtFile = new File(tgtFilePath);
        tgtFile.createNewFile();

        BufferedOutputStream os = null;
        try {
            os = new BufferedOutputStream(new FileOutputStream(tgtFile));
            for (File f : fileList) {
                byte[] bytes = FileUtils.readFileToByteArray(f);
                os.write(bytes);
                f.delete();
            }
        }finally {
            if(os!=null)
                os.close();
        }
    }

    public int copy(InputStream input,byte[] buffer)throws IOException{
        return input.read(buffer);
    }

    public static int numOfChunks(long fileSize){
        if(fileSize%CHUNK_SIZE==0)
            return(int)(fileSize/CHUNK_SIZE);
        else
            return (int)(fileSize/CHUNK_SIZE+1);
    }

    public static void createFile(String filePath) throws IOException {
        File file = new File(filePath);
        FileUtils.forceMkdir(Paths.get(filePath).getParent().toFile());

        if(file.exists()==false)
            file.createNewFile();
    }

    public static void writeToFile(byte[] data,String filePath) throws IOException {
        BufferedOutputStream outputStream = null;
        try{
            outputStream = new BufferedOutputStream(new FileOutputStream(new File(filePath)));
            outputStream.write(data);
            System.out.println("finished writing file"+filePath);
        }catch (Exception ex){

        }finally {
            if(outputStream!=null)
                outputStream.close();
        }
    }

    public void copyLarge(File src,File tgt) throws FileNotFoundException,IOException {
        if(!src.exists())
            throw new FileNotFoundException();
        if(tgt.exists()){
            tgt.delete();
        }
        File tempFile = getTempFile();
        BufferedOutputStream os = null;
        BufferedInputStream is = null;
        try {
            os = new BufferedOutputStream(new FileOutputStream(tempFile));
            is = new BufferedInputStream(new FileInputStream(src));
            byte[] buffer = new byte[BUFFER_SIZE];
            long cnt = 1;
            while (cnt > 0) {
                    cnt = copy(is, buffer);
                    os.write(buffer);
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
