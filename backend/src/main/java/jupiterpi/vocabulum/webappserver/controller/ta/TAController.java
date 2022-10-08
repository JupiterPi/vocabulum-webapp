package jupiterpi.vocabulum.webappserver.controller.ta;

import jupiterpi.vocabulum.core.ta.TAException;
import jupiterpi.vocabulum.core.ta.TAResult;
import jupiterpi.vocabulum.core.ta.TranslationAssistance;
import jupiterpi.vocabulum.webappserver.controller.CoreService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/ta")
public class TAController {
    public TAController() {
        CoreService.get();
    }

    @GetMapping("/search/{query}")
    public List<TAResultItemDTO> performTranslationAssistance(@PathVariable String query) throws TAException {
        TAResult result = new TranslationAssistance(query).getResult();
        List<TAResultItemDTO> dtos = new ArrayList<>();
        for (TAResult.TAResultItem item : result.getItems()) {
            dtos.add(TAResultItemDTO.fromTAResultItem(item));
        }
        return dtos;
    }
}
