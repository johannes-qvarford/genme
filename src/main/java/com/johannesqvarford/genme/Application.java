package com.johannesqvarford.genme;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.*;
import java.util.*;
import java.util.function.*;

public class Application {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
				JFrame frame = new JFrame("genme");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.getContentPane().setLayout(new GridLayout(0, 1));
				frame.setMinimumSize(new Dimension(300, 50));

				addButton(frame, "CPR", Application::generateCpr);
				addButton(frame, "Personnummer utan bindestreck", Application::generatePersonnummer);

				//Display the window.
				frame.pack();
				frame.setVisible(true);
            }
        });
	}

	private static void addButton(JFrame frame, String name, Supplier<String> generator)
	{
		JButton button = new JButton(name);
		frame.getContentPane().add(button);
		button.addActionListener(e -> {
			Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
			String s = generator.get();
			StringSelection selection = new StringSelection(s);
			cb.setContents(selection, selection);
		});
	}

	private static String generateCpr()
	{
		Random r = new Random();
		//int day = 1; int month = 1; int year = 00;
		int day = r.nextInt(28) + 1;
		int month = r.nextInt(12) + 1;
		int year = r.nextInt(100);
		// 4 and greater is 20XX.
		int centuryNumber = r.nextInt(4);
		int seq2 = r.nextInt(10);
		
		// 4 3 2 7 6 5 4 3 2 1
		int checksumWithoutLast2Digits =
			(((day / 10) * 4) +
			((day % 10) * 3) +
			((month / 10) * 2) +
			((month % 10) * 7) +
			((year / 10) * 6) +
			((year % 10) * 5) +
			(centuryNumber * 4) +
			(seq2 * 3)) % 11;
		
		int rest = 11 - checksumWithoutLast2Digits;
		int seq3 = rest / 2;
		int seq4 = rest % 2;

		String s = String.format("%d%d%d%d%d%d%d%d%d%d",
			day / 10, day % 10, month / 10, month % 10, year / 10, year % 10,
			centuryNumber, seq2, seq3, seq4);
		
		return s;
	}

	private static String generatePersonnummer()
	{
		Random r = new Random();
		//int day = 1; int month = 1; int year = 00;
		int day = r.nextInt(28) + 1;
		int month = r.nextInt(12) + 1;
		int year = r.nextInt(100);

		int seq1 = r.nextInt(10);
		int seq2 = r.nextInt(10);
		int seq3 = r.nextInt(10);

		int checksumWithoutSeq4 = 
			((year / 10) * 2) +
			((year % 10) * 1) +
			((month / 10) * 2) +
			((month % 10) * 1) +
			((day / 10) * 2) +
			((day % 10) * 1) +
			(seq1 * 2) +
			(seq2 * 1);

		int seq4 = (10 - (checksumWithoutSeq4 % 10)) % 10;

		String s = String.format("%d%d%d%d%d%d%d%d%d%d",
			year / 10, year % 10, month / 10, month % 10, day / 10, day % 10,
			seq1, seq2, seq3, seq4);
		
		return s;
	}
}
