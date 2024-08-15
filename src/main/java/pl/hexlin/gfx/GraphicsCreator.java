package pl.hexlin.gfx;

import pl.hexlin.Instance;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class GraphicsCreator {
    public final Instance instance;

    public GraphicsCreator(Instance instance) {
        this.instance = instance;
    }

    public File createMapImage(String userId, String flagURL, String dictatorURL, String mapURL, String countryName, String rulingParty, String ideologyName, String dictatorName, String allianceName, String militaryCount) throws IOException, FontFormatException {
        BufferedImage layoutImage = ImageIO.read(new URL("http://hexlin.rsbmw.pl/lejaout.png"));
        BufferedImage flagImage = ImageIO.read(new URL(flagURL));
        BufferedImage dictatorImage = ImageIO.read(new URL(dictatorURL));
        BufferedImage mapImage = ImageIO.read(new URL(mapURL));

        int flagxCoordinate = 19;
        int flagyCoordinate = 398;
        int flagWidth = 226;
        int flagHeight = 137;

        int dictatorxCoordinate = 21;
        int dictatoryCoordinate = 76;
        int dictatorWidth = 221;
        int dictatorHeight = 268;

        int mapxCoordinate = 270;
        int mapyCoordinate = 40;
        int mapWidth = 731;
        int mapHeight = 658;

        Graphics g = layoutImage.getGraphics();
        g.drawImage(flagImage, flagxCoordinate, flagyCoordinate, flagWidth, flagHeight, null);
        g.drawImage(dictatorImage, dictatorxCoordinate, dictatoryCoordinate, dictatorWidth, dictatorHeight, null);
        g.drawImage(mapImage, mapxCoordinate, mapyCoordinate, mapWidth, mapHeight, null);


        int maxTextWidth = 150;

        Font customFont = new Font("Arial", Font.BOLD, 10);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color textColor = Color.WHITE;
        g.setFont(customFont);
        g.setColor(textColor);

        for (int fontSize = 10; fontSize > 0; fontSize--) {
            customFont = customFont.deriveFont(Font.BOLD, fontSize);
            FontMetrics metrics = g.getFontMetrics(customFont);
            int textWidth = metrics.stringWidth(countryName);
            if (textWidth <= maxTextWidth) {
                break;
            }
        }

        int countryxCoordinate = 1058;
        int countryyCoordinate = 90;

        g.drawString(countryName, countryxCoordinate, countryyCoordinate);

        int rulingpartyxCoordinate = 1058;
        int rulingpartyyCoordinate = 157;

        g.drawString(rulingParty, rulingpartyxCoordinate, rulingpartyyCoordinate);


        int ideologyxCoordinate = 1058;
        int ideologyyCoordinate = 223;

        g.drawString(ideologyName, ideologyxCoordinate, ideologyyCoordinate);

        int dictator1xCoordinate = 1058;
        int dictator1yCoordinate = 285;

        g.drawString(dictatorName, dictator1xCoordinate, dictator1yCoordinate);

        int alliancexCoordinate = 1058;
        int allianceyCoordinate = 344;

        g.drawString(allianceName, alliancexCoordinate, allianceyCoordinate);

        int militaryxCoordinate = 1058;
        int militaryyCoordinate = 412;

        g.drawString(militaryCount, militaryxCoordinate, militaryyCoordinate);
        g.dispose();

        File file = new File(userId + ".png");
        ImageIO.write(layoutImage, "png", file);
        return file;
    }

    public File createQuizImage(String userId, String fileName, Double percentageQuiz) throws IOException {
        BufferedImage originalLayoutImage = ImageIO.read(new File(fileName));

        int newWidth = 1100;
        int newHeight = 440;
        BufferedImage layoutImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = layoutImage.createGraphics();
        g2d.drawImage(originalLayoutImage, 0, 0, newWidth, newHeight, null);
        g2d.dispose();

        BufferedImage avatarImage = ImageIO.read(new URL(instance.api.getUserById(userId).join().getAvatar().getUrl().toString()));
        String userName = instance.api.getUserById(userId).join().getName().toUpperCase();

        int usernamex = 480;
        int usernamey = 125;

        int percentagex = 777;
        int percentagey = 403;

        int avatarxCoordinate = 435;
        int avataryCoordinate = 132;
        int avatarWidth = 231;
        int avatarHeight = 196;

        Graphics g = layoutImage.getGraphics();
        Font customFont = new Font("Arial Black", Font.BOLD, 36);

        g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color textColor = Color.WHITE;
        g.setFont(customFont);
        g.setColor(textColor);

        g.drawImage(avatarImage, avatarxCoordinate, avataryCoordinate, avatarWidth, avatarHeight, null);
        int roundVal = (int) Math.round(Double.valueOf(percentageQuiz));
        g.drawString(String.valueOf(roundVal) + '%', percentagex, percentagey);

        g.dispose();
        File file = new File(userId + ".png");
        ImageIO.write(layoutImage, "png", file);
        return file;
    }

    public File createQuizImage2(String userId, String fileName, Double percentageQuiz) throws IOException {
        BufferedImage originalLayoutImage = ImageIO.read(new URL(fileName));

        int newWidth = 1100;
        int newHeight = 440;
        BufferedImage layoutImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = layoutImage.createGraphics();
        g2d.drawImage(originalLayoutImage, 0, 0, newWidth, newHeight, null);
        g2d.dispose();

        BufferedImage avatarImage = ImageIO.read(new URL(instance.api.getUserById(userId).join().getAvatar().getUrl().toString()));
        String userName = instance.api.getUserById(userId).join().getName().toUpperCase();

        int usernamex = 480;
        int usernamey = 125;

        int percentagex = 777;
        int percentagey = 403;

        int avatarxCoordinate = 435;
        int avataryCoordinate = 132;
        int avatarWidth = 231;
        int avatarHeight = 196;

        Graphics g = layoutImage.getGraphics();
        Font customFont = new Font("Arial Black", Font.BOLD, 36);

        g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color textColor = Color.WHITE;
        g.setFont(customFont);
        g.setColor(textColor);

        g.drawImage(avatarImage, avatarxCoordinate, avataryCoordinate, avatarWidth, avatarHeight, null);
        int roundVal = (int) Math.round(Double.valueOf(percentageQuiz));
        g.drawString(String.valueOf(roundVal) + '%', percentagex, percentagey);

        g.dispose();
        File file = new File(userId + ".png");
        ImageIO.write(layoutImage, "png", file);
        return file;
    }

    public File createPoliticalQuizImage(String userId, String fileName, int socialistAxe, int capitalistAxe, int liberalismAxe, int authoritarianAxe, int nationalismAxe, int internationalismAxe, int religionAxe, int traditionalismAxe, int militarismAxe, int progressAxe) throws IOException {
        BufferedImage originalLayoutImage = ImageIO.read(new File(fileName));
        int newWidth = 500;
        int newHeight = 300;
        BufferedImage layoutImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = layoutImage.createGraphics();
        g2d.drawImage(originalLayoutImage, 0, 0, newWidth, newHeight, null);
        g2d.dispose();

        BufferedImage avatarImage = ImageIO.read(new URL(instance.api.getUserById(userId).join().getAvatar().getUrl().toString()));

        int avatarxCoordinate = 332;
        int avataryCoordinate = 38;
        int avatarWidth = 144;
        int avatarHeight = 134;

        Graphics g = layoutImage.getGraphics();
        Font customFont = new Font("Arial Black", Font.BOLD, 10);

        g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color textColor = Color.WHITE;
        g.setFont(customFont);
        g.setColor(textColor);

        g.drawString(instance.calculatePercentage(socialistAxe, 9) + "%" + " " + instance.parseAxis(socialistAxe) + " /9", 42, 295);
        g.drawString(instance.calculatePercentage(capitalistAxe, 9) + "%" + " " + instance.parseAxis(capitalistAxe) + " /9", 118, 295);
        g.drawString(instance.calculatePercentage(liberalismAxe, 8) + "%" + " " + instance.parseAxis(liberalismAxe) + " /8", 192, 295);
        g.drawString(instance.calculatePercentage(authoritarianAxe, 2) + "%" + " " + instance.parseAxis(authoritarianAxe) + " /2", 269, 295);
        g.drawString(instance.calculatePercentage(nationalismAxe, 8) + "%" + " " + instance.parseAxis(nationalismAxe) + " /8", 342, 295);
        g.drawString(instance.calculatePercentage(internationalismAxe, 3) + "%" + " " + instance.parseAxis(internationalismAxe) + " /3", 192, 207);
        g.drawString(instance.calculatePercentage(religionAxe, 2) + "%" + " " + instance.parseAxis(religionAxe) + " /2", 269, 207);
        g.drawString(instance.calculatePercentage(traditionalismAxe, 9) + "%" + " " + instance.parseAxis(traditionalismAxe) + " /9", 419, 295);
        g.drawString(instance.calculatePercentage(militarismAxe, 2) + "%" + " " + instance.parseAxis(militarismAxe) + " /2", 42, 207);
        g.drawString(instance.calculatePercentage(progressAxe, 8) + "%" + " " + instance.parseAxis(progressAxe) + " /8", 118, 207);

        g.drawImage(avatarImage, avatarxCoordinate, avataryCoordinate, avatarWidth, avatarHeight, null);
        g.dispose();
        File file = new File(userId + ".png");
        ImageIO.write(layoutImage, "png", file);
        return file;
    }

    public File createLapaimage(String userId, String content, String fileName) throws IOException {
        BufferedImage originalLayoutImage = ImageIO.read(new URL(fileName));

        int newWidth = 512;
        int newHeight = 512;
        BufferedImage layoutImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = layoutImage.createGraphics();
        g2d.drawImage(originalLayoutImage, 0, 0, newWidth, newHeight, null);
        g2d.dispose();

        int contentx = 11;
        int contenty = 100;

        Graphics g = layoutImage.getGraphics();
        Font customFont = new Font("Arial Black", Font.BOLD, 27);


        g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color textColor = Color.BLACK;
        g.setFont(customFont);
        g.setColor(textColor);

        g.drawString(content, contentx, contenty);

        g.dispose();
        File file = new File(userId + ".png");
        ImageIO.write(layoutImage, "png", file);
        return file;
    }
}
