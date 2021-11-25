package com.solo.projectreactor;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileService {

    public Mono<String> readFile() {
        Path path = Path.of("").toAbsolutePath().resolve("src/test/resources/file.txt");
          try(Stream<String> stream =Files.lines(path)){
              String collect = stream.collect(Collectors.joining(","));
              System.out.println(collect);
          }catch (Exception e) {
              e.printStackTrace();
          }
        return Mono.empty();
    }

    @Test
    void name() {
        readFile();
    }
}
