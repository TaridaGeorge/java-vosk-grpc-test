package dev.greenmoire;

import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import vosk.stt.v1.SttServiceGrpc;
import vosk.stt.v1.SttServiceOuterClass;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.util.UUID;

public class STTServicePartial {

    private final SttServiceGrpc.SttServiceStub sttClient;

    public STTServicePartial() {

        ManagedChannel channel = ManagedChannelBuilder.forAddress("172.17.0.2", 5001)
                .usePlaintext()
                .build();

        sttClient = SttServiceGrpc.newStub(channel);
    }

    public void transcribeAudioFile(File file) throws IOException {
        int AUDIO_CHUNK_BUFFER_SIZE = 4000;

        StreamObserver<SttServiceOuterClass.StreamingRecognitionRequest> serverHandler = sendConfigRequest(UUID.randomUUID());

        FileInputStream fileInputStream = new FileInputStream(file);
        FileChannel fileChannel = fileInputStream.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(AUDIO_CHUNK_BUFFER_SIZE);

        while (fileChannel.read(buffer) > 0) {
            buffer.flip();

            SttServiceOuterClass.StreamingRecognitionRequest request;

            request = SttServiceOuterClass.StreamingRecognitionRequest.newBuilder()
                    .setAudioContent(ByteString.copyFrom(buffer))
                    .build();

            serverHandler.onNext(request);
            serverHandler.onCompleted();
        }

        fileChannel.close();
    }

    private StreamObserver<SttServiceOuterClass.StreamingRecognitionRequest> sendConfigRequest(UUID requestUUID) {
        StreamObserver<SttServiceOuterClass.StreamingRecognitionRequest> serverHandler;
        SttServiceOuterClass.RecognitionConfig config = SttServiceOuterClass.RecognitionConfig.newBuilder()
                .setSpecification(SttServiceOuterClass.RecognitionSpec.newBuilder()
                        .setSampleRateHertz(8000)
                        .setAudioEncoding(SttServiceOuterClass.RecognitionSpec.AudioEncoding.LINEAR16_PCM)
                        .setPartialResults(true)
                        .build())
                .build();

        serverHandler = sttClient.streamingRecognize(handleSpeechRecognitionResult(requestUUID));

        SttServiceOuterClass.StreamingRecognitionRequest request = SttServiceOuterClass.StreamingRecognitionRequest.newBuilder()
                .setConfig(config)
                .build();

        serverHandler.onNext(request);

        return serverHandler;
    }

    private StreamObserver<SttServiceOuterClass.StreamingRecognitionResponse> handleSpeechRecognitionResult(UUID uuid) {
        return new StreamObserver<SttServiceOuterClass.StreamingRecognitionResponse>() {
            @Override
            public void onNext(SttServiceOuterClass.StreamingRecognitionResponse streamingRecognitionResponse) {
                System.out.println(streamingRecognitionResponse.toString());
            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
                System.err.println(throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println("COMPLETED");
            }
        };
    }
}
