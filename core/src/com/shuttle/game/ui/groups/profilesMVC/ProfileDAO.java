package com.shuttle.game.ui.groups.profilesMVC;

import com.shuttle.game.conf.GamePreferences;

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
    private GamePreferences preferences;

    @Getter
    @Setter
    private boolean active;

    @Getter
    @Setter
    private String id;

    private ProfileDAO() {
    }

    public void copy(ProfileDAO profile) {
        this.type = profile.getType();
        this.username = profile.getUsername();
        this.preferences = profile.getPreferences();
        this.active = profile.isActive();
    }

    public ProfileDAO(Type type, String username, GamePreferences preferences, boolean active) {
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
