package jupiterpi.vocabulum.webappserver.controller.ai;

import com.theokanning.openai.OpenAiService;
import com.theokanning.openai.completion.CompletionRequest;
import jupiterpi.tools.files.TextFile;

public class AiService {
    private final String OPENAI_API_KEY;
    private OpenAiService openAiService;

    public AiService() {
        OPENAI_API_KEY = new TextFile("openai_api_key.txt").getLine(0);
        openAiService = new OpenAiService(OPENAI_API_KEY);
    }

    public String createCompletion(String prompt) {
        CompletionRequest completionRequest = CompletionRequest.builder()
                .prompt(prompt)
                .model("text-davinci-003")
                .maxTokens(128)
                .build();
        return openAiService.createCompletion(completionRequest).getChoices().get(0).getText().trim();
    }
}