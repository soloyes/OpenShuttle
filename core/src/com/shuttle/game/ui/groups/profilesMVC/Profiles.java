package com.shuttle.game.ui.groups.profilesMVC;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapLikeType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.shuttle.game.conf.FileConstants;
import com.shuttle.game.conf.GamePreferences;
import com.shuttle.game.conf.PaintConstants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Shuttle on 17/06/19.
 */

public class Profiles {
    @JsonIgnore
    private Map<String, ProfileDAO> profiles;

    @JsonIgnore
    private static Profiles instance;

    @JsonIgnore
    private FileHandle file;

    @JsonIgnore
    private ProfileDAO empty;

    @JsonIgnore
    private static List<ProfileDAO> list = new ArrayList<ProfileDAO>();

    private Profiles() {
        this.profiles = new HashMap<String, ProfileDAO>();
        this.file = Gdx.files.internal(FileConstants.PROFILES);
        this.empty = asEmpty();
    }

    public static Map<String, ProfileDAO> getProfileMap() {
        return getInstance().profiles;
    }

    public static List<ProfileDAO> getProfileList() {
        list.clear();
        list.addAll(getInstance().profiles.values());
        return list;
    }

    private static Profiles getInstance() {
        if (instance == null) {
            instance = new Profiles();
            //First game start
            ObjectMapper mapper = new ObjectMapper();
            try {
                if (instance.file.exists()) {
                    MapLikeType mapLikeType =
                            TypeFactory
                                    .defaultInstance()
                                    .constructMapLikeType(Map.class, String.class, ProfileDAO.class);
                    instance.profiles = mapper.readValue(instance.file.file(), mapLikeType);
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
            instance.profiles.put(dummy.getId(), dummy);
            GamePreferences.apply(GamePreferences.defaults);
        }
    }

    private static ProfileDAO asEmpty() {
        return new ProfileDAO(ProfileDAO.Type.PROFILE, "Empty Profile", GamePreferences.defaults, false);
    }

    private static ProfileDAO asDummy() {
        return new ProfileDAO(ProfileDAO.Type.DUMMY, "", null, false);
    }

    public static void setEmptyProfile(ProfileDAO profile) {
        profile.copy(instance.empty);
    }

    public static void removeProfile(ProfileDAO profile) {
        Map<String, ProfileDAO> profiles = getProfileMap();
        profiles.remove(profile.getId());
    }

    public static void addEmptyProfile() {
        ProfileDAO empty = asEmpty();
        instance.profiles.put(empty.getId(), empty);
    }

    private static void activateProfile(ProfileDAO profile) {
        List<ProfileDAO> profiles = getProfileList();
        for (int i = 0; i < profiles.size(); i++) {
            profiles.get(i).setActive(false);
        }
        profile.setActive(true);
    }

    public static void addDummyProfile() {
        ProfileDAO dummy = asDummy();
        instance.profiles.put(dummy.getId(), dummy);
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
