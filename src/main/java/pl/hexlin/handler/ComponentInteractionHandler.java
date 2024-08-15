package pl.hexlin.handler;

import org.javacord.api.entity.channel.PrivateChannel;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.MessageFlag;
import org.javacord.api.entity.message.component.ActionRow;
import org.javacord.api.entity.message.component.Button;
import org.javacord.api.entity.message.component.TextInput;
import org.javacord.api.entity.message.component.TextInputStyle;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.interaction.MessageComponentCreateEvent;
import org.javacord.api.listener.interaction.MessageComponentCreateListener;
import pl.hexlin.Instance;
import pl.hexlin.gfx.GfxProcess;
import pl.hexlin.gfx.GfxStage;
import pl.hexlin.question.Question;
import pl.hexlin.quiz.Quiz;
import pl.hexlin.voting.Election;
import pl.hexlin.voting.RepresentativeCandidate;
import pl.hexlin.voting.Suggestion;

import java.awt.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

public class ComponentInteractionHandler implements MessageComponentCreateListener {
    public final Instance instance;

    public ComponentInteractionHandler(Instance instance) {
        this.instance = instance;
    }

    @Override
    public void onComponentCreate(MessageComponentCreateEvent event) {
    /*    switch (event.getMessageComponentInteraction().asSelectMenuInteraction().get().getChosenOptions().get(0).getLabel()) {
            case "<:flag_ksh:1202942352143159307> Opcja 1" -> {
                event.getMessageComponentInteraction().createImmediateResponder()
                        .setContent("goida 1")
                        .respond();
            }

            case "<:flag_ksh:1202942352143159307> Opcja 2" -> {
                event.getMessageComponentInteraction().createImmediateResponder()
                        .setContent("goida 2")
                        .respond();
            }

            case "<:flag_ksh:1202942352143159307> Opcja 3" -> {
                event.getMessageComponentInteraction().createImmediateResponder()
                        .setContent("goida 3")
                        .respond();
            }
        }*/

        switch (event.getMessageComponentInteraction().getCustomId()) {
            case "quiz-1" -> {
                instance.getQuestHandler().guessCountryFlag();
                event.getMessageComponentInteraction().acknowledge();
            }
            case "quiz-2" -> {
                instance.getQuestHandler().guessCountryCapital();
                event.getMessageComponentInteraction().acknowledge();
            }
            case "lapa" -> {
                event.getInteraction().respondWithModal("lapamodal", "(Bot sam zaczyna zdanie.)",
                        ActionRow.of(TextInput.create(TextInputStyle.SHORT, "lapa", "Pokaż łapę jeśli", true)));
                event.getMessageComponentInteraction().acknowledge();
            }
            case "guess-flag" -> {
                event.getInteraction().respondWithModal("guessflagmodal", "Zgadywanie",
                        ActionRow.of(TextInput.create(TextInputStyle.SHORT, "flag", "Do jakiego państwa należy ta flaga?")));
                event.getMessageComponentInteraction().acknowledge();
            }
            case "guess-capital" -> {
                event.getInteraction().respondWithModal("guesscapitalmodal", "Zgadywanie",
                        ActionRow.of(TextInput.create(TextInputStyle.SHORT, "capital", "Jak się nazywa stolica tego państwa?")));
                event.getMessageComponentInteraction().acknowledge();
            }
            case "quiz-yes" -> {
                if (event.getMessageComponentInteraction().getMessage().isPrivateMessage() && instance.getQuizManager().getUserQuiz(event.getMessageComponentInteraction().getUser().getIdAsString()) != null && instance.getQuizManager().getUserQuiz(event.getMessageComponentInteraction().getUser().getIdAsString()).getQuizName().equalsIgnoreCase("goida")) {
                    Quiz quiz = instance.getQuizManager().getUserQuiz(event.getMessageComponentInteraction().getUser().getIdAsString());
                    PrivateChannel privateChannel = event.getMessageComponentInteraction().getMessage().getPrivateChannel().get();
                    event.getMessageComponentInteraction().acknowledge();
                    switch (quiz.getCurrentStage()) {
                        case 0 -> {
                            quiz.yesAnswered.add(instance.getQuizManager().getAvailableQuiz("goida").getQuestions().get(0));
                            quiz.next(event.getMessageComponentInteraction().getMessage().getIdAsString(), event.getMessageComponentInteraction().getChannel().get());
                        }
                        case 1 -> {
                            quiz.yesAnswered.add(instance.getQuizManager().getAvailableQuiz("goida").getQuestions().get(1));
                            quiz.next(event.getMessageComponentInteraction().getMessage().getIdAsString(), event.getMessageComponentInteraction().getChannel().get());
                        }
                        case 2 -> {
                            quiz.yesAnswered.add(instance.getQuizManager().getAvailableQuiz("goida").getQuestions().get(2));
                            quiz.next(event.getMessageComponentInteraction().getMessage().getIdAsString(), event.getMessageComponentInteraction().getChannel().get());
                        }
                        case 3 -> {
                            quiz.yesAnswered.add(instance.getQuizManager().getAvailableQuiz("goida").getQuestions().get(3));
                            quiz.next(event.getMessageComponentInteraction().getMessage().getIdAsString(), event.getMessageComponentInteraction().getChannel().get());
                        }
                        case 4 -> {
                            quiz.yesAnswered.add(instance.getQuizManager().getAvailableQuiz("goida").getQuestions().get(4));
                            quiz.next(event.getMessageComponentInteraction().getMessage().getIdAsString(), event.getMessageComponentInteraction().getChannel().get());
                        }
                        case 5 -> {
                            quiz.yesAnswered.add(instance.getQuizManager().getAvailableQuiz("goida").getQuestions().get(5));
                            quiz.next(event.getMessageComponentInteraction().getMessage().getIdAsString(), event.getMessageComponentInteraction().getChannel().get());
                        }
                        case 6 -> {
                            quiz.yesAnswered.add(instance.getQuizManager().getAvailableQuiz("goida").getQuestions().get(6));
                            quiz.next(event.getMessageComponentInteraction().getMessage().getIdAsString(), event.getMessageComponentInteraction().getChannel().get());
                        }
                        case 7 -> {
                            quiz.yesAnswered.add(instance.getQuizManager().getAvailableQuiz("goida").getQuestions().get(7));
                            quiz.next(event.getMessageComponentInteraction().getMessage().getIdAsString(), event.getMessageComponentInteraction().getChannel().get());
                        }
                        case 8 -> {
                            quiz.yesAnswered.add(instance.getQuizManager().getAvailableQuiz("goida").getQuestions().get(8));
                            quiz.next(event.getMessageComponentInteraction().getMessage().getIdAsString(), event.getMessageComponentInteraction().getChannel().get());
                        }
                        case 9 -> {
                            quiz.yesAnswered.add(instance.getQuizManager().getAvailableQuiz("goida").getQuestions().get(9));
                            quiz.next(event.getMessageComponentInteraction().getMessage().getIdAsString(), event.getMessageComponentInteraction().getChannel().get());
                        }
                        case 10 -> {
                            quiz.yesAnswered.add(instance.getQuizManager().getAvailableQuiz("goida").getQuestions().get(10));
                            quiz.next(event.getMessageComponentInteraction().getMessage().getIdAsString(), event.getMessageComponentInteraction().getChannel().get());
                        }
                        case 11 -> {
                            quiz.yesAnswered.add(instance.getQuizManager().getAvailableQuiz("goida").getQuestions().get(11));
                            quiz.next(event.getMessageComponentInteraction().getMessage().getIdAsString(), event.getMessageComponentInteraction().getChannel().get());
                        }
                        case 12 -> {
                            quiz.yesAnswered.add(instance.getQuizManager().getAvailableQuiz("goida").getQuestions().get(12));
                            quiz.next(event.getMessageComponentInteraction().getMessage().getIdAsString(), event.getMessageComponentInteraction().getChannel().get());
                        }
                        case 13 -> {
                            quiz.yesAnswered.add(instance.getQuizManager().getAvailableQuiz("goida").getQuestions().get(13));
                            quiz.next(event.getMessageComponentInteraction().getMessage().getIdAsString(), event.getMessageComponentInteraction().getChannel().get());
                        }
                        case 14 -> {
                            quiz.yesAnswered.add(instance.getQuizManager().getAvailableQuiz("goida").getQuestions().get(14));
                            quiz.next(event.getMessageComponentInteraction().getMessage().getIdAsString(), event.getMessageComponentInteraction().getChannel().get());
                        }
                        case 15 -> {
                            quiz.yesAnswered.add(instance.getQuizManager().getAvailableQuiz("goida").getQuestions().get(15));
                            quiz.next(event.getMessageComponentInteraction().getMessage().getIdAsString(), event.getMessageComponentInteraction().getChannel().get());
                        }
                        case 16 -> {
                            quiz.yesAnswered.add(instance.getQuizManager().getAvailableQuiz("goida").getQuestions().get(16));
                            quiz.next(event.getMessageComponentInteraction().getMessage().getIdAsString(), event.getMessageComponentInteraction().getChannel().get());
                        }
                        case 17 -> {
                            quiz.yesAnswered.add(instance.getQuizManager().getAvailableQuiz("goida").getQuestions().get(17));
                            quiz.finish(event.getMessageComponentInteraction().getMessage().getIdAsString(), event.getMessageComponentInteraction().getChannel().get());
                            privateChannel.sendMessage("Rozwiązano test, wyniki zostały wysłane na kanale <#1222053317136617545>.");
                            try {
                                instance.getWebhookManager().sendGoidaResult(event.getMessageComponentInteraction().getUser(), instance.graphicsCreator.createQuizImage(event.getMessageComponentInteraction().getUser().getIdAsString(), "/home/rusekh/hexszon/goidownik.png", quiz.getPercentage()), quiz.getResult());
                                instance.quizManager.availableQuizes.remove(instance.quizManager.getUserQuiz(event.getMessageComponentInteraction().getUser().getIdAsString()));
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            return;
                        }
                    }
                }
                if (event.getMessageComponentInteraction().getMessage().isPrivateMessage() && instance.getQuizManager().getUserQuiz(event.getMessageComponentInteraction().getUser().getIdAsString()) != null && instance.getQuizManager().getUserQuiz(event.getMessageComponentInteraction().getUser().getIdAsString()).getQuizName().equalsIgnoreCase("banderowiec")) {
                    Quiz quiz = instance.getQuizManager().getUserQuiz(event.getMessageComponentInteraction().getUser().getIdAsString());
                    PrivateChannel privateChannel = event.getMessageComponentInteraction().getMessage().getPrivateChannel().get();
                    event.getMessageComponentInteraction().acknowledge();
                    switch (quiz.getCurrentStage()) {
                        case 0 -> {
                            quiz.yesAnswered.add(instance.getQuizManager().getAvailableQuiz("banderowiec").getQuestions().get(0));
                            quiz.next(event.getMessageComponentInteraction().getMessage().getIdAsString(), event.getMessageComponentInteraction().getChannel().get());
                        }
                        case 1 -> {
                            quiz.yesAnswered.add(instance.getQuizManager().getAvailableQuiz("banderowiec").getQuestions().get(1));
                            quiz.next(event.getMessageComponentInteraction().getMessage().getIdAsString(), event.getMessageComponentInteraction().getChannel().get());
                        }
                        case 2 -> {
                            quiz.yesAnswered.add(instance.getQuizManager().getAvailableQuiz("banderowiec").getQuestions().get(2));
                            quiz.next(event.getMessageComponentInteraction().getMessage().getIdAsString(), event.getMessageComponentInteraction().getChannel().get());
                        }
                        case 3 -> {
                            quiz.yesAnswered.add(instance.getQuizManager().getAvailableQuiz("banderowiec").getQuestions().get(3));
                            quiz.next(event.getMessageComponentInteraction().getMessage().getIdAsString(), event.getMessageComponentInteraction().getChannel().get());
                        }
                        case 4 -> {
                            quiz.yesAnswered.add(instance.getQuizManager().getAvailableQuiz("banderowiec").getQuestions().get(4));
                            quiz.next(event.getMessageComponentInteraction().getMessage().getIdAsString(), event.getMessageComponentInteraction().getChannel().get());
                        }
                        case 5 -> {
                            quiz.yesAnswered.add(instance.getQuizManager().getAvailableQuiz("banderowiec").getQuestions().get(5));
                            quiz.next(event.getMessageComponentInteraction().getMessage().getIdAsString(), event.getMessageComponentInteraction().getChannel().get());
                        }
                        case 6 -> {
                            quiz.yesAnswered.add(instance.getQuizManager().getAvailableQuiz("banderowiec").getQuestions().get(6));
                            quiz.next(event.getMessageComponentInteraction().getMessage().getIdAsString(), event.getMessageComponentInteraction().getChannel().get());
                        }
                        case 7 -> {
                            quiz.yesAnswered.add(instance.getQuizManager().getAvailableQuiz("banderowiec").getQuestions().get(7));
                            quiz.next(event.getMessageComponentInteraction().getMessage().getIdAsString(), event.getMessageComponentInteraction().getChannel().get());
                        }
                        case 8 -> {
                            quiz.yesAnswered.add(instance.getQuizManager().getAvailableQuiz("banderowiec").getQuestions().get(8));
                            quiz.next(event.getMessageComponentInteraction().getMessage().getIdAsString(), event.getMessageComponentInteraction().getChannel().get());
                        }
                        case 9 -> {
                            quiz.yesAnswered.add(instance.getQuizManager().getAvailableQuiz("banderowiec").getQuestions().get(9));
                            quiz.next(event.getMessageComponentInteraction().getMessage().getIdAsString(), event.getMessageComponentInteraction().getChannel().get());
                        }
                        case 10 -> {
                            quiz.yesAnswered.add(instance.getQuizManager().getAvailableQuiz("banderowiec").getQuestions().get(10));
                            quiz.next(event.getMessageComponentInteraction().getMessage().getIdAsString(), event.getMessageComponentInteraction().getChannel().get());
                        }
                        case 11 -> {
                            quiz.yesAnswered.add(instance.getQuizManager().getAvailableQuiz("banderowiec").getQuestions().get(11));
                            quiz.next(event.getMessageComponentInteraction().getMessage().getIdAsString(), event.getMessageComponentInteraction().getChannel().get());
                        }
                        case 12 -> {
                            quiz.yesAnswered.add(instance.getQuizManager().getAvailableQuiz("banderowiec").getQuestions().get(12));
                            quiz.next(event.getMessageComponentInteraction().getMessage().getIdAsString(), event.getMessageComponentInteraction().getChannel().get());
                        }
                        case 13 -> {
                            quiz.yesAnswered.add(instance.getQuizManager().getAvailableQuiz("banderowiec").getQuestions().get(13));
                            quiz.next(event.getMessageComponentInteraction().getMessage().getIdAsString(), event.getMessageComponentInteraction().getChannel().get());
                        }
                        case 14 -> {
                            quiz.finish(event.getMessageComponentInteraction().getMessage().getIdAsString(), event.getMessageComponentInteraction().getChannel().get());
                            privateChannel.sendMessage("Rozwiązano test, wyniki zostały wysłane na kanale <#1222469995162894337>.");
                            try {
                                instance.getWebhookManager().sendBanderaResult(event.getMessageComponentInteraction().getUser(), instance.graphicsCreator.createQuizImage(event.getMessageComponentInteraction().getUser().getIdAsString(), "/home/rusekh/hexszon/banderowiec.png", quiz.getPercentage()), quiz.getResult());
                                instance.quizManager.availableQuizes.remove(instance.quizManager.getUserQuiz(event.getMessageComponentInteraction().getUser().getIdAsString()));
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            return;
                        }
                    }
                }

                if (event.getMessageComponentInteraction().getMessage().isPrivateMessage() && instance.getQuizManager().getUserQuiz(event.getMessageComponentInteraction().getUser().getIdAsString()) != null && instance.getQuizManager().getUserQuiz(event.getMessageComponentInteraction().getUser().getIdAsString()).getQuizName().equalsIgnoreCase("narodowiec")) {
                    Quiz quiz = instance.getQuizManager().getUserQuiz(event.getMessageComponentInteraction().getUser().getIdAsString());
                    PrivateChannel privateChannel = event.getMessageComponentInteraction().getMessage().getPrivateChannel().get();
                    event.getMessageComponentInteraction().acknowledge();
                    switch (quiz.getCurrentStage()) {
                        case 0 -> {
                            quiz.yesAnswered.add(instance.getQuizManager().getAvailableQuiz("narodowiec").getQuestions().get(0));
                            quiz.next(event.getMessageComponentInteraction().getMessage().getIdAsString(), event.getMessageComponentInteraction().getChannel().get());
                        }
                        case 1 -> {
                            quiz.yesAnswered.add(instance.getQuizManager().getAvailableQuiz("narodowiec").getQuestions().get(1));
                            quiz.next(event.getMessageComponentInteraction().getMessage().getIdAsString(), event.getMessageComponentInteraction().getChannel().get());
                        }
                        case 2 -> {
                            quiz.yesAnswered.add(instance.getQuizManager().getAvailableQuiz("narodowiec").getQuestions().get(2));
                            quiz.next(event.getMessageComponentInteraction().getMessage().getIdAsString(), event.getMessageComponentInteraction().getChannel().get());
                        }
                        case 3 -> {
                            quiz.yesAnswered.add(instance.getQuizManager().getAvailableQuiz("narodowiec").getQuestions().get(3));
                            quiz.next(event.getMessageComponentInteraction().getMessage().getIdAsString(), event.getMessageComponentInteraction().getChannel().get());
                        }
                        case 4 -> {
                            quiz.yesAnswered.add(instance.getQuizManager().getAvailableQuiz("narodowiec").getQuestions().get(4));
                            quiz.next(event.getMessageComponentInteraction().getMessage().getIdAsString(), event.getMessageComponentInteraction().getChannel().get());
                        }
                        case 5 -> {
                            quiz.yesAnswered.add(instance.getQuizManager().getAvailableQuiz("narodowiec").getQuestions().get(5));
                            quiz.next(event.getMessageComponentInteraction().getMessage().getIdAsString(), event.getMessageComponentInteraction().getChannel().get());
                        }
                        case 6 -> {
                            quiz.yesAnswered.add(instance.getQuizManager().getAvailableQuiz("narodowiec").getQuestions().get(6));
                            quiz.next(event.getMessageComponentInteraction().getMessage().getIdAsString(), event.getMessageComponentInteraction().getChannel().get());
                        }
                        case 7 -> {
                            quiz.next(event.getMessageComponentInteraction().getMessage().getIdAsString(), event.getMessageComponentInteraction().getChannel().get());
                        }
                        case 8 -> {
                            quiz.yesAnswered.add(instance.getQuizManager().getAvailableQuiz("narodowiec").getQuestions().get(8));
                            quiz.next(event.getMessageComponentInteraction().getMessage().getIdAsString(), event.getMessageComponentInteraction().getChannel().get());
                        }
                        case 9 -> {
                            quiz.yesAnswered.add(instance.getQuizManager().getAvailableQuiz("narodowiec").getQuestions().get(9));
                            quiz.next(event.getMessageComponentInteraction().getMessage().getIdAsString(), event.getMessageComponentInteraction().getChannel().get());
                        }
                        case 10 -> {
                            quiz.yesAnswered.add(instance.getQuizManager().getAvailableQuiz("narodowiec").getQuestions().get(10));
                            quiz.next(event.getMessageComponentInteraction().getMessage().getIdAsString(), event.getMessageComponentInteraction().getChannel().get());
                        }
                        case 11 -> {
                            quiz.yesAnswered.add(instance.getQuizManager().getAvailableQuiz("narodowiec").getQuestions().get(11));
                            quiz.next(event.getMessageComponentInteraction().getMessage().getIdAsString(), event.getMessageComponentInteraction().getChannel().get());
                        }
                        case 12 -> {
                            quiz.yesAnswered.add(instance.getQuizManager().getAvailableQuiz("narodowiec").getQuestions().get(12));
                            quiz.next(event.getMessageComponentInteraction().getMessage().getIdAsString(), event.getMessageComponentInteraction().getChannel().get());
                        }
                        case 13 -> {
                            quiz.yesAnswered.add(instance.getQuizManager().getAvailableQuiz("narodowiec").getQuestions().get(13));
                            quiz.next(event.getMessageComponentInteraction().getMessage().getIdAsString(), event.getMessageComponentInteraction().getChannel().get());
                        }
                        case 14 -> {
                            quiz.finish(event.getMessageComponentInteraction().getMessage().getIdAsString(), event.getMessageComponentInteraction().getChannel().get());
                            privateChannel.sendMessage("Rozwiązano test, wyniki zostały wysłane na kanale <#1229568592199159892>.");
                            try {
                                instance.getWebhookManager().sendNarodowiecResult(event.getMessageComponentInteraction().getUser(), instance.graphicsCreator.createQuizImage2(event.getMessageComponentInteraction().getUser().getIdAsString(), "http://hexlin.rsbmw.pl/narodowiec.png", quiz.getPercentage()), quiz.getResult());
                                instance.quizManager.availableQuizes.remove(instance.quizManager.getUserQuiz(event.getMessageComponentInteraction().getUser().getIdAsString()));
                            } catch (IOException e) {
                                privateChannel.sendMessage("Wystąpił błąd: " + e.getMessage());
                            }
                            return;
                        }
                    }
                }
            }
            case "quiz-no" -> {
                if (event.getMessageComponentInteraction().getMessage().isPrivateMessage() && instance.getQuizManager().getUserQuiz(event.getMessageComponentInteraction().getUser().getIdAsString()) != null && instance.getQuizManager().getUserQuiz(event.getMessageComponentInteraction().getUser().getIdAsString()).getQuizName().equalsIgnoreCase("banderowiec")) {
                    event.getMessageComponentInteraction().acknowledge();
                    Quiz quiz = instance.getQuizManager().getUserQuiz(event.getMessageComponentInteraction().getUser().getIdAsString());
                    PrivateChannel privateChannel = event.getMessageComponentInteraction().getMessage().getPrivateChannel().get();
                    event.getMessageComponentInteraction().acknowledge();
                    switch (quiz.getCurrentStage()) {
                        case 0, 3, 1, 2, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13 -> {
                            quiz.next(event.getMessageComponentInteraction().getMessage().getIdAsString(), event.getMessageComponentInteraction().getChannel().get());
                        }
                        case 14 -> {
                            quiz.yesAnswered.add(instance.getQuizManager().getAvailableQuiz("banderowiec").getQuestions().get(14));
                            quiz.finish(event.getMessageComponentInteraction().getMessage().getIdAsString(), event.getMessageComponentInteraction().getChannel().get());
                            privateChannel.sendMessage("Rozwiązano test, wyniki zostały wysłane na kanale <#1222469995162894337>.");
                            try {
                                instance.getWebhookManager().sendBanderaResult(event.getMessageComponentInteraction().getUser(), instance.graphicsCreator.createQuizImage(event.getMessageComponentInteraction().getUser().getIdAsString(), "/home/rusekh/hexszon/banderowiec.png", quiz.getPercentage()), quiz.getResult());
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            instance.quizManager.availableQuizes.remove(instance.quizManager.getUserQuiz(event.getMessageComponentInteraction().getUser().getIdAsString()));
                            return;
                        }
                    }
                }
                if (event.getMessageComponentInteraction().getMessage().isPrivateMessage() && instance.getQuizManager().getUserQuiz(event.getMessageComponentInteraction().getUser().getIdAsString()) != null && instance.getQuizManager().getUserQuiz(event.getMessageComponentInteraction().getUser().getIdAsString()).getQuizName().equalsIgnoreCase("goida")) {
                    Quiz quiz = instance.getQuizManager().getUserQuiz(event.getMessageComponentInteraction().getUser().getIdAsString());
                    PrivateChannel privateChannel = event.getMessageComponentInteraction().getMessage().getPrivateChannel().get();
                    event.getMessageComponentInteraction().acknowledge();
                    switch (quiz.getCurrentStage()) {
                        case 0, 3, 1, 2, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 -> {
                            quiz.next(event.getMessageComponentInteraction().getMessage().getIdAsString(), event.getMessageComponentInteraction().getChannel().get());
                        }
                        case 17 -> {
                            quiz.yesAnswered.add(instance.getQuizManager().getAvailableQuiz("goida").getQuestions().get(14));
                            quiz.finish(event.getMessageComponentInteraction().getMessage().getIdAsString(), event.getMessageComponentInteraction().getChannel().get());
                            privateChannel.sendMessage("Rozwiązano test, wyniki zostały wysłane na kanale <#1222053317136617545>.");
                            try {
                                instance.getWebhookManager().sendGoidaResult(event.getMessageComponentInteraction().getUser(), instance.graphicsCreator.createQuizImage(event.getMessageComponentInteraction().getUser().getIdAsString(), "/home/rusekh/hexszon/goidownik.png", quiz.getPercentage()), quiz.getResult());
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            instance.quizManager.availableQuizes.remove(instance.quizManager.getUserQuiz(event.getMessageComponentInteraction().getUser().getIdAsString()));
                            return;
                        }
                    }
                }
                if (event.getMessageComponentInteraction().getMessage().isPrivateMessage() && instance.getQuizManager().getUserQuiz(event.getMessageComponentInteraction().getUser().getIdAsString()) != null && instance.getQuizManager().getUserQuiz(event.getMessageComponentInteraction().getUser().getIdAsString()).getQuizName().equalsIgnoreCase("narodowiec")) {
                    Quiz quiz = instance.getQuizManager().getUserQuiz(event.getMessageComponentInteraction().getUser().getIdAsString());
                    PrivateChannel privateChannel = event.getMessageComponentInteraction().getMessage().getPrivateChannel().get();
                    event.getMessageComponentInteraction().acknowledge();
                    switch (quiz.getCurrentStage()) {
                        case 0, 3, 1, 2, 4, 5, 6, 8, 9, 10, 11, 12, 13 -> {
                            quiz.next(event.getMessageComponentInteraction().getMessage().getIdAsString(), event.getMessageComponentInteraction().getChannel().get());
                        }
                        case 7 -> {
                            quiz.yesAnswered.add(instance.getQuizManager().getAvailableQuiz("narodowiec").getQuestions().get(7));
                            quiz.next(event.getMessageComponentInteraction().getMessage().getIdAsString(), event.getMessageComponentInteraction().getChannel().get());
                        }
                        case 14 -> {
                            quiz.finish(event.getMessageComponentInteraction().getMessage().getIdAsString(), event.getMessageComponentInteraction().getChannel().get());
                            privateChannel.sendMessage("Rozwiązano test, wyniki zostały wysłane na kanale <#1229568592199159892>.");
                            try {
                                instance.getWebhookManager().sendNarodowiecResult(event.getMessageComponentInteraction().getUser(), instance.graphicsCreator.createQuizImage2(event.getMessageComponentInteraction().getUser().getIdAsString(), "http://hexlin.rsbmw.pl/narodowiec.png", quiz.getPercentage()), quiz.getResult());
                            } catch (IOException e) {
                                privateChannel.sendMessage("Wystąpił błąd: " + e.getMessage());
                            }
                            instance.quizManager.availableQuizes.remove(instance.quizManager.getUserQuiz(event.getMessageComponentInteraction().getUser().getIdAsString()));
                            return;
                        }
                    }
                }
            }

            case "question-start" -> {
                event.getInteraction().respondWithModal("questionmodal", "Tekst pytania.",
                        ActionRow.of(TextInput.create(TextInputStyle.SHORT, "userid", "Tekst pytania")));
            }
            case "banderowiecquiz" -> {
                if (instance.getQuizManager().getUserQuiz(event.getInteraction().getUser().getIdAsString()) != null) {
                    event.getInteraction().getUser().sendMessage("Już posiadasz odpalony quiz, najpierw go ukończ zanim zaczniesz nowy.");
                    event.getMessageComponentInteraction().acknowledge();
                    return;
                }
                instance.quizManager.addAvailableQuiz(new Quiz(event.getInteraction().getUser().getIdAsString(), "banderowiec", 14,
                        List.of("Czy popierasz LGBT?",
                                "Czy Ukraina powinna wstąpić do NATO i EU?",
                                "Czy Przemyśl powinien być Ukraińskim terytorium?",
                                "Czy Stefan Bandera jest wielkim bohaterem walczącym o niepodległość (Zabijając Polaków)?",
                                "Czy Polacy są sługami Ukraińskiego narodu?",
                                "Czy terytorium Noworosji powinno należeć do Ukrainy?",
                                "Czy obwody Rostowu Nad Donem i Krasnodaru powinny należeć do Ukrainy?",
                                "Czy Federacja Rosyjska nie powinna istnieć albo być rządem marionetkowym Ukrainy?",
                                "Czy Krym powinien być Ukrainskim terytorium?",
                                "Czy zagłosowałbyś na partie lewicowe w Polsce?",
                                "Czy uważasz że Ukraincy to nasi bracia?",
                                "Czy uważasz że Ukraina wygra wojnę?",
                                "Czy uważasz że to fałszywa informacja żę Rosja przyjeła najwięcej Ukraińskich imigrantów niż jakikolwiek Europejski kraj (1. Rosja 2. Niemcy 3. Polska)?",
                                "Czy kiedykolwiek brałeś udział w gejowskim seksie?",
                                "Czy Ukraińcy powinni oddac nam Lwów?"
                        ), "Jesteś prawdziwym banderowcem, popierasz wyższość Ukrainy nad Polakami i Rosjanami oraz posiadasz progresywne poglądy.", "Jeszcze troche brakuje Ci do zostania prawdziwym banderowcem, z większością ideii banderyzmu się zgadzasz oraz prawdopodobnei chciałbyś być Ukraińcem a nie Polakiem", "Popeirasz fundemantalne idee banderyzmu, chociaż prawdopodobnie po prostu popierasz zwycięstwo Ukrainy a nie sam banderyzm.", "Nie jesteś banderowcem chociaż lubisz Ukrainę.", "Nie jesteś banderowcem i prawdopodobnie chciałbyś aby Rosja odniosła zwyciestwo albo żebyśmy zajeli lwów lub po prostu jesteś neutralny.", instance));
                int questionSize = instance.getQuizManager().getAvailableQuiz("banderowiec").getQuestions().size() - 1;
                new MessageBuilder().setContent("Na pytania odpowiadaj Tak lub Nie. \n" + instance.getQuizManager().getAvailableQuiz("banderowiec").getQuestions().get(0) + " | 1/" + questionSize)
                        .addComponents(ActionRow.of(Button.success("quiz-yes", "✅ Tak")), ActionRow.of(Button.danger("quiz-no", "❎ Nie")))
                        .send(event.getMessageComponentInteraction().getUser());
                event.getMessageComponentInteraction().acknowledge();
            }
            case "goidaquiz" -> {
                if (instance.getQuizManager().getUserQuiz(event.getInteraction().getUser().getIdAsString()) != null) {
                    event.getInteraction().getUser().sendMessage("Już posiadasz odpalony quiz, najpierw go ukończ zanim zaczniesz nowy.");
                    event.getMessageComponentInteraction().acknowledge();
                    return;
                }
                instance.quizManager.addAvailableQuiz(new Quiz(event.getInteraction().getUser().getIdAsString(), "goida", 17, List.of("Czy zagłosowałbyś na Putina jeżeli byś był obywatelem Federacji Rosyjskiej?",
                        "Czy popierasz specjalną operację militarną na Ukrainie?",
                        "Czy uważasz kulturę zachodu za szkodliwą dla Polski?",
                        "Czy uważasz że Ukraina jest rządzona albo jest tam wysoka ilość banderowców?",
                        "Czy Polska powinna dołączyć do CSTO (Rosyjskie NATO)?",
                        "Czy Polska powinna naprawić relacje z Rosją?",
                        "Czy powinniśmy pomóc Rosji w operacji na Ukrainie biorąc w niej udział i zajmując Lwów?",
                        "Czy uważasz Rosjanów jako bratni dla nas naród?",
                        "Czy chciałbyś kiedyś odwiedzić Rosję lub (albo i) uczysz się języka Rosyjskiego?",
                        "Czy uważasz że Polska nie powinna przyjmować Ukrainców (albo przyjmować tylko wtedy gdy mają zamiar się zasymilować)?",
                        "Czy głosujesz na partie prawicowe takie jak Konfederacja?",
                        "Czy uważasz że nieheteroseksualność to coś co trzeba zwalczać?",
                        "Czy wierzysz w Chrześcijańskiego Boga?",
                        "Czy Rosja powinna być 1 światowym mocarstwem zamiast USA?",
                        "Czy słuchasz Rosyjskiej muzyki?",
                        "Czy autorytaryzm jest lepszy od demokracji?",
                        "Czy chciałbyś się napić Rosyjkich alkoholi?",
                        "Czy uważasz że region Noworosji (południowy-wschód ukrainy) to powinno być Rosyjskie terytorium (Większość populacji to Rosjanie)."
                ), "Jesteś certyfikowanym Goidownikiem, popierasz prezydenta Władimira Putina, specjalną operację militarną, ideologię Z, czujesz większy patriotyzm do Rosji niż kraju z którego jesteś i prawdopodobnie masz wschodnie pochodzenie.", "Popierasz większość ideii Goidowników oraz lubisz wschód. Jesteś o włos do zostania prawdziwym Goidownikiem.", "Popierasz tylko fundamentalne idee wschodu, możliwe że jesteś sceptyczny co do Putina i raczej wolisz zbalansowaną między wschodem a zachodem politykę i relacje Polski z Rosją oraz pokój na Ukrainie.", "Niestety do prawdziwego Goidownika Ci daleko, wyrażasz bardzo małe poparcie co do polityki Rosji i raczej jesteś pro-Ukrainski.", "Nie wyrażasz prawie żadnego poparcia do ideii wschodu i polityki Rosji, jesteś pro-zachodni i pro-Ukraiński i chciałbyś aby Rosja była rządzona przez wolnościowych polityków.", instance));
                int questionSize = instance.getQuizManager().getAvailableQuiz("goida").getQuestions().size() - 1;
                new MessageBuilder().setContent("Na pytania odpowiadaj Tak lub Nie. \n" + instance.getQuizManager().getAvailableQuiz("goida").getQuestions().get(0) + " | 1/" + questionSize)
                        .addComponents(ActionRow.of(Button.success("quiz-yes", "✅ Tak")), ActionRow.of(Button.danger("quiz-no", "❎ Nie")))
                        .send(event.getMessageComponentInteraction().getUser());
                event.getMessageComponentInteraction().acknowledge();
            }

            case "narodowiecquiz" -> {
                if (instance.getQuizManager().getUserQuiz(event.getInteraction().getUser().getIdAsString()) != null) {
                    event.getInteraction().getUser().sendMessage("Już posiadasz odpalony quiz, najpierw go ukończ zanim zaczniesz nowy.");
                    event.getMessageComponentInteraction().acknowledge();
                    return;
                }
                instance.quizManager.addAvailableQuiz(new Quiz(event.getInteraction().getUser().getIdAsString(), "narodowiec", 12,
                        List.of("Czy naród to najwyższa wartość?",
                                "Czy chciałbyś rozbioru Ukrainy?",
                                "A femboya?",
                                "Czy chciałbyś naprawienia relacji Polski z Rosją?",
                                "Czy Polska powinna odzyskać kresy?",
                                "Czy Polska powinna opuścic UE?",
                                "Czy popierasz faszystowskie idee?",
                                "Czy Polska powinna wpuszczać imigrantów?",
                                "Czy uważasz że 5G jest szkodliwe?",
                                "Czy uważasz że wojny są potrzebne?",
                                "Czy projekt międzymorza to przeznaczenie dla Polski i powinien zostać on zrealizowany?",
                                "Czy użyłbyś przemocy na ulicy gdybyś napotkał irytującego lewaka?",
                                "Czy Wilno i Lwów to są Polskie miasta?",
                                "Czy powinniśmy kultywować nasze tradycje i kulturę?",
                                "Czy powinniśmy chronić naszych narodowych interesów zamiast patrzeć na interes innych państw?"
                        ),
                        "Jesteś prawdziwym narodowcem bez dwóch zdań.", "Nie zostało ci dużo do zostania prawdziwym narodowcem, popierasz większość ich postulatów.", "Zgadasz się w większości z postulatami narodowców ale nadal masz do tego pewien dystans.", "Popierasz może tylko kilka podstawowych ideii, do prawdziwego narodowca Ci daleko.", "Nie popierasz żadnych pomysłów narodowców i prawdopodobnie jesteś kosmopolitą i popierasz internacjonalizm.", instance));
                int questionSize = instance.getQuizManager().getAvailableQuiz("narodowiec").getQuestions().size() - 1;
                new MessageBuilder().setContent("Na pytania odpowiadaj Tak lub Nie. \n" + instance.getQuizManager().getAvailableQuiz("narodowiec").getQuestions().get(0) + " | 1/" + questionSize)
                        .addComponents(ActionRow.of(Button.success("quiz-yes", "✅ Tak")), ActionRow.of(Button.danger("quiz-no", "❎ Nie")))
                        .send(event.getMessageComponentInteraction().getUser());
                event.getMessageComponentInteraction().acknowledge();
            }
            case "creator" -> {
                if (event.getInteraction().getUser().getIdAsString().equalsIgnoreCase("960579015054360616")) {
                    event.getInteraction().getUser().sendMessage("Przykry jesteś w chuj.");
                    return;
                }
                if (instance.getGfxProcessManager().getGfxProcess(event.getInteraction().getUser().getIdAsString()) != null) {
                    event.getInteraction().getUser().sendMessage("Już posiadasz odpalony proces tworzenia grafikii, wpisz \"anuluj\" aby anulować.");
                    return;
                }
                instance.getGfxProcessManager().addGfx(new GfxProcess(event.getInteraction().getUser().getIdAsString(), GfxStage.STAGE_1, null, null, null, null, null, null, null, null, null, null));
                event.getInteraction().getUser().sendMessage("Tworzenie twojej grafikii: podaj link do flagi twojego państwa (Pamiętaj że wszystkie linki muszą mieć końcówkę .png albo .jpg, zalcane jest wrzucenie na zapodaj.net i kliknięciem prawym i \"kopiuj adres obrazu\" bo zwykły link nie zadziała. Unikaj serwisu imgur ponieważ bot nie może odczytywać plików z tej strony, wina jest po ich stronie.)");
                event.getMessageComponentInteraction().acknowledge();
            }
            case "shop-1" -> {
                event.getInteraction().respondWithModal("mutemodal1", "ID Użytkownika.",
                        ActionRow.of(TextInput.create(TextInputStyle.SHORT, "userid", "ID Użytkownika")));
            }
            case "shop-2" -> {
                event.getInteraction().respondWithModal("mutemodal2", "ID Użytkownika",
                        ActionRow.of(TextInput.create(TextInputStyle.SHORT, "userid2", "ID Użytkownika")));
            }
            case "shop-3" -> {
                event.getInteraction().respondWithModal("rolemodal", "Nazwa roli.",
                        ActionRow.of(TextInput.create(TextInputStyle.SHORT, "rolename", "Nazwa twojej roli")), ActionRow.of(TextInput.create(TextInputStyle.SHORT, "hexcolor", "Kolor #HEX")));
            }
            case "representative" -> {
                event.getInteraction().respondWithModal("representantivemodal", "Panel Propozycji",
                        ActionRow.of(TextInput.create(TextInputStyle.PARAGRAPH, "propozycja", "Opisz swoją propozycję.")));
            }
            case "question-p" -> {
                Question question = instance.getQuestionManager().getQuestion(event.getMessageComponentInteraction().getMessage().getIdAsString());
                User questionAuthor = instance.api.getUserById(question.getUserId()).join();

                if (question.getVoteUserById(event.getInteraction().getUser().getIdAsString()) != null) {
                    event.getMessageComponentInteraction().createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("Nie możesz ponownie oddać swojego głosu.").respond();
                    return;
                }

                question.addFor(1);
                question.addVotedUser(event.getInteraction().getUser());
                instance.questionManager.getQuestion(event.getMessageComponentInteraction().getMessage().getIdAsString()).save();

                EmbedBuilder questionEmbed = new EmbedBuilder()
                        .setTitle("**❓ ● Użytkownik " + questionAuthor.getName() + " ogłosił pytanie dnia**")
                        .setDescription(question.content)
                        .setColor(Color.decode("#B32DFF"))
                        .setImage("http://hexlin.rsbmw.pl/pytanie.png")
                        .setFooter(event.getInteraction().getServer().get().getName() + " | By Hexlin")
                        .setTimestamp(OffsetDateTime.now().toInstant());


                instance.api.getMessageById(question.getId(), instance.api.getTextChannelById("1218238831392723115").get()).thenAccept(message -> {
                    message.createUpdater()
                            .removeAllEmbeds()
                            .removeAllComponents()
                            .setEmbed(questionEmbed)
                            .addComponents(ActionRow.of(org.javacord.api.entity.message.component.Button.secondary("question-p", "✔\uFE0F Tak (" + question._for + ")"), Button.secondary("question-n", "❌ Nie (" + question.against + ")"))).applyChanges();

                    event.getMessageComponentInteraction().acknowledge();
                });
            }

            case "question-n" -> {
                Question question = instance.getQuestionManager().getQuestion(event.getMessageComponentInteraction().getMessage().getIdAsString());
                User questionAuthor = instance.api.getUserById(question.getUserId()).join();

                if (question.getVoteUserById(event.getInteraction().getUser().getIdAsString()) != null) {
                    event.getMessageComponentInteraction().createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("Nie możesz ponownie oddać swojego głosu.").respond();
                    return;
                }

                question.addAgainst(1);
                question.addVotedUser(event.getInteraction().getUser());
                instance.questionManager.getQuestion(event.getMessageComponentInteraction().getMessage().getIdAsString()).save();

                EmbedBuilder questionEmbed = new EmbedBuilder()
                        .setTitle("**❓ ● Użytkownik " + questionAuthor.getName() + " ogłosił pytanie dnia**")
                        .setDescription(question.content)
                        .setColor(Color.decode("#B32DFF"))
                        .setImage("http://hexlin.rsbmw.pl/pytanie.png")
                        .setFooter(event.getInteraction().getServer().get().getName() + " | By Hexlin")
                        .setTimestamp(OffsetDateTime.now().toInstant());


                instance.api.getMessageById(question.getId(), instance.api.getTextChannelById("1218238831392723115").get()).thenAccept(message -> {
                    message.createUpdater()
                            .removeAllEmbeds()
                            .removeAllComponents()
                            .setEmbed(questionEmbed)
                            .addComponents(ActionRow.of(org.javacord.api.entity.message.component.Button.secondary("question-p", "✔\uFE0F Tak (" + question._for + ")"), Button.secondary("question-n", "❌ Nie (" + question.against + ")"))).applyChanges();

                    event.getMessageComponentInteraction().acknowledge();
                });
            }

            case "p" -> {
                Suggestion suggestion = instance.getSuggestionManager().getSuggestion(event.getMessageComponentInteraction().getMessage().getIdAsString());
                User suggestionAuthor = instance.api.getUserById(suggestion.getUserId()).join();

                if (suggestion.isFinished()) {
                    event.getMessageComponentInteraction().createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("To głosowanie się już zakończyło.").respond();
                    return;
                }

                if (suggestion.getVoteUserById(event.getInteraction().getUser().getIdAsString()) != null) {
                    event.getMessageComponentInteraction().createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("Nie możesz ponownie oddać swojego głosu.").respond();
                    return;
                }

                suggestion.addFor(1);
                suggestion.addVotedUser(event.getInteraction().getUser());
                System.out.println(instance.suggestionManager.suggestionMap);
                instance.suggestionManager.getSuggestion(event.getMessageComponentInteraction().getMessage().getIdAsString()).save();

                EmbedBuilder suggestionEmbed = new EmbedBuilder()
                        .setTitle("**\uD83C\uDF03 Reprezentant " + suggestionAuthor.getName() + " wnosi do Dumy PSH nową propozycję.**")
                        .addField("\uD83D\uDDFD Ustawa wniesiona przez Reprezentanta Dumy przewiduje: ", suggestion.content)
                        .setImage("https://i.imgur.com/jS8Rar3.png")
                        .setDescription("⏰ Głosowanie dobiega końca " + suggestion.isGoingToExpire + ".")
                        .setColor(Color.decode("#00F2FF"))
                        .setFooter(event.getInteraction().getServer().get().getName() + " | By Hexlin", "https://i.imgur.com/1HhOGue.png");

                instance.api.getMessageById(suggestion.getId(), instance.api.getTextChannelById("1206714282809626684").get()).thenAccept(message -> {
                    message.createUpdater()
                            .removeAllEmbeds()
                            .removeAllComponents()
                            .setEmbed(suggestionEmbed)
                            .addComponents(ActionRow.of(org.javacord.api.entity.message.component.Button.secondary("p", "✔\uFE0F Popieram (" + suggestion._for + ")"), Button.secondary("n", "❌ Sprzeciw (" + suggestion.against + ")"))).applyChanges();

                    event.getMessageComponentInteraction().acknowledge();
                });
            }

            case "n" -> {
                Suggestion suggestion = instance.getSuggestionManager().getSuggestion(event.getMessageComponentInteraction().getMessage().getIdAsString());
                User suggestionAuthor = instance.api.getUserById(suggestion.getUserId()).join();

                if (suggestion.isFinished()) {
                    event.getMessageComponentInteraction().createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("To głosowanie się już zakończyło.").respond();
                    return;
                }

                if (suggestion.getVoteUserById(event.getInteraction().getUser().getIdAsString()) != null) {
                    event.getMessageComponentInteraction().createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("Nie możesz ponownie oddać swojego głosu.").respond();
                    return;
                }

                suggestion.addAgainst(1);
                suggestion.addVotedUser(event.getInteraction().getUser());
                instance.suggestionManager.getSuggestion(event.getMessageComponentInteraction().getMessage().getIdAsString()).save();

                EmbedBuilder suggestionEmbed = new EmbedBuilder()
                        .setTitle("**\uD83C\uDF03 ● Reprezentant " + suggestionAuthor.getName() + " wnosi do Dumy PSH nową propozycję.**")
                        .addField("\uD83D\uDDFD Ustawa wniesiona przez Reprezentanta Dumy przewiduje: ", suggestion.content)
                        .setDescription("⏰ Głosowanie dobiega końca " + suggestion.isGoingToExpire + ".")
                        .setImage("https://i.imgur.com/jS8Rar3.png")
                        .setColor(Color.decode("#00F2FF"))
                        .setFooter(event.getInteraction().getServer().get().getName() + " | By Hexlin", "https://i.imgur.com/1HhOGue.png");

                instance.api.getMessageById(suggestion.getId(), instance.api.getTextChannelById("1206714282809626684").get()).thenAccept(message -> {
                    message.createUpdater()
                            .removeAllEmbeds()
                            .removeAllComponents()
                            .setEmbed(suggestionEmbed)
                            .addComponents(ActionRow.of(org.javacord.api.entity.message.component.Button.secondary("p", "✔\uFE0F Popieram (" + suggestion._for + ")"), Button.secondary("n", "❌ Sprzeciw (" + suggestion.against + ")"))).applyChanges();

                    event.getMessageComponentInteraction().acknowledge();
                });
            }

            case "representative-elections" -> {
                if (event.getInteraction().getUser().getIdAsString().equalsIgnoreCase("960579015054360616")) {
                    event.getInteraction().getUser().sendMessage("Przykry jesteś w chuj.");
                    return;
                }
                LocalDateTime currentTime = LocalDateTime.now();
                LocalDateTime expiringTime = currentTime.plusMinutes(1);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm");
                String formattedTime = formatter.format(expiringTime);

                EmbedBuilder suggestionEmbed = new EmbedBuilder()
                        .setTitle("**Zostały otwarte wybory na stanowisko Reprezentanta Dumy**")
                        .addField("\uD83D\uDDFD Lista obecnych kandydatów:", "Brak")
                        .setDescription("⏰ Wybory dobiegają końca: **" + formattedTime + "." + "**\n\uD83D\uDCBB Dowiedz się więcej o możliwościach posady Reprezentanta Dumy na <#1206713642381353070>. \n\uD83D\uDCCB Aby zostać kandydatem musisz mieć conajmniej 100 wiadomości na serwerze.")
                        .setColor(Color.decode("#00F2FF"))
                        .setThumbnail("https://i.imgur.com/XuIIA1v.png")
                        .setFooter(event.getInteraction().getServer().get().getName() + " | By Hexlin", "https://i.imgur.com/jS8Rar3.png");

                instance.api.getTextChannelById("1206713642381353070").get().sendMessage(suggestionEmbed).thenAccept(message -> {
                    instance.representativeElectionManager.startElections(message.getIdAsString());

                    message.createUpdater()
                            .removeAllEmbeds()
                            .removeAllComponents()
                            .setEmbed(suggestionEmbed)
                            .addComponents(ActionRow.of(
                                    Button.secondary("first_c", "1\uFE0F⃣"),
                                    Button.secondary("second_c", "2\uFE0F⃣"),
                                    Button.secondary("third_c", "3\uFE0F⃣"),
                                    Button.secondary("fourth_c", "4\uFE0F⃣"),
                                    Button.secondary("fifth_c", "\uD83D\uDDF3\uFE0F Zostań kandydatem.")
                            )).applyChanges();

                    instance.representativeElectionManager.electionMap.values().forEach(Election::save);
                    event.getMessageComponentInteraction().acknowledge();
                });
            }

            case "first_c" -> {
                User user = event.getInteraction().getUser();
                Election election = instance.representativeElectionManager.getElection(event.getMessageComponentInteraction().getMessage().getIdAsString());

                if (!election.isOngoing) {
                    event.getMessageComponentInteraction().createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("Te wybory zostały już zakończone.").respond();
                    return;
                }

                if (election.getVoteUserById(user.getIdAsString()) != null) {
                    event.getMessageComponentInteraction().createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("Nie możesz ponownie oddać swojego głosu.").respond();
                    return;
                }

                if (election.getCandidate(user.getIdAsString()) != null) {
                    event.getMessageComponentInteraction().createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("Nie możesz oddać głosu będąc kandydatem.").respond();
                    return;
                }

                election.candidates.get(0).setVotes(1);
                election.addVotedUser(user);
                instance.representativeElectionManager.electionMap.values().forEach(Election::save);

                EmbedBuilder suggestionEmbed = new EmbedBuilder()
                        .setTitle("**Zostały otwarte wybory na stanowisko Reprezentanta Dumy**")
                        .setDescription("⏰ Wybory dobiegają końca: **" + election.isGoingToEnd.replace("T", "-") + "." + "**\n\uD83D\uDCBB Dowiedz się więcej o możliwościach posady Reprezentanta Dumy na <#1206713642381353070>. \n\uD83D\uDCCB Aby zostać kandydatem musisz mieć conajmniej 100 wiadomości na serwerze.")
                        .setColor(Color.decode("#00F2FF"))
                        .setThumbnail("https://i.imgur.com/XuIIA1v.png")
                        .setFooter(event.getInteraction().getServer().get().getName() + " | By Hexlin", "https://i.imgur.com/1HhOGue.png");

                buildCandidatesField(suggestionEmbed, election.candidates);

                instance.api.getMessageById(election.electionMessageId, instance.api.getTextChannelById("1206713642381353070").get()).thenAccept(message -> {
                    message.createUpdater()
                            .removeAllEmbeds()
                            .setEmbed(suggestionEmbed).applyChanges();
                    event.getMessageComponentInteraction().acknowledge();
                });
            }

            case "second_c" -> {
                User user = event.getInteraction().getUser();
                Election election = instance.representativeElectionManager.getElection(event.getMessageComponentInteraction().getMessage().getIdAsString());

                if (!election.isOngoing) {
                    event.getMessageComponentInteraction().createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("Te wybory zostały już zakończone.").respond();
                    return;
                }

                if (election.getVoteUserById(user.getIdAsString()) != null) {
                    event.getMessageComponentInteraction().createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("Nie możesz ponownie oddać swojego głosu.").respond();
                    return;
                }

                if (election.getCandidate(user.getIdAsString()) != null) {
                    event.getMessageComponentInteraction().createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("Nie możesz oddać głosu będąc kandydatem.").respond();
                    return;
                }

                election.candidates.get(1).setVotes(1);
                election.addVotedUser(user);
                instance.representativeElectionManager.electionMap.values().forEach(Election::save);

                EmbedBuilder suggestionEmbed = new EmbedBuilder()
                        .setTitle("**Zostały otwarte wybory na stanowisko Reprezentanta Dumy**")
                        .setDescription("⏰ Wybory dobiegają końca: **" + election.isGoingToEnd.replace("T", "-") + "." + "**\n\uD83D\uDCBB Dowiedz się więcej o możliwościach posady Reprezentanta Dumy na <#1206713642381353070>. \n\uD83D\uDCCB Aby zostać kandydatem musisz mieć conajmniej 100 wiadomości na serwerze.")
                        .setColor(Color.decode("#00F2FF"))
                        .setThumbnail("https://i.imgur.com/XuIIA1v.png")
                        .setFooter(event.getInteraction().getServer().get().getName() + " | By Hexlin", "https://i.imgur.com/1HhOGue.png");

                buildCandidatesField(suggestionEmbed, election.candidates);

                instance.api.getMessageById(election.electionMessageId, instance.api.getTextChannelById("1206713642381353070").get()).thenAccept(message -> {
                    message.createUpdater()
                            .removeAllEmbeds()
                            .setEmbed(suggestionEmbed).applyChanges();
                    event.getMessageComponentInteraction().acknowledge();
                });
            }

            case "third_c" -> {
                User user = event.getInteraction().getUser();
                Election election = instance.representativeElectionManager.getElection(event.getMessageComponentInteraction().getMessage().getIdAsString());

                if (!election.isOngoing) {
                    event.getMessageComponentInteraction().createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("Te wybory zostały już zakończone.").respond();
                    return;
                }

                if (election.getVoteUserById(user.getIdAsString()) != null) {
                    event.getMessageComponentInteraction().createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("Nie możesz ponownie oddać swojego głosu.").respond();
                    return;
                }

                if (election.getCandidate(user.getIdAsString()) != null) {
                    event.getMessageComponentInteraction().createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("Nie możesz oddać głosu będąc kandydatem.").respond();
                    return;
                }

                election.candidates.get(2).setVotes(1);
                election.addVotedUser(user);
                instance.representativeElectionManager.electionMap.values().forEach(Election::save);

                EmbedBuilder suggestionEmbed = new EmbedBuilder()
                        .setTitle("**Zostały otwarte wybory na stanowisko Reprezentanta Dumy**")
                        .setDescription("⏰ Wybory dobiegają końca: **" + election.isGoingToEnd.replace("T", "-") + "." + "**\n\uD83D\uDCBB Dowiedz się więcej o możliwościach posady Reprezentanta Dumy na <#1206713642381353070>. \n\uD83D\uDCCB Aby zostać kandydatem musisz mieć conajmniej 100 wiadomości na serwerze.")
                        .setColor(Color.decode("#00F2FF"))
                        .setThumbnail("https://i.imgur.com/XuIIA1v.png")
                        .setFooter(event.getInteraction().getServer().get().getName() + " | By Hexlin", "https://i.imgur.com/1HhOGue.png");

                buildCandidatesField(suggestionEmbed, election.candidates);

                instance.api.getMessageById(election.electionMessageId, instance.api.getTextChannelById("1206713642381353070").get()).thenAccept(message -> {
                    message.createUpdater()
                            .removeAllEmbeds()
                            .setEmbed(suggestionEmbed).applyChanges();
                    event.getMessageComponentInteraction().acknowledge();
                });
            }

            case "fourth_c" -> {
                User user = event.getInteraction().getUser();
                Election election = instance.representativeElectionManager.getElection(event.getMessageComponentInteraction().getMessage().getIdAsString());

                if (!election.isOngoing) {
                    event.getMessageComponentInteraction().createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("Te wybory zostały już zakończone.").respond();
                    return;
                }

                if (election.getVoteUserById(user.getIdAsString()) != null) {
                    event.getMessageComponentInteraction().createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("Nie możesz ponownie oddać swojego głosu.").respond();
                    return;
                }

                if (election.getCandidate(user.getIdAsString()) != null) {
                    event.getMessageComponentInteraction().createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("Nie możesz oddać głosu będąc kandydatem.").respond();
                    return;
                }

                election.candidates.get(3).setVotes(1);
                election.addVotedUser(user);
                instance.representativeElectionManager.electionMap.values().forEach(Election::save);

                EmbedBuilder suggestionEmbed = new EmbedBuilder()
                        .setTitle("**Zostały otwarte wybory na stanowisko Reprezentanta Dumy**")
                        .setDescription("⏰ Wybory dobiegają końca: **" + election.isGoingToEnd.replace("T", "-") + "." + "**\n\uD83D\uDCBB Dowiedz się więcej o możliwościach posady Reprezentanta Dumy na <#1206713642381353070>. \n\uD83D\uDCCB Aby zostać kandydatem musisz mieć conajmniej 100 wiadomości na serwerze.")
                        .setColor(Color.decode("#00F2FF"))
                        .setThumbnail("https://i.imgur.com/XuIIA1v.png")
                        .setFooter(event.getInteraction().getServer().get().getName() + " | By Hexlin", "https://i.imgur.com/1HhOGue.png");

                buildCandidatesField(suggestionEmbed, election.candidates);

                instance.api.getMessageById(election.electionMessageId, instance.api.getTextChannelById("1206713642381353070").get()).thenAccept(message -> {
                    message.createUpdater()
                            .removeAllEmbeds()
                            .setEmbed(suggestionEmbed).applyChanges();
                    event.getMessageComponentInteraction().acknowledge();
                });
            }

            case "fifth_c" -> {
                User user = event.getInteraction().getUser();
                Election election = instance.representativeElectionManager.getElection(event.getMessageComponentInteraction().getMessage().getIdAsString());

                if (!election.isOngoing) {
                    event.getMessageComponentInteraction().createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("Te wybory zostały już zakończone.").respond();
                    return;
                }

                if (instance.getServerMemberManager().getServerMember(user.getId()).getMessagesSent() < 50) {
                    event.getMessageComponentInteraction().createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("Nie możesz brać udziału w wyborach bo nie masz conajmniej 50 wysłanych wiadomości.").respond();
                    return;
                }
                if (election.getCandidate(user.getIdAsString()) != null) {
                    event.getMessageComponentInteraction().createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("Już jesteś kandydatem w tych wyborach.").respond();
                    return;
                }
                if (election.candidates.size() == 4) {
                    event.getMessageComponentInteraction().createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("Wyczerpano wszystkie miejsca na kandydatów.").respond();
                    return;
                }

                RepresentativeCandidate representativeCandidate = new RepresentativeCandidate(user.getIdAsString(), 0);
                instance.representativeElectionManager.addCandidate(representativeCandidate);
                instance.representativeElectionManager.electionMap.values().forEach(Election::save);

                EmbedBuilder suggestionEmbed = new EmbedBuilder()
                        .setTitle("**Zostały otwarte wybory na stanowisko Reprezentanta Dumy**")
                        .setDescription("⏰ Wybory dobiegają końca: **" + election.isGoingToEnd.replace("T", "-") + "." + "**\n\uD83D\uDCBB Dowiedz się więcej o możliwościach posady Reprezentanta Dumy na <#1206713642381353070>. \n\uD83D\uDCCB Aby zostać kandydatem musisz mieć conajmniej 100 wiadomości na serwerze.")
                        .setColor(Color.decode("#00F2FF"))
                        .setThumbnail("https://i.imgur.com/XuIIA1v.png")
                        .setFooter(event.getInteraction().getServer().get().getName() + " | By Hexlin", "https://i.imgur.com/1HhOGue.png");

                buildCandidatesField(suggestionEmbed, election.candidates);

                instance.api.getMessageById(election.electionMessageId, instance.api.getTextChannelById("1206713642381353070").get()).thenAccept(message -> {
                    message.createUpdater()
                            .removeAllEmbeds()
                            .setEmbed(suggestionEmbed).applyChanges();
                    event.getMessageComponentInteraction().acknowledge();
                });
            }

            case "send" -> {
                if (event.getInteraction().getUser().getRoles(event.getInteraction().getServer().get()).contains(instance.api.getRoleById("1199465856032329849").get())) {
                    event.getMessageComponentInteraction().createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("Jesteś już zweryfikowany.").respond();
                    return;
                }

                if (instance.verificationMap.get(event.getInteraction().getUser().getId()) == null) {
                    Random random = new Random();
                    int code = random.nextInt(1000);
                    instance.verificationMap.put(event.getInteraction().getUser().getId(), code);

                    event.getMessageComponentInteraction().createImmediateResponder()
                            .setContent("Twój kod do wpisania to: " + code)
                            .setFlags(MessageFlag.EPHEMERAL)
                            .respond();
                    event.getMessageComponentInteraction().acknowledge();
                    return;
                } else {
                    event.getMessageComponentInteraction().createImmediateResponder()
                            .setContent("Twój kod do wpisania to: " + instance.verificationMap.get(event.getInteraction().getUser().getId()))
                            .setFlags(MessageFlag.EPHEMERAL)
                            .respond();
                    event.getMessageComponentInteraction().acknowledge();
                }
                event.getMessageComponentInteraction().acknowledge();
            }

            case "type" -> {
                event.getInteraction().respondWithModal("verificationmodal", "Kod Weryfikacji",
                        ActionRow.of(TextInput.create(TextInputStyle.SHORT, "code", "Wpisz tutaj zapamiętany kod")));
            }
        }

        if (!event.getMessageComponentInteraction().getCustomId().equalsIgnoreCase("send") || event.getMessageComponentInteraction().getCustomId().equalsIgnoreCase("weryfikacja") || event.getMessageComponentInteraction().getCustomId().equalsIgnoreCase("type")) {
            if (event.getInteraction().getUser().getRoles(event.getInteraction().getServer().get()).contains(instance.api.getRoleById(instance.getServerRoleById(event.getMessageComponentInteraction().getCustomId())).get())) {
                event.getInteraction().getUser().removeRole(instance.api.getRoleById(instance.getServerRoleById(event.getMessageComponentInteraction().getCustomId())).get());
                event.getMessageComponentInteraction().acknowledge();
                return;
            }
            event.getInteraction().getUser().addRole(instance.api.getRoleById(instance.getServerRoleById(event.getMessageComponentInteraction().getCustomId())).get());
        }

        event.getMessageComponentInteraction().acknowledge();
    }

    private void buildCandidatesField(EmbedBuilder embedBuilder, List<RepresentativeCandidate> candidatesList) {
        if (!candidatesList.isEmpty()) {
            String[] numberEmojis = {"1️⃣", "2️⃣", "3️⃣", "4️⃣"};

            StringBuilder candidatesStringBuilder = new StringBuilder();
            for (int i = 0; i < Math.min(candidatesList.size(), numberEmojis.length); i++) {
                RepresentativeCandidate representativeCandidate = candidatesList.get(i);
                String candidateName = instance.api.getUserById(representativeCandidate.userId).join().getName();
                String candidateWithEmoji = numberEmojis[i] + " " + candidateName + " | " + representativeCandidate.votes + " głosów.";
                candidatesStringBuilder.append(candidateWithEmoji).append("\n");
            }
            embedBuilder.addField("\uD83D\uDDFD Lista obecnych kandydatów:", candidatesStringBuilder.toString().trim());
        } else {
            embedBuilder.addField("\uD83D\uDDFD Lista obecnych kandydatów:", "Brak");
        }
    }
}
