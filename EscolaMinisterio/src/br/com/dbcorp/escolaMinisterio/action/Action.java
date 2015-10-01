package br.com.dbcorp.escolaMinisterio.action;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.beans.PropertyVetoException;

import javax.swing.AbstractAction;
import javax.swing.JInternalFrame;

import br.com.dbcorp.escolaMinisterio.Log;
import br.com.dbcorp.escolaMinisterio.ui.InternalUI;
import br.com.dbcorp.escolaMinisterio.ui.MainFrame;

public abstract class Action extends AbstractAction {
	private static final long serialVersionUID = 7045015726193193251L;
	
	private Log log = Log.getInstance();
	
	@SuppressWarnings("rawtypes")
	Class internalFrameClass;
	MainFrame mainFrame;
	InternalUI internalFrame;
	
	public Action(String name, MainFrame mainFrame) {
		super(name);
		this.mainFrame = mainFrame;
	}
	
	public void actionPerformed(ActionEvent arg0) {
		this.mainFrame.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		
		try {
			if (this.isNaoAberta()) {
				this.internalFrame = (InternalUI) this.internalFrameClass.newInstance();
				this.addInternalFrame(internalFrame);

				this.internalFrame.show();
				this.internalFrame.grabFocus();
				this.internalFrame.setMaximum(true);
				((javax.swing.plaf.basic.BasicInternalFrameUI) this.internalFrame.getUI()).setNorthPane(null);
			}
		}catch (Exception exception) {
			this.log.error("", exception);
		}
		
		this.mainFrame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}
	
	protected boolean isNaoAberta() {
		JInternalFrame[] frames = this.mainFrame.getDesktop().getAllFrames();
		if (frames != null && frames.length > 0) {
			for (int i = 0; i < frames.length; i++) {
				Object objeto = frames[i];
				if (this.internalFrameClass.isInstance(objeto)) {
					this.internalFrame = (InternalUI) frames[i];
					try {
						this.internalFrame.setSelected(true);
						
						if (this.internalFrame.isIcon()) {
							this.mainFrame.getDesktop().getDesktopManager().deiconifyFrame(internalFrame); 
						}
						
						this.internalFrame.moveToFront();
					} catch (PropertyVetoException e) {
					}
					return false;
				}
			}
		}
		return true;
	}
	
	private void addInternalFrame(JInternalFrame f) {
		this.mainFrame.getDesktop().add(f);
		try {
			f.setSelected(true);
		} catch (Exception ex) {
		}
	}
}
