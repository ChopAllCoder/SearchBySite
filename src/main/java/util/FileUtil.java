package util;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author saysky
 * @date 2021/11/12
 */
public class FileUtil {
    /**
     * 读取txt文件
     *
     * @param file 要读取文件路径。例
     * @return
     */
    public static String readFile(String file) {

        StringBuilder content = new StringBuilder("");
        try {
//            FileReader reader = new FileReader(file);
            // 读取到缓冲区
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            String line;
            // 一次读入一行数据
            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            System.out.println("ERROR 输入文件[ " + file + " ]不存在");
        }
        return content.toString();
    }

    /**
     * 读取txt文件
     *
     * @param file 要读取文件路径。例
     * @return
     */
    public static String readFileByStream(InputStream file) {

        StringBuilder content = new StringBuilder("");
        try {
//            FileReader reader = new FileReader(file);
            // 读取到缓冲区
            BufferedReader br = new BufferedReader(new InputStreamReader(file, "UTF-8"));
            String line;
            // 一次读入一行数据
            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            System.out.println("ERROR 输入文件[ " + file + " ]不存在");
        }
        return content.toString();
    }


    /**
     * 写入txt文件
     *
     * @param file    要写入文件路径。例：D://test.txt
     * @param content 写入内容
     */
    public static void writeFile(String file, String content) {
        try {
            File writeName = new File(file);
            // 创建新文件,同名的文件会被覆盖
            writeName.createNewFile();
            FileWriter writer = new FileWriter(writeName);
            BufferedWriter out = new BufferedWriter(writer);
            // 缓存区内容写入内容
            out.write(content);
            // 把缓存区内容写入文件
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 追加文件：使用FileWriter
     *
     * @param file
     * @param content
     */
    public static synchronized void  appendFile(String file, String content) {
        FileWriter writer = null;
        try {
            // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
            writer = new FileWriter(file, true);
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 清空文件内容
     *
     * @param fileName
     */
    public static void clearFile(String fileName) {
        File file = new File(fileName);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write("");
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建文件夹
     *
     * @param fileName
     */
    public static void createFile(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
                System.out.println(fileName + "创建成功");
            } catch (IOException e) {
                System.out.println(fileName + "文件创建失败");
            }
        }
    }

    /**
     * 创建文件夹
     *
     * @param fileName
     */
    public static void createDirectory(String fileName) {
        File file = new File(fileName);
        //如果文件夹不存在则创建
        if (!file.exists() && !file.isDirectory()) {
            System.out.println(fileName + "创建成功");
            file.mkdir();
        }
    }

}



