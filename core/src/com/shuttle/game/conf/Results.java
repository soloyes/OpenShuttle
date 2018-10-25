package com.shuttle.game.conf;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Shuttle on 7/14/18.
 */
public class Results {
    private FileHandle resultsFile;
    private List<String> results = new ArrayList<String>();

    public Results() {
        resultsFile = Gdx.files.local("results");
        if (!resultsFile.exists()) {
            resultsFile.writeString("results", true);
        }
    }

    public List<String> getResults() {
        results = Arrays.asList(resultsFile.readString().split("\n"));
        if (results.size() == 1) {
            return null;
        } else {
            return results;
        }
    }

    public void setResult(String result) {
        results = Arrays.asList(resultsFile.readString().split("\n"));
        boolean exists = false;
        for (int i = 1; i < results.size(); i++) {
            if (results.get(i).split(" ")[0].equals(result.split(" ")[0]) &&
                    results.get(i).split(" ")[3].equals(result.split(" ")[3])
                    ) {
                exists = true;
                results.set(i, result.replace("\n", ""));
                break;
            }
        }

        resultsFile.writeString("results\n", false);

        for (int i = 1; i < results.size(); i++) {
            resultsFile.writeString(results.get(i) + "\n", true);
        }

        if (!exists) {
            resultsFile.writeString(result, true);
        }
    }
}
