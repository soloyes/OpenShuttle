package com.buran.game.ui.groups.mvc.global;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.buran.game.conf.FileConstants;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * @author soloyes on 23/12/20.
 */

public class Global {
    private static Global instance;
    private FileHandle file;
    private GlobalDAO globalDAO;

    public Global() {
        this.globalDAO = new GlobalDAO(false);
        this.file = Gdx.files.local(FileConstants.GLOBAL);
    }

    private Global getInstance() {
        if (instance == null) {
            instance = new Global();
            //First game start
            ObjectMapper mapper = new ObjectMapper();
            try {
                if (instance.file.exists()) {
                    instance.globalDAO = mapper.readValue(instance.file.file(), GlobalDAO.class);
                } else {
                    initDefault();
                    mapper.writeValue(instance.file.file(), instance.globalDAO);
                }
            } catch (IOException e) {
                initDefault();
                e.printStackTrace();
            }
            //
        }
        return instance;
    }

    private void initDefault() {
        globalDAO.setHasStarted(false);
    }

    public void init() {
        getInstance();
    }

    private void write() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            if (instance.file.exists()) {
                mapper.writeValue(instance.file.file(), instance.globalDAO);
            } else {
                initDefault();
            }
        } catch (IOException e) {
            initDefault();
            e.printStackTrace();
        }
    }

    public void setGlobalStart(boolean flag) {
        instance.globalDAO.setHasStarted(flag);
    }

    public boolean isGlobalStart(){
        return instance.globalDAO.isHasStarted();
    }

    public void dispose(){
        write();
    }
}