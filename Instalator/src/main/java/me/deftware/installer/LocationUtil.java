package me.deftware.installer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * @author Deftware
 */
public class LocationUtil {

	private URL url;

	private File file;

	private LocationUtil(URL url) {
		this.url = url;
	}

	private LocationUtil(File file) {
		this.file = file;
	}

	public static LocationUtil getClassPhysicalLocation(@Nonnull Class<?> c) {
		try {
			URL codeSourceLocation = c.getProtectionDomain().getCodeSource().getLocation();
			if (codeSourceLocation != null) {
				return new LocationUtil(codeSourceLocation);
			}
		} catch (SecurityException | NullPointerException ignored) { }

		URL classResource = c.getResource(c.getSimpleName() + ".class");
		if (classResource == null) {
			return new LocationUtil((URL) null);
		}
		String url = classResource.toString(), suffix = c.getCanonicalName().replace('.', '/') + ".class";
		if (!url.endsWith(suffix)) {
			return new LocationUtil((URL) null);
		}
		String base = url.substring(0, url.length() - suffix.length()), path = base;
		if (path.startsWith("jar:")) {
			path = path.substring(4, path.length() - 2);
		}
		try {
			return new LocationUtil(new URL(path));
		} catch (MalformedURLException e) {
			return new LocationUtil((URL) null);
		}
	}

	public File toFile() {
		if (file != null) {
			return file;
		}
		if (url != null) {
			String path = url.toString();
			if (path.startsWith("jar:")) {
				path = path.substring(4, path.indexOf("!/"));
			}
			try {
				if (OSUtils.isWindows() && path.matches("file:[A-Za-z]:.*")) {
					path = "file:/" + path.substring(5);
				}
				return new File(new URL(path).toURI());
			} catch (MalformedURLException | URISyntaxException ignored) {
			}
			if (path.startsWith("file:")) {
				return new File(path.substring(5));
			}
		}
		return null;
	}

}
