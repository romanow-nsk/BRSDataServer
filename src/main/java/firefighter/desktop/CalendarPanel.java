/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package firefighter.desktop;

import firefighter.core.constants.Values;
import firefighter.core.constants.ValuesBase;
import firefighter.core.utils.OwnDateTime;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 *
 * @author romanow
 */
public class CalendarPanel extends javax.swing.JPanel {
    private OwnDateTime date = new OwnDateTime();
    private OwnDateTime cdate = new OwnDateTime();
    private I_Calendar back;
    private int dayInMonth=0;
    private Button days[]=new Button[0];
    /**
     * Creates new form CalendarPanel
     */
    public CalendarPanel(I_Calendar back0) {
        back = back0;
        setVisible(true);
        initComponents();
        setBounds(0,0,230,350);
        date.day(1);
        Prev.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                date.decMonth();
                createList();
                back.onMonth(dayInMonth,date.month(),date.year());
            }});
        Next.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                date.incMonth();
                createList();
                back.onMonth(dayInMonth,date.month(),date.year());
            }});
        createList();
        }
    public void enableDay(int day, boolean enable){
        days[day-1].setEnabled(enable);
        }
    public void enableAllDays(boolean enable){
        for(int i=0;i<days.length;i++)
            days[i].setEnabled(enable);
        }
    public void sendCalendarEvent(){
        back.onMonth(dayInMonth,date.month(),date.year());
        }
    public int dayInMonth(){ return dayInMonth; }
    public void setDayColor(int day, Color color){
        days[day-1].setBackground(color);
        }
    public void setAllDaysColor(Color color){
        for(int i=0;i<days.length;i++)
            days[i].setBackground(color);
            }
    public void createList(){
        int i,j,k;
        Prev.setText(date.month()==1 ? Values.mnt[11] : Values.mnt[date.month()-2]);
        Next.setText(date.month()==12 ? Values.mnt[0] : Values.mnt[date.month()]);
        Month.setText(""+Values.mnt[date.month()-1]+" "+(date.year()));
        Days.removeAll();
        for (j=0;j<7;j++){
            Button ff = new Button();
            ff.setEnabled(false);
            ff.setBounds(30*j,0,28,28);
            ff.setLabel(""+Values.week[j]);
            Days.add(ff);
            }
        int k0=date.dayOfWeek()-1;
        int k1=31;
        k=0;
        final int month=date.month();
        if (month==2){
            k1=28; if (date.year()%4==0) k1=29;
            }
        if (month==4 || month==6 || month==9 || month==11) k1=30;
        days = new Button[k1];
        dayInMonth = k1;
        for (i=0;i<6;i++){
            for (j=0;j<7;j++,k++){
                if (k>=k0 && k<k0+k1){
                    Button ff = new Button();
                    ff.setBounds(30*j,(i+1)*30,28,28);
                    ff.setLabel(""+Values.week[j]);
                    Days.add(ff);
                    final int nn=k-k0+1;
                    ff.setLabel(""+nn);
                    days[nn-1] = ff;
                    if (month==cdate.month() && nn==cdate.day() && date.year()==cdate.year()) {
                        Rectangle rr = ff.getBounds();
                        rr.x-=2;
                        rr.y-=2;
                        rr.height+=2;
                        rr.width+=2;
                        ff.setBounds(rr);
                    	//ff.setBackground(Color.GREEN);
                        }
                    ff.addMouseListener(new MouseListener() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            switch (e.getButton()){
                                case 1:
                                    back.onDay(nn,date.month(),date.year());
                                    break;
                                case 3:
                                    back.onDayRight(nn,date.month(),date.year());
                                    break;
                                }
                            }
                        @Override
                        public void mousePressed(MouseEvent e) {}
                        @Override
                        public void mouseReleased(MouseEvent e) {}
                        @Override
                        public void mouseEntered(MouseEvent e) {}
                        @Override
                        public void mouseExited(MouseEvent e) {}
                    });
                    //ff.addActionListener(new ActionListener() {
                    //    @Override
                    //    public void actionPerformed(ActionEvent e) {
                    //        back.onDay(nn,date.month(),date.year());
                    //        }
                    //    });
                    }
                }
            }
        }        

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Prev = new javax.swing.JButton();
        Month = new javax.swing.JLabel();
        Next = new javax.swing.JButton();
        Days = new javax.swing.JPanel();

        setLayout(null);
        add(Prev);
        Prev.setBounds(10, 40, 90, 25);

        Month.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Month.setText("Month");
        add(Month);
        Month.setBounds(40, 10, 150, 20);
        add(Next);
        Next.setBounds(130, 40, 90, 25);

        Days.setLayout(null);
        add(Days);
        Days.setBounds(10, 70, 210, 220);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Days;
    private javax.swing.JLabel Month;
    private javax.swing.JButton Next;
    private javax.swing.JButton Prev;
    // End of variables declaration//GEN-END:variables
    public static void main(String ss[]){
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {}
        JFrame ff = new JFrame();
        ff.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        ff.setBounds(100,100,240,320);
        CalendarPanel cc = new CalendarPanel(new I_Calendar() {
            @Override
            public void onMonth(int dd, int month, int year) {
                System.out.println(dd+" "+month+"."+year);
                }
            @Override
            public void onDay(int day, int month, int year) {
                System.out.println(day+"."+month+"."+year);
                }
            @Override
            public void onDayRight(int day, int month, int year) { System.out.println(day+"."+month+"."+year); }
        });
        cc.enableAllDays(false);
        cc.enableDay(2,true);
        cc.enableDay(12,true);
        cc.setDayColor(3,Color.red);
        ff.add(cc);
        ff.setVisible(true);
    }
}
