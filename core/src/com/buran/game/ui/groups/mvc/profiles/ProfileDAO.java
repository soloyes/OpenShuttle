package com.buran.game.ui.groups.mvc.profiles;

import com.buran.game.ui.groups.mvc.results.ResultDAO;

import java.util.Set;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author soloyes on 17/06/19.
 */

@ToString
public class ProfileDAO {
    @Getter
    @Setter
    private Type type;

    @Getter
    @Setter
    private String username;

    @Getter
    @Setter
    private SettingsDAO preferences;

    @Getter
    @Setter
    private boolean active;

    @Getter
    @Setter
    private String id;

    @Getter
    @Setter
    private Set<ResultDAO> resultSet;

    private ProfileDAO() {
    }

    public void copy(ProfileDAO profile) {
        this.type = profile.getType();
        this.username = profile.getUsername();
        this.preferences = new SettingsDAO(profile.getPreferences());
        this.active = profile.isActive();
    }

    public ProfileDAO(Type type, String username, SettingsDAO preferences, Set<ResultDAO> resultSet, boolean active) {
        this.type = type;
        this.username = username;
        this.preferences = preferences;
        this.active = active;
        this.resultSet = resultSet;

        this.id = UUID.randomUUID().toString();
    }

    public enum Type {
        PROFILE, DUMMY
    }
}