import java.util.Scanner;
import java.util.ArrayList;
import java.io.FileReader;
import java.io.FileOutputStream;
import java.util.*;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

class FoodClass {
    File newFile = new File("billingDetails.csv");
    static Scanner sc1 = new Scanner(System.in);
    static ArrayList<ArrayList<String>> Menu = new ArrayList<ArrayList<String>>();
    static ArrayList<ArrayList<String>> foodPlan = new ArrayList<ArrayList<String>>();

    //Reading the file
    static void Readingfile(String fileName, int n) {
        try {
            String line = "";
            Scanner sc = new Scanner(new FileReader(fileName));
            while ((sc.hasNext())) {
                line = sc.nextLine();
                String[] FoodItems1 = line.split(",");
                List<String> fixedLenghtList = Arrays.asList(FoodItems1);
                ArrayList<String> Fooditems = new ArrayList<String>(fixedLenghtList);
                if (n == 0)
                    Menu.add(Fooditems);
                else
                    foodPlan.add(Fooditems);
            }
            sc.close();
        } catch (Exception e) {
            System.out.println("Runtime Error");
        }
    }

    void AddingIntoFile() {
        try {
            newFile.delete();
            if (newFile.createNewFile()){
                System.out.println("New file created");
            }
            for (ArrayList<String> arrayList : foodPlan) {
                String listString = String.join(",", arrayList);
                listString += "\n";
                byte[] byteInput = listString.getBytes();
                FileOutputStream File = new FileOutputStream("billingDetails.csv",
                        true);
                File.write(byteInput);
                File.close();
            }
        } catch (Exception e) {
            System.out.println();
        }
    }

    // For creating the new order
    void CreatingNewOrder() {
        String st = "\n", Food = "";
        ArrayList<String> dummy = foodPlan.get(foodPlan.size() - 1);
        int lastIndex = Integer.parseInt(dummy.get(0));
        double totalAmount = 0;
        int i = 1, it, count;
        while (true) {
            System.out.println();
            System.out.println("Enter Order Details");
            System.out.print("Item:" + i + "  Enter ItemId : ");
            it = sc1.nextInt();
            System.out.print("Item:" + i + "  Quantity : ");
            count = sc1.nextInt();
            i++;
            totalAmount += ClaculateAmount(it, count);
            Food += String.valueOf(it) + " " + String.valueOf(count) + " ";
            System.out.println();
            System.out.println("Press y to palce Another order");
            char c = sc1.next().charAt(0);
            if (c != 'y')
                break;
        }
        LocalDateTime Date = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-dd-MMM");
        String formattedDate = Date.format(myFormatObj);
        st += String.valueOf((lastIndex + 1)) + "," + formattedDate + "," + String.valueOf(totalAmount) + ',' + Food;
        String[] m = Food.split(" ");
        st += ConfirmOrder(m);
        sc1.nextLine();
        try {
            byte[] ByteInput = st.getBytes();
            FileOutputStream FileWrite = new FileOutputStream("billingDetails.csv", true);
            FileWrite.write(ByteInput);
            foodPlan.clear();
            Readingfile("billingDetails.csv", 1);
            FileWrite.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //For calculating the amount
    static double ClaculateAmount(int id, int q) {
        double v = 0;
        for (int i = 0; i < Menu.size(); i++) {
            ArrayList<String> dump = Menu.get(i);
            if (id == Integer.parseInt(dump.get(0))) {
                v += (q * Integer.parseInt(dump.get(2)));
            }
        }
        return v;
    }
    //For confirming the order
    static String ConfirmOrder(String m[]) {
        int Amount = 0;
        System.out.println();
        System.out.println("Your Ordered Items");
        for (int k = 0; k < m.length; k += 2) {
            for (int j = 0; j < Menu.size(); j++) {
                ArrayList<String> dump = Menu.get(j);
                if (Integer.parseInt(m[k]) == Integer.parseInt(dump.get(0))) {
                    System.out.println("[ " + dump.get(1) + "    ||  Cost  :  Rs."
                            + Integer.parseInt(m[k + 1]) * Integer.parseInt(dump.get(2)) + " ||  Qty : "
                            + Integer.parseInt(m[k + 1]) + " ]");
                    Amount += Integer.parseInt(m[k + 1]) * Integer.parseInt(dump.get(2));
                }
            }
        }
        System.out.println();
        System.out.println("Total Bill : Rs." + Amount);
        System.out.println();
        System.out.println("Enter 'y' to Confirm order");
        char c = sc1.next().charAt(0);
        if (c == 'y')
            return ",Approved";
        return ",Cancelled";
    }
    //To check the bill
    void CheckBillStatus() {
        System.out.print("Enter order Id : ");
        int n = sc1.nextInt();
        for (int j = 0; j < foodPlan.size(); j++) {
            ArrayList<String> dump = foodPlan.get(j);
            if (n == Integer.parseInt(dump.get(0))) {
                System.out.println(dump);
                System.out.println("press 'p' to place Order");
                char c = sc1.next().charAt(0);
                if (c == 'p')
                    foodPlan.get(j).set(4, "Approved");
                else
                    foodPlan.get(j).set(4, "Cancelled");
            }
        }
        sc1.nextLine();
        AddingIntoFile();
    }
    //for the day collection
    void CollectionOfDay() {
        System.out.print("Enter the date in Format year-day-month :");
        String date = sc1.nextLine();
        double Coll = 0.0;
        for (ArrayList<String> dump : foodPlan) {
            if ((dump.get(1)).equals(date)) {
                Coll += (Double.parseDouble(dump.get(2)));
            }
        }
        System.out.println(date + " day's Total Collection is  Rs." + Coll);
    }

    FoodClass() {
        Readingfile("menuList.csv", 0);
        Readingfile("billingDetails.csv", 1);
    }
}

// Main class of the App
class RestaurantAppTask {
    static Scanner sc = new Scanner(System.in);
    static String menuItems[] = { "New Order", "Bill Status", "Day collection" };
    static FoodClass orderA = new FoodClass();

    static void Menulist() {
        System.out.println();
        System.out.println("Welcome to the 'Driven Cafe' , Hyderabad");
        for (int i = 0; i < 3; i++)
            System.out.println((i + 1) + "." + menuItems[i]);
    }

    static void ExecutingMenu() {
        System.out.println();
        System.out.print("Enter your Choice : ");
        int item = sc.nextInt();
        if (item == 1){
            orderA.CreatingNewOrder();
        }
        else if (item == 2){
            orderA.CheckBillStatus();
        }
        else if (item == 3) {
            orderA.CollectionOfDay();
        } else
            System.out.println("Entered out of the choice");
    }

    public static void main(String[] args) {
        while (true) {

            ExecutingMenu();
            Menulist();
            System.out.println("Press 'y' to return 'Main_Menu'");

            char ch = sc.next().charAt(0);
                if (ch != 'y')
                    break;
        }
    }
}