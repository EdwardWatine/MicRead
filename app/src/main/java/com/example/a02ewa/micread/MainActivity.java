package com.example.a02ewa.micread;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.nio.ByteBuffer;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private final int REQUEST_MICROPHONE = 1;
    private final HashMap mymap = new HashMap(64, (float) 1);
    private final String[] notes = {"A", "A#/Bb", "B", "C", "C#/Db", "D", "D#/Eb", "E", "F", "F#/Gb", "G", "G#/Ab"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        for (int i=0; i < 65; i++){
            mymap.put(); //stuff
        }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    REQUEST_MICROPHONE);
        } else {
            recordSound();
        }
    }

    public void recordSound(){
        int sampleRateInHz = 11025;//8000 44100, 22050 and 11025 //picked 11025 based on high piano notes C7
        int channelConfig = AudioFormat.CHANNEL_CONFIGURATION_MONO;
        int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
        int bufferSize = 2048;
        short sData[] = new short[bufferSize/Short.BYTES];
        double realout[] = new double[sampleRateInHz/3];
        double imagout[] = new double[realout.length];
        double magnitude[] = new double[realout.length]; //3.27 = difference of A1 and Bb1
        System.out.println("BEFORE");
        AudioRecord recorder = new AudioRecord(
                MediaRecorder.AudioSource.DEFAULT,
                sampleRateInHz,
                channelConfig,
                audioFormat,
                bufferSize
        );
        System.out.println("BEFORE2");
        recorder.startRecording();
        System.out.println("AFTER");
        while (true) {
            /*recorder.read(
                    sData,
                    0,
                    sData.length
            );*/
            double doubleArray[] = new double[sData.length];
            for (int i=0; i < doubleArray.length; i++){
                doubleArray[i] = Math.sin(Math.toRadians(i*20));//sData[i];
            }
            computeDft(doubleArray, realout, imagout, magnitude);

            /*System.out.print("OUTPUT: ");
            for (int i=0; i<magnitude.length; i++){
                //System.out.print(magnitude[i]+",");
                if (i%100==0){
                    //System.out.println("");
                }
            }
            //System.out.println("");*/
            System.out.println("FINISHED");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_MICROPHONE:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    // Permission Denied
                    Toast.makeText(MainActivity.this, "Audio permissions denied", Toast.LENGTH_SHORT).show();
                } else {
                    recordSound();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /*
     * Discrete Fourier transform (Java)
     * by Project Nayuki, 2017. Public domain.
     * https://www.nayuki.io/page/how-to-implement-the-discrete-fourier-transform
     */

    public static void computeDft(double[] inreal, double[] outreal, double[] outimag, double[] magnitude) {
        int n = inreal.length;
        for (int k = 0; k < magnitude.length; k++) {  // For each output element
            double sumreal = 0;
            double sumimag = 0;
            for (int t = 0; t < n; t++) {  // For each input element
                double angle = 2 * Math.PI * t * k / magnitude.length;
                sumreal +=  inreal[t] * Math.cos(angle) + inreal[t] * Math.sin(angle);
                sumimag += -inreal[t] * Math.sin(angle) + inreal[t] * Math.cos(angle);
            }
            outreal[k] = sumreal;
            outimag[k] = sumimag;
            magnitude[k] = Math.sqrt(sumreal*sumreal+sumimag*sumimag);

        }
    }
}
