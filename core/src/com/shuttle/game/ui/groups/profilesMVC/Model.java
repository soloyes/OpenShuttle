package com.shuttle.game.ui.groups.profilesMVC;

import java.util.List;

public interface Model {
    List<ProfileDAO> getProfileList();

    void setEmptyProfile(ProfileDAO profile);

    void removeProfile(ProfileDAO profile);

    void activateProfile(ProfileDAO profile);

    ProfileDAO addEmptyProfile();

    void addDummyProfile();

    void write();
}
