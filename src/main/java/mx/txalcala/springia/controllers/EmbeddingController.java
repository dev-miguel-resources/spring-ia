package mx.txalcala.springia.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/embeddings")
@RequiredArgsConstructor
public class EmbeddingController {

    // Para trabajar con texto y convertirlos en valores númericos (vectores)
    private final OpenAiEmbeddingModel openAiEmbeddingModel;

    // Para guardar y consultar los vectores mediante búsquedas semánticas.
    private final VectorStore vectorStore;

    @GetMapping("/generate")
    public Map<String, EmbeddingResponse> generate(@RequestParam String message) {
        // Convertir el texto recibido en una lista de embeddings usando el modelo de
        // Open IA.
        // El resultado incluye el vector número que representa el significado del
        // texto.
        EmbeddingResponse response = openAiEmbeddingModel.embedForResponse(List.of(message));

        // Devolvemos el embedding generado dentro de un mapa JSON con la clave
        // "embedding"
        return Map.of("embedding", response);
    }

    @GetMapping("/vectorstore")
    public List<Document> useVectorStore(@RequestParam String message) {

        // Crear algunos documentos de ejemplo que serán convertidos a embeddings.
        // Cada Document representa un bloque de texto con la data a almacenar como
        // vectores con metadata opcional.
        List<Document> documents = List.of(
                new Document("Spring AI es lo máximo", Map.of("meta-id-1", "meta-value1")),
                new Document("Python es más popular en IA"),
                new Document("El futuro es la inteligencia artificial", Map.of("meta-id-2", "meta-value2")));

        // Agregar los documentos al vector store.
        // Esto internamente crea embeddings para cada uno y los guarda.
        vectorStore.add(documents);

        // Realizar una búsqueda semántica.
        // Se convierte el texto del usuario "message" en un embedding
        // y se busca el contenido más similar en el almacén vectorial de acuerdo a los
        // documentos.
        // TopK(): devuelve el documento más parecido.
        List<Document> results = vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(message)
                        .topK(1) // cantidad de resutlados más cercanos a devolver.
                        .build());

        // Retorna la lista de documentos en este caso 1 con la respuesta semántica más
        // precisa.
        // El resultado contiene el texto y, si fue definido la metadata asociada.
        return results;
    }

}
