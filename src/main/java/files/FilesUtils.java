package files;

import hr.java.production.enumeration.Gradovi;
import hr.java.production.model.*;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

public class FilesUtils {

    private static final String FILE_NAME_ITEMS = "dat/items.txt";
    private static final int NUMBER_OF_LINES_FOR_CATEGORY = 3;
    private static final String FILE_NAME_CATEGORIES = "dat/categories.txt";
    private static final Integer NUMBER_OF_LINES_FOR_ADDRESS = 5;
    private static final int NUMBER_OF_LINES_FOR_FACTORY = 4;
    private static final String FILE_NAME_FACTORIES = "dat/factories.txt";
    private static final String FILE_NAME_ADDRESS = "dat/addresses.txt";
    private static final String FILE_NAME_STORES = "dat/stores.txt";
    private static final String FILE_NAME_LIJEKOVI = "dat/recepti.txt";


    public static Optional<List<Category>> fetchCategoriesFromFile() {
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


    public static Optional<List<Item>> fetchItemsFromFile(List<Category> categories) {
        File itemsFile = new File(FILE_NAME_ITEMS);
        List<Item> itemList = new ArrayList<>();
        String type;
        String readLine;

        try (BufferedReader itemsFileReader = new BufferedReader(new FileReader(itemsFile))) {

            while ((readLine = itemsFileReader.readLine()) != null) {


                String name = readLine;
                String cat = itemsFileReader.readLine().toLowerCase();

                BigDecimal width = new BigDecimal(itemsFileReader.readLine());
                BigDecimal height = new BigDecimal(itemsFileReader.readLine());
                BigDecimal length = new BigDecimal(itemsFileReader.readLine());
                BigDecimal productionCost = new BigDecimal(itemsFileReader.readLine());
                BigDecimal sellingPrice = new BigDecimal(itemsFileReader.readLine());
                String id = itemsFileReader.readLine();

                Category categ = categories.stream().filter(ctg -> ctg.getName().toLowerCase(Locale.ROOT).equals(cat)).findAny().get();



                itemList.add(new Item(name, categ, width, height, length, productionCost, sellingPrice,Long.parseLong(id)));


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



    public static void saveItem(List<Item> itemList) throws IOException{
        FileWriter writer = new FileWriter(FILE_NAME_ITEMS);
        PrintWriter printer = new PrintWriter(writer);

        for(Item item : itemList){

            printer.println(item.getName());
            printer.println(item.getCategory());
            printer.println(item.getWidth());
            printer.println(item.getHeight());
            printer.println(item.getLength());
            printer.println(item.getProductionCost());
            printer.println(item.getSellingPrice());
            printer.println(item.getId());

        }
        writer.flush();

    }



    public static void saveCategory(List<Category> categoryList) throws IOException{
        FileWriter writer = new FileWriter(FILE_NAME_CATEGORIES);
        PrintWriter printer = new PrintWriter(writer);

        for(Category category : categoryList){
            printer.println(category.getId());
            printer.println(category.getName());
            printer.println(category.getDescription());

        }
        writer.flush();

    }


    public static void saveFactory(List<Factory> factoryList) throws IOException{
        FileWriter writer = new FileWriter(FILE_NAME_FACTORIES);
        PrintWriter printer = new PrintWriter(writer);
        for(Factory factory : factoryList){
            printer.println(factory.getId());
            printer.println(factory.getName());
            printer.println((factory.getAddressObj().getPostalCode()==10000 ? NUMBER_OF_LINES_FOR_CATEGORY-2:NUMBER_OF_LINES_FOR_CATEGORY-1));

            printer.println(factory.getItemsIds());

        }
        writer.flush();

    }

    public static void saveStore(List<Store> storeList) throws IOException {
        FileWriter writer = new FileWriter(FILE_NAME_STORES);
        PrintWriter printer = new PrintWriter(writer);
        for(Store store : storeList){
            printer.println(store.getId());
            printer.println(store.getName());
            printer.println(store.getWebAddress());
            printer.println(store.getItemsIds());

        }
        writer.flush();

    }



        public static Optional<List<Address>> fetchAddressFromFile() {
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


    public static Optional<List<Factory>> fetchFactoriesFromFile(List<Address> adrese, List<Item> items) {
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


    public static Optional<List<Store>> fetchStoresFromFile( List<Item> items) {
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


    public static Optional<List<Recept>> fetchReceptfromFile( ) {
        File lijekoviFile = new File(FILE_NAME_LIJEKOVI);
        List<Recept> receptList = new ArrayList<>();
        Integer redniBroj=0;
        String name="";
        Lijek Lijek=new Lijek("");
        String napomena="";
        String dostava="";

        try (BufferedReader storesFileReader = new BufferedReader(new FileReader(lijekoviFile))) {
            String line;
            Integer lineCounter = 1;


            while ((line = storesFileReader.readLine()) != null) {
                if (lineCounter % NUMBER_OF_LINES_FOR_FACTORY == 1) {
                    redniBroj = Integer.parseInt(line);
                    System.out.println(redniBroj);
                    } else if (lineCounter % NUMBER_OF_LINES_FOR_FACTORY == 2) {
                    name = line;
                    Lijek = new Lijek(name);
                } else if (lineCounter % NUMBER_OF_LINES_FOR_FACTORY == 3) {
                    napomena = line;
                } else if (lineCounter % NUMBER_OF_LINES_FOR_FACTORY == 0) {
                    dostava = line;




                    receptList.add(new Recept(napomena,redniBroj,Lijek,LocalDateTime.now(),dostava));
                }
                lineCounter++;
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        if (receptList.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(receptList);
        }

    }





}
