package com.shuttle.game.ui.groups.mvc.profiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.shuttle.game.conf.FileConstants;
import com.shuttle.game.conf.GamePreferences;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.ui.groups.ConfigGroup;
import com.shuttle.game.ui.groups.mvc.Model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Shuttle on 17/06/19.
 */

//todo: remove results also when remove profile (после того как дизайнер заапрувит таблицу результатов). А также удялять результаты построчно. Надо MVC.
public class Profiles implements Model<ProfileDAO> {
    private List<ProfileDAO> profiles;

    private static Profiles instance;

    private FileHandle file;

    private ProfileDAO emptyProfile;

    private static boolean empty;

    public static boolean isEmpty() {
        return empty;
    }

    public Profiles() {
        this.profiles = new ArrayList<ProfileDAO>();
        this.file = Gdx.files.internal(FileConstants.PROFILES);
        this.emptyProfile = asEmpty();
    }

    @Override
    public List<ProfileDAO> getList() {
        return getInstance().profiles;
    }

    //Why? Because it is not good to work with static resources (file e.g.)
    private Profiles getInstance() {
        if (instance == null) {
            instance = new Profiles();
            //First game start
            ObjectMapper mapper = new ObjectMapper();
            try {
                if (instance.file.exists()) {
                    CollectionLikeType collectionLikeType =
                            TypeFactory
                                    .defaultInstance()
                                    .constructCollectionLikeType(List.class, ProfileDAO.class);
                    instance.profiles = mapper.readValue(instance.file.file(), collectionLikeType);
                } else {
                    initDefaultProfiles();
                    mapper.writeValue(instance.file.file(), instance.profiles);
                }
            } catch (IOException e) {
                initDefaultProfiles();
                e.printStackTrace();
            }
            //
        }
        return instance;
    }

    private void initDefaultProfiles() {
        for (int i = 0; i < PaintConstants.DUMMY_PROFILES; i++) {
            ProfileDAO dummy = asDummy();
            getList().add(dummy);
        }
    }

    private static ProfileDAO asEmpty() {
        return new ProfileDAO(ProfileDAO.Type.PROFILE, PaintConstants.UI_EMPTY_PROFILE, GamePreferences.defaults, false);
    }

    private static ProfileDAO asDummy() {
        return new ProfileDAO(ProfileDAO.Type.DUMMY, "", null, false);
    }

    @Override
    public void setEmpty(ProfileDAO profile) {
        preferencesToActiveProfile();
        profile.copy(instance.emptyProfile);
    }

    private void preferencesToActiveProfile() {
        if (getActive() != null) {
            getActive().setPreferences(GamePreferences.getCurrent());
        }
    }

    @Override
    public ProfileDAO getActive() {
        List<ProfileDAO> profiles = getList();
        ProfileDAO profile = null;
        for (int i = 0; i < profiles.size(); i++) {
            if (profiles.get(i).isActive()) {
                profile = profiles.get(i);
            }
        }
        return profile;
    }

    @Override
    public void remove(ProfileDAO profile) {
        List<ProfileDAO> profiles = getList();
        for (int i = 0; i < profiles.size(); i++) {
            if (profiles.get(i).getId().equals(profile.getId())) {
                if (profile.isActive()) {
                    if (i == 0) {
                        profiles.get(i + 1).setActive(true);
                    } else {
                        profiles.get(i - 1).setActive(true);
                    }
                }

                profiles.remove(profile);

                //If have we deleted all from list
                ProfileDAO tmpProfile = profiles.get(0);
                if (tmpProfile.getType() == ProfileDAO.Type.DUMMY) {
                    tmpProfile.setActive(false);
                }
                break;
            }
        }
    }

    @Override
    public ProfileDAO addEmpty() {
        preferencesToActiveProfile();
        ProfileDAO empty = asEmpty();
        getList().add(empty);
        return empty;
    }

    @Override
    public void activate(ProfileDAO profile) {
        List<ProfileDAO> profiles = getList();
        preferencesToActiveProfile();
        for (int i = 0; i < profiles.size(); i++) {
            profiles.get(i).setActive(false);
        }
        profile.setActive(true);
    }

    @Override
    public void addDummy() {
        ProfileDAO dummy = asDummy();
        getList().add(dummy);
    }

    @Override
    public void write() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            if (instance.file.exists()) {
                mapper.writeValue(instance.file.file(), instance.profiles);
            } else {
                initDefaultProfiles();
            }
        } catch (IOException e) {
            initDefaultProfiles();
        }

        empty = true;
        for (int i = 0; i < getList().size(); i++) {
            ProfileDAO profile = getList().get(i);
            if (profile.getType() != ProfileDAO.Type.DUMMY) {
                empty = false;
                break;
            }
        }

        if (getActive() != null) {
            GamePreferences.apply(getActive().getPreferences());
        } else {
            GamePreferences.apply(GamePreferences.defaults);
        }
        ConfigGroup.getInstance().refresh();
    }

    public void dispose() {
        preferencesToActiveProfile();
        write();
    }
}