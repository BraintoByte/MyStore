package MyProject.MyStore;

import java.io.IOException;

import MyProject.MyStore.BusinessData.StoreItem;
import MyProject.MyStore.Service.PrintService;
import MyProject.MyStore.Service.StoreFrontService;
import MyProject.MyStore.Utility.StoreFrontUtility;

public class App 
{
    public static void main( String[] args )
    {
    	PrintService printService = new PrintService("C:\\Users\\andy.gasparini\\eclipse-workspace\\MyStore\\ErrorMessagesLog\\");
    	StoreFrontService storeFrontService = new StoreFrontService();
    	StoreFront storeFront = new StoreFront(storeFrontService, printService, "My store front");
    	storeFront.runStoreFront();
    }
}