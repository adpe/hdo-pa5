package ch.ffhs.hdo.infrastructure;

import java.io.File;
import java.io.IOException;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Applikationsweite Settings Settings werden in einem PropertyFile gespeichert
 * das sich im User-Verzeichnis findet. Diese Klasse ist Singleton.
 * 
 * @author Denis Bittante
 *
 */
public class ApplicationSettings {

	private static final File APP_CONFIG = new File(
			System.getProperty("user.home") + File.separator + "hdo" + File.separator + "hdo.settings");

	private static Logger LOGGER = LogManager.getLogger(ApplicationSettings.class);
	private static final String INBOXPATH = "inboxPath";
	private static ApplicationSettings instance = null;

	private static PropertiesConfiguration config = null;

	protected ApplicationSettings() {

		try {

			if (!APP_CONFIG.exists()) {
				APP_CONFIG.getParentFile().mkdirs();
				APP_CONFIG.createNewFile();
				config = new PropertiesConfiguration(APP_CONFIG);

			} else {
				config = new PropertiesConfiguration(APP_CONFIG);
			}

		} catch (IOException e) {
			LOGGER.error("File konnte nicht erstellt werden", e);
		} catch (ConfigurationException e) {

			LOGGER.error("Konfigurationsfehler beim laden der Daten", e);
		}

	}

	/**
	 * Gibt die Instanz vom ApplikationSettings zurück
	 * 
	 * @return see {@link ApplicationSettings}
	 */
	public static ApplicationSettings getInstance() {
		if (instance == null) {
			instance = new ApplicationSettings();
		}
		return instance;
	}

	/**
	 * Getter für den Inbox-Pfad
	 * 
	 * @return Pfad zur Inbox
	 */
	public String getInbox_path() {

		return config.getString(INBOXPATH);

	}

	/**
	 * Speichert den InboxPfad in eine Konfigurationsdatei
	 * 
	 * @param inboxPath
	 */
	public void saveInboxPath(String inboxPath) {
		try {

			config.setProperty(INBOXPATH, inboxPath);
			config.save();

		} catch (ConfigurationException e) {
			LOGGER.error("Fehler beim Speichern der Konfigdatei", e);
		}
	}

}
