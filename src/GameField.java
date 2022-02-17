import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GameField extends JPanel implements ActionListener{
    private final int SIZE = 320;
    private final int DOT_SIZE = 16;
    private final int ALL_DOTS = 400;
    private Image dot;
    private Image apple;
    private Image super_apple;
    private int super_appleX = -100;
    private int super_appleY = -100;
    private int summonSApple;
    private int appleX;
    private int appleY;
    private int[] x = new int[ALL_DOTS];
    private int[] y = new int[ALL_DOTS];
    private int dots;
    private Timer timer;
    private boolean left = false;
    private boolean right = true;
    private boolean up = false;
    private boolean down = false;
    private boolean inGame = true;

    private int result;
    private int speed = 250;
    private String[] options = {"Easy", "Normal", "Hard"};


    public GameField(){
        setBackground(Color.black);
        loadImages();
        selectSpeed();
        initGame();
        addKeyListener(new FieldKeyListener());
        setFocusable(true);

    }

    public void selectSpeed(){
        int x = JOptionPane.showOptionDialog(null, "Game Difficulty",
                "Select your Game Difficulty",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        if(x == 0){
            speed = 250;
        }
        if(x == 1){
            speed = 100;
        }
        if(x == 2){
            speed = 50;
        }
    }

    public void initGame(){
        dots = 3;
        for (int i = 0; i < dots; i++) {
            x[i] = 48 - i*DOT_SIZE;
            y[i] = 48;
        }
        timer = new Timer(speed,this);
        timer.start();
        createApple();
    }

    public void createApple(){
        appleX = new Random().nextInt(15)*DOT_SIZE;
        appleY = new Random().nextInt(15)*DOT_SIZE;
        rSummonAppleS();
    }

    public void rSummonAppleS(){
        summonSApple = new Random().nextInt(5);
        if (summonSApple == 3){
            super_appleX = new Random().nextInt(15)*DOT_SIZE;
            super_appleY = new Random().nextInt(15)*DOT_SIZE;
        }
    }


    public void loadImages(){
        ImageIcon iia = new ImageIcon("textures/apple.png");
        apple = iia.getImage();
        ImageIcon iid = new ImageIcon("textures/dot.png");
        dot = iid.getImage();
        ImageIcon iis = new ImageIcon("textures/super_apple.png");
        super_apple = iis.getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(inGame){
            g.setColor(Color.white);
            g.drawString("Счет: " + result, 0, 15);
            g.drawImage(apple,appleX,appleY,this);
            g.drawImage(super_apple, super_appleX, super_appleY, this);
            for (int i = 0; i < dots; i++) {
                g.drawImage(dot,x[i],y[i],this);
            }
        } else{
            String str = "Game Over";
            g.setColor(Color.white);
            g.drawString(str,125,SIZE/2);
            g.drawString("Результат: " + result, 120, SIZE/2+20);
        }
    }

    public void move(){
        for (int i = dots; i > 0; i--) {
            x[i] = x[i-1];
            y[i] = y[i-1];
        }
        if(left){
            x[0] -= DOT_SIZE;
        }
        if(right){
            x[0] += DOT_SIZE;
        } if(up){
            y[0] -= DOT_SIZE;
        } if(down){
            y[0] += DOT_SIZE;
        }
    }

    public void checkApple(){
        if(x[0] == appleX && y[0] == appleY){
            dots++;
            result++;
            createApple();
        }
        if(x[0] == super_appleX && y[0] == super_appleY){
            dots += 2;
            result += 2;
            super_appleX = -100;
            super_appleY = -100;
            rSummonAppleS();
        }
    }

    public void checkCollisions(){
        for (int i = dots; i >0 ; i--) {
            if(i>4 && x[0] == x[i] && y[0] == y[i]){
                inGame = false;
            }
        }

        if(x[0]>SIZE){
            inGame = false;
        }
        if(x[0]<0){
            inGame = false;
        }
        if(y[0]>SIZE){
            inGame = false;
        }
        if(y[0]<0){
            inGame = false;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(inGame){
            checkApple();
            checkCollisions();
            move();

        }
        repaint();
    }

    class FieldKeyListener extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e);
            int key = e.getKeyCode();
            if(key == KeyEvent.VK_LEFT && !right){
                left = true;
                up = false;
                down = false;
            }
            if(key == KeyEvent.VK_RIGHT && !left){
                right = true;
                up = false;
                down = false;
            }

            if(key == KeyEvent.VK_UP && !down){
                right = false;
                up = true;
                left = false;
            }
            if(key == KeyEvent.VK_DOWN && !up){
                right = false;
                down = true;
                left = false;
            }
        }
    }


}