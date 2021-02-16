package main.ParseFile;

import main.GraphInfo.ClassInfo;
import main.util.DirExplorer;
import main.util.Extractor;

import java.io.File;
import java.util.*;

public class ParseFile {
    private String projectName;
    private String commit;
    private String committer;
    private Set<ClassInfo> classInfos;

    public ParseFile(String projectName, String commit, String committer, String root) {
        this.projectName = projectName;
        this.commit = commit;
        this.committer = committer;
        File file = new File(root);
        analyze(listJavaFiles(file));
    }

    private void analyze(List<String> fileList) {
        for (String path : fileList) {
            Extractor extractor = new Extractor(path, projectName);
            String packageName = extractor.getPackageName();
            classInfos = extractor.getClassInfos();
        }
    }


    //寻找路径中的Java文件
    private List<String> listJavaFiles(File projectDir) {
        List<String> pathList = new ArrayList<>();
        new DirExplorer((level, path, file) -> path.endsWith(".java"),
                (level, path, file) -> pathList.add(file.getAbsolutePath())).explore(projectDir);
        return pathList;
    }

//    public List<String> createCqlList(Boolean isNode) {
//        List<String> cqlList = new ArrayList<>();
//
//        String label = "`" + projectName + "`:`"+ committer + "`:`" + commit + "`:`";
//
//
//        for (Map.Entry<String, Map<String, PackageInfo>> packageInfoEntry : moduleInfos.entrySet()) {
//            String newLabel = label + packageInfoEntry.getKey() + "`";
//            for (PackageInfo packageInfo : packageInfoEntry.getValue().values()) {
//                cqlList.addAll(isNode ? packageInfo.toNeo4jNode(newLabel) : packageInfo.toNeo4jRelation(newLabel , newLabel));
//            }
//        }
//        return cqlList;
//    }

//    public Map<String, String> createCqlMapStat() {
//        Map<String,String> cqlListPara = new HashMap<>();
//        String label = "`" + projectName + "`:`"+ committer + "`:`" + commit + "`:`";
//        for (Map.Entry<String, Map<String, PackageInfo>> packageInfoEntry : moduleInfos.entrySet()) {
//            String newLabel = label + packageInfoEntry.getKey() + "`";
//            for (PackageInfo packageInfo : packageInfoEntry.getValue().values()) {
//                cqlListPara.putAll(packageInfo.StatToNeo4jNode(newLabel));
//            }
//        }
//        return cqlListPara;
//    }



    public Set<ClassInfo> getClassInfos() {
        return classInfos;
    }

    public void setClassInfos(Set<ClassInfo> classInfos) {
        this.classInfos = classInfos;
    }
}
