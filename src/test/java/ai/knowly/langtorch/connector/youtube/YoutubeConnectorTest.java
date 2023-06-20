package ai.knowly.langtorch.connector.youtube;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;
import org.junit.Test;

public class YoutubeConnectorTest {

  // Arrange.
  private final String WRONG_URL ="https://www.youtube.com/watch?v=notexist";
  private final String WRONG_DESTINATION_PATH ="/not_exist/%(title)s.%(ext)s";

  private final String CORRECT_URL ="https://www.youtube.com/watch?v=1emA1EFsPMM";
  private final String CORRECT_DESTINATION_PATH ="src/test/resources/%(title)s.%(ext)s";

  @Test
  public void testReadSubtitle() {
    // Arrange.
    // Act.
    Optional<String> result = new YoutubeConnector(
        YoutubeConnectorOption.builder().setOutputDirectory(CORRECT_DESTINATION_PATH).setUrl(
            CORRECT_URL).build()).read();

    // Assert.
    assertThat(result.get())
        .isEqualTo(
            "WEBVTT\n"
                + "Kind: captions\n"
                + "Language: en\n"
                + "\n"
                + "00:01:08.714 --> 00:01:12.338\n"
                + "I’m wearing a vintage suit with a cane in my hand.  I’m playing a magical symphony.\n"
                + "\n"
                + "00:01:12.338 --> 00:01:15.087\n"
                + "Wandering in Samaritaine, renovated throughout years\n"
                + "\n"
                + "00:01:15.087 --> 00:01:18.000\n"
                + "A borderless empire. A throne built with music notes.\n"
                + "\n"
                + "00:01:18.000 --> 00:01:21.000\n"
                + "I pass through the timeless 1920s with my piano.\n"
                + "\n"
                + "00:01:21.000 --> 00:01:24.000\n"
                + "Ah, obsession was the apple I showed Magritte.\n"
                + "\n"
                + "00:01:24.000 --> 00:01:27.107\n"
                + "Am I the surreal one or was it the clown he wanted to paint?\n"
                + "\n"
                + "00:01:27.107 --> 00:01:30.000\n"
                + "Ceci  n’est pas une pipe. The pigeon was still on his face.\n"
                + "\n"
                + "00:01:30.000 --> 00:01:32.652\n"
                + "Please remember Magritte is an artist, not some made-up drink.\n"
                + "\n"
                + "00:01:32.652 --> 00:01:35.614\n"
                + "Who inspired Dali and his mustache? \n"
                + "\n"
                + "00:01:35.614 --> 00:01:38.485\n"
                + "‘Tis I who bends the spoon.\n"
                + "\n"
                + "00:01:38.485 --> 00:01:41.730\n"
                + "Is it cheese or a clock that’s melting?\n"
                + "\n"
                + "00:01:41.730 --> 00:01:44.538\n"
                + "You never pick up the lobster phone.\n"
                + "\n"
                + "00:01:44.538 --> 00:01:47.652\n"
                + "Decadence is the freely roaring Chinese painting.\n"
                + "\n"
                + "00:01:47.652 --> 00:01:50.623\n"
                + "The crossed legs of Paris are a foreign nostalgia\n"
                + "\n"
                + "00:01:50.623 --> 00:01:53.401\n"
                + "planted in a sea of bleak tenderness, painted by the cosmos.\n"
                + "\n"
                + "00:01:53.401 --> 00:01:56.242\n"
                + "Sanyu’s flower can only grow from a lonely branch.\n"
                + "\n"
                + "00:01:56.242 --> 00:02:01.974\n"
                + "Boats travel quietly along Matisse’s shore.\n"
                + "\n"
                + "00:02:01.974 --> 00:02:07.493\n"
                + "Let Van Gogh light up this starry night\n"
                + "\n"
                + "00:02:07.493 --> 00:02:13.543\n"
                + "Munch screams on the bridge for the short-lived dreams.\n"
                + "\n"
                + "00:02:13.543 --> 00:02:20.029\n"
                + "All the glamour in this world blooms from loneliness.\n"
                + "\n"
                + "00:03:22.992 --> 00:03:26.075\n"
                + "A garden blanketed in sunshine; the scent of flower permeates.\n"
                + "\n"
                + "00:03:26.075 --> 00:03:28.822\n"
                + "I ask Monet for a portrait of me.\n"
                + "\n"
                + "00:03:28.822 --> 00:03:31.807\n"
                + "He gazes into the distance, studying the shapes of color.\n"
                + "\n"
                + "00:03:31.807 --> 00:03:35.192\n"
                + "Then suddenly asks, “What’s your impression of yourself?”\n"
                + "\n"
                + "00:03:35.192 --> 00:03:37.924\n"
                + "A decade of madness. The king of music. The world bows to my symphony.\n"
                + "\n"
                + "00:03:37.924 --> 00:03:40.928\n"
                + "The road is winding. I’m still going.  The melody won't stop yearning .\n"
                + "\n"
                + "00:03:40.928 --> 00:03:43.695\n"
                + "A decade of madness. The king of music. I don’t need any framing.\n"
                + "\n"
                + "00:03:43.695 --> 00:03:46.827\n"
                + "No frame will hold the speed of my notes. My music is futurism\n"
                + "\n"
                + "00:03:47.294 --> 00:03:50.270\n"
                + "Sun rises at the impressionist’s harbor. \n"
                + "\n"
                + "00:03:50.270 --> 00:03:52.936\n"
                + "Light awakens the dormant flowers\n"
                + "\n"
                + "00:03:52.936 --> 00:03:56.405\n"
                + "The lawn is cheering for a drizzle of rain.\n"
                + "\n"
                + "00:03:56.405 --> 00:03:58.843\n"
                + "We are in love with this world.\n"
                + "\n"
                + "00:03:58.843 --> 00:04:02.188\n"
                + "The butterfly that stops in Cambridge\n"
                + "\n"
                + "00:04:02.188 --> 00:04:05.283\n"
                + "  flies toward Florence throughout the night.\n"
                + "\n"
                + "00:04:05.283 --> 00:04:08.325\n"
                + "Regrets are hidden in poetry\n"
                + "\n"
                + "00:04:08.325 --> 00:04:10.721\n"
                + "where warm smiles cannot reach.\n"
                + "\n"
                + "00:04:10.721 --> 00:04:13.786\n"
                + "Scales of Paris is written in melancholy. Its pages must be turned with music.\n"
                + "\n"
                + "00:04:13.786 --> 00:04:16.626\n"
                + "On a breezy night, I swapped the traveler’s herbal tea under the lamp with coffee.\n"
                + "\n"
                + "00:04:16.626 --> 00:04:19.744\n"
                + "He then fell in love with the conundrum named “bitterness”\n"
                + "\n"
                + "00:04:19.744 --> 00:04:22.689\n"
                + "Because that’s how it feels to wave goodbye to the clouds.\n"
                + "\n"
                + "00:04:22.689 --> 00:04:27.351\n"
                + "Boats travel quietly along Matisse’s shore.\n"
                + "\n"
                + "00:04:27.351 --> 00:04:33.163\n"
                + "Let Van Gogh light up this starry night\n"
                + "\n"
                + "00:04:33.163 --> 00:04:39.370\n"
                + "Munch screams on the bridge for the short-lived dreams.\n"
                + "\n"
                + "00:04:39.370 --> 00:04:45.288\n"
                + "All the glamour in this world blooms from loneliness.\n"
                + "\n");
  }

  @Test
  public void testUsingWrongUrl() {
    // Arrange.

    // Act.
    Optional<String> result = new YoutubeConnector(
        YoutubeConnectorOption.builder().setOutputDirectory(CORRECT_DESTINATION_PATH).setUrl(WRONG_URL).build()).read();

    // Assert.
    assertThat(result)
        .isEqualTo(Optional.empty());
  }

  @Test
  public void testDestinationPathNotExist() {
    // Arrange.

    // Act.
    Optional<String> result = new YoutubeConnector(
        YoutubeConnectorOption.builder().setOutputDirectory(WRONG_DESTINATION_PATH).setUrl(
            CORRECT_URL).build()).read();

    // Assert.
    assertThat(result)
        .isEqualTo(Optional.empty());
  }




}
