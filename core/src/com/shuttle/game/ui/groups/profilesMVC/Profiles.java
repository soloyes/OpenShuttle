package com.shuttle.game.ui.groups.profilesMVC;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.shuttle.game.conf.FileConstants;
import com.shuttle.game.conf.GamePreferences;
import com.shuttle.game.conf.PaintConstants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Shuttle on 17/06/19.
 */

public class Profiles {
    @JsonIgnore
    private List<ProfileDAO> profiles;

    @JsonIgnore
    private static Profiles instance;

    @JsonIgnore
    private FileHandle file;

    @JsonIgnore
    private ProfileDAO empty;

    @JsonIgnore
    public static boolean isEmpty;

    private Profiles() {
        this.profiles = new ArrayList<ProfileDAO>();
        this.file = Gdx.files.internal(FileConstants.PROFILES);
        this.empty = asEmpty();
    }

    //todo: make implements Model => controller need to know about model. Exit from statics
    public static List<ProfileDAO> getProfileList() {
        return getInstance().profiles;
    }

    //Why? Because it is not good to work with static resources (file e.g.)
    private static Profiles getInstance() {
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
                    initProfiles();
                    mapper.writeValue(instance.file.file(), instance.profiles);
                }
            } catch (IOException e) {
                initProfiles();
                e.printStackTrace();
            }
            //
        }
        return instance;
    }

    private static void initProfiles() {
        for (int i = 0; i < PaintConstants.DUMMY_PROFILES; i++) {
            ProfileDAO dummy = asDummy();
            getProfileList().add(dummy);
            GamePreferences.apply(GamePreferences.defaults);
        }
    }

    private static ProfileDAO asEmpty() {
        return new ProfileDAO(ProfileDAO.Type.PROFILE, PaintConstants.UI_EMPTY_PROFILE, GamePreferences.defaults, false);
    }

    private static ProfileDAO asDummy() {
        return new ProfileDAO(ProfileDAO.Type.DUMMY, "", null, false);
    }

    public static void setEmptyProfile(ProfileDAO profile) {
        profile.copy(instance.empty);
    }

    public static ProfileDAO getActive() {
        List<ProfileDAO> profiles = getProfileList();
        ProfileDAO profile = null;
        for (int i = 0; i < profiles.size(); i++) {
            if (profiles.get(i).isActive()) {
                profile = profiles.get(i);
            }
        }
        return profile;
    }

    public static void removeProfile(ProfileDAO profile) {
        List<ProfileDAO> profiles = getProfileList();
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

    public static ProfileDAO addEmptyProfile() {
        ProfileDAO empty = asEmpty();
        getProfileList().add(empty);
        return empty;
    }

    public static void activateProfile(ProfileDAO profile) {
        List<ProfileDAO> profiles = getProfileList();
        for (int i = 0; i < profiles.size(); i++) {
            profiles.get(i).setActive(false);
        }
        profile.setActive(true);
    }

    public static void addDummyProfile() {
        ProfileDAO dummy = asDummy();
        getProfileList().add(dummy);
    }

    public static void write() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            if (instance.file.exists()) {
                mapper.writeValue(instance.file.file(), instance.profiles);
            } else {
                initProfiles();
            }
        } catch (IOException e) {
            initProfiles();
        }

        isEmpty = true;
        for (int i = 0; i < getProfileList().size(); i++) {
            ProfileDAO profile = getProfileList().get(i);
            if (profile.getType() != ProfileDAO.Type.DUMMY) {
                isEmpty = false;
                break;
            }
        }
    }
}