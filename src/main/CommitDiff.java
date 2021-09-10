package main;

import main.GraphInfo.ClassInfo;
import main.util.Extractor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CommitDiff {
    private String type;

    private String projectName;
    private String commit;
    private String committer;
    private Set<ClassInfo> classInfos;


    public CommitDiff(String type,String path,String projectName) {
        analyze(path,projectName);
        if(type == "add"){

        }
        else if(type == "remove"){

        }
        else{

        }
    }

    private void analyze(String path,String projectName) {
        try {
            Extractor extractor = new Extractor(path, projectName);
//            String packageName = extractor.getPackageName();
            if(classInfos!=null&& classInfos.size()>=0) classInfos.addAll(extractor.getClassInfos());
            else classInfos = extractor.getClassInfos();
        }catch(Exception e){
            System.out.println("123");
        }

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
}


