package vnext.com.openvidu.parser;

import java.lang.reflect.Type;
import java.sql.Date;
import java.util.Map;


import com.alibaba.fastjson.parser.deserializer.DateDeserializer;
import com.alibaba.fastjson.serializer.DateSerializer;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public class JsonUtils {

	public static final Gson gson = new GsonBuilder()
			.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
			.registerTypeAdapter(Date.class, new DateSerializer())
			.registerTypeAdapter(Date.class, new DateDeserializer()).create();

	public static <T> T fromJson(String json, Class<T> clazz) {
		return gson.fromJson(json, clazz);
	}

	public static <T> T fromJson(String json, Type type) {
		return gson.fromJson(json, type);
	}

	public static String toJson(Object src) {
		return gson.toJson(src);
	}

	public static String toJson(Object src, String root) {
		JsonObject json = new JsonObject();
		json.add(root, gson.toJsonTree(src));
		return json.toString();
	}
}