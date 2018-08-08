package com.example.a02ewa.micread;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int sampleRateInHz = 44100;//8000 44100, 22050 and 11025
        int channelConfig = AudioFormat.CHANNEL_CONFIGURATION_MONO;
        int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
        int BufferElements2Rec = 1024; // want to play 2048 (2K) since 2 bytes we use only 1024
        int BytesPerElement = 2; // 2 bytes in 16bit format
        byte sData[] = new byte[BufferElements2Rec];

        AudioRecord recorder = new AudioRecord( MediaRecorder.AudioSource.DEFAULT,
                sampleRateInHz, channelConfig, audioFormat, BufferElements2Rec*BytesPerElement);

        recorder.startRecording();
        while (true){
            recorder.read(sData, 0, BufferElements2Rec);
            System.out.println("Bytes recorded: " + sData.toString());
        }
    }
}
