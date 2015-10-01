package br.com.dbcorp.escolaMinisterio.ui.model;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellRenderer;

public class DesignacaoCellRender implements TableCellRenderer {

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		if (value != null) {
			char valor = (Character) value;
			
			String status = null;
			
			JTextField l = new JTextField();

			switch (valor) {
			case 'P':
				l.setBackground(Color.BLUE);
				status = "Passou em ";
				break;
			case 'F':
				l.setBackground(Color.RED);
				status = "Não passou em ";
				break;
			case 'A':
				l.setBackground(Color.GREEN);
				status = "A fazer em ";
				break;
			default:
				l.setBackground(Color.WHITE);
			}
			
			if (table.getModel() instanceof DesignacaoTableModel) {
				DesignacaoTableModel model = (DesignacaoTableModel) table.getModel();
				
				l.setToolTipText(status + model.getObservacao(column));
			}
			
			return l;
		}
		
		return null;
	}
}
