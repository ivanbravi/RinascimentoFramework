package gui;

import game.action.Action;
import game.action.active.buy.board.BuyBoardCard;
import game.action.active.buy.reserved.BuyReservedCard;
import game.action.active.pick.different.PickDifferentCoins;
import game.action.active.pick.same.PickSameCoins;
import game.action.active.reserve.board.ReserveBoardCard;
import game.action.active.reserve.deck.ReserveDeckCard;
import game.state.Result;
import game.state.State;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;

public class UIPlayerInput {

	private volatile Queue<Action> q = new ArrayDeque<>();
	private JFrame frame;
	private RinascimentoUI mainUI;
	private int playerId;

	public UIPlayerInput(RinascimentoUI mainUI){
		this.mainUI = mainUI;

		frame= new JFrame();
		frame.setTitle("Player Input");
		frame.setSize(600, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);

		showActions();
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

	private void showBuyCard(){
		frame.getContentPane().removeAll();
		frame.revalidate();
		frame.repaint();

		frame.setTitle("Buy Card");
		frame.setLayout(new BorderLayout());
		JPanel grid = new JPanel();
		grid.setLayout(new GridLayout(3,4));

		State s = mainUI.getState();

		for(int i=0; i<3; i++){
			for(int j=0; j<4; j++) {
				JButton b = new JButton((i+1)+","+(j+1));
				int finalI = i;
				int finalJ = j;
				b.addActionListener(e -> {
					Action a = new BuyBoardCard(playerId, finalI, finalJ);
					checkAction(a);
				});

				grid.add(b);
			}
		}

		frame.add(grid, BorderLayout.CENTER);

		JButton back = new JButton("back");
		back.addActionListener(e -> showActions());
		frame.add(back,BorderLayout.PAGE_END);

		frame.revalidate();
		frame.repaint();
		frame.setVisible(true);
	}

	private void showBuyReservedCard(){
		frame.getContentPane().removeAll();
		frame.revalidate();
		frame.repaint();

		frame.setTitle("Buy Reserved Card");
		frame.setLayout(new BorderLayout());
		JPanel grid = new JPanel();
		grid.setLayout(new GridLayout(1,3));

		State s = mainUI.getState();

		for(int i=0; i<3; i++){
			JButton b = new JButton("Card "+String.valueOf(i+1));
			int finalI = i;
			b.addActionListener(e -> {
				Action a = new BuyReservedCard(playerId,finalI);
				checkAction(a);
			});

			grid.add(b);
		}

		frame.add(grid, BorderLayout.CENTER);

		JButton back = new JButton("back");
		back.addActionListener(e -> showActions());
		frame.add(back,BorderLayout.PAGE_END);

		frame.revalidate();
		frame.repaint();
		frame.setVisible(true);
	}
	private void showReserveCard(){
		frame.getContentPane().removeAll();
		frame.revalidate();
		frame.repaint();

		frame.setTitle("Reserve Card");
		frame.setLayout(new BorderLayout());
		JPanel grid = new JPanel();
		grid.setLayout(new GridLayout(3,4));
		State s = mainUI.getState();

		for(int i=0; i<3; i++){
			for(int j=0; j<4; j++) {
				JButton b = new JButton((i+1)+","+(j+1));
				int finalI = i;
				int finalJ = j;
				b.addActionListener(e -> {
					Action a = new ReserveBoardCard(playerId, finalI, finalJ);
					checkAction(a);
				});

				grid.add(b);
			}
		}

		frame.add(grid,BorderLayout.CENTER);

		JButton back = new JButton("back");
		back.addActionListener(e -> showActions());
		frame.add(back,BorderLayout.PAGE_END);

		frame.revalidate();
		frame.repaint();
		frame.setVisible(true);
	}
	private void showReserveDeckCard(){
		frame.getContentPane().removeAll();
		frame.revalidate();
		frame.repaint();

		frame.setTitle("Reserve Deck Card");
		frame.setLayout(new BorderLayout());
		JPanel grid = new JPanel();
		grid.setLayout(new GridLayout(1,3));

		State s = mainUI.getState();

		for(int i=0; i<3; i++){
			JButton b = new JButton("Deck "+String.valueOf(i+1));
			int finalI = i;
			b.addActionListener(e -> {
				Action a = new ReserveDeckCard(playerId,finalI);
				checkAction(a);
			});

			grid.add(b);
		}

		frame.add(grid,BorderLayout.CENTER);

		JButton back = new JButton("back");
		back.addActionListener(e -> showActions());
		frame.add(back,BorderLayout.PAGE_END);

		frame.revalidate();
		frame.repaint();
		frame.setVisible(true);
	}
	private void showPickDifferent(){
		int coins = 5;
		int elements = coins+2;
		frame.getContentPane().removeAll();
		frame.revalidate();
		frame.repaint();

		frame.setTitle("Pick 3 different coins");
		frame.setLayout(new GridLayout(1,elements));
		Font valueFont = new Font("SansSerif", Font.BOLD, 20);

		State s = mainUI.getState();

		ArrayList<JTextField> values = new ArrayList<>();

		for(int i=0; i<5; i++){
			JPanel block = new JPanel();
			JButton plus = new JButton("+");
			JButton minus = new JButton("-");
			JTextField value = new JTextField("0");

			plus.addActionListener(e -> {
				int v = Integer.parseInt(value.getText());
				value.setText(String.valueOf(v+1));
				value.repaint();
			});

			minus.addActionListener(e -> {
				int v = Integer.parseInt(value.getText());
				value.setText(String.valueOf(v-1));
				value.repaint();
			});

			value.setHorizontalAlignment(JTextField.CENTER);
			value.setFont(valueFont);
			value.setForeground(new Color(UIStandards.suitColor(i)));
			value.setBackground(Color.gray);

			values.add(value);

			block.setLayout(new GridLayout(3,1));
			block.add(plus);
			block.add(value);
			block.add(minus);
			frame.add(block);
		}

		JButton go = new JButton("Play!");
		go.addActionListener(e -> {
			int[] vs = new int[coins];

			for(int i=0; i<vs.length; i++){
				vs[i] = Integer.parseInt(values.get(i).getText());
			}
			Action a = new PickDifferentCoins(playerId,vs);
			checkAction(a);
		});
		frame.add(go);

		JButton back = new JButton("back");
		back.addActionListener(e -> showActions());
		frame.add(back);

		frame.revalidate();
		frame.repaint();
		frame.setVisible(true);
	}

	private void showPickSame(){
		int coins = 5;
		int elements = coins+2;
		frame.getContentPane().removeAll();
		frame.revalidate();
		frame.repaint();

		frame.setTitle("Pick 2 coins of same type");
		frame.setLayout(new GridLayout(1,elements));

		State s = mainUI.getState();
		Font valueFont = new Font("SansSerif", Font.BOLD, 20);

		ArrayList<JTextField> values = new ArrayList<>();

		for(int i=0; i<5; i++){
			JPanel block = new JPanel();
			JButton plus = new JButton("+");
			JButton minus = new JButton("-");
			JTextField value = new JTextField("0");

			plus.addActionListener(e -> {
				int v = Integer.parseInt(value.getText());
				value.setText(String.valueOf(v+1));
				value.repaint();
			});

			minus.addActionListener(e -> {
				int v = Integer.parseInt(value.getText());
				value.setText(String.valueOf(v-1));
				value.repaint();
			});

			value.setHorizontalAlignment(JTextField.CENTER);
			value.setForeground(new Color(UIStandards.suitColor(i)));
			value.setBackground(Color.gray);
			value.setFont(valueFont);

			values.add(value);

			block.setLayout(new GridLayout(3,1));
			block.add(plus);
			block.add(value);
			block.add(minus);
			frame.add(block);
		}

		JButton go = new JButton("Play!");
		go.addActionListener(e -> {
			int[] vs = new int[coins];

			for(int i=0; i<vs.length; i++){
				vs[i] = Integer.parseInt(values.get(i).getText());
			}
			Action a = new PickSameCoins(playerId,vs);
			checkAction(a);
		});
		frame.add(go);

		JButton back = new JButton("back");
		back.addActionListener(e -> showActions());
		frame.add(back);

		frame.revalidate();
		frame.repaint();
		frame.setVisible(true);
	}

	private void error(String s){
		JOptionPane.showMessageDialog(new JFrame(),s,"Error",JOptionPane.ERROR_MESSAGE);
	}

	private void checkAction(Action a){
		if(a.canPerform(mainUI.getState())){
			q.add(a);
			showActions();
		}else {
			error("Illegal Action!");
		}
	}

	private void showActions(){
		frame.getContentPane().removeAll();
		frame.revalidate();
		frame.repaint();

		JButton buycard, buyreserved, reservecard,reservedeck, pickdifferent,picksame;
		buycard = new JButton("buy card");
		buyreserved = new JButton("buy reserved card");
		reservecard = new JButton("reserve card");
		reservedeck = new JButton("reserve deck card");
		pickdifferent = new JButton("pick 3 different tokens");
		picksame = new JButton("pick 2 same tokens");

		buycard.addActionListener(e -> showBuyCard());
		buyreserved.addActionListener(e -> showBuyReservedCard());
		reservecard.addActionListener(e -> showReserveCard());
		reservedeck.addActionListener(e -> showReserveDeckCard());
		pickdifferent.addActionListener(e -> showPickDifferent());
		picksame.addActionListener(e -> showPickSame());

		frame.add(buycard);
		frame.add(reservecard);
		frame.add(pickdifferent);
		frame.add(buyreserved);
		frame.add(reservedeck);
		frame.add(picksame);
		frame.setLayout(new GridLayout(2,3));

		frame.revalidate();
		frame.repaint();
		frame.setVisible(true);
		if(mainUI.getState()!=null) {
			if (mainUI.getState().isGameOver()) {
				String d = "You ";
				Result r = new Result(mainUI.getState());
				if(r.position[1]==1){
					d = d+" WON!";
				}else{
					d = d+" LOST!";
				}
				JOptionPane.showMessageDialog(new JFrame(), d, "Game Over", JOptionPane.INFORMATION_MESSAGE);
				System.exit(0);
			}
		}

	}

	public Action dequeueAction(){
		return q.poll();
	}


}
