import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * You might wonder about the naming I was going to write a simulation (mathematical)
 * for the Saturn rocket to orbit, I guess I changed my mind.
 * 
 * @author charl
 *
 */
public class Rocket extends JFrame {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static int height = 500;
    private static int width = 500;
    private static final int SCALE_X = 25;
    private static final int SCALE_Y = 20;
    private static final int SCALE_X_MYSTERY = 35;
    private static final int SCALE_Y_MYSTERY = 20;
    private static final int FODDER_WIDTH = 15;
    private static final int FODDER_HEIGHT = 10;
    private static final int OFFSET_CRAFT_X = 50;
    private static final int OFFSET_CRAFT_Y = 100;
    private static final int SCALE_SHIELD_X = 150;
    private static final int OFFSET_SHIELD_X = 50;
    private static final int OFFSET_SHIELD_Y = 350;
    private static final int OFFSET_GUN_X = 50;
    private static final int OFFSET_GUN_Y = height -50;
    public static final int GAME_OVER = 2;
    private static final int HIGH_SCORE = 3;
    private static final int NOMINAL = 1;
    private static final int POPUP = 4;
    private int screen = NOMINAL;
    BufferedImage buffer = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
    Craft[][] fodder;
    Shield[] shields;
    Gun gun;
    Bullet[] bullets;
    int selected = 0;
    int gunDirection = 0;
    protected boolean fire = false;
    Explosion explosion = null;
    List<Craft> pointOfFire;
    private int shipCount = 3;
    private int score = 0;
    private int hiscore = 0;
    private List<ScoreName> last10Hiscores = new ArrayList<>();
    private int frameCount = 0;
    private JLabel name = new JLabel("Name:");
    private JTextField nameString = new JTextField("Charlie Swires");
    int right = 1;
    int down = 0;
    int position = 0;
    double speed = 1.0;
    int gunDirectionA;
    int gunDirectionD;

    /**
     * Constructor used once
     */
    public Rocket() {
        super();
        try {
            HiScores george = new HiScores();
            george.load();
            this.last10Hiscores = george.getLast10Hiscores();
            hiscore = this.last10Hiscores.get(this.last10Hiscores.size() - 1).score;
        } catch (ClassNotFoundException | IOException | SecurityException  | IllegalArgumentException e1) {
            System.out.println(e1.getMessage());
        }
        init();
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {


            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyChar() == 'a'||e.getKeyChar() == 'A') {
                    gunDirectionA = -3;
                }
                if (e.getKeyChar() == 'd'||e.getKeyChar() == 'D') {
                    gunDirectionD = 3;

                }
                gunDirection = gunDirectionA + gunDirectionD;
                if (e.getKeyChar() == ' ') {
                    fire  = true;

                }

            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyChar() == 'a'||e.getKeyChar() == 'A') {
                    gunDirectionA = 0;
                }
                if (e.getKeyChar() == 'd'||e.getKeyChar() == 'D') {
                    gunDirectionD = 0;

                }
                gunDirection = gunDirectionA + gunDirectionD;
                if (e.getKeyChar() == ' ') {
                    fire = false;

                }


            }

        });
    }
    /**
     * Used every game cycle sets up prey, predator and shields
     */
    public void init() {

        right = 1;
        down = 0;
        position = 0;
        speed = 1.0;

        shipCount = 3;
        screen = NOMINAL;
        pointOfFire = new ArrayList<Craft>();
        fodder = new Craft[5][11];
        shields = new Shield[3];
        bullets = new Bullet[5];
        fire = false;
        gunDirectionA = 0;
        gunDirectionD = 0;

        score = 0;
        //5rows x 11 columns
        int row = 0;
        for(int i = 0;i < fodder[row].length;i++) {
            fodder[row][i] = new Craft(CraftEnum.TOP_LINE,SCALE_X*i+OFFSET_CRAFT_X,SCALE_Y*row+OFFSET_CRAFT_Y);
            pointOfFire.add(fodder[row][i]);
        }
        row ++;
        for(int i = 0;i < fodder[row].length;i++) {
            fodder[row][i] = new Craft(CraftEnum.MIDDLE_LINE,SCALE_X*i+OFFSET_CRAFT_X,SCALE_Y*row+OFFSET_CRAFT_Y);
            pointOfFire.add(fodder[row][i]);
        }
        row ++;
        for(int i = 0;i < fodder[row].length;i++) {
            fodder[row][i] = new Craft(CraftEnum.MIDDLE_LINE,SCALE_X*i+OFFSET_CRAFT_X,SCALE_Y*row+OFFSET_CRAFT_Y);
            pointOfFire.add(fodder[row][i]);
        }
        row ++;
        for(int i = 0;i < fodder[row].length;i++) {
            fodder[row][i] = new Craft(CraftEnum.BOTTOM_LINE,SCALE_X*i+OFFSET_CRAFT_X,SCALE_Y*row+OFFSET_CRAFT_Y);
            pointOfFire.add(fodder[row][i]);
        }
        row ++;
        for(int i = 0;i < fodder[row].length;i++) {
            fodder[row][i] = new Craft(CraftEnum.BOTTOM_LINE,SCALE_X*i+OFFSET_CRAFT_X,SCALE_Y*row+OFFSET_CRAFT_Y);
            pointOfFire.add(fodder[row][i]);
        }

        //3 shields
        for(int i = 0; i < shields.length; i++)
            shields[i] = new Shield(SCALE_SHIELD_X*i+OFFSET_SHIELD_X,OFFSET_SHIELD_Y);

        //gun
        gun  = new Gun(OFFSET_GUN_X, OFFSET_GUN_Y);


    }
    /**
     * Used for initialising the high score table file and maintaining it.
     * 
     * @author charl
     *
     */
    class HiScores {

        /**
         * 
         */
        private static final long serialVersionUID = 1;
        private List<ScoreName> last10Hiscores;

        public void load() throws IOException, ClassNotFoundException {
            File scores = new File("scores.bin");
            FileInputStream is = new FileInputStream(scores);
            int b;
            byte[] bs = new byte[1];
            int index = 0;
            Random generator = new Random(73890);
            while( (b = is.read()) != -1) {
                bs[index] = (byte)(0xff & (b ^ ( (int)(generator.nextDouble() * 255.0))));
                index++;
                byte[] temp = new byte[index+1];
                int index2 = 0;
                for(byte tb : bs) {
                    temp[index2++]=tb;
                }
                bs = new byte[index+1];
                int i = 0;
                for(byte tb : temp) {
                    bs[i++] = tb;
                }
            }
            is.close();

            byte[] temp = new byte[--index+1];
            int index2 = 0;

            for(@SuppressWarnings("unused") byte tb : temp) {
                temp[index2]=bs[index2++];
            }

            setLast10Hiscores(deserialize(temp));
        }
        List<ScoreName> deserialize(byte[] temp) {
            char[] chars = new char[temp.length];
            int index = 0;
            for(@SuppressWarnings("unused") char c : chars) {
                chars[index] = (char) temp[index++];
            }
            String fred = String.copyValueOf(chars);
            String[] rows = fred.split("],");
            List<ScoreName> list = new ArrayList<>();
            for (String row : rows) {
                String cleaned = row.replace("ScoreName [", "");
                String[] pair = cleaned.split(", ");
                String score = pair[0].split("=")[1];
                String name = pair[1].split("=")[1].replace("]]", "");
                ScoreName sn = new ScoreName();
                sn.name = name;
                sn.score = new Integer(score);
                list.add(sn);
            }
            return list;
        }

        public void save() throws IOException {
            byte[] bs = serialize(last10Hiscores);
            byte[] temp = new byte[bs.length];
            int index = 0;
            Random generator = new Random(73890);
            while(index < bs.length) {
                temp[index] = (byte)(0xff & (bs[index++] ^ ((int)(generator.nextDouble() * 255.0))));

            } 

            File scores = new File("scores.bin");
            FileOutputStream os = new FileOutputStream(scores);
            index = 0;
            while(index < temp.length) {
                os.write(temp[index++]);
            }
            os.flush();
            os.close();

        }

        byte[] serialize(List<ScoreName> last10Hiscores) {
            return last10Hiscores.toString().getBytes();
        }
        public List<ScoreName> getLast10Hiscores() {
            return this.last10Hiscores;
        }

        public void setLast10Hiscores(List<ScoreName> last10Hiscores) {
            this.last10Hiscores = last10Hiscores;
        }
    }
    /**
     * Score name pair
     * 
     * @author charl
     *
     */
    class ScoreName{
        public int score;
        public String name;
        @Override
        public String toString() {
            return "ScoreName [score=" + score + ", name=" + name + "]";
        }

    }
    /**
     * Used in conjunction with the Craft class to allow the Craft class to be 
     * general purpose well for four aliens and their scores.
     * 
     * @author charl
     *
     */
    enum CraftEnum {
        MYSTERY(0),
        TOP_LINE(30),
        MIDDLE_LINE(20),
        BOTTOM_LINE(1);

        private int value;
        private static Map<Integer,CraftEnum> map = new HashMap<>();

        private CraftEnum(int value) {
            this.value = value;
        }

        static {
            for (CraftEnum pageType : CraftEnum.values()) {
                map.put(pageType.value, pageType);
            }
        }

        public static CraftEnum valueOf(int pageType) {
            return (CraftEnum) map.get(pageType);
        }

        public int getValue() {
            return value;
        }    
    }

    /**
     * This is for the animation of the explosions
     * 
     * @author charl
     *
     */
    class Explosion {
        private static final int NO_POINTS = 200;
        private double x[] = null;
        private double y[] = null;
        private double dx[] = null;
        private double dy[] = null;
        private int count = 255;
        public Explosion(int x, int y) {
            this.x = new double[NO_POINTS];
            this.y = new double[NO_POINTS];
            this.dx = new double[NO_POINTS];
            this.dy = new double[NO_POINTS];
            for (int i = 0; i < NO_POINTS;i++) {
                this.x[i]=x;
                this.y[i] =y;
                double bearing = 2.0 * Math.PI * Math.random();
                double speed = Math.random()*5.0;
                this.dx[i] = speed*Math.sin(bearing);;
                this.dy[i] = speed*Math.cos(bearing);
            }
        }

        void draw(Graphics g) {
            Color c = new Color(count,count,count--);
            g.setColor(c);
            for (int i = 0; i < NO_POINTS;i++) {
                g.drawLine((int)x[i], (int)y[i], (int)x[i], (int)y[i]);
                this.x[i]+=dx[i];
                this.y[i]+=dy[i];
            }
        }
        public int getCount() {
            return count;
        }
    }
    /**
     * This is for the alien craft both draw and collide.
     * 
     * @author charl
     *
     */
    class Craft {
        private BufferedImage[] ib = new BufferedImage[2];

        private CraftEnum craftType;
        private int x;
        private int y;

        public Craft(CraftEnum craftType, int x, int y) {
            super();
            this.x = x;
            this.y = y;
            setCraftType(craftType);
        }

        public CraftEnum getCraftType() {
            return craftType;
        }

        public void setCraftType(CraftEnum craftType) {
            switch(craftType) {
            case MYSTERY:
                ib[0] = readBI("bonus.png");
                ib[1] = readBI("bonus.png");
                break;
            case TOP_LINE:
                ib[0] = readBI("top1.png");
                ib[1] = readBI("top2.png");
                break;
            case MIDDLE_LINE:
                ib[0] = readBI("middle1.png");
                ib[1] = readBI("middle2.png");
                break;
            case BOTTOM_LINE:
                ib[0] = readBI("bottom1.png");
                ib[1] = readBI("bottom2.png");
                break;
            default:
                throw new IllegalArgumentException();
            }
            this.craftType = craftType;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }


        public void setIb(BufferedImage[] ib) {
            this.ib = ib;
        }

        public boolean collision(double x, double y) {
            double rsqr;

            if (craftType == CraftEnum.MYSTERY) {
                rsqr = (this.x+ SCALE_X_MYSTERY / 2.0 - x)*(this.x+ SCALE_X_MYSTERY / 2.0 - x)+
                        (this.y+ SCALE_Y_MYSTERY / 2.0 - y)*(this.y+ SCALE_Y_MYSTERY / 2.0 - y);
                return rsqr <= (SCALE_X_MYSTERY/2.0)*(SCALE_X_MYSTERY/2.0);

            } else {
                rsqr = (this.x+ FODDER_WIDTH / 2.0 - x)*(this.x+ FODDER_WIDTH / 2.0 - x)+
                        (this.y+ FODDER_HEIGHT / 2.0 - y)*(this.y+ FODDER_HEIGHT / 2.0 - y);
                return rsqr <= (FODDER_WIDTH/2.0)*(FODDER_WIDTH/2.0);

            }
        }

        public void draw(Graphics g) {
            if(ib != null) {
                if (craftType == CraftEnum.MYSTERY) {
                    g.drawImage(ib[selected],this.getX(), this.getY(), 
                            SCALE_X_MYSTERY, SCALE_Y_MYSTERY, null);
                }else {
                    g.drawImage(ib[selected],this.getX(), this.getY(), 
                            FODDER_WIDTH, FODDER_HEIGHT, null);
                    if (background != null) {
                        Graphics g2 = background.getGraphics();
                        c = Color.BLACK;
                        if (g2 != null) {
                            g2.setColor(c);
                            g2.fillRect(this.getX(), this.getY(), 
                                    FODDER_WIDTH, FODDER_HEIGHT);

                        }
                    }

                }
            }

        }

        public boolean getIsDisplayed() {
            return ib !=null;
        }
    }

    /**
     * Drawing of the shield.
     *  
     * @author charl
     *
     */
    class Shield {
        private BufferedImage ib = null;
        private int x;
        private int y;
        public Shield(int x, int y) {
            super();
            ib = readBI("Shields.png");
            this.x = x;
            this.y = y;
        }


        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        public BufferedImage getIb() {
            return ib;
        }

        public void setIb(BufferedImage ib) {
            this.ib = ib;
        }


        public void draw(Graphics g) {
            if(getIb() != null) {
                g.drawImage(getIb(),getX(), getY(), null);
            }

        }      
    }

    /**
     * Draw and collide for the gun.
     * 
     * @author charl
     *
     */
    class Gun {
        public Gun(int x, int y) {
            super();
            bi = readBI("gun.png");
            this.x = x;
            this.y = y;
        }
        private BufferedImage bi = null;
        private int x;
        private int y;
        //        public BufferedImage getBi() {
        //            return bi;
        //        }
        public void setBi(BufferedImage bi) {
            this.bi = bi;
        }
        public int getX() {
            return x;
        }
        public void setX(int x) {
            this.x = x;
        }
        public int getY() {
            return y;
        }
        public void setY(int y) {
            this.y = y;
        }
        public boolean collision(double x, double y) {
            double rsqr = (this.x+ FODDER_WIDTH / 2.0 - x)*(this.x+ FODDER_WIDTH / 2.0 - x)+
                    (this.y+ FODDER_HEIGHT / 2.0 - y)*(this.y+ FODDER_HEIGHT / 2.0 - y);
            return rsqr <= (FODDER_WIDTH/2.0)*(FODDER_WIDTH/2.0);
        }
        public void draw(Graphics g) {
            if(bi != null) {
                g.drawImage(bi,this.getX(), this.getY(),FODDER_WIDTH, FODDER_HEIGHT, null);
            }        
        }
    }
    /**
     * Draw enemy bullet and friend.
     * 
     * @author charl
     *
     */
    class Bullet {
        public Bullet(int x, int y) {
            super();
            this.x = x;
            this.y = y;
        }
        private int x;
        private int y;
        private boolean hitShields = false;
        public int getX() {
            return x;
        }
        public void setX(int x) {
            this.x = x;
        }
        public int getY() {
            return y;
        }
        public void setY(int y) {
            this.y = y;
        }
        public void drawFriendly(Graphics g) {
            c = Color.RED;
            g.setColor(c);
            g.drawLine(getX(), getY(), getX(), getY()-5);
            Graphics g2 = background.getGraphics();
            c = Color.BLACK;
            g2.setColor(c);
            hitShields = (background.getRGB(x, y) > Color.BLACK.getRGB());  //eat shields
            g2.drawLine(getX(), getY(), getX(), getY()-5);


        }
        public void drawEnemy(Graphics g) {
            c = Color.YELLOW;
            g.setColor(c);
            g.drawLine(getX(), getY(), getX(), getY()+5);
            Graphics g2 = background.getGraphics();
            c = Color.BLACK;
            g2.setColor(c);
            hitShields = (background.getRGB(x, y) > Color.BLACK.getRGB());  //eat shields
            g2.drawLine(getX(), getY(), getX(), getY()+5);

        }
        public boolean isHitShields() {
            return hitShields;
        }
        public void setHitShields(boolean hitShields) {
            this.hitShields = hitShields;
        }
    }

    private static Map<String, BufferedImage> bifiles = new HashMap<>();

    /**
     * Prevents common download of images happening they only happen once
     * 
     * @param filename
     * @return
     */
    private static BufferedImage readBI(String filename) {
        if(!bifiles.containsKey(filename)) {
            try {
                bifiles.put(filename, ImageIO.read(new File(filename)));
            } catch (IOException e) {
                throw new RuntimeException("filename not found: "+ filename);
            }           
        }
        return bifiles.get(filename);
    }
    Color c;
    BufferedImage background = null;
    Craft mysteryCraft = null;

    /**
     * Called each frame refresh to draw everything. Double buffered.
     * 
     */
    public void paint(Graphics g) {
        Graphics screengc = null;

        if (g != null) {
            screengc = g;
            g=buffer.getGraphics();

            switch (screen) {
            case NOMINAL:
                //Clear
                c = explosion != null && explosion.getCount() >= 254 ? Color.YELLOW : Color.BLACK;
                g.setColor(c);

                if (background == null) {
                    g.fillRect(0, 0, width, height);
                    //draw shields
                    for (int i = 0; i < shields.length; i++) {
                        shields[i].draw(g);
                    }
                    if (!(explosion != null && explosion.getCount() >= 254 )) {
                        background = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                        for(int y = 0; y < height; y++) {
                            for(int x = 0; x < width; x++) {
                                background.setRGB(x, y, buffer.getRGB(x, y));
                            }
                        }
                    }

                } else {
                    g.drawImage(background, 0, 0, width, height, null);
                }
                //draw fodder
                if(mysteryCraft != null)mysteryCraft.draw(g);
                for (int row = 0; row < fodder.length; row++) {
                    for (int i = 0; i < fodder[row].length; i++) {
                        fodder[row][i].draw(g);
                    }
                }
                //draw gun
                gun.draw(g);

                //draw bullets
                if(bullets[0] != null) {
                    bullets[0].drawFriendly(g);
                }

                for (int i = 1; i <bullets.length; i++) {
                    if(bullets[i] != null) {
                        bullets[i].drawEnemy(g);

                    }

                }

                if(explosion != null) {
                    if (explosion.getCount() > 0)
                        explosion.draw(g);
                    else
                        explosion = null;
                }

                c = Color.WHITE;
                g.setColor(c);
                g.drawString("Score: "+score, width /2 -70, 50);
                g.drawString("High Score: "+hiscore, width /2+50, 50);
                g.drawString("Lives: "+shipCount, width /2-50, height -20);

                break;
            case GAME_OVER:
                c = Color.BLACK;
                g.setColor(c);

                g.fillRect(0, 0, width, height);
                c = Color.WHITE;
                g.setColor(c);
                g.drawString("GAME OVER", width /2 -50, height /2);
                break;
            case HIGH_SCORE:
                c = Color.BLACK;
                g.setColor(c);

                g.fillRect(0, 0, width, height);
                c = Color.WHITE;
                g.setColor(c);
                g.drawString("HI SCORES", width /2 -40, 70);

                for (int i = 0; i < 10;i++) {
                    g.drawLine(width /2 -100, i * 30 + 100,width /2 +100, i * 30 + 100);
                    int lastFirst = last10Hiscores != null && last10Hiscores.size() > 0 ? last10Hiscores.size() - i -1:0;
                    if (last10Hiscores != null && last10Hiscores.size() > 0 && lastFirst >= 0 && last10Hiscores.get(lastFirst) != null) {
                        ScoreName sn = last10Hiscores.get(lastFirst);
                        g.drawString(""+sn.score, width /2 -60, i * 30 + 90);
                        g.drawString(""+sn.name, width /2, i * 30 + 90);
                    } 
                }
                break;
            case POPUP:
                break;

            default:
                throw new InvalidParameterException("screen= "+screen);
            }

            screengc.drawImage(buffer, 0, 0, null);
        }
    }
    /**
     * High score entry.
     * 
     * @author charl
     *
     */
    class HiScorePanel extends JPanel implements ActionListener{
        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        public HiScorePanel() {
            super();
            new JPanel(new BorderLayout());
            add(name,BorderLayout.WEST);
            add(nameString,BorderLayout.CENTER);
            JButton b = new JButton("select");
            add(b,BorderLayout.SOUTH);
            b.addActionListener(this);

        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (score > hiscore) {
                hiscore = score;
                ScoreName sn = new ScoreName();
                sn.score = hiscore;
                sn.name = nameString.getText();
                if (last10Hiscores.size() < 10) {

                    last10Hiscores.add(sn);
                } else {
                    last10Hiscores.remove(0);
                    last10Hiscores.add(sn);

                } 
                HiScores george = new HiScores();
                george.setLast10Hiscores(last10Hiscores);;
                try {
                    george.save();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            setVisible(false);

        }
    }
    /**
     * This is the control logic the tick gets shorter and shorter if you get through a level
     * 
     * @author charl
     *
     */
    class MyThread extends Thread{
        HiScorePanel hi;
        long tick = 40;   
        @SuppressWarnings("static-access")
        public void run() {
            while (true) {
                boolean clearedLevel = false;
                do {
                    background = null;

                    int mystery = (int)(Math.random() * 500.0);
                    int mysteryDirection = -6;
                    mysteryCraft = null;
                    while(shipCount > 0 && !clearedLevel) {
                        position += right;
                        if (position >= 150) {  //zig zag
                            right = -(int)speed;
                            down = 10;
                            speed+=0.3;

                        } else if (position <= 0) {
                            right = (int)speed;
                            down = 10;
                            speed+=0.3;
                        }
                        if(position%5 == 0) {   //craft frame
                            selected = selected ^ 1;
                        }
                        //draw fodder
                        for (int row = 0; row < fodder.length; row++) {
                            for (int i = 0; i < fodder[row].length; i++) {
                                if(fodder[row][i].getIsDisplayed()) {
                                    fodder[row][i].setX(fodder[row][i].getX()+right);
                                    fodder[row][i].setY(fodder[row][i].getY()+down);
                                }
                            }
                        }  
                        down = 0;

                        // gun
                        if (gun.getX()+gunDirection >= 50 && gun.getX()+gunDirection <= (width - 50))
                            gun.setX(gun.getX()+gunDirection);

                        //bullets gun
                        if (bullets[0] != null) {
                            bullets[0].setY(bullets[0].getY() - 6);
                            Craft hit = collision(bullets[0].getX(), bullets[0].getY());    //hit enemy
                            if (bullets[0].getY() <= 10) {  //top erase
                                bullets[0] = null;
                            }
                            if (hit != null) {  //hit true set off explosion
                                score += hit.getCraftType().getValue();
                                explosion = new Explosion(bullets[0].getX(), bullets[0].getY());
                                bullets[0]=null;
                            }
                            if (bullets[0] != null && bullets[0].isHitShields()) {  // hit shields erase
                                bullets[0]=null;

                            }
                        } else if (fire) {
                            bullets[0]= new Bullet(gun.getX()+8,gun.getY());
                        }

                        //bullets craft
                        for (int i = 1; i < bullets.length; i++) {
                            // bullet collides with gun
                            if (bullets[i] != null) {
                                bullets[i].setY(bullets[i].getY()+6);
                                if (gun.collision(bullets[i].getX(), bullets[i].getY())) {
                                    explosion = new Explosion(bullets[i].getX(), bullets[i].getY());
                                    shipCount--;
                                    bullets[i] = null;
                                }
                                if (bullets[i] != null && bullets[i].isHitShields()) {  //erase if hit shields
                                    bullets[i]=null;

                                }
                            } else {    //spare bullet initialise from available craft
                                Craft naughtyOne = pointOfFire.get((int)(Math.random()*(pointOfFire.size()-1)));
                                bullets[i] = new Bullet(naughtyOne.getX()+8,naughtyOne.getY());
                            }
                            if (bullets[i] != null && bullets[i].getY() >= height) {    //erase if beyond window
                                bullets[i] = null;
                            }

                        }
                        // enemy collides with gun.
                        for (int i = 0; i < pointOfFire.size(); i++) {
                            if (gun.collision(pointOfFire.get(i).getX(), pointOfFire.get(i).getY())) {
                                explosion = new Explosion(pointOfFire.get(i).getX(), pointOfFire.get(i).getY());
                                shipCount--;
                                break;
                            }
                        }
                        clearedLevel  = pointOfFire.size() == 0;    //no craft then cleared level
                        if (pointOfFire.size() > 0)
                            for(Craft craft : pointOfFire) {
                                if (craft.getY() >= height) {   //terminate game if enemy craft reach bottom
                                    shipCount = 0;
                                    break;
                                }
                            }
                        mystery--;
                        if (mystery <= 0) {
                            if(mysteryCraft == null) {
                                mysteryCraft = new Craft(CraftEnum.MYSTERY, 500, 60);
                            } else {
                                if (mysteryCraft.getX() <= 50) {    //animate
                                    mysteryDirection = 6;
                                }
                                mysteryCraft.setX(mysteryCraft.getX() + mysteryDirection);
                                if (bullets[0] != null && mysteryCraft.collision(bullets[0].getX(), bullets[0].getY())) {
                                    score += (int)(Math.random() * 500.0);  // hit the bonus craft
                                    explosion = new Explosion(bullets[0].getX(), bullets[0].getY());
                                    bullets[0] = null;
                                    mysteryCraft = null;
                                    mysteryDirection = -6;

                                }
                                if (mysteryCraft != null && mysteryCraft.getX() >= 550){    //create new craft at random time
                                    mystery = (int)(Math.random() * 500.0);
                                    mysteryCraft = null;
                                    mysteryDirection = -6;

                                }
                            }
                        }
                        try {
                            this.sleep(tick);
                        } catch (InterruptedException e) {
                            // e.printStackTrace();
                        }
                        repaint();  //refresh screen
                    }
                    if (clearedLevel) { //next go
                        int tempScore = score;
                        int tempLives = shipCount;
                        init();
                        score = tempScore;
                        if (clearedLevel)shipCount = tempLives + 1;
                        else shipCount = 0;
                        tick--;
                    }
                } while (shipCount > 0 && clearedLevel);

                frameCount++;   //outside the game
                if (frameCount < 49)
                    screen = GAME_OVER;
                else if (frameCount > 50 &&  frameCount < 500) {    //enter the high score
                    screen = POPUP;
                    if (score > hiscore) {
                        this.hi.setVisible(true);
                    }else {
                        frameCount = 501; //skip high score entry
                    }
                }
                else if (frameCount== 500) {    //confirm
                    if(score > hiscore)this.hi.actionPerformed(null);
                    else this.hi.setVisible(false);
                }
                else if (frameCount > 500 && frameCount < 750) {    //display table
                    screen = HIGH_SCORE;

                }
                else if (frameCount > 750){     //restart the game
                    frameCount = 0;
                    init();
                    tick=40;
                }
                try {
                    this.sleep(40);     //25 frames a second
                } catch (InterruptedException e) {
                    // e.printStackTrace();
                }
                if (screen != POPUP)repaint();  //don't repaint when you are entering your name
            }
        }

        public void setHi(HiScorePanel hi) {
            this.hi =hi;

        }
    }

    /**
     * 
     * @param x
     * @param y
     * @return
     */
    private Craft collision(int x, int y) {
        //scan the aliens for a collision
        for (int row = fodder.length - 1; row >= 0; row--) {
            for (int i = 0; i < fodder[row].length; i++) {
                if(fodder[row][i].getIsDisplayed() && fodder[row][i].collision((double)x,(double)y)){
                    fodder[row][i].setIb(null);
                    pointOfFire.remove(fodder[row][i]);
                    return fodder[row][i];
                }

            }
        }  
        return null;
    }
    /**
     * 
     * @param args
     */
    public static void main(String[] args) {    //set everything going
        Rocket app = new Rocket();
        Container cp = app.getContentPane();
        MyThread mythread = app.new MyThread();
        mythread.start();
        app.setSize(width, height);
        app.setTitle("Rockets");
        app.setVisible(true);
        HiScorePanel hi = app.new HiScorePanel();
        mythread.setHi(hi);
        hi.setSize(300, 200);
        hi.setVisible(false);
        cp.add(hi);
    }

}
