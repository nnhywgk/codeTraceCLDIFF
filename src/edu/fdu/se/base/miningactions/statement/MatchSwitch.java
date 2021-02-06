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
import edu.fdu.se.base.miningchangeentity.statement.SwitchChangeEntity;

import java.util.ArrayList;
import java.util.List;

public class MatchSwitch {
	
	public static void matchSwitch(MiningActionData fp, Action a){
		ChangePacket changePacket = new ChangePacket();
		List<Action> subActions = new ArrayList<>();
		if(!BasicTreeTraversal.traverseWhenActionIsMove(a,subActions,changePacket,true)){
			DefaultUpDownTraversal.traverseSwitchStatements(a,subActions,changePacket);
		}
		ClusteredActionBean mBean = new ClusteredActionBean(ChangeEntityDesc.StageITraverseType.TRAVERSE_UP_DOWN,a,subActions,changePacket);
		SwitchChangeEntity code = new SwitchChangeEntity(mBean);
		code.stageIIBean.setEntityCreationStage(ChangeEntityDesc.StageIIGenStage.ENTITY_GENERATION_STAGE_GT_UD);
		code.stageIIBean.setGranularity(ChangeEntityDesc.StageIIGranularity.GRANULARITY_STATEMENT);
		code.stageIIBean.setChangeEntity(ChangeEntityDesc.StageIIENTITY.ENTITY_SWITCH_STMT);
		code.stageIIBean.setOpt(ChangeEntityDesc.getChangeEntityDescString(a));
//		code.stageIIBean.setOpt2(null);
		code.stageIIBean.setSubEntity(null);
		code.stageIIBean.setLineRange(code.lineRange.toString());
		code.stageIIBean.setLocation(AstRelations.getLocationString(a.getNode()));
		fp.setActionTraversedMap(subActions);
		fp.addOneChangeEntity(code);
	}

	public static void matchSwitchCase(MiningActionData fp, Action a){
		ChangePacket changePacket = new ChangePacket();
		List<Action> subActions = new ArrayList<>();
		if(!BasicTreeTraversal.traverseWhenActionIsMove(a,subActions,changePacket,false)){
			DefaultUpDownTraversal.traverseSwitchCase(a, subActions, changePacket);
		}
		ClusteredActionBean mBean = new ClusteredActionBean(ChangeEntityDesc.StageITraverseType.TRAVERSE_UP_DOWN,a,subActions,changePacket);
		SwitchChangeEntity code = new SwitchChangeEntity(mBean);
		code.stageIIBean.setEntityCreationStage(ChangeEntityDesc.StageIIGenStage.ENTITY_GENERATION_STAGE_GT_UD);
		code.stageIIBean.setGranularity(ChangeEntityDesc.StageIIGranularity.GRANULARITY_STATEMENT);
		code.stageIIBean.setChangeEntity(ChangeEntityDesc.StageIIENTITY.ENTITY_SWITCH_STMT);
		code.stageIIBean.setOpt(ChangeEntityDesc.StageIIOpt.OPT_CHANGE);
//		code.stageIIBean.setOpt2(OperationTypeConstants.getChangeEntityDescString(a));
		code.stageIIBean.setLineRange(code.lineRange.toString());
		code.stageIIBean.setLocation(AstRelations.getLocationString(a.getNode()));
		if(a.getNode().getChildren() == null || a.getNode().getChildren().size()==0){
			code.stageIIBean.setSubEntity(ChangeEntityDesc.StageIISub.SUB_SWITCH_CASE_DEFAULT);
		}else {
			code.stageIIBean.setSubEntity(ChangeEntityDesc.StageIISub.SUB_SWITCH_CASE);
		}
		fp.setActionTraversedMap(subActions);
		fp.addOneChangeEntity(code);

	}

	public static void matchSwitchNewEntity(MiningActionData fp,Action a, Tree queryFather,int treeType,Tree traverseFather){
		ChangePacket changePacket = new ChangePacket();
		List<Action> subActions = new ArrayList<>();
		if(!BasicTreeTraversal.traverseWhenActionIsMove(a,subActions,changePacket,false)){
			DefaultDownUpTraversal.traverseSwitchCondition(traverseFather,subActions,changePacket);
		}
		fp.setActionTraversedMap(subActions);
		ClusteredActionBean mBean =  new ClusteredActionBean(ChangeEntityDesc.StageITraverseType.TRAVERSE_DOWN_UP,a,subActions,changePacket,queryFather,treeType);
		SwitchChangeEntity code = new SwitchChangeEntity(mBean);

		code.stageIIBean.setEntityCreationStage(ChangeEntityDesc.StageIIGenStage.ENTITY_GENERATION_STAGE_GT_DUD);
		code.stageIIBean.setGranularity(ChangeEntityDesc.StageIIGranularity.GRANULARITY_STATEMENT);
		if(a instanceof Move){
			code.stageIIBean.setOpt(ChangeEntityDesc.StageIIOpt.OPT_CHANGE_MOVE);
			code.stageIIBean.setChangeEntity(((Tree)a.getNode()).getAstClass().getSimpleName());
		}else {
			code.stageIIBean.setOpt(ChangeEntityDesc.StageIIOpt.OPT_CHANGE);
			code.stageIIBean.setChangeEntity(ChangeEntityDesc.StageIIENTITY.ENTITY_SWITCH_STMT);
		}
//		code.stageIIBean.setOpt2(null);
		code.stageIIBean.setSubEntity(ChangeEntityDesc.StageIISub.SUB_CONDITION);
		code.stageIIBean.setLineRange(code.lineRange.toString());
		code.stageIIBean.setLocation(AstRelations.getLocationString(a.getNode()));
		fp.addOneChangeEntity(code);

	}
	public static void matchSwitchCurrEntity(MiningActionData fp, Action a, ChangeEntity changeEntity,Tree traverseFather){
		ChangePacket changePacket = changeEntity.clusteredActionBean.changePacket;
		List<Action> actions = changeEntity.clusteredActionBean.actions;
		List<Action> newActions = new ArrayList<>();
		if(!BasicTreeTraversal.traverseWhenActionIsMove(a,actions,changePacket,false)){
			DefaultDownUpTraversal.traverseSwitchCondition(traverseFather,actions,changePacket);
		}
		for(Action tmp:newActions){
			if(fp.mGeneratingActionsData.getAllActionMap().get(tmp)==1){
				continue;
			}
			actions.add(tmp);
		}
//		changeEntity.linkBean.addAppendedActions(newActions);
		fp.setActionTraversedMap(newActions);
	}


	public static void matchSwitchCaseNewEntity(MiningActionData fp, Action a, Tree queryFather,int treeType,Tree traverseFather){
		ChangePacket changePacket = new ChangePacket();
		List<Action> subActions = new ArrayList<>();
		if(!BasicTreeTraversal.traverseWhenActionIsMove(a,subActions,changePacket,false)){
			DefaultDownUpTraversal.traverseFatherNodeGetSameNodeActions(traverseFather,subActions,changePacket);
		}
		fp.setActionTraversedMap(subActions);
		ClusteredActionBean mBean =  new ClusteredActionBean(ChangeEntityDesc.StageITraverseType.TRAVERSE_DOWN_UP,a,subActions,changePacket,queryFather,treeType);
		SwitchChangeEntity code = new SwitchChangeEntity(mBean);
		code.stageIIBean.setEntityCreationStage(ChangeEntityDesc.StageIIGenStage.ENTITY_GENERATION_STAGE_GT_DUD);
		code.stageIIBean.setGranularity(ChangeEntityDesc.StageIIGranularity.GRANULARITY_STATEMENT);
		if(a instanceof Move){
			code.stageIIBean.setOpt(ChangeEntityDesc.StageIIOpt.OPT_CHANGE_MOVE);
			code.stageIIBean.setChangeEntity(((Tree)a.getNode()).getAstClass().getSimpleName());
		}else {
			code.stageIIBean.setOpt(ChangeEntityDesc.StageIIOpt.OPT_CHANGE);
			code.stageIIBean.setChangeEntity(ChangeEntityDesc.StageIIENTITY.ENTITY_SWITCH_STMT);
		}
//		code.stageIIBean.setOpt2(null);
		code.stageIIBean.setSubEntity(ChangeEntityDesc.StageIISub.SUB_SWITCH_CASE);
		code.stageIIBean.setLineRange(code.lineRange.toString());
		code.stageIIBean.setLocation(AstRelations.getLocationString(a.getNode()));
		fp.addOneChangeEntity(code);
	}

	public static void matchSwitchCaseCurrEntity(MiningActionData fp, Action a, ChangeEntity changeEntity,Tree traverseFather){
		ChangePacket changePacket = changeEntity.clusteredActionBean.changePacket;
		List<Action> actions = changeEntity.clusteredActionBean.actions;
		List<Action> newActions = new ArrayList<>();
		if(!BasicTreeTraversal.traverseWhenActionIsMove(a,newActions,changePacket,false)){
			DefaultDownUpTraversal.traverseFatherNodeGetSameNodeActions(traverseFather,newActions,changePacket);
		}
		for(Action tmp:newActions){
			if(fp.mGeneratingActionsData.getAllActionMap().get(tmp)==1){
				continue;
			}
			actions.add(tmp);
		}
//		changeEntity.linkBean.addAppendedActions(newActions);
		fp.setActionTraversedMap(newActions);
	}
}
