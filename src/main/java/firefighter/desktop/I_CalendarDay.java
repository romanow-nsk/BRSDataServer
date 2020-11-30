package firefighter.desktop;

public abstract class I_CalendarDay implements I_Calendar {
    @Override
    public void onMonth(int daInMonth, int month, int year) { }
    @Override
    public void onDayRight(int day, int month, int year) { }
}
