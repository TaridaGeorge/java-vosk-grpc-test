package dev.greenmoire;

import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import vosk.stt.v1.SttServiceGrpc;
import vosk.stt.v1.SttServiceOuterClass;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class STTService {

    private final SttServiceGrpc.SttServiceStub sttClient;

    public STTService() {

        ManagedChannel channel = ManagedChannelBuilder.forAddress("172.17.0.2", 5001)
                .usePlaintext()
                .build();

        sttClient = SttServiceGrpc.newStub(channel);
    }

    public void transcribeAudioFile(Path filePath) throws IOException {
        System.out.println("New transcribe request " + filePath.toString());

        byte[] data = Files.readAllBytes(filePath);
        StreamObserver<SttServiceOuterClass.StreamingRecognitionRequest> server = sttClient.streamingRecognize(handleSpeechRecognitionResult());

        SttServiceOuterClass.StreamingRecognitionRequest req = SttServiceOuterClass.StreamingRecognitionRequest.newBuilder()
                .setConfig(getStreamingConfig())
                .setAudioContent(ByteString.copyFrom(data))
                .build();

        server.onNext(req);
        server.onCompleted();

        System.out.println("Audio scheduled for transcribing.");
    }

    private StreamObserver<SttServiceOuterClass.StreamingRecognitionResponse> handleSpeechRecognitionResult() {
        return new StreamObserver<SttServiceOuterClass.StreamingRecognitionResponse>() {
            @Override
            public void onNext(SttServiceOuterClass.StreamingRecognitionResponse streamingRecognitionResponse) {
                for (SttServiceOuterClass.SpeechRecognitionChunk chunk : streamingRecognitionResponse.getChunksList()) {
                    boolean isFinal = chunk.getFinal();
                    System.out.println(chunk.toString());
                }
            }

            @Override
            public void onError(Throwable throwable) {
                System.err.println(throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println("COMPLETED");
            }
        };
    }

    private SttServiceOuterClass.RecognitionConfig getStreamingConfig() {
        return SttServiceOuterClass.RecognitionConfig.newBuilder()
                .setSpecification(SttServiceOuterClass.RecognitionSpec.newBuilder()
                        .setSampleRateHertz(8000)
                        .setAudioEncoding(SttServiceOuterClass.RecognitionSpec.AudioEncoding.LINEAR16_PCM)
                        .setPartialResults(false)
                        .build())
                .build();
    }

}
