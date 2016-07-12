package venmo;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class DrawVenmoGraph extends JPanel {
	// Name-constants
	public static final int CANVAS_WIDTH = 1280;
	public static final int CANVAS_HEIGHT = 960;
	private List<Payment> payments;
	private Map<String, Coordinate> map = new HashMap<>();
	private float cellWidth = 80.0f;
	private float cellHeight = 60.0f;
	private float scale = 1.0f;

	/** Constructor to setup the GUI components */
	public DrawVenmoGraph(List<Payment> payments) {
		this.payments = payments;
		map = convertToMap(this.payments);
		setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
	}

	public FontMetrics pickFont(Graphics2D g2, String longString, float xSpace) {
		boolean fontFits = false;
		Font font = g2.getFont();
		FontMetrics fontMetrics = g2.getFontMetrics();
		int size = font.getSize();
		String name = font.getName();
		int style = font.getStyle();

		while (!fontFits) {
			int maxCharHeight = 15;
			if ((fontMetrics.getHeight() <= maxCharHeight) && (fontMetrics.stringWidth(longString) <= xSpace)) {
				fontFits = true;
			} else {
				int minFontSize = 4;
				if (size <= minFontSize) {
					fontFits = true;
				} else {
					g2.setFont(font = new Font(name, style, --size));
					fontMetrics = g2.getFontMetrics();
				}
			}
		}

		return fontMetrics;
	}

	/** Custom painting codes on this JPanel */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g); // paint background
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		setBackground(Color.WHITE);
		g2d.setStroke(new BasicStroke(3.0f));
		g2d.setPaint(Color.BLACK);
		for (int i = 0; i < payments.size(); i++) {
			Coordinate vertice1 = map.get(payments.get(i).names.get(0));
			Coordinate vertice2 = map.get(payments.get(i).names.get(1));
			g2d.draw(new Line2D.Double(vertice1.x + 50 * scale * 0.5, vertice1.y + 40 * scale * 0.5, vertice2.x + 50 * scale * 0.5, vertice2.y + 40 * scale * 0.5));
		}
		
		g2d.setPaint(Color.WHITE);
		for (Map.Entry<String, Coordinate> entry : map.entrySet()) {
			g2d.setPaint(Color.WHITE);
			g2d.fill(new Ellipse2D.Double(entry.getValue().x, entry.getValue().y, 50 * scale, 40 * scale));
			g2d.setPaint(Color.BLACK);
			g2d.draw(new Ellipse2D.Double(entry.getValue().x, entry.getValue().y, 50 * scale, 40 * scale));
			FontMetrics fontMetrics = pickFont(g2d, entry.getKey(), 90 * scale);
			drawText(g2d, entry.getKey(), fontMetrics, 40 * scale, 30 * scale, entry.getValue().x, entry.getValue().y);
			
		}
		

	}

	
	private Map<String, Coordinate> convertToMap(List<Payment> payments) {
		Map<String, Coordinate> newMap = new HashMap<>();
		for (int i = 0; i < payments.size(); i++) {
			for (int j = 0; j < 2; j++) {
				if (!newMap.containsKey(payments.get(i).names.get(j))) {
					newMap.put(payments.get(i).names.get(j), null);
				}
			}
		}
		
		
		
		while (newMap.size() > (int)CANVAS_WIDTH * CANVAS_HEIGHT / cellWidth / cellHeight * 0.75) {
			scale -= 0.1;
			cellWidth *= scale;
			cellHeight *= scale;
		}
		int row = (int) (CANVAS_HEIGHT / cellHeight);
		int col = (int)(CANVAS_WIDTH / cellWidth);
		int count = 0;
		for (Map.Entry<String, Coordinate> entry : newMap.entrySet()) {
			float x = (count % col) * cellWidth;
			float y = (count / col) * cellHeight;
			entry.setValue(new Coordinate(x, y));
			count++;
		}
		
		return newMap;
	}

	private void drawText(Graphics2D g2d, String name, FontMetrics fontMetrics, float width, float height, float xloc, float yloc) {
		
		AttributedString styledText = new AttributedString(name);
		styledText.addAttribute(TextAttribute.FONT, fontMetrics.getFont());
		AttributedCharacterIterator m_iterator = styledText.getIterator();
		int m_start = m_iterator.getBeginIndex();
		int m_end = m_iterator.getEndIndex();
		FontRenderContext frc = g2d.getFontRenderContext();
		LineBreakMeasurer measurer = new LineBreakMeasurer(m_iterator, frc);
		measurer.setPosition(m_start);

		xloc += 5 * scale;
		yloc += 5 * scale;
		
		while (measurer.getPosition() < m_end) {
			TextLayout layout = measurer.nextLayout(width);

			yloc += layout.getAscent();
			float dx = layout.isLeftToRight() ? 0 : width - layout.getAdvance();
			layout.draw(g2d, xloc + dx, yloc);
			yloc += layout.getDescent() + layout.getLeading();
		}
	}
	
}
