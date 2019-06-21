package com.shuttle.game.ui.groups.profilesMVC;

import java.util.List;

/**
 * @author Shuttle on 21/06/19.
 */

public interface Controller {

    void updateProfiles();

    void removeProfile(ProfileDAO profile);

    void selectProfile(ProfileDAO profile);

    ProfileDAO addProfile();

    ProfileDAO addProfile(String username);

    List<ProfileDAO> getProfileList();
}
