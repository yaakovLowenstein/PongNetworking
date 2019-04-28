package pong;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Properties;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.WindowConstants;

class PongPanel extends JPanel {

    private Point paddle = new Point(585, 200);
    private Point compPaddle = new Point(5, 10);
    private Point paddleDelta = new Point(5, 5);
    private Point ball = new Point(30, 10);
    private Point ballDelta = new Point(5, 5);
    private Timer timer;
    int score = 0;
    int highScore;
    Properties pongProps = new Properties();
    ArrayList<Integer> highScoresList = new ArrayList<>(10);
    ArrayList<String> initials = new ArrayList<>(10);
    JButton startButton = new startButton();

    public PongPanel() {
        setBounds(40, 25, 600, 400);
        setSize(600, 400);
        setBackground(Color.black);

        loadTopTenScores();
        loadHighestScore();
        addMouseWheel();
        addKeyListener();
        add(startButton);
        playGame();
    }

    private void endGame() {
        timer.stop();
        startButton.setVisible(true);
        if (!highScoresList.isEmpty()) {
            if (score > highScoresList.get(0)) {
                highScore = score;
                pongProps.setProperty("High Score", highScore + "");
                try {
                    FileOutputStream out = new FileOutputStream("PongProps.txt");
                    pongProps.store(out, "Pong Properties");
                    out.close();
                } catch (Exception e) {
                }
            }
        }
        highScoreKeeper(score, highScoresList, initials);

    }

    private void highScoreKeeper(int score, ArrayList<Integer> highScoresList, ArrayList<String> initials) {

        String initialsInput = "";
        if (highScoresList.size() < 10) {
            initialsInput = javax.swing.JOptionPane.showInputDialog("enter initials");
            highScoresList.add(score);
            Collections.sort(highScoresList, Collections.reverseOrder());
        } else if (score > highScoresList.get(9)) {
            initialsInput = javax.swing.JOptionPane.showInputDialog("enter initials");
            highScoresList.set(9, score);

            Collections.sort(highScoresList, Collections.reverseOrder());

        }
        //adding initials to list
        int indexOfScore = highScoresList.indexOf(score);
        initials.add(indexOfScore, initialsInput);

        //serialization
        String fileName = "file";
        try {
            FileOutputStream file = new FileOutputStream(fileName);
            ObjectOutputStream out = new ObjectOutputStream(file);

            out.writeObject(highScoresList);
            out.writeObject(initials);
            out.close();
            file.close();

        } catch (IOException ex) {
            System.out.println("Error0");
        }

        String output = "";
        for (int i = 0; i < highScoresList.size(); i++) {
            String temp = " " + highScoresList.get(i);
            output += (i + 1) + ". " + initials.get(i) + temp + "\n";

        }
        JOptionPane.showMessageDialog(null, output);

    }

    private void updateBall() {

        if (ball.y > 365) {
            ballDelta.y = -ballDelta.y;
        }

        if (ball.y < 5) {
            ballDelta.y = -ballDelta.y;

        }

        if (ball.x > paddle.x - 30 && ball.y + 15 > paddle.y && ball.y + 15 < paddle.y + 60) {
            ballDelta.x = -ballDelta.x;
        }
        if (ball.x < compPaddle.x + 20) {
            ballDelta.x = -ballDelta.x;

        }
        ball.x += ballDelta.x;
        ball.y += ballDelta.y;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.RED);
        g.fillRect(paddle.x, paddle.y, 10, 60);
        g.fillRect(compPaddle.x, compPaddle.y, 10, 60);
        g.setColor(Color.GREEN);
        g.fillOval(ball.x, ball.y, 30, 30);
        g.setColor(Color.white);
        g.drawString("Score: " + score, 10, 380);
        g.drawString("High Score: " + highScore, 250, 380);
    }

    private void compPlay() {
        compPaddle.y = ball.y;
    }

    private void userPaddleFixedMovement() {
        if (paddle.y == 0) {
            paddleDelta.y = -paddleDelta.y;
        }
        if (paddle.y > 335) {
            paddleDelta.y = -paddleDelta.y;
        }
        paddle.y += paddleDelta.y;

    }

    private void loadHighestScore() {
        try {
            FileInputStream input = new FileInputStream("PongProps.txt");
            pongProps.load(input);
            highScore = Integer.parseInt(pongProps.getProperty("High Score"));
            input.close();
        } catch (Exception ex) {
        }
    }

    private void loadTopTenScores() {

        try {
            FileInputStream file = new FileInputStream("file");
            ObjectInputStream in = new ObjectInputStream(file);
            highScoresList = (ArrayList) in.readObject();
            initials = (ArrayList) in.readObject();
            in.close();
            file.close();

        } catch (IOException ex) {

        } catch (ClassNotFoundException ex) {
        }
        String output = "";
        if (!highScoresList.isEmpty()) {
            for (int i = 0; i < highScoresList.size(); i++) {
                String temp = " " + highScoresList.get(i);
                highScoresList.get(i);
                initials.add(initials.get(i));
                output += (i + 1) + ". " + initials.get(i) + temp + "\n";

            }

            JOptionPane.showMessageDialog(null, output);
        }
    }

    private void addMouseWheel() {
        addMouseWheelListener(new MouseWheelListener() {

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                int notch = e.getWheelRotation() * 10;
                if (paddle.y <= 325 && paddle.y > 0) {
                    paddle.y += notch;
                    repaint();
                } else {
                    paddle.y -= notch;
                    repaint();
                }
            }
        });
    }

    private void addKeyListener() {
        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    if (paddle.y <= 325 && paddle.y > 0) {
                        paddle.y -= 10;
                        repaint();
                    } else {
                        paddle.y += 10;
                        repaint();
                    }
                }
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    if (paddle.y <= 325 && paddle.y > 0) {
                        paddle.y += 10;
                        repaint();
                    } else {
                        paddle.y -= 10;
                        repaint();
                    }
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
    }

    private void playGame() {
        timer = new Timer(25, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                updateBall();
                compPlay();
                userPaddleFixedMovement();
                repaint();
                if (ball.x + 30 == paddle.x) {
                    score++;
                }
                if (ball.x + 20 > paddle.x) {
                    endGame();
                }
            }
        });
    }

    private class startButton extends JButton {

        public startButton() {
            setText("start");
            addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    score = 0;
                    ball = new Point(30, 10);
                    timer.start();
                    setVisible(false);

                }

            });
        }
    }
}

public class Pong extends JFrame {

    public Pong() {
        setTitle("pong");
        setSize(700, 500);
        setVisible(true);
        setLayout(null);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel panel = new PongPanel();
        add(panel);
        repaint();
        revalidate();

    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
    }

    public class main {

        public void main(String[] args) {
            Pong p = new Pong();
        }
    }

}
