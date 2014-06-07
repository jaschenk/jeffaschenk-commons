package jeffaschenk.commons.frameworks.cnxidx.utility;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Mark A. Miller
 * @version $Revision: #3 $
 * @since 1.0
 */
public class StopWatch implements java.io.Serializable {

    private Date start;
    private Date stop;
    protected long startMemory;
    protected long stopMemory;

    public StopWatch() {
        start = new Date();
        stop = null;
        Runtime runtime = Runtime.getRuntime();
        startMemory = runtime.totalMemory() - runtime.freeMemory();
        stopMemory = 0;
    }

    public void start() {
        start = new Date();
        Runtime runtime = Runtime.getRuntime();
        startMemory = runtime.totalMemory() - runtime.freeMemory();
    }


    public void stop() {
        stop = new Date();
        Runtime runtime = Runtime.getRuntime();
        stopMemory = runtime.totalMemory() - runtime.freeMemory();
    }


    public Date getStartTime() {
        return start;
    }


    public Date getStopTime() {
        return stop;
    }

    public long getStartMemory() {
        return startMemory;
    }

    public long getStopMemory() {
        return stopMemory;
    }

    public String getElapsedTimeString() {
        SimpleDateFormat elapsedFormat = new SimpleDateFormat("HH:mm:ss.SSS");
        return elapsedFormat.format(getElapsedTime());
    }


    public Date getElapsedTime() {
        Calendar now = Calendar.getInstance();
        now.clear();
        now.set(Calendar.MILLISECOND, (int) getElapsedMillis());
        return now.getTime();
    }

    public long getUsedMemory() {
        Runtime runtime = Runtime.getRuntime();
        long tempMemory = runtime.totalMemory() - runtime.freeMemory();
        return (tempMemory - startMemory);
    }

    public long getElapsedMillis() {
        long startMillis = 0;
        long stopMillis = 0;

        if (start == null) {
            return 0;
        } else {
            startMillis = start.getTime();
        }

        if (stop == null) {
            stopMillis = System.currentTimeMillis();
        } else {
            stopMillis = stop.getTime();
        }

        return stopMillis - startMillis;
    }


    public String toString() {
        StringBuffer buf = new StringBuffer();

        SimpleDateFormat startStopFormat = new SimpleDateFormat("HH:mm:ss");
        //buf.append("Start: ");
        if (start != null) {
            buf.append(startStopFormat.format(start));
        }
        //buf.append("\tStop: ");
        buf.append("\t");
        if (stop != null) {
            buf.append(startStopFormat.format(stop));
        } else {
            // Just to keep the spacing consistent
            buf.append("        ");
        }
        //buf.append("\tElapsed: ").append(getElapsedTimeString());
        buf.append("\t").append(getElapsedTimeString());

        buf.append("\t");
        buf.append(Long.toString(startMemory));
        buf.append("\t");
        buf.append(Long.toString(stopMemory));
        buf.append("\t");
        buf.append(Long.toString(stopMemory - startMemory));

        return buf.toString();
    }


    public static void main(String[] args) {

        StopWatch sw = new StopWatch();
        sw.start();
        for (int ii = 1; ii < 10; ii++) {
            try {
                Thread.sleep((long) (1000 * Math.random()));
            } catch (InterruptedException ignore) {
            }
            sw.stop();
            System.out.println(sw.toString());
        }
    }
}
