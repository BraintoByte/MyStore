package MyProject.MyStore;

import java.util.ArrayList;
import java.util.Scanner;

import MyProject.MyStore.BusinessData.StoreItem;
import MyProject.MyStore.Service.PrintService;
import MyProject.MyStore.Service.StoreFrontService;
import MyProject.MyStore.Service.StoreFrontService.TYPES;
import MyProject.MyStore.Utility.StoreFrontUtility;

public class StoreFront {

	private enum MENUCATHEGORYLEVEL{
		MAIN,
		SUBMENU,
		DONE
	}

	private StoreFrontService storeFrontService;
	private String storeFrontName;
	private Scanner input;
	private ArrayList<StoreItem> chosenItems = new ArrayList<>();
	private MENUCATHEGORYLEVEL menuLevelTracker;
	private TYPES chosenType;
	private PrintService printService;

	public StoreFront(StoreFrontService storeFrontService, PrintService printService, String storeFrontName) {
		this.storeFrontService = storeFrontService;
		this.storeFrontName = storeFrontName;
		this.input = new Scanner(System.in);
		this.menuLevelTracker = main();
		this.printService = printService;
		this.chosenType = TYPES.NONE;
	}

	public void runStoreFront() {

		printService.printWelcomeMessage(this.storeFrontName);

		while(menuLevelTracker != done()) {
			printService.selectCathegoryMessage();
			String userInput = input.next();
			makeInitialChoiceFromInput(userInput);
			TYPES currentTypeChosen = chosenType;

			while(chosenType == currentTypeChosen) {
				printService.printSubmenu(storeFrontService.getItems().get(chosenType));
				userInput = input.next();
				makeSubMenuChoice(userInput);
			}
		}

		makeTheSalePrint();
	}


	private void makeInitialChoiceFromInput(String input) {

		switch(input) {
		case "1":
			chosenType = TYPES.CANDIES;
			break;
		case "2":
			chosenType = TYPES.DESSERTS;
			break;
		case "3":
			chosenType = TYPES.HARDWARE;
			break;
		default:
			printService.unknownChoice();
			break;
		}
	}

	private boolean isPrimaryChoice(String input) {
		switch(input) {
		case "q":
			System.exit(0);
			return true;
		case "b":
			backChoice();
			return true;
		case "d":
			menuLevelTracker = done();
			chosenType = TYPES.NONE;
			return true;
		}

		return false;
	}


	private void makeSubMenuChoice(String input) {

		menuLevelTracker = submenu();

		ArrayList<StoreItem> items = storeFrontService.getItems().get(chosenType);
		StoreItem[] itemsSelected = items.toArray(new StoreItem[items.size()]);

		if(!isPrimaryChoice(input)) {

			try {
				int itemArrayIndex = Integer.parseInt(input) - 1;
				if(itemArrayIndex > -1 && itemArrayIndex < itemsSelected.length) {

					StoreItem item = itemsSelected[Integer.parseInt(input) - 1];
					chosenItems.add(item);

					printService.printItemSelected(item.getName());

					return;
				}
			}catch(Exception ex) {
				printService.printErrorMessage("Something went wrong in inserting an item", ex);
				StoreFrontUtility.logErrorMessages(printService.getJSON_EXPORT_ERROR_FILE_PATH());
			}

			printService.itemToPrintDoesNotExist();
		}
	}


	private void makeTheSalePrint() {

		if(chosenItems.size() > 0) {
			double totalCost = storeFrontService.calculateTotalCost(chosenItems);
			printService.printCalculatedCostsAndItems(chosenItems, totalCost);
		}
		printService.goodBye();
	}

	private void backChoice() {
		if(menuLevelTracker == main()) {
			printService.noBackMenu();
		}else {
			menuLevelTracker = main();
			chosenType = TYPES.NONE;
		}
	}

	private MENUCATHEGORYLEVEL main() {
		return MENUCATHEGORYLEVEL.MAIN;
	}

	private MENUCATHEGORYLEVEL submenu() {
		return MENUCATHEGORYLEVEL.SUBMENU;
	}

	private MENUCATHEGORYLEVEL done() {
		return MENUCATHEGORYLEVEL.DONE;
	}
}