import modelo.Produto;
import modelo.dao.ProdutoDao;
import org.h2.tools.Server;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class Tela extends JFrame {
    private JTextField txtProduto;
    private JTextField txtPreco;
    private JTextField txtQuantidade;
    private JButton btnSalvar;
    private JButton btnDeletar;
    private JButton btnAlterar;
    private JButton btnListar;
    private JButton btnLimparCampos;
    private JTable tabelaProduto;
    private DefaultTableModel modeloTabela;
    private JScrollPane scrollPane;

    private int produtoSelecionadoId = -1;
    private ProdutoDao produtoDao;

    public Tela() {
        try {
            produtoDao = new ProdutoDao();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao conectar com o banco de dados: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        initComponents();
        setupLayout();
        setupEvents();
        carregarDados();
    }

    private void initComponents() {
        setTitle("Sistema de Gerenciamento de Produtos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setResizable(true);

        txtProduto = new JTextField(20);
        txtPreco = new JTextField(10);
        txtQuantidade = new JTextField(10);

        btnSalvar = new JButton("Salvar");
        btnDeletar = new JButton("Deletar");
        btnAlterar = new JButton("Alterar");
        btnListar = new JButton("Listar");
        btnLimparCampos = new JButton("Limpar Campos");

        String[] colunas = {"ID", "Nome do Produto", "Preço", "Quantidade em Estoque"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelaProduto = new JTable(modeloTabela);
        tabelaProduto.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scrollPane = new JScrollPane(tabelaProduto);
        scrollPane.setPreferredSize(new Dimension(750, 300));

        styleComponents();
    }

    private void styleComponents() {
        Color corPrimaria = new Color(52, 152, 219);
        Font fonteLabel = new Font("Arial", Font.BOLD, 12);
        Font fonteCampo = new Font("Arial", Font.PLAIN, 12);

        JButton[] botoes = {btnSalvar, btnDeletar, btnAlterar, btnListar, btnLimparCampos};
        Color[] coresBotoes = {
                new Color(46, 204, 113),
                new Color(231, 76, 60),
                new Color(241, 196, 15),
                new Color(52, 152, 219),
                new Color(119, 83, 12)
        };

        for (int i = 0; i < botoes.length; i++) {
            botoes[i].setBackground(coresBotoes[i]);
            botoes[i].setForeground(Color.WHITE);
            botoes[i].setFont(new Font("Arial", Font.BOLD, 12));
            botoes[i].setFocusPainted(false);
            botoes[i].setBorderPainted(false);
            botoes[i].setPreferredSize(new Dimension(100, 35));
        }

        txtProduto.setFont(fonteCampo);
        txtPreco.setFont(fonteCampo);
        txtQuantidade.setFont(fonteCampo);

        tabelaProduto.setFont(fonteCampo);
        tabelaProduto.getTableHeader().setFont(fonteLabel);
        tabelaProduto.getTableHeader().setBackground(corPrimaria);
        tabelaProduto.getTableHeader().setForeground(Color.WHITE);
        tabelaProduto.setRowHeight(25);
        tabelaProduto.setGridColor(new Color(189, 195, 199));
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        JPanel painelFormulario = new JPanel(new GridBagLayout());
        painelFormulario.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Cadastro de Produtos"));
        painelFormulario.setBackground(new Color(236, 240, 241));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        painelFormulario.add(new JLabel("Nome do Produto:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        painelFormulario.add(txtProduto, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1; gbc.fill = GridBagConstraints.NONE;
        painelFormulario.add(new JLabel("Preço (R$):"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        painelFormulario.add(txtPreco, gbc);

        gbc.gridx = 2; gbc.gridy = 1;
        painelFormulario.add(new JLabel("Quantidade:"), gbc);
        gbc.gridx = 3;
        painelFormulario.add(txtQuantidade, gbc);

        JPanel painelBotoes = new JPanel(new FlowLayout());
        painelBotoes.setBackground(new Color(236, 240, 241));
        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnAlterar);
        painelBotoes.add(btnDeletar);
        painelBotoes.add(btnListar);
        painelBotoes.add(btnLimparCampos);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 4;
        painelFormulario.add(painelBotoes, gbc);

        JPanel painelTabela = new JPanel(new BorderLayout());
        painelTabela.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Lista de Produtos"));
        painelTabela.add(scrollPane, BorderLayout.CENTER);

        add(painelFormulario, BorderLayout.NORTH);
        add(painelTabela, BorderLayout.CENTER);
    }

    private void setupEvents() {
        btnSalvar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                salvarProduto();
            }
        });

        btnAlterar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                alterarProduto();
            }
        });

        btnDeletar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deletarProduto();
            }
        });

        btnListar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                carregarDados();
            }
        });
        btnLimparCampos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limparCampos();
            }
        });

        tabelaProduto.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int linhaSelecionada = tabelaProduto.getSelectedRow();
                if (linhaSelecionada >= 0) {
                    preencherCampos();
                }
            }
        });
    }

    private void salvarProduto() {
        if (!validarCampos()) return;

        try {
            Produto produto = new Produto();
            produto.setTitulo(txtProduto.getText());
            produto.setPreco(Double.parseDouble(txtPreco.getText().replace(",", ".")));
            produto.setQuantidade(Integer.parseInt(txtQuantidade.getText()));

            produtoDao.salvar(produto);

            JOptionPane.showMessageDialog(this, "Produto salvo com sucesso!");
            limparCampos();


        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Verifique os valores numéricos inseridos!",
                    "Erro de Formato", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar produto: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void alterarProduto() {

        if (produtoSelecionadoId == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um produto na tabela para alterar!");
            return;
        }
        if (!validarCampos()) return;

        try {
            Produto produto = new Produto();
            produto.setId(produtoSelecionadoId);
            produto.setTitulo(txtProduto.getText().trim());
            produto.setPreco(Double.parseDouble(txtPreco.getText().replace(",", ".")));
            produto.setQuantidade(Integer.parseInt(txtQuantidade.getText()));

            produtoDao.alterar(produtoSelecionadoId,produto);
            carregarDados();
            JOptionPane.showMessageDialog(this, "Produto alterado com sucesso!");
            limparCampos();
            produtoSelecionadoId = -1;

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Verifique os valores numéricos inseridos!",
                    "Erro de Formato", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void deletarProduto() {
        if (produtoSelecionadoId == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um produto na tabela para deletar!");
            return;
        }

        int confirmacao = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja deletar este produto?",
                "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);

        if (confirmacao == JOptionPane.YES_OPTION) {
            try {
                produtoDao.deletar(produtoSelecionadoId);

                JOptionPane.showMessageDialog(this, "Produto deletado com sucesso!");
                limparCampos();
                carregarDados();
                produtoSelecionadoId = -1;

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao deletar produto: " + ex.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void carregarDados() {
        try {
            List<Produto> produtos = produtoDao.listar();

            modeloTabela.setRowCount(0);

            NumberFormat formatoMoeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

            for (Produto produto : produtos) {
                Object[] linha = {
                        produto.getId(),
                        produto.getTitulo(),
                        formatoMoeda.format(produto.getPreco()),
                        produto.getQuantidade()
                };
                modeloTabela.addRow(linha);
            }
            preencherCampos();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar dados: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }

    }

    private void preencherCampos() {
        try {
            int linha = tabelaProduto.getSelectedRow();
            produtoSelecionadoId = (Integer) modeloTabela.getValueAt(linha, 0);
            txtProduto.setText((String) modeloTabela.getValueAt(linha, 1));


            String valor = modeloTabela.getValueAt(linha, 2).toString();
            valor = valor.replace("R$", "").trim().replace(".", "").replace(",", ".");
            txtPreco.setText(valor);

            txtQuantidade.setText(modeloTabela.getValueAt(linha, 3).toString());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Por favor Atenção ao preencher os campos ");
        }
    }

    private boolean validarCampos() {
        if (txtProduto.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nome do produto é obrigatório!");
            txtProduto.requestFocus();
            return false;
        }

        if (txtPreco.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preço é obrigatório!");
            txtPreco.requestFocus();
            return false;
        }

        if (txtQuantidade.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Quantidade é obrigatória!");
            txtQuantidade.requestFocus();
            return false;
        }

        try {
            Double.parseDouble(txtPreco.getText().replace(",", "."));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Preço deve ser um valor numérico válido!");
            txtPreco.requestFocus();
            return false;
        }

        try {
            Integer.parseInt(txtQuantidade.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Quantidade deve ser um número inteiro!");
            txtQuantidade.requestFocus();
            return false;
        }

        return true;
    }

    private void limparCampos() {
        txtProduto.setText("");
        txtPreco.setText("");
        txtQuantidade.setText("");
        produtoSelecionadoId = -1;
        tabelaProduto.clearSelection();
    }

    public static void main(String[] args) {
        try {
            Server.createWebServer("-web", "-webAllowOthers", "-webPort", "4042").start();
            System.out.println("Console H2 acessível em: http://localhost:4042");
        } catch (SQLException e) {
            System.err.println("Erro ao inicializar servidor H2: " + e.getMessage());
        }

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            Tela tela = new Tela();
            tela.setVisible(true);
        });
    }
}