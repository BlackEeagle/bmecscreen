/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bmec.bmecscreen.service.rpi.pushbutton;

/**
 *
 * @author Thom
 */
public class PushbuttonConfiguration {

    private boolean screen1;

    private boolean screen2;

    private boolean up;

    private boolean down;

    private boolean left;

    private boolean right;

    public PushbuttonConfiguration() {

    }

    public PushbuttonConfiguration(boolean screen1, boolean screen2, boolean up, boolean down, boolean left, boolean right) {
        setConfiguration(screen1, screen2, up, down, left, right);
    }

    public void setConfiguration(boolean screen1, boolean screen2, boolean up, boolean down, boolean left, boolean right) {
        this.screen1 = screen1;
        this.screen2 = screen2;
        this.up = up;
        this.down = down;
        this.left = left;
        this.right = right;
    }

    public boolean isScreen1() {
        return screen1;
    }

    public void setScreen1(boolean screen1) {
        this.screen1 = screen1;
    }

    public boolean isScreen2() {
        return screen2;
    }

    public void setScreen2(boolean screen2) {
        this.screen2 = screen2;
    }

    public boolean isUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public boolean isDown() {
        return down;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    @Override
    public String toString() {
        return "PushbuttonConfiguration{" + "screen1=" + screen1 + ", screen2=" + screen2 + ", up=" + up + ", down=" + down + ", left=" + left + ", right=" + right + '}';
    }

}
