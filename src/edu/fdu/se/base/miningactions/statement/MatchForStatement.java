package edu.fdu.se.base.miningactions.statement;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Move;
import com.github.gumtreediff.tree.Tree;
import edu.fdu.se.base.miningactions.bean.ChangePacket;
import edu.fdu.se.base.miningactions.bean.MiningActionData;
import edu.fdu.se.base.miningactions.util.AstRelations;
import edu.fdu.se.base.miningactions.util.BasicTreeTraversal;
import edu.fdu.se.base.miningactions.util.DefaultDownUpTraversal;
import edu.fdu.se.base.miningactions.util.DefaultUpDownTraversal;
import edu.fdu.se.base.miningchangeentity.ClusteredActionBean;
import edu.fdu.se.base.miningchangeentity.base.ChangeEntity;
import edu.fdu.se.base.miningchangeentity.base.ChangeEntityDesc;
import edu.fdu.se.base.miningchangeentity.statement.ForChangeEntity;

import java.util.ArrayList;
import java.util.List;

public class MatchForStatement {
    public static void matchForStatement(MiningActionData fp, Action a){
        ChangePacket changePacket = new ChangePacket();
        List<Action> subActions = new ArrayList<>();
        if(!BasicTreeTraversal.traverseWhenActionIsMove(a,subActions,changePacket,true)){
            DefaultUpDownTraversal.traverseIf(a,subActions,changePacket);
        }
        ClusteredActionBean mBean = new ClusteredActionBean(ChangeEntityDesc.StageITraverseType.TRAVERSE_UP_DOWN,a,subActions,changePacket);
        ForChangeEntity code = new ForChangeEntity(mBean);
        code.stageIIBean.setEntityCreationStage(ChangeEntityDesc.StageIIGenStage.ENTITY_GENERATION_STAGE_GT_UD);
        code.stageIIBean.setGranularity(ChangeEntityDesc.StageIIGranularity.GRANULARITY_STATEMENT);
        code.stageIIBean.setOpt(ChangeEntityDesc.getChangeEntityDescString(a));
        code.stageIIBean.setChangeEntity(ChangeEntityDesc.StageIIENTITY.ENTITY_FOR_STMT);
//        code.stageIIBean.setOpt2(null);// 暂时不设置
        code.stageIIBean.setSubEntity(null);
        code.stageIIBean.setLineRange(code.lineRange.toString());
        code.stageIIBean.setLocation(AstRelations.getLocationString(a.getNode()));
        fp.setActionTraversedMap(subActions);
        fp.addOneChangeEntity(code);
    }

    public static void matchEnhancedForStatement(MiningActionData fp, Action a){
        ChangePacket changePacket = new ChangePacket();
        List<Action> subActions = new ArrayList<>();
        if(!BasicTreeTraversal.traverseWhenActionIsMove(a,subActions,changePacket,true)){
            DefaultUpDownTraversal.traverseIf(a,subActions,changePacket);
        }
        ClusteredActionBean mBean = new ClusteredActionBean(ChangeEntityDesc.StageITraverseType.TRAVERSE_UP_DOWN,a,subActions,changePacket);
        ForChangeEntity code = new ForChangeEntity(mBean);
        code.stageIIBean.setEntityCreationStage(ChangeEntityDesc.StageIIGenStage.ENTITY_GENERATION_STAGE_GT_UD);
        code.stageIIBean.setGranularity(ChangeEntityDesc.StageIIGranularity.GRANULARITY_STATEMENT);
        code.stageIIBean.setOpt(ChangeEntityDesc.getChangeEntityDescString(a));
        code.stageIIBean.setChangeEntity(ChangeEntityDesc.StageIIENTITY.ENTITY_ENHANCED_FOR_STMT);
//        code.stageIIBean.setOpt2(null);// 暂时不设置
        code.stageIIBean.setSubEntity(null);
        code.stageIIBean.setLineRange(code.lineRange.toString());
        code.stageIIBean.setLocation(AstRelations.getLocationString(a.getNode()));
        fp.addOneChangeEntity(code);
        fp.setActionTraversedMap(subActions);
    }

    public static void matchForConditionChangeNewEntity(MiningActionData fp, Action a, Tree queryFather,int treeType, Tree traverseFather) {
        ChangePacket changePacket = new ChangePacket();
        List<Action> sameEdits = new ArrayList<>();
        if(!BasicTreeTraversal.traverseWhenActionIsMove(a,sameEdits,changePacket,false)){
            DefaultDownUpTraversal.traverseIfPredicate(traverseFather, sameEdits, changePacket);
        }
        fp.setActionTraversedMap(sameEdits);
        ClusteredActionBean mBean = new ClusteredActionBean(ChangeEntityDesc.StageITraverseType.TRAVERSE_DOWN_UP,a,sameEdits,changePacket,queryFather,treeType);
        ForChangeEntity code = new ForChangeEntity(mBean);
        fp.addOneChangeEntity(code);
        code.stageIIBean.setEntityCreationStage(ChangeEntityDesc.StageIIGenStage.ENTITY_GENERATION_STAGE_GT_DUD);
        code.stageIIBean.setGranularity(ChangeEntityDesc.StageIIGranularity.GRANULARITY_STATEMENT);
        if(a instanceof Move){
            code.stageIIBean.setOpt(ChangeEntityDesc.StageIIOpt.OPT_CHANGE_MOVE);
            code.stageIIBean.setChangeEntity(((Tree)a.getNode()).getAstClass().getSimpleName());
        }else {
            code.stageIIBean.setOpt(ChangeEntityDesc.StageIIOpt.OPT_CHANGE);
            code.stageIIBean.setChangeEntity(ChangeEntityDesc.StageIIENTITY.ENTITY_FOR_STMT);
        }
//        code.stageIIBean.setOpt2(null);// 暂时不设置
        code.stageIIBean.setSubEntity(null);
        code.stageIIBean.setLineRange(code.lineRange.toString());
        code.stageIIBean.setLocation(AstRelations.getLocationString(a.getNode()));

    }

    public static void matchEnhancedForConditionChangeNewEntity(MiningActionData fp, Action a, Tree queryFather,int treeType, Tree traverseFather) {
        ChangePacket changePacket = new ChangePacket();
        List<Action> sameEdits = new ArrayList<>();
        if(!BasicTreeTraversal.traverseWhenActionIsMove(a,sameEdits,changePacket,false)){
            DefaultDownUpTraversal.traverseIfPredicate(traverseFather, sameEdits, changePacket);
        }
        fp.setActionTraversedMap(sameEdits);

        ClusteredActionBean mBean = new ClusteredActionBean(ChangeEntityDesc.StageITraverseType.TRAVERSE_DOWN_UP,a,sameEdits,changePacket,queryFather,treeType);
        ForChangeEntity code = new ForChangeEntity(mBean);
        fp.addOneChangeEntity(code);
        code.stageIIBean.setEntityCreationStage(ChangeEntityDesc.StageIIGenStage.ENTITY_GENERATION_STAGE_GT_DUD);
        code.stageIIBean.setGranularity(ChangeEntityDesc.StageIIGranularity.GRANULARITY_STATEMENT);
        if(a instanceof Move){
            code.stageIIBean.setOpt(ChangeEntityDesc.StageIIOpt.OPT_CHANGE_MOVE);
            code.stageIIBean.setChangeEntity(((Tree)a.getNode()).getAstClass().getSimpleName());
        }else {
            code.stageIIBean.setOpt(ChangeEntityDesc.StageIIOpt.OPT_CHANGE);
            code.stageIIBean.setChangeEntity(ChangeEntityDesc.StageIIENTITY.ENTITY_ENHANCED_FOR_STMT);
        }
//        code.stageIIBean.setOpt2(null);// 暂时不设置
        code.stageIIBean.setSubEntity(null);
        code.stageIIBean.setLineRange(code.lineRange.toString());
        code.stageIIBean.setLocation(AstRelations.getLocationString(a.getNode()));
    }

    public static void matchForConditionChangeCurrEntity(MiningActionData fp, Action a, ChangeEntity changeEntity,Tree traverseFather){
        ChangePacket changePacket = changeEntity.clusteredActionBean.changePacket;
        List<Action> actions = changeEntity.clusteredActionBean.actions;
        List<Action> newActions = new ArrayList<>();
        if(!BasicTreeTraversal.traverseWhenActionIsMove(a,newActions,changePacket,false)){
            DefaultDownUpTraversal.traverseIfPredicate(traverseFather,newActions,changePacket);
        }
        for(Action tmp:newActions){
            if(fp.mGeneratingActionsData.getAllActionMap().get(tmp)==1){
                continue;
            }
            actions.add(tmp);
        }
//        changeEntity.linkBean.addAppendedActions(newActions);
        fp.setActionTraversedMap(newActions);

    }

    public static void matchEnhancedForConditionChangeCurrEntity(MiningActionData fp, Action a, ChangeEntity changeEntity,Tree traverseFather){
        ChangePacket changePacket = changeEntity.clusteredActionBean.changePacket;
        List<Action> actions = changeEntity.clusteredActionBean.actions;
        List<Action> newActions = new ArrayList<>();
        if(!BasicTreeTraversal.traverseWhenActionIsMove(a,newActions,changePacket,false)){
            DefaultDownUpTraversal.traverseIfPredicate(traverseFather,newActions,changePacket);
        }
        for(Action tmp:newActions){
            if(fp.mGeneratingActionsData.getAllActionMap().get(tmp)==1){
                continue;
            }
            actions.add(tmp);
        }
//        changeEntity.linkBean.addAppendedActions(newActions);
        fp.setActionTraversedMap(newActions);
    }
}
