package de.insiderpie;

import io.micronaut.runtime.Micronaut;

public class Application {

    public static void main(String[] args) {
        System.out.println("Stanford NER server, made with <3 by InsiderPie");
        Micronaut.run(Application.class, args);
    }
}

