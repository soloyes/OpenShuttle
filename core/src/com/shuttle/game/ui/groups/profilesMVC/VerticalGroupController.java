package com.shuttle.game.ui.groups.profilesMVC;

import com.shuttle.game.conf.PaintConstants;

import java.util.List;

/**
 * @author Shuttle on 17/06/19.
 */

public class VerticalGroupController {
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

    public void addProfile() {
        int i;
        List<ProfileDAO> profiles = Profiles.getProfileList();
        for (i = 0; i < profiles.size(); i++) {
            ProfileDAO profile = profiles.get(i);
            if (profile.getType() == ProfileDAO.Type.DUMMY) {
                Profiles.setEmptyProfile(profile);
                break;
            }
        }
        if (i == profiles.size()) {
            Profiles.addEmptyProfile();
        }
        initView();
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
