package local.tcltk;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ContainerFactory;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.*;

import static local.tcltk.Constants.QUERY_URL;
import static local.tcltk.HTMLHelper.getVKResponse;

/**
 * Created by user on 24.06.2017.
 */
public class Solution {

    public static String getMessage() {
        return "MURINO 2017";
    }


    public static void main(String[] args) throws ParseException {
//        JSONParser parser=new JSONParser();
//
//        System.out.println("=======decode=======");
//
//        String s="[0,{\"1\":{\"2\":{\"3\":{\"4\":[5,{\"6\":7}]}}}}]";
//        Object obj=parser.parse(s);
//        JSONArray array=(JSONArray)obj;
//        System.out.println("======the 2nd element of array======");
//        System.out.println(array.get(1));
//        System.out.println();
//
//        JSONObject obj2=(JSONObject)array.get(1);
//        System.out.println("======field \"1\"==========");
//        System.out.println(obj2.get("1"));
//
//        s="{}"; obj=parser.parse(s); System.out.println(obj);
//
//        s="[5,]"; obj=parser.parse(s); System.out.println(obj);
//
//        s="[5,,2]"; obj=parser.parse(s); System.out.println(obj);


//        String jsonText = "{\"first\": 123, \"second\": [4, 5, 6], \"third\": 789}";
//        JSONParser parser = new JSONParser();
//        ContainerFactory containerFactory = new ContainerFactory() {
//            public List creatArrayContainer() { return new LinkedList(); }
//            public Map createObjectContainer() { return new LinkedHashMap(); }
//        };
//
//        try{
//            Map json = (Map)parser.parse(jsonText, containerFactory);
//            Iterator iter = json.entrySet().iterator();
//            System.out.println("==iterate result==");
//            while(iter.hasNext()) {
//                Map.Entry entry = (Map.Entry) iter.next();
//                System.out.println(entry.getKey() + "=>" + entry.getValue());
//            }
//
//            System.out.println("==toJSONString()==");
//            System.out.println(JSONValue.toJSONString(json));
//        } catch(ParseException pe){
//            System.out.println(pe);
//        }
////        Result: ==iterate result== first=>123 second=>[4, 5, 6] third=>789 ==toJSONString()== {"first":123,"second":[4,5,6],"third":789}

        // Parsing id, first_name, last_name, photo_50 for one id:

        String queryParams = "users.get?user_id=6191031" +
                "&fields=photo_50" +
                "&v=5.52";
        String query = QUERY_URL + queryParams;

        String json = getVKResponse(query);
        System.out.println(json);

        JSONParser parser = new JSONParser();
        Object obj = parser.parse(json);
        JSONObject response = (JSONObject) obj;
        System.out.println("============");
//        System.out.println(response.get("response").size());

        JSONArray array = (JSONArray) response.get("response");
        System.out.println(array.size());
        System.out.println(array.get(0));
        System.out.println();

        JSONObject map = (JSONObject) array.get(0);
        System.out.println("id: " + map.get("id"));
        System.out.println("first_name: " + map.get("first_name"));
        System.out.println("last_name: " + map.get("last_name"));
        System.out.println("photo_50: " + map.get("photo_50"));
        System.out.println();





    }
}
