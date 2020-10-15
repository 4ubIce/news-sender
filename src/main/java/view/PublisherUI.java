package view;

import controller.PublisherController;

import javax.swing.*;
import java.awt.*;

public class PublisherUI extends JFrame {

    private final JFrame mainFrame = new JFrame("Издатель");
    private final JTextArea archiveNewsArea;
    private static final int FRAME_WIDTH = 800;
    private static final int FRAME_HEIGHT = 600;
    private static final int FRAME_MIN_WIDTH = 640;
    private static final int FRAME_MIN_HEIGHT = 480;

    public PublisherUI() {

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


        JLabel archiveNewsLabel = new JLabel("Архив новостей");
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridheight = 1;
        constraints.weightx = 100;
        constraints.weighty = 5;

        constraints.fill = GridBagConstraints.HORIZONTAL;
        bgPanel.add(archiveNewsLabel, constraints);

        archiveNewsArea = new JTextArea();
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridheight = 1;
        constraints.weightx = 100;
        constraints.weighty = 40;
        constraints.fill = GridBagConstraints.BOTH;
        archiveNewsArea.setRows(30);
        archiveNewsArea.setEditable(false);
        bgPanel.add(archiveNewsArea, constraints);

        JLabel newNewsLabel = new JLabel("Новая новость");
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridheight = 1;
        constraints.weightx = 100;
        constraints.weighty = 5;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        bgPanel.add(newNewsLabel, constraints);

        JTextArea newNewsArea = new JTextArea();
        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.gridheight = 1;
        constraints.weightx = 100;
        constraints.weighty = 40;
        constraints.fill = GridBagConstraints.BOTH;
        newNewsArea.setRows(30);
        bgPanel.add(newNewsArea, constraints);

        JButton sendButton = new JButton("Опубликовать");
        constraints.gridx = 0;
        constraints.gridy = 5;
        constraints.gridwidth = 2;
        constraints.gridheight = 1;
        constraints.weightx = 100;
        constraints.weighty = 5;
        constraints.anchor = GridBagConstraints.LINE_START;
        constraints.fill = GridBagConstraints.BOTH;
        bgPanel.add(sendButton, constraints);

        sendButton.addActionListener(e -> {
            PublisherController.send(newNewsArea.getText());
        });
    }

    public JTextArea getArchiveNewsArea() {
        return archiveNewsArea;
    }

    public void start(){
        mainFrame.setVisible(true);
    }
}
