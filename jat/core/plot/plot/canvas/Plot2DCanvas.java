package jat.core.plot.plot.canvas;

import static jat.core.plot.plot.plotObjects.Base.LINEAR;
import static jat.core.plot.plot.utils.Array.getColumnCopy;
import static jat.core.plot.plot.utils.Array.getColumnsRangeCopy;
import static jat.core.plot.plot.utils.Array.increment;
import static jat.core.plot.plot.utils.Array.mergeColumns;
import static jat.core.plot.plot.utils.Histogram.histogram_classes;
import static jat.core.plot.plot.utils.Histogram.histogram_classes_2D;
import jat.core.plot.plot.plotObjects.Base;
import jat.core.plot.plot.plotObjects.BasePlot;
import jat.core.plot.plot.plots.BarPlot;
import jat.core.plot.plot.plots.BoxPlot2D;
import jat.core.plot.plot.plots.CloudPlot2D;
import jat.core.plot.plot.plots.HistogramPlot2D;
import jat.core.plot.plot.plots.LinePlot;
import jat.core.plot.plot.plots.ScatterPlot;
import jat.core.plot.plot.plots.StaircasePlot;
import jat.core.plot.plot.render.AWTDrawer2D;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;


/**
 * BSD License
 * 
 * @author Yann RICHET
 */
public class Plot2DCanvas extends PlotCanvas {

	// public final static String PARALLELHISTOGRAM = "PARALLELHISTOGRAM";

	private static final long serialVersionUID = 1L;

	public Plot2DCanvas() {
		super();
		ActionMode = ZOOM;
	}

	public Plot2DCanvas(Base b, BasePlot bp) {
		super(b, bp);
		ActionMode = ZOOM;
	}

	public Plot2DCanvas(double[] min, double[] max, String[] axesScales, String[] axesLabels) {
		super(min, max, axesScales, axesLabels);
		ActionMode = ZOOM;
	}

	
	public void paint(Graphics gcomp) {
		// System.out.println("PlotCanvas.paint");

		Graphics2D gcomp2D = (Graphics2D) gcomp;

		// anti-aliasing methods
		gcomp2D.addRenderingHints(AALIAS);
		gcomp2D.setRenderingHint(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);

		gcomp2D.setColor(getBackground());
	//	gcomp2D.fillRect(0, 0, getSize().width, getSize().height);

		draw.initGraphics(gcomp2D);

		// draw plot
		grid.plot(draw);

		for (int i = 0; i < plots.size(); i++) {
			getPlot(i).plot(draw);
			if (linkedLegendPanel != null) {
				linkedLegendPanel.nonote(i);
			}
		}

		for (int i = 0; i < objects.size(); i++) {
			getPlotable(i).plot(draw);
		}

		// draw note
		if (allowNote) {
			/*
			 * if (allowNoteCoord && coordNoted != null) {
			 * draw.setColor(NOTE_COLOR); draw.drawCoordinate(coordNoted);
			 * draw.drawText(Array.cat(reverseMapedData(coordNoted)),
			 * coordNoted); }
			 */
			for (int i = 0; i < plots.size(); i++) {
				if (getPlot(i).noted) {
					if (linkedLegendPanel != null) {
						linkedLegendPanel.note(i);
					}
					getPlot(i).note(draw);
					if (allowNoteCoord && coordNoted != null) {
						getPlot(i).noteCoord(draw, coordNoted);
					}
					// return;
				}
			}
		}
	}
	
	
	public void initDrawer() {
		draw = new AWTDrawer2D(this);
	}

	public void initBasenGrid(double[] min, double[] max) {
		initBasenGrid(min, max, new String[] { LINEAR, LINEAR }, new String[] { "X", "Y" });
	}

	public void initBasenGrid() {
		initBasenGrid(new double[] { 0, 0 }, new double[] { 1, 1 });
	}

	private static double[][] convertY(double[] XY) {
		double[] x = increment(XY.length, 1, 1);
		return mergeColumns(x, XY);
	}

	private static double[][] convertXY(double[]... XY) {
		if (XY.length == 2 && XY[0].length != 2)
			return mergeColumns(XY[0], XY[1]);
		else
			return XY;
	}

	public int addScatterPlot(String name, Color c, double[] Y) {
		return addPlot(new ScatterPlot(name, c,  convertY(Y)));
	}
	
	public int addScatterPlot(String name, Color c, double[][] XY) {
		return addPlot(new ScatterPlot(name, c, convertXY(XY)));
	}
	
	public int addScatterPlot(String name, Color c, double[] X, double[] Y) {
		return addPlot(new ScatterPlot(name, c, convertXY(X,Y)));
	}

	
	
	
	public int addLinePlot(LinePlot l) {
		return addPlot(l);
	}
	
	public int addLinePlot(String name, Color c, double[] Y) {
		return addPlot(new LinePlot(name, c, convertY(Y)));
	}
	
	public int addLinePlot(String name, Color c, double[][] XY) {
		return addPlot(new LinePlot(name, c, convertXY(XY)));
	}
	
	public int addLinePlot(String name, Color c, double[] X, double[] Y) {
		return addPlot(new LinePlot(name, c, convertXY(X,Y)));
	}

	public int addBarPlot(String name, Color c, double[] Y) {
		return addPlot(new BarPlot(name, c, convertY(Y)));
	}
	
	public int addBarPlot(String name, Color c, double[][] XY) {
		return addPlot(new BarPlot(name, c, convertXY(XY)));
	}
	
	public int addBarPlot(String name, Color c, double[] X, double[] Y) {
		return addPlot(new BarPlot(name, c, convertXY(X,Y)));
	}

	public int addStaircasePlot(String name, Color c, double[] Y) {
		return addPlot(new StaircasePlot(name, c, convertY(Y)));
	}
	
	public int addStaircasePlot(String name, Color c, double[][] XY) {
		return addPlot(new StaircasePlot(name, c, convertXY(XY)));
	}
	
	public int addStaircasePlot(String name, Color c, double[] X, double[] Y) {
		return addPlot(new StaircasePlot(name, c, convertXY(X,Y)));
	}
	

	public int addBoxPlot(String name, Color c, double[][] XY, double[][] dX) {
		return addPlot(new BoxPlot2D(XY, dX, c, name));
	}

	public int addBoxPlot(String name, Color c, double[][] XYdX) {
		return addPlot(new BoxPlot2D(getColumnsRangeCopy(XYdX, 0, 1), getColumnsRangeCopy(XYdX, 2, 3), c, name));
	}

	public int addHistogramPlot(String name, Color c, double[][] XY, double[] dX) {
		return addPlot(new HistogramPlot2D(name, c, XY, dX));
	}

	public int addHistogramPlot(String name, Color c, double[][] XY, double dX) {
		return addPlot(new HistogramPlot2D(name, c, XY, dX));
	}

	public int addHistogramPlot(String name, Color c, double[][] XYdX) {
		return addPlot(new HistogramPlot2D(name, c, getColumnsRangeCopy(XYdX, 0, 1), getColumnCopy(XYdX, 2)));
	}

	public int addHistogramPlot(String name, Color c, double[] X, int n) {
		double[][] XY = histogram_classes(X, n);
		return addPlot(new HistogramPlot2D(name, c, XY, XY[1][0] - XY[0][0]));
	}

	public int addHistogramPlot(String name, Color c, double[] X, double... bounds) {
		double[][] XY = histogram_classes(X, bounds);
		return addPlot(new HistogramPlot2D(name, c, XY, XY[1][0] - XY[0][0]));
	}

	public int addHistogramPlot(String name, Color c, double[] X, double min, double max, int n) {
		double[][] XY = histogram_classes(X, min, max, n);
		return addPlot(new HistogramPlot2D(name, c, XY, XY[1][0] - XY[0][0]));
	}

	public int addCloudPlot(String name, Color c, double[][] sampleXY, int nX, int nY) {
		double[][] XYh = histogram_classes_2D(sampleXY, nX, nY);
		return addPlot(new CloudPlot2D(name, c, XYh, XYh[1][0] - XYh[0][0], XYh[nX][1] - XYh[0][1]));
	}
	
	public static void main(String[] args) {
		/*
		 * Plot2DPanel p2d = new Plot2DPanel(DoubleArray.random(10, 2), "plot
		 * 1", PlotPanel.SCATTER); new FrameView(p2d);
		 * p2d.addPlot(DoubleArray.random(10, 2), "plot 2", PlotPanel.SCATTER);
		 * p2d.grid.getAxe(0).darkLabel.setCorner(0.5, -10);
		 * p2d.grid.getAxe(1).darkLabel.setCorner(0, -0.5);
		 */
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}