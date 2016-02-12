package br.com.dbcorp.escolaMinisterio;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class IniTools {
	private static Path iniPath = Paths.get(Params.getAppPath() + File.separator + "escola.ini");
	private static Path oldPath = Paths.get(Params.getAppPath() + File.separator + "escola.old");
	
	
	public static boolean hasLine(String line) throws IOException {
		return Files.lines(iniPath).anyMatch(l->line.equalsIgnoreCase(l));
	}
	
	
	public static void apagarLinha(String lineToRemove) throws IOException {
		IniTools.criaEstrutura();
		
		List<String> linhas = new ArrayList<>();
		
		Files.lines(oldPath).filter(l->!lineToRemove.equalsIgnoreCase(l)).forEach(l->linhas.add(l));
		
		IniTools.gravaNovoConteudo(linhas);
		
		Files.delete(oldPath);
	}
	
	public static void modificarValor(String chave, String valor) throws IOException {
		IniTools.criaEstrutura();
		
		List<String> linhas = new ArrayList<>();
		
		Files.lines(oldPath).forEach(l->{
			if (l.startsWith(chave)) {
				l = chave + "=" + valor;
			}
			
			linhas.add(l);
		});
		
		IniTools.gravaNovoConteudo(linhas);
		
		Files.delete(oldPath);
	}
	
	public static String obterValor(String chave) throws IOException {
		String line = Files.lines(iniPath).filter(l->l.startsWith(chave)).findFirst().orElse("");
		
		return line.length() > 0 ? line.substring(line.indexOf("=") + 1) : line;
	}

	private static void criaEstrutura() throws IOException {
		File file = iniPath.toFile();
		file.renameTo(oldPath.toFile());
		
		Files.createFile(iniPath);
	}
	
	private static void gravaNovoConteudo(List<String> linhas) throws IOException {
		BufferedWriter bw = Files.newBufferedWriter(iniPath, StandardOpenOption.APPEND);
		
		for (String linha : linhas) {
			bw.write(linha);
			bw.newLine();
		}
		
		bw.flush();
	}
}
