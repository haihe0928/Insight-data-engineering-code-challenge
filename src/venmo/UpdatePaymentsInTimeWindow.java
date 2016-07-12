package venmo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


public class UpdatePaymentsInTimeWindow {
	public static final String TITLE = "Venmo Graph";
	private static String filePath;
	private static long timeSpan;
	private JSONParser parser = new JSONParser();
	private List<Payment> paymentsList = new LinkedList<>(); //use paymentsList to record all payments within current time window
	private Map<List<String>, Integer> map = new HashMap<>();  //use HashMap to recording each payment within current time window and its index in paymentsList
	
//UpdatePaymentsInTimeWindow constructor
	public UpdatePaymentsInTimeWindow(String filePath, long timeSpan) {
		UpdatePaymentsInTimeWindow.filePath = filePath;
		UpdatePaymentsInTimeWindow.timeSpan = timeSpan;
	}
//read the input file line by line, and print median degree and venmo graph after each payment	
	public void read() {
		MedianDegree maintainMedianDegree = new MedianDegree();
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(filePath));
			String line;
			BufferedWriter output = null;
			File file = new File("venmo_output/output.txt");
			output = new BufferedWriter(new FileWriter(file));
			while ((line = br.readLine()) != null) {
				
					//parse each input line to JSONObject, convert JSONObject to Payment object, the names of actor and target are sorted in each Payment object
					Payment newPayment = new Payment((JSONObject) parser.parse(line));

					if (paymentsList.size() == 0 || !map.containsKey(newPayment.names)) {
						//if it's a new payment, add into HashMap and increase degree for actor and target
						insertPayment(newPayment); 
						maintainMedianDegree.increment(newPayment.names.get(0));
						maintainMedianDegree.increment(newPayment.names.get(1));
					} else {
						//if it is a an existing payment, update its timestamp, reorder paymentsList and update index in HashMap
						updatePayment(newPayment); 
					}
					
					// remove any old payments >= 60 second
					if (paymentsList.get(paymentsList.size() - 1).timestamp - paymentsList.get(0).timestamp >= 60) {
						while (paymentsList.get(paymentsList.size() - 1).timestamp - paymentsList.get(0).timestamp >= 60) {
							Payment cur = paymentsList.get(0);
							//decrease the degree of corresponding actor and target after removing each old payment
							maintainMedianDegree.decrement(cur.names.get(0));
							maintainMedianDegree.decrement(cur.names.get(1));
							map.remove(cur.names); //remove payment from HashMap
							paymentsList.remove(0);  //remove payment from paymentsList
							
						}
						updateIndex(0, paymentsList.size() - 1); //update index in HashMap after removing all old payments
					}
					
					//output median degree to output.txt
					output.write(maintainMedianDegree.median().toString());
					output.newLine();
					
					
					//draw venmo graph after each incoming payment
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							JFrame frame = new JFrame(TITLE);
							DrawVenmoGraph venmo = new DrawVenmoGraph(paymentsList); //pass paymentsList to DrawVenmoGraph to update the venmo graph
							frame.setContentPane(venmo);
							frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
							frame.pack(); 
							frame.setLocationRelativeTo(null); // center the application

							frame.setVisible(true); // show the graph
						}
					});

			
			}
			
			output.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	//Insert the new payment in paymentsList if the new (actor, target) pair hasn't shown up before, maintain the ascending order.
	private void insertPayment(Payment newPayment) {
		int targetPosition = findTargetPosition(newPayment); //find the insertion position
		paymentsList.add(targetPosition, newPayment); //add the new payment
		updateIndex(targetPosition, paymentsList.size() - 1); //update index value in HashMap
	}
	//Use binary search to find the insertion position for the new payment, maintain the ascending order of the paymentsList
	private int findTargetPosition(Payment newPayment) {
		int left = 0;
		int right = paymentsList.size() - 1;
		while (left <= right) {
			int mid = left + (right - left) / 2;
			if (paymentsList.get(mid).compareTo(newPayment) == 0) {
				return mid;
			} else if (paymentsList.get(mid).compareTo(newPayment) < 0) {
				left = mid + 1;
			} else {
				right = mid - 1;
			}
		}
		return left;
	}
	//update the timestamp of an existing pair of person if the new incoming payment is more recent
	private void updatePayment(Payment newPayment) {
		int oldPosition = map.get(newPayment.names);
		if (newPayment.timestamp <= paymentsList.get(oldPosition).timestamp) {
			return;
		}
		int targetPosition = findTargetPosition(newPayment);
		paymentsList.add(targetPosition, newPayment);
		paymentsList.remove(map.get(newPayment.names));
		updateIndex(oldPosition, targetPosition - 1);
	}
	//update the index values in HashMap for the payments in a given range
	private void updateIndex(int start, int end) {
		for (int i = start; i <= end; i++) {
			map.put(paymentsList.get(i).names, i);
		}
	}
}
