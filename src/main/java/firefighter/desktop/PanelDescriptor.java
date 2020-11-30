package firefighter.desktop;

public class PanelDescriptor {
    public final String name;
    public final Class view;
    public final int userTypes[];
    public PanelDescriptor(String nm, Class zz, int tt[]) {
        name=nm;
        view=zz;
        userTypes=tt;
        }
}
