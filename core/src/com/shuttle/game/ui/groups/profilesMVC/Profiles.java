package com.shuttle.game.ui.groups.profilesMVC;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
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

    private ProfileDAO empty;

    private Profiles() {
        this.profiles = new ArrayList<ProfileDAO>();
        this.file = Gdx.files.internal(FileConstants.PROFILES);
        this.empty = asEmpty();
    }

    public static List<ProfileDAO> getProfiles() {
        return getInstance().profiles;
    }

    private static Profiles getInstance() {
        if (instance == null) {
            instance = new Profiles();
            //First game start
            ObjectMapper mapper = new ObjectMapper();
            try {
                if (instance.file.exists()) {
                    CollectionType collectionType =
                            TypeFactory
                                    .defaultInstance()
                                    .constructCollectionType(ArrayList.class, ProfileDAO.class);
                    instance.profiles = mapper.readValue(instance.file.file(), collectionType);
                } else {
                    initProfiles();
                    mapper.writeValue(instance.file.file(), instance.profiles);
                }
            } catch (IOException e) {
                initProfiles();
            }
            //
        }
        return instance;
    }

    private static void initProfiles() {
        for (int i = 0; i < PaintConstants.DUMMY_PROFILES; i++) {
            instance.profiles.add(new ProfileDAO(ProfileDAO.Type.DUMMY, "", null, false));
        }
    }

    private static ProfileDAO asEmpty() {
        GamePreferences preferences = null;
        return new ProfileDAO(ProfileDAO.Type.PROFILE, "Empty Profile", preferences, false);
    }

    private static ProfileDAO asDummy() {
        return new ProfileDAO(ProfileDAO.Type.DUMMY, "", null, false);
    }

    public static void setEmptyProfile(ProfileDAO profile) {
        profile.copy(instance.empty);
    }

    public static void addEmptyProfile() {
        instance.profiles.add(asEmpty());
    }

    public static void addDummyProfile() {
        instance.profiles.add(asDummy());
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
    }
}
