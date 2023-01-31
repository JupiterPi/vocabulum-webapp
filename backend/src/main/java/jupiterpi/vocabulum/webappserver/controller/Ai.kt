package jupiterpi.vocabulum.webappserver.controller.ai;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
public class AiController {
    private AiService aiService = new AiService();

    @PostMapping("/completion")
    public String createCompletion(@RequestBody AiCompletionDTO dto) {
        return aiService.createCompletion(dto.getPrompt());
    }
}