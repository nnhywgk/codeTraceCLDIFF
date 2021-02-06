package main.GitAction;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Ref;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FindBranch {
    public FindBranch(String repoPath){
        try {
            Collection<Ref> refList;
            refList = Git.lsRemoteRepository().setRemote(repoPath).call();
            List<String> branchnameList = new ArrayList<>(4);
            for (Ref ref : refList) {
                String refName = ref.getName();
                if (refName.startsWith("refs/heads/")) {                       //需要进行筛选
                    String branchName = refName.replace("refs/heads/", "");
                    branchnameList.add(branchName);
                }
            }

            System.out.println("共用分支" + branchnameList.size() + "个");
            branchnameList.forEach(System.out::println);
        } catch (Exception e) {
            System.out.println("error");
        }    }
}
