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

    private final String uri = "bolt://localhost:7687";
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


    public static void main(String[] args) throws IOException, GitAPIException {

//        SpringApplication.run(CodeTraceJavaApplication.class, args);

        String repoPath = "/Users/weisun/Documents/毕业工具/codeTraceCLDIFF";
        String projectName = "codeTraceCLDIFF";

        ArrayList commitList =  new ArrayList<String>();
        ArrayList commitListTime =  new ArrayList<String>();

        String outputDir = "/Users/weisun/Documents/毕业工具/测试项目/codeTraceCLDIFF-output";
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
        curProject = new ProjectInfoBuilder(projectName, firstCommit,firstCommiter , repoPath);
        codeTraceJavaApplication.graphConstructionBaseOneCommit(curProject);

//        commitList.remove(0); //去除首个版本
//        Collections.reverse(commitList);  //翻转列表

        String[] commitId = (String[]) commitList.toArray(new String[commitList.size()]);
//        String[] commitIdTime = (String[]) commitListTime.toArray(new String[commitListTime.size()]);
        for(int i=0;i<commitList.size();i++){
            System.out.println(commitId[i]);
            System.out.println(commitListTime.get(i));

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





    }







}
