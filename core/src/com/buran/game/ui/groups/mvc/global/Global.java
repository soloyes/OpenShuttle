package com.buran.game.ui.groups.mvc.global;

import com.buran.game.ui.groups.mvc.SaveStore;

/**
 * @author soloyes on 23/12/20.
 */

public class Global {
    private static Global instance;
    private GlobalDAO globalDAO;

    public Global() {
        this.globalDAO = new GlobalDAO(false);
    }

    private Global getInstance() {
        if (instance == null) {
            instance = new Global();
            //First game start
            GlobalDAO saved = SaveStore.readGlobal();
            if (saved != null) {
                instance.globalDAO = saved;
            } else {
                instance.globalDAO.setHasStarted(false);
                SaveStore.writeGlobal(instance.globalDAO);
            }
        }
        return instance;
    }

    public void init() {
        getInstance();
    }

    private void write() {
        SaveStore.writeGlobal(instance.globalDAO);
    }

    public void setGlobalStart(boolean flag) {
        instance.globalDAO.setHasStarted(flag);
    }

    public boolean isGlobalStart() {
        return instance.globalDAO.isHasStarted();
    }

    public void dispose() {
        write();
    }
}
