package jupiterpi.vocabulum.webappserver.controller

import com.theokanning.openai.OpenAiService
import com.theokanning.openai.completion.CompletionRequest
import jupiterpi.vocabulum.core.util.TextFile
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/ai")
class AiController {
    private val aiService = AiService()

    @PostMapping("/completion")
    fun createCompletion(@RequestBody dto: AiCompletionDTO) = aiService.createCompletion(dto.prompt)
}

data class AiCompletionDTO(
    val prompt: String,
)

class AiService {
    private val openaiApiKey = TextFile.readFile("openai_api_key.txt").lines.first()
    private val openAiService: OpenAiService = OpenAiService(openaiApiKey)

    fun createCompletion(prompt: String): String {
        val completionRequest = CompletionRequest.builder()
            .prompt(prompt)
            .model("text-davinci-003")
            .maxTokens(128)
            .build()
        return openAiService.createCompletion(completionRequest).choices[0].text.trim()
    }
}