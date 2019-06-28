package com.shuttle.game.ui.groups.mvc.profiles;

import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.ui.groups.mvc.Controller;
import com.shuttle.game.ui.groups.mvc.Model;
import com.shuttle.game.ui.groups.mvc.View;

import java.util.List;

/**
 * @author Shuttle on 17/06/19.
 */

public class GroupController implements Controller<ProfileDAO> {
    private View<ProfileDAO> view;
    private Model<ProfileDAO> model;

    @Override
    public List<ProfileDAO> getList() {
        return model.getList();
    }

    public GroupController(View<ProfileDAO> view, Model<ProfileDAO> model) {
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
        while (model.getList().size() < PaintConstants.DUMMY_PROFILES) {
            model.addDummy();
        }
        model.write();
        view.init();
    }

    @Override
    public ProfileDAO add(String s) {
        ProfileDAO profile = add();
        profile.setUsername(s);
        return profile;
    }

    @Override
    public void update() {
        initView();
    }

    @Override
    public ProfileDAO add() {
        int i;
        ProfileDAO tmpProfile = null;
        List<ProfileDAO> profiles = model.getList();
        for (i = 0; i < profiles.size(); i++) {
            ProfileDAO profile = profiles.get(i);
            if (profile.getType() == ProfileDAO.Type.DUMMY) {
                tmpProfile = profile;
                model.setEmpty(profile);
                break;
            }
        }
        if (i == profiles.size()) {
            tmpProfile = model.addEmpty();
        }
        initView();
        return tmpProfile;
    }

    @Override
    public void remove(ProfileDAO profile) {
        model.remove(profile);
        initView();
    }

    @Override
    public void select(ProfileDAO profile) {
        model.activate(profile);
        initView();
    }

    public void dispose() {
        model.dispose();
    }
}
