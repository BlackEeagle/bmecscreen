/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bmec.bmecscreen.controller;

import ch.bmec.bmecscreen.service.rpi.pushbutton.PushbuttonConfiguration;

/**
 *
 * @author Thom
 */
public interface VncViewerController {
    
    public PushbuttonConfiguration handlePush(PushbuttonConfiguration pushedConfiguration);
    
}
