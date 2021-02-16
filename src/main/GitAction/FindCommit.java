package main.GitAction;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class FindCommit {
    private static ArrayList commitList =  new ArrayList<String>();  //初始号数组

    public FindCommit(String repoPath) throws GitAPIException, IOException {
        Repository repo = new FileRepository(repoPath+ "/.git");
        Git git = new Git(repo);
        RevWalk walk = new RevWalk(repo);

        List<Ref> branches = git.branchList().call();



        for (Ref branch : branches) {
            String branchName = branch.getName();

//            System.out.println("Commits of branch: " + branch.getName());
//            System.out.println("-------------------------------------");

            Iterable<RevCommit> commits = git.log().all().call();

            for (RevCommit commit : commits) {
                boolean foundInThisBranch = false;

                RevCommit targetCommit = walk.parseCommit(repo.resolve(
                        commit.getName()));
                for (Map.Entry<String, Ref> e : repo.getAllRefs().entrySet()) {
                    if (e.getKey().startsWith(Constants.R_HEADS)) {
                        if (walk.isMergedInto(targetCommit, walk.parseCommit(
                                e.getValue().getObjectId()))) {
                            String foundInBranch = e.getValue().getName();
                            if (branchName.equals(foundInBranch)) {
                                foundInThisBranch = true;
                                break;
                            }
                        }
                    }
                }

                if (foundInThisBranch) {
//                    System.out.println(commit.getName());


                    commitList.add(commit.getName()); //添加commit

//                    if(commit.getParents().length==0) {}
//                    else if(commit.getParents().length==1){
//                        System.out.println(commit.getParents()[0]);
//                    }
//                    else {
//                        System.out.print(commit.getParents()[0]+" ");
//                        System.out.println(commit.getParents()[1]);
//
//                    }
//
//                    System.out.println(commit.getAuthorIdent().getName());
//                    System.out.println(new Date(commit.getCommitTime() * 1000L));
//                    System.out.println(commit.getFullMessage());
                }
            }
        }

        commitList  = single(commitList);

    }

    public static ArrayList getCommitList(){
        return commitList;
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
}
