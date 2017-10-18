/* FACULDADE COTEMIG
 * TRABALHO PR�TICO - COMPILADORES
 * IDE PARA COMPILADOR
 * REVIS�O: 2017.2.1
 * AUTOR: prof. VIRGILIO BORGES DE OLIVEIRA.
 * DATA DA �LTIMA ALTERA��O: 09/10/2017
 **/
 
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.FileReader;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;

public class IDE extends JFrame implements ActionListener {
    //array de tokens
    public String[] TOKENS  = {"TERMINADOR", "ID", "NUM_INTEIRO", "NUM_REAL", "OP_SOMA", "OP_SUBTRAI", "OP_MULTIPLICA",
            "OP_POTENCIA", "OP_DIVISAO", "OP_IGUAL", "OP_OR", "OP_AND", "ABRE_ARRAY", "FECHA_ARRAY", "SEPARADOR",
            "ATRIB", "ABRE_BLOCO", "FECHA_BLOCO", "OP_MAIOR", "OP_MAIOR_IGUAL", "OP_MENOR", "OP_MENOR_IGUAL",
            "ABRE_EXPR", "FECHA_EXPR", "MOD", "DIFERENTE", "NEGA", "CHAR" ,"STRING", "INPUT", "OUTPUT", "WHILE",
            "IF", "ELSE", "ELSEIF","EOF"
    };

	JTextPane editor = new JTextPane();
	JScrollPane p1 = new JScrollPane(editor);
	
	JTextPane msg = new JTextPane();
	JScrollPane p2 = new JScrollPane(msg); 

	JMenuBar mnBar = new JMenuBar();
	
	JMenu mnArquivo = new JMenu("Arquivo");
	JMenuItem mnNovo = new JMenuItem("Novo", KeyEvent.VK_N);
	JMenuItem mnAbrir = new JMenuItem("Abrir", KeyEvent.VK_A);
	JMenuItem mnSair = new JMenuItem("Sair", KeyEvent.VK_R);
	
	JMenu mnProjeto = new JMenu("Projeto");
	JMenuItem mnCompilar = new JMenuItem("Compilar", KeyEvent.VK_C);

	JMenu mnAjuda = new JMenu("Ajuda");
	JMenuItem mnSobre = new JMenuItem("Sobre...", KeyEvent.VK_S);
	
    public IDE() {
    	super("Compiladores - IDE versão 2017.2.1");
    	setLayout(null);
    	
    	mnBar.add(mnArquivo);
    	
    	mnArquivo.add(mnNovo);
    	mnNovo.addActionListener(this);
    	
    	mnArquivo.add(mnAbrir);
    	mnAbrir.addActionListener(this);
    	
    	mnArquivo.addSeparator();
    	
    	mnArquivo.add(mnSair);
    	mnSair.addActionListener(this);
    	
    	mnBar.add(mnProjeto);
    	
    	mnProjeto.add(mnCompilar);    	
    	mnCompilar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
    	mnCompilar.addActionListener(this);
    	
    	mnBar.add(mnAjuda);
    	
    	mnAjuda.add(mnSobre);
    	
    	mnSobre.addActionListener(this);
    	
    	setJMenuBar(mnBar);
    	
    	setSize(700, 550);
    	msg.setEditable(false);    	
    	getContentPane().add(p1);
    	getContentPane().add(p2);
    	setDefaultCloseOperation(EXIT_ON_CLOSE);
    	setResizable(false);
    	setVisible(true);    	
    	int larg = this.getContentPane().getWidth();
    	int alt = this.getContentPane().getHeight();
		editor.setFont(new Font("Monospaced", Font.PLAIN, 13));
		msg.setFont(new Font("Monospaced", Font.PLAIN, 13));
		msg.setBackground(Color.LIGHT_GRAY);
		msg.setText("Saída:");
    	p1.setBounds(1, 0, larg - 1, alt - 100);
    	p2.setBounds(1, alt - 100, larg - 1, 100);    	
    }
    
    public static void main(String[] args) {
    	new IDE();
    }
    
    public void actionPerformed(ActionEvent e) {
    	if(e.getSource() == mnNovo) {
    		editor.setText("");
    	}
    	else if(e.getSource() == mnAbrir) {
    		JFileChooser fc = new JFileChooser();
    		int returnVal = fc.showOpenDialog(this);    		
    	     if (returnVal == JFileChooser.APPROVE_OPTION) {
    	     	try {
	        		editor.setContentType("text/plain");
					editor.read(new BufferedReader(new FileReader(fc.getSelectedFile().getAbsoluteFile())), "");
				}
				catch(Exception ex) {
					JOptionPane.showMessageDialog(this, "Erro ao abrir arquivo.\n" + ex);
				}
			}   		
    	}
    	else if(e.getSource() == mnCompilar) {
    		Lexico lex = new Lexico(editor, msg);
            int token = lex.anaLex();
            msg.setText(msg.getText() + "\n Token identificado: " + TOKENS[token]);
            //descobrir porque o while esta dando loop infinito
            while (token != 35){
                token = lex.anaLex();
                msg.setText(msg.getText() + "\n Token identificado: " + TOKENS[token]);
            }

            msg.setText(msg.getText() + "\n Fim da execução");
    	}
    	else if(e.getSource() == mnSair) {
    		System.exit(0);    		
    	}
    	else if(e.getSource() == mnSobre) {
    		JOptionPane.showMessageDialog(this, "Trabalho de Compiladores" +
                    "\nIDE versão 2017.2.1\n" +
                    "\nDesenvolvido por: prof. Virgilio Borges de Oliveira" +
                    "\nModificado por: Arthur Mendonça Ribeiro" +
					"\nSomente para fins didáticos.");
    	}

    }
}
