import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.awt.*;

public class Sudoku extends JFrame{
    public static int width = 400;
    public static int height = 400;    
    public int[][] value = new int[9][9];
    /*
    int[][] value = {{0,6,0,5,9,3,0,0,0},
                     {9,0,1,0,0,0,5,0,0},
                     {0,3,0,4,0,0,0,9,0},
                     {1,0,8,0,2,0,0,0,4},
                     {4,0,0,3,0,9,0,0,1},
                     {2,0,0,0,1,0,6,0,9},
                     {0,8,0,0,0,6,0,2,0},
                     {0,0,4,0,0,0,8,0,7},
                     {0,0,0,7,8,5,0,1,0}};
    */
    /*
    int[][] value = {{8,0,0,0,0,0,0,0,0},
                     {0,0,3,6,0,0,0,0,0},
                     {0,7,0,0,9,0,2,0,0},
                     {0,5,0,0,0,7,0,0,0},
                     {0,0,0,0,4,0,7,0,0},
                     {0,0,0,1,0,5,0,3,0},
                     {0,0,1,0,0,0,0,6,8},
                     {0,0,8,5,0,0,0,1,0},
                     {0,9,0,0,0,0,4,0,0}};
    */
    public int[][][] ans = new int[1000][9][9];
    public int ansn = 0;
    public int currentansn = 0;
    
    public static Sudoku map;
    public static JPanel sudokumap;
    public static JTextField[][] Input; 
    public static JPanel downarea;
    public static JButton solve;
    public static JButton next;
    public static JButton prev;
    public static JTextField show;
    public static JButton showans;
    public static JButton clear;
    
    class prevAns implements ActionListener{
        public void actionPerformed(ActionEvent e){
            if (currentansn == 0){ return; }
            currentansn--;
            for (int i=0; i<9; i++)
                for (int j=0; j<9; j++)
                    Input[i][j].setText(ans[currentansn][i][j]+"");
            
            show.setText("Total ans: " + ansn + "       Current ans: "+(currentansn + 1));
        }
    }
    class nextAns implements ActionListener{
        public void actionPerformed(ActionEvent e){
            if (currentansn == ansn - 1){ return; }
            currentansn++;
            for (int i=0; i<9; i++)
                for (int j=0; j<9; j++)
                    Input[i][j].setText(ans[currentansn][i][j]+"");
            
            show.setText("Total ans: " + ansn + "       Current ans: "+(currentansn + 1));
        }
    }
    class showAns implements ActionListener{
        public void actionPerformed(ActionEvent e){
            for (int i=0; i<9; i++)
                for (int j=0; j<9; j++)
                    Input[i][j].setText(ans[currentansn][i][j]+"");
            
            show.setText("Total ans: " + ansn + "       Current ans: "+(currentansn + 1));
        }
    }
    class clearAns implements ActionListener{
        public void actionPerformed(ActionEvent e){
            for (int i=0; i<9; i++)
                for (int j=0; j<9; j++)
                    Input[i][j].setText("");
            
            show.setText("Total ans: " + ansn + "       Current ans: null");
        }
    }    
    class Calculate implements ActionListener{
        public void actionPerformed(ActionEvent e){
            System.out.println("Start calculate the answers...");
            
            for (int i=0; i<9; i++)
                for (int j=0; j<9; j++){
                    String tmp = Input[i][j].getText();
                    if (tmp.length() != 0) value[i][j] = Integer.parseInt(tmp); else value[i][j] = 0;
                    //System.out.println(i + " " + j + " " + value[i][j]);
                }
            
            calculate();
        }
        
        private boolean isValid(int[][] grid){
            for (int i=0; i<9; i++)
                if(!is1To9(grid[i])) return false;
        
            for (int j=0; j<9; j++){
                int[] column = new int[9];
                for (int i=0; i<9; i++) column[i] = grid[i][j];
                if (!is1To9(column)) return false;
            }
        
            for (int i=0; i<3; i++){
                for (int j=0; j<3; j++){
                    int k = 0;
                    int[] list = new int[9];
                    for (int row = i * 3; row < i * 3 + 3; row++)
                        for (int col = j * 3; col < j * 3 + 3; col++) list[k++] = grid[row][col];
                    
                    if (!is1To9(list)) return false;
                }
            }
            return true;
        }
    
        public boolean is1To9(int[] list){
            int[] temp = new int[list.length];
            System.arraycopy(list,0,temp,0,list.length);
            java.util.Arrays.sort(temp);
        
            for(int i=0 ;i<9; i++) if (temp[i] != i+1) return false;
            return true;
        }
    
        private void dfs(int i,int j,int[][] tmp,boolean[][] row,boolean[][] col,boolean[][] set){
            //System.out.println(i + " " + j);
            int nexti,nextj;
            if (i == 9){
                if (isValid(tmp)){
                    for(int ii = 0; ii < 9; ii++){
                        for(int jj = 0; jj < 9; jj++){
                            ans[ansn][ii][jj] = tmp[ii][jj];
                            System.out.print(tmp[ii][jj] + " ");
                        }
                        System.out.println();
                    }
                    ansn++;
                    System.out.println();
                } else System.out.println("wrong answer!");
                
                return;
            }
            if (j == 8){ nextj = 0; nexti = i + 1; } 
                else { nextj = j+1; nexti = i; }
                
            if (value[i][j] == 0){
                for(int k=0; k<9; k++){
                    //System.out.println("row"+i+" col"+j+" k"+k+" row"+row[i][k]+" col"+col[j][k]+" set"+set[i/3 * 3 + j/3][k]);
                
                    if (row[i][k] == false && col[j][k] == false && set[i/3 * 3 + j/3][k] == false){
                        tmp[i][j] = k + 1;
                        row[i][k] = true;
                        col[j][k] = true;
                        set[i/3 * 3 + j/3][k] = true;
                        dfs(nexti,nextj,tmp,row,col,set);
                        row[i][k] = false;
                        col[j][k] = false;
                        set[i/3 * 3 + j/3][k] = false;
                    }
                }
            }else {
                dfs(nexti,nextj,tmp,row,col,set);
            }
            
        }
        private void calculate(){
            boolean[][] row = new boolean[9][9];
            boolean[][] col = new boolean[9][9];
            boolean[][] set = new boolean[9][9];
            int[][] tmp = new int[9][9];
            for (int i=0; i<9; i++)
                for (int j=0; j<9; j++){
                    tmp[i][j] = value[i][j];
                    if (tmp[i][j] > 0){
                        row[i][tmp[i][j]-1] = true;
                        col[j][tmp[i][j]-1] = true;
                        set[i/3 * 3 + j/3][tmp[i][j]-1] = true;
                    }
                }
            dfs(0,0,tmp,row,col,set);
            System.out.println("calculate done!");
            System.out.println("ansn = "+ ansn);
            show.setText("Total ans: " + ansn + "       Current ans: "+"0");
        }
        
    }
    
    Sudoku(){
        //9*9 sudokumap using 9*9 input[][]
        sudokumap = new JPanel();
        sudokumap.setLayout(new GridLayout(9,9));
        
        Input = new JTextField[9][9];
        for (int i=0; i<9; i++)
            for (int j=0; j<9; j++){
                Input[i][j] = new JTextField();
                Input[i][j].setHorizontalAlignment(JTextField.CENTER); 
                sudokumap.add(Input[i][j]);
            }
        
        //down button
        downarea = new JPanel();
        downarea.setLayout(new GridLayout(1,5));
        
        solve = new JButton("solve");
        solve.addActionListener(new Calculate());
        
        showans = new JButton("show");
        showans.addActionListener(new showAns());
        
        prev = new JButton("prev");
        prev.addActionListener(new prevAns());
        
        next = new JButton("next");
        next.addActionListener(new nextAns());
        
        clear = new JButton("clear");
        clear.addActionListener(new clearAns());
        
        downarea.add(solve);
        downarea.add(showans);
        downarea.add(prev);
        downarea.add(next);
        downarea.add(clear);
        
        //top 
        show = new JTextField();
        show.setEditable(false);
    }
    
    public static void main(String[] args){
        //JFrame map
        map = new Sudoku();
        map.setTitle("Sudoku By Swind");
		map.setLocationRelativeTo(null);
        map.setSize(width,height);
        map.setLayout(new BorderLayout());
		map.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);    
	    
        //add 
        
        map.add(sudokumap,BorderLayout.CENTER);
        map.add(downarea,BorderLayout.SOUTH);
        map.add(show,BorderLayout.NORTH);
        map.setVisible(true);
        
    }
   
}
