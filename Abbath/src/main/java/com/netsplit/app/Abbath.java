package com.netsplit.app;
import org.jibble.pircbot.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.File;
import org.json.*;
import de.umass.lastfm.*;
import java.text.DateFormat;
import java.util.*;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.*;

public class Abbath extends PircBot {

	// Configuration file - currently unused
	private static final String CONFIG_FILE = "config.properties";

	// API Key
	private static final String API_KEY = "0e9e588a8b4623f89de5af442ff28596";

	// User data
	private static final String user_properties_file = "/home/mike/Projects/pirc/Abbath/user.properties";

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

		if (args[0].startsWith("!")) {
			if (args.length < 2) {
				String no_user_name = get_user_data(channel, sender.toLowerCase());
				System.out.println(no_user_name);
				String[] newArgs = new String[2];
				newArgs[0] = args[0];
				newArgs[1] = no_user_name;
				args = newArgs;
			} // if

			switch (args[0].toLowerCase()) {
			case "!help":
				sendMessage(channel, sender + ": Hello World");
				break;
			case "!setuser":
				if (args.length > 1) {
					set_user_data(sender.toLowerCase(), args[1].toLowerCase());
				} // if
				break;
			case "!np":
				if (args.length > 1) {
					findUserNowPlaying(channel, args[1]);
				} // if
				break;
			case "!top":
				if (args.length > 1) {
					findUserRecentArtists(channel, args[1]);
				} // if
				break;
			case "!neighbours":
				if (args.length > 1) {
					findUserNeighbours(channel, args[1]);
				} // if
				break;
			case "!whois":
				if (args.length > 1) {
					String user_name = get_user_data(channel, args[1].toLowerCase());
					sendMessage(channel, user_name);
				} // if
				break;
			} // switch
		}
	} // ::onMessage()

	/*
	protected void get_user_data(String channel, String nick) {
		BufferedReader br = null;
		String line = "";
		String split_by = ",";

		try {
			br = new BufferedReader(new FileReader(user_data));
			while ((line = br.readLine()) != null) {
				String[] user_file_info = line.split(split_by);
				for (String user_line : user_file_info) {
					System.out.println(user_line);
					//sendMessage(channel, user_line[1]);
				} // for
			} // while
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} // try
	} // get_user_data()
	*/

	/*
	protected void get_user_data(String channel, String nick) {
		try {
			Scanner scanner = new Scanner(new File(user_data));
			scanner.useDelimiter(",");
			while (scanner.hasNext()) {
				System.out.print(scanner.next()+"\n");
			} // while
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} // try
	} // get_user_data()
	*/

	/**
	 * Find a given user's Last.fm userid from user.properties file.
	 *
	 * @param string channel	Command issued here
	 * @param string nick		User to search
	 * @return string
	 * @since 1.0
	 */
	protected static String get_user_data(String channel, String nick) {
		Properties user_data = new Properties();

		InputStream input = null;

		try {
			input = new FileInputStream("/home/mike/Projects/pirc/Abbath/user.properties");
			user_data.load(input);
			String user_name = user_data.getProperty(nick);

			return user_name;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (input != null)
					input.close();
			} catch (IOException e) {
				e.printStackTrace();
			} // try
		} // try

		return null;
	} // get_user_data()

	/**
	 * Set a given user's Last.fm userid in the user.properties file.
	 *
	 * @param string nick	User who issued the command.
	 * @param string userid	The userid they have specified.
	 */
	protected void set_user_data(String nick, String userid) {
		Properties prop = new Properties();
		final File props_file = new File(user_properties_file);
		OutputStream output = null;

		try {
			prop.load(new FileInputStream(props_file));
			//output = new FileOutputStream(user_properties_file);
			prop.setProperty(nick, userid);

			//prop.store(output, null);
			prop.save(new FileOutputStream(props_file), "");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (Exception e) {
					e.printStackTrace();
				} // catch
			} // if
		} // finally
	} // set_user_data()

	/**
	 * Display given user's now playing.
	 *
	 * @param string channel	Where the command was issued
	 * @param string username	Last.fm username to search
	 * @since 1.0
	 */
	protected void findUserNowPlaying(String channel, String username) {
		PaginatedResult<de.umass.lastfm.Track> tracks =
			de.umass.lastfm.User.getRecentTracks(username,
				1, 1, API_KEY);
		String output = username;
		int i = 0;
		for (de.umass.lastfm.Track track : tracks) {
			if (i < 1) {
				/*
				if (track.getNowPlaying() == true) {
					output += " is now playing ";
				} else {
					output = " last played ";
				}
				*/
				System.out.println(track);
				output += track.getArtist()+" \""+track.getName()
					+"\" ["+track.getAlbum()+"]";
			} // if
			i++;
		} // for

		sendMessage(channel, output);
	} // findUserNowPlaying()

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
				output += artist.getName()+"["
					+artist.getPlaycount()+"], ";
			}
			i++;
		} // for
		String output_t = output.substring(0, output.lastIndexOf(","));
		sendMessage(channel, output_t);
	} // findUserRecentArtists()

	protected void findUserNeighbours(String channel, String username) {
		Collection<de.umass.lastfm.User> neighbours = de.umass.lastfm.User.getNeighbours(
				username, 5, API_KEY);
		String output = "Neighbours: ";
		int i = 0;
		String neighbour_list = null;
		for (de.umass.lastfm.User neighbour : neighbours) {
			if (i < 5) {
				output += neighbour.getName()+", ";
			} // if
			i++;
		} // for
		String output_t = output.substring(0, output.lastIndexOf(","));
		sendMessage(channel, output_t);
	} // findUserNeighbours()

} // Abbath::
