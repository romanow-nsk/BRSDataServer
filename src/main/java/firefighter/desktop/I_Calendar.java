/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package firefighter.desktop;

/**
 *
 * @author romanow
 */
public interface I_Calendar {
    public void onMonth(int daInMonth,int month, int year);
    public void onDay(int day, int month, int year);
    public void onDayRight(int day, int month, int year);
}