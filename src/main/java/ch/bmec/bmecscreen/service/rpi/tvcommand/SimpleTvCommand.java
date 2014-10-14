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
public class SimpleTvCommand extends TvCommand {

    private SimpleTvCommand(TvCommandType type) {
        super(type);
    }

    @Override
    public String getCommand() {
        return type.getCommand();
    }

    @Override
    public String acceptTvCommandPart(TvCommandVisitor visitor) {
        return visitor.getTvCommandPart(this);
    }

    public static TvCommand getToggle() {
        return new SimpleTvCommand(TvCommandType.POWER_TOGGLE);
    }

    public static TvCommand getOn() {
        return new SimpleTvCommand(TvCommandType.ON);
    }

    public static TvCommand getOff() {
        return new SimpleTvCommand(TvCommandType.OFF);
    }

    public static TvCommand getVolumeUp() {
        return new SimpleTvCommand(TvCommandType.VOLUME_UP);
    }

    public static TvCommand getVolumeDown() {
        return new SimpleTvCommand(TvCommandType.VOLUME_DOWN);
    }

    public static TvCommand getPictureDynamic() {
        return new SimpleTvCommand(TvCommandType.PICTURE_DYNAMIC);
    }

    public static TvCommand getPictureStandard() {
        return new SimpleTvCommand(TvCommandType.PICTURE_STANDARD);
    }

    public static TvCommand getPictureMovie() {
        return new SimpleTvCommand(TvCommandType.PICTURE_MOVIE);
    }
}
