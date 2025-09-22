package mx.txalcala.springia.controllers;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiImageModel;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.ai.content.Media;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/multis")
@RequiredArgsConstructor
public class MultiModalityOCRController {

    private final OpenAiChatModel openAiChatModel;
    private final OpenAiImageModel openAiImageModel;

    @GetMapping
    public String multimodality() {
        ClassPathResource classPathResource = new ClassPathResource("/images/image1.jpg");

        // Construir el mensaje que hará de prompt.
        UserMessage userMessage = UserMessage.builder()
                .text("¿Explicame que ves en esta imagen? e inventame una historia alrededor de ello")
                .media(new Media(MimeTypeUtils.IMAGE_JPEG, classPathResource)).build();

        // Enviar el prompt (mensaje + imagen) al modelo de chat
        ChatResponse chatResponse = openAiChatModel.call(new Prompt(List.of(userMessage)));

        // Extraer el resultado en texto
        String result = chatResponse.getResult().getOutput().getText();

        return result;
    }

    @GetMapping("/image")
    public String multimodalityURL() throws Exception {

        UserMessage userMessage = UserMessage.builder()
                .text("¿Explicame que ves en esta imagen? y si pertenece a una pelicula")
                .media(new Media(MimeTypeUtils.IMAGE_JPEG, new URI(
                        "https://media.istockphoto.com/id/1186954832/es/foto/peque%C3%B1o-gatito-negro-jugando-y-disfruta-con-bola-naranja-en-la-sala-de-estar-de-la-casa.jpg?s=612x612&w=0&k=20&c=JuJ1cJegjH7GqrPkwmhVq77JOSLyJfE3fz5Z_8Fbq2k=")
                        .toURL().toURI()))
                .build();

        ChatResponse chatResponse = openAiChatModel.call(new Prompt(List.of(userMessage)));

        String result = chatResponse.getResult().getOutput().getText();

        return result;
    }

    @GetMapping("/image2")
    public String multiModalityURL2() throws Exception {
        UserMessage userMessage = UserMessage.builder()
                .text("Explicame que ves en cada imagen? dime si es de una pelicula e inventa una historia de ambos")
                .media(List.of(
                        // Primera imagen desde URL
                        new Media(MimeTypeUtils.IMAGE_JPEG, new URI(
                                "https://img.europapress.es/fotoweb/fotonoticia_20220412165352_1200.jpg")
                                .toURL().toURI()),
                        // Segunda imagen desde otra URL
                        new Media(MimeTypeUtils.IMAGE_JPEG, new URI(
                                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRkr4crv5vRotmYd78_VghLFcnM73R-9CES1g&s")
                                .toURL().toURI())))
                .build();

        ChatResponse chatResponse = openAiChatModel.call(new Prompt(List.of(userMessage)));

        String result = chatResponse.getResult().getOutput().getText();

        return result;
    }

    @GetMapping("/upload")
    public String multiModalityUpload(@RequestParam("image") MultipartFile imageFile) throws Exception {
        UserMessage userMessage = UserMessage.builder()
                .text("Explicame que ves en esta imagen? e inventame una historia")
                .media(List.of(
                        new Media(MimeTypeUtils.IMAGE_JPEG, new ByteArrayResource(imageFile.getBytes()))))
                .build();

        ChatResponse chatResponse = openAiChatModel.call(new Prompt(List.of(userMessage)));

        String description = chatResponse.getResult().getOutput().getText();

        // Con la descripción generada, le pedimos al modelo de imagen que cree un
        // avatar/portada en base a esa historia
        String url = openAiImageModel.call(
                new ImagePrompt("Generame un avatar o portada de esta descripción: " + description,
                        OpenAiImageOptions.builder()
                                .model("dall-e-3")
                                .quality("hd")
                                .N(1)
                                .height(1024)
                                .width(1024)
                                .build()))
                .getResult().getOutput().getUrl(); // Obtenemos la URL de la imagen generada

        return url;
    }

}
