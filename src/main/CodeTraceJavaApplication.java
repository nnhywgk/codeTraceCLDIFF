package main;


import com.alibaba.fastjson.JSONObject;
import edu.fdu.se.base.common.Global;
import edu.fdu.se.cldiff.CLDiffLocal;
import main.CLDiffAnalysis.AnalyseMeta;
import main.GitAction.FindCommit;
import main.GraphBuild.GraphBuilder;
import main.GraphInfo.ClassInfo;
import main.jgit.JGitHelper;
import main.util.DirExplorer;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;


//exclude= {DataSourceAutoConfiguration.class} 声明不启动数据库
//@SpringBootApplication
public class CodeTraceJavaApplication {

    private final String uri = "bolt://localhost:11002";
    private final String user = "neo4j";
    private final String password = "123456";

    //构造图
    private GraphBuilder graphBuilder ;

    public CodeTraceJavaApplication() {
        graphBuilder = new GraphBuilder(uri, user, password);
    }


    public static List<String> listClasses(File projectDir) {
        List<String> pathList = new ArrayList<>();
        new DirExplorer((level, path, file) -> path.endsWith(".java"),
                (level, path, file) -> pathList.add(file.getAbsolutePath())).explore(projectDir);
        return pathList;
    }

    public void graphConstructionBaseOneCommit(ProjectInfoBuilder projectInfo) {

        //构造节点
        graphBuilder.executeBatchCql(projectInfo.createCqlList(true));
        System.out.println("node");
        //构造关系
//        graphBuilder.executeBatchCql(projectInfo.createCqlList(false));
//        System.out.println("relation");
    }

    public void graphConstructionBaseOneCommitbyCLDiff(CommitDiff commitdiff) {

        //构造节点
        graphBuilder.executeBatchCql(commitdiff.createCqlList(true));
//        System.out.println("node");
        //构造关系
//        graphBuilder.executeBatchCql(projectInfo.createCqlList(false));
//        System.out.println("relation");
    }
    /***** 这两个函数用于给commit列表排序  ****/



//    public static String[] sortWithIndex (String[] strArr, int[] intIndex )
//    {
//        if (! isSorted(intIndex)){
//            final List<String> stringList = Arrays.asList(strArr);
//            Collections.sort(stringList, Comparator.comparing(s -> intIndex[stringList.indexOf(s)]));
//            return stringList.toArray(new String[stringList.size()]);
//        }
//        else
//            return strArr;
//    }
//    public static boolean isSorted(int[] arr) {
//        for (int i = 0; i < arr.length - 1; i++) {
//            if (arr[i + 1] < arr[i]) {
//                return false;
//            };
//        }
//        return true;
//    }


    /***** 这两个函数用于给commit列表排序  ****/


    public static void main(String[] args) throws IOException, GitAPIException {

//        SpringApplication.run(CodeTraceJavaApplication.class, args);
        long start,end;
        start = System.currentTimeMillis();

        String repoPath = "/Users/weisun/Documents/实验室工作/毕业设计/实验项目/dubbo";
        String projectName = "dubbo";

        ArrayList commitList =  new ArrayList<String>();
        ArrayList commitListTime =  new ArrayList<String>();

        String outputDir = "/Users/weisun/Documents/实验室工作/毕业设计/实验项目/out";
        Global.runningMode = 0;

//      /**********根据仓库地址查找所有的分支**********/
//		FindBranch findBranch = new FindBranch(repoPath);

        /**********根据仓库地址查找所有的Commit**********/
        FindCommit findCommit = new FindCommit(repoPath);
        commitList = findCommit.getCommitList();
        commitListTime = findCommit.getCommitListTime();


        /**********获取最初的版本**********/
        String firstCommit = (String) commitList.get(commitList.size() - 1);

        CodeTraceJavaApplication codeTraceJavaApplication = new CodeTraceJavaApplication(); //声明一个codeTraceJavaApplication
        ProjectInfoBuilder curProject;
        JGitHelper jGitHelper = new JGitHelper(repoPath);
        String firstCommiter = jGitHelper.getAuthorName(firstCommit);
//        jGitHelper.checkout(firstCommit);

        try {
            curProject = new ProjectInfoBuilder(projectName, firstCommit,firstCommiter , repoPath);
            codeTraceJavaApplication.graphConstructionBaseOneCommit(curProject);
        }catch(Exception e){
            System.out.println("123");
        }


//        commitList.remove(0); //去除首个版本
//        Collections.reverse(commitList);  //翻转列表


        /**********将ArrayList转为Array**********/
        String[] commitId = (String[]) commitList.toArray(new String[commitList.size()]); //获取commit

        int[] commitTime = new int[commitListTime.size()];  //循环遍历获取committime
        for(int i = 0;i<commitListTime.size();i++){
            commitTime[i] = (int) commitListTime.get(i);
        }

        Map map = new HashMap();

        for (int i=0;i<commitId.length;i++){
            //map集合添加数据
            map.put(commitId[i],commitTime[i]);
        }

        List<Map.Entry<String, Integer>> list = new ArrayList<>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>()
        {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2)
            {
                //按照value值，重小到大排序
//                return o1.getValue() - o2.getValue();

                //按照value值，从大到小排序
//                return o2.getValue() - o1.getValue();

                //按照value值，用compareTo()方法默认是从小到大排序
                return o1.getValue().compareTo(o2.getValue());
            }
        });

        commitList = single(commitList);
        for(int i=1;i<commitList.size();i++){
            try{
                String curCommit = list.get(i).getKey();
                CLDiffLocal CLDiffLocal = new CLDiffLocal();
                CLDiffLocal.run(curCommit,repoPath+"/.git",outputDir);

                AnalyseMeta analyseMeta = new AnalyseMeta(outputDir+ "/" + projectName ,curCommit);
                Map<JSONObject, String> diffFileAction = analyseMeta.gitMetaInfo();
//
                for (Map.Entry<JSONObject, String> m : diffFileAction.entrySet()) {

                    //遍历meta文件中的结果
                    if (! m.getKey().getString("file_name").endsWith(".java")) {

                        continue;

                    }
                    System.out.println(m.getKey().getString("file_name"));
//
                    String diffpath = m.getKey().getString("diffPath"); //diff路径
                    String difftype = m.getValue(); //diff类型
                    String curfile = m.getKey().getString("curr_file_path"); //当前代码
                    String prefile = m.getKey().getString("prev_file_path"); //上版本代码
                    String parent[] = {m.getKey().getString("parent")}; //父版本

                    CommitDiff curCommitDiiff = new CommitDiff(difftype,outputDir+"/"+projectName+"/"+curCommit+"/"+curfile,projectName);
                    codeTraceJavaApplication.graphConstructionBaseOneCommitbyCLDiff(curCommitDiiff);



                }
            }
            catch (Exception e){

            }



        }
//        for(String str:commitId){
//            System.out.println(str);
//            CLDiffLocal CLDiffLocal = new CLDiffLocal();
//            CLDiffLocal.run(str,repoPath,outputDir);
//
//            AnalyseMeta analyseMeta = new AnalyseMeta(outputDir+ "/" + projectName ,str);
//            Map<JSONObject, String> diffFileAction = analyseMeta.gitMetaInfo();
//
//            for (Map.Entry<JSONObject, String> m : diffFileAction.entrySet()) {
//                //遍历meta文件中的结果
//                if (! m.getKey().getString("file_name").endsWith(".java")) {
//                    System.out.println(m.getKey().getString("diffpath"));
//                    continue;
//                }
//
//            }
//        }



//
//
//
//        /**********执行Cldiff产生meta文件**********/
//        //执行cldiff
//        CLDiffLocal cLDiffLocal = new CLDiffLocal();
//        cLDiffLocal.run("ff515fd998339241c59b9875b4517444d1da0469",repoPath,outputDir);
//        //分析meta文件
//        AnalyseMeta analyseMeta = new AnalyseMeta(outputDir+ "/" + projectName ,"ff515fd998339241c59b9875b4517444d1da0469");
//        Map<JSONObject, String> diffFileAction = analyseMeta.gitMetaInfo();
//
//        for (Map.Entry<JSONObject, String> m : diffFileAction.entrySet()) {
//            //遍历meta文件中的结果
//            if (! m.getKey().getString("file_name").endsWith(".java")) {
//                continue;
//            }
//
//        }
//
//
//
//        String[] commitId = (String[]) commitList.toArray(new String[commitList.size()]);
//
//        for(String str:commitId){
//            System.out.println(str);
//            CLDiffLocal CLDiffLocal = new CLDiffLocal();
//            CLDiffLocal.run(str,repoPath,outputDir);
//        }


        end = System.currentTimeMillis();
        System.out.println("start time:" + start+ "; end time:" + end+ "; Run Time:" + (end - start) + "(ms)");
        System.out.println("MAP:"+map.size());
        System.out.println("commitNum:"+commitList.size());


    }



    public static ArrayList single(ArrayList al) {
        ArrayList temp =new ArrayList();   //新建一个ArrayList容器
        Iterator it = al.iterator();   //新建迭代器
        while(it.hasNext()) {   //使用迭代器遍历需要除重的容器
            Object obj=it.next();
            if (!temp.contains(obj)) {  //如果新容器中不包含当前元素
                temp.add(obj);   //添加之
            }
        }
        return temp;    //此方法返回的是一个ArrayList对象，需要用一个ArrayList对象来接收
    }

    public class KeyCompareUtil implements Comparator<String>{

        /**
         *
         *  从小到大排序
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        @Override
        public int compare(String s1, String s2) {
            return Integer.valueOf(s1).compareTo(Integer.valueOf(s2));
        }
    }

}
