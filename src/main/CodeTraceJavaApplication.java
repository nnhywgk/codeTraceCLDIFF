package main;


import com.alibaba.fastjson.JSONObject;
import edu.fdu.se.base.common.Global;
import edu.fdu.se.cldiff.CLDiffLocal;
import main.CLDiffAnalysis.AnalyseMeta;
import main.GitAction.FindCommit;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import java.io.IOException;
import java.util.*;


//exclude= {DataSourceAutoConfiguration.class} 声明不启动数据库
@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
public class CodeTraceJavaApplication {

    public static void main(String[] args) throws IOException, GitAPIException {

        SpringApplication.run(CodeTraceJavaApplication.class, args);

        String repoPath = "/Users/weisun/Documents/毕业工具/codeTraceCLDIFF/.git";
        String projectName = "codeTraceCLDIFF";

        ArrayList commitList =  new ArrayList<String>();

        FindCommit findCommit = new FindCommit(repoPath);
        commitList = findCommit.getCommitList();


        /**********根据仓库地址查找所有的分支**********/
//		FindBranch findBranch = new FindBranch(repoPath);


        String outputDir = "/Users/weisun/Documents/毕业工具/测试项目/codeTraceCLDIFF-output";
        Global.runningMode = 0;

        /**********执行Cldiff产生meta文件**********/
        //执行cldiff
        CLDiffLocal cLDiffLocal = new CLDiffLocal();
        cLDiffLocal.run("ff515fd998339241c59b9875b4517444d1da0469",repoPath,outputDir);
        //分析meta文件
        AnalyseMeta analyseMeta = new AnalyseMeta(outputDir+ "/" + projectName ,"ff515fd998339241c59b9875b4517444d1da0469");
        Map<JSONObject, String> diffFileAction = analyseMeta.gitMetaInfo();

        for (Map.Entry<JSONObject, String> m : diffFileAction.entrySet()) {
            //遍历meta文件中的结果
            if (! m.getKey().getString("file_name").endsWith(".java")) {
                continue;
            }

        }



//        String[] commitId = (String[]) commitList.toArray(new String[commitList.size()]);
//
//        for(String str:commitId){
//            System.out.println(str);
//            CLDiffLocal CLDiffLocal = new CLDiffLocal();
//            CLDiffLocal.run(str,repoPath,outputDir);
//        }





    }







}
