package jupiterpi.vocabulum.webappserver.dtos;

public class SearchResultDTO {
    private boolean isInflexible;
    private String matchedForm;
    private int matchStart;
    private int matchEnd;
    private String inForm;
    private int additionalFormsCount;
    private PreviewVocabularyDTO vocabulary;

    public SearchResultDTO(boolean isInflexible, String matchedForm, int matchStart, int matchEnd, String inForm, int additionalFormsCount, PreviewVocabularyDTO vocabulary) {
        this.isInflexible = isInflexible;
        this.matchedForm = matchedForm;
        this.matchStart = matchStart;
        this.matchEnd = matchEnd;
        this.inForm = inForm;
        this.additionalFormsCount = additionalFormsCount;
        this.vocabulary = vocabulary;
    }

    public boolean isInflexible() {
        return isInflexible;
    }

    public String getMatchedForm() {
        return matchedForm;
    }

    public int getMatchStart() {
        return matchStart;
    }

    public int getMatchEnd() {
        return matchEnd;
    }

    public String getInForm() {
        return inForm;
    }

    public int getAdditionalFormsCount() {
        return additionalFormsCount;
    }

    public PreviewVocabularyDTO getVocabulary() {
        return vocabulary;
    }
}