package com.shuttle.game.ui.groups.profilesMVC;

import com.shuttle.game.conf.PaintConstants;

import java.util.List;

/**
 * @author Shuttle on 17/06/19.
 */

public class VerticalGroupController implements Controller {
    private VerticalGroupView verticalGroupView;

    public VerticalGroupController(VerticalGroupView verticalGroupView) {
        this.verticalGroupView = verticalGroupView;
        initView();
    }

    private void initView() {
        List<ProfileDAO> profiles = Profiles.getProfileList();
        //if not enough dummy lines -> to show scroll from very begin
        while (profiles.size() < PaintConstants.DUMMY_PROFILES) {
            Profiles.addDummyProfile();
        }
        Profiles.write();
        verticalGroupView.init();
    }

    public ProfileDAO addProfile(String username) {
        ProfileDAO profile = addProfile();
        profile.setUsername(username);
        return profile;
    }

    public void updateProfiles() {
        initView();
    }

    public ProfileDAO addProfile() {
        int i;
        ProfileDAO tmpProfile = null;
        List<ProfileDAO> profiles = Profiles.getProfileList();
        for (i = 0; i < profiles.size(); i++) {
            ProfileDAO profile = profiles.get(i);
            if (profile.getType() == ProfileDAO.Type.DUMMY) {
                tmpProfile = profile;
                Profiles.setEmptyProfile(profile);
                break;
            }
        }
        if (i == profiles.size()) {
            tmpProfile = Profiles.addEmptyProfile();
        }
        initView();
        return tmpProfile;
    }

    public void removeProfile(ProfileDAO profile) {
        Profiles.removeProfile(profile);
        initView();
    }

    public void selectProfile(ProfileDAO profile) {
        Profiles.activateProfile(profile);
        initView();
    }
}
