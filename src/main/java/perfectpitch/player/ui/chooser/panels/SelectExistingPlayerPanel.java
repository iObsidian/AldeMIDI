package perfectpitch.player.ui.chooser.panels;

import alde.commons.util.WrapLayout;
import org.slf4j.LoggerFactory;
import perfectpitch.player.Player;
import perfectpitch.player.Players;
import perfectpitch.player.password.PasswordStorage;
import perfectpitch.player.password.PasswordStorage.CannotPerformOperationException;
import perfectpitch.player.password.PasswordStorage.InvalidHashException;
import perfectpitch.player.ui.PlayerImageViewer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Consumer;

public class SelectExistingPlayerPanel extends JPanel {

    private static org.slf4j.Logger log = LoggerFactory.getLogger(SelectExistingPlayerPanel.class);

    Player selected;

    /**
     * Create the panel.
     */
    public SelectExistingPlayerPanel(final Consumer<Player> callback, final Runnable goBack) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JPanel selectPanel = new JPanel();
        selectPanel.setBorder(null);
        add(selectPanel);
        selectPanel.setLayout(new BorderLayout(0, 0));

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportBorder(null);
        selectPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel warpLayout = new JPanel();
        warpLayout.setBorder(null);
        warpLayout.setLayout(new WrapLayout());
        scrollPane.setViewportView(warpLayout);

        for (Player p : Players.getPlayers()) {

            warpLayout.add(new SelectablePanel<>(new PlayerImageViewer(p, false), p, player -> {

                try {

                    String password = getPassword();

                    if (password == null || password.isEmpty()) {
                        log.info("Password is null or empty");
                    } else {
                        if (PasswordStorage.verifyPassword(password, player.getHashedPassword())) {
                            log.info("Verified!");
                            callback.accept(player);
                        } else {
                            log.info("Wrong password, not verified...");
                            JOptionPane.showMessageDialog(null, "Wrong password", "Error", JOptionPane.INFORMATION_MESSAGE, null);
                        }
                    }

                } catch (CannotPerformOperationException e) {
                    log.info("Error...");
                    e.printStackTrace();
                } catch (InvalidHashException e) {
                    log.info("Error..");
                    e.printStackTrace();
                }

            }));
            //playersComboBox.addItem(p);
        }

        JPanel acceptPanel = new JPanel();
        add(acceptPanel);
        acceptPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        JButton btnBack = new JButton("Cancel");
        btnBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                goBack.run();
            }
        });
        acceptPanel.add(btnBack);

    }

    public String getPassword() {
        JPanel panel = new JPanel();
        JLabel label = new JLabel("Password : ");
        JPasswordField pass = new JPasswordField(10);
        panel.add(label);
        panel.add(pass);
        String[] options = new String[]{"OK", "Cancel"};
        int option = JOptionPane.showOptionDialog(null, panel, "Enter your password", JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[1]);
        if (option == 0) {
            return new String(pass.getPassword());
        }
        return null;
    }

}
