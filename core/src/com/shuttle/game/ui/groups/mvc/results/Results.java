package com.shuttle.game.ui.groups.mvc.results;

import com.shuttle.game.ui.groups.mvc.profiles.ProfileDAO;
import com.shuttle.game.ui.groups.mvc.profiles.Profiles;

/**
 * @author Shuttle on 7/14/18.
 */
public class Results {
    private StringBuffer stringBuffer;
    private Profiles profiles;

    public Results() {
        this.stringBuffer = new StringBuffer();
        this.profiles = new Profiles();
    }

    public String shareResults() {
        stringBuffer.setLength(0);
        for (ProfileDAO p : profiles.getList()) {
            if (!p.getResultSet().isEmpty()) {
                for (ResultDAO r : p.getResultSet()) {
                    stringBuffer
                            .append(p.getUsername())
                            .append(" ")
                            .append(r.getCurrent().getScore())
                            .append(" ")
                            .append(r.getDate())
                            .append(" ")
                            .append(r.getLevel())
                            .append("\n");
                }
            }
        }
        return stringBuffer.toString();
    }
}