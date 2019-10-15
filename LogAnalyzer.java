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
    // Where to calculate the daily access counts.
    private int[] dayCounts;
    // Where to calculate the monthly access counts.
    private int[] monthCounts;
    // Where to calculate access counts for each day of the week.
    private int[] dayOfTheWeekCounts;
    // Where to calculate access counts for each year.
    private int[] yearCounts;
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
        // An array to hold daily access counts
        dayCounts = new int[28];
        // An array to hold monthly access counts
        monthCounts = new int[12];
        // An array to hold access counts for days of the week
        dayOfTheWeekCounts = new int [7];
        // An array to hold access counts for years
        // Only goes up to 5 years because thats the most we are working with right now
        yearCounts = new int [5];
        // Create the reader to obtain the data.
        reader = new LogfileReader();
    }
    
    /**
     * Create an object to open a certain file
     */
    public LogAnalyzer(String fileName)
    {
        hourCounts = new int[24];
        dayCounts = new int [28];
        monthCounts = new int [12];
        dayOfTheWeekCounts = new int [7];
        yearCounts = new int [5];
        
        reader = new LogfileReader(fileName);
        
        // We can fill out the array lists right away because we have
        // all the data in the file
        analyzeHourlyData();
        analyzeDailyData();
        analyzeMonthlyData();
        analyzeDayOfTheWeekData();
        analyzeYearlyData();
    }

    /**
     * Analyze the hourly access data from the log file.
     */
    public void analyzeHourlyData()
    {
        reader.reset();
        while(reader.hasNext()) {
            LogEntry entry = reader.next();
            int hour = entry.getHour();
            hourCounts[hour]++;
        }
    }
    
    /**
     * Analyze the daily access data from the log file.
     */
    public void analyzeDailyData()
    {
        reader.reset();
        while(reader.hasNext())
        {
            LogEntry entry = reader.next();
            int day = entry.getDay();
            dayCounts[day-1]++;
        }
    }
    
    /**
     * Analyze the monthly access data from the log file
     */
    public void analyzeMonthlyData()
    {
        reader.reset();
        while(reader.hasNext())
        {
            LogEntry entry = reader.next();
            int month = entry.getMonth();
            monthCounts[month-1]++;
        }
    }
    
    /**
     * Analyze the data from the log files for each day of the week
     */
    public void analyzeDayOfTheWeekData()
    {
        for(int i=0; dayCounts.length > i; i++)
        {
            if(i < 7)
            {
                dayOfTheWeekCounts[i] += dayCounts[i];
            }
            if((i > 6) && (i < 14))
            {
                dayOfTheWeekCounts[i-7] += dayCounts[i];
            }
            if((i > 13) && (i < 21))
            {
                dayOfTheWeekCounts[i-14] += dayCounts[i];
            }
            if(i >20)
            {
                dayOfTheWeekCounts[i-21] += dayCounts[i];
            }
        }
    }
    
    /**
     * Analyze the yearly access data from the log file
     */
    public void analyzeYearlyData()
    {
        reader.reset();
        while(reader.hasNext())
        {
            LogEntry entry = reader.next();
            int year = entry.getYear();
            yearCounts[year-2015]++;
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
     * This will find the buesiest 2 hour window
     * @return the first hour of the two hour period that has the most logs
     */
    public int busiestTwoHours()
    {
        int theBusiestHour = 0;
        int mostNumberOfLogs = 0;
        
        for(int i=1; hourCounts.length > i; i++)
        {
            if((hourCounts[i] + hourCounts[i-1]) > mostNumberOfLogs)
            {
                theBusiestHour = i-1;
                mostNumberOfLogs = (hourCounts[i] + hourCounts[i-1]);
            }
        }
        return theBusiestHour;
    }
    
    /**
     * This will return the quietest hour if there are multiple hours with the same ammount of logs
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
                if(hourCounts[i] == hourCounts[theBusiestHour])
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
    
    /**
     * This will return the day with the most ammount of total logs from all months
     * if there is more then one day with the same ammount of logs only the first will be returned
     * @return the day with the most ammount of total logs
     */
    public int busiestDay()
    {
        int theBusiestDay = 0;
        
        for(int i=1; dayCounts.length > i; i++)
        {
            if(dayCounts[i] > dayCounts[theBusiestDay])
            {
                theBusiestDay = i;
            }
        }
        return (theBusiestDay + 1);
    }
    
    /**
     * This will return the day with the least ammount of total logs from all months
     * if there is more then one day with the same ammount of logs only the first will be returned
     * @return the day with the least ammount of total logs
     */
    public int quietestDay()
    {
        int theQuiestestDay = 0;
        
        for(int i=1; dayCounts.length > i; i++)
        {
            if(dayCounts[i] < dayCounts[theQuiestestDay])
            {
                theQuiestestDay = i;
            }
        }
        return (theQuiestestDay + 1);
    }
    
    /**
     * This will return the month with the most ammount of total logs
     * if there is more then one month with the same ammount of logs only the first will be returned
     * @return the month withe the most logs
     */
    public int busiestMonth()
    {
        int theBusiestMonth = 0;
        
        for(int i=1; monthCounts.length > i; i++)
        {
            if(monthCounts[i] > monthCounts[theBusiestMonth])
            {
                theBusiestMonth = i;
            }
        }
        return (theBusiestMonth + 1);
    }
    
    /**
     * This will return the month with the least ammount of total logs
     * if there is more then one month with the same ammount of logs only the first will be returned
     * @return the month withe the least logs
     */
    public int quietestMonth()
    {
        int theQuietestMonth = 0;
        
        for(int i=1; monthCounts.length > i; i++)
        {
            if(monthCounts[i] < monthCounts[theQuietestMonth])
            {
                theQuietestMonth = i;
            }
        }
        return (theQuietestMonth + 1);
    }
}
