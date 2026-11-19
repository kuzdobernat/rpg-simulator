package hu.rpg.persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import hu.rpg.domain.Warrior;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

public class GameRepository {
    private final Gson gson;

    public GameRepository() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public void saveWarrior(Warrior warrior, String filePath) throws IOException {
        try (Writer writer = new FileWriter(filePath)) {
            gson.toJson(warrior, writer);
        }
    }

    public Warrior loadWarrior(String filePath) throws IOException {
        try (Reader reader = new FileReader(filePath)) {
            return gson.fromJson(reader, Warrior.class);
        }
    }
}
