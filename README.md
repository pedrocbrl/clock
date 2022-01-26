# Simple Clock Demonstration
I've tried to make as simple as possible using the [Swing Toolkit](https://docs.oracle.com/javase/7/docs/api/javax/swing/package-summary.html). First calculating the positions of the hours marks on the clock face with the following equations:

```java
X Position = cos((hour * 30 + minute / 2) * 3.14f / 180 - 3.14f / 2) * 90);
Y Position = sin((hour * 30 + minute / 2) * 3.14f / 180 - 3.14f / 2) * 90);
```
Now with the each position done, i just have to draw the circle markers for each hour using [JPanel](https://docs.oracle.com/javase/7/docs/api/javax/swing/JPanel.html) and drawning the components.
```java
private void drawCircleTimeMarks(Graphics g) {
		for (int i = 1; i <= 12; i++) {
			int x = (int) (Math.cos((i * 30) * 3.14f / 180 - 3.14f / 2) * 135 + xcenter);
			int y = (int) (Math.sin((i * 30) * 3.14f / 180 - 3.14f / 2) * 135 + ycenter);

			g.setColor(Color.cyan);
			g.drawOval(x - 2, y - 2, 5, 5);
			g.fillOval(x - 2, y - 2, 5, 5);
		}

	}
```

Then i've updated the class to implements the [Runnable](https://docs.oracle.com/javase/7/docs/api/java/lang/Runnable.html) interface and run a thread that will update the clock face.

```java
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
```
I had to extend the Graphics class with Graphics2D so the border thickness can be adjusted to match the video.
```java
    Graphics2D g2d = (Graphics2D) g;
    g2d.setStroke(new BasicStroke(3));
    g2d.drawOval(xcenter - 150, ycenter - 150, 300, 300);
```
Now, to draw the hands i must draw a line between the center of the window and their respective positions, first formatting the date to obtain the current time and parsing each one separately, then calculating the position, always clamping the result so it will match an existing integer position on the Jpanel and setting each color before drawning.
```java
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
```

At last for the digital clock i've formatted the date to obtain the current time string, configured the String position, size and color, then call the digital clock draw method before the analog hands, so it will be rendered behind.

```java
private void setDigitalClock(Graphics g) {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
		Date date = calendar.getTime();
		String time = timeFormat.format(date);

		g.setFont(new Font("TimesRoman", Font.BOLD, 55));
		g.setColor(Color.white);
		g.drawString(time, xcenter - 100, ycenter - 5);
	}
  ```
  ```java
  public void paint(Graphics g) {
		int xHour, yHour, xMinute, yMinute, xSecond, ySecond, second, minute, hour;

		drawStructure(g);
		setDigitalClock(g);
    ...  
