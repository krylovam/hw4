package fintech4;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.lang.String;
import org.apache.http.HttpResponse;

public class Main {

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader("database.txt"));
        String url = new String();
        String user = new String();
        String pass = new String();
        try{
            url= br.readLine();
            user = br.readLine();
            pass = br.readLine();
        }finally {
            br.close();
        }
        Random rand = new Random();
        Helper helper = new Helper();
        HelperDB helperDB = new HelperDB();
        int count = 1 + rand.nextInt(30);
        ArrayList<fintech4.User> UserMas = new ArrayList<fintech4.User>();
        HttpResponse response = helper.getResponse();
        String[] header;
        if (response == null) {
            boolean res = true;
            int countInDB = 0;
            Connection con0 = null;
            Statement stmt = null;
            ResultSet rs = null;
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                con0 = DriverManager.getConnection(url, user, pass);
                String queryEmptySet = "Select * from persons";
                stmt = con0.createStatement();
                rs = stmt.executeQuery(queryEmptySet);
                while (rs.next()) {
                    countInDB = rs.getInt("id");
                }
                if (countInDB < count) res = false;
            } catch (SQLException e) {
                res = false;
                System.out.println("wrong connection or query");
            }finally{
                con0.close();
                stmt.close();
                rs.close();
            }
            if (!res) {
                final int constCount = 30;
                int countMen = rand.nextInt(count + 1);
                int countWomen = count - countMen;
                UserMas = helper.getUserFromFiles(constCount,count,countMen,countWomen);
                Collections.shuffle(UserMas);
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection con = DriverManager.getConnection(url, user, pass);
                for (int i = 0; i < count; i ++){
                    helperDB.writeToDB(con, UserMas.get(i));
                }
                header = new String[]{"Фамилия", "Имя", "Отчество", "Возраст", "Пол", "Дата рождения", "ИНН", "Индекс", "Страна","Область", "Город", "Улица", "Дом", "Квартира"};
            }else{
                ArrayList<Integer> Numbers = new ArrayList<Integer>(helper.getNumbers(count,countInDB));
                for (int i = 0; i < count; i ++){
                    User User = new User();
                    Connection con = DriverManager.getConnection(url, user, pass);
                    User = User.getUserFromDB(con, Numbers.get(i));
                    UserMas.add(User);
                }
                header = new String[]{"Фамилия", "Имя", "Title/Отчество", "Возраст", "Пол", "Дата рождения", "ИНН", "Индекс", "Страна","Область", "Город", "Улица", "Дом", "Квартира"};
            }
        } else {
            Class.forName("com.mysql.cj.jdbc.Driver");

            for (int i = 0; i < count; i++) {
                Connection con = DriverManager.getConnection(url, user, pass);
                UserApi userApi = new UserApi();
                String json = userApi.streamToString(helper.getResponse());
                userApi = userApi.mapUser(json);
                User User = userApi.getUserApi();
                UserMas.add(User);
                helperDB.writeToDB(con, User);
                try{}
                finally {
                    con.close();
                }
            }
            header = new String[]{"Фамилия", "Имя", "Title", "Возраст", "Пол", "Дата рождения", "ИНН", "Индекс", "Страна","Область", "Город", "Улица", "Дом", "Квартира"};
        }

        helper.writeToExcel(UserMas, count, header);
        helper.writeToPdf(UserMas, count, header);



    }

}


