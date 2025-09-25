package mx.txalcala.springia.controllers;

import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.ai.openai.OpenAiAudioSpeechModel;
import org.springframework.ai.openai.OpenAiAudioSpeechOptions;
import org.springframework.ai.openai.api.OpenAiAudioApi;
import org.springframework.ai.openai.audio.speech.SpeechPrompt;
import org.springframework.ai.openai.audio.speech.SpeechResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/tts")
@RequiredArgsConstructor
public class TTSController {

    private final OpenAiAudioSpeechModel openAiAudioSpeechModel;

    @GetMapping
    public byte[] tts(@RequestParam String message) throws Exception {

        OpenAiAudioSpeechOptions options = OpenAiAudioSpeechOptions.builder()
                .responseFormat(OpenAiAudioApi.SpeechRequest.AudioResponseFormat.MP3)
                .voice(OpenAiAudioApi.SpeechRequest.Voice.ONYX)
                .speed(2.0f)
                .model(OpenAiAudioApi.TtsModel.TTS_1_HD.value)
                .build();

        // Crear el speech Prompt cn el texto recibido + las opciones de configuración.
        SpeechPrompt prompt = new SpeechPrompt(message, options);

        // Llamada para generar el audio a partir del texto.
        SpeechResponse response = openAiAudioSpeechModel.call(prompt);

        byte[] responseBytes = response.getResult().getOutput();

        Path directory = Paths.get("src/main/resources/audios/tts/");
        if (!Files.notExists(directory)) {
            Files.createDirectories(directory);
        }

        // Definir la ruta y nombre del archivo de salida.
        Path filePath = directory.resolve("tts_1.mp3");

        // Guardar el archivo MP3 en el sistema de archivos
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(responseBytes)) {
            // Copiar el contenido en el archivo, reemplazando en el ya existente
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        }

        return responseBytes;

    }

}
