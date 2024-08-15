package pl.hexlin.voting;

import org.bson.Document;
import pl.hexlin.Instance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class SuggestionManager {
    public final Map<String, Suggestion> suggestionMap = new HashMap<>();
    public final ArrayList<Suggestion> suggestions;
    private final Instance instance;

    public SuggestionManager(Instance instance) {
        this.suggestions = new ArrayList<>();
        this.instance = instance;
        instance.suggestionCollection.find().forEach((Consumer<? super Document>) document -> {
            Suggestion suggestion = new Suggestion(document, instance);
            suggestionMap.putIfAbsent(suggestion.getId(), suggestion);
            suggestions.add(suggestion);
        });
    }

    public void addSuggestion(Suggestion suggestion) {
        suggestionMap.putIfAbsent(suggestion.getId(), suggestion);
        suggestion.save();
    }

    public void removeSuggestion(Suggestion suggestion) {
        instance.suggestionCollection.find().forEach((Consumer<? super Document>) document -> {
            document.remove(suggestion);
        });
        suggestionMap.remove(suggestion.getId(), suggestion);
        suggestions.remove(suggestion);
        suggestion.save();
    }

    public Suggestion getSuggestion(String suggestionId) {
        return suggestionMap.get(suggestionId);
    }
}
