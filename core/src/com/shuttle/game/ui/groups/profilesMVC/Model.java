package com.shuttle.game.ui.groups.profilesMVC;

import java.util.List;

/**
 * @author Shuttle on 21/06/19.
 */

public interface Model {

    void setEmptyProfile(ProfileDAO profile);

    void removeProfile(ProfileDAO profile);

    void activateProfile(ProfileDAO profile);

    void addDummyProfile();

    void write();

    ProfileDAO getActive();

    ProfileDAO addEmptyProfile();

    List<ProfileDAO> getProfileList();
}
