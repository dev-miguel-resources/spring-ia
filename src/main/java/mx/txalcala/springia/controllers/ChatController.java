package mx.txalcala.springia.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import mx.txalcala.springia.Utils.ChatHistory;
import mx.txalcala.springia.dtos.AuthorBook;
import mx.txalcala.springia.dtos.AuthorBook2;
import mx.txalcala.springia.dtos.ResponseDTO;

@RestController
@RequestMapping("/chats")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ChatController {

    // Modelo de ChatBot: Chat-GPT
    private final OpenAiChatModel openAiChatModel;
    private final ChatHistory chatHistory;
    private final ChatMemory chatMemory;
    private final JdbcChatMemoryRepository chatMemoryRepository;

    // Método 1: Consulta normal mediante un Prompt
    @GetMapping("/generate")
    public ResponseEntity<ResponseDTO<String>> generateText(@RequestParam String message) {
        // tenemos el contenido de la llamada de acuerdo al prompt.
        ChatResponse chatResponse = openAiChatModel.call(new Prompt(message));
        // Abstraer el contenido que devolvió la respuesta de la linea anterior.
        String result = chatResponse.getResult().getOutput().getText();

        // Devolvemos el formato de salida como respuesta http.
        return ResponseEntity.ok(new ResponseDTO<>(200, "success", result));
    }

    // Método 2: Consulta mediante un Prompt con el uso de templates (plantillas)
    @GetMapping("/generate/prompt")
    public ResponseEntity<ResponseDTO<String>> generatePrompt(@RequestParam String author,
            @RequestParam String bookName) {
        // Creación de un template
        PromptTemplate promptTemplate = new PromptTemplate(
                "Tell me about ${author} and his ${bookName} and only print 500 characters");
        Prompt prompt = promptTemplate.create(Map.of("author", author, "bookName", bookName));

        ChatResponse chatResponse = openAiChatModel.call(prompt);
        String result = chatResponse.getResult().getOutput().getText();

        return ResponseEntity.ok(new ResponseDTO<>(200, "success", result));
    }

    // Método 3: Consulta mediante un Prompt + templates haciendo uso de un Record
    // Class personalizado
    @GetMapping("/generate/output")
    public ResponseEntity<AuthorBook> generateOutput(@RequestParam String author) {

        // Preparar la definición de salida del output
        BeanOutputConverter<AuthorBook> outputConverter = new BeanOutputConverter<>(AuthorBook.class);

        String template = """
                Tell me book title of ${author}. {format}
                """;

        PromptTemplate promptTemplate = new PromptTemplate(template);
        Prompt prompt = promptTemplate.create(Map.of("author", author, "format", outputConverter.getFormat()));

        ChatResponse chatResponse = openAiChatModel.call(prompt);
        String result = chatResponse.getResult().getOutput().getText();

        // Convertir el resultado a una instancia de AuthorBook
        AuthorBook authorBook = outputConverter.convert(result);

        // retorno el contenido
        return ResponseEntity.ok(authorBook);
    }

    // Método 4: Consulta mediante un Prompt + templates haciendo uso de Model
    // Class personalizado
    @GetMapping("/generate/output2")
    public ResponseEntity<AuthorBook2> generateOutput2(@RequestParam String author) {

        // Preparar la definición de salida del output
        BeanOutputConverter<AuthorBook2> outputConverter = new BeanOutputConverter<>(AuthorBook2.class);

        String template = """
                Tell me book title of ${author}. {format}
                """;

        PromptTemplate promptTemplate = new PromptTemplate(template);
        Prompt prompt = promptTemplate.create(Map.of("author", author, "format", outputConverter.getFormat()));

        ChatResponse chatResponse = openAiChatModel.call(prompt);
        String result = chatResponse.getResult().getOutput().getText();

        // Convertir el resultado a una instancia de AuthorBook
        AuthorBook2 authorBook = outputConverter.convert(result);

        // retorno el contenido
        return ResponseEntity.ok(authorBook);
    }

    // Método 5: Modelo conversacional para referencias de contextos anteriores.
    // Histórico de conversaciones mediante ChatHistory = mantiene todos los
    // mensajes previos sin ninguna condición.
    @GetMapping("/generateConversation")
    public ResponseEntity<ResponseDTO<String>> generateConversation(@RequestParam String message) {

        // Quiero recuperar un mensaje con lo que el usuario está mandando en la
        // solicitud
        // chatId: tienen que ser String
        // String username = SecurityContextHolder.getAuthentication().getName();
        // username
        chatHistory.addMessage("1", new UserMessage(message));

        // quiero obtener todos aquellos mensajes del id 1
        ChatResponse chatResponse = openAiChatModel.call(new Prompt(chatHistory.getAll("1")));

        String result = chatResponse.getResult().getOutput().getText();

        return ResponseEntity.ok(new ResponseDTO<>(200, "success", result));
    }

    // Método 6: Utiliza un default size de 20 mensajes como máximo, luego
    // sobreescribe el más antiguo. Formato: FIFO
    @GetMapping("/memory")
    public ResponseEntity<ResponseDTO<String>> memory(@RequestParam String message) {
        chatMemory.add("1", List.of(new UserMessage(message)));

        // recuperar los mensajes con el alias 1.
        ChatResponse chatResponse = openAiChatModel.call(new Prompt(chatMemory.get("1")));
        String result = chatResponse.getResult().getOutput().getText();

        return ResponseEntity.ok(new ResponseDTO<>(200, "success", result));
    }

    @GetMapping("/memoryrepo")
    public ResponseEntity<ResponseDTO<String>> memoryRepo(@RequestParam String username,
            @RequestParam String message) {

        ChatMemory chatMemoryRepo = MessageWindowChatMemory.builder()
                .chatMemoryRepository(chatMemoryRepository)
                .maxMessages(5) // FIFO
                .build();

        chatMemoryRepo.add(username, List.of(new UserMessage(message)));

        // recuperar los mensajes con el username requerido
        ChatResponse chatResponse = openAiChatModel.call(new Prompt(chatMemory.get(username)));
        String result = chatResponse.getResult().getOutput().getText();

        return ResponseEntity.ok(new ResponseDTO<>(200, "success", result));
    }

}
