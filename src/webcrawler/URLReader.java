package webcrawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

public class URLReader {
	Queue<String> queue = new LinkedList<String>();
	HashSet<String> set = new HashSet<String>();
	Path urlfile;

	public URLReader() {
		urlfile = Paths.get("urlfile");

	}

	public void getURLs(String input) {
		for (int i = 0; i < input.length();) {
			if (i + 7 < input.length()
					&& input.substring(i, i + 7).equals("http://")
					|| (i + 8 < input.length() && input.substring(i, i + 8)
							.equals("https://"))) {
				int r = i + 7;
				while (r < input.length()) {
					char c = input.charAt(r);
					if (c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z'
							|| c >= '0' && c <= '9' || c == '/' || c == '.'
							|| c == '_' || c == '-')
						r++;
					else
						break;
				}
				if (!set.contains(input.substring(i, r))) {
					set.add(input.substring(i, r));
					queue.add(input.substring(i, r));
					String tempstring = input.substring(i, r) + '\n';
					try {
						Files.write(urlfile, tempstring.getBytes(),
								StandardOpenOption.APPEND,
								StandardOpenOption.CREATE);
					} catch (IOException e) {
						// TODO Auto-generated catch block
					}
				}
				i = r + 1;
			} else {
				i++;
			}
		}
	}

	public void URLreader(String rawurl) {
		try {
			URL url = new URL(rawurl);

			BufferedReader in = new BufferedReader(new InputStreamReader(
					url.openStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				getURLs(inputLine);
			}
			in.close();

			System.out.println(rawurl + "   queue size:" + queue.size());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			// do nothing
		} catch (IOException e) {
			// TODO Auto-generated catch block
			// do nothing
		} catch (IllegalArgumentException e) {
			// do nothing
		} catch (Exception e) {
			// do nothing
		}
	}

	public void start(String seed) {
		queue.add(seed);
		while (!queue.isEmpty()) {
			URLreader(queue.poll());
		}
	}


	public static void main(String[] args) {
		URLReader test = new URLReader();
		String seed = "http://www.amazon.com/";
		test.start(seed);
	}
}
