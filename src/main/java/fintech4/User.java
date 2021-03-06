package fintech4;

import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Random;

public class User {
    private String mGender;

    private String mTitle;
    private String mFirstName;
    private String mLastName;
    private String mInn;

    private String mCity;
    private String mStreet;
    private String mState;
    private String mCountry;
    private int mHouse;
    private int mFlat;
    private String mPostcode;

    private GregorianCalendar mDob;
    private int mAge;

    public String getCountry() {
        return mCountry;
    }

    public void setCountry(String country) {
        this.mCountry = country;
    }

    public String getGender() {
        return mGender;
    }
    public void SetGender(String gender){
        if (gender.equals("female")){
            this.mGender = "Ж";
        }else{
            this.mGender = "М";
        }
    }
    public void setInn(String inn) {this.mInn = inn;}
    public void setGender(String gender) {
        this.mGender = gender;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String firstName) {
        this.mFirstName = firstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public void setLastName(String lastName) {
        this.mLastName = lastName;
    }

    public String getCity() {
        return mCity;
    }

    public void setCity(String city) {
        this.mCity = city;
    }

    public String getStreet() {
        return mStreet;
    }

    public void setStreet(String street) {
        this.mStreet = street;
    }

    public String getState() {
        return mState;
    }

    public void setState(String state) {
        this.mState = state;
    }

    public int getHouse() {
        return mHouse;
    }

    public void setHouse(int house) {
        this.mHouse = house;
    }

    public String getPostcode() {
        return mPostcode;
    }

    public void setPostcode(String postcode) {
        this.mPostcode = postcode;
    }

    public GregorianCalendar getDob() {
        return mDob;
    }

    public void setDob(GregorianCalendar dob) {
        this.mDob = dob;
    }

    public int getAge() {
        return mAge;
    }

    public void setAGE(int age) {
        this.mAge = age;
    }

    public void setFlat() {
        Random rand = new Random();
        int flat = (1 + rand.nextInt(1000));
        this.mFlat = flat;
    }
    public int getFlat() {
        return mFlat;
    }
    public void setINN() throws Exception {
        String inn = new String();
        Random rand = new Random();
        ArrayList<Integer> Inn = new ArrayList(12);
        Inn.add(7);
        Inn.add(7);

        int kontrNumber1;
        for(kontrNumber1 = 2; kontrNumber1 < 10; ++kontrNumber1) {
            Inn.add(rand.nextInt(9));
        }

        kontrNumber1 = 0;
        int[] Arr1 = new int[]{7, 2, 4, 10, 3, 5, 9, 4, 6, 8};

        for(int i = 0; i < 10; ++i) {
            kontrNumber1 += Arr1[i] * (Integer)Inn.get(i);
        }
        int k1 = kontrNumber1 % 11;
        Inn.add(k1 % 10);
        int[] Arr2 = new int[]{3, 7, 2, 4, 10, 3, 5, 9, 4, 6, 8};
        int kontrNumber2 = 0;

        for(int i = 0; i < 11; ++i) {
            kontrNumber2 += Arr2[i] * (Integer)Inn.get(i);
        }

        int k2 = kontrNumber2 % 11;
        Inn.add(k2 % 10);

        int i;
        for(Iterator var11 = Inn.iterator(); var11.hasNext(); inn = inn + i) {
            i = (Integer)var11.next();
        }
        this.mInn = inn;
    }
    public String getInn() {
        return mInn;
    }
    public void setHouse() {
        Random rand = new Random();
        int house = (1 + rand.nextInt(100));
        this.mHouse = house;
    }
    public void setFlat(int flat){ this.mFlat = flat;}
    public void setDayOfBirth() {
        GregorianCalendar gc = new GregorianCalendar();
        int year = randBetween(1900, 2018);
        gc.set(1, year);
        int dayOfYear = randBetween(1, gc.getActualMaximum(6));
        gc.set(6, dayOfYear);
        this.mDob = gc;
    }

    public int randBetween(int start, int end) {
        return start + (int)Math.round(Math.random() * (double)(end - start));
    }

    public void setAge(GregorianCalendar dob) {
        GregorianCalendar today = new GregorianCalendar();
        int year1 = today.get(1);
        int year2 = dob.get(1);
        int age = year1 - year2;
        int month1 = today.get(2);
        int month2 = dob.get(2);
        if (month2 > month1) {
            --age;
        } else if (month1 == month2) {
            int day1 = today.get(5);
            int day2 = dob.get(5);
            if (day2 > day1) {
                --age;
            }
        }
        this.mAge = age;
    }
    public static String getAddress(String filename,int count)throws Exception {
        BufferedReader bufferedReader = new BufferedReader(beginReading(filename));
        Random rand = new Random();
        int num = 1 + rand.nextInt(count);
        String ans = new String();
        for (Integer pointLine = 1; bufferedReader.ready(); pointLine = pointLine + 1) {
            String line = bufferedReader.readLine();
            if (pointLine == num){
                ans = line;
            }
        }
        return ans;
    }

    private static BufferedReader beginReading (String filename) throws Exception {
        File file = new File(filename);
        String encoding = System.getProperty("console.encoding", "Cp1251");
        Reader fileReader = new InputStreamReader(new FileInputStream(file), encoding);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        return bufferedReader;
    }
    public void setPostcode(){
        Random rand = new Random();
        long postcode = 100000 + rand.nextInt(100000);
        this.mPostcode = String.valueOf(postcode);
    }
    public User getUserFromDB(Connection con, int i)throws Exception {
        User user = new User();
        Statement stmt = con.createStatement();
        ResultSet rsPerson = null;
        ResultSet rsAddress = null;
        try {
            String queryGetUserPerson = String.format("%s%s%s", "select * from persons where id = ", String.valueOf(i), ";");
            rsPerson = stmt.executeQuery(queryGetUserPerson);
            int address_id = 0;
            while (rsPerson.next()) {
                user.setLastName(rsPerson.getString("surname"));
                user.setFirstName(rsPerson.getString("name"));
                user.setTitle(rsPerson.getString("middlename"));
                user.setGender(rsPerson.getString("gender"));
                GregorianCalendar gc = new GregorianCalendar();
                gc.setTime(rsPerson.getDate("birthday"));
                user.setDob(gc);
                user.setAge(gc);
                user.setInn(rsPerson.getString("inn"));
                address_id = rsPerson.getInt("address_id");
            }
            stmt = null;
            String queryGetUserAddress = String.format("%s%s%s", "select * from address where id = ", String.valueOf(address_id), ";");
            stmt = con.createStatement();
            rsAddress = stmt.executeQuery(queryGetUserAddress);
            while (rsAddress.next()) {
                user.setPostcode(rsAddress.getString("postcode"));
                user.setCountry(rsAddress.getString("country"));
                user.setState(rsAddress.getString("region"));
                user.setCity(rsAddress.getString("city"));
                user.setStreet(rsAddress.getString("street"));
                user.setHouse(rsAddress.getInt("house"));
                user.setFlat(rsAddress.getInt("flat"));
            }


        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("wrong select query");
        } finally {
            con.close();
            stmt.close();
            rsAddress.close();
            rsPerson.close();
        }
        return user;
    }
}
