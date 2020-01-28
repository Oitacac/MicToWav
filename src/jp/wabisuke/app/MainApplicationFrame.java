package jp.wabisuke.app;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

public class MainApplicationFrame {

    private JFrame frame;
    private JTextField outputFile;
    private JButton recodingStartButton;
    private JButton recodingStopButton;

    private MicController controller;
    private Thread thread;
    private JTextField sampleRate;
    private JTextField sizeBits;

    private JRadioButton endian_little;
    private JRadioButton endian_big;
    private JRadioButton channel_streo;
    private JRadioButton channel_mono;

    private JLabel recLabel;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    MainApplicationFrame window = new MainApplicationFrame();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public MainApplicationFrame() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.setResizable(false);
        frame.setBounds(100, 100, 726, 474);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JLabel label = new JLabel("出力ファイル");
        label.setBounds(6, 23, 103, 16);
        frame.getContentPane().add(label);

        outputFile = new JTextField();
        outputFile.setBounds(143, 18, 577, 26);
        frame.getContentPane().add(outputFile);
        outputFile.setColumns(10);

        recodingStartButton = new JButton();
        recodingStartButton.setText("録音開始");
        recodingStartButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // 録音実行
                controller = new MicController(Float.parseFloat(sampleRate.getText()),
                        Integer.parseInt(sizeBits.getText()), channel_mono.isSelected(), MicController.SIGNED,
                        endian_big.isSelected());
                controller.setOutputFile(new File(outputFile.getText()));
                thread = new Thread(controller);
                thread.start();
                recLabel.setVisible(true);
                recodingStartButton.setEnabled(false);
            }
        });
        recodingStartButton.setBounds(603, 318, 117, 29);
        frame.getContentPane().add(recodingStartButton);

        recodingStopButton = new JButton("録音終了");
        recodingStopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 録音を終了
                controller.stopRecoding();
                recLabel.setVisible(false);
                recodingStartButton.setEnabled(true);
            }
        });
        recodingStopButton.setBounds(603, 359, 117, 29);
        frame.getContentPane().add(recodingStopButton);

        channel_streo = new JRadioButton("ステレオ");
        channel_streo.setBounds(143, 191, 141, 23);
        frame.getContentPane().add(channel_streo);

        channel_mono = new JRadioButton("モノラル");
        channel_mono.setBounds(357, 191, 141, 23);
        frame.getContentPane().add(channel_mono);

        ButtonGroup bg_channel = new ButtonGroup();
        bg_channel.add(channel_mono);
        bg_channel.add(channel_streo);

        channel_mono.setSelected(MicController.CHANNELS);
        endian_little = new JRadioButton("リトルエンディアン");
        endian_little.setBounds(144, 243, 179, 23);
        frame.getContentPane().add(endian_little);

        endian_big = new JRadioButton("ビックエンディアン");
        endian_big.setBounds(358, 243, 172, 23);
        frame.getContentPane().add(endian_big);

        ButtonGroup bg_endian = new ButtonGroup();
        bg_endian.add(endian_little);
        bg_endian.add(endian_big);

        endian_big.setSelected(MicController.BIGENDIAN);

        sampleRate = new JTextField();
        sampleRate.setBounds(144, 70, 130, 26);
        frame.getContentPane().add(sampleRate);
        sampleRate.setColumns(10);
        sampleRate.setText(String.valueOf(MicController.SAMPLE_RATE));

        JLabel lblNewLabel = new JLabel("サンプルレート");
        lblNewLabel.setBounds(6, 75, 103, 16);
        frame.getContentPane().add(lblNewLabel);

        JLabel lblH = new JLabel("Hz");
        lblH.setBounds(286, 75, 61, 16);
        frame.getContentPane().add(lblH);

        JLabel lblNewLabel_1 = new JLabel("サンプルビット");
        lblNewLabel_1.setBounds(6, 127, 91, 16);
        frame.getContentPane().add(lblNewLabel_1);

        sizeBits = new JTextField();
        sizeBits.setBounds(143, 122, 130, 26);
        frame.getContentPane().add(sizeBits);
        sizeBits.setColumns(10);
        sizeBits.setText(String.valueOf(MicController.SAMPLE_SIZE_BITS));

        JLabel lblNewLabel_2 = new JLabel("bit");
        lblNewLabel_2.setBounds(286, 127, 61, 16);
        frame.getContentPane().add(lblNewLabel_2);

        JLabel lblNewLabel_3 = new JLabel("録音方式");
        lblNewLabel_3.setBounds(6, 195, 61, 16);
        frame.getContentPane().add(lblNewLabel_3);

        JLabel lblBite = new JLabel("バイト順序");
        lblBite.setBounds(6, 247, 91, 16);
        frame.getContentPane().add(lblBite);

        recLabel = new JLabel("録音中...");
        recLabel.setFont(new Font("Lucida Grande", Font.BOLD, 16));
        recLabel.setBorder(new LineBorder(Color.red));
        recLabel.setForeground(Color.red);
        recLabel.setBounds(629, 68, 91, 29);
        frame.getContentPane().add(recLabel);
        recLabel.setVisible(false);
    }
}
