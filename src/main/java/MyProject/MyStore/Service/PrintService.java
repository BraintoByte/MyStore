package MyProject.MyStore.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import MyProject.MyStore.BusinessData.StoreItem;
import MyProject.MyStore.Service.StoreFrontService.TYPES;
import MyProject.MyStore.Utility.StoreFrontUtility;

public class PrintService {

	private String JSON_EXPORT_ERROR_FILE_PATH;

	public PrintService( String jsonExportErrorFilePath) {
		this.JSON_EXPORT_ERROR_FILE_PATH = jsonExportErrorFilePath;
	}

	public void printWelcomeMessage(String storeFrontName) {
		System.out.println("Welcome to " + storeFrontName);
	}

	public void selectCathegoryMessage() {
		String typesToString = getTypesValues();
		System.out.println("Please select which cathegory you want, press b to go back, q to quit, d for done\n\n" + typesToString);
	}

	private String getTypesValues() {

		TYPES[] typesValues = TYPES.values();

		String typesToString = "";

		for (int i = 0; i < typesValues.length; i++) {
			if(typesValues[i] != TYPES.NONE) {
				typesToString += (i + 1) + ". " + typesValues[i].toString() + "\n";
			}
		}

		return typesToString;
	}

	public void printSubmenu(ArrayList<StoreItem> subMenuItems) {
		System.out.println("These are the items we offer: ");

		int i = 0;

		for (StoreItem item : subMenuItems) {
			String itemName = item.getName();
			System.out.println((i + 1) + ". " + itemName);
			i++;
		}

		System.out.print("Please select an item: ");
	}

	public void printCalculatedCostsAndItems(ArrayList<StoreItem> chosenItems, double totalCost) {
		System.out.println("Your cart: ");

		chosenItems.forEach(item -> {
			System.out.println("Item: " + item.getName() + " Price: $" + item.getPrice());
		});

		System.out.println("Your total cost is: $" + totalCost);
	}

	public void printErrorMessage(String customMessage, Exception exception) {
		String errorMessageStackTrace = StoreFrontUtility.errorMessageToString(exception);
		StoreFrontUtility.addErrorMessage(customMessage, errorMessageStackTrace);
		System.out.println(customMessage);
	}

	public void printItemSelected(String itemName) {
		System.out.println("You have selected item: " + itemName);
	}

	public void itemToPrintDoesNotExist() {
		System.out.println("Item selected does not exists!");
	}

	public void noBackMenu() {
		System.out.println("No back menu");
	}

	public void unknownChoice() {
		System.out.println("Uknown choice!");
	}

	public void goodBye() {
		System.out.println("GoodBye!");
	}

	public String getJSON_EXPORT_ERROR_FILE_PATH() {
		return JSON_EXPORT_ERROR_FILE_PATH;
	}
}