package client.catan;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import shared.definitions.CatanColor;
import client.base.IAction;


@SuppressWarnings("serial")
public class GameStatePanel extends JPanel
{
	private JButton button;
	
	public GameStatePanel()
	{
		this.setLayout(new FlowLayout());
		this.setBackground(Color.white);
		this.setOpaque(true);
		
		button = new JButton();
		
		Font font = button.getFont();
		Font newFont = font.deriveFont(font.getStyle(), 20);
		button.setFont(newFont);
		
		button.setPreferredSize(new Dimension(400, 50));
		
		this.add(button);
		
		updateGameState("Waiting for other Players", false, null);
	}
	
	public void updateGameState(String stateMessage, boolean enable, CatanColor localColor)
	{
		System.out.println("catan color: " + localColor);
		if(localColor != null) {
			System.out.println("setting button background color???");
			button.setBackground(localColor.getJavaColor());
		}
		button.setText(stateMessage);
		button.setEnabled(enable);
	}
	
	public void setButtonAction(final IAction action)
	{
		ActionListener[] listeners = button.getActionListeners();
		for(ActionListener listener : listeners) {
			button.removeActionListener(listener);
		}
		
		ActionListener actionListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
//				System.out.println("end turn button pressed?");
				action.execute();
			}
		};
		button.addActionListener(actionListener);
	}
}

