package org.example;

public class Edge {
    private int firstNode;
    private int secondNode;
    private double flow;

    Edge(int firstNode, int secondNode, double flow) {
        this.firstNode = firstNode;
        this.secondNode = secondNode;
        this.flow = flow;
    }

    public int getFirstNode() {
        return firstNode;
    }

    public void setFirstNode(int firstNode) {
        this.firstNode = firstNode;
    }

    public int getSecondNode() {
        return secondNode;
    }

    public void setSecondNode(int secondNode) {
        this.secondNode = secondNode;
    }

    public double getFlow() {
        return flow;
    }

    public void setFlow(double flow) {
        this.flow = flow;
    }
}
