package mx.txalcala.springia.config;

import org.springframework.ai.chroma.vectorstore.ChromaApi;
import org.springframework.ai.chroma.vectorstore.ChromaVectorStore;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChromaConfig {

    // Este bean representa el almacén vectorial que se usará para guardar y
    // consultar embeddings
    @Bean
    public VectorStore chromaVectorStore(EmbeddingModel embeddingModel, ChromaApi chromaApi) {
        // Crear una instancia de ChromaVectorStore recibiendo las referencias claves
        // necesarias.
        return ChromaVectorStore.builder(chromaApi, embeddingModel)
                // Nombre de la colección (similar a una tabla en una BD relacional)
                // Aquí es donde se persisten los vectores
                .collectionName("TestCollection")
                // Indica que se debe inicilizar el esquema si aún no existe.
                // Esto crea automáticamente la colección si en Chroma no está creada.
                .initializeSchema(true)
                .build();
    }

}
