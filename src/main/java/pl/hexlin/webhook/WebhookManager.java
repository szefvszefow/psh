package pl.hexlin.webhook;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.send.WebhookEmbed;
import club.minnced.discord.webhook.send.WebhookEmbedBuilder;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import org.javacord.api.entity.Attachment;
import org.javacord.api.entity.channel.AutoArchiveDuration;
import org.javacord.api.entity.emoji.CustomEmoji;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;
import pl.hexlin.Instance;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class WebhookManager {
    public final Instance instance;
    public WebhookClient client;
    public List<String> imageExtensions;

    public WebhookManager(Instance instance) {
        this.instance = instance;
        this.client = WebhookClient.withUrl("https://discord.com/api/webhooks/1221528523466870937/vJcgtb1IJB-3TGw3iv4Li5bk266AkCWqWIe84Ld3F_Om64HeixGl-NHBjuA919C28_It");
        this.imageExtensions = Arrays.asList("jpg", "jpeg", "png", "gif", "bmp", "tiff", "webp", "svg",
                "jfif", "heif");
    }

    public String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex != -1 && lastDotIndex < fileName.length() - 1) {
            return fileName.substring(lastDotIndex + 1);
        }
        return "";
    }

    public void sendGoidaResult(User user, File file, String result) {
        WebhookClient client1 = WebhookClient.withUrl("https://discord.com/api/webhooks/1232076895080808538/Kv8H1xR_koyPCwuAwYpikqtHW2JJ4SKcTQNgNVLoyt0owNRTqzURCIZ5QhAWOwlpyiUz");
        WebhookMessageBuilder builder = new WebhookMessageBuilder();
        builder.setUsername(user.getName());
        builder.setAvatarUrl(user.getAvatar().getUrl().toString());

        builder.setContent("\uD83C\uDDF7\uD83C\uDDFA Test rozwiązany przez uzytkownika: " + user.getMentionTag() + "\n" +
                "❓ Wynik: " + result);
        builder.addFile(file);
        CustomEmoji r1 = instance.api.getServerById("1199446281840492629").get().getCustomEmojiById("1199874474737143911").get();
        CustomEmoji r2 = instance.api.getServerById("1199446281840492629").get().getCustomEmojiById("1199874959359606804").get();

        client1.send(builder.build()).thenAccept(readonlyMessage -> {
            instance.api.getMessageById(readonlyMessage.getId(), instance.api.getTextChannelById("1222053317136617545").get()).thenAccept(message -> {
                message.addReaction(r1);
                message.addReaction(r2);
            }).join();
        });
    }
    public void sendBanderaResult(User user, File file, String result) {
        WebhookClient client1 = WebhookClient.withUrl("https://discord.com/api/webhooks/1222475751195148308/4thgN9sdGGjvOQsmQMtBRGhWU-pikk1BRyd23-bL1xV05KRUHXLnPlOb2VWLW3lWyaV_");
        WebhookMessageBuilder builder = new WebhookMessageBuilder();
        builder.setUsername(user.getName());
        builder.setAvatarUrl(user.getAvatar().getUrl().toString());

        builder.setContent("\uD83C\uDDFA\uD83C\uDDE6 Test rozwiązany przez uzytkownika: " + user.getMentionTag() + "\n" +
                "❓ Wynik: " + result);
        builder.addFile(file);
        CustomEmoji r1 = instance.api.getServerById("1199446281840492629").get().getCustomEmojiById("1199874474737143911").get();
        CustomEmoji r2 = instance.api.getServerById("1199446281840492629").get().getCustomEmojiById("1199874959359606804").get();

        client1.send(builder.build()).thenAccept(readonlyMessage -> {
            instance.api.getMessageById(readonlyMessage.getId(), instance.api.getTextChannelById("1222469995162894337").get()).thenAccept(message -> {
                message.addReaction(r1);
                message.addReaction(r2);
            }).join();
        });
    }

    public void sendNarodowiecResult(User user, File file, String result) {
        WebhookClient client1 = WebhookClient.withUrl("https://discord.com/api/webhooks/1229569569069203517/mOGPr-ZzgS1Ou6iSzs7A5tfV0uNSarCrcEeUdrpYXe5VQiXzUpI20ZIlarByBfPURayU");
        WebhookMessageBuilder builder = new WebhookMessageBuilder();
        builder.setUsername(user.getName());
        builder.setAvatarUrl(user.getAvatar().getUrl().toString());

        builder.setContent("\uD83C\uDDFA\uD83C\uDDE6 Test rozwiązany przez uzytkownika: " + user.getMentionTag() + "\n" +
                "❓ Wynik: " + result);
        builder.addFile(file);
        CustomEmoji r1 = instance.api.getServerById("1199446281840492629").get().getCustomEmojiById("1199874474737143911").get();
        CustomEmoji r2 = instance.api.getServerById("1199446281840492629").get().getCustomEmojiById("1199874959359606804").get();

        client1.send(builder.build()).thenAccept(readonlyMessage -> {
            instance.api.getMessageById(readonlyMessage.getId(), instance.api.getTextChannelById("1229568592199159892").get()).thenAccept(message -> {
                message.addReaction(r1);
                message.addReaction(r2);
            }).join();
        });
    }
    public void sendMeme(User user, Attachment meme) {
        WebhookMessageBuilder builder = new WebhookMessageBuilder();
        builder.setUsername(user.getName());
        builder.setAvatarUrl(user.getAvatar().getUrl().toString());
        CompletableFuture<BufferedImage> watermarkFuture = instance.applyWatermark(meme);
        watermarkFuture.thenAccept(watermarkedImage -> {
            try {
                File outputImageFile = File.createTempFile("watermarked", ".png");
                ImageIO.write(watermarkedImage, "png", outputImageFile);

                builder.addFile(outputImageFile);

                CustomEmoji r1 = instance.api.getServerById("1199446281840492629").get().getCustomEmojiById("1199874474737143911").get();
                CustomEmoji r2 = instance.api.getServerById("1199446281840492629").get().getCustomEmojiById("1199874959359606804").get();

                client.send(builder.build()).thenAccept(readonlyMessage -> {
                    instance.api.getMessageById(readonlyMessage.getId(), instance.api.getTextChannelById("1199459264025079858").get()).thenAccept(message -> {
                        message.addReaction(r1);
                        message.addReaction(r2);
                    }).join();
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void sendCountryGraphic(User user, File file) {
        WebhookClient client1 = WebhookClient.withUrl("https://discord.com/api/webhooks/1224730026121891923/m2kh-ZMMwYX1ePawssSjiryec2-lt1X7iAN8huEgwGtUQN2mhud0ZAbuVMylOrcvo1Rc");
        WebhookMessageBuilder builder = new WebhookMessageBuilder();
        builder.setUsername(user.getName());
        builder.setAvatarUrl(user.getAvatar().getUrl().toString());

        builder.setContent("Grafika wygenerowana przez użytkownika: " + user.getMentionTag());
        builder.addFile(file);
        CustomEmoji r1 = instance.api.getServerById("1199446281840492629").get().getCustomEmojiById("1199874474737143911").get();
        CustomEmoji r2 = instance.api.getServerById("1199446281840492629").get().getCustomEmojiById("1199874959359606804").get();

        client1.send(builder.build()).thenAccept(readonlyMessage -> {
            instance.api.getMessageById(readonlyMessage.getId(), instance.api.getTextChannelById("1210408376912191568").get()).thenAccept(message -> {
                message.addReaction(r1);
                message.addReaction(r2);
                message.createThread("Komentarze", AutoArchiveDuration.ONE_WEEK);
            }).join();
        });
    }

    public void sendPoliticalGraphic(User user, File file) {
        WebhookClient client1 = WebhookClient.withUrl("https://discord.com/api/webhooks/1228408015460503693/Jm2VoAv7VZrg9nnQSXhq46gDOjCJsu1EX49v9YQzPQU-J6nQGlE3dGAb-9XKekarVPUR");
        WebhookMessageBuilder builder = new WebhookMessageBuilder();
        builder.setUsername(user.getName());
        builder.setAvatarUrl(user.getAvatar().getUrl().toString());

        builder.setContent("Wyniki testu użytkownika: " + user.getMentionTag());
        builder.addFile(file);
        CustomEmoji r1 = instance.api.getServerById("1199446281840492629").get().getCustomEmojiById("1199874474737143911").get();
        CustomEmoji r2 = instance.api.getServerById("1199446281840492629").get().getCustomEmojiById("1199874959359606804").get();

        client1.send(builder.build()).thenAccept(readonlyMessage -> {
            instance.api.getMessageById(readonlyMessage.getId(), instance.api.getTextChannelById("1210408376912191568").get()).thenAccept(message -> {
                message.addReaction(r1);
                message.addReaction(r2);
            }).join();
        });
    }
}
