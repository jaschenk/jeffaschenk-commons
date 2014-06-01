package jeffaschenk.commons.touchpoint.model.transitory;

/**
 * Simple Transitory Object to represent the
 * String Parsed alarmEntry Information.
 *
 * @author jeffaschenk@gmail.com
 */
public class ParsedAlarmEntry {
    
    private String name;
    private String minute;
    private String hour;
    private String dayOfMonth;
    private String month;
    private String dayOfWeek;
    private String nextAlarm;

    /**
     * Default Constructor
     *
     */
    public ParsedAlarmEntry() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMinute() {
        return minute;
    }

    public void setMinute(String minute) {
        this.minute = minute;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(String dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getNextAlarm() {
        return nextAlarm;
    }

    public void setNextAlarm(String nextAlarm) {
        this.nextAlarm = nextAlarm;
    }

    @Override
    public String toString() {
        return "ParsedAlarmEntry{" +
                "name='" + name + '\'' +
                ", minute='" + minute + '\'' +
                ", hour='" + hour + '\'' +
                ", dayOfMonth='" + dayOfMonth + '\'' +
                ", month='" + month + '\'' +
                ", dayOfWeek='" + dayOfWeek + '\'' +
                ", nextAlarm='" + nextAlarm + '\'' +
                '}';
    }

    /**
     * Generates a Table Entry for HTML Output.
     *
     * @return String
     */
    public String toHTML() {
        StringBuffer sb = new StringBuffer();
        sb.append("<tr>");
        sb.append("<td>");
        sb.append(this.name);
        sb.append("</td>");
        sb.append("<td>");
        sb.append(this.nextAlarm);
        sb.append("</td>");
        sb.append("</tr>");
        return sb.toString();
    }

    /**
     * Generates a Table Entry for HTML Output.
     *
     * @return String
     */
    public String toFullHTML() {
        StringBuffer sb = new StringBuffer();
        sb.append("<tr>");
        sb.append("<td>");
        sb.append(this.name);
        sb.append("</td>");
        sb.append("<td>");
        sb.append(this.nextAlarm);
        sb.append("</td>");
        sb.append("<td>");
        sb.append(this.minute);
        sb.append("</td>");
        sb.append("<td>");
        sb.append(this.hour);
        sb.append("</td>");
        sb.append("<td>");
        sb.append(this.dayOfMonth);
        sb.append("</td>");
        sb.append("<td>");
        sb.append(this.month);
        sb.append("</td>");
        sb.append("<td>");
        sb.append(this.dayOfWeek);
        sb.append("</td>");
        sb.append("</tr>");
        return sb.toString();
    }
    
}
