package pl.hexlin.radio;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.javacord.api.audio.AudioSource;
import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import pl.hexlin.Instance;

import java.awt.*;
import java.time.OffsetDateTime;

public class TrackPlayer {
    public final Instance instance;
    public final AudioPlayerManager audioPlayerManager;
    public AudioPlayer audioPlayer;
    public TrackScheduler trackScheduler;
    public YoutubeAudioSourceManager youtubeAudioSourceManager;

    public TrackPlayer(Instance instance) {
        this.instance = instance;
        this.audioPlayerManager = new DefaultAudioPlayerManager();
        this.audioPlayerManager.registerSourceManager(new YoutubeAudioSourceManager());
        AudioSourceManagers.registerRemoteSources(audioPlayerManager);
        audioPlayer = audioPlayerManager.createPlayer();
        trackScheduler = new TrackScheduler(instance, audioPlayer);
        audioPlayer.addListener(trackScheduler);

        youtubeAudioSourceManager = new YoutubeAudioSourceManager();
        this.youtubeAudioSourceManager.configureRequests(config -> RequestConfig.copy(config)
                .setCookieSpec(CookieSpecs.IGNORE_COOKIES)
                .build());
    }

    public void loadTrack(TextChannel channel, final String trackUrl, ServerVoiceChannel serverVoiceChannel) {
        AudioSource source = new LavaPlayerAudioSource(instance.api, audioPlayer);
        serverVoiceChannel.connect().thenAccept(audioConnection -> {
            audioConnection.setAudioSource(source);
            audioConnection.setSelfDeafened(false);

            audioPlayerManager.loadItemOrdered(audioPlayerManager, trackUrl, new AudioLoadResultHandler() {
                @Override
                public void trackLoaded(AudioTrack track) {
                    trackScheduler.queue(track);

                    EmbedBuilder queue = new EmbedBuilder()
                            .setTitle("● Dodano do kolejki utwór " + track.getInfo().title)
                            .setColor(Color.decode("#B32DFF"))
                            .setFooter("\uD83E\uDD85PSH - Serwer Polityczno-Gamingowy | By Hexlin 2024", "https://i.imgur.com/XuIIA1v.png")
                            .setTimestamp(OffsetDateTime.now().toInstant());

                    new MessageBuilder().setEmbed(queue).send(channel);
                }

                @Override
                public void playlistLoaded(AudioPlaylist playlist) {
                    EmbedBuilder queue = new EmbedBuilder()
                            .setTitle("● Dodano do kolejki wszystkie utwory z playlisty.")
                            .setColor(Color.decode("#B32DFF"))
                            .setFooter("\uD83E\uDD85PSH - Serwer Polityczno-Gamingowy | By Hexlin 2024", "https://i.imgur.com/XuIIA1v.png")
                            .setTimestamp(OffsetDateTime.now().toInstant());

                    new MessageBuilder().setEmbed(queue).send(channel);
                    playlist.getTracks().forEach(audioTrack -> {
                        trackScheduler.queue(audioTrack);
                    });
                }

                @Override
                public void noMatches() {
                    EmbedBuilder queue = new EmbedBuilder()
                            .setTitle("● Nie odnaleziono twojego utworu.")
                            .setColor(Color.decode("#B32DFF"))
                            .setFooter("\uD83E\uDD85PSH - Serwer Polityczno-Gamingowy | By Hexlin 2024", "https://i.imgur.com/XuIIA1v.png")
                            .setTimestamp(OffsetDateTime.now().toInstant());

                    new MessageBuilder().setEmbed(queue).send(channel);
                }

                @Override
                public void loadFailed(FriendlyException exception) {
                    EmbedBuilder queue = new EmbedBuilder()
                            .setTitle("● Nie udało się odtworzyć utworu z powodu: `" + exception.getMessage() + "`")
                            .setColor(Color.decode("#B32DFF"))
                            .setFooter("\uD83E\uDD85PSH - Serwer Polityczno-Gamingowy | By Hexlin 2024", "https://i.imgur.com/XuIIA1v.png")
                            .setTimestamp(OffsetDateTime.now().toInstant());

                    new MessageBuilder().setEmbed(queue).send(channel);
                }
            });
        });
    }

    public void stop() {
        audioPlayer.stopTrack();
        audioPlayer.destroy();
    }
}