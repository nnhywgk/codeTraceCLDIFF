package main;

import edu.fdu.se.base.common.Global;
import edu.fdu.se.cldiff.CLDiffLocal;

/**
 * Created by huangkaifeng on 2018/10/11.
 */
public class CLDIFFCmd {

    public static void main(String args[]){
        Global.runningMode = 0;
        String repo = "/Users/weisun/Documents/毕业工具/测试项目/WxJava/.git";
        String commitId = "4759606f3733486c680e93eaf7c1d7fbdb9f668e";
        String outputDir = "/Users/weisun/Documents/毕业工具/测试项目/WxJava-out";
        CLDiffLocal CLDiffLocal = new CLDiffLocal();
        CLDiffLocal.run(commitId,repo,outputDir);
    }
}
