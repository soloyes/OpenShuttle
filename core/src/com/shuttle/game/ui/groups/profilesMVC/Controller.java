package com.shuttle.game.ui.groups.profilesMVC;

public interface Controller {
    ProfileDAO addProfile();

    ProfileDAO addProfile(String username);

    void updateProfiles();

    void removeProfile(ProfileDAO profile);

    void selectProfile(ProfileDAO profile);
}
