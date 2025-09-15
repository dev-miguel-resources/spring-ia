package mx.txalcala.springia.controllers;

import java.util.List;

import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.openai.OpenAiImageModel;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import mx.txalcala.springia.dtos.ResponseDTO;

@RestController
@RequestMapping("/images")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ImageController {

    private final OpenAiImageModel openAiImageModel;

    @GetMapping("/generate")
    public ResponseEntity<ResponseDTO<String>> generateImage(@RequestParam String param) {

        ImageResponse imageResponse = openAiImageModel.call(new ImagePrompt(param,
                OpenAiImageOptions.builder()
                        .model("dall-e-3") // dall-e-3 ó dall-e-2
                        .quality("hd") // puede ser hd ó standard
                        .N(1) // Número de imagenes a generar (DALL-E-3 solo permite 1 imagen)
                        .height(1024) // Altura de la imagen
                        .width(1024) // Ancho de la imagen
                        .build()));

        // Obtenemos la url de la imagen generada
        String url = imageResponse.getResult().getOutput().getUrl();

        return ResponseEntity.ok(new ResponseDTO<>(200, "success", url));
    }

    @GetMapping("/generate/dalle2")
    public ResponseEntity<ResponseDTO<List<String>>> generateImage2(@RequestParam String param) {

        ImageResponse imageResponse = openAiImageModel.call(new ImagePrompt(param,
                OpenAiImageOptions.builder()
                        .model("dall-e-2") // dall-e-3 ó dall-e-2
                        .N(3) // Número de imagenes a generar (DALL-E-2 permite varias)
                        .height(256) // Altura de la imagen (imágenes más rápido y más ligeras)
                        .width(256) // Ancho de la imagen (imágenes más rápido y más ligeras)
                        .build()));

        // Recorremos los resultados y se extrae la URL de cada imagen generada
        List<String> images = imageResponse.getResults()
                .stream()
                .map(e -> e.getOutput().getUrl())
                .toList();

        return ResponseEntity.ok(new ResponseDTO<>(200, "success", images));
    }

    @GetMapping("/generateB64")
    public ResponseEntity<ResponseDTO<String>> generateImageB64(@RequestParam String param) {

        ImageResponse imageResponse = openAiImageModel.call(new ImagePrompt(param,
                OpenAiImageOptions.builder()
                        .model("dall-e-3") // dall-e-3 ó dall-e-2
                        .quality("standard")
                        .N(1)
                        .height(1024)
                        .width(1024)
                        .responseFormat("b64_json") // Se solicita respuesta en formato Base 64 en JSON
                        .build()));

        // Recorremos los resultados y se extrae la URL de cada imagen generada
        String b64 = imageResponse.getResult().getOutput().getB64Json();

        return ResponseEntity.ok(new ResponseDTO<>(200, "success", b64));
    }

}
