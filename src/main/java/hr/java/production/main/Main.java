package hr.java.production.main;

import hr.java.production.enumeration.Gradovi;
import hr.java.production.exception.SameArticleException;
import hr.java.production.exception.SameNameCategory;
import hr.java.production.genericsi.FoodStore;
import hr.java.production.genericsi.TechnicalStore;
import hr.java.production.model.*;
import hr.java.production.sort.ProductionSorter;


import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Used for running application
 */
public class Main {
    private static final int NUMBER_OF_CATEGORIES = 3;
    private static final int NUMBER_OF_ITEMS = 2;
    private static final int NUMBER_OF_FACTORIES = 1;
    private static final int NUMBER_OF_STORES = 1;
    private static final int NUMBER_OF_SPEZIALED_STORES = 2;
    private static final int NUMBER_OF_LINES_FOR_CATEGORY = 3;
    private static final int NUMBER_OF_LINES_FOR_FACTORY = 4;
    private static final Integer NUMBER_OF_LINES_FOR_ADDRESS = 5;

    private static final String FILE_NAME_CATEGORIES = "dat/categories.txt";
    private static final String FILE_NAME_ITEMS = "dat/items.txt";
    private static final String FILE_NAME_ADDRESS = "dat/addresses.txt";
    private static final String FILE_NAME_FACTORIES = "dat/factories.txt";
    private static final String FILE_NAME_STORES = "dat/stores.txt";
    private static final String FACTORY_SERIALIZATION_FILE_NAME= "dat/factories.ser";
    private static final String STORE_SERIALIZATION_FILE_NAME= "dat/stores.ser";



    public static void main(String[] args) throws FileNotFoundException {


        List<Category> categories = new ArrayList<>();
        List<Item> items = new ArrayList<>();
        List<Factory> factories = new ArrayList<>();
        List<Store> stores = new ArrayList<>();
        List<Address> addresses = new ArrayList<>();
        Scanner input = new Scanner(System.in);


        List<Laptop> laptopi = new ArrayList<>();
        List<Edible> foodStore = new ArrayList<>();


        for(Category cat :fetchCategoriesFromFile().get() ){
            categories.add(cat);
        }

        for(Item item :fetchItemsFromFile(categories).get() ){
            items.add(item);
        }


        for(Address address : fetchAddressFromFile().get()){
            addresses.add(address);
        }

        for(Factory fact : fetchFactoriesFromFile(addresses, items).get()){
            factories.add(fact);
        }

        for(Store store : fetchStoresFromFile(items).get()){
            stores.add(store);
        }

        for(Store st : stores){
            System.out.println(st.getItems());
        }

        try (ObjectOutputStream objectWriterForFactory = new ObjectOutputStream(new FileOutputStream(FACTORY_SERIALIZATION_FILE_NAME))){
            objectWriterForFactory.writeObject(factories.stream().filter(f->f.getItems().size()>=5).collect(Collectors.toList()));
        } catch (IOException e) {
            e.printStackTrace();
        }



        try (ObjectOutputStream objectWriterForStore = new ObjectOutputStream(new FileOutputStream(STORE_SERIALIZATION_FILE_NAME))){
            objectWriterForStore.writeObject(stores.stream().filter(s->s.getItems().size()>=5).collect(Collectors.toList()));
        } catch (IOException e) {
            e.printStackTrace();
        }



        Boolean found = true;
        Integer brojac = 0;

        while (found) {
            if (items.get(brojac) instanceof Laptop) {
                shortestWaranty(items);
                found = false;
            }
            brojac += 1;
            if (brojac == items.size()) {
                found = false;
            }
        }

        Integer simC = 0;

        for (Item item : items) {
            if ((item instanceof Rice || item instanceof Ananas) && simC == 0) {
                mostKcal(items);
                mostExpensive(items);
                simC = 1;
            }

        }


        BigDecimal[] cheapestStore = new BigDecimal[NUMBER_OF_STORES];

        for (int i = 0; i < NUMBER_OF_STORES; i++) {

           // stores.add(insertNewStore(input, items));

            BigDecimal[] costs = new BigDecimal[stores.get(i).getItems().size()];
            BigDecimal lowestCost;
            Integer count = 0;

            for (Item item : stores.get(i).getItems()) {
                System.out.println(item.getName());
                costs[count] = item.getSellingPrice();
                count += 1;
            }

            lowestCost = costs[0];

            for (int j = 0; j < costs.length; j++) {
                if (costs[j].compareTo(lowestCost) == -1) {
                    lowestCost = costs[j];
                }
            }

            cheapestStore[i] = lowestCost;
        }


        BigDecimal lowestFactory = cheapestStore[0];
        Integer indexStore = 0;


        for (int i = 0; i < cheapestStore.length; i++) {
            if (cheapestStore[i].compareTo(lowestFactory) == -1) {
                indexStore = i;
            }
        }

        System.out.println("Trgovina koja prodaje najjeftiniji artikl je:  " + stores.get(indexStore).getName());


        BigDecimal[] maxVolumesOfFactories = new BigDecimal[NUMBER_OF_FACTORIES];





        for (int i = 0; i < NUMBER_OF_FACTORIES; i++) {
//            factories.add(insertNewFactory(input, items, stores));


            BigDecimal maxV;
            BigDecimal[] vol = new BigDecimal[factories.get(i).getItems().size()];
            Integer counter = 0;

            for (Item item : factories.get(i).getItems()) {
                vol[counter] = (item.getHeight()).multiply((item.getLength())).multiply((item.getWidth()));
                counter += 1;
            }

            maxV = vol[0];

            for (int j = 0; j < vol.length; j++) {
                if (vol[j].compareTo(maxV) == 1) {
                    maxV = vol[j];
                }
            }

            maxVolumesOfFactories[i] = maxV;
        }


        BigDecimal bigFactory = maxVolumesOfFactories[0];
        Integer indexFactory = 0;
        for (int i = 0; i < maxVolumesOfFactories.length; i++) {
            if (maxVolumesOfFactories[i].compareTo(bigFactory) == 1) {
                indexFactory = i;
            }
        }

        System.out.println("Tvronica koja proizvodi artikl sa najvecim volumenom je: " + factories.get(indexFactory).getName());

        Map<Category, List<Item>> mapaItema = new HashMap<>();
        List<Category> listOfCategoriesInMap = new ArrayList<>();

        for (Item item : items) {
            if (mapaItema.containsKey(item.getCategory())) {
                mapaItema.get(item.getCategory()).add(item);
            } else {
                List<Item> _items = new ArrayList<>();
                _items.add(item);
                mapaItema.put(item.getCategory(), _items);
                listOfCategoriesInMap.add(item.getCategory());
            }
        }


        for (Category cat : listOfCategoriesInMap) {
            System.out.println(mapaItema.get(cat));
            Collections.sort(mapaItema.get(cat), new ProductionSorter());
            System.out.println("Najskuplji item ove kategorije je: " + mapaItema.get(cat).get(mapaItema.get(cat).size() - 1).getName());
            System.out.println("Najjeftinji item ove kategorije je: " + mapaItema.get(cat).get(0).getName());

        }


        List<Item> EdibleAndTechnical = new ArrayList<>();
        for (Item item : items) {
            if (item instanceof Ananas || item instanceof Rice || item instanceof Laptop) {
                EdibleAndTechnical.add(item);
            }
        }

        Collections.sort(EdibleAndTechnical, new ProductionSorter());
        System.out.println("Najskuplji artikl koji nasljeđuje sučelja Edible i Technical je: " + EdibleAndTechnical.get(EdibleAndTechnical.size() - 1).getName());
        System.out.println("Najjeftiniji artikl koji nasljeđuje sučelja Edible i Technical je: " + EdibleAndTechnical.get(0).getName());


        TechnicalStore<Laptop> StoreTech = new TechnicalStore<Laptop>("Trgovina tehnike", "www.tech.com", items, laptopi);
        FoodStore<Edible> storeFood = new FoodStore<>("Trgovina hrane", "www.ananas.com", items, foodStore);


        Set<Item> itemiStora = new HashSet<>();
        for (Store store : stores) {
            itemiStora.addAll(store.getItems());
        }


        itemiStora.addAll(storeFood.getItems());
        itemiStora.addAll(StoreTech.getItems());

        List<Item> itemiStoreList = new ArrayList<>();
        itemiStoreList.addAll(itemiStora);

        itemiStoreList.sort((Item it1, Item it2) -> it1.getVolume().compareTo(it2.getVolume()));


        for (Item item : itemiStoreList) {
            System.out.println(item.getName());
        }
        BigDecimal vol = new BigDecimal("0");
        for (Item item : items) {
            vol = vol.add(item.getVolume());

        }
        BigDecimal avgVol = vol.divide(new BigDecimal(items.size()));
        System.out.println("Average volume is: " + avgVol);
        long startTime1 = System.nanoTime();

        OptionalDouble avgPrice = items.stream().filter(Item -> Item.getVolume().compareTo(avgVol) == 1).mapToDouble(a -> a.getSellingPrice().doubleValue()).average();
        long endTime1 = System.nanoTime();
        long duration1 = (endTime1 - startTime1);


        int numberOfArticles = 0;
        for (Store store : stores) {
            numberOfArticles = numberOfArticles + store.getItems().size();
        }
        numberOfArticles = numberOfArticles + storeFood.getItems().size() + StoreTech.getItems().size();


        Integer avgArticles = numberOfArticles / stores.size() + NUMBER_OF_SPEZIALED_STORES;
        long startTime3 = System.nanoTime();

        List<Store> aboveAverageStores = stores.stream().filter(number -> number.getItems().size() > avgArticles).collect(Collectors.toList());
        long endTime3 = System.nanoTime();
        long duration3 = (endTime3 - startTime3);


        BigDecimal tmp1 = new BigDecimal("0");
        int ct1 = 0;
        long startTime2 = System.nanoTime();

        for (Item item : items) {
            if (item.getVolume().compareTo(avgVol) == 1) {
                tmp1.add(item.getSellingPrice());
                ct1++;
            }

        }
       // BigDecimal avgPrice2 = tmp1.divide(BigDecimal.valueOf(ct1));
        long endTime2 = System.nanoTime();
        long duration2 = (endTime2 - startTime2);


        System.out.println("Duratino for average price with lambda: " + duration1 + " Duration with for: " + duration1);

        long startTime4 = System.nanoTime();
        List<Store> aboveAverageStores2 = new ArrayList<>();
        for (Store store : stores) {
            if (store.getItems().size() > avgArticles) {
                aboveAverageStores.add(store);
            }
        }
        long endTime4 = System.nanoTime();
        long duration4 = (endTime4 - startTime4);

        System.out.println("Duration for above average number of article in store with lambda: " + duration3 + " Duration with for: " + duration4);


        Optional<List<Item>> filteredList = Optional.of(items.stream()
                .filter(item -> item.getDiscount().discountAmount().compareTo(new BigDecimal("0")) == 1).toList());


        System.out.println("Broj artikala u svakoj trgovini");
        stores.stream().map(store -> store.getItems().size()).forEach(System.out::println);
        System.out.println("Artikli trgovine: ");
        stores.stream().map(Store::getItems).forEach(System.out::println);


        //End of main function

    }


    private static Optional<List<Category>> fetchCategoriesFromFile() {
        File categoryFile = new File(FILE_NAME_CATEGORIES);
        List<Category> categoryList = new ArrayList<>();
        Optional<Long> id = Optional.empty();
        Optional<String> name = Optional.empty();
        Optional<String> description = Optional.empty();
        try (BufferedReader categoriesFileReader = new BufferedReader(new FileReader(categoryFile))) {
            String line;
            Integer lineCounter = 1;


            while ((line = categoriesFileReader.readLine()) != null) {
                if (lineCounter % NUMBER_OF_LINES_FOR_CATEGORY == 1) {
                    id = Optional.of(Long.parseLong(line));
                } else if (lineCounter % NUMBER_OF_LINES_FOR_CATEGORY == 2) {
                    name = Optional.of(line);
                } else if (lineCounter % NUMBER_OF_LINES_FOR_CATEGORY == 0) {
                    description = Optional.of(line);
                    Category cat = new Category(name.get(), description.get(), id.get());
                    categoryList.add(cat);
                }
                lineCounter++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        if (categoryList.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(categoryList);
        }
    }

    private static Optional<List<Item>> fetchItemsFromFile(List<Category> categories) {
        File itemsFile = new File(FILE_NAME_ITEMS);
        List<Item> itemList = new ArrayList<>();
        String type;
        String line;

        Integer objectCount = 1;
        try (BufferedReader itemsFileReader = new BufferedReader(new FileReader(itemsFile))) {

            while ((type = itemsFileReader.readLine()) != null) {


                String name = itemsFileReader.readLine();
                String cat = itemsFileReader.readLine();
                BigDecimal width = new BigDecimal(itemsFileReader.readLine());
                BigDecimal height = new BigDecimal(itemsFileReader.readLine());
                BigDecimal length = new BigDecimal(itemsFileReader.readLine());
                BigDecimal productionCost = new BigDecimal(itemsFileReader.readLine());
                BigDecimal sellingPrice = new BigDecimal(itemsFileReader.readLine());
                BigDecimal discount = new BigDecimal(itemsFileReader.readLine());
                Long id = Long.parseLong(itemsFileReader.readLine());
                Optional<Integer> caloriesPerKg = Optional.empty();
                Optional<BigDecimal> weight = Optional.empty();

                Optional<Integer> waranty = Optional.empty();

                Category categ = categories.stream().filter(ctg -> ctg.getName().toLowerCase(Locale.ROOT).equals(cat)).findAny().get();


                if (type.equals("ananas")) {
                    caloriesPerKg = Optional.of(Integer.parseInt(itemsFileReader.readLine()));
                    weight = Optional.of(new BigDecimal(itemsFileReader.readLine()));

                    itemList.add(new Ananas(weight.get(), caloriesPerKg.get(), name, categ, width, height, length, productionCost, sellingPrice, discount, id));
                } else if (type.equals("rice")) {
                    caloriesPerKg = Optional.of(Integer.parseInt(itemsFileReader.readLine()));
                    itemList.add(new Rice(weight.get(), caloriesPerKg.get(), name, categ, width, height, length, productionCost, sellingPrice, discount, id));

                } else if (type.equals("laptop")) {
                    waranty = Optional.of(Integer.parseInt(itemsFileReader.readLine()));
                    itemList.add(new Laptop(waranty.get(), name, categ, width, height, length, productionCost, sellingPrice, discount, id));
                } else {
                    itemList.add(new Item(name, categ, width, height, length, productionCost, sellingPrice, discount, id));
                }

            }


        } catch (IOException e) {
            e.printStackTrace();
        }


        if (itemList.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(itemList);
        }
    }


    /**
     * Used for finding shortest waranty of items declared as instances of class latpop
     *
     * @param items List of items in which we need to find min
     */

    private static void shortestWaranty(List<Item> items) {
        Integer minWaranty = -1;
        Optional<Laptop> minLaptop = Optional.empty();
        Integer brojac = 0;
        Boolean found = true;

        while (found) {
            if (items.get(brojac) instanceof Laptop laptop) {
                minWaranty = laptop.getWaranty();
                minLaptop = Optional.of(laptop);
                found = false;
            }
            brojac += 1;
            if (brojac == items.size()) {
                found = false;
            }
        }

        for (Item item : items) {

            if (item instanceof Laptop laptop) {
                if (laptop.getWaranty() < minWaranty) {
                    minWaranty = laptop.getWaranty();
                    minLaptop = Optional.of(laptop);
                }
            }
        }


        System.out.println("Laptop sa najkracom garancijom je " + minLaptop.get().getName() + " sa: " + minLaptop.get().getWaranty() + " godina garancije");


    }


    /**
     * Used to input all parameters from console to generate  instance of class Laptop
     *
     * @param input      Scanner used to input fields from console
     * @param categories List of all avaliable categories for user to choose from
     * @return Returns new Item with subclass of laptop
     */
    private static Item insertNewLaptop(Scanner input, List<Category> categories) {
        System.out.println("Unesite  kategoriju: ");
        Integer chosenCategory = -1;

        boolean nastaviPetlju = false;
        do {
            try {

                for (int i = 0; i < NUMBER_OF_CATEGORIES; i++) {
                    System.out.println("Za odabir " + categories.get(i).getName() + " kategorije pritisnite " + (i + 1));
                }
                chosenCategory = input.nextInt();
                if (chosenCategory < 1 || chosenCategory > NUMBER_OF_CATEGORIES) {
                    System.out.println("Pogrešan unos, pokušajte ponovo!");
                }
                nastaviPetlju = false;

            } catch (InputMismatchException ex1) {

                System.out.println("Morate unijeti brojčane vrijednosti.");
                input.nextLine();
                nastaviPetlju = true;
            }

        } while (nastaviPetlju || (chosenCategory < 1 || chosenCategory > 3));
        input.nextLine();


        System.out.print("Unesite ime artikla: ");
        String name = input.nextLine();


        Optional<Integer> waranty = Optional.empty();

        do {
            try {

                System.out.print("Unesite rok trajanja garancije laptopa: ");
                waranty = Optional.of(input.nextInt());
                nastaviPetlju = false;

            } catch (InputMismatchException ex1) {

                System.out.println("Morate unijeti brojčane vrijednosti.");
                input.nextLine();
                nastaviPetlju = true;
            }

        } while (nastaviPetlju);


        BigDecimal discount = new BigDecimal("0");
        do {
            try {

                System.out.print("Unesite discount laptopa: ");
                discount = input.nextBigDecimal();
                nastaviPetlju = false;

            } catch (InputMismatchException ex1) {

                System.out.println("Morate unijeti brojčane vrijednosti.");
                input.nextLine();
                nastaviPetlju = true;
            }

        } while (nastaviPetlju);


        BigDecimal width = new BigDecimal("0");
        do {
            try {

                System.out.print("Unesite width laptopa: ");
                width = input.nextBigDecimal();
                nastaviPetlju = false;

            } catch (InputMismatchException ex1) {
                System.out.println("Morate unijeti brojčane vrijednosti.");
                input.nextLine();
                nastaviPetlju = true;
            }

        } while (nastaviPetlju);

        BigDecimal height = new BigDecimal("0");
        do {
            try {

                System.out.print("Unesite height laptopa: ");
                height = input.nextBigDecimal();
                nastaviPetlju = false;

            } catch (InputMismatchException ex1) {
                System.out.println("Morate unijeti brojčane vrijednosti.");
                input.nextLine();
                nastaviPetlju = true;
            }

        } while (nastaviPetlju);

        BigDecimal length = new BigDecimal("0");
        do {
            try {

                System.out.print("Unesite length laptopa: ");
                length = input.nextBigDecimal();
                nastaviPetlju = false;

            } catch (InputMismatchException ex1) {
                System.out.println("Morate unijeti brojčane vrijednosti.");
                input.nextLine();
                nastaviPetlju = true;
            }

        } while (nastaviPetlju);

        BigDecimal productionCost = new BigDecimal("0");
        do {
            try {

                System.out.print("Unesite productionCost laptopa: ");
                productionCost = input.nextBigDecimal();
                nastaviPetlju = false;

            } catch (InputMismatchException ex1) {
                System.out.println("Morate unijeti brojčane vrijednosti.");
                input.nextLine();
                nastaviPetlju = true;
            }

        } while (nastaviPetlju);


        BigDecimal sellingPrice = new BigDecimal("0");
        do {
            try {
                System.out.print("Unesite sellingPrice laptopa: ");
                sellingPrice = input.nextBigDecimal();
                nastaviPetlju = false;

            } catch (InputMismatchException ex1) {
                System.out.println("Morate unijeti brojčane vrijednosti.");
                input.nextLine();
                nastaviPetlju = true;
            }

        } while (nastaviPetlju);


        input.nextLine();
        return new Laptop(waranty.get(), name, categories.get(chosenCategory - 1), width, height, length, productionCost, sellingPrice, discount, categories.get(chosenCategory - 1).getId());


    }

    /**
     * Used to calculate which of the edible items have most kilocalories
     *
     * @param items List of items which contains all items generated from user input
     */
    private static void mostKcal(List<Item> items) {
        Integer maxRiceKcal = -1;
        Integer maxAnanasKcal = -1;
        Optional<Rice> maxRice = Optional.empty();
        Optional<Ananas> maxAnanas = Optional.empty();
        Integer brojac = 0;
        Boolean found = true;

        while (found) {
            if (items.get(brojac) instanceof Rice rice) {
                maxRiceKcal = rice.calculateKilocalories();
                maxRice = Optional.of(rice);
                found = false;
            }
            brojac += 1;
            if (brojac == items.size()) {
                found = false;
            }
        }
        brojac = 0;
        found = true;

        while (found) {
            if (items.get(brojac) instanceof Ananas ananas) {
                maxAnanasKcal = ananas.calculateKilocalories();
                maxAnanas = Optional.of(ananas);
                ;
                found = false;

            }
            brojac += 1;
            if (brojac == items.size()) {
                found = false;
            }
        }


        for (Item item : items) {

            if (item instanceof Rice rice) {
                if (rice.calculateKilocalories() > maxRiceKcal) {
                    maxRiceKcal = rice.calculateKilocalories();
                    maxRice = Optional.of(rice);
                    ;
                }
            }
        }

        for (Item item : items) {
            if (item instanceof Ananas ananas) {
                if (ananas.calculateKilocalories() > maxAnanasKcal) {
                    maxAnanasKcal = ananas.calculateKilocalories();
                    maxAnanas = Optional.of(ananas);
                    ;
                }
            }
        }
        if (maxAnanasKcal > maxRiceKcal) {
            System.out.println("Artikl sa najvise kcal je " + maxAnanas.get().getName() + "sa : " + maxAnanas.get().calculateKilocalories() + " kcal");
        } else {
            System.out.println("Artikl sa najvise kcal je " + maxRice.get().getName() + "sa : " + maxRice.get().calculateKilocalories() + " kcal");
        }
    }


    /**
     * Used to calculate most expensive items from all avaliable items
     *
     * @param items List of items which contains all items generated from user input
     */
    private static void mostExpensive(List<Item> items) {
        BigDecimal maxRicePrice = new BigDecimal("-1.0");
        BigDecimal maxAnanasPrice = new BigDecimal("-1.0");
        Optional<Rice> maxRice = Optional.empty();
        Optional<Ananas> maxAnanas = Optional.empty();
        Integer brojac = 0;
        Boolean found = true;

        while (found) {
            if (items.get(brojac) instanceof Rice rice) {
                maxRicePrice = rice.calculatePrice();
                maxRice = Optional.of(rice);
                found = false;
            }
            brojac += 1;
            if (brojac == items.size()) {
                found = false;
            }
        }
        brojac = 0;
        found = true;

        while (found) {
            if (items.get(brojac) instanceof Ananas ananas) {
                maxAnanasPrice = ananas.calculatePrice();
                maxAnanas = Optional.of(ananas);
                found = false;

            }
            brojac += 1;
            if (brojac == items.size()) {
                found = false;
            }
        }


        for (Item item : items) {

            if (item instanceof Rice rice) {
                if ((rice.calculatePrice().compareTo(maxRicePrice)) == 1) {
                    maxRicePrice = rice.calculatePrice();
                    maxRice = Optional.of(rice);
                }
            }
        }

        for (Item item : items) {
            if (item instanceof Ananas ananas) {
                if (ananas.calculatePrice().compareTo(maxAnanasPrice) == 1) {
                    maxAnanasPrice = ananas.calculatePrice();
                    maxAnanas = Optional.of(ananas);
                }
            }
        }
        if (maxAnanasPrice.compareTo(maxRicePrice) == 1) {
            System.out.println("Artikl sa najvisom cijenom je  " + maxAnanas.get().getName() + "sa : " + maxAnanas.get().calculatePrice() + " hrk");
        } else {
            System.out.println("Artikl sa najvisom cijenom je " + maxRice.get().getName() + "sa : " + maxRice.get().calculatePrice() + " hrk");
        }
    }


    /**
     * Used to input all parameters from console to generate  instance of class Rice
     *
     * @param input      Scanner used to input fields from console
     * @param categories List of all avaliable categories for user to choose from
     * @return Returns new item of class Rice
     */

    private static Rice insertNewRice(Scanner input, List<Category> categories) {
        System.out.println("Unesite  kategoriju: ");
        Integer chosenCategory = -1;

        boolean nastaviPetlju = false;
        do {
            try {

                for (int i = 0; i < NUMBER_OF_CATEGORIES; i++) {
                    System.out.println("Za odabir " + categories.get(i).getName() + " kategorije pritisnite " + (i + 1));
                }
                chosenCategory = input.nextInt();
                if (chosenCategory < 1 || chosenCategory > NUMBER_OF_CATEGORIES) {
                    System.out.println("Pogrešan unos, pokušajte ponovo!");
                }
                nastaviPetlju = false;

            } catch (InputMismatchException ex1) {
                System.out.println("Morate unijeti brojčane vrijednosti.");
                input.nextLine();
                nastaviPetlju = true;
            }

        } while (nastaviPetlju || (chosenCategory < 1 || chosenCategory > 3));
        input.nextLine();


        System.out.print("Unesite ime artikla: ");
        String name = input.nextLine();


        BigDecimal weight = new BigDecimal("0");
        do {
            try {

                System.out.print("Unesite weight riže: ");
                weight = input.nextBigDecimal();
                nastaviPetlju = false;

            } catch (InputMismatchException ex1) {
                System.out.println("Morate unijeti brojčane vrijednosti.");
                input.nextLine();
                nastaviPetlju = true;
            }

        } while (nastaviPetlju);


        Integer kcal = 0;
        do {
            try {

                System.out.print("Unesite broj kilokalorija riže: ");
                kcal = input.nextInt();
                nastaviPetlju = false;

            } catch (InputMismatchException ex1) {
                System.out.println("Morate unijeti brojčane vrijednosti.");
                input.nextLine();
                nastaviPetlju = true;
            }

        } while (nastaviPetlju);

        BigDecimal discount = new BigDecimal("0");
        ;
        do {
            try {

                System.out.print("Unesite discount riže: ");
                discount = input.nextBigDecimal();
                nastaviPetlju = false;

            } catch (InputMismatchException ex1) {
                System.out.println("Morate unijeti brojčane vrijednosti.");
                input.nextLine();
                nastaviPetlju = true;
            }

        } while (nastaviPetlju);


        BigDecimal width = new BigDecimal("0");
        ;
        do {
            try {

                System.out.print("Unesite width riže: ");
                width = input.nextBigDecimal();
                nastaviPetlju = false;

            } catch (InputMismatchException ex1) {
                System.out.println("Morate unijeti brojčane vrijednosti.");
                input.nextLine();
                nastaviPetlju = true;
            }

        } while (nastaviPetlju);

        BigDecimal height = new BigDecimal("0");
        ;
        do {
            try {

                System.out.print("Unesite height riže: ");
                height = input.nextBigDecimal();
                nastaviPetlju = false;

            } catch (InputMismatchException ex1) {
                System.out.println("Morate unijeti brojčane vrijednosti.");
                input.nextLine();
                nastaviPetlju = true;
            }

        } while (nastaviPetlju);

        BigDecimal length = new BigDecimal("0");
        ;
        do {
            try {

                System.out.print("Unesite length riže: ");
                length = input.nextBigDecimal();
                nastaviPetlju = false;

            } catch (InputMismatchException ex1) {
                System.out.println("Morate unijeti brojčane vrijednosti.");
                input.nextLine();
                nastaviPetlju = true;
            }

        } while (nastaviPetlju);

        BigDecimal productionCost = new BigDecimal("0");
        ;
        do {
            try {

                System.out.print("Unesite productionCost riže: ");
                productionCost = input.nextBigDecimal();
                nastaviPetlju = false;

            } catch (InputMismatchException ex1) {
                System.out.println("Morate unijeti brojčane vrijednosti.");
                input.nextLine();
                nastaviPetlju = true;
            }

        } while (nastaviPetlju);


        BigDecimal sellingPrice = new BigDecimal("0");
        ;
        do {
            try {
                System.out.print("Unesite sellingPrice riže: ");
                sellingPrice = input.nextBigDecimal();
                nastaviPetlju = false;

            } catch (InputMismatchException ex1) {
                System.out.println("Morate unijeti brojčane vrijednosti.");
                input.nextLine();
                nastaviPetlju = true;
            }

        } while (nastaviPetlju);

        input.nextLine();
        return new Rice(weight, kcal, name, categories.get(chosenCategory - 1), width, height, length, productionCost, sellingPrice, discount, categories.get(chosenCategory - 1).getId());
    }

    /**
     * Used to input all parameters from console to generate  instance of class Ananas
     *
     * @param input      Scanner used to input fields from console
     * @param categories List of all avaliable categories for user to choose from
     * @return Returns new item of class Ananas
     */
    private static Ananas insertNewAnanas(Scanner input, List<Category> categories) {
        System.out.println("Unesite  kategoriju: ");
        Integer chosenCategory = -1;

        boolean nastaviPetlju = false;
        do {
            try {

                for (int i = 0; i < NUMBER_OF_CATEGORIES; i++) {
                    System.out.println("Za odabir " + categories.get(i).getName() + " kategorije pritisnite " + (i + 1));
                }
                chosenCategory = input.nextInt();
                if (chosenCategory < 1 || chosenCategory > NUMBER_OF_CATEGORIES) {
                    System.out.println("Pogrešan unos, pokušajte ponovo!");
                }
                nastaviPetlju = false;

            } catch (InputMismatchException ex1) {
                System.out.println("Morate unijeti brojčane vrijednosti.");
                input.nextLine();
                nastaviPetlju = true;
            }

        } while (nastaviPetlju || (chosenCategory < 1 || chosenCategory > 3));
        input.nextLine();


        System.out.print("Unesite ime artikla: ");
        String name = input.nextLine();


        BigDecimal weight = new BigDecimal("0");
        ;
        do {
            try {

                System.out.print("Unesite weight ananasa: ");
                weight = input.nextBigDecimal();
                nastaviPetlju = false;

            } catch (InputMismatchException ex1) {
                System.out.println("Morate unijeti brojčane vrijednosti.");
                input.nextLine();
                nastaviPetlju = true;
            }

        } while (nastaviPetlju);


        Integer kcal = 0;
        do {
            try {

                System.out.print("Unesite broj kilokalorija ananasa: ");
                kcal = input.nextInt();
                nastaviPetlju = false;

            } catch (InputMismatchException ex1) {
                System.out.println("Morate unijeti brojčane vrijednosti.");
                input.nextLine();
                nastaviPetlju = true;
            }

        } while (nastaviPetlju);


        BigDecimal discount = new BigDecimal("0");
        ;
        do {
            try {

                System.out.print("Unesite discount ananasa: ");
                discount = input.nextBigDecimal();
                nastaviPetlju = false;

            } catch (InputMismatchException ex1) {
                System.out.println("Morate unijeti brojčane vrijednosti.");
                input.nextLine();
                nastaviPetlju = true;
            }

        } while (nastaviPetlju);


        BigDecimal width = new BigDecimal("0");
        ;
        do {
            try {

                System.out.print("Unesite width ananasa: ");
                width = input.nextBigDecimal();
                nastaviPetlju = false;

            } catch (InputMismatchException ex1) {
                System.out.println("Morate unijeti brojčane vrijednosti.");
                input.nextLine();
                nastaviPetlju = true;
            }

        } while (nastaviPetlju);

        BigDecimal height = new BigDecimal("0");
        ;
        do {
            try {

                System.out.print("Unesite height ananasa: ");
                height = input.nextBigDecimal();
                nastaviPetlju = false;

            } catch (InputMismatchException ex1) {
                System.out.println("Morate unijeti brojčane vrijednosti.");
                input.nextLine();
                nastaviPetlju = true;
            }

        } while (nastaviPetlju);

        BigDecimal length = new BigDecimal("0");
        ;
        do {
            try {

                System.out.print("Unesite length ananasa: ");
                length = input.nextBigDecimal();
                nastaviPetlju = false;

            } catch (InputMismatchException ex1) {
                System.out.println("Morate unijeti brojčane vrijednosti.");
                input.nextLine();
                nastaviPetlju = true;
            }

        } while (nastaviPetlju);

        BigDecimal productionCost = new BigDecimal("0");
        ;
        do {
            try {

                System.out.print("Unesite productionCost ananasa: ");
                productionCost = input.nextBigDecimal();
                nastaviPetlju = false;

            } catch (InputMismatchException ex1) {
                System.out.println("Morate unijeti brojčane vrijednosti.");
                input.nextLine();
                nastaviPetlju = true;
            }

        } while (nastaviPetlju);


        BigDecimal sellingPrice = new BigDecimal("0");
        ;
        do {
            try {
                System.out.print("Unesite sellingPrice ananasa: ");
                sellingPrice = input.nextBigDecimal();
                nastaviPetlju = false;

            } catch (InputMismatchException ex1) {
                System.out.println("Morate unijeti brojčane vrijednosti.");
                input.nextLine();
                nastaviPetlju = true;
            }

        } while (nastaviPetlju);

        input.nextLine();
        return new Ananas(weight, kcal, name, categories.get(chosenCategory - 1), width, height, length, productionCost, sellingPrice, discount, categories.get(chosenCategory - 1).getId());
    }


    static String[] names = new String[NUMBER_OF_CATEGORIES];

    static Integer br = 0;

    /**
     * Used to check if same name is used twice when naming avaliable categories
     *
     * @param name String used for comparison
     * @throws SameNameCategory Throws exception in case the same name is used twice or more
     */
    public static void provjera_imena(String name) throws SameNameCategory {
        if (Arrays.asList(names).contains(name)) {
            throw new SameNameCategory("Isto ime uneseno , pokušajte ponovo!");
        }

        names[br] = name;
        br += 1;


    }


    /**
     * Used to generate new Category object based on user input
     * @param input Scanner used to input fields from console
     * @return Returns object of class Category with fields based on user input
     */
//    public static Category insertNewCategory(Scanner input){
//
//        boolean pogodio = false;
//        Optional<String> name = Optional.empty();
//        Optional<String> description =  Optional.empty();
//        do {
//
//            System.out.print("Unesite ime kategorije: ");
//             name = Optional.of(input.nextLine());
//
//            System.out.print("Unesite opis kategorije: ");
//             description = Optional.of( input.nextLine());
//            try{
//                provjera_imena(name.get());
//                pogodio=false;
//            }catch (SameNameCategory ex){
//                logger.error("Same category name was initialized twice!",ex);
//                System.out.println(ex.getMessage());
//                pogodio = true;
//
//            }
//
//        }while(pogodio);
//        return new Category(name.get(), description.get());
//
//    }

    /**
     * Used to generate new object of base class Item with field based on user input
     *
     * @param input      Scanner used to input fields from console
     * @param categories List of all avaliable catefories for user to choose from
     * @return Returns object of class Item with fields based on user input
     */
    public static Item insertNewItem(Scanner input, List<Category> categories) {
        input.nextLine();

        System.out.print("Unesite ime artikla: ");
        String name = input.nextLine();
        System.out.println("Unesite  kategoriju: ");
        Integer chosenCategory = -1;

        boolean nastaviPetlju = false;
        do {
            try {

                for (int i = 0; i < NUMBER_OF_CATEGORIES; i++) {
                    System.out.println("Za odabir " + categories.get(i).getName() + " kategorije pritisnite " + (i + 1));
                }
                chosenCategory = input.nextInt();
                if (chosenCategory < 1 || chosenCategory > NUMBER_OF_CATEGORIES) {
                    System.out.println("Pogrešan unos, pokušajte ponovo!");
                }
                nastaviPetlju = false;

            } catch (InputMismatchException ex1) {
                System.out.println("Morate unijeti brojčane vrijednosti.");
                input.nextLine();
                nastaviPetlju = true;
            }

        } while (nastaviPetlju || (chosenCategory < 1 || chosenCategory > 3));
        input.nextLine();


        BigDecimal discount = new BigDecimal("0");
        do {
            try {

                System.out.print("Unesite popust u %: ");
                discount = input.nextBigDecimal();
                nastaviPetlju = false;

            } catch (InputMismatchException ex1) {
                System.out.println("Morate unijeti brojčane vrijednosti.");
                input.nextLine();
                nastaviPetlju = true;
            }

        } while (nastaviPetlju);

        BigDecimal width = new BigDecimal("0");
        do {
            try {

                System.out.print("Unesite width artikla: ");
                width = input.nextBigDecimal();
                nastaviPetlju = false;

            } catch (InputMismatchException ex1) {
                System.out.println("Morate unijeti brojčane vrijednosti.");
                input.nextLine();
                nastaviPetlju = true;
            }

        } while (nastaviPetlju);

        BigDecimal height = new BigDecimal("0");
        do {
            try {

                System.out.print("Unesite height artikla: ");
                height = input.nextBigDecimal();
                nastaviPetlju = false;

            } catch (InputMismatchException ex1) {
                System.out.println("Morate unijeti brojčane vrijednosti.");
                input.nextLine();
                nastaviPetlju = true;
            }

        } while (nastaviPetlju);

        BigDecimal length = new BigDecimal("0");
        do {
            try {

                System.out.print("Unesite length artikla: ");
                length = input.nextBigDecimal();
                nastaviPetlju = false;

            } catch (InputMismatchException ex1) {
                System.out.println("Morate unijeti brojčane vrijednosti.");
                input.nextLine();
                nastaviPetlju = true;
            }

        } while (nastaviPetlju);

        BigDecimal productionCost = new BigDecimal("0");
        do {
            try {

                System.out.print("Unesite productionCost artikla: ");
                productionCost = input.nextBigDecimal();
                nastaviPetlju = false;

            } catch (InputMismatchException ex1) {
                System.out.println("Morate unijeti brojčane vrijednosti.");
                input.nextLine();
                nastaviPetlju = true;
            }

        } while (nastaviPetlju);


        BigDecimal sellingPrice = new BigDecimal("0");
        do {
            try {
                System.out.print("Unesite sellingPrice artikla: ");
                sellingPrice = input.nextBigDecimal();
                nastaviPetlju = false;

            } catch (InputMismatchException ex1) {
                System.out.println("Morate unijeti brojčane vrijednosti.");
                input.nextLine();
                nastaviPetlju = true;
            }

        } while (nastaviPetlju);
        input.nextLine();
        return new Item(name, categories.get(chosenCategory - 1), width, height, length, productionCost, sellingPrice, discount, categories.get(chosenCategory - 1).getId());

    }


    /**
     * Used to generate new object of class Store with field based on user input
     *
     * @param input Scanner used to input fields from console
     * @param items List of all avaliable items for user to choose from
     * @return Returns object of class Store with fields based on user input
     */

    public static Store insertNewStore(Scanner input, List<Item> items) {
        System.out.print("Unesite ime trgovine: ");
        String name = input.nextLine();
        System.out.print("Unesite web adresu: ");
        String webAdress = input.nextLine();

        String chosenItemIndex = "s";


        Integer counter = 0;

        Item[] chosenItems = new Item[NUMBER_OF_ITEMS];

        do {
            for (int i = 0; i < NUMBER_OF_ITEMS; i++) {
                System.out.println("Za odabir " + items.get(i).getName() + " kategorije pritisnite y ili n ako ne zelite odabrati");

                chosenItemIndex = input.nextLine();

                if (chosenItemIndex.equals("y")) {


                    chosenItems[counter] = items.get(i);
                    counter = counter + 1;


                } else if (!chosenItemIndex.equals("y") && !chosenItemIndex.equals("n")) {
                    System.out.println("Pogrešan unos, pokušajte ponovo!");
                    i--;

                }
            }
        } while (!chosenItemIndex.equals("y") && !chosenItemIndex.equals("n"));

        Item[] chosenItemsCopy = new Item[counter];

        for (int i = 0; i < counter; i++) {
            chosenItemsCopy[i] = chosenItems[i];
        }


        return new Store(name, webAdress, chosenItemsCopy, chosenItems[0].getId());
    }

    /**
     * Used to check if same item was being added to multiple classes
     *
     * @param item   used to determine if there was attempt of adding same item multiple times
     * @param stores List of all object of class Store which can possibly contain same item twice
     * @throws SameArticleException Exception thrown if same item was being added to multiple classes
     */
    public static void provjera_artikla(Item item, List<Store> stores) throws SameArticleException {

        for (int i = 0; i < stores.size(); i++) {
            if (Arrays.asList(stores.get(i).getItems()).contains(item)) {
                System.out.println("drugi");
                throw new SameArticleException("Vec je odabran ovaj artikl!");
            }
        }
    }


    private static Optional<List<Address>> fetchAddressFromFile() {
        File addressesFile = new File(FILE_NAME_ADDRESS);
        List<Address> addressList = new ArrayList<>();
        try (BufferedReader addressesFileReader = new BufferedReader(new FileReader(addressesFile))) {

            String line;
            Integer lineCounter = 1;

            Address.Builder addressBuilder = new Address.Builder();

            while ((line = addressesFileReader.readLine()) != null) {
                if (lineCounter % NUMBER_OF_LINES_FOR_ADDRESS == 1) {
                    addressBuilder.asStreet(line);
                } else if (lineCounter % NUMBER_OF_LINES_FOR_ADDRESS == 2) {
                    Optional<Gradovi> grad = Optional.empty();
                    switch (line) {
                        case "ZAGREB":
                            grad = Optional.of(Gradovi.ZAGREB);
                            break;
                        case "RIJEKA":
                            grad = Optional.of(Gradovi.RIJEKA);
                            break;
                        case "SPLIT":
                            grad = Optional.of(Gradovi.SPLIT);
                            break;
                    }

                    addressBuilder.asGrad(grad.get());
                } else if (lineCounter % NUMBER_OF_LINES_FOR_ADDRESS == 3) {
                    addressBuilder.houseNumber(line);
                } else if (lineCounter % NUMBER_OF_LINES_FOR_ADDRESS == 4) {
                    addressBuilder.atCity(line);
                } else if (lineCounter % NUMBER_OF_LINES_FOR_ADDRESS == 0) {
                    addressBuilder.atPostal(Integer.parseInt(line));
                    Address newAddress = addressBuilder.build();
                    addressList.add(newAddress);
                    addressBuilder = new Address.Builder();
                }
                lineCounter++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        if (addressList.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(addressList);
        }
    }


    private static Optional<List<Factory>> fetchFactoriesFromFile(List<Address> adrese, List<Item> items) {
        File factoriesFile = new File(FILE_NAME_FACTORIES);
        List<Factory> factoryList = new ArrayList<>();
        Optional<Long> id = Optional.empty();
        Optional<String> name = Optional.empty();
        Optional<Integer> addressId = Optional.empty();

        try (BufferedReader factoriesFileReader = new BufferedReader(new FileReader(factoriesFile))) {
            String line;
            Integer lineCounter = 1;


            while ((line = factoriesFileReader.readLine()) != null) {
                if (lineCounter % NUMBER_OF_LINES_FOR_FACTORY == 1) {
                    id = Optional.of(Long.parseLong(line));
                } else if (lineCounter % NUMBER_OF_LINES_FOR_FACTORY == 2) {
                    name = Optional.of(line);
                } else if (lineCounter % NUMBER_OF_LINES_FOR_FACTORY == 3) {
                    addressId = Optional.of(Integer.parseInt(line));
                } else if (lineCounter % NUMBER_OF_LINES_FOR_FACTORY == 0) {
                    List<Long> ids = List.of(line.split(",")).stream().map(s -> Long.parseLong(s)).toList();
                    List<Item> itemsForFactory = new ArrayList<>();

                    for (Item item : items) {
                        for (Long ln : ids) {
                            if (Objects.equals(ln, item.getId())) {
                                itemsForFactory.add(item);
                            }
                        }
                    }


                    factoryList.add(new Factory(name.get(), adrese.get(addressId.get()-1), itemsForFactory, id.get()));
                }
                lineCounter++;
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        if (factoryList.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(factoryList);
        }

    }





    private static Optional<List<Store>> fetchStoresFromFile( List<Item> items) {
        File storesFile = new File(FILE_NAME_STORES);
        List<Store> storeList = new ArrayList<>();
        Optional<Long> id = Optional.empty();
        Optional<String> name = Optional.empty();
        Optional<String> webAddress = Optional.empty();

        try (BufferedReader storesFileReader = new BufferedReader(new FileReader(storesFile))) {
            String line;
            Integer lineCounter = 1;


            while ((line = storesFileReader.readLine()) != null) {
                if (lineCounter % NUMBER_OF_LINES_FOR_FACTORY == 1) {
                    id = Optional.of(Long.parseLong(line));
                } else if (lineCounter % NUMBER_OF_LINES_FOR_FACTORY == 2) {
                    name = Optional.of(line);
                } else if (lineCounter % NUMBER_OF_LINES_FOR_FACTORY == 3) {
                    webAddress = Optional.of(line);
                } else if (lineCounter % NUMBER_OF_LINES_FOR_FACTORY == 0) {
                    List<Long> ids = List.of(line.split(",")).stream().map(s -> Long.parseLong(s)).toList();
                    List<Item> itemsForStore = new ArrayList<>();

                    for (Item item : items) {
                        for (Long ln : ids) {
                            if (Objects.equals(ln, item.getId())) {
                                itemsForStore.add(item);
                            }
                        }
                    }


                    storeList.add(new Store(name.get(),webAddress.get() , itemsForStore, id.get()));
                }
                lineCounter++;
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        if (storeList.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(storeList);
        }

    }























    /**
     *  Used to generate new object of class Factory with field based on user input
     * @param input Scanner used to input fields from console
     * @param items List of all avaliable items for user to choose from
     * @param stores List of all object of class Store used for later determination of duplicates
     * @return Returns object of class Factory with fields based on user input
     */


//    public static Factory insertNewFactory(Scanner input, List<Item> items, List<Store> stores) {
//        System.out.print("Unesite ime tvornice: ");
//        String name = input.nextLine();
//        System.out.print("Unesite ulicu : ");
//        String street = input.nextLine();
//
//
//        boolean nastaviPetlju = false;
//        Integer gr = 0;
//        do {
//            try {
//                System.out.println("Unesite (1) za Zagreb, (2) za Rijeku, (3) za Split");
//                gr = input.nextInt();
//                nastaviPetlju = false;
//
//            }
//            catch(InputMismatchException ex1) {
//                logger.error("Wrong input!",ex1);
//                System.out.println("Morate unijeti brojčane vrijednosti.");
//                input.nextLine();
//                nastaviPetlju = true;
//            }
//
//        } while(nastaviPetlju);
//
//        Optional<Gradovi> grad = Optional.empty();
//        switch (gr){
//            case 1:
//                 grad = Optional.of(Gradovi.ZAGREB);
//                break;
//            case 2:
//                 grad = Optional.of(Gradovi.RIJEKA);
//                 break;
//            case 3:
//                grad =  Optional.of(Gradovi.SPLIT);
//                break;
//        }
//        input.nextLine();
//
//
//        System.out.print("Unesite broj : ");
//        String number = input.nextLine();
//
//
//        String chosenItemIndex="s";
//
//        Address adresaFinal = new Address.Builder(street).houseNumber(number).asGrad(grad.get()).build();
//
//        Integer counter =0;
//
//        Item[] chosenItems=new Item[NUMBER_OF_ITEMS];
//
//        boolean pogodio = false;
//        int i=0;
//
//        do {
//            System.out.println("Za odabir " + items.get(i).getName() + " kategorije pritisnite y ili n ako ne zelite odabrati");
//
//            chosenItemIndex = input.nextLine();
//
//            if (chosenItemIndex.equals("y")) {
//                try {
//                    provjera_artikla(items.get(i), stores);
//                    chosenItems[counter] = items.get(i);
//                    counter = counter + 1;
//
//
//                    pogodio = false;
//                } catch (SameArticleException ex) {
//                    logger.error("Same article was initilized twice!",ex);
//                    System.out.println(ex.getMessage());
//                    pogodio = true;
//
//
//                }
//                i+=1;
//                if(i==NUMBER_OF_ITEMS){
//                    pogodio=false;
//                }
//            }else{
//                i+=1;
//                if(i==NUMBER_OF_ITEMS){
//                    pogodio=false;
//                }
//            }
//
//
//        }while(pogodio);
//
//
//
//        Item[] chosenItemsCopy=new Item[counter];
//
//        for(int j=0;j<counter;j++){
//            chosenItemsCopy[j] = chosenItems[j];
//        }
//
//        return new Factory(name,adresaFinal,chosenItemsCopy,chosenItemsCopy[0].getId());
//
//    }
//
}
