package com.netsplit.app;
import org.jibble.pircbot.*;
import java.util.Date;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import org.json.*;
import de.umass.lastfm.*;
import java.text.DateFormat;
import java.util.*;

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
		Caller.getInstance().setUserAgent("Abbath");
	} // main()

	public Abbath() {
		super();
		this.setName("Abbath1");
	} // ::Abbath()

	/**
	 * Override Pirc's onMessage command to handle our own commands.
	 * If Abbath is extended this will need moving to a main file.
	 *
	 * @param string channel	Where the command was issued from
	 * @param string sender		Username who issued the command
	 * @param string login		User's ident?
	 * @param string hostname	User's hostname?
	 * @param string message	Command issued
	 * @return void
	 */
	@Override
	protected void onMessage(String channel, String sender,
			String login, String hostname, String message) {
		String[] args = message.split(" ");
		System.out.println(Arrays.toString(args));

		switch (args[0].toLowerCase()) {
		case "!help":
			sendMessage(channel, sender + ": Hello World");
			break;
		case "!setuser":

			break;
		case "!np":
			findUserNowPlaying(channel, args[1]);
			break;
		case "!top":
			if (args.length > 1) {
				findUserRecentArtists(channel, args[1]);
			} // if
			break;
		} // switch
	} // ::onMessage()

	/**
	 * Display given user's now playing.
	 *
	 * @param string channel	Where the command was issued
	 * @param string username	Last.fm username to search
	 * @since 1.0
	 */
	protected void findUserNowPlaying(String channel, String username) {
		String api_call = "http://ws.audioscrobbler.com/2.0/" +
			"?method=user.getrecenttracks&user=" + username +
			"&api_key=" + API_KEY + "&format=json";
		sendMessage(channel, "This feature is currently in development.");
	} // ::findUserNowPlaying()

	/**
	 * Display user's top 10 bands for the past week.
	 *
	 * @param string channel	Where the command was issued
	 * @param string username	Last.fm username to search
	 * @since 1.0
	 */
	protected void findUserRecentArtists(String channel, String username) {
		Collection<Artist> chart = de.umass.lastfm.User.getTopArtists(
				username, Period.WEEK, API_KEY);
		String output = "Charts: ";
		int i = 0;
		String artist_list = null;
		for (Artist artist : chart) {
			if (i < 9) {
				//System.out.println(artist.getName());
				output += artist.getName() + "["+ artist.getPlaycount() +"], ";
			}
			i++;
		} // for
		String output_t = output.substring(0, output.lastIndexOf(","));
		sendMessage(channel, output_t);
	} // ::findUserRecentArtists()


} // Abbath::