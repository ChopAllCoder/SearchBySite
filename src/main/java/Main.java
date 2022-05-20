
import conf.Constant;
import conf.InitConfService;
import input.Input;
import input.InputService;
import task.SearchTask;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * @author saysky
 * @date 2021/11/12
 */
public class Main {


    public static void main(String[] args) throws Exception {
        ExecutorService threadPool = Executors.newFixedThreadPool(10);
        InputService inputService = new InputService();
        InitConfService initConfService = new InitConfService();

        try {
//            1. 初始化配置,读入cong.txt 文件。文件中存放检索时的各类参数配置
//            2. 读入website/下的各类文件，文件中存放各种语言下的定向检索网址
            System.out.println("开始读入初始化配置 ··· ");
            initConfService.initConf();
            System.out.println("初始化配置完成");

//          # 语言(只能写一种语言)：中简、英语、法语、俄语、德语、韩语、日语
/*            if (Constant.LANG_NAME=="中简"){

            }else if (Constant.LANG_NAME=="英语"){

            }else if (Constant.LANG_NAME=="韩语"){

            }else if (Constant.LANG_NAME=="日语"){

            }*/

//          3. 读入input.txt文件，文件中存放要被检索的图书信息
//           input中有五列分别是：书名、作者、出版社、国家、要检索的国家网址属于哪个国家（只能填写zh-has,en,jp,ko，其他不行）
            List<Input> inputList = inputService.getInputList();
            System.out.println("输入文件读取完成，共读取到" + inputList.size() + "个待处理的书本信息");
//            for (String S:Constant.WEBSITE_MAP.keySet()
//                 ) {
//                System.out.println(S+Constant.WEBSITE_MAP.get(S));
//            }
            System.exit(0);

            int count = 0;
            for (int i = 0; i < inputList.size(); i++) {
                Input input = inputList.get(i);
                SearchTask searchTask = new SearchTask();
                searchTask.setInput(input);
                searchTask.setNum(i);  // 第几本书的搜索任务
                searchTask.setTotal(inputList.size());  // 总共输入的要搜索书的个数
                threadPool.submit(searchTask);
            }
//            int count = 0;
//            Input input = inputList.get(i);
/*            SearchTask searchTask = new SearchTask();
            searchTask.setInput(input1);
            searchTask.setNum(1);  //第几个任务
            searchTask.setTotal(1);  // 总共的任务数
            threadPool.submit(searchTask);*/

//            threadPool.dealInputFile(inputList);
//            System.out.println("任务全部完成，总共获得" + count + "条数据，请检查result目录数据");

        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("系统异常，请检查文件后重新运行start.bat");
        }
    }
}
