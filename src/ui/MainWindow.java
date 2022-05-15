package ui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;

public class MainWindow implements Initializable {

	@FXML
	private Canvas canvas;

	private ArrayList<Double> ejeX = new ArrayList<>();
	private ArrayList<Double> ejeY = new ArrayList<>();
	private GraphicsContext gc;
	private BufferedReader bf;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		gc = canvas.getGraphicsContext2D();

		paint();
	}

	public void paint() {
		// -------
		// gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
		readFiles();
		double[] resX = getMinMax(ejeX);
		double minX = resX[0];
		double maxX = resX[1];

		System.out.println(maxX);
		double[] resY = getMinMax(ejeY);
		double minY = resY[0];
		double maxY = resY[1];
		System.out.println(maxY);
		double deltaPx = canvas.getWidth() - 15;
		double deltaDias = maxX - minX;

		double pendienteX = deltaPx / deltaDias;

		double intercepto = pendienteX * minX * (-1);

		double deltaPy = -canvas.getHeight() + 25;

		double deltaAccidentes = maxY - minY;

		double pendienteY = deltaPy / deltaAccidentes;
		double interceptoY = pendienteY * maxY * (-1);
		// System.out.println(minX + " "+maxX+" "+minY+" "+maxY);
		gc.setStroke(Color.rgb(151, 188, 250));
		gc.setLineWidth(1);
		for (int i = 0; i < ejeX.size() - 1; i++) {
			// gc.setLineWidth(2);
			gc.moveTo(conversion(ejeX.get(i), pendienteX, intercepto) + 3,
					conversion(ejeY.get(i), pendienteY, interceptoY) + 3);
			gc.lineTo(conversion(ejeX.get(i + 1), pendienteX, intercepto) + 3,
					conversion(ejeY.get(i + 1), pendienteY, interceptoY) + 3);
		}
		gc.stroke();
		paintBg(maxX, maxY);
		gc.setFill(Color.rgb(54, 115, 216));
		for (int i = 0; i < ejeX.size(); i++) {
			gc.fillOval(conversion(ejeX.get(i), pendienteX, intercepto),
					conversion(ejeY.get(i), pendienteY, interceptoY), 6, 6);
		}

	}

	private void paintBg(double maxX, double maxY) {
		// TODO Auto-generated method stub
		gc.setStroke(Color.rgb(214, 214, 214));
		gc.setLineWidth(0.5);
		gc.setFill(Color.rgb(0, 0, 0));
		double y = canvas.getHeight() / 4;
		double x = canvas.getWidth() / 4;
		for (int i = 0; i < 4; i++) {
			gc.moveTo(0, y);
			gc.lineTo(canvas.getWidth(), y);
			if(i!=3)
			gc.fillText((int) (maxY * (1-(25*0.01 * (i+1)))) + "", 10, y - 5);
			gc.moveTo(x, 0);
			gc.lineTo(x, canvas.getHeight());
			gc.fillText((int) (maxX * (25*0.01 * (i+1))) + "", x + 5, canvas.getHeight() -15);
			y = y + (canvas.getHeight() / 4);
			x = x + (canvas.getWidth() / 4);
		}
		gc.stroke();
	}

	public void readFiles() {
		try {
			// Open .csv in buffer's reading mode
			bf = new BufferedReader(new FileReader("data/data.csv"));

			// Read a file line
			String currentLine = bf.readLine();

			// if the line is not empty we keep reading the file
			int i = 0;
			while (currentLine != null) {
				if (i != 0) {
					String[] recordSplit = currentLine.split(",");
					recordSplit[1] = recordSplit[1].replace(";", "");
					ejeX.add(Double.parseDouble(recordSplit[0]));
					ejeY.add(Double.parseDouble(recordSplit[1]));
				}
				// Read the next file line
				currentLine = bf.readLine();
				i++;
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// Close the buffer reader
			if (bf != null) {
				try {
					bf.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	public double[] getMinMax(ArrayList<Double> eje) {
		ArrayList<Double> aux = new ArrayList<>();
		aux.addAll(eje);
		Collections.sort(aux);
		double min = aux.get(0);
		double max = aux.get(aux.size() - 1);
		return new double[] { min, max };
	}


	private double conversion(double x, double m, double b) {

		return m * x + b;
	}

}
