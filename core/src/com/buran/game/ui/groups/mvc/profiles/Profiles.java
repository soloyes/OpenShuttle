package com.buran.game.ui.groups.mvc.profiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.player.Standing;
import com.shuttle.game.screens.ScreenManager;
import com.shuttle.game.ui.groups.mvc.profiles.SettingsDAO;
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

public class Profiles implements com.buran.game.ui.groups.mvc.Model<ProfileDAO> {
    private static Profiles instance;
    private static boolean empty;
    private List<com.shuttle.game.ui.groups.mvc.profiles.ProfileDAO> profiles;
    private FileHandle file;
    private com.shuttle.game.ui.groups.mvc.profiles.ProfileDAO emptyProfile;

    public Profiles() {
        this.profiles = new ArrayList<com.shuttle.game.ui.groups.mvc.profiles.ProfileDAO>();
        this.file = Gdx.files.local(com.buran.game.conf.FileConstants.PROFILES);
        this.emptyProfile = asEmpty();
    }

    private static com.shuttle.game.ui.groups.mvc.profiles.ProfileDAO asEmpty() {
        return new com.shuttle.game.ui.groups.mvc.profiles.ProfileDAO(com.shuttle.game.ui.groups.mvc.profiles.ProfileDAO.Type.PROFILE, PaintConstants.UI_EMPTY_PROFILE, new com.shuttle.game.ui.groups.mvc.profiles.SettingsDAO(), new HashSet<ResultDAO>(), false);
    }

    public void init() {
        getInstance();
    }

    @Override
    public com.shuttle.game.ui.groups.mvc.profiles.ProfileDAO addEmpty() {
        com.shuttle.game.ui.groups.mvc.profiles.ProfileDAO empty = asEmpty();
        getList().add(empty);
        return empty;
    }

    @Override
    public void addDummy() {
        com.shuttle.game.ui.groups.mvc.profiles.ProfileDAO dummy = asDummy();
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
            com.shuttle.game.ui.groups.mvc.profiles.ProfileDAO profile = getList().get(i);
            if (profile.getType() != com.shuttle.game.ui.groups.mvc.profiles.ProfileDAO.Type.DUMMY) {
                empty = false;
                break;
            }
        }
    }

    private static com.shuttle.game.ui.groups.mvc.profiles.ProfileDAO asDummy() {
        return new com.shuttle.game.ui.groups.mvc.profiles.ProfileDAO(com.shuttle.game.ui.groups.mvc.profiles.ProfileDAO.Type.DUMMY, "", null, new HashSet<ResultDAO>(), false);
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
                                    .constructCollectionLikeType(List.class, com.shuttle.game.ui.groups.mvc.profiles.ProfileDAO.class);
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
    public void setEmpty(com.shuttle.game.ui.groups.mvc.profiles.ProfileDAO profile) {
        profile.copy(instance.emptyProfile);
    }

    @Override
    public void remove(com.shuttle.game.ui.groups.mvc.profiles.ProfileDAO profile) {
        List<com.shuttle.game.ui.groups.mvc.profiles.ProfileDAO> profiles = getList();
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
                com.shuttle.game.ui.groups.mvc.profiles.ProfileDAO tmpProfile = profiles.get(0);
                if (tmpProfile.getType() == com.shuttle.game.ui.groups.mvc.profiles.ProfileDAO.Type.DUMMY) {
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
    public void activate(com.shuttle.game.ui.groups.mvc.profiles.ProfileDAO profile) {
        List<com.shuttle.game.ui.groups.mvc.profiles.ProfileDAO> profiles = getList();
        for (int i = 0; i < profiles.size(); i++) {
            profiles.get(i).setActive(false);
        }
        profile.setActive(true);
    }

    @Override
    public com.shuttle.game.ui.groups.mvc.profiles.ProfileDAO getActive() {
        List<com.shuttle.game.ui.groups.mvc.profiles.ProfileDAO> profiles = getList();
        com.shuttle.game.ui.groups.mvc.profiles.ProfileDAO profile = null;
        for (int i = 0; i < profiles.size(); i++) {
            if (profiles.get(i).isActive()) {
                profile = profiles.get(i);
            }
        }
        return profile;
    }

    @Override
    public List<com.shuttle.game.ui.groups.mvc.profiles.ProfileDAO> getList() {
        return getInstance().profiles;
    }

    public void writeResults(com.buran.game.ui.groups.mvc.results.DetailedResultDAO detailedResultDAO) {
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
                result.setCurrent(new com.buran.game.ui.groups.mvc.results.DetailedResultDAO(detailedResultDAO));
            } else {
                if (result.getPrevious() == null) {
                    result.setPrevious(new com.buran.game.ui.groups.mvc.results.DetailedResultDAO());
                }
                if (list.size() == 1) {
                    result.setPrevious(new com.buran.game.ui.groups.mvc.results.DetailedResultDAO(list.get(0).getCurrent()));
                } else {
                    result.setPrevious(new com.buran.game.ui.groups.mvc.results.DetailedResultDAO(list.get(1).getCurrent()));
                }

                result.setCurrent(new com.buran.game.ui.groups.mvc.results.DetailedResultDAO(detailedResultDAO));
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

    public static com.shuttle.game.ui.groups.mvc.profiles.SettingsDAO getSettings() {
        if (instance.getActive() == null) {
            return new SettingsDAO();
        }
        return instance.getActive().getPreferences();
    }
}