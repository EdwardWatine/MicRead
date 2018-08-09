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

public class MainActivity extends AppCompatActivity {

    private final int REQUEST_MICROPHONE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        int sampleRateInHz = 44100;//8000 44100, 22050 and 11025
        int channelConfig = AudioFormat.CHANNEL_CONFIGURATION_MONO;
        int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
        int bufferSize = 2048;
        short sData[] = new short[bufferSize/Short.BYTES];
        double realout[] = new double[sData.length];
        double imagout[] = new double[sData.length];
        double magnitude[] = new double[sData.length];
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
            for (int i=0; i < sData.length; i++){
                doubleArray[i] = Math.sin(Math.toRadians(i*30));//sData[i];
            }
            computeDft(doubleArray, realout, imagout, magnitude);

            System.out.print("OUTPUT: ");
            for (int i=0; i<sData.length; i++){
                System.out.print(magnitude[i]+",");
            }
            System.out.println("");
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
        for (int k = 0; k < n; k++) {  // For each output element
            double sumreal = 0;
            double sumimag = 0;
            for (int t = 0; t < n; t++) {  // For each input element
                double angle = 2 * Math.PI * t * k / n;
                sumreal +=  inreal[t] * Math.cos(angle) + inreal[t] * Math.sin(angle);
                sumimag += -inreal[t] * Math.sin(angle) + inreal[t] * Math.cos(angle);
            }
            outreal[k] = sumreal;
            outimag[k] = sumimag;
            magnitude[k] = Math.sqrt(sumreal*sumreal+sumimag*sumimag);

        }
    }
}
