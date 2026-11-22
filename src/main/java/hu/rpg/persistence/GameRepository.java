package hu.rpg.persistence;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import hu.rpg.domain.*;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class GameRepository {
    private final Gson gson;

    public GameRepository() {
        this.gson = new GsonBuilder()
                .registerTypeAdapter(Fighter.class, new FighterTypeAdapter())
                .setPrettyPrinting()
                .create();
    }

    public void saveRoster(List<Fighter> roster, String filePath) throws IOException {
        try (Writer writer = new FileWriter(filePath)) {
            Type listType = new TypeToken<List<Fighter>>() {}.getType();
            gson.toJson(roster, listType, writer);
        }
    }

    public List<Fighter> loadRoster(String filePath) throws IOException {
        try (Reader reader = new FileReader(filePath)) {
            Type listType = new TypeToken<List<Fighter>>() {}.getType();
            List<Fighter> loaded = gson.fromJson(reader, listType);
            return loaded != null ? loaded : new ArrayList<>();
        }
    }

    private static class FighterTypeAdapter implements JsonSerializer<Fighter>, JsonDeserializer<Fighter> {
        private static final String TYPE_FIELD = "fighterType";
        private static final String DATA_FIELD = "data";

        @Override
        public JsonElement serialize(Fighter src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject wrapper = new JsonObject();
            wrapper.addProperty(TYPE_FIELD, src.getClass().getSimpleName());
            wrapper.add(DATA_FIELD, context.serialize(src, src.getClass()));
            return wrapper;
        }

        @Override
        public Fighter deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            JsonObject wrapper = json.getAsJsonObject();
            String type = wrapper.get(TYPE_FIELD).getAsString();
            JsonElement data = wrapper.get(DATA_FIELD);

            return switch (type) {
                case "Warrior" -> context.deserialize(data, Warrior.class);
                case "Mage" -> context.deserialize(data, Mage.class);
                case "Archer" -> context.deserialize(data, Archer.class);
                case "Rogue" -> context.deserialize(data, Rogue.class);
                case "Paladin" -> context.deserialize(data, Paladin.class);
                case "FlameSwordDecorator" -> context.deserialize(data, FlameSwordDecorator.class);
                case "MagicShieldDecorator" -> context.deserialize(data, MagicShieldDecorator.class);
                default -> throw new JsonParseException("Ismeretlen harcos típus a mentésben: " + type);
            };
        }
    }
}
