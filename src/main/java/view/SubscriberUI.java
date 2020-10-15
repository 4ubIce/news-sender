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

    private final JFrame mainFrame = new JFrame("Подписчик");
    private final JTextArea newsArea;

    public SubscriberUI() {

        final GraphicsDevice gD = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        final int screenCenterX = gD.getDisplayMode().getWidth()/2; //определяем центр экрана по оси X
        final int screenCenterY = gD.getDisplayMode().getHeight()/2; //определяем центр экрана по оси Y

        final int frameWidth  = Math.max(FRAME_MIN_WIDTH, screenCenterX); //определяем размер окна программы по оси X
        final int frameHeight = Math.max(FRAME_MIN_HEIGHT, screenCenterY); //определяем размер окна программы по оси Y

        final int frameOffsetLeft = screenCenterX - frameWidth/2; //определяем положение верхнего левого угла программы по оси X
        final int frameOffsetTop = screenCenterY - frameHeight/2; //определяем положение верхнего левого угла программы по оси Y

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        mainFrame.setSize(new Dimension(frameWidth, frameHeight)); //устанавливаем размер окна программы
        mainFrame.setLocation(frameOffsetLeft, frameOffsetTop); //устанавливаем положение окна программы на экране
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); //закрываем окно программы при нажатии на кнопку закрытия
        mainFrame.setMinimumSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT)); //устанавливаем минимальный размер окна программы
        mainFrame.setPreferredSize(new Dimension(FRAME_WIDTH * 2, FRAME_HEIGHT * 2)); //устанавливаем предпочтительный размер окна программы
        mainFrame.setResizable(true); //включаем возможность изменения размера окна программы
        JPanel bgPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints= new GridBagConstraints();
        mainFrame.add(bgPanel);
        constraints.insets = new Insets(5, 5, 5, 5); //отступы каждого элемента окна программы


        JLabel newsLabel = new JLabel("Новости");
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
