package br.com.dbcorp.escolaMinisterio.ui;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class NumberVerify extends InputVerifier {

	public enum Tipo {
		INTEGER, LONG, FLOAT, DOUBLE;
	}
	
	private Tipo tipo;
	
	public NumberVerify(Tipo tipo) {
		this.tipo = tipo;
	}
	
	@Override
	public boolean verify(JComponent input) {
		JTextField textField = (JTextField) input;
		
		if ( !textField.getText().equals("")) {
			try {
				switch (tipo) {
				case INTEGER:
					Integer.parseInt(textField.getText());
					return true;
				case LONG:
					Long.parseLong(textField.getText());
					return true;
				case FLOAT:
					Float.parseFloat(textField.getText());
					return true;
				case DOUBLE:
					Double.parseDouble(textField.getText());
					return true;
				}
			} catch (Exception exception) {
				JOptionPane.showMessageDialog(input, "Valor informado no campo está incorreto, deverá ser um tipo numérico.", "Valor incorreto!", JOptionPane.WARNING_MESSAGE);
				return false;
			}
		} else {
			return true;
		}
		
		return false;
	}
}