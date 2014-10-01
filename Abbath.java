import org.jibble.pircbot.*;
import java.util.Date;
import org.apache.http.client.*;

public class Abbath extends PircBot {

	// Configuration file - currently unused
	private static final String CONFIG_FILE = "config.properties";

	// API Key
	private static final String API_KEY = "0e9e588a8b4623f89de5af442ff28596";

	public static void main(String[] args) throws Exception {
		Abbath bot = new Abbath();
		bot.setVerbose(true);
		bot.connect("irc.snoonet.org");
		bot.joinChannel("#mike");
	} // main()

	public Abbath() {
		super();
		this.setName("Abbath1");
	} // ::Abbath()

	@Override
	protected void onMessage(String channel, String sender,
			String login, String hostname, String message) {
		String[] args = message.split(" ");

		switch (args[0].toLowerCase()) {
		case "!help":
			sendMessage(channel, sender + ": Hello World");
			break;
		case "!time":
			getTime(channel, sender);
			break;
		case "!np":
			findUserNowPlaying(channel, args[1]);
			break;
		} // switch
	} // ::onMessage()

	protected void getTime(String channel, String sender) {
		String time = new Date().toString();
		sendMessage(channel, sender + ": The time is now " + time);
	} // ::getTime()

	protected void findUserNowPlaying(String channel, String user) {
		String api_call = "http://ws.audioscrobbler.com/2.0/?method=user.getrecenttracks&user=" +
			user + "&api_key=" + API_KEY + "&format=json";
		HttpClient client = new HttpClient();
		GetMethod method = new GetMethod(api_call);
		int status_code = client.executeMethod(method);
		sendMessage(channel, status_code);
	} // ::findUserNowPlaying()

} // Abbath::
