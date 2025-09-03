package mx.txalcala.springia;

import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.FileCopyUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import mx.txalcala.springia.models.Author;
import mx.txalcala.springia.models.Book;
import mx.txalcala.springia.services.IAuthorService;
import mx.txalcala.springia.services.IBookService;

@SpringBootApplication
@RequiredArgsConstructor
public class SpringiaApplication implements ApplicationRunner {

	private final ResourceLoader resourceLoader; // Sirve para cargar recursos como archivos desde el classpath de
													// resources
	private final IAuthorService authorService; // servicio para manejar authors
	private final IBookService bookService; // servicio para manejar books

	public static void main(String[] args) {
		SpringApplication.run(SpringiaApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		// Este método se ejecuta automáticamente después de que la app arranca.
		// Sirve para inicializar datos o ejecutar lógica al inicio.

		// Cargar los recursos JSON desde el resources desde el atributo classpath
		Resource resource1 = resourceLoader.getResource("classpath:json/authors.json");
		Resource resource2 = resourceLoader.getResource("classpath:json/books.json");

		// Leer los archivos como arreglos de bytes
		byte[] jsonData1 = FileCopyUtils.copyToByteArray(resource1.getInputStream());
		byte[] jsonData2 = FileCopyUtils.copyToByteArray(resource2.getInputStream());

		// Convertir los bytes en String (codificación UTF-8) como texto
		String jsonString1 = new String(jsonData1, StandardCharsets.UTF_8);
		String jsonString2 = new String(jsonData2, StandardCharsets.UTF_8);

		// Crear un ObjectMapper para convertir JSON a objetos Java
		ObjectMapper objectMapper = new ObjectMapper();

		// Convertir el JSON en listas de objetos Author y Book
		List<Author> authors = objectMapper.readValue(jsonString1, new TypeReference<>() {
		});
		List<Book> books = objectMapper.readValue(jsonString2, new TypeReference<>() {
		});

		// Guardar en la bdd usando los servicios correspondientes
		authorService.saveAll(authors);
		bookService.saveAll(books);
	}

}
