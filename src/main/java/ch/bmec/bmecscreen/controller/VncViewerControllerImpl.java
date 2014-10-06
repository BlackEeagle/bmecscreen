/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bmec.bmecscreen.controller;

import ch.bmec.bmecscreen.config.VncConfig;
import ch.bmec.bmecscreen.service.configuration.ConfigurationService;
import ch.bmec.bmecscreen.service.rpi.pushbutton.PushbuttonConfiguration;
import ch.bmec.bmecscreen.service.vnc.VncServerService;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Thom
 */
@Component
public class VncViewerControllerImpl implements VncViewerController {

    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    private VncServerService vncServerService;

    private int currentTop;

    private int currentLeft;

    @PostConstruct
    public void setup() {
        currentTop = getConfig().getInitTop();
        currentLeft = getConfig().getInitLeft();
    }

    @Override
    public PushbuttonConfiguration handlePush(PushbuttonConfiguration pushedConfiguration) {

        defineCurrentTopAndLeft(pushedConfiguration);

        vncServerService.applyNewTopLeft(currentTop, currentLeft);

        PushbuttonConfiguration newConfig = getConfigForCurrentTopAndCurrentLeft();

        return newConfig;
    }

    @Override
    public PushbuttonConfiguration currentPushConfig() {
        return getConfigForCurrentTopAndCurrentLeft();
    }

    @Override
    public void reset() {
        setup();
    }

    private PushbuttonConfiguration getConfigForCurrentTopAndCurrentLeft() {

        int maxWidth = getConfig().getServerResolution().getWidth();
        int maxHeight = getConfig().getServerResolution().getHeight();

        int viewWidth = getConfig().getViewerResolution().getWidth();
        int viewHeight = getConfig().getViewerResolution().getHeight();

        int initTop = getConfig().getInitTop();
        int initLeft = getConfig().getInitLeft();

        // screen 2 always disabled (for now)
        PushbuttonConfiguration newConfig = new PushbuttonConfiguration(true, false, true, true, true, true);
        // check top
        if (currentTop == initTop) {
            newConfig.setUp(false);
        } else if (currentTop == initTop + maxHeight - viewHeight) {
            newConfig.setDown(false);
        }
        // check left
        if (currentLeft == initLeft) {
            newConfig.setLeft(false);
        } else if (currentLeft == initLeft + maxWidth - viewWidth) {
            newConfig.setRight(false);
        }
        return newConfig;
    }

    private void defineCurrentTopAndLeft(PushbuttonConfiguration pushedConfiguration) {
        int stepSize = getConfig().getStepSize();

        int newTop = currentTop;
        int newLeft = currentLeft;

        int maxWidth = getConfig().getServerResolution().getWidth();
        int maxHeight = getConfig().getServerResolution().getHeight();

        int viewWidth = getConfig().getViewerResolution().getWidth();
        int viewHeight = getConfig().getViewerResolution().getHeight();

        int initTop = getConfig().getInitTop();
        int initLeft = getConfig().getInitLeft();

        if (pushedConfiguration.isDown()) {
            newTop += stepSize;
        } else if (pushedConfiguration.isUp()) {
            newTop -= stepSize;
        } else if (pushedConfiguration.isRight()) {
            newLeft += stepSize;
        } else if (pushedConfiguration.isLeft()) {
            newLeft -= stepSize;
        }

        // check top
        int viewBottomLine = newTop - initTop + viewHeight;
        if (viewBottomLine > maxHeight) {
            int difference = viewBottomLine - maxHeight;

            if (difference < stepSize) {
                newTop -= difference;
                currentTop = newTop;
            }
        } else if (newTop < initTop) {
            currentTop = initTop;
        } else {
            currentTop = newTop;
        }

        // check left
        int viewRightLine = newLeft - initLeft + viewWidth;
        if (viewRightLine > maxWidth) {
            int difference = viewRightLine - maxWidth;

            if (difference < stepSize) {
                newLeft -= difference;
                currentLeft = newLeft;
            }
        } else if (newLeft < initLeft) {
            currentLeft = initLeft;
        } else {
            currentLeft = newLeft;
        }
    }

    private VncConfig getConfig() {
        return configurationService.getConfig().getVncConfig();
    }
}
