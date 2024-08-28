package org.example;

import java.util.ArrayList;

public class Node {
    private int nodeLabel; // Glasets nummer
    private double volume; // Glasets volym
    private final double MAX_VOLUME = 400; // Max volym 400 ml

    private double fillSpeed;

    Node (int nodeLabel) {
        this.nodeLabel = nodeLabel;
    }

    public int getNodeLabel() {
        return nodeLabel;
    }

    public void setNodeLabel(int nodeLabel) {
        this.nodeLabel = nodeLabel;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public double getMAX_VOLUME() {
        return MAX_VOLUME;
    }

    public void setFillSpeed(double fillSpeed) {
        this.fillSpeed = fillSpeed;
    }

    public double getFillSpeed() {
        return fillSpeed;
    }
}
