/**
 * @description:
 * @author: fancying
 * @create: 2019-05-26 20:51
 **/
package main.GraphInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MethodInfo implements Neo4jInformation{

    private String signature;

    private String modifier;
    private String primitiveType;
    private int begin;
    private int end;

    @Override
    public List<String> toNeo4jNode(String label) {
        List<String> cqlNodeList = new ArrayList<>();
//        String cql = "CREATE (:" + label + ":method" + "{" +
//                "signature:\"" + signature.replace("\"", "\\\"") +
//                "\",modifier:\"" + modifier +
//                "\",primitiveType:\""+ primitiveType +
//                "\",begin:" + begin +
//                ",end:" + end  + "})";
        String cql = "CREATE (method:method {" +
                "signature:\"" + signature.replace("\"", "/") +
                "\",modifier:\"" + modifier +
                "\",primitiveType:\""+ primitiveType +
                "\",begin:" + begin +
                ",end:" + end  + "})";
        cqlNodeList.add(cql);
/*        for (StatementInfo statementInfo : statementInfos) {
            cqlNodeList.addAll(statementInfo.toNeo4jNode(label + ":`" + signature + "`"));
        }*/
        return cqlNodeList;
    }

    @Override
    public List<String> toNeo4jRelation(String node1_label_name, String node2_label_name) {
        List<String> cqlRelationList = new ArrayList<>();
        String cql = "MATCH (p:method:" + node1_label_name + "),(c:statement:" + node2_label_name + ":`"+ signature +
                "`)where p.signature = \""+ signature.replace("\"", "\\\"")   +
                "\" CREATE (p)-[r:include{}]->(c)" ;
        cqlRelationList.add(cql);
        return cqlRelationList;
    }


    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public String getPrimitiveType() {
        return primitiveType;
    }

    public void setPrimitiveType(String primitiveType) {
        this.primitiveType = primitiveType;
    }

    public int getBegin() {
        return begin;
    }

    public void setBegin(int begin) {
        this.begin = begin;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }
}