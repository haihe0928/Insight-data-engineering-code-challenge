package venmo;

//main method to run the whole program
public class Main {
	public static void main(String[] args) {
//set input file path and time window span
		UpdatePaymentsInTimeWindow updatePayments = new UpdatePaymentsInTimeWindow("venmo_input/venmo-trans.txt", 60);
		updatePayments.read();
		
	}

}
