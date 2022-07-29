package jupiterpi.vocabulum.webappserver.test;

import jupiterpi.vocabulum.core.db.LoadingDataException;
import jupiterpi.vocabulum.core.i18n.I18n;
import jupiterpi.vocabulum.core.i18n.I18nException;
import jupiterpi.vocabulum.core.i18n.I18nManager;
import jupiterpi.vocabulum.core.interpreter.lexer.LexerException;
import jupiterpi.vocabulum.core.interpreter.parser.ParserException;
import jupiterpi.vocabulum.core.portions.Portion;
import jupiterpi.vocabulum.core.portions.PortionManager;
import jupiterpi.vocabulum.core.vocabularies.Vocabulary;
import jupiterpi.vocabulum.core.vocabularies.conjugated.form.VerbFormDoesNotExistException;
import jupiterpi.vocabulum.core.vocabularies.conjugated.schemas.ConjugationClasses;
import jupiterpi.vocabulum.core.vocabularies.declined.DeclinedFormDoesNotExistException;
import jupiterpi.vocabulum.core.vocabularies.declined.schemas.DeclensionClasses;
import jupiterpi.vocabulum.core.wordbase.WordbaseManager;

import java.util.Map;

public class CoreService {
    private static CoreService instance = null;
    public static CoreService get() {
        if (instance == null) {
            try {
                instance = new CoreService();
            } catch (LoadingDataException | ParserException | DeclinedFormDoesNotExistException | I18nException |
                     LexerException | VerbFormDoesNotExistException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    // ----- //

    public I18nManager i18nManager = new I18nManager();
    public I18n i18n = i18nManager.de;
    public PortionManager portionManager;
    public WordbaseManager wordbaseManager;

    public CoreService() throws LoadingDataException, ParserException, DeclinedFormDoesNotExistException, I18nException, LexerException, VerbFormDoesNotExistException {
        DeclensionClasses.loadDeclensionSchemas();
        ConjugationClasses.loadConjugationSchemas();

        portionManager = new PortionManager();
        wordbaseManager = new WordbaseManager();

        Map<String, Portion> portions = portionManager.getPortions();
        for (String key : portions.keySet()) {
            Portion portion = portions.get(key);
            System.out.println(portion);
        }

        wordbaseManager.clearAll();
        for (Portion portion : portions.values()) {
            System.out.println(portion.getName());
            for (Vocabulary vocabulary : portion.getVocabularies()) {
                System.out.println(vocabulary);
                wordbaseManager.saveVocabulary(vocabulary);
            }
            System.out.println();
        }
    }
}