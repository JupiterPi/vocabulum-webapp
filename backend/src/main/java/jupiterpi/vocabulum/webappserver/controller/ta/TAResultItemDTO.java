package jupiterpi.vocabulum.webappserver.controller.ta;

import jupiterpi.vocabulum.core.ta.TAResult;
import jupiterpi.vocabulum.core.vocabularies.inflexible.Inflexible;
import jupiterpi.vocabulum.webappserver.controller.CoreService;

import java.util.List;

public class TAResultItemDTO {
    private String title;
    private boolean isPunctuation;
    private boolean inflexible;
    private List<String> forms;
    private String definition;
    private List<String> translations;
    private String base_form;

    public TAResultItemDTO(String title, boolean isPunctuation, boolean inflexible, List<String> forms, String definition, List<String> translations, String base_form) {
        this.title = title;
        this.isPunctuation = isPunctuation;
        this.inflexible = inflexible;
        this.forms = forms;
        this.definition = definition;
        this.translations = translations;
        this.base_form = base_form;
    }

    public static TAResultItemDTO fromTAResultItem(TAResult.TAResultItem item) {
        if (item instanceof TAResult.TAPunctuation) {
            TAResult.TAPunctuation punctuation = (TAResult.TAPunctuation) item;
            return new TAResultItemDTO(
                    punctuation.getPunctuation(),
                    true,
                    false, List.of(), "", List.of(), ""
            );
        } else {
            TAResult.TAWord word = (TAResult.TAWord) item;
            return new TAResultItemDTO(
                    word.getWord(),
                    false,
                    word.getVocabulary() instanceof Inflexible,
                    word.getForms(CoreService.get().i18n),
                    word.getVocabulary().getDefinition(CoreService.get().i18n),
                    word.getTranslations(),
                    word.getVocabulary().getBaseForm()
            );
        }
    }

    public String getTitle() {
        return title;
    }

    public boolean isPunctuation() {
        return isPunctuation;
    }

    public boolean isInflexible() {
        return inflexible;
    }

    public List<String> getForms() {
        return forms;
    }

    public String getDefinition() {
        return definition;
    }

    public List<String> getTranslations() {
        return translations;
    }

    public String getBase_form() {
        return base_form;
    }
}
