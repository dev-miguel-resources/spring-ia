package mx.txalcala.springia.controllers;

import java.util.List;

import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/tools")
@RequiredArgsConstructor
public class ToolController {

    private final OpenAiChatModel openAiChatModel;

    @GetMapping
    public ResponseEntity<String> getWeatherInfo() {
        UserMessage userMessage = new UserMessage("What's the weather in San Francisco?");

        ChatResponse chatResponse = openAiChatModel.call(new Prompt(List.of(userMessage),
                OpenAiChatOptions.builder().toolNames("weatherFunction").build()));

        String result = chatResponse.getResult().getOutput().getText();

        return ResponseEntity.ok(result);
    }

    @GetMapping("/book")
    public ResponseEntity<String> getBookInfo(@RequestParam String bookName) {
        UserMessage userMessage = new UserMessage("¿Cuál es la información de este libro " + bookName + "?");

        ChatResponse chatResponse = openAiChatModel.call(new Prompt(List.of(userMessage),
                OpenAiChatOptions.builder().toolNames("bookInfoFunction").build()));

        String result = chatResponse.getResult().getOutput().getText();

        return ResponseEntity.ok(result);

    }

}
