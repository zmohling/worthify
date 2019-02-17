package team_10.client.utility;

import java.util.Date;
import team_10.client.constant.Scales;

public class DateUtil {

    public int temporalUnitsBetweenDates(Date from, Date to, Scales s) {

        int scale;

        switch (s) {
            case WEEK:
                scale = (1000 * 3600 * 24);
                break;
            case MONTH:
                scale = (1000 * 3600 * 24);
                break;
            case YEAR:
                scale = (1000 * 3600 * 24 * 7);
                break;
            default:
                scale = (1000 * 3600 * 24);
        }

        int difference = (int) (to.getTime() - from.getTime()) / scale;


        return 0;
    }

}
