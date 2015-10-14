package br.com.dbcorp.escolaMinisterio.ui;

import java.awt.Component;

import javax.swing.JScrollPane;

public class DScrollPane extends JScrollPane {
	private static final long serialVersionUID = 4654783093865746198L;

	public DScrollPane() {
		super();
		this.getVerticalScrollBar().setUnitIncrement(30);
	}
	
	public DScrollPane(Component component) {
		super(component);
		this.getVerticalScrollBar().setUnitIncrement(30);
	}
}
