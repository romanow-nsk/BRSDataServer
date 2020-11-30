package firefighter.desktop;

import firefighter.core.entity.Entity;
import firefighter.core.entity.EntityList;
import firefighter.core.entity.baseentityes.JLong;
import retrofit2.Response;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;

public abstract class EntityBasePanel extends javax.swing.JPanel {
    Entity current;
    String entityName="";
    String entityTitleName="";
    public Response apiFunGetAll() throws IOException  { return  null; };
    public Response apiFunGetById() throws IOException { return  null; };
    public Response<JLong> apiFunAdd() throws IOException { return  null; };
    public Response<String> apiFunUpdate() throws IOException { return  null; };
    abstract public void showRecord();
    abstract public void updateRecord();
    abstract public void updateAction(KeyEvent evt);
    abstract public void getById();
    abstract public void getById(long oid);
    abstract public void getAllEvent();
    abstract public Choice listBox();
    abstract public EntityList<Entity> getData();
}
