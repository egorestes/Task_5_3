package com.company.tree.demo;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.company.tree.BinaryTreeAlgorithms;
import com.company.tree.SimpleBinaryTree;
import com.company.tree.BinaryTreePainter;
import com.company.util.SwingUtils;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;

public class TreeDemoFrame extends JFrame {
    private JPanel panelMain;
    private JButton buttonPreOrderTraverse;
    private JButton buttonInOrderTraverse;
    private JButton buttonPostOrderTraverse;
    private JButton buttonByLevelTraverse;
    private JTextArea textAreaSystemOut;
    private JTextField textFieldBracketNotationTree;
    private JButton buttonMakeTree;
    private JSplitPane splitPaneMain;
    private JPanel panelPaintArea;
    private JButton buttonSaveImage;
    private JButton buttonToBracketNotation;
    private JCheckBox checkBoxTransparent;
    private JButton buttonDeleteTopLeaves;

    private JPanel paintPanel = null;
    private JFileChooser fileChooserSave;

    SimpleBinaryTree<Integer> tree = new SimpleBinaryTree<>();

    public TreeDemoFrame() {
        this.setTitle("Двоичные деревья");
        this.setContentPane(panelMain);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();

        splitPaneMain.setDividerLocation(0.5);
        splitPaneMain.setResizeWeight(1.0);
        splitPaneMain.setBorder(null);

        paintPanel = new JPanel() {
            private Dimension paintSize = new Dimension(0, 0);

            @Override
            public void paintComponent(Graphics gr) {
                super.paintComponent(gr);
                paintSize = BinaryTreePainter.paint(tree, gr);
                if (!paintSize.equals(this.getPreferredSize())) {
                    SwingUtils.setFixedSize(this, paintSize.width, paintSize.height);
                }
            }
        };
        JScrollPane paintJScrollPane = new JScrollPane(paintPanel);
        panelPaintArea.add(paintJScrollPane);

        fileChooserSave = new JFileChooser();
        fileChooserSave.setCurrentDirectory(new File("./images"));
        FileFilter filter = new FileNameExtensionFilter("SVG images", "svg");
        fileChooserSave.addChoosableFileFilter(filter);
        fileChooserSave.setAcceptAllFileFilterUsed(false);
        fileChooserSave.setDialogType(JFileChooser.SAVE_DIALOG);
        fileChooserSave.setApproveButtonText("Save");

        buttonMakeTree.addActionListener(actionEvent -> {
            try {
                SimpleBinaryTree<Integer> tree = new SimpleBinaryTree<>(Integer::parseInt);
                tree.fromBracketNotation(textFieldBracketNotationTree.getText());
                this.tree = tree;
                repaintTree();
            } catch (Exception ex) {
                SwingUtils.showErrorMessageBox(ex);
            }
        });

        buttonDeleteTopLeaves.addActionListener(actionEvent -> {
            tree.removeTopLeaves();
            buttonToBracketNotation.doClick();
            buttonMakeTree.doClick();
        });

        buttonToBracketNotation.addActionListener(actionEvent -> {
            if (tree == null) {
                return;
            }
            textFieldBracketNotationTree.setText(tree.toBracketStr());
        });

        buttonSaveImage.addActionListener(actionEvent -> {
            if (tree == null) {
                return;
            }
            try {
                if (fileChooserSave.showSaveDialog(TreeDemoFrame.this) == JFileChooser.APPROVE_OPTION) {
                    String filename = fileChooserSave.getSelectedFile().getPath();
                    if (!filename.toLowerCase().endsWith(".svg")) {
                        filename += ".svg";
                    }
                    BinaryTreePainter.saveIntoFile(tree, filename, checkBoxTransparent.isSelected());
                }
            } catch (Exception e) {
                SwingUtils.showErrorMessageBox(e);
            }
        });

        buttonPreOrderTraverse.addActionListener(actionEvent -> {
            showSystemOut(() -> {
                System.out.println("Посетитель:");
                BinaryTreeAlgorithms.preOrderVisit(tree.getRoot(), (value, level) -> {
                    System.out.println(value + " (уровень " + level + ")");
                });
                System.out.println();
                System.out.println("Итератор:");
                for (Integer i : BinaryTreeAlgorithms.preOrderValues(tree.getRoot())) {
                    System.out.println(i);
                }
            });
        });

        buttonInOrderTraverse.addActionListener(actionEvent -> {
            showSystemOut(() -> {
                System.out.println("Посетитель:");
                BinaryTreeAlgorithms.inOrderVisit(tree.getRoot(), (value, level) -> {
                    System.out.println(value + " (уровень " + level + ")");
                });
                System.out.println();
                System.out.println("Итератор:");
                for (Integer i : BinaryTreeAlgorithms.inOrderValues(tree.getRoot())) {
                    System.out.println(i);
                }
            });
        });

        buttonPostOrderTraverse.addActionListener(actionEvent -> {
            showSystemOut(() -> {
                System.out.println("Посетитель:");
                BinaryTreeAlgorithms.postOrderVisit(tree.getRoot(), (value, level) -> {
                    System.out.println(value + " (уровень " + level + ")");
                });
                System.out.println();
                System.out.println("Итератор:");
                for (Integer i : BinaryTreeAlgorithms.postOrderValues(tree.getRoot())) {
                    System.out.println(i);
                }
            });
        });

        buttonByLevelTraverse.addActionListener(actionEvent -> {
            showSystemOut(() -> {
                System.out.println("Посетитель:");
                BinaryTreeAlgorithms.byLevelVisit(tree.getRoot(), (value, level) -> {
                    System.out.println(value + " (уровень " + level + ")");
                });
                System.out.println();
                System.out.println("Итератор:");
                for (Integer i : BinaryTreeAlgorithms.byLevelValues(tree.getRoot())) {
                    System.out.println(i);
                }
            });
        });
    }

    public void repaintTree() {
        paintPanel.repaint();
    }

    private void showSystemOut(Runnable action) {
        PrintStream oldOut = System.out;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            System.setOut(new PrintStream(baos, true, "UTF-8"));

            action.run();

            textAreaSystemOut.setText(baos.toString("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            SwingUtils.showErrorMessageBox(e);
        }
        System.setOut(oldOut);
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panelMain = new JPanel();
        panelMain.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(10, 10, 10, 10), 10, 10));
        splitPaneMain = new JSplitPane();
        panelMain.add(splitPaneMain, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        splitPaneMain.setLeftComponent(panel1);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(3, 4, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Дерево в скобочной нотации:");
        panel2.add(label1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 4, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textFieldBracketNotationTree = new JTextField();
        textFieldBracketNotationTree.setText("8 (6 (4 (5), 6), 5 (, 5 (2, 8)))");
        panel2.add(textFieldBracketNotationTree, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 4, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        buttonMakeTree = new JButton();
        buttonMakeTree.setText("Построить дерево");
        panel2.add(buttonMakeTree, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        panel2.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(2, 2, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        buttonDeleteTopLeaves = new JButton();
        buttonDeleteTopLeaves.setText("Удалить верхние листья");
        panel2.add(buttonDeleteTopLeaves, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panelPaintArea = new JPanel();
        panelPaintArea.setLayout(new BorderLayout(0, 0));
        panel1.add(panelPaintArea, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer2 = new com.intellij.uiDesigner.core.Spacer();
        panelPaintArea.add(spacer2, BorderLayout.CENTER);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 4, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel3, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonSaveImage = new JButton();
        buttonSaveImage.setText("Сохранить изображение в SVG");
        panel3.add(buttonSaveImage, new com.intellij.uiDesigner.core.GridConstraints(0, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonToBracketNotation = new JButton();
        buttonToBracketNotation.setText("В скобочное представление");
        panel3.add(buttonToBracketNotation, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer3 = new com.intellij.uiDesigner.core.Spacer();
        panel3.add(spacer3, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        checkBoxTransparent = new JCheckBox();
        checkBoxTransparent.setText("прозрачность");
        panel3.add(checkBoxTransparent, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(5, 1, new Insets(0, 0, 0, 0), -1, -1));
        splitPaneMain.setRightComponent(panel4);
        buttonPreOrderTraverse = new JButton();
        buttonPreOrderTraverse.setText("Прямой обход");
        panel4.add(buttonPreOrderTraverse, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonInOrderTraverse = new JButton();
        buttonInOrderTraverse.setText("Симметричный обход");
        panel4.add(buttonInOrderTraverse, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonPostOrderTraverse = new JButton();
        buttonPostOrderTraverse.setText("Обратный обход");
        panel4.add(buttonPostOrderTraverse, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonByLevelTraverse = new JButton();
        buttonByLevelTraverse.setText("Обход в ширину");
        panel4.add(buttonByLevelTraverse, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel4.add(scrollPane1, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        textAreaSystemOut = new JTextArea();
        scrollPane1.setViewportView(textAreaSystemOut);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panelMain;
    }

}
