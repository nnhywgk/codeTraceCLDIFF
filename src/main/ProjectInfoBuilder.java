/**
 * @description:
 * @author: fancying
 * @create: 2019-05-26 22:26
 **/
package main;

import main.GraphInfo.ClassInfo;
import main.util.DirExplorer;
import main.util.Extractor;

import java.io.File;
import java.util.*;

public class ProjectInfoBuilder {

    private String projectName;
    private String commit;
    private String committer;
    private Set<ClassInfo> classInfos;
//    private Set<PackageInfo> packageInfos;

    public ProjectInfoBuilder(String projectName, String commit, String committer, String root) {
        this.projectName = projectName;
        this.commit = commit;
        this.committer = committer;
//        packageInfos = new HashSet<>();
        File file = new File(root);
        analyze(listJavaFiles(file));
    }

    public ProjectInfoBuilder(String projectName, String commit, String committer, List<String> fileList) {
        this.projectName = projectName;
        this.commit = commit;
        this.committer = committer;
//        packageInfos = new HashSet<>();
        analyze(fileList);
    }


    private void analyze(List<String> fileList) {
        for (String path : fileList) {
            Extractor extractor = new Extractor(path, projectName);
            String packageName = extractor.getPackageName();
            classInfos = extractor.getClassInfos();
//            PackageInfo packageInfo ;
//            Map<String, PackageInfo> packageInfoMap;
//            packageInfoMap = new HashMap<>();
//
//            if (! packageInfoMap.containsKey(packageName)) {
//                if (packageName == null)
//                    continue;
//                packageInfo = new PackageInfo(moduleName, packageName, extractor.getClassInfos());
//                packageInfoMap.put(packageName, packageInfo);
//            } else {
//                packageInfo = packageInfoMap.get(packageName);
//                for (ClassInfo classInfo : extractor.getClassInfos()) {
//                    packageInfo.getClassInfos().add(classInfo);
//                }
//            }
        }
//        int n = 0;
//        for (Map<String, PackageInfo> packageInfoMap : moduleInfos.values()) {
//            n += packageInfoMap.values().size();
//            packageInfos.addAll(packageInfoMap.values());
//            if (n!=packageInfos.size())
//                System.out.println("eeee");
//        }


    }

    private List<String> listJavaFiles(File projectDir) {
        List<String> pathList = new ArrayList<>();
        new DirExplorer((level, path, file) -> path.endsWith(".java"),
                (level, path, file) -> pathList.add(file.getAbsolutePath())).explore(projectDir);
        return pathList;
    }

    public List<String> createCqlList(Boolean isNode) {
        List<String> cqlList = new ArrayList<>();

        String label = "`" + projectName + "`:`"+ committer + "`:`" + commit + "':'";
        for (ClassInfo classInfo : classInfos) {
            cqlList.addAll(isNode ? classInfo.toNeo4jNode(label) : classInfo.toNeo4jRelation(label , label));
        }

//        for (Map.Entry<String, Map<String, PackageInfo>> packageInfoEntry : moduleInfos.entrySet()) {
//            String newLabel = label + packageInfoEntry.getKey() + "`";
//            for (PackageInfo packageInfo : packageInfoEntry.getValue().values()) {
//                cqlList.addAll(isNode ? packageInfo.toNeo4jNode(newLabel) : packageInfo.toNeo4jRelation(newLabel , newLabel));
//            }
//        }
        return cqlList;
    }

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

    public String getProjectName() {
        return projectName;
    }

    public String getCommit() {
        return commit;
    }

    public String getCommitter() {
        return committer;
    }

//    public Map<String, PackageInfo> getPackageInfosByModuleName(String moduleName) {
//        return moduleInfos.get(moduleName);
//    }

//    public Map<String, Map<String, PackageInfo>> getModuleInfos() {
//        return moduleInfos;
//    }


//    public Set<PackageInfo> getPackageInfos() {
//        return packageInfos;
//    }

}