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
    private FileHandle file;
    private Set<ResultDAO> results = new LinkedHashSet<ResultDAO>();
    private StringBuffer stringBuffer;

    public Set<ResultDAO> getResults() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            CollectionLikeType collectionLikeType =
                    TypeFactory
                            .defaultInstance()
                            .constructCollectionLikeType(Set.class, ResultDAO.class);
            results = mapper.readValue(file.file(), collectionLikeType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return results;
    }

    public Results() {
        stringBuffer = new StringBuffer();
        file = Gdx.files.local(FileConstants.RESULTS);
        if (!file.exists()) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                Set<ResultDAO> results = new LinkedHashSet<ResultDAO>();
                results.add(new ResultDAO());
                mapper.writeValue(file.file(), results);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String shareResults() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            CollectionLikeType collectionLikeType =
                    TypeFactory
                            .defaultInstance()
                            .constructCollectionLikeType(Set.class, ResultDAO.class);
            results = mapper.readValue(file.file(), collectionLikeType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        stringBuffer.setLength(0);
        for (ResultDAO r : results) {
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
            CollectionLikeType collectionLikeType =
                    TypeFactory
                            .defaultInstance()
                            .constructCollectionLikeType(Set.class, ResultDAO.class);
            results = mapper.readValue(file.file(), collectionLikeType);
            results.remove(result);
            results.add(result);
            mapper.writeValue(file.file(), results);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
