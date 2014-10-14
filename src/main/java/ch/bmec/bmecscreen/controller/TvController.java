/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bmec.bmecscreen.controller;

/**
 *
 * @author Thom
 */
public interface TvController {

    public Void turnOn();

    public Void turnOff();

    public Void volumeUp();

    public Void volumeDown();

    public Void pictureModeDynamic();

    public Void pictureModeStandard();

    public Void pictureModeMovie();

}
