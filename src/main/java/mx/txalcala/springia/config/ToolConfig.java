package mx.txalcala.springia.config;

import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.function.FunctionToolCallback;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import mx.txalcala.springia.repos.IBookRepo;
import mx.txalcala.springia.services.impl.BookToolServiceImpl;
import mx.txalcala.springia.services.impl.MockWeatherService;

@Configuration
public class ToolConfig {

    @Bean
    public ToolCallback weatherFunctionInfo() {
        return FunctionToolCallback.builder("weatherFunction", new MockWeatherService())
                .description("Get the weather in location")
                .inputType(MockWeatherService.Request.class)
                .build();
    }

    // pendiente lo del book
    @Bean
    public ToolCallback bookInfoFunction(IBookRepo repo) {
        return FunctionToolCallback.builder("bookInfoFunction", new BookToolServiceImpl(repo))
                .description("Get book info from book name")
                .inputType(BookToolServiceImpl.Request.class)
                .build();
    }

}
