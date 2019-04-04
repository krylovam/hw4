package fintech4;


import org.apache.http.HttpResponse;
import org.codehaus.jackson.*;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.GregorianCalendar;
import java.util.Map;


@JsonIgnoreProperties(ignoreUnknown = true)
public class UserApi implements AutoCloseable{
    private String gender;
    private Map<String, String> mName;
    private Map<Object, Object> mLocation;
    private Map<String, String> mDob;

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Map<String, String> getName() {
        return mName;
    }

    public void setName(Map<String, String> name) {
        this.mName = name;
    }

    public Map<Object, Object> getLocation() {
        return mLocation;
    }

    public void setLocation(Map<Object, Object> location) {
        this.mLocation = location;
    }

    public Map<String, String> getDob() {
        return mDob;
    }

    public void setDob(Map<String, String> dob) {
        this.mDob = dob;
    }

    public User getUserApi() throws Exception {
        String date = mDob.get("date");
        String year = date.substring(0,4);
        String month = date.substring(5,7);
        String day = date.substring(8, 10);
        GregorianCalendar calendar = new GregorianCalendar(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
        User user = new User();
        user.setFirstName(mName.get("first"));
        user.setLastName(mName.get("last"));
        user.setTitle(mName.get("title"));
        if (gender.equals("female")){
            user.setGender("Ж");
        }
        else user.setGender("М");
        user.setCity((String)mLocation.get("city"));
        user.setState(String.valueOf(mLocation.get("state")));
        user.setCountry("-");
        user.setStreet(String.valueOf(mLocation.get("street")));
        user.setPostcode(String.valueOf(mLocation.get("postcode")));
        user.setAGE(Integer.parseInt(mDob.get("age")));
        user.setHouse();
        user.setFlat();
        user.setINN();
        user.setDob(calendar);
        return user;
    }
    public void close(){
        this.setDob(null);
        this.setGender(null);
        this.setName(null);
        this.setLocation(null);
    }
    public UserApi mapUser(String json)throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(json).get("results");
        UserApi userApi = new UserApi();
        try{
            UserApi userApi1 = mapper.readValue(jsonNode.get(0).toString(),UserApi.class);
            userApi = userApi1;
            return userApi;
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("error mapping json");
        }
        finally {
            return userApi;
        }
    }


    public String streamToString(HttpResponse res) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(res.getEntity().getContent()));
        try {
            StringBuilder stringBuilder = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }

            return stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("error reading");
        }finally {
            reader.close();
        }
        return null;
    }

}
