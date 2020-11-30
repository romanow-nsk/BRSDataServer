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
public interface I_PanelEvent {
    public void refresh();
    public void  eventPanel(int code, int par1, long par2, String par3);
    public void shutDown();
    public boolean isMainMode();
    public boolean isESSMode();
}
