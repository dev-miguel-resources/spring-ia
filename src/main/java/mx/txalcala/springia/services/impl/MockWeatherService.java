package mx.txalcala.springia.services.impl;

import java.util.function.Function;

public class MockWeatherService implements Function<MockWeatherService.Request, MockWeatherService.Response> {

    // Definir centigrados y farenheit
    public enum Unit {
        C, F
    }

    // Location y unidades
    public record Request(String location, Unit unit) {
    }

    // devolver la temperatura y unidad de medida
    public record Response(double temp, Unit unit) {

    }

    // Vamos a procesar un resultado como si fuera desde una bdd, haciendonos esa
    // idea.
    // En el apply: se deja la transacción de lógica de negocio.
    @Override
    public Response apply(Request request) {
        return new Response(30.0, Unit.C);
    }

}
