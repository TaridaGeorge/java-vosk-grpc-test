# java-vosk-grpc-test
A simple test connecting via grpc from java to vosk-server 
 
Server Logs: 
 
LOG (VoskAPI:ConfigureV2():vosk/model.cc:175) Decoding params beam=13 max-active=7000 lattice-beam=6 
LOG (VoskAPI:ConfigureV2():vosk/model.cc:178) Silence phones 1:2:3:4:5:6:7:8:9:10:11:12:13:14:15 
LOG (VoskAPI:RemoveOrphanNodes():nnet-nnet.cc:948) Removed 1 orphan nodes. 
LOG (VoskAPI:RemoveOrphanComponents():nnet-nnet.cc:847) Removing 2 orphan components. 
LOG (VoskAPI:Collapse():nnet-utils.cc:1472) Added 1 components, removed 2 
LOG (VoskAPI:CompileLooped():nnet-compile-looped.cc:345) Spent 0.00511408 seconds in looped compilation. 
LOG (VoskAPI:ReadDataFiles():vosk/model.cc:219) Loading i-vector extractor from /opt/vosk-model-en/model/ivector/final.ie 
LOG (VoskAPI:ComputeDerivedVars():ivector-extractor.cc:183) Computing derived variables for iVector extractor 
LOG (VoskAPI:ComputeDerivedVars():ivector-extractor.cc:204) Done. 
LOG (VoskAPI:ReadDataFiles():vosk/model.cc:233) Loading HCLG from /opt/vosk-model-en/model/graph/HCLG.fst 
LOG (VoskAPI:ReadDataFiles():vosk/model.cc:252) Loading words from /opt/vosk-model-en/model/graph/words.txt 
LOG (VoskAPI:ReadDataFiles():vosk/model.cc:260) Loading winfo /opt/vosk-model-en/model/graph/phones/word_boundary.int 
LOG (VoskAPI:ReadDataFiles():vosk/model.cc:268) Loading CARPA model from /opt/vosk-model-en/model/rescore/G.carpa 
ERROR:grpc._server:Exception iterating responses: 'result' 
Traceback (most recent call last): 
  File "/usr/local/lib/python3.7/dist-packages/grpc/_server.py", line 453, in _take_response_from_response_iterator 
    return next(response_iterator), True 
  File "./stt_server.py", line 85, in StreamingRecognize 
    yield self.get_response(recognizer.FinalResult()) 
  File "./stt_server.py", line 70, in get_response 
    words = [self.get_word_info(x) for x in res['result']] 
KeyError: 'result' 
 
Client Logs: 
 
UNKNOWN: Exception iterating responses: 'result' 
