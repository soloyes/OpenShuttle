package com.shuttle.game.ui.groups.profilesMVC;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Map;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Shuttle on 17/06/19.
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
    private Map<String, Object> preferences;

    @Getter
    @Setter
    private boolean active;

    @Getter
    @Setter
    private String id;

    @JsonIgnore
    private static int i;
    private ProfileDAO() {
    }

    public void copy(ProfileDAO profile) {
        this.type = profile.getType();
        this.username = profile.getUsername() + ++i;
        this.preferences = profile.getPreferences();
        this.active = profile.isActive();
    }

    public ProfileDAO(Type type, String username, Map<String, Object> preferences, boolean active) {
        this.type = type;
        this.username = username;
        this.preferences = preferences;
        this.active = active;
        this.id = UUID.randomUUID().toString();
    }

    public enum Type {
        PROFILE, DUMMY
    }
}
