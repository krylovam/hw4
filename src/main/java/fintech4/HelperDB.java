package fintech4;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

public class HelperDB {
    public void writeToDB(Connection con, User user)throws Exception{
        String checkUpdate = String.format("%s%s%s%s%s%s%s","select id, address_id from persons where surname = '",user.getLastName(),"' and name = '",user.getFirstName(),"' and middlename = '",user.getTitle(),"';");
        Statement stmt = null;
        ResultSet rs = null;
        try{
            stmt = con.createStatement();
            rs = stmt.executeQuery(checkUpdate);
            if (rs.next()){
                int id = rs.getInt("id");
                int address_id = rs.getInt("address_id");
                updateUserInDataBase(user, con,id, address_id);
            }else{
                getNewUserToDataBase(user, con);
            }
        }catch(SQLException e){
            e.printStackTrace();
            System.out.println("error writing to DB");
        }finally {
            stmt.close();
            rs.close();
        }
    }

    public void getNewUserToDataBase(User user, Connection con) throws Exception {
        String templateAddress = "insert into address(postcode, country, region, city,street, house, flat) VALUES";
        String unicAddress = String.format("%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s", "('", user.getPostcode(), "','", user.getCountry(), "','", user.getState(), "','", user.getCity(),
                "','", user.getStreet(), "',", String.valueOf(user.getHouse()), ",", String.valueOf(user.getFlat()), ");");
        String templatePerson = "insert into persons(surname, name, middlename, birthday, gender,inn,address_id) VALUES";
        GregorianCalendar gc = user.getDob();

        SimpleDateFormat format = new SimpleDateFormat("YYYYMMdd");
        String birthday = String.format("%s%s%s", "(", format.format(gc.getTime()), ")");
        String unicPerson = String.format("%s%s%s%s%s%s%s%s%s%s%s%s%s", "('", user.getLastName(), "','", user.getFirstName(), "','", user.getTitle(), "',", birthday,
                ",'", user.getGender(), "','", user.getInn(), "',");
        Statement stmt = null;
        try {
            String queryAddress = String.format("%s%s", templateAddress, unicAddress);
            stmt = con.createStatement();
            int response = stmt.executeUpdate(queryAddress);
            String querySelectID = String.format("%s%s%s%s%s%s%s", "select id from address where city ='", user.getCity(), "' and street ='", user.getStreet(), "' and house =", String.valueOf(user.getHouse()), ";");
            stmt = null;
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(querySelectID);
            int address_ID = 0;
            while (rs.next()) {
                address_ID = rs.getInt("id");
            }
            String queryPerson = String.format("%s%s%s%s", templatePerson, unicPerson, String.valueOf(address_ID), ");");
            stmt = null;
            stmt = con.createStatement();
            response = stmt.executeUpdate(queryPerson);

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("error adding in DB");
        }finally {
            stmt.close();
        }
    }
    public void updateUserInDataBase(User user, Connection con, int id, int address_id)throws Exception{
        Statement stmt = null;
        try {
            String queryUpdateAddress = String.format("%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s", "update address set postcode = '", user.getPostcode(), "', country = '", user.getCountry(), "', region = '", user.getState(),
                    "', city = '", user.getCity(), "', street = '", user.getStreet(), "', house =", String.valueOf(user.getHouse()), ", flat = ", String.valueOf(user.getFlat()), " where id = ", String.valueOf(address_id), ";");
            stmt = con.createStatement();
            int response = stmt.executeUpdate(queryUpdateAddress);
            SimpleDateFormat format = new SimpleDateFormat("YYYYMMdd");
            GregorianCalendar gc = user.getDob();
            String birthday = String.format("%s%s%s", "(", format.format(gc.getTime()), ")");
            String queryUpdatePerson = String.format("%s%s%s%s%s%s%s%s%s","update persons set birthday =",birthday,", gender = '",user.getGender().charAt(0),"', inn = '",user.getInn(),"' where id =",String.valueOf(id),";");
            stmt = null;
            stmt = con.createStatement();
            response = stmt.executeUpdate(queryUpdatePerson);
        }catch (SQLException e){
            e.printStackTrace();
            System.out.println("error updating in DB");
        }finally {
            con.close();
            stmt.close();
        }
    }
}
