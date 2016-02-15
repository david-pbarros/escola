package br.com.dbcorp.escolaMinisterio.ui;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import br.com.dbcorp.escolaMinisterio.action.Action;
import br.com.dbcorp.escolaMinisterio.action.AjudanteAction;
import br.com.dbcorp.escolaMinisterio.action.AvaliacaoAction;
import br.com.dbcorp.escolaMinisterio.action.ConfigAction;
import br.com.dbcorp.escolaMinisterio.action.ContatoAction;
import br.com.dbcorp.escolaMinisterio.action.EstudanteAction;
import br.com.dbcorp.escolaMinisterio.action.EstudoAction;
import br.com.dbcorp.escolaMinisterio.action.HistoricoDesignacaoAction;
import br.com.dbcorp.escolaMinisterio.action.ImportEstudantesAction;
import br.com.dbcorp.escolaMinisterio.action.ImportEstudosAction;
import br.com.dbcorp.escolaMinisterio.action.NovaDesignacaoAction;
import br.com.dbcorp.escolaMinisterio.action.ProfileAction;
import br.com.dbcorp.escolaMinisterio.action.SincronismoAction;
import br.com.dbcorp.escolaMinisterio.action.UsuarioAction;
import br.com.dbcorp.escolaMinisterio.dataBase.Gerenciador;
import br.com.dbcorp.escolaMinisterio.entidades.ItemProfile;
import br.com.dbcorp.escolaMinisterio.entidades.ItemProfile.ItensSeg;
import br.com.dbcorp.escolaMinisterio.ui.designacao.inicial.DesignacoesUI;
import br.com.dbcorp.escolaMinisterio.ui.dialog.SincDialog;

public class MainFrame extends JFrame {
	private static final long serialVersionUID = 8238298330191746759L;

	private JDesktopPane desktop;
	private Gerenciador gerenciador;
	
	/**
	 * Create the frame.
	 */
	public MainFrame(int width, int height, Splash splash) {
		this.gerenciador = new Gerenciador();
		
		this.desktop = new JDesktopPane();
		desktop.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);

		this.setMenu();
		getContentPane().add(desktop);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(width, height);
		setLocationRelativeTo(null);
		setTitle("Escola do Ministério Teocrático");
		setIconImage(Params.iconeAplicacao());
		setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);
		
		InternalUI inicial = new DesignacoesUI();
		inicial.show();
		inicial.grabFocus();
		((javax.swing.plaf.basic.BasicInternalFrameUI) inicial.getUI()).setNorthPane(null);
		
		
		this.getDesktop().add(inicial);
		try {
			inicial.setMaximum(true);
			inicial.setSelected(true);
		} catch (Exception ex) {
		}
		
		this.addComponentListener(new ComponentAdapter() {
			MainFrame frame;
			
			private ComponentAdapter init(MainFrame frame){
				this.frame = frame;
		        return this;
		    }
			
			@Override
			public void componentResized(ComponentEvent event) {
				super.componentResized(event);
				Params.setDimensions(this.frame);
			}
			
			
		}.init(this));
		
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if (Params.isOnLineMode() && (boolean)br.com.dbcorp.escolaMinisterio.Params.propriedades().get("doSinc")) {
					int response = JOptionPane.showConfirmDialog(null, "Recomendamos um sincronismo com a WEB. Deseja Realizar?", "Sincronizar?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					if (response == JOptionPane.YES_OPTION) {
						new SincDialog(SincDialog.GERAL).setVisible(true);
					}
				}
			    
			    System.exit(0);
			}
		});
		
		splash.setVisible(false);
	}
	
	@Override
	public void setVisible(boolean arg0) {
		super.setVisible(arg0);
		Params.setDimensions(this);
	}
	
	public JDesktopPane getDesktop() {
		return this.desktop;
	}
	
	private void setMenu() {
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnProgramacao = new JMenu("Programa");
		JMenu mnCadastros = new JMenu("Cadastros");
		JMenu mnOpcoes = new JMenu("Opções");
		
		if (exibe(this.gerenciador.getUser().getProfile().getItens(), ItensSeg.MENU_PROGRAMA)) {
			this.setMenuItem(mnProgramacao, this.itensPrograma());
			menuBar.add(mnProgramacao);
		}
		
		if (exibe(this.gerenciador.getUser().getProfile().getItens(), ItensSeg.MENU_CADASTRO)) {
			this.setMenuItem(mnCadastros, itensCadastro());
			
			if (this.exibe(this.gerenciador.getUser().getProfile().getItens(), ItensSeg.ITM_IMPORTAR)) {
				JMenu mnImport = new JMenu("Importar");
				mnCadastros.add(mnImport);
				this.setMenuItem(mnImport, new Action[]{new ImportEstudantesAction(this), new ImportEstudosAction(this)});
			}
			
			if (this.exibe(this.gerenciador.getUser().getProfile().getItens(), ItensSeg.ITM_SEGURANCA)) {
				JMenu mnSeguranca = new JMenu("Segurança");
				mnCadastros.add(mnSeguranca);
				this.setMenuItem(mnSeguranca, new Action[]{new UsuarioAction(this), new ProfileAction(this)});
			}
			
			menuBar.add(mnCadastros);
		}
		
		this.setMenuItem(mnOpcoes, this.itensOpcao());
		
		menuBar.add(mnOpcoes);
	}
	
	private void setMenuItem(JMenu menu, Action[] actions) {
		for (Action action : actions) {
			menu.add(new JMenuItem(action));
		}
	}
	
	private boolean exibe(List<ItemProfile> itens, ItensSeg item) {
		ItemProfile itemP = new ItemProfile(item);
		
		return itens.contains(itemP);
	}
	
	private Action[] itensOpcao() {
		List<Action> itens = new ArrayList<Action>();
		
		itens.add(new SincronismoAction(this));
		
		itens.add(new ConfigAction(this));
		
		itens.add(new ContatoAction(this));
		
		return itens.toArray(new Action[0]);
	}
	
	private Action[] itensPrograma() {
		List<ItemProfile> itensProf = this.gerenciador.getUser().getProfile().getItens();
		
		List<Action> itens = new ArrayList<Action>();
		
		if (this.exibe(itensProf, ItensSeg.ITM_NOVA)) {
			itens.add(new NovaDesignacaoAction(this));
		}
		
		if (this.exibe(itensProf, ItensSeg.ITM_AVALIA)) {
			itens.add(new AvaliacaoAction(this));
		}
		if (this.exibe(itensProf, ItensSeg.ITM_HISTORICO)) {
			itens.add(new HistoricoDesignacaoAction(this));
		}
		
		return itens.toArray(new Action[0]);
	}
	
	private Action[] itensCadastro() {
		List<ItemProfile> itensProf = this.gerenciador.getUser().getProfile().getItens();
		
		List<Action> itens = new ArrayList<Action>();
		
		if (this.exibe(itensProf, ItensSeg.ITM_ESTUDANTES)) {
			itens.add(new EstudanteAction(this));
		}
		
		if (this.exibe(itensProf, ItensSeg.ITM_AJUDANTES)) {
			itens.add( new AjudanteAction(this));
		}
		if (this.exibe(itensProf, ItensSeg.ITM_ESTUDO)) {
			itens.add(new EstudoAction(this));
		}
		
		return itens.toArray(new Action[0]);
	}
	
	
}
