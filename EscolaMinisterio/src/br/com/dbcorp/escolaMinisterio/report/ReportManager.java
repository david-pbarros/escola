package br.com.dbcorp.escolaMinisterio.report;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import br.com.dbcorp.escolaMinisterio.Log;

public class ReportManager {
	
	private JasperPrint print;
	
	private Log log = Log.getInstance();
	
	public void createReport(String fileName, Properties parms, JRDataSource datasource) {
		try {
			InputStream input = ReportManager.class.getClassLoader().getResourceAsStream("report/" +  fileName + ".jrxml");
			
			if (input == null) {
				input = ReportManager.class.getClassLoader().getResourceAsStream("resources/report/" +  fileName + ".jrxml");
			}
			
			if (input == null) {
				input = ReportManager.class.getClassLoader().getResourceAsStream("/report/" +  fileName + ".jrxml");
			}
			
			if (input == null) {
				input = ReportManager.class.getClassLoader().getResourceAsStream("/resources/report/" +  fileName + ".jrxml");
			}
			
			JasperReport report = JasperCompileManager.compileReport(input);
			
			if (datasource != null) {
				this.print = JasperFillManager.fillReport(report, this.processaParametros(parms), datasource);
				
			} else {
				this.print = JasperFillManager.fillReport(report, this.processaParametros(parms), new JREmptyDataSource());
			}
		} catch (JRException e) {
			String erro = "Não pode compilar arquivo de relatorio!";
			
			this.log.error(erro, e);
			throw new RuntimeException(erro);
		}
	}
	
	public InputStream toPDF() {
		try {
			return new ByteArrayInputStream(JasperExportManager.exportReportToPdf(this.print));
			
		} catch (Exception e) {
			this.log.error("Não gerar PDF do relatório", e);
			
			throw new RuntimeException("Não gerar PDF do relatório");
		}
	}
	
	/*public TableModel criarTabela(String[] columns, List<Object[]> dados) {
		return  new TableModel().setColumnNames( columns ).setObjectData( dados );
	}*/
	
	private Map<String, Object> processaParametros(Properties prop) {
		Map<String, Object> parametros = new HashMap<String, Object>();
		
		for (Object key : prop.keySet()) {
			parametros.put((String)key, prop.get(key));
		}
		
		return parametros;		
	}
}
