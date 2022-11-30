package it.unibo.oop.lab.streams;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 * Modify this small program adding new filters.
 * Realize this exercise using as much as possible the Stream library.
 *
 * 1) Convert to lowercase
 *
 * 2) Count the number of chars
 *
 * 3) Count the number of lines
 *
 * 4) List all the words in alphabetical order
 * 
 * 5) Write the count for each word, e.g. "word word pippo" should output "pippo -> 1 word -> 2"
 *
 */
public final class LambdaFilter extends JFrame {

    private static final long serialVersionUID = 1760990730218643730L;

    private enum Command {
        /**
         * Commands.
         */
        IDENTITY("No modifications", Function.identity()),
        TOLOWERCASE("Set all case as lower", x -> x.toLowerCase()), //NOPMD
        COUNTCHAR("Count the chars", x -> Long.toString(x.chars().count())),
        COUNTNEWSPACE("Count the new spaces", x -> Long.toString(x.chars()
        .mapToObj(y -> (char) y)
        .filter(z -> z == '\n')
        .count())),
        
        SORT("Order alphabetically the characters", x -> Stream.of(x.split(" "))
        .sorted()
        .reduce((a,b) -> a.concat(" " + b ))
        .get()),
        
        //.mapToObj(y -> (String) y)
        //.sorted().toString()),

        COUNTOCCORUCENSES("Count how many time a word is present in the text",
        x -> Stream.of(x.split(" "))
        .collect(Collectors.toMap(Function.identity(), a -> 1, (a, b) -> a += b))
        .entrySet().stream()
        .collect(
            () -> new StringBuilder(), 
            (q, w) -> q.append(w.getKey() + " - " + w.getValue() + "; \n"), 
            (e, r) -> e.append(" (!nonvisualizzato!) ").append(r))
        .toString());
        //(a, b) -> a.append(",").append(b)));
        //forEach( x -> String.toString(x.getKey() + x.getValue())) );
        //.collect(Collectors.toMap(Song::getAlbumName, Song::getDuration, (x,y) -> x+=y ))
        //.entrySet().stream().max( (x,y) -> x.getValue() > y.getValue() ? 1 : -1).get().getKey());

        private final String commandName;
        private final Function<String, String> fun;

        Command(final String name, final Function<String, String> process) {
            commandName = name;
            fun = process;
        }

        @Override
        public String toString() {
            return commandName;
        }

        public String translate(final String s) {
            return fun.apply(s);
        }
    }

    private LambdaFilter() {
        super("Lambda filter GUI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        final JPanel panel1 = new JPanel();
        final LayoutManager layout = new BorderLayout();
        panel1.setLayout(layout);
        final JComboBox<Command> combo = new JComboBox<>(Command.values());
        panel1.add(combo, BorderLayout.NORTH);
        final JPanel centralPanel = new JPanel(new GridLayout(1, 2));
        final JTextArea left = new JTextArea();
        left.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        final JTextArea right = new JTextArea();
        right.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        right.setEditable(false);
        centralPanel.add(left);
        centralPanel.add(right);
        panel1.add(centralPanel, BorderLayout.CENTER);
        final JButton apply = new JButton("Apply");
        apply.addActionListener(ev -> right.setText(((Command) combo.getSelectedItem()).translate(left.getText())));
        panel1.add(apply, BorderLayout.SOUTH);
        setContentPane(panel1);
        final Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        final int sw = (int) screen.getWidth();
        final int sh = (int) screen.getHeight();
        setSize(sw / 4, sh / 4);
        setLocationByPlatform(true);
    }

    /**
     * @param a unused
     */
    public static void main(final String... a) {
        final LambdaFilter gui = new LambdaFilter();
        gui.setVisible(true);
    }
}
