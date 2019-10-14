/**
 * Read web server data and analyse hourly access patterns.
 * 
 * @author David J. Barnes and Michael Kölling.
 * 
 * @author Steve Cate
 * @version    10/14/19
 */
public class LogAnalyzer
{
    // Where to calculate the hourly access counts.
    private int[] hourCounts;
    // Use a LogfileReader to access the data.
    private LogfileReader reader;

    /**
     * Create an object to analyze hourly web accesses.
     */
    public LogAnalyzer()
    { 
        // Create the array object to hold the hourly
        // access counts.
        hourCounts = new int[24];
        // Create the reader to obtain the data.
        reader = new LogfileReader();
    }
    
    /**
     * Create an object to open a certain file
     */
    public LogAnalyzer(String fileName)
    {
        hourCounts = new int[24];
        
        reader = new LogfileReader(fileName);
        
        // We can fill out the array list right away because we have
        // all the data in the file
        analyzeHourlyData();
        
    }

    /**
     * Analyze the hourly access data from the log file.
     */
    public void analyzeHourlyData()
    {
        while(reader.hasNext()) {
            LogEntry entry = reader.next();
            int hour = entry.getHour();
            hourCounts[hour]++;
        }
    }

    /**
     * Print the hourly counts.
     * These should have been set with a prior
     * call to analyzeHourlyData.
     */
    public void printHourlyCounts()
    {
        System.out.println("Hr: Count");
        for(int hour = 0; hour < hourCounts.length; hour++) {
            System.out.println(hour + ": " + hourCounts[hour]);
        }
    }
    
    /**
     * Print the lines of data read by the LogfileReader
     */
    public void printData()
    {
        reader.printData();
    }
    
    /**
     * This will add all the logs from each hour to get a total
     * @return total number of logs
     * 
     */
    public int numberOfAccesses()
    {
        int total = 0;
        for(int i=0; hourCounts.length > i; i++)
        {
            total += hourCounts[i];
        }
        
        return total;
    }
    
    /**
     * This will return the busiest hour if there are multiple hours with the same ammount of logs
     * then only the first busiest hour will be returned
     * @return theBusiestHour the hour with the most logs
     */
    public int busiestHour()
    {
        int theBusiestHour = 0;
        
        for(int i=1; hourCounts.length > i; i++)
        {
            if(hourCounts[i] > hourCounts[theBusiestHour])
            {
                theBusiestHour = i;
            }
        }
        return theBusiestHour;
    }
    
    /**
     * This will retunr the quietest hour if there are multiple hours with the same ammount of logs
     * only the first quietest hour will be returned
     * @return theQuietestHour the hour with the least ammount of logs
     */
    public int quietestHour()
    {
        int theQuietestHour = 0;
        
        for(int i=1; hourCounts.length > i; i++)
        {
            if (hourCounts[i] < hourCounts[theQuietestHour])
            {
                theQuietestHour = i;
            }
        }
        return theQuietestHour;
    }
    
    /**
     * This will look at all the logs and let you know what
     * the busiest hour or hours are if there are mor then one.
     */
    public void printBusiestHour()
    {
        int theBusiestHour = 0;
        boolean multipleBusiestHours = false;
        
        for(int i=1; hourCounts.length > i; i++)
        {
            if (hourCounts[i] == theBusiestHour)
            {
                multipleBusiestHours = true;
            }
            if (hourCounts[i] > hourCounts[theBusiestHour])
            {
                theBusiestHour = i;
            }
        }
        
        if (multipleBusiestHours)
        {
            System.out.print("The Busiest Hours are ");
            for(int i=0; hourCounts.length > i; i++)
            {
                if(hourCounts[1] == hourCounts[theBusiestHour])
                {
                    System.out.print(i + ", ");
                }
            }
            System.out.println();
        }
        else
        {
            System.out.println("The Busiest Hour is " + theBusiestHour);
        }
    }
}
