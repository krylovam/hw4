package fintech4;

import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.*;
import java.lang.String;

import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.sun.jna.platform.win32.Netapi32Util;
import com.sun.org.apache.xml.internal.utils.StringToIntTable;
import org.apache.commons.logging.Log;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.fluent.Response;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.http.client.*;
import org.apache.http.client.methods.*;
import org.apache.http.HttpEntity;
import org.codehaus.jackson.type.TypeReference;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.apache.http.Header;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.util.Date;


import static org.apache.http.HttpHeaders.USER_AGENT;

public class hw4 {

    public static void main(String[] args) throws Exception {
        String URL = "jdbc:mysql://localhost:3306/fintech" + "?verifyServerCertificate=false" + "&useSSL=false&allowPublicKeyRetrieval=true" + "&requireSSL=false" + "&useLegacyDatetimeCode=false" + "&amp" + "&serverTimezone=UTC";
        String USER = "root";
        String PASS = "fintech4";
        Random rand = new Random();
        Helper helper = new Helper();
        int count = 1 + rand.nextInt(30);
        ArrayList<fintech4.User> UserMas = new ArrayList<fintech4.User>();
        HttpResponse response = helper.getResponse();
        String[] Header = new String[13];
        if (response == null) {
            boolean res = true;
            int countInDB = 0;
            Connection con0 = null;
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                con0 = DriverManager.getConnection(URL, USER, PASS);
                Statement stmt = null;
                String queryEmptySet = "Select * from persons";
                stmt = con0.createStatement();
                ResultSet rs = stmt.executeQuery(queryEmptySet);
                while (rs.next()) {
                    countInDB = rs.getInt("id");
                }
                if (countInDB < count) res = false;
            } catch (SQLException e) {
                res = false;
            }finally{
                con0.close();
            }
            if (!res) {
                final int constCount = 30;
                int countMen = rand.nextInt(count + 1);
                int countWomen = count - countMen;

                String dir = String.format("%s%s%s%s%s%s%s%s", System.getProperty("user.dir"), File.separator, "src", File.separator, "main", File.separator, "resources", File.separator);
                ArrayList<Integer> NumbersFIOMen = new ArrayList(helper.getNumbers(countMen, constCount));
                ArrayList<Integer> NumbersFIOWomen = new ArrayList(helper.getNumbers(countWomen, constCount));
                ArrayList<Integer> NumbersAddress = new ArrayList(helper.getNumbers(count, constCount));
                ArrayList<String> SurnameMen = new ArrayList<String>(helper.getLineByLine(NumbersFIOMen, String.format("%s%s", dir, "SurnameMen.txt")));
                ArrayList<String> NameMen = new ArrayList<String>(helper.getLineByLine(NumbersFIOMen, String.format("%s%s", dir, "NameMen.txt")));
                ArrayList<String> PatronymicMen = new ArrayList<String>(helper.getLineByLine(NumbersFIOMen, String.format("%s%s", dir, "PatronymicMen.txt")));
                ArrayList<String> SurnameWomen = new ArrayList<String>(helper.getLineByLine(NumbersFIOWomen, String.format("%s%s", dir, "SurnameWomen.txt")));
                ArrayList<String> NameWomen = new ArrayList<String>(helper.getLineByLine(NumbersFIOWomen, String.format("%s%s", dir, "NameWomen.txt")));
                ArrayList<String> PatronymicWomen = new ArrayList<String>(helper.getLineByLine(NumbersFIOWomen, String.format("%s%s", dir, "PatronymicWomen.txt")));
                for (int i = 0; i < count; i++) {
                    UserMas.add(new fintech4.User());
                    UserMas.get(i).setCountry(fintech4.User.getAddress(String.format("%s%S", dir, "Country.txt"), constCount));
                    UserMas.get(i).setState("-");
                    UserMas.get(i).setCity(fintech4.User.getAddress(String.format("%s%s", dir, "City.txt"), constCount));
                    UserMas.get(i).setStreet(fintech4.User.getAddress(String.format("%s%s", dir, "Street.txt"), constCount));
                    UserMas.get(i).setPostcode();
                    UserMas.get(i).setHouse();
                    UserMas.get(i).setFlat();
                    UserMas.get(i).setINN();
                    UserMas.get(i).setDayOfBirth();
                    UserMas.get(i).setAge(UserMas.get(i).getDob());
                }
                for (int i = 0; i < countMen; i++) {
                    UserMas.get(i).setLastName(SurnameMen.get(i));
                    UserMas.get(i).setFirstName(NameMen.get(i));
                    try {
                        UserMas.get(i).setTitle(PatronymicMen.get(i));
                    } catch (IndexOutOfBoundsException e) {
                    }
                    UserMas.get(i).setGender("M");
                }
                for (int i = 0; i < countWomen; i++) {
                    UserMas.get(i + countMen).setLastName(SurnameWomen.get(i));
                    UserMas.get(i + countMen).setFirstName(NameWomen.get(i));
                    try {
                        UserMas.get(i + countMen).setTitle(PatronymicWomen.get(i));
                    } catch (IndexOutOfBoundsException e) {
                    }
                    UserMas.get(i + countMen).setGender("Ж");
                }
                Collections.shuffle(UserMas);
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection con = DriverManager.getConnection(URL, USER, PASS);
                for (int i = 0; i < count; i ++){
                    helper.writeToDB(con, UserMas.get(i));
                }
                Header = new String[]{"Фамилия", "Имя", "Отчество", "Возраст", "Пол", "Дата рождения", "ИНН", "Индекс", "Страна","Область", "Город", "Улица", "Дом", "Квартира"};
            }else{
                ArrayList<Integer> Numbers = new ArrayList<Integer>(helper.getNumbers(count,countInDB));
                for (int i = 0; i < count; i ++){
                    User user = new User();
                    Connection con = DriverManager.getConnection(URL, USER, PASS);
                    user.getUserFromDB(con, Numbers.get(i));
                    UserMas.add(user);
                }
                Header = new String[]{"Фамилия", "Имя", "Title/Отчество", "Возраст", "Пол", "Дата рождения", "ИНН", "Индекс", "Страна","Область", "Город", "Улица", "Дом", "Квартира"};
            }
        } else {
            Class.forName("com.mysql.cj.jdbc.Driver");

            for (int i = 0; i < count; i++) {
                Connection con = DriverManager.getConnection(URL, USER, PASS);
                String json = helper.streamToString(helper.getResponse());
                UserApi userApi = helper.mapUser(json);
                User user = userApi.getUserApi();
                UserMas.add(user);
                helper.writeToDB(con, user);
            }
            Header = new String[]{"Фамилия", "Имя", "Title", "Возраст", "Пол", "Дата рождения", "ИНН", "Индекс", "Страна","Область", "Город", "Улица", "Дом", "Квартира"};
        }

        helper.writeToExcel(UserMas, count, Header);
        helper.writeToPdf(UserMas, count, Header);



    }

}

