package fr.mrcraftcod.interfaces;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.logging.Level;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import fr.mrcraftcod.Main;
import fr.mrcraftcod.objects.TransparentPane;

/**
 * Used to show a startup frame with a progress bar.
 * 
 * @author MrCraftCod
 */
public class InterfaceStartup
{
	private JFrame frame;
	private JLabel label;
	private JProgressBar progressBar;
	private int step = 0;

	/**
	 * Constructor.
	 * 
	 * @param maxStep How many steps are in the startup.
	 */
	public InterfaceStartup(int maxStep)
	{
		Main.logger.log(Level.INFO, "Creating startup frame...");
		setFrame(new JFrame());
		getFrame().setUndecorated(true);
		getFrame().setContentPane(new TransparentPane(new BorderLayout()));
		getFrame().getContentPane().setBackground(Color.BLACK);
		getFrame().setTitle("Starting " + Main.APPNAME);
		getFrame().setIconImages(Main.icons);
		getFrame().setBackground(new Color(0, 255, 0, 0));
		label = new JLabel();
		label.setFont(Main.fontMain);
		label.setForeground(new Color(255, 255, 255));
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setVerticalAlignment(JLabel.CENTER);
		progressBar = new JProgressBar(0, maxStep);
		progressBar.setStringPainted(true);
		progressBar.setFont(Main.fontMain);
		refreshProgressBarStep();
		getFrame().getContentPane().add(progressBar, BorderLayout.NORTH);
		getFrame().getContentPane().add(label, BorderLayout.SOUTH);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		getFrame().setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		getFrame().setLocation((dim.width - 500) / 2, (dim.height - 50) / 2);
		getFrame().setPreferredSize(new Dimension(500, 50));
		getFrame().setVisible(true);
		getFrame().toFront();
		getFrame().pack();
	}

	/**
	 * Used to refresh the current step showed by the progress bar.
	 */
	private void refreshProgressBarStep()
	{
		progressBar.setValue(step);
	}

	/**
	 * Used to reset the current step.
	 */
	public void reset()
	{
		Main.logger.log(Level.INFO, "Resetting startup frame...");
		step = 0;
		label.setText("");
		refreshProgressBarStep();
	}

	/**
	 * Used to set a new step.
	 * 
	 * @param step The current step.
	 * @param text The text printed to indicate what step is currently processed.
	 */
	public void setStartupText(int step, String text)
	{
		Main.logger.log(Level.INFO, "Setting startup (" + step + ") text to " + text);
		if(label != null)
		{
			label.setText(text);
			this.step = step;
			refreshProgressBarStep();
		}
	}

	/**
	 * Used to add a new step.
	 * 
	 * @param text The text printed to indicate what step is currently processed.
	 */
	public void addStartupText(String text)
	{
		Main.logger.log(Level.INFO, "Add startup text to " + text);
		if(label != null)
		{
			label.setText(text);
			step++;
			refreshProgressBarStep();
		}
	}

	/**
	 * Used to skip a step.
	 */
	public void skipStep()
	{
		Main.logger.log(Level.INFO, "Skipping startup step...");
		step++;
	}

	/**
	 * Used to close the frame
	 */
	public void exit()
	{
		Main.logger.log(Level.INFO, "Exitting startup frame...");
		getFrame().dispose();
	}

	public JFrame getFrame()
	{
		return frame;
	}

	public void setFrame(JFrame frame)
	{
		this.frame = frame;
	}

	public void setPercent(int i)
	{
		float percent = i / 100f;
		this.step = (int) (percent * this.progressBar.getMaximum());
		this.refreshProgressBarStep();
	}
}
