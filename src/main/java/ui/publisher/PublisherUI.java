package ui.publisher;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PublisherUI extends JFrame {

    private JFrame mainFrame = new JFrame("Издатель");
    private static final int FRAME_WIDTH = 800;
    private static final int FRAME_HEIGHT = 600;
    private static final int FRAME_MIN_WIDTH = 640;
    private static final int FRAME_MIN_HEIGHT = 480;

    public PublisherUI() {

        final GraphicsDevice gD = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        final int screenCenterX = gD.getDisplayMode().getWidth()/2;
        final int screenCenterY = gD.getDisplayMode().getHeight()/2;

        final int frameWidth  = Math.max(FRAME_MIN_WIDTH, screenCenterX);
        final int frameHeight = Math.max(FRAME_MIN_HEIGHT, screenCenterY);

        final int frameOffsetLeft = screenCenterX - frameWidth/2;
        final int frameOffsetTop = screenCenterY - frameHeight/2;

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        mainFrame.setSize(new Dimension(frameWidth, frameHeight));
        mainFrame.setLocation(frameOffsetLeft, frameOffsetTop);
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setMinimumSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
        mainFrame.setPreferredSize(new Dimension(FRAME_WIDTH * 2, FRAME_HEIGHT * 2));
        mainFrame.setResizable(true);
        JPanel bgPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints= new GridBagConstraints();
        mainFrame.add(bgPanel);

        JLabel archiveNewsLabel = new JLabel("Архив новостей");
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridheight = 1;
        constraints.weightx = 100;
        constraints.weighty = 5;

        constraints.fill = GridBagConstraints.HORIZONTAL;
        bgPanel.add(archiveNewsLabel, constraints);

        JTextArea archiveNewsArea = new JTextArea();
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridheight = 1;
        constraints.weightx = 100;
        constraints.weighty = 40;
        constraints.fill = GridBagConstraints.BOTH;
        archiveNewsArea.setRows(30);
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
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.gridx = 0;
        constraints.gridy = 5;
        constraints.gridwidth = 2;
        constraints.gridheight = 1;
        constraints.weightx = 100;
        constraints.weighty = 5;
        constraints.anchor = GridBagConstraints.LINE_START;
        constraints.fill = GridBagConstraints.BOTH;
        bgPanel.add(sendButton, constraints);

        sendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//                for (String s : getEmails(fromLine.getText())) {
//                    bodyArea.append(s + "\n");
//                }
//                System.out.println(isValidSubject(subjectLine.getText()));
            }
        } );
    }

    public void start(){
        mainFrame.setVisible(true);
    }
}
