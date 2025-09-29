package mx.txalcala.springia.controllers;

import org.springframework.ai.chroma.vectorstore.ChromaVectorStore;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/etls")
@RequiredArgsConstructor
public class ETLController {

    private final ChromaVectorStore chromaVectorStore;

    // Modelo de Chat Conversational de Open IA que usaremos para generar la
    // respuesta final.
    private final OpenAiChatModel openAiChatModel;

}
