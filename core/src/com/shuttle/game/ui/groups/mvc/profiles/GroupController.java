package com.shuttle.game.ui.groups.mvc.profiles;

import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.ui.groups.mvc.Controller;
import com.shuttle.game.ui.groups.mvc.Model;
import com.shuttle.game.ui.groups.mvc.View;

import java.util.List;

/**
 * @author Shuttle on 17/06/19.
 */

public class GroupController implements Controller<com.shuttle.game.ui.groups.mvc.profiles.ProfileDAO> {
    private com.shuttle.game.ui.groups.mvc.View<com.shuttle.game.ui.groups.mvc.profiles.ProfileDAO> view;
    private com.shuttle.game.ui.groups.mvc.Model<com.shuttle.game.ui.groups.mvc.profiles.ProfileDAO> model;

    @Override
    public List<com.shuttle.game.ui.groups.mvc.profiles.ProfileDAO> getList() {
        return model.getList();
    }

    public GroupController(View<com.shuttle.game.ui.groups.mvc.profiles.ProfileDAO> view, Model<com.shuttle.game.ui.groups.mvc.profiles.ProfileDAO> model) {
        this.view = view;
        this.model = model;
    }

    public static boolean isEmpty() {
        return com.shuttle.game.ui.groups.mvc.profiles.Profiles.isEmpty();
    }

    public com.shuttle.game.ui.groups.mvc.profiles.ProfileDAO getActive() {
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
    public com.shuttle.game.ui.groups.mvc.profiles.ProfileDAO add(String s) {
        com.shuttle.game.ui.groups.mvc.profiles.ProfileDAO profile = add();
        profile.setUsername(s);
        return profile;
    }

    @Override
    public void update() {
        initView();
    }

    @Override
    public com.shuttle.game.ui.groups.mvc.profiles.ProfileDAO add() {
        int i;
        com.shuttle.game.ui.groups.mvc.profiles.ProfileDAO tmpProfile = null;
        List<com.shuttle.game.ui.groups.mvc.profiles.ProfileDAO> profiles = model.getList();
        for (i = 0; i < profiles.size(); i++) {
            com.shuttle.game.ui.groups.mvc.profiles.ProfileDAO profile = profiles.get(i);
            if (profile.getType() == com.shuttle.game.ui.groups.mvc.profiles.ProfileDAO.Type.DUMMY) {
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
    public void remove(com.shuttle.game.ui.groups.mvc.profiles.ProfileDAO profile) {
        model.remove(profile);
        initView();
    }

    @Override
    public void select(com.shuttle.game.ui.groups.mvc.profiles.ProfileDAO profile) {
        model.activate(profile);
        initView();
    }

    public void dispose() {
        model.dispose();
    }
}
