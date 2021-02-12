package main.CLDiffAnalysis;

import main.util.DirExplorer;
import org.apache.commons.io.FileUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnalyseMeta {

    private String commitId;
    private Map<JSONObject, String> diffFileAction = new HashMap<>();


    public AnalyseMeta(String outputDir, String commitId){

        this.commitId = commitId;
        System.out.println(outputDir + "/" + commitId);
        File file = new File(outputDir + "/" + commitId);
        String metaPath = findMetaFile(file);
        JSONArray fileInfoJsonArray;
        JSONArray preCommits;
        try {
            String input = FileUtils.readFileToString(new File(metaPath), "UTF-8");
            JSONObject metaInfoJSON = JSONObject.parseObject(input);
            fileInfoJsonArray = metaInfoJSON.getJSONArray("files");
            JSONArray actions = metaInfoJSON.getJSONArray("actions");
            for (int i = 0; i < fileInfoJsonArray.size(); i++) {
                if (fileInfoJsonArray.getJSONObject(i).getString("file_name").contains(".java")) {
                    diffFileAction.put(fileInfoJsonArray.getJSONObject(i), actions.getString(i));
                }
            }

//            preCommits = metaInfoJSON.getJSONArray("parents");
//            for (int i = 0; i < preCommits.size(); i++) {
//                String preCommit = preCommits.getString(i);
//                String preCommitter = jGitHelper.getAuthorName(preCommit);
//                jGitHelper.checkout(preCommit);
//                ProjectInfoBuilder preProjectInfo = new ProjectInfoBuilder(projectName, preCommit, preCommitter, repoPath);
//                AnalyzeDiffFile analyzeDiffFile = new AnalyzeDiffFile(preProjectInfo, curProjectInfo);
//                jGitHelper.checkout(commitId);
//                notChangedFileList = listClasses(new File(repoPath));
//                analyzeMetaInfoFiles(diffFileAction, analyzeDiffFile, preCommit, metaPath);
//
//                analyzeDiffFile.packageRelationAnalyze(notChangedFileList);
//                cqlSet.addAll(analyzeDiffFile.getCqlSet());
//            }
//            return new ArrayList<>(cqlSet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map<JSONObject, String> gitMetaInfo(){
        return diffFileAction;
    }

    private String findMetaFile(File projectDir) {
        List<String> pathList = new ArrayList<>();
        new DirExplorer((level, path, file) -> path.endsWith("meta.json"),
                (level, path, file) -> pathList.add(file.getAbsolutePath())).explore(projectDir);
        return pathList.get(0);
    }
}
