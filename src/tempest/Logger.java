package tempest;

import tempest.types.Database;

import java.io.FileWriter;
import java.text.SimpleDateFormat;

public class Logger {
    private FileWriter writer;
    private String path;
    private final SimpleDateFormat sdf = new SimpleDateFormat("[yyyy-MMM-dd HH:mm:ss] ");

    public Logger(String path) {
        this.path = path;
    }

    public void log(String s) {
        log("", s);
    }

    public void log(Database db, String s) {
        log(db.getName(), s);
    }

    public void log(String database, String s) {
        if(writer == null && path != null) {
            try {
                //System.out.println("creating log file: "+path);
                writer = new FileWriter(path, false);
            } catch(Exception e) {
                System.out.println("WARN! Can't create log file '" + path + "' :" + e);
                writer = null;
            }
        }
        if(writer != null) {
            try {
                writer.write(sdf.format(new java.util.Date()) + s + "\n");
            } catch(Exception e) {
                try {
                    writer.close();
                    writer = null;
                } catch(Exception ee) {}
            }
        } else {
            System.out.println(sdf.format(new java.util.Date()) + s);
        }
    }

    public void close() {
        try {
            if(writer != null) writer.close();
        } catch(Exception e) {}
        writer = null;
    }
}