package reversi;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class createBoard extends JPanel{
	
	public static final int WIDTH = 8;
	public static final int HEIGHT = 8;
	
	public createBoard(){
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setBackground(Color.BLACK);
	}
	
	public void board(int player, JLabel messageLabel, JPanel[][] boardPanels, IController controller, JFrame playerFrame) {
		
        playerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        playerFrame.setLayout(new BorderLayout());
        playerFrame.setResizable(false);
        playerFrame.setSize(600, 600);
        playerFrame.getContentPane().setBackground(Color.WHITE);
        playerFrame.setLocation((player == 1) ? 120 : 720, 120);
        playerFrame.add(messageLabel, BorderLayout.NORTH);

        JPanel boardPanel = new JPanel(new GridLayout(8, 8)); 
        boardPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); 
        playerFrame.add(boardPanel, BorderLayout.CENTER);
        
        
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                JPanel cellPanel = new JPanel();
                cellPanel.setBackground(new Color(0, 200, 0)); 
                cellPanel.setBorder(new LineBorder(Color.BLACK));
                boardPanel.add(cellPanel);
                boardPanels[row][col] = cellPanel;

                int finalRow = row;
                int finalCol = col;
                cellPanel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) { 
                        if (player == 1) {
                        	controller.squareSelected(player, finalRow, finalCol); 
                        } else {
                        	controller.squareSelected(player, 7 - finalRow, 7 - finalCol);
                        }
                    }
                });
            }
        }
	}
	
	
	
	void chip(JPanel[][] boardPanels, int row, int col, Color color) {
		
		JPanel cellPanel = boardPanels[row][col];
        cellPanel.removeAll(); // Clear existing components
        Color oppositeColor = (color == Color.WHITE) ? Color.BLACK : Color.WHITE;
		JPanel chipPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
            	super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setColor(color);
                g2d.fillOval(5, 5, getWidth() - 10, getHeight() - 10);
                g2d.setColor(oppositeColor); 
                g2d.drawOval(5, 5, getWidth() - 10, getHeight() - 10); 
                g2d.dispose();
            }
        };
        chipPanel.setOpaque(false);
        chipPanel.setPreferredSize(new Dimension(40, 40));
        
        cellPanel.setLayout(new BorderLayout());
        cellPanel.add(chipPanel, BorderLayout.CENTER);
	}

	public void rChip(JPanel[][] boardPanels, int row, int col) {
        JPanel cellPanel = boardPanels[row][col];
        
        cellPanel.removeAll();
        cellPanel.repaint();
    }
	
	
	
	

}
