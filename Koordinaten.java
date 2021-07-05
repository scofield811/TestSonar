package aufgabe2;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * 
 * @author Manuel Stahl
 * @version 1.0.0, 06/2020
 * Koordinatenprogramm
 */


public class Koordinaten extends Application {
	
    public static void main(String[] args) {
        launch(args);
    }
    
    private Button btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn10;
    private Group grp = new Group();
    
    
    public void start(Stage primaryStage) {
    	GridPane root = new GridPane();
    	GridPane gPButton = new GridPane();

		//root.setGridLinesVisible(true);
		root.setHgap(10);
		root.setVgap(10);
		root.setPadding(new Insets(10,10,10,10));
		
		Label labelkoord = new Label("Koordinatensystem");
		root.add(labelkoord, 0,0);
		Rectangle panel = new Rectangle(500,500);
		panel.setStyle("-fx-fill: white; -fx-stroke: black; -fx-stroke-width: 1;");
		
				
		root.add(grp,0,1);
		grp.getChildren().add(panel);
		
		Label labelfunc = new Label("Funktionen");
		root.add(labelfunc, 1, 0);
		root.add(gPButton, 1, 1);
		
		drawKo();
		
		initButtons(gPButton);
		eventHandling();
		
		primaryStage.setScene(new Scene(root));
		primaryStage.setTitle("Koordinatensystem");
		primaryStage.setResizable(false);
		primaryStage.show();
    }
    
    //Zeichnet ein Koordinatensystem
    private void drawKo() {

    	//X-Achse
    	Line linex = new Line(0,250,500,250);
    	linex.setStroke(Color.GREY);
    	grp.getChildren().add(linex);
    	
    	//Y-Achse
    	Line liney = new Line(250, 0, 250,500);
    	liney.setStroke(Color.GREY);
    	grp.getChildren().add(liney);
    	
    	//setze alle 25Pixel einen Markierungsstrich für X und Y
    	for(int i=25; i<500; i+=25) {
    		Line markX = new Line();
        	markX.setStroke(Color.GREY);
    		markX.setStartX(i);
    		markX.setStartY(245);
    		markX.setEndX(i);
    		markX.setEndY(255);
    		grp.getChildren().add(markX);
    		
    		Line markY = new Line();
        	markY.setStroke(Color.GREY);
    		markY.setStartX(245);
    		markY.setStartY(i);
    		markY.setEndX(255);
    		markY.setEndY(i);
    		grp.getChildren().add(markY);
    	}
    	//Pfeile für X und Y
    	Line arrowX1 = new Line(245,10,250,0);
    	arrowX1.setStroke(Color.GREY);
    	Line arrowX2 = new Line(255,10,250,0);
    	arrowX2.setStroke(Color.GREY);
    	
    	Line arrowY1 = new Line(490,245,500,250);
    	arrowY1.setStroke(Color.GREY);
    	Line arrowY2 = new Line(490,255,500,250);
    	arrowY2.setStroke(Color.GREY);
    	
        Text textX = new Text(225, 20, "Y");
        textX.setStroke(Color.GREY);
        Text textY = new Text(480, 235, "X");
        textY.setStroke(Color.GREY);
        grp.getChildren().addAll(textX,textY,arrowX1,arrowX2,arrowY1,arrowY2);
       	
    }

    //Zeichnet eine Function
    private void drawFunction(String func, Boolean sin) {
    	func = func.replaceAll("y=", ""); //löscht aus dem String y=
    	String fx = "";
    	int x = 0;
    	int tx = -1;
    	int y = 0;
    	int ty = -1;
    	
    	for (int i=-10; i<=10; i++) {
    		x = getPixelFromPoint(i,'x');
    		System.out.println("PunktX: "+i +" Pixel: "+x);
    		if (sin) { 			//überprüft ob es eine Sinusfunktion ist oder nicht
    			y = getPixelFromPoint(calcSin(func,i),'y'); 	// berechnung des Y-Wertes
    			System.out.println("PunktY: "+calcSin(func,i) +" Pixel: "+y);
    		} else {
    			fx = func.replaceAll("x", Integer.toString(i)); 	//Wenn es keine Sinusfunktion ist ersetze x mit aktuellen Wert aus der For-Schleife
    			y = getPixelFromPoint(calc(fx),'y');  //ermittle Y-Wert
    			System.out.println("PunktY: "+calc(fx) +" Pixel: "+y);
    		}
    		
    		if (y>=0 && y<=500) { 		//Überprüfung ob ergebnis innerhalb der 0 bis 500 Pixel für das Panel/Koordinatensystem
    			grp.getChildren().add(new Circle (x,y,2,Color.BLUE));
    			if (ty != -1 && tx != -1) {
    				Line line = new Line();
    				line.setStartX(tx);
    				line.setStartY(ty);
    				line.setEndX(x);
    				line.setEndY(y);
    				grp.getChildren().add(line);
    			}
    			tx = x;
    			ty = y;
    		}
		}
    }

    //Berechne den Y-Wert für eine Funktion (Versuch es dynamisch zu Programmieren)
    //Punkt vor Strich-Rechnung wird aktuell nicht beachtet nur wenn quadriert wird
    private double calc(String fx) {
    	//Regular Expression zum teilen der Zahlen und Operatoren
    	String delimsOp = "[^+\\-*/\\^sin( ]+";
    	String delimsDig = "[+\\-*/\\^sin() ]+";
    	String[] ops = fx.split(delimsOp); 		//enthält alle Operatoren
    	String[] tokens = fx.split(delimsDig);		//enthält alle Zahlen
    	
    	double tempErgebnis = 0;

    	//Wenn kein Operator vorhanden setze aktuellen Wert (bsp. y=x)
    	if (ops.length == 0) {
    		tempErgebnis = Double.parseDouble(tokens[0]);
    	}

    	for(int i=0; i<ops.length; i++) {
    		//Berechnung der entpsrechenden Stelle im String
    		switch(ops[i]) {
    		case "+-":		//negatives Vorzeichen
    			tokens[i] ="-"+tokens[i];
    			System.out.println("zahl: "+tokens[i]);
    		case "+":
    			if (ops.length >= i+2 && ops[i+1].equals("^")) {	//Wenn nachfolgender Operator ein Quadrat ist wird dies zuerst ausgeführt
    				System.out.println("erst quadrieren");
    				tempErgebnis = tempErgebnis + Math.pow(Double.parseDouble(tokens[i]),Double.parseDouble(tokens[i+1]));
    			} else {
    				tempErgebnis = tempErgebnis + Double.parseDouble(tokens[i]);
    			}
    			break;
    		case "--":
    			tokens[i] ="-"+tokens[i];
    			System.out.println("zahl: "+tokens[i]);
    		case "-":
    			if (ops.length >= i+2 && ops[i+1].equals("^")) {
    				System.out.println("erst quadrieren");
    				if (tokens[0].isEmpty()) {
    					System.out.println("vorzeichen -");
    					tempErgebnis = tempErgebnis + Math.pow(Double.parseDouble(tokens[i+1]),Double.parseDouble(tokens[i+2]));
    				} else {
    					tempErgebnis = tempErgebnis - Math.pow(Double.parseDouble(tokens[i]),Double.parseDouble(tokens[i+1]));
    				}
    			} else {
    				if (tokens[0].isEmpty()) {
    					System.out.println("vorzeichen -");
    					tempErgebnis = tempErgebnis - Double.parseDouble(tokens[i+1]);
    				} else {
    					System.out.println("index"+i+":"+tokens[i]);
        				tempErgebnis = tempErgebnis - Double.parseDouble(tokens[i]);
    				}
    				
    			}
    			break;
    		case "*-":
    			tokens[i] ="-"+tokens[i];
    			System.out.println("zahl: "+tokens[i]);
    		case "*":
    			if (ops.length >= i+2 && ops[i+1].equals("^")) {
    				System.out.println("erst quadrieren");
    				tempErgebnis = tempErgebnis * Math.pow(Double.parseDouble(tokens[i]),Double.parseDouble(tokens[i+1]));
    			} else {
    				tempErgebnis = tempErgebnis * Double.parseDouble(tokens[i]);
    			}
    			
    			break;
    		case "/-":
    			tokens[i] ="-"+tokens[i];
    			System.out.println("zahl: "+tokens[i]);
    		case "/":
    			if (ops.length >= i+2 && ops[i+1].equals("^")) {
    				System.out.println("erst quadrieren");
    				tempErgebnis = tempErgebnis / Math.pow(Double.parseDouble(tokens[i]),Double.parseDouble(tokens[i+1]));
    			} else {
    				tempErgebnis = tempErgebnis / Double.parseDouble(tokens[i]);
    			}
    			break;
    		case "^": 	//quadrieren
    			if (!ops[i-1].equals("*") && !ops[i-1].equals("/") && !ops[i-1].equals("+") && !ops[i-1].equals("-") && !ops[i-1].equals("*-")) {
    				tempErgebnis =  tempErgebnis + Math.pow(Double.parseDouble(tokens[i-1]),Double.parseDouble(tokens[i]));
    			} 
    			
    			break;
    		case "":
    			//kein Operator
    			if (ops.length >= i+2 && ops[i+1].equals("^")) {
    				//System.out.println("Debug: erst quadrieren");
    			} else {
    				tempErgebnis = Double.parseDouble(tokens[i]);
    			}
    			break;
    		}
    	}
    	
    	return tempErgebnis;
    }
    
    //berechner den Y-Wert für eine Sinus Funktion -> übergibt Ursprungsfunktion und den aktuellen x Wert aus der For-Schleife
    private double calcSin(String fx, int x) {
		double ergebnis = 0;
		
		//Berechnungen der Funktionen in einem Switch-Case
		switch(fx) {
		case "3*sin(x*π/10)":
			ergebnis = 3*Math.sin(x*Math.PI/10); 
			break;
		case "6*sin(x*π/10)":
			ergebnis = 6*Math.sin(x*Math.PI/10);
			break;
		case "6*sin(x*π/5)":
			ergebnis = 6*Math.sin(x*Math.PI/5);
			break;
		case "5*sin(x*π/10)+(5/3)*sin(3*x*π/10)+sin(x*π/2)+(5/7)*sin(7*x*π/10)":
			ergebnis = 5*Math.sin(x*Math.PI/10)+(5/3)*Math.sin(3*x*Math.PI/10)+Math.sin(x*Math.PI/2)+(5/7)*Math.sin(7*x*Math.PI/10);
			break;
		}
			
		return ergebnis;
	}

    //ermittelt die Pixel vom übergebenen Wert und der entsprechenden Achse
    private int getPixelFromPoint(double wert,char achse) {
    	int mitte = 250;
    	int schritt = 25;
    	int pDot = 0;
    	
    	if (achse == 'x') {
    		pDot = (int) Math.round(mitte + (schritt * wert));
    	} 
    	if (achse == 'y'){
    		pDot = (int) Math.round(mitte - (schritt * wert));
    	}
    	return pDot;
    }
    
    //Buttons in einem GridPane hinzufügen -> übergabe Parameter theroretisch nicht notwendig wenn global definiert
	private void initButtons(GridPane root) {
		root.setGridLinesVisible(true);
		root.setHgap(10);
		root.setVgap(10);
		
		btn1 = new Button("y=0.5*x");
		btn1.setPrefSize(125, 50);
		btn2 = new Button("y=x");
		btn2.setPrefSize(125, 50);
		btn3 = new Button("y=2*x");
		btn3.setPrefSize(125, 50);
		btn4 = new Button("y=0.1*x^2");
		btn4.setPrefSize(125, 50);
		btn5 = new Button("y=x^2");
		btn5.setPrefSize(125, 50);
		btn6 = new Button("y=10*x^2");
		btn6.setPrefSize(125, 50);
		btn7 = new Button("y=3*sin(x*π/10)");
		btn7.setPrefSize(125, 50);
		btn8 = new Button("y=6*sin(x*π/10)");
		btn8.setPrefSize(125, 50);
		btn9 = new Button("y=6*sin(x*π/5)");
		btn9.setPrefSize(125, 50);
		btn10 = new Button("y=5*sin(x*π/10)+(5/3)*sin(3*x*π/10)+sin(x*π/2)+(5/7)*sin(7*x*π/10)");
		btn10.setPrefSize(395, 50);
		root.add(btn1, 0, 0);
		root.add(btn2, 1, 0);
		root.add(btn3, 2, 0);
		root.add(btn4, 0, 1);
		root.add(btn5, 1, 1);
		root.add(btn6, 2, 1);
		root.add(btn7, 0, 2);
		root.add(btn8, 1, 2);
		root.add(btn9, 2, 2);
		root.add(btn10, 0, 3, 3,3);
	}
	
	//Inhalte/Graphen vom Panel löschen und Koordinatensystem neu zeichnen
	private void clearPanel() {
		grp.getChildren().clear();
		Rectangle panel = new Rectangle(500,500);
		panel.setStyle("-fx-fill: white; -fx-stroke: black; -fx-stroke-width: 1;");
		grp.getChildren().add(panel);
		drawKo();
	}
	
	//Eventhandler der Buttons
	private void eventHandling() {
		EventHandler<ActionEvent> eventHandlerFx = (ActionEvent event) -> processFx(event, false); //Eventhandler für lineare Funktionen
		EventHandler<ActionEvent> eventHandlerFxSin = (ActionEvent event) -> processFx(event, true); //Eventhandler für Sinus Funktionen

        btn1.setOnAction(eventHandlerFx);
        btn2.setOnAction(eventHandlerFx);
        btn3.setOnAction(eventHandlerFx);
        btn4.setOnAction(eventHandlerFx);
        btn5.setOnAction(eventHandlerFx);
        btn6.setOnAction(eventHandlerFx);
        btn7.setOnAction(eventHandlerFxSin);
        btn8.setOnAction(eventHandlerFxSin);
        btn9.setOnAction(eventHandlerFxSin);
        btn10.setOnAction(eventHandlerFxSin);
      
	}
	
	//Steuerung der Methoden zur Berechnung der Funktionen
	private void processFx(ActionEvent event, boolean sin) {
		clearPanel();
		
		String fx = ((Button)event.getSource()).getText();
		System.out.println("Button "+fx+" gedrückt!");
		if (sin) {
			drawFunction(fx,true);
		} else {
			drawFunction(fx,false);
		}
		
	}
	
	
}
