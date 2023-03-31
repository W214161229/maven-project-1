package com.mycompany.mavenproject1;
import com.sun.jersey.api.client.*;
import org.json.simple.*;
import org.json.simple.parser.*;
import java.util.*;
public class Mavenproject1 {

    public static void main(String[] args) {
        JsonStudent database = new JsonStudent();
        try{
        database.populateStudents("https://hccs-advancejava.s3.amazonaws.com/student.json");
        } catch(ParseException e) {System.out.println("The parse failed.");}
        System.out.println(database.searchByName("Caleb"));
    }
}
class JsonStudent
{
    private ArrayList<Student> students;
    public void populateStudents(String url) throws ParseException
    {
        students = new ArrayList<Student>();
        Client client = Client.create();
        WebResource webResource = client.resource(url);
        
        ClientResponse clientResponse = webResource.accept("application/json").get(ClientResponse.class);
        if(clientResponse.getStatus() != 200)
        {
            throw new RuntimeException("Parse failed: " + clientResponse.toString());
        }
        
        JSONArray jsonArray = (JSONArray) new JSONParser().parse(clientResponse.getEntity(String.class));
        
        Iterator<Object> it = jsonArray.iterator();
        
        while(it.hasNext())
        {
            JSONObject jsonObject = (JSONObject)it.next();
            System.out.println(jsonObject.get("id"));
            students.add(new Student((long)jsonObject.get("id"), (String)jsonObject.get("first_name"), (String)jsonObject.get("gpa"), (String)jsonObject.get("email"), (String)jsonObject.get("gender")));
        }
    }
    public Student searchByName(String n)
    {
        for(int x = 0; x < students.size(); x++)
        {
            if(students.get(x).getFirstName().equals(n))
                return students.get(x);
        }
        return null;
    }
}
class Student
{
    private long id;
    private String firstName;
    private double gpa;
    private String email;
    private String gender;
    public Student(long i, String n, String g, String e, String d)
    {
        id = i;
        firstName = n;
        gpa = Double.parseDouble(g);
        email = e;
        gender = d;
    }
    public long getID()
    {
        return id;
    }
    public double getGPA()
    {
        return gpa;
    }
    public String getFirstName()
    {
        return firstName;
    }
    public String getEmail()
    {
        return email;
    }
    public String getGender()
    {
        return gender;
    }
    @Override 
    public String toString()
    {
        return firstName + ", ID#" + id + ". " + gpa + " GPA, " + gender + ", " + email + ".";
    }
}
