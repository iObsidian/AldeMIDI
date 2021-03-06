package perfectpitch.player;

import perfectpitch.achievement.Achievement;
import perfectpitch.achievement.Achievements;
import perfectpitch.player.serialization.SerializableBufferedImage;
import perfectpitch.util.GetResource;
import perfectpitch.util.ResizeBufferedImage;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.List;

public class Player implements Serializable {

    private static final BufferedImage defaultImage = (BufferedImage) GetResource.getDefaultUserImage();

    private static final Color defaultColor = Color.BLACK;

    private String name;

    private SerializableBufferedImage image;

    private boolean configured;

    private Color color;

    private int EXP;

    private String hashedPassword;

    public List<Achievement> achievements;

    public Player(String name, String hashedPassword) {
        this.name = name;
        this.hashedPassword = hashedPassword;
        save();
    }

    public String getName() {
        return name;
    }

    public void setImage(BufferedImage image) {
        if (image.getHeight() > 100 || image.getWidth() > 100) {
            int ratio = image.getWidth() / image.getHeight();
            image = ResizeBufferedImage.resize(image, 100, 100 * ratio);
        }

        this.image = new SerializableBufferedImage(image);
        save();
    }

    public BufferedImage getImage() {
        if (this.image == null) {
            setImage(defaultImage);
        }
        return this.image.getBufferedImage();
    }

    public void setColor(Color color) {
        this.color = color;
        save();
    }

    public Color getColor() {
        if (this.color == null) {
            return defaultColor;
        }
        return this.color;
    }

    public void setEXP(int EXP) {
        this.EXP = EXP;
        save();
    }

    public List<Achievement> getAchievements() {
        if (achievements == null) {
            achievements = Achievements.getAchievements();
        }

        return achievements;
    }

    public int getEXP() {
        return this.EXP;
    }

    @Override
    public String toString() {
        return name;
    }

    private void save() {
        Players.saveToFile(this);
    }

    public int getLevel() {
        return 10;
    }

    public boolean isConfigured() {
        return this.configured;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

}
