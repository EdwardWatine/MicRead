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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    private final int REQUEST_MICROPHONE = 1;
    public final HashMap mymap = new HashMap(64, (float) 1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mymap.put(27.50, "A0");
        mymap.put(29.14, "A#0/Bb0");
        mymap.put(30.87, "B0");
        mymap.put(32.70, "C1");
        mymap.put(34.65, "C#1/Db1");
        mymap.put(36.71, "D1");
        mymap.put(38.89, "D#1/Eb1");
        mymap.put(41.20, "E1");
        mymap.put(43.65, "F1");
        mymap.put(46.25, "F#1/Gb1");
        mymap.put(49.00, "G1");
        mymap.put(51.91, "G#1/Ab1");
        mymap.put(55.00, "A1");
        mymap.put(58.27, "A#1/Bb1");
        mymap.put(61.74, "B1");
        mymap.put(65.41, "C2");
        mymap.put(69.30, "C#2/Db2");
        mymap.put(73.42, "D2");
        mymap.put(77.78, "D#2/Eb2");
        mymap.put(82.41, "E2");
        mymap.put(87.31, "F2");
        mymap.put(92.50, "F#2/Gb2");
        mymap.put(98.00, "G2");
        mymap.put(103.83, "G#2/Ab2");
        mymap.put(110.00, "A2");
        mymap.put(116.54, "A#2/Bb2");
        mymap.put(123.47, "B2");
        mymap.put(130.81, "C3");
        mymap.put(138.59, "C#3/Db3");
        mymap.put(146.83, "D3");
        mymap.put(155.56, "D#3/Eb3");
        mymap.put(164.81, "E3");
        mymap.put(174.61, "F3");
        mymap.put(185.00, "F#3/Gb3");
        mymap.put(196.00, "G3");
        mymap.put(207.65, "G#3/Ab3");
        mymap.put(220.00, "A3");
        mymap.put(233.08, "A#3/Bb3");
        mymap.put(246.94, "B3");
        mymap.put(261.63, "C4");
        mymap.put(277.18, "C#4/Db4");
        mymap.put(293.6, "D4");
        mymap.put(311.13, "D#4/Eb4");
        mymap.put(329.63, "E4");
        mymap.put(349.23, "F4");
        mymap.put(369.99, "F#4/Gb4");
        mymap.put(392.00, "G4");
        mymap.put(415.30, "G#4/Ab4");
        mymap.put(440.00, "A4");
        mymap.put(466.16, "A#4/Bb4");
        mymap.put(493.88, "B4");
        mymap.put(523.25, "C5");
        mymap.put(554.37, "C#5/Db5");
        mymap.put(587.33, "D5");
        mymap.put(622.25, "D#5/Eb5");
        mymap.put(659.25, "E5");
        mymap.put(698.46, "F5");
        mymap.put(739.99, "F#5/Gb5");
        mymap.put(783.99, "G5");
        mymap.put(830.61, "G#5/Ab5");
        mymap.put(880.00, "A5");
        mymap.put(932.33, "A#5/Bb5");
        mymap.put(987.77, "B5");
        mymap.put(1046.50, "C6");
        mymap.put(1108.73, "C#6/Db6");
        mymap.put(1174.66, "D6");
        mymap.put(1244.51, "D#6/Eb6");
        mymap.put(1318.51, "E6");
        mymap.put(1396.91, "F6");
        mymap.put(1479.98, "F#6/Gb6");
        mymap.put(1567.98, "G6");
        mymap.put(1661.22, "G#6/Ab6");
        mymap.put(1760.00, "A6");
        mymap.put(1864.66, "A#6/Bb6");
        mymap.put(1975.53, "B6");
        mymap.put(2093.00, "C7");

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
        double realout[] = new double[mymap.size()];
        double imagout[] = new double[realout.length];
        double magnitude[] = new double[realout.length]; //A1
        double relative[] = new double[realout.length];
        int count = 0;
        ArrayList<Double> iter = new ArrayList<>(mymap.keySet());
        for (int i=0; i < iter.size(); i++){
            relative[count] = iter.get(i)/sampleRateInHz;
            count++;
        }
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
            recorder.read(
                    sData,
                    0,
                    sData.length
            );
            double doubleArray[] = new double[sData.length];
            for (int i = 0; i < doubleArray.length; i++) {
                doubleArray[i] = (double) sData[i];
            }
            computeDft(doubleArray, relative, realout, imagout, magnitude);

            /*System.out.print("OUTPUT: ");
            for (int i=0; i<magnitude.length; i++){
                System.out.print(magnitude[i]+",");
            }
            System.out.println("");*/

            double max = 0;
            int highest = 0;
            for (int i = 0; i < magnitude.length; i++) {
                if (magnitude[i] > max) {
                    max = magnitude[i];
                    highest = i;
                }
            }
            String note = (String) mymap.get(iter.get(highest));
            System.out.println(note);
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

    public static void computeDft(double[] inreal, double[] atf, double[] outreal, double[] outimag, double[] magnitude) {
        int n = inreal.length;
        assert atf.length==magnitude.length;
        for (int k = 0; k < magnitude.length; k++) {  // For each output element
            double sumreal = 0;
            double sumimag = 0;
            for (int t = 0; t < n; t++) {  // For each input element
                double angle = 2 * Math.PI * t * atf[k];
                sumreal +=  inreal[t] * Math.cos(angle) + inreal[t] * Math.sin(angle);
                sumimag += -inreal[t] * Math.sin(angle) + inreal[t] * Math.cos(angle);
            }
            outreal[k] = sumreal;
            outimag[k] = sumimag;
            magnitude[k] = Math.sqrt(sumreal*sumreal+sumimag*sumimag);

        }
    }
}
