package MyProject.MyStore.Utility;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.joda.time.LocalDateTime;

import com.google.gson.Gson;

import MyProject.MyStore.BusinessData.StoreItem;
import MyProject.MyStore.Utility.Entity.ErrorEventEntity;

public class StoreFrontUtility {

	//	 This part is mainly to show "if you do really need to write something custom"

	private static String separator = File.separator;
	private static HashMap<String, ErrorEventEntity> errorMessages = new HashMap<>();
	public static String JSON_EXPORT_ERROR_FILE_PATH;
	public static String userDir = System.getProperty("user.dir");

	public static ArrayList<StoreItem> fileToObject(String filepath, String itemType, String outputFilePath) {

		ArrayList<Gson> jsonObjects = new ArrayList<>();
		ArrayList<StoreItem> storeItems = new ArrayList<>();
		File file = new File(filepath);

		try {
			HashMap<String, Double> items = readFile(file);

			items.keySet().forEach(itemKey -> {
				Double itemValue = items.get(itemKey);
				StoreItem storeItem = new StoreItem();
				storeItem.setName(itemKey);
				storeItem.setPrice(itemValue);
				storeItem.setType(itemType);
				storeItems.add(storeItem);
				try {
					writeToJson(outputFilePath + separator + storeItem.getName() + ".json", storeItem);
				} catch (IOException e) {
					e.printStackTrace();
				}
			});

		}catch(IOException ex) {
			System.out.println("There was an exception reading the file: " + ex.getMessage());
		}catch(ParseException e) {
			System.out.println("There was an exception parsing a numnber in the file: " + e.getMessage());
		}

		return storeItems;
	}

	public static ArrayList<StoreItem> getItemsInJsonFromFolder(String directoryPath) throws IOException{

		File directory = new File(directoryPath);
		File[] files = directory.listFiles();
		ArrayList<StoreItem> storeItems = new ArrayList<>();

		if(files.length == 0) {
			return storeItems;
		}

		for (int i = 0; i < files.length; i++) {
			StoreItem storeItem = jsonToObject(files[i].getPath());
			storeItems.add(storeItem);
		}

		return storeItems;
	}

	public static StoreItem jsonToObject(String jsonFilePath) throws IOException {

		List<String> allLines = Files.readAllLines(toPath(jsonFilePath));
		StringBuilder jsonToString = new StringBuilder();
		allLines.forEach(line -> {
			jsonToString.append(line);
		});

		Gson jsonObject = new Gson();
		StoreItem result = (StoreItem) jsonObject.fromJson(jsonToString.toString(), StoreItem.class);

		return result;
	}

	private static <T> void writeToJson(String finalFilePath, T jsonItem) throws IOException {

		File parentPath = new File(finalFilePath).getParentFile();

		if(parentPath.exists()) {
			FileWriter fileWriter = new FileWriter(new File(finalFilePath));
			Gson jsonObject = new Gson();
			jsonObject.toJson(jsonItem, fileWriter);
			fileWriter.flush();
			fileWriter.close();
		}
	}

	// Make a method to read the file, in this case I made my own custom since it's just 2 properties
	// But you can really get a file from anything, excel or whatever
	private static HashMap<String, Double> readFile(File file) throws IOException, ParseException {

		Path path = file.toPath();
		List<String> lines = Files.readAllLines(path);
		HashMap<String, Double> fileResult = new HashMap<String, Double>();

		lines.forEach(line -> {
			String[] splittedString = line.split(":");
			fileResult.put(splittedString[0], Double.parseDouble(splittedString[1]));
		});

		return fileResult;
	}

	protected static Path toPath(String path) {
		return new File(path).toPath();
	}

	public static String getDate() {
		LocalDateTime dateTime = new LocalDateTime();

		return dateTime.toString();
	}

	public static void addErrorMessage(String customMessage, String stackTrace) {
		String date = StoreFrontUtility.getDate();
		ErrorEventEntity errorEvent = new ErrorEventEntity();

		errorEvent.setDate(date);
		errorEvent.setCustomMessage(customMessage);
		errorEvent.setErrorMessage(stackTrace);

		errorMessages.put(date, errorEvent);
	}

	public static void logErrorMessages(String path) {
		errorMessages.keySet().forEach(date -> {
			try {
				String finalFilePath = path + separator + date.replace(":", "-").replace("T", "") + ".json";
				writeToJson(finalFilePath, errorMessages.get(date));
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

	public static String errorMessageToString(Exception exception) {
		String errorMessage = "";
		StackTraceElement[] stackTrace = exception.getStackTrace();
		for (int i = 0; i < stackTrace.length; i++) {
			errorMessage += stackTrace[i].toString() + "\n";
		}
		
		return errorMessage;
	}
}