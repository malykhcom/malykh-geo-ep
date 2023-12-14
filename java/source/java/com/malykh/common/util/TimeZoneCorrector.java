package com.malykh.common.util;

import java.util.Date;
import java.util.TimeZone;
import java.util.Arrays;
import java.util.logging.Logger;
import java.text.DateFormat;

/**
 * @author Anton Malykh
 */
public class TimeZoneCorrector
{
    private static final Logger logger = Logger.getLogger(TimeZoneCorrector.class.getName());
    private static final String IRKUTSK_TZ_ID = "Asia/Irkutsk";
    private static final String ULAANBAATAR_TZ_ID = "Asia/Ulaanbaatar";
    public static void correct()
    {
        final TimeZone tz = TimeZone.getDefault();
        if (tz.getID().equals(ULAANBAATAR_TZ_ID))
        {
            if (Arrays.asList(TimeZone.getAvailableIDs()).contains(IRKUTSK_TZ_ID))
            {
                final TimeZone newtz = TimeZone.getTimeZone(IRKUTSK_TZ_ID);
                TimeZone.setDefault(newtz);
                logger.info("Current TZ: "+tz.getID()+", changed to "+newtz.getID()+" (workaround)");
            }
        }
    }
    public static void main(String[] args)
    {
        TimeZoneCorrector.correct();
        Date d = new Date();
        System.out.println(DateFormat.getDateTimeInstance().format(d));
    }
}

