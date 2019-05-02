package team_10.client.constant;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public enum PERIOD {
    NONE("Not Recurring", 0, 0),
    DAY("Daily", 1, 7),
    WEEK("Weekly", 7, 4),
    MONTH("Monthly", 30, 12),
    YEAR("Yearly", 365, 5);

    private String displayName;
    private int daysPerPeriod;
    private int periodsOnGraph;

    PERIOD(@NonNull String displayName, @NonNull int daysPerPeriod, @NonNull int periodsOnGraph) {
        this.displayName = displayName;
        this.daysPerPeriod = daysPerPeriod;
        this.periodsOnGraph = periodsOnGraph;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public int getDaysPerPeriod() {
        return this.daysPerPeriod;
    }

    public int getPeriodsOnGraph() { return this.periodsOnGraph; }

    @Override
    public @NonNull String toString() {
        return this.displayName;
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
