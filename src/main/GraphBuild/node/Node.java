/**
 * @description: node in neo4j which include basic element
 * @author: fancying
 * @create: 2019-05-25 11:48
 **/
package main.GraphBuild.node;

import main.GraphBuild.relation.Edge;
import org.neo4j.graphdb.Label;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class Node implements Label {
    private String name;

    private int beginLine;
    private int endLine;

    private String type;

    private LinkedList<Node> children;
    private LinkedList<Edge> edges;

    private boolean isFakeLeaf = false;

    public Node() {
        children = new LinkedList<>();
        edges = new LinkedList<>();
    }

    public Set<Node> getLeaves(){
        Set<Node> leaves = new HashSet<>();
        if(isFakeLeaf || children == null || children.size() == 0){
            leaves.add(this);
            this.setFakeLeaf(false);
            return leaves;
        }
        for (Node c: children) {
            leaves.addAll(c.getLeaves());
        }
        return leaves;
    }

    public void addChild(Node node, Edge edge) {
        if(node == null){
            return;
        }

        if(children == null || children.size() == 0){
            children = new LinkedList<>();
            edges = new LinkedList<>();
        }

        children.addLast(node);
        edges.addLast(edge);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(type).append(":").append(name).append(" - ");
        for (int i = 0; i < children.size(); i++) {
            sb.append(children.get(i));
        }
        return sb.toString();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LinkedList<Node> getChildren() {
        return children;
    }

    public void setChildren(LinkedList<Node> children) {
        this.children = children;
    }

    public LinkedList<Edge> getEdges() {
        return edges;
    }

    public void setEdges(LinkedList<Edge> edges) {
        this.edges = edges;
    }

    public int getBeginLine() {
        return beginLine;
    }

    public void setBeginLine(int beginLine) {
        this.beginLine = beginLine;
    }

    public int getEndLine() {
        return endLine;
    }

    public void setEndLine(int endLine) {
        this.endLine = endLine;
    }

    public boolean isFakeLeaf() {
        return isFakeLeaf;
    }

    public void setFakeLeaf(boolean fakeLeaf) {
        isFakeLeaf = fakeLeaf;
    }

    public void setName(String name){
        this.name = name;
    }

    @Override
    public String name() {
        return name;
    }
}