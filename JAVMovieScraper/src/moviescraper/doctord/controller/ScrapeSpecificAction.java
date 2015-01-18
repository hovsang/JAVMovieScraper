package moviescraper.doctord.controller;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import moviescraper.doctord.Movie;
import moviescraper.doctord.GUI.GUIMain;
import moviescraper.doctord.SiteParsingProfile.SiteParsingProfile;

public class ScrapeSpecificAction extends AbstractAction {
	
	private static final long serialVersionUID = 1L;
	
	private GUIMain main;
	private SiteParsingProfile profile;
	
	public ScrapeSpecificAction(GUIMain main, SiteParsingProfile profile) {
		this.main = main;
		this.profile = profile;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		try
		{
			main.setMainGUIEnabled(false);
			main.removeOldScrapedMovieReferences();
			List<File> toScrape = main.getCurrentFile();
			if (toScrape != null) {
				for(File currentFile : toScrape)
				{ 
					//reset the SiteParsingProfile so we don't get leftover stuff from the last file scraped
					//we want it to be of the same type, so we use the newInstance() method which will automatically
					//return a new object of the type the SiteParsingProfile actually is
					SiteParsingProfile spp = profile.newInstance();
					spp.setScrapingLanguage(main.getPreferences());
					SpecificScraperAction action = new SpecificScraperAction(spp, spp.getMovieScraper(), currentFile );
					Movie scrapedMovie = action.scrape(main.getPreferences());
					if(scrapedMovie != null)
						main.movieToWriteToDiskList.add(scrapedMovie);
					main.getFileDetailPanel().setNewMovie( scrapedMovie , true);
				}

			} else {
				JOptionPane.showMessageDialog(main.getFrmMoviescraper(), "No file selected.", "No file selected.", JOptionPane.ERROR_MESSAGE);
			}
		}
		finally
		{
			main.setMainGUIEnabled(true);
		}
	}
}
