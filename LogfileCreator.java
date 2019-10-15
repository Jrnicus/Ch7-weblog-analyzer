import java.io.*;
import java.util.*;

/**
 * A class for creating log files of random data.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version    2016.02.29
 */
public class LogfileCreator
{
    private Random rand;
    // to store codes
    private int[] codes;

    /**
     * Create log files.
     */
    public LogfileCreator()
    {
        rand = new Random();
        // so that certain codes have a diffrent percentage chance to come out
        codes = new int[100];
        logCodeArray();
    }
    
    /**
     * Create a file of random log entries.
     * @param filename The file to write.
     * @param numEntries How many entries.
     * @return true if successful, false otherwise.
     */
    public boolean createFile(String filename, int numEntries)
    {
        boolean success = false;
        
        if(numEntries > 0) {
            try (FileWriter writer = new FileWriter(filename)) {
                LogEntry[] entries = new LogEntry[numEntries];
                for(int i = 0; i < numEntries; i++) {
                    entries[i] = createEntry();
                }
                Arrays.sort(entries);
                for(int i = 0; i < numEntries; i++) {
                    writer.write(entries[i].toString());
                    writer.write('\n');
                }
                
                success = true;
            }
            catch(IOException e) {
                System.err.println("There was a problem writing to " + filename);
            }
                
        }
        return success;
    }
    
    /**
     * Make a array to pick what code the entry gets randomly
     * there will be a 95% chane the code is 200 OK
     * a 3% chance the code is 404 Not Found
     * and a 2% chance the code is 403 Forbidden
     */
    private void logCodeArray()
    {
        for(int i=0; 100 > i; i++)
        {
            if(2 > i)
            {
                codes[i] = 403;
            }
            if(5 > i)
            {
                codes[i] = 404;
            }
            if (4 < i)
            {
                codes[i] = 200;
            }
        }
    }
    
    /**
     * Create a single (random) entry for a log file.
     * @return A log entry containing random data.
     */
    public LogEntry createEntry()
    {
        int year = 2015 + rand.nextInt(5);
        int month = 1 + rand.nextInt(12);
        // Avoid the complexities of days-per-month.
        int day = 1 + rand.nextInt(28);
        int hour = rand.nextInt(24);
        int minute = rand.nextInt(60);
        int code = codes[rand.nextInt(100)];
        return new LogEntry(year, month, day, hour, minute, code);
    }

}
