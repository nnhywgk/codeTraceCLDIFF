/**
 * @description:
 * @author: fancying
 * @create: 2019-05-26 20:38
 **/
package main.GraphInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassInfo implements Neo4jInformation{

    private String className;
    private String modifier;
    private String filePath;
    private int begin;
    private int end;
    private List<String> extendedList;
    private List<String> implementedList;
    private List<MethodInfo> methodInfos;


    @Override
    public List<String> toNeo4jNode(String label) {
        List<String> cqlNodeList = new ArrayList<>();
//        String cql = "CREATE (`" + className + "`:" + label + ":class:`" + filePath + "`{" +
//                "className:\"" + className +
//                "\",filePath:\"" + filePath +
//                "\",modifier:\"" + modifier +
//                "\",extended:\""+ asString(extendedList) +
//                "\",implemented:\""+ asString(implementedList) +
//                "\"})";

        String cql = "CREATE ("+className+":class{" +
                "className:\"" + className +
                "\",filePath:\"" + filePath +
                "\",modifier:\"" + modifier +
                "\",extended:\""+ asString(extendedList) +
                "\",implemented:\""+ asString(implementedList) +
                "\"})";
        cqlNodeList.add(cql);

        for (MethodInfo methodInfo : methodInfos) {
            cqlNodeList.addAll(methodInfo.toNeo4jNode(label + ":`" + className + "`"));
        }
        return cqlNodeList;
    }

    @Override
    public List<String> toNeo4jRelation(String node1_label_name, String node2_label_name) {
        List<String> cqlRelationList = new ArrayList<>();
        String cql = "MATCH (p:class:" + node1_label_name + "),(c:field:" + node2_label_name + ":`"+ className +
                "`)where p.className = \""+ className   +
                "\" CREATE (p)-[r:include{}]->(c)" ;
        cqlRelationList.add(cql);
        cql = "MATCH (p:class:" + node1_label_name + "),(c:method:" + node2_label_name + ":`"+ className +
                "`)where p.className = \""+ className   +
                "\" CREATE (p)-[r:include{}]->(c)" ;
        cqlRelationList.add(cql);
        for (MethodInfo methodInfo : methodInfos) {
            cqlRelationList.addAll(methodInfo.toNeo4jRelation(node1_label_name + ":`"+ className + "`", node2_label_name));
        }

        return cqlRelationList;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public List<String> getExtendedList() {
        return extendedList;
    }

    public void setExtendedList(List<String> extendedList) {
        this.extendedList = extendedList;
    }

    public List<String> getImplementedList() {
        return implementedList;
    }

    public void setImplementedList(List<String> implementedList) {
        this.implementedList = implementedList;
    }

    public List<MethodInfo> getMethodInfos() {
        return methodInfos;
    }

    public void setMethodInfos(List<MethodInfo> methodInfos) {
        this.methodInfos = methodInfos;
    }

    private String asString(List<String> stringList) {
        StringBuilder sb = new StringBuilder();
        for (String string : stringList) {
            sb.append(string);
            sb.append(" ");
        }
        return sb.toString();
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
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