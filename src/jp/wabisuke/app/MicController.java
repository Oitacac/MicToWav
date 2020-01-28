package jp.wabisuke.app;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

/**
 * マイクでの入出力を管理する
 * @author Wabisuke
 *
 */
public class MicController implements Runnable {

    /** デフォルトサンプルレート */
    public static final float SAMPLE_RATE = 44100;
    /** デフォルトサンプルビット */
    public static final int SAMPLE_SIZE_BITS = 8;
    /** デフォルト録音方式 <p>true...モノラル方式</p><p>false...ステレオ方式</p> */
    public static final boolean CHANNELS = true;
    public static final boolean SIGNED = true;
    /** デフォルトエンディアン <p> true...ビッグエンディアン</p><p>false...リトルエンディアン</p> */
    public static final boolean BIGENDIAN = true;

    // サンプルレート 初期値は44100Hz
    private float sampleRate;
    // サンプルサイズ 初期値は8ビット
    private int sampleSizeBits;
    // チャンネル数 初期値はモノラル
    private int channels;
    // 型 初期値は正負型
    private boolean signed;
    // バイト順序 初期値はリトルエンディアン
    private boolean endian;

    // 出力ファイル
    TargetDataLine targetLine;
    private File file;
    AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;

    private AudioFormat format;

    /**
     * このクラスのコンストラクタ
     */
    public MicController() {
        this(MicController.SAMPLE_RATE, MicController.SAMPLE_SIZE_BITS, MicController.CHANNELS,
                MicController.SIGNED, MicController.BIGENDIAN);
    }

    /**
     * このクラスのコンストラクタ
     * @param sampleRate サンプルレート
     * @param sampleSizeBits ビット数
     * @param channels 録音方式
     * @param signed 正負型
     * @param endian バイト順序
     */
    public MicController(Float sampleRate, int sampleSizeBits, boolean channels, boolean signed, boolean endian) {
        this.sampleRate = sampleRate;
        this.sampleSizeBits = sampleSizeBits;
        if (channels)
            this.channels = 1;
        else
            this.channels = 2;
        this.signed = signed;
        this.endian = endian;

        try {
            format = new AudioFormat(this.sampleRate, this.sampleSizeBits, this.channels, this.signed, this.endian);
            targetLine = AudioSystem.getTargetDataLine(format);
            targetLine.open();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
        System.out.println("AudioFormat:" + format.toString());
    }

    /**
     * 録音を開始する
     */
    public void startRecoding() {
        System.out.println("startRecoding...");
        try {
            targetLine.start();
            AudioInputStream ais = new AudioInputStream(targetLine);
            AudioSystem.write(ais, fileType, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 録音を停止する
     */
    public void stopRecoding() {
        System.out.println("stopRecoding");
        targetLine.stop();
        targetLine.close();
    }

    /**
     * 出力先ファイルをセット
     * @param f
     */
    public void setOutputFile(File f) {
        if (f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        file = f;
    }

    @Override
    public void run() {
        startRecoding();
    }
}
