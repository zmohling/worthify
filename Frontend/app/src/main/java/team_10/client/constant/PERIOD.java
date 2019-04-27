package team_10.client.constant;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public enum PERIOD {
    NONE("Not Recurring", 0),
    DAY("Daily", 1),
    WEEK("Weekly", 7),
    MONTH("Monthly", 30),
    YEAR("Yearly", 365);

    private String displayName;
    private int daysPerPeriod;

    PERIOD(@NonNull String displayName, @NonNull int daysPerPeriod) {
        this.displayName = displayName;
        this.daysPerPeriod = daysPerPeriod;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public int getDaysPerPeriod() {
        return this.daysPerPeriod;
    }

    public static List<String> getAllAsStrings() {
        ArrayList<String> periods = new ArrayList<>();
        PERIOD[] p = PERIOD.values();

        int i;
        for (i = 0; i < p.length; i++) {
            periods.add(p[i].toString());
        }

        return periods;
    }
}
