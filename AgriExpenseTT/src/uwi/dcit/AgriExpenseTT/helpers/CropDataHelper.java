package uwi.dcit.AgriExpenseTT.helpers;


import android.content.Context;
import android.content.res.Resources;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import uwi.dcit.AgriExpenseTT.R;
import uwi.dcit.AgriExpenseTT.helpers.PlantingHelper.Helper;
import uwi.dcit.AgriExpenseTT.helpers.PlantingHelper.HelperFactory;

public class CropDataHelper {

	public HashMap<String,Integer> resources;
	public CropDataHelper(Context ctx){
		super();
		resources = new HashMap<String, Integer>();
		populateResources(ctx);
	}

	private static JSONObject getCropsJSON(Context context) throws IOException {
		InputStream is = context.getResources().openRawResource(R.raw.crops);
		Writer writer = new StringWriter();
		char[] buffer = new char[1024];
		try {
			Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			int n;
			while ((n = reader.read(buffer)) != -1) {
				writer.write(buffer, 0, n);
			}
			String jsonString = writer.toString();
			return new JSONObject(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			is.close();
		}
		return null;
	}

	public static ArrayList<String> getCrops(Context context) {
		ArrayList<String> list = new ArrayList<>();
		try {
			JSONObject cropsJson = getCropsJSON(context);
			Iterator<String> i = cropsJson.keys();
			while (i.hasNext()) {

				list.add(i.next());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	// Used in the CycleListAdapter in the FragmentViewCycles
	public static int getCropsDrawable(Context context, String cropName) {
		String name = "crop_under_rain_solid";
		try {
			JSONObject cropsJson = getCropsJSON(context);
			if (cropsJson != null && cropsJson.has(cropName)) {
				JSONObject rec = cropsJson.getJSONObject(cropName);
				if (rec.has("image")) {
					name = rec.getString("image");
				}
			}
			Resources resources = context.getResources();
			final int id = resources.getIdentifier(name,"drawable",context.getPackageName());
			return id;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	private void populateResources(Context ctx){
		HelperFactory factory = new HelperFactory();
		Helper planting = factory.getHelper("cropsresource");
		planting.addImage(ctx,resources);
	}
	public Integer getResourceId(String key){
		if(resources.containsKey(key))
			return resources.get(key);
		return R.drawable.plant;
	}
}
