package jupiterpi.vocabulum.webappserver.controller.dtos;

import jupiterpi.vocabulum.core.db.lectures.Lectures;

public class ExampleSentenceDTO {
    private String line;
    private int matchStart;
    private int matchEnd;
    private String lecture;
    private int lineIndex;

    public static ExampleSentenceDTO fromExampleLine(Lectures.ExampleLine exampleLine) {
        ExampleSentenceDTO dto = new ExampleSentenceDTO();
        dto.line = exampleLine.getLine();
        dto.matchStart = exampleLine.getStartIndex();
        dto.matchEnd = exampleLine.getEndIndex();
        dto.lecture = exampleLine.getLecture().getName();
        dto.lineIndex = exampleLine.getLineIndex();
        return dto;
    }

    public String getLine() {
        return line;
    }

    public int getMatchStart() {
        return matchStart;
    }

    public int getMatchEnd() {
        return matchEnd;
    }

    public String getLecture() {
        return lecture;
    }

    public int getLineIndex() {
        return lineIndex;
    }
}