package com.shuttle.game.ui.groups.mvc.results;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.shuttle.game.conf.FileConstants;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Shuttle on 7/14/18.
 */
public class Results {
    private static Results instance;
    private FileHandle file;
    private Set<ResultDAO> results;
    private StringBuffer stringBuffer;

    private Results getInstance() {
        if (instance == null) {
            instance = new Results();
            //First game start
            ObjectMapper mapper = new ObjectMapper();
            try {
                if (file.exists()) {
                    CollectionLikeType collectionLikeType =
                            TypeFactory
                                    .defaultInstance()
                                    .constructCollectionLikeType(Set.class, ResultDAO.class);
                    instance.results = mapper.readValue(instance.file.file(), collectionLikeType);
                } else {
                    instance.results.add(new ResultDAO());
                }
            } catch (IOException e) {
                instance.results.add(new ResultDAO());
                e.printStackTrace();
            }
            //
        }
        return instance;
    }

    public Set<ResultDAO> getResults() {
        return getInstance().results;
    }

    public Results() {
        this.stringBuffer = new StringBuffer();
        this.results = new LinkedHashSet<ResultDAO>();
        this.file = Gdx.files.local(FileConstants.RESULTS);
    }

    public String shareResults() {
        stringBuffer.setLength(0);
        for (ResultDAO r : getInstance().results) {
            if (!r.getUsername().isEmpty()) {
                stringBuffer
                        .append(r.getUsername())
                        .append(" ")
                        .append(r.getScore())
                        .append(" ")
                        .append(r.getDate())
                        .append(" ")
                        .append(r.getLevel())
                        .append("\n");
            }
        }
        return stringBuffer.toString();
    }

    public void addResult(ResultDAO result) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            instance.results.remove(result);
            instance.results.add(result);
            mapper.writeValue(instance.file.file(), instance.results);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
