package Data;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JsonReader {

	public static Object[][] getJSONData(String JSON_path, String JSON_Data, int JSON_attributes)
			throws FileNotFoundException, IOException, ParseException {

		Object object = new JSONParser().parse(new FileReader(JSON_path));
		JSONObject jsonObject = (JSONObject) object;
		JSONArray jsonArry = (JSONArray) jsonObject.get(JSON_Data);

		Object[][] arr = new String[jsonArry.size()][JSON_attributes];
		for (int i = 0; i < jsonArry.size(); i++) {
			JSONObject jsonObject1 = (JSONObject) jsonArry.get(i);
			arr[i][0] = String.valueOf(jsonObject1.get("SearchData"));

		}

		return arr;

	}

}
