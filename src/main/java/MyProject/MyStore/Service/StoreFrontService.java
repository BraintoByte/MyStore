package MyProject.MyStore.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

import MyProject.MyStore.BusinessData.StoreItem;
import MyProject.MyStore.Utility.StoreFrontUtility;



public class StoreFrontService {

	public enum TYPES{
		CANDIES{
			@Override
			public String toString() {
				return "Candies";
			}
		},
		DESSERTS{
			@Override
			public String toString() {
				return "Desserts";
			}
		},
		HARDWARE{
			@Override
			public String toString() {
				return "Hardware";
			}
		},
		NONE {
			@Override
			public String toString() {
				return "None";
			}
		}
	}

	private HashMap<TYPES, ArrayList<StoreItem>> items = new HashMap<>();
	private ArrayList<StoreItem> desserts = new ArrayList<>();
	private ArrayList<StoreItem> hardware = new ArrayList<>();
	private ArrayList<StoreItem> candies = new ArrayList<>();

	public StoreFrontService() {
		allItemsToList(StoreFrontUtility.userDir + File.separator + "StoreInventory\\Candies");
		allItemsToList(StoreFrontUtility.userDir + File.separator + "StoreInventory\\Desserts");
		allItemsToList(StoreFrontUtility.userDir + File.separator + "StoreInventory\\Hardware");
	}

	private void allItemsToList(String folderPath) {

		ArrayList<StoreItem> itemsContainer = null;

		try {

			itemsContainer = StoreFrontUtility.getItemsInJsonFromFolder(folderPath);
			itemsContainer.forEach(item -> {

				String itemType = item.getType();

				if(itemType.equals(TYPES.HARDWARE.toString())) {
					hardware.add(item);
				}else if(itemType.equals(TYPES.DESSERTS.toString())) {
					desserts.add(item);
				}else if(item.getType().equals(TYPES.CANDIES.toString())) {
					candies.add(item);
				}
			});
		} catch (IOException e) {
			System.out.println("An error occurred while loading items");
		}

		items.put(TYPES.HARDWARE, hardware);
		items.put(TYPES.CANDIES, candies);
		items.put(TYPES.DESSERTS, desserts);

		return;
	}

	public Double calculateTotalCost(ArrayList<StoreItem> storeItems) {
		return storeItems.stream().map(item -> item.getPrice()).collect(Collectors.summingDouble(Double::doubleValue));
	}

	public HashMap<TYPES, ArrayList<StoreItem>> getItems() {
		return items;
	}

	public ArrayList<StoreItem> getHardware() {
		return hardware;
	}

	public ArrayList<StoreItem> getDesserts() {
		return desserts;
	}

	public ArrayList<StoreItem> getCandies() {
		return candies;
	}
}
