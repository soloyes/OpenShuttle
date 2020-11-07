package com.shuttle.game.ui.groups.mvc.profiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.shuttle.game.conf.FileConstants;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.player.Standing;
import com.shuttle.game.screens.ScreenManager;
import com.shuttle.game.ui.groups.mvc.Model;
import com.shuttle.game.ui.groups.mvc.results.DetailedResultDAO;
import com.shuttle.game.ui.groups.mvc.results.ResultDAO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * @author soloyes on 17/06/19.
 */

public class Profiles implements Model<ProfileDAO> {
    private static Profiles instance;
    private static boolean empty;
    private List<ProfileDAO> profiles;
    private FileHandle file;
    private ProfileDAO emptyProfile;

    public Profiles() {
        this.profiles = new ArrayList<ProfileDAO>();
        this.file = Gdx.files.local(FileConstants.PROFILES);
        this.emptyProfile = asEmpty();
    }

    private static ProfileDAO asEmpty() {
        return new ProfileDAO(ProfileDAO.Type.PROFILE, PaintConstants.UI_EMPTY_PROFILE, new SettingsDAO(), new HashSet<ResultDAO>(), false);
    }

    public void init() {
        getInstance();
    }

    @Override
    public ProfileDAO addEmpty() {
        ProfileDAO empty = asEmpty();
        getList().add(empty);
        return empty;
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
            e.printStackTrace();
        }

        empty = true;
        for (int i = 0; i < getList().size(); i++) {
            ProfileDAO profile = getList().get(i);
            if (profile.getType() != ProfileDAO.Type.DUMMY) {
                empty = false;
                break;
            }
        }
    }

    private static ProfileDAO asDummy() {
        return new ProfileDAO(ProfileDAO.Type.DUMMY, "", null, new HashSet<ResultDAO>(), false);
    }

    private void initDefaultProfiles() {
        if (getList().size() < PaintConstants.DUMMY_PROFILES) {
            for (int i = 0; i < PaintConstants.DUMMY_PROFILES; i++) {
                getList().add(asDummy());
            }
        }
    }

    private void resultsToActiveProfile() {
        if (getActive() != null && ScreenManager.getInstance().getCurrentScreen() != ScreenManager.ScreenType.MENU) {
            Standing.getInstance().writeResult();
        }
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

    public static boolean isEmpty() {
        return empty;
    }

    @Override
    public void setEmpty(ProfileDAO profile) {
        profile.copy(instance.emptyProfile);
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

    public void dispose() {
        resultsToActiveProfile();
        write();
    }

    public void flush() {
        dispose();
    }

    @Override
    public void activate(ProfileDAO profile) {
        List<ProfileDAO> profiles = getList();
        for (int i = 0; i < profiles.size(); i++) {
            profiles.get(i).setActive(false);
        }
        profile.setActive(true);
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
    public List<ProfileDAO> getList() {
        return getInstance().profiles;
    }

    public void writeResults(DetailedResultDAO detailedResultDAO) {
        if (getActive() != null) {

            ResultDAO result = new ResultDAO(
                    new Date(),
                    Rules.StartLevel.values()[getActive().getPreferences().getLevel()].name(),
                    detailedResultDAO,
                    getActive().getPreferences().isChild()
            );

            List<ResultDAO> list = getActiveProfileResultList(Rules.StartLevel.values()[getActive().getPreferences().getLevel()].name());
            Set<ResultDAO> set = getActive().getResultSet();

            if (list.size() == 0) {
                result.setCurrent(new DetailedResultDAO(detailedResultDAO));
            } else {
                if (result.getPrevious() == null) {
                    result.setPrevious(new DetailedResultDAO());
                }
                if (list.size() == 1) {
                    result.setPrevious(new DetailedResultDAO(list.get(0).getCurrent()));
                } else {
                    result.setPrevious(new DetailedResultDAO(list.get(1).getCurrent()));
                }

                result.setCurrent(new DetailedResultDAO(detailedResultDAO));
            }

            set.remove(result);
            set.add(result);
        }
    }

    public List<ResultDAO> getActiveProfileResultList(String level) {
        List<ResultDAO> list = new LinkedList<ResultDAO>();
        if (getActive() != null) {
            for (ResultDAO r : getActive().getResultSet()) {
                if (r.getLevel().equals(level)) {
                    list.add(r);
                }
            }
        }
        Collections.sort(list, new Comparator<ResultDAO>() {
            @Override
            public int compare(ResultDAO resultDAO1, ResultDAO resultDAO2) {
                return resultDAO2.getDate().compareTo(resultDAO1.getDate());
            }
        });
        return list;
    }

    public static SettingsDAO getSettings() {
        if (instance.getActive() == null) {
            return new SettingsDAO();
        }
        return instance.getActive().getPreferences();
    }
}