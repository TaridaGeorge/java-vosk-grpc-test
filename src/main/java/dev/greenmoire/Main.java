package dev.greenmoire;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) throws URISyntaxException, IOException {
        STTServicePartial STTService = new STTServicePartial();

        Path filePath = Paths.get(Thread.currentThread().getContextClassLoader().getResource("test.wav").toURI());

        STTService.transcribeAudioFile(filePath.toFile());

        System.out.println("Waiting for key press to exit .. ");
        System.in.read();
    }


}
