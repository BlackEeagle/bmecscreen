/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bmec.bmecscreen.controller;

import ch.bmec.bmecscreen.service.rpi.RPiCommunicationService;
import ch.bmec.bmecscreen.service.rpi.tvcommand.SimpleTvCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Thom
 */
@Component
public class TvControllerImpl implements TvController {

    @Autowired
    private RPiCommunicationService rPiCommunicationService;

    @Override
    public Void turnOn() {
        rPiCommunicationService.configureTv(SimpleTvCommand.getOn());
        return null;
    }

    @Override
    public Void turnOff() {
        rPiCommunicationService.configureTv(SimpleTvCommand.getOff());
        return null;
    }

    @Override
    public Void volumeUp() {
        rPiCommunicationService.configureTv(SimpleTvCommand.getVolumeUp());
        return null;
    }

    @Override
    public Void volumeDown() {
        rPiCommunicationService.configureTv(SimpleTvCommand.getVolumeDown());
        return null;
    }

    @Override
    public Void pictureModeDynamic() {
        rPiCommunicationService.configureTv(SimpleTvCommand.getPictureDynamic());
        return null;
    }

    @Override
    public Void pictureModeStandard() {
        rPiCommunicationService.configureTv(SimpleTvCommand.getPictureStandard());
        return null;
    }

    @Override
    public Void pictureModeMovie() {
        rPiCommunicationService.configureTv(SimpleTvCommand.getPictureMovie());
        return null;
    }

}
