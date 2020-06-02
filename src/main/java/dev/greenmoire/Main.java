package dev.greenmoire;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) throws URISyntaxException, IOException {
	    STTService sttService = new STTService();

        Path filePath = Paths.get(Thread.currentThread().getContextClassLoader().getResource("test.wav").toURI());

        sttService.transcribeAudioFile(filePath);

        System.out.println("Waiting for key press to exit .. ");
        System.in.read();
    }


}
