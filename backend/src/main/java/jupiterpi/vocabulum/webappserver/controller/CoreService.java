package jupiterpi.vocabulum.webappserver.controller;

import jupiterpi.vocabulum.core.db.Database;
import jupiterpi.vocabulum.core.db.LoadingDataException;
import jupiterpi.vocabulum.core.i18n.I18n;
import jupiterpi.vocabulum.core.i18n.I18nException;
import jupiterpi.vocabulum.core.interpreter.lexer.LexerException;
import jupiterpi.vocabulum.core.interpreter.parser.ParserException;
import jupiterpi.vocabulum.core.vocabularies.conjugated.form.VerbFormDoesNotExistException;
import jupiterpi.vocabulum.core.vocabularies.declined.DeclinedFormDoesNotExistException;

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

    public I18n i18n;

    public CoreService() throws LoadingDataException, ParserException, DeclinedFormDoesNotExistException, I18nException, LexerException, VerbFormDoesNotExistException {
        Database.get().connectAndLoad("mongodb://localhost");
        Database.get().prepareWordbase();
        i18n = Database.get().getI18ns().de();
    }
}