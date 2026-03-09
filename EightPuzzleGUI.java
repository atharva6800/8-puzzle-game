import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.Arrays;

public class EightPuzzleGUI extends JFrame {

    JPanel panel;
    JButton[][] buttons = new JButton[3][3];
    int[][] board = new int[3][3];

    JLabel moveLabel, timerLabel, scoreLabel;
    int moves = 0, seconds = 0;
    javax.swing.Timer timer;

    String difficulty = "Medium";

    public EightPuzzleGUI() {
        setTitle("8 Puzzle Game - Advanced");
        setSize(420, 600);
        setLayout(new BorderLayout());

        panel = new JPanel(new GridLayout(3,3,5,5));
        panel.setBackground(Color.BLACK);

        Font font = new Font("Arial", Font.BOLD, 28);

        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                buttons[i][j] = new JButton();
                buttons[i][j].setFont(font);
                buttons[i][j].setFocusPainted(false);

                int x=i,y=j;
                buttons[i][j].addActionListener(e -> moveTile(x,y));

                panel.add(buttons[i][j]);
            }
        }

        JPanel top = new JPanel();
        moveLabel = new JLabel("Moves: 0");
        timerLabel = new JLabel("Time: 0");
        scoreLabel = new JLabel("Score: 0");

        JButton restart = new JButton("Restart");
        restart.addActionListener(e -> startGame());

        JButton hint = new JButton("Hint");
        hint.addActionListener(e -> highlightMoves());

        JButton solve = new JButton("Solve");
        solve.addActionListener(e -> autoSolve());

        String[] levels = {"Easy","Medium","Hard"};
        JComboBox<String> levelBox = new JComboBox<>(levels);
        levelBox.addActionListener(e -> {
            difficulty = (String) levelBox.getSelectedItem();
            startGame();
        });

        top.add(moveLabel); top.add(timerLabel); top.add(scoreLabel);
        top.add(restart); top.add(hint); top.add(solve); top.add(levelBox);

        add(top,BorderLayout.NORTH);
        add(panel,BorderLayout.CENTER);

        startGame();

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    void startGame(){
        moves=0; seconds=0;
        generateSolvableBoard();
        updateUI();

        if(timer!=null) timer.stop();
        timer = new javax.swing.Timer(1000,e->{
            seconds++;
            timerLabel.setText("Time: "+seconds);
            scoreLabel.setText("Score: "+calculateScore());
        });
        timer.start();
    }

    void generateSolvableBoard(){
        java.util.List<Integer> nums = new ArrayList<>();
        for(int i=0;i<9;i++) nums.add(i);

        int shuffleCount = difficulty.equals("Easy")?10:
                           difficulty.equals("Medium")?30:80;

        for(int i=0;i<shuffleCount;i++) Collections.shuffle(nums);

        int k=0;
        for(int i=0;i<3;i++)
            for(int j=0;j<3;j++)
                board[i][j]=nums.get(k++);
    }

    void updateUI(){
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                if(board[i][j]==0){
                    buttons[i][j].setText("");
                    buttons[i][j].setBackground(Color.LIGHT_GRAY);
                }else{
                    buttons[i][j].setText(""+board[i][j]);
                    buttons[i][j].setBackground(Color.WHITE);
                }
            }
        }
        moveLabel.setText("Moves: "+moves);
    }

    void moveTile(int x,int y){
        int[] b=findBlank();
        int bx=b[0],by=b[1];

        if(Math.abs(bx-x)+Math.abs(by-y)==1){
            int temp = board[bx][by];
            board[bx][by]=board[x][y];
            board[x][y]=temp;

            moves++;
            playSound();
            updateUI();

            if(isGoal()){
                timer.stop();
                saveHighScore();
                JOptionPane.showMessageDialog(this,
                        "🎉 Solved!\nScore: "+calculateScore());
            }
        }
    }

    void highlightMoves(){
        int[] b=findBlank();
        int bx=b[0],by=b[1];

        for(JButton[] row:buttons)
            for(JButton btn:row)
                btn.setBackground(Color.WHITE);

        if(bx>0) buttons[bx-1][by].setBackground(Color.YELLOW);
        if(bx<2) buttons[bx+1][by].setBackground(Color.YELLOW);
        if(by>0) buttons[bx][by-1].setBackground(Color.YELLOW);
        if(by<2) buttons[bx][by+1].setBackground(Color.YELLOW);
    }

    void autoSolve(){
        new Thread(()->{
            java.util.List<int[][]> path = solve(board);
            try{
                for(int[][] step:path){
                    Thread.sleep(300);
                    board=copy(step);
                    SwingUtilities.invokeLater(()->updateUI());
                }
            }catch(Exception e){}
        }).start();
    }

    java.util.List<int[][]> solve(int[][] start){
        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(n->n.f));
        Set<String> vis=new HashSet<>();

        pq.add(new Node(start,0,null));

        while(!pq.isEmpty()){
            Node cur=pq.poll();
            if(isGoal(cur.s)) return buildPath(cur);

            vis.add(Arrays.deepToString(cur.s));

            for(int[][] n:getNeighbors(cur.s)){
                if(!vis.contains(Arrays.deepToString(n))){
                    pq.add(new Node(n,cur.g+1,cur));
                }
            }
        }
        return new ArrayList<>();
    }

    java.util.List<int[][]> buildPath(Node n){
        java.util.List<int[][]> path=new ArrayList<>();
        while(n!=null){ path.add(n.s); n=n.p;}
        Collections.reverse(path);
        return path;
    }

    static class Node{
        int[][] s; int g,f; Node p;
        Node(int[][] s,int g,Node p){
            this.s=copy(s); this.g=g; this.p=p;
            this.f=g+heuristic(s);
        }
    }

    static int heuristic(int[][] s){
        int d=0;
        for(int i=0;i<3;i++)
            for(int j=0;j<3;j++){
                int v=s[i][j];
                if(v!=0){
                    int x=(v-1)/3,y=(v-1)%3;
                    d+=Math.abs(i-x)+Math.abs(j-y);
                }
            }
        return d;
    }

    java.util.List<int[][]> getNeighbors(int[][] s){
        java.util.List<int[][]> list = new ArrayList<>();
        int[] b=findBlank(s);
        int bx=b[0],by=b[1];

        int[][] dir={{1,0},{-1,0},{0,1},{0,-1}};
        for(int[] d:dir){
            int nx=bx+d[0],ny=by+d[1];
            if(nx>=0&&nx<3&&ny>=0&&ny<3){
                int[][] n=copy(s);
                n[bx][by]=n[nx][ny];
                n[nx][ny]=0;
                list.add(n);
            }
        }
        return list;
    }

    int calculateScore(){
        return Math.max(1000-(moves*5+seconds*2),0);
    }

    void saveHighScore(){
        try{
            File f=new File("highscore.txt");
            int score=calculateScore();

            if(!f.exists() || score>readHighScore()){
                FileWriter w=new FileWriter(f);
                w.write(""+score);
                w.close();
            }
        }catch(Exception e){}
    }

    int readHighScore(){
        try{
            BufferedReader r=new BufferedReader(new FileReader("highscore.txt"));
            return Integer.parseInt(r.readLine());
        }catch(Exception e){ return 0;}
    }

    void playSound(){
        Toolkit.getDefaultToolkit().beep();
    }

    int[] findBlank(){
        return findBlank(board);
    }

    int[] findBlank(int[][] s){
        for(int i=0;i<3;i++)
            for(int j=0;j<3;j++)
                if(s[i][j]==0) return new int[]{i,j};
        return null;
    }

    boolean isGoal(){
        return isGoal(board);
    }

    boolean isGoal(int[][] s){
        int[][] g={{1,2,3},{4,5,6},{7,8,0}};
        return Arrays.deepEquals(s,g);
    }

    static int[][] copy(int[][] s){
        int[][] n=new int[3][3];
        for(int i=0;i<3;i++) n[i]=s[i].clone();
        return n;
    }

    public static void main(String[] args){
        new EightPuzzleGUI();
    }
}