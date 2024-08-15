package pl.hexlin.task;

import org.javacord.api.entity.activity.ActivityType;
import org.javacord.api.entity.user.UserStatus;
import pl.hexlin.Instance;

import java.time.LocalTime;
import java.util.*;

public class PresenceUpdateTask extends TimerTask {
    public final Instance instance;
    public List<String> presences = new ArrayList<>();
    public Random random = new Random();

    public PresenceUpdateTask(Instance instance) {
        this.instance = instance;
        presences.addAll(Arrays.asList(
                "Pisanie z femboyami",
                "HOI4 w zakolanówkach",
                "Kupowanie zakolanówek i spódnicy",
                "Nakładanie sankcji",
                "Czytanie manifestu komunistycznego",
                "Propagowanie komunistycznych sentymentów",
                "Przygotowywanie komunistycznej rewolucji",
                "Osiąganie Socrealizmu",
                "Represje Stalinowskie",
                "Wakacje w Białorusii",
                "Wakacje w Nowosybirsku",
                "Obraz Putina nad monitorem",
                "Kremlowską propagandę",
                "Ideologię Z",
                "Grupę Wagnera",
                "Wakacje w Moskwie",
                "Obraz Stalina nad monitorem",
                "Femboyów na PSH",
                "Akcję FSB",
                "Mauzoleum Lenina",
                "Specjalną Operację Militarną",
                "Szturm Donbasu",
                "Pabyedę",
                "Koncert Putina"
        ));
    }

    @Override
    public void run() {
        int randomPresence = random.nextInt(presences.size());
        instance.api.updateActivity(ActivityType.WATCHING, presences.get(randomPresence));
        instance.api.updateStatus(UserStatus.IDLE);
    }
}
