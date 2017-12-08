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

public class CropImageHelper {

	public HashMap<String,Integer> resources;
	public CropImageHelper(Context ctx){
		super();
		resources = new HashMap<String, Integer>();
		populateResources(ctx);
	}

//
	private void populateResources(Context ctx){
		HelperFactory factory = new HelperFactory();
		Helper planting = factory.getHelper("cropsresource");
		planting.addImage(ctx,resources);
	}
	// Sets default image and default image if image is not found
	public Integer getResourceId(String key){
		if(resources.containsKey(key))
			return resources.get(key);
		return R.drawable.plant;
	}
}
