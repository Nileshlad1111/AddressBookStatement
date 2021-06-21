package bridgelabz.utils;


import bridgelabz.models.Person;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import jdk.nashorn.internal.parser.JSONParser;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.LinkedList;

public class FileOperations {

    private Object JsonObject;

    public void convertToFile(LinkedList<Person> addressBook, String filePath, int fileOperations) throws JSONException {
        switch (fileOperations) {
            case 1:
                JSONArray personList = new JSONArray();
                for (Person person : addressBook) {
                    JSONObject personDetails = new JSONObject();
                    personDetails.put("First Name", person.getFirstName());
                    personDetails.put("Last Name", person.getLastName());
                    personDetails.put("Phone", person.getPhone());
                    personDetails.put("Address", person.getAddress());
                    personDetails.put("City", person.getCity());
                    personDetails.put("State", person.getState());
                    personDetails.put("Zip", person.getZip());
                    JSONObject personObject = new JSONObject();
                    personObject.put("person", personDetails);
                    personList.put(personObject);
                }
                try {
                    FileWriter fileWriter = new FileWriter(filePath);
                    fileWriter.append(personList.join(filePath));
                    fileWriter.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                try (Writer writer = Files.newBufferedWriter(Paths.get(filePath))) {
                    StatefulBeanToCsv<Person> beanToCsv = new StatefulBeanToCsvBuilder(writer)
                            .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                            .build();
                    beanToCsv.write(addressBook);
                } catch (IOException | CsvRequiredFieldEmptyException | CsvDataTypeMismatchException e) {
                    e.printStackTrace();
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + fileOperations);
        }
    }

    public LinkedList<Person> getDataInList(String filePath, int fileOperations) throws IOException, JSONException {

        LinkedList<Person> addressBook = new LinkedList<>();

        switch (fileOperations) {
            case 1:
               /* JSONParser jsonParser = new JSONParser(parseJSONObject((JSONObject) JsonObject));
                try {
                    FileReader fileReader = new FileReader(filePath);
                    Object obj = jsonParser.parse(fileReader);
                    JSONArray personList = (JSONArray) obj;
                    personList.get(person -> addressBook.add(parseJSONObject((JSONObject) person)));
                } catch (IOException | ParseException e) {
                    e.printStackTrace();
                }*/
                break;
            case 2:
                try (
                        Reader reader = Files.newBufferedReader(Paths.get(filePath));
                        CSVReader csvReader = new CSVReader(reader)
                ) {
                    csvReader.readNext();
                    String[] nextPerson;
                    while ((nextPerson = csvReader.readNext()) != null) {
                        addressBook.add(new Person(nextPerson[2],
                                nextPerson[3],
                                nextPerson[0],
                                nextPerson[1],
                                nextPerson[5],
                                nextPerson[6],
                                nextPerson[4]));
                    }
                }
                break;
        }

        return addressBook;
    }

    private Person parseJSONObject(JSONObject personJson) throws JSONException {
        JSONObject personObj = (JSONObject) personJson.get("person");
        return new Person((String) personObj.get("First Name"),
                (String) personObj.get("Last Name"),
                (String) personObj.get("Address"),
                (String) personObj.get("City"),
                (String) personObj.get("State"),
                (String) personObj.get("Zip"),
                (String) personObj.get("Phone"));
    }
}