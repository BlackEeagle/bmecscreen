/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bmec.bmecscreen.service.rpi.tvcommand;

/**
 *
 * @author Thom
 */
public enum TvCommandType {

    POWER_TOGGLE("toggle"), OFF("off"), ON("on"), VOLUME_UP("volumeUp"), VOLUME_DOWN("volumeDown"), PICTURE_DYNAMIC("pictureDynamic"), PICTURE_STANDARD("pictureStandard"), PICTURE_MOVIE("pictureMovie"), PICTURE_CONTRAST("pictureContrast"), PICTURE_BRIGHTNESS("pictureBrightness"), PICTURE_SHARPNESS("pictureSharpness"), PICTURE_COLOR("pictureColor"), PICTURE_TINT("pictureTint");

    private final String command;

    private TvCommandType(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

}
