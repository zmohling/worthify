package team_10.client.utility;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Class for general calculations.
 */
public class General {
    /**
     * Rounds a decimal.
     * @param value value
     * @param places places to round to
     * @return double value
     */
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
