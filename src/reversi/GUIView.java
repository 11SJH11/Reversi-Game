package reversi;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GUIView implements IView{
    private IModel model;
    private IController controller;
    private JLabel messageLabelWhite;
    private JLabel messageLabelBlack;
    private JPanel[][] whiteBoardPanels;
    private JPanel[][] blackBoardPanels;
    private createBoard board = new createBoard();
    private final int boardSize = 8; 

    public GUIView() {
        
    }

    @Override
    public void initialise(IModel model, IController controller) {
        this.model = model;
        this.controller = controller;
        createWhiteFrame();
        createBlackFrame();
    }
    

    @Override
    public void refreshView() {
        int width = model.getBoardWidth();
        int height = model.getBoardHeight();
	    for ( int x = 0 ; x < width ; x++ ) {
		    for ( int y = 0 ; y < height ; y++ ) {
				 if (model.getBoardContents(x, y) == 1) {
				     placeChip(whiteBoardPanels, x, y, Color.WHITE); 
				     placeChip(blackBoardPanels, 7 - x,7 - y, Color.WHITE);
				 } else if(model.getBoardContents(x, y) == 2) {
				 	 placeChip(whiteBoardPanels, x, y, Color.BLACK);
				     placeChip(blackBoardPanels, 7 - x,7 - y, Color.BLACK);
				 }else if(model.getBoardContents(x, y) == 0) {
				 	 removeChip(whiteBoardPanels, x, y);
				     removeChip(blackBoardPanels, 7 - x,7 - y);
				 }
		    }
	    }   
    }
    

    @Override
    public void feedbackToUser(int player, String message) {
        if (player == 1) {
        	messageLabelWhite.setText(message);
        } else if (player == 2) {
            messageLabelBlack.setText(message);
        }
    }
    
    
    
    private void createWhiteFrame() {
    	
    	JFrame playerFrame = new JFrame("Reversi - White player");
    	messageLabelWhite = new JLabel();
    	whiteBoardPanels = new JPanel[boardSize][boardSize];
    	
    	board.board(1, messageLabelWhite, whiteBoardPanels, controller, playerFrame);

        JButton greedyAI = new JButton("Greedy AI (play white)");
        JButton restart = new JButton("Restart");
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 1));
        buttonPanel.add(greedyAI);
        buttonPanel.add(restart);

        greedyAI.addActionListener(e -> controller.doAutomatedMove(1));
        restart.addActionListener(e -> controller.startup());

        playerFrame.add(buttonPanel, BorderLayout.SOUTH);
        playerFrame.setVisible(true);
        
        messageLabelWhite.setHorizontalAlignment(SwingConstants.LEFT);
        messageLabelWhite.setOpaque(true);
        messageLabelWhite.setBackground(new Color(211, 211, 211));

        
    }
    
    
    
private void createBlackFrame() {
    	
    	JFrame playerFrame = new JFrame("Reversi - Black player");
    	messageLabelBlack = new JLabel();
    	blackBoardPanels = new JPanel[boardSize][boardSize];
    	
    	board.board(2, messageLabelBlack, blackBoardPanels, controller, playerFrame);

        JButton greedyAI = new JButton("Greedy AI (play black)");
        JButton restart = new JButton("Restart");
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 1));
        buttonPanel.add(greedyAI);
        buttonPanel.add(restart);

        greedyAI.addActionListener(e -> controller.doAutomatedMove(2));
        restart.addActionListener(e -> controller.startup());

        playerFrame.add(buttonPanel, BorderLayout.SOUTH);
        playerFrame.setVisible(true);
        
        messageLabelBlack.setHorizontalAlignment(SwingConstants.LEFT);
        messageLabelBlack.setOpaque(true);
        messageLabelBlack.setBackground(new Color(211, 211, 211));

        
    }


    private void placeChip(JPanel[][] boardPanels, int row, int col, Color color) {
        
        board.chip(boardPanels, row, col, color);
    }
    
    private void removeChip(JPanel[][] boardPanels, int row, int col) {
        
        board.rChip(boardPanels, row, col);
    }
    
    

    public static void main(String[] args) {
    }
}