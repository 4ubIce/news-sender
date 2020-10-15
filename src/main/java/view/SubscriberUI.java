package view;

import controller.SubscriberController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class SubscriberUI extends JFrame {

    private static final int FRAME_WIDTH = 800;
    private static final int FRAME_HEIGHT = 600;
    private static final int FRAME_MIN_WIDTH = 640;
    private static final int FRAME_MIN_HEIGHT = 480;

    private final JFrame mainFrame = new JFrame("���������");
    private final JTextArea newsArea;

    public SubscriberUI() {

        final GraphicsDevice gD = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        final int screenCenterX = gD.getDisplayMode().getWidth()/2; //���������� ����� ������ �� ��� X
        final int screenCenterY = gD.getDisplayMode().getHeight()/2; //���������� ����� ������ �� ��� Y

        final int frameWidth  = Math.max(FRAME_MIN_WIDTH, screenCenterX); //���������� ������ ���� ��������� �� ��� X
        final int frameHeight = Math.max(FRAME_MIN_HEIGHT, screenCenterY); //���������� ������ ���� ��������� �� ��� Y

        final int frameOffsetLeft = screenCenterX - frameWidth/2; //���������� ��������� �������� ������ ���� ��������� �� ��� X
        final int frameOffsetTop = screenCenterY - frameHeight/2; //���������� ��������� �������� ������ ���� ��������� �� ��� Y

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        mainFrame.setSize(new Dimension(frameWidth, frameHeight)); //������������� ������ ���� ���������
        mainFrame.setLocation(frameOffsetLeft, frameOffsetTop); //������������� ��������� ���� ��������� �� ������
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); //��������� ���� ��������� ��� ������� �� ������ ��������
        mainFrame.setMinimumSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT)); //������������� ����������� ������ ���� ���������
        mainFrame.setPreferredSize(new Dimension(FRAME_WIDTH * 2, FRAME_HEIGHT * 2)); //������������� ���������������� ������ ���� ���������
        mainFrame.setResizable(true); //�������� ����������� ��������� ������� ���� ���������
        JPanel bgPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints= new GridBagConstraints();
        mainFrame.add(bgPanel);
        constraints.insets = new Insets(5, 5, 5, 5); //������� ������� �������� ���� ���������


        JLabel newsLabel = new JLabel("�������");
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridheight = 1;
        constraints.weightx = 100;
        constraints.weighty = 5;

        constraints.fill = GridBagConstraints.HORIZONTAL;
        bgPanel.add(newsLabel, constraints);

        newsArea = new JTextArea();
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridheight = 1;
        constraints.weightx = 100;
        constraints.weighty = 95;
        constraints.fill = GridBagConstraints.BOTH;
        newsArea.setRows(30);
        bgPanel.add(newsArea, constraints);

        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                SubscriberController.doBeforeExit();
            }
        });
    }

    public JTextArea getNewsArea() {
        return newsArea;
    }

    public void start(){
        mainFrame.setVisible(true);
    }

}
