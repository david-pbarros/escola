package br.com.dbcorp.escolaMinisterio.ui;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JInternalFrame;

public abstract class InternalUI extends JInternalFrame {
	private static final long serialVersionUID = 6224423399781867732L;

	/**
	 * Create the frame.
	 */
	public InternalUI() {
		setFrameIcon(null);
		setVisible(true);
		setResizable(true);
		setClosable(true);
		setIconifiable(true);
		getContentPane().setLayout(new BorderLayout());
		
		this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent event) {
				super.componentResized(event);
				
				reset();
			}
			
		});
	}
	
	@Override
	public void setVisible(boolean arg0) {
		super.setVisible(arg0);
	}
	
	protected int getScreenWidth() {
		return Toolkit.getDefaultToolkit().getScreenSize().width;
	}
	
	protected int getScreenHeight() {
		return Toolkit.getDefaultToolkit().getScreenSize().height;
	}
	
	public abstract void reset();
}