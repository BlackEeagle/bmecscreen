/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bmec.bmecscreen.config;

/**
 *
 * @author Thom
 */
public class VncConfig {

    private String path;

    private Resolution viewerResolution;

    private Resolution serverResolution;

    private int initTop;

    private int initLeft;

    private int stepSize;

    public VncConfig() {
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Resolution getViewerResolution() {
        return viewerResolution;
    }

    public void setViewerResolution(Resolution viewerResolution) {
        this.viewerResolution = viewerResolution;
    }

    public Resolution getServerResolution() {
        return serverResolution;
    }

    public void setServerResolution(Resolution serverResolution) {
        this.serverResolution = serverResolution;
    }

    public int getInitTop() {
        return initTop;
    }

    public void setInitTop(int initTop) {
        this.initTop = initTop;
    }

    public int getInitLeft() {
        return initLeft;
    }

    public void setInitLeft(int initLeft) {
        this.initLeft = initLeft;
    }

    public int getStepSize() {
        return stepSize;
    }

    public void setStepSize(int stepSize) {
        this.stepSize = stepSize;
    }

    @Override
    public String toString() {
        return "VncConfig{" + "path=" + path + ", viewerResolution=" + viewerResolution + ", serverResolution=" + serverResolution + ", initTop=" + initTop + ", initLeft=" + initLeft + ", stepSize=" + stepSize + '}';
    }

}
