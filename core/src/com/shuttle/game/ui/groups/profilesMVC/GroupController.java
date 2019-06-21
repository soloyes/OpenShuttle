package com.shuttle.game.ui.groups.profilesMVC;

import com.shuttle.game.conf.PaintConstants;

import java.util.List;

/**
 * @author Shuttle on 17/06/19.
 */

public class GroupController implements Controller {
    private View view;
    private Model model;

    @Override
    public List<ProfileDAO> getProfileList() {
        return model.getProfileList();
    }

    public GroupController(View view, Model model) {
        this.view = view;
        this.model = model;
    }

    public static boolean isEmpty() {
        return Profiles.isEmpty();
    }

    public ProfileDAO getActive() {
        return model.getActive();
    }

    private void initView() {
        //if not enough dummy lines -> to show scroll from very begin
        while (model.getProfileList().size() < PaintConstants.DUMMY_PROFILES) {
            model.addDummyProfile();
        }
        model.write();
        view.init();
    }

    @Override
    public ProfileDAO addProfile(String username) {
        ProfileDAO profile = addProfile();
        profile.setUsername(username);
        return profile;
    }

    @Override
    public void updateProfiles() {
        initView();
    }

    @Override
    public ProfileDAO addProfile() {
        int i;
        ProfileDAO tmpProfile = null;
        List<ProfileDAO> tmpProfiles = model.getProfileList();
        for (i = 0; i < tmpProfiles.size(); i++) {
            ProfileDAO profile = tmpProfiles.get(i);
            if (profile.getType() == ProfileDAO.Type.DUMMY) {
                tmpProfile = profile;
                model.setEmptyProfile(profile);
                break;
            }
        }
        if (i == tmpProfiles.size()) {
            tmpProfile = model.addEmptyProfile();
        }
        initView();
        return tmpProfile;
    }

    @Override
    public void removeProfile(ProfileDAO profile) {
        model.removeProfile(profile);
        initView();
    }

    @Override
    public void selectProfile(ProfileDAO profile) {
        model.activateProfile(profile);
        initView();
    }
}
