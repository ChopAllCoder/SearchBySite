package input;

import conf.Constant;
import util.FileUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 输入服务
 *
 * @author saysky
 * @date 2021/11/12
 */
public class InputService {

    /**
     * 根据文件
     *
     * @return
     */
    private List<String> getWordList() throws Exception {
        try {
            String path = Constant.INPUT_PATH;
            String content = FileUtil.readFile(path);
            String[] arr = content.split("\n");
            return Arrays.asList(arr);
        } catch (Exception e) {
            throw new Exception("输入文件读取失败，请检查input.txt");
        }
    }


    /**
     * 获取待处理数据列表
     *
     * @return
     * @throws Exception
     * 将input 配置文件内容读入 list<input>中并且返回
     */
    public List<Input> getInputList() throws Exception {
        List<Input> inputList = new ArrayList<>();
        List<String> wordList = getWordList();  // 将input.txt中的内容按行读入
//        对wordlist进行分割，装配进入inputlist中
        for (String str : wordList) {
            if (str == null || "".equals(str.trim())) {
                continue;
            }
            if (str.trim().startsWith("#")) {  // 如果是 # 开头的也不要进行读入
                continue;
            }
            Input input = new Input();
            String[] arr = str.split("\t");
            if (arr.length == 4) {
                input.setBookname(arr[0].trim());
                input.setAuthor(arr[1].trim());
                input.setPublication(arr[2].trim());
                input.setCountry(arr[3].trim());
//                System.out.println(arr[0].trim()+arr[1].trim()+arr[2].trim()+arr[3].trim());
                inputList.add(input);
            }
        }
        return inputList;
    }


//
//    /**
//     * 根据文件
//     *
//     * @return
//     */
//    public List<String> getFinishKeywords() throws Exception {
//        try {
//            String path = Constant.FINISH_KEYWORDS_PATH;
//            String content = FileUtil.readFile(path);
//            String[] arr = content.split("\n");
//            return Arrays.asList(arr);
//        } catch (Exception e) {
//            throw new Exception("输入文件读取失败，请检查input.txt");
//        }
//    }
//
//    /**
//     * 查询没有完成的
//     *
//     * @return
//     * @throws Exception
//     */
//    public List<String> getNotFinishWordList() throws Exception {
//        List<String> wordList = this.getWordList();
//        List<String> finishKeywords = this.getFinishKeywords();
//        if (finishKeywords != null && finishKeywords.size() > 0) {
//            wordList.removeAll(finishKeywords);
//        }
//        return wordList;
//    }
}
