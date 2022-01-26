package clockrender;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Simple animated clock demonstration.
 * @author Pedro henrique Cabral
 * @version 1.0
 */
public class Clock extends JPanel implements Runnable {
	private static final long serialVersionUID = 6478475849173185384L;
	Thread thread = null;
	SimpleDateFormat formatter = new SimpleDateFormat("s", Locale.getDefault());
	Date currentDate;
	
	// Center position on the window
	private int xcenter = 175, ycenter = 175;

	/**
	 * Draw the circular hour marks on the clock face.
	 * 
	 * @param g The Graphics context
	 */
	private void drawCircleTimeMarks(Graphics g) {
		for (int i = 1; i <= 12; i++) {
			int x = (int) (Math.cos((i * 30) * 3.14f / 180 - 3.14f / 2) * 135 + xcenter);
			int y = (int) (Math.sin((i * 30) * 3.14f / 180 - 3.14f / 2) * 135 + ycenter);

			g.setColor(Color.cyan);
			g.drawOval(x - 2, y - 2, 5, 5);
			g.fillOval(x - 2, y - 2, 5, 5);
		}

	}

	/**
	 * Must be called on <strong>paint()</strong> method. Draw the base structure
	 * for the analog clock face.
	 * 
	 * @param g The Graphics context
	 * 
	 * @see javax.swing.JComponent#paint()
	 */
	private void drawStructure(Graphics g) {
		g.setFont(new Font("TimesRoman", Font.BOLD, 20));
		g.setColor(Color.black);
		g.fillOval(xcenter - 150, ycenter - 150, 300, 300);
		g.setColor(Color.blue);
		g.setColor(Color.green);

		drawCircleTimeMarks(g);
		g.setColor(Color.cyan);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setStroke(new BasicStroke(3));
		g2d.drawOval(xcenter - 150, ycenter - 150, 300, 300);
	}

	/**
	 * Draw a digital clock on the analog clock face
	 * 
	 * @param g The Graphics context
	 */
	private void setDigitalClock(Graphics g) {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
		Date date = calendar.getTime();
		String time = timeFormat.format(date);

		g.setFont(new Font("TimesRoman", Font.BOLD, 55));
		g.setColor(Color.white);
		g.drawString(time, xcenter - 100, ycenter - 5);
	}

	/**
	 * Calculate the position, defines properties and draw the clock hands.
	 */
	@Override
	public void paint(Graphics g) {
		int xHour, yHour, xMinute, yMinute, xSecond, ySecond, second, minute, hour;

		drawStructure(g);
		setDigitalClock(g);

		currentDate = new Date();
		formatter.applyPattern("s");
		second = Integer.parseInt(formatter.format(currentDate));
		formatter.applyPattern("m");
		minute = Integer.parseInt(formatter.format(currentDate));
		formatter.applyPattern("h");
		hour = Integer.parseInt(formatter.format(currentDate));
		xSecond = (int) (Math.cos(second * 3.14f / 30 - 3.14f / 2) * 125 + xcenter);
		ySecond = (int) (Math.sin(second * 3.14f / 30 - 3.14f / 2) * 125 + ycenter);
		xMinute = (int) (Math.cos(minute * 3.14f / 30 - 3.14f / 2) * 110 + xcenter);
		yMinute = (int) (Math.sin(minute * 3.14f / 30 - 3.14f / 2) * 110 + ycenter);
		xHour = (int) (Math.cos((hour * 30 + minute / 2) * 3.14f / 180 - 3.14f / 2) * 90 + xcenter);
		yHour = (int) (Math.sin((hour * 30 + minute / 2) * 3.14f / 180 - 3.14f / 2) * 90 + ycenter);

		g.setColor(Color.gray);
		g.drawLine(xcenter, ycenter, xSecond, ySecond);
		g.setColor(Color.red);
		g.drawLine(xcenter, ycenter - 1, xMinute, yMinute);
		g.drawLine(xcenter - 1, ycenter, xMinute, yMinute);
		g.setColor(Color.blue);
		g.drawLine(xcenter, ycenter - 1, xHour, yHour);
		g.drawLine(xcenter - 1, ycenter, xHour, yHour);
	}

	/**
	 * Thread start
	 */
	public void start() {
		if (thread == null) {
			thread = new Thread(this);
			thread.start();
		}
	}

	/**
	 * Called when the thread starts. Calls the <strong>repaint()</strong> method to
	 * update the clock view.
	 */
	public void run() {
		while (thread != null) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
			repaint();
		}
		thread = null;
	}
	
	/**
	 * Create instance of JFrame and Clock component class. Calls <strong>Clock.start()</strong>
	 * @see clockrender.Clock#start()
	 * @param args
	 */
	public static void main(String args[]) {
		JFrame window = new JFrame("Watchface test");
		window.setBackground(Color.black);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setBounds(0, 0, 400, 400);
		Clock clock = new Clock();
		window.getContentPane().add(clock);

		window.setVisible(true);
		clock.start();
	}
}