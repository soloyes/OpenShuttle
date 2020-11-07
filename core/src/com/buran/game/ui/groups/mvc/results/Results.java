package com.buran.game.ui.groups.mvc.results;

/**
 * @author soloyes on 7/14/18.
 */

public class Results {
    private StringBuffer stringBuffer;
    private com.buran.game.ui.groups.mvc.profiles.Profiles profiles;

    public Results() {
        this.stringBuffer = new StringBuffer();
        this.profiles = new com.buran.game.ui.groups.mvc.profiles.Profiles();
    }

    public String shareResults() {
        stringBuffer.setLength(0);
        for (com.buran.game.ui.groups.mvc.profiles.ProfileDAO p : profiles.getList()) {
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
                            .append(r.isChild() ? " as Child" : "")
                            .append("\n");
                }
            }
        }
        return stringBuffer.toString();
    }
}