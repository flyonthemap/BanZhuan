package com.shuao.banzhuan.tools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.AbstractList;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class JSONUtils {

    private static final int CODE_BYTE = 1;
    private static final int CODE_SHORT = 2;
    private static final int CODE_INT = 3;
    private static final int CODE_FLOAT = 4;
    private static final int CODE_LONG = 5;
    private static final int CODE_DOUBLE = 6;
    private static final int CODE_BOOLEAN = 7;
    private static final int CODE_CHAR = 8;
    private static final int CODE_STRING = 9;
    private static final int CODE_DATE = 10;
    private static final int CODE_ARRAY = 12;
    // private static final int CODE_LIST = 13;
    private static final int CODE_ENUM = 11;
    private static final int CODE_COLLECTION = 14;
    private static final int CODE_BEAN = 15;
    private static final int CODE_MAP = 16;
    private static final int CODE_JSON = 17;
    private static final int CODE_PACK = 18;// 基本类型的包装类 int char 等;
    private static final int CODE_OBJECT = 19;
    private static final int CODE_JSONOBJECT = 20;
    private static final int CODE_JSONARRAY = 21;

    private static final int DIRECT_TRAN_TAG = 110;
    private static final int ARRAY_OR_COLLECTION = 111;
    private static final int MAP_TAG = 112;
    private static final int BEAN_TAG = 113;
    private static final int DATE_TAG = 114;
    private static final int ENUM_TAG = 115;

    public static JSONObject instanceJsonObject(String jsonStr) {
        JSONObject jsonObject = null;
        ;
        try {
            jsonObject = new JSONObject(jsonStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    /**
     * bean对象生成JSONObject
     *
     * @param object
     * @return
     */
    public static JSONObject bean2JSONObject(Object object) {
        if (object == null) {
            return null;
        }
        Class clz = object.getClass();
        if (isArray(clz) || isCollection(clz) || isList(clz)) {// 如果是集合类型直接返回null
            return null;
        }
        if (iSMap(clz)) {
            return map2JSONObject((Map<String, Object>) object);
        }
        JSONObject jsonObject = new JSONObject();
        if (isBean(clz)) {
            Field[] fields = clz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                String key = field.getName();
                Object value = null;
                try {
                    value = field.get(object);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                try {
                    if (value == null) {
                        jsonObject.put(key, JSONObject.NULL);
                    } else {
                        Class<?> type = field.getType();// 该属性的数据类型
                        int code = getJSONPutTag(type);
                        switch (code) {
                            case DIRECT_TRAN_TAG:
                                jsonObject.put(key, value);
                                break;
                            case ARRAY_OR_COLLECTION:
                                jsonObject.put(key, collection2JSONArray(value));
                                break;
                            case MAP_TAG:
                                jsonObject.put(key, map2JSONObject((Map<String, Object>) value));
                                break;
                            case BEAN_TAG:
                                jsonObject.put(key, bean2JSONObject(value));
                                break;
                            case DATE_TAG:
                                Date date = (Date) value;
                                jsonObject.put(key, setDateJSONValue(date));
                                break;
                            case ENUM_TAG:
                                Enum e = (Enum) value;
                                jsonObject.put(key, e.name());
                                break;
                            default:
                                break;
                        }
                    }
                } catch (JSONException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        }
        return jsonObject;
    }

    /**
     * Map对象生成JSONObject
     *
     * @param map
     * @return
     */
    public static JSONObject map2JSONObject(Map<String, Object> map) {
        if (map == null || !iSMap(map.getClass())) {
            return null;
        }
        JSONObject jsonObject = new JSONObject();

        Set<String> set = map.keySet();
        for (String key : set) {
            Object value = map.get(key);
            try {
                if (value == null) {
                    jsonObject.put(key, JSONObject.NULL);
                } else {
                    Class<?> cls = value.getClass();
                    int code = getJSONPutTag(cls);
                    switch (code) {
                        case DIRECT_TRAN_TAG:
                            jsonObject.put(key, value);
                            break;
                        case ARRAY_OR_COLLECTION:
                            jsonObject.put(key, collection2JSONArray(value));
                            break;
                        case MAP_TAG:
                            jsonObject.put(key, map2JSONObject((Map<String, Object>) value));
                            break;
                        case BEAN_TAG:
                            jsonObject.put(key, bean2JSONObject(value));
                            break;
                        case DATE_TAG:
                            Date date = (Date) value;
                            jsonObject.put(key, setDateJSONValue(date));
                            break;
                        case ENUM_TAG:
                            Enum e = (Enum) value;
                            jsonObject.put(key, e.name());
                            break;
                        default:
                            break;
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jsonObject;
    }

    /**
     * 集合类型转化成JSONArray
     *
     * @param <T>
     *
     * @param object
     * @return
     */
    public static <T> JSONArray collection2JSONArray(Object object) {
        if (object == null) {
            return null;
        }
        Class clazz = object.getClass();
        if (isArray(clazz)) {// 数组类型
            return array2JSONArray(object);
        } else if (isCollection(clazz)) {
            Collection collection = (Collection) object;
            JSONArray jsonArray = new JSONArray();
            for (Iterator inter = collection.iterator(); inter.hasNext();) {
                Object obj = inter.next();
                Class<? extends Object> type = obj.getClass();
                switch (getJSONPutTag(type)) {
                    case DIRECT_TRAN_TAG:// 可直接转化类型
                        jsonArray.put(obj);
                        break;
                    case ARRAY_OR_COLLECTION:// 集合或者数组类型
                        jsonArray.put(collection2JSONArray(obj));
                        break;
                    case MAP_TAG:// MAP类型
                        jsonArray.put(map2JSONObject((Map<String, Object>) obj));
                        break;
                    case BEAN_TAG:// Javabean类型
                        jsonArray.put(bean2JSONObject(obj));
                        break;
                    case DATE_TAG:
                        Date date = (Date) obj;
                        jsonArray.put(setDateJSONValue(date));

                        break;
                    case ENUM_TAG:
                        Enum e = (Enum) obj;
                        jsonArray.put(e.name());

                        break;
                    default:
                        break;
                }
            }
            return jsonArray;
        }
        return null;
    }

    /**
     * 数组类型编程JsonArray
     *
     * @param object
     * @return
     */
    public static JSONArray array2JSONArray(Object object) {
        if (object == null) {
            return null;
        }
        Class<?> clazz = object.getClass();
        if (!isArray(clazz)) {
            return null;
        }

        JSONArray jsonArray = new JSONArray();
        int length = Array.getLength(object);
        if (length == 0) {
            return jsonArray;
        }
        Class<?> elementype = clazz.getComponentType();// 数组中的元素的数据类型
        int code = getJSONPutTag(elementype);
        switch (code) {
            case DIRECT_TRAN_TAG:
                for (int i = 0; i < length; i++) {
                    jsonArray.put(Array.get(object, i));
                }
            case ARRAY_OR_COLLECTION:
                for (int i = 0; i < length; i++) {
                    Object o = Array.get(object, i);
                    jsonArray.put(collection2JSONArray(o));
                }
                break;
            case MAP_TAG:
                for (int i = 0; i < length; i++) {
                    Object o = Array.get(object, i);
                    jsonArray.put(map2JSONObject((Map<String, Object>) o));
                }
                break;
            case BEAN_TAG:
                for (int i = 0; i < length; i++) {
                    jsonArray.put(bean2JSONObject(Array.get(object, i)));
                }
                break;
            case DATE_TAG:
                for (int i = 0; i < length; i++) {
                    Date date = (Date) Array.get(object, i);
                    jsonArray.put(setDateJSONValue(date));
                }
                break;
            case ENUM_TAG:
                for (int i = 0; i < length; i++) {
                    Enum e = (Enum) Array.get(object, i);
                    jsonArray.put(e.name());
                }
                break;

            default:
                break;
        }
        return jsonArray;

    }

    public static <T> T jsonObject2Bean(JSONObject jsonObject, Class<T> beaClass) {

        if (jsonObject == null || beaClass == null || !isBean(beaClass)) {

            return null;
        }
        T instance = null;
        try {
            instance = beaClass.newInstance();
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (instance != null) {
            Field[] fields = beaClass.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                String name = field.getName();
                Class clazz = field.getType();
                if (!jsonObject.isNull(name)) {
                    putJSONObjectValue2BeanField(jsonObject, name, clazz, field, instance);
                }
            }
        }
        return (T) instance;
    }

    public static <T> Map<String, T> jsonObject2Map(JSONObject jsonObject, Class<?> mapClass, Class<T> valueClazz) {
        if (!iSMap(mapClass)) {
            return null;
        }
        Map<String, T> map = null;
        if (Modifier.isAbstract(mapClass.getModifiers())) {// 传递的Class是一个接口或者抽象类
            map = new HashMap<String, T>();
        } else {
            try {
                map = (Map<String, T>) mapClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (jsonObject.length() == 0) {
            return map;
        }
        JSONArray array = jsonObject.names();
        int length = array.length();

        switch (getClazzTag(valueClazz)) {
            case CODE_BEAN:
                for (int i = 0; i < length; i++) {
                    try {
                        String key = array.getString(i);
                        if (!jsonObject.isNull(key)) {
                            JSONObject jObject = jsonObject.getJSONObject(key);
                            map.put(key, jsonObject2Bean(jObject, valueClazz));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CODE_COLLECTION:
                for (int i = 0; i < length; i++) {
                    try {
                        String key = array.getString(i);
                        if (!jsonObject.isNull(key)) {
                            JSONArray jsonArray = jsonObject.getJSONArray(key);
                            map.put(key, jsonArray2Colletion(jsonArray, valueClazz, String.class));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CODE_ARRAY:
                Class<?> elementype = valueClazz.getComponentType();// 数组中的元素的数据类型
                for (int i = 0; i < length; i++) {
                    try {

                        String key = array.getString(i);
                        if (!jsonObject.isNull(key)) {
                            JSONArray jsonArray = jsonObject.getJSONArray(key);
                            map.put(key, (T) jsonArray2Array(jsonArray, elementype));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            case CODE_ENUM:
                for (int i = 0; i < length; i++) {
                    try {
                        String key = array.getString(i);
                        if (!jsonObject.isNull(key)) {
                            Enum enumvalue = parseJsonValue2Enum(valueClazz, jsonObject.getString(key));
                            map.put(key, (T) enumvalue);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CODE_DATE:
                for (int i = 0; i < length; i++) {
                    try {
                        String key = array.getString(i);
                        if (!jsonObject.isNull(key)) {
                            Date date = parseJSONValue2Date(jsonObject.getString(key));
                            map.put(key, (T) date);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CODE_MAP:
                for (int i = 0; i < length; i++) {
                    try {
                        String key = array.getString(i);
                        if (!jsonObject.isNull(key)) {
                            map.put(key, (T) jsonObject2Map(jsonObject.getJSONObject(key), valueClazz, String.class));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                for (int i = 0; i < length; i++) {
                    try {
                        String key = array.getString(i);
                        if (!jsonObject.isNull(key)) {
                            map.put(key, (T) jsonObject.get(key));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }

        return map;

    }

    @SuppressWarnings("unchecked")
    public static <T, E> T jsonArray2Colletion(JSONArray jsonArray, Class<T> colectionClass, Class<E> valueClass) {
        if (jsonArray == null || colectionClass == null || !isCollection(colectionClass)) {
            return null;
        } else {
            Collection c = null;
            if (!Modifier.isAbstract(colectionClass.getModifiers())) {// 如果是个普通类（不是抽象类或者接口）
                try {
                    c = (Collection) colectionClass.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (colectionClass == Collection.class || colectionClass == List.class
                    || (AbstractList.class.isAssignableFrom(colectionClass) && Modifier.isAbstract(colectionClass.getModifiers()))) {// list接口
                c = new ArrayList();
            } else if (colectionClass == Set.class || (AbstractSet.class.isAssignableFrom(colectionClass) && Modifier.isAbstract(colectionClass.getModifiers()))) {
                c = new HashSet();
            }
            if (c != null) {
                c.addAll(jsonArray2ArrayList(jsonArray, valueClass));
            }
            return (T) c;
        }
    }

    public static <T> Object[] jsonArray2Array(JSONArray jsonArray, Class<T> elementype) {
        if (jsonArray == null || elementype == null || !elementype.isArray()) {
            return null;
        }
        ArrayList list = jsonArray2ArrayList(jsonArray, elementype);
        return list.toArray();

    }

    public static <T> ArrayList jsonArray2ArrayList(JSONArray jsonArray, Class<T> elementype) {
        ArrayList list = new ArrayList();
        int length = jsonArray.length();
        switch (getClazzTag(elementype)) {
            case CODE_ARRAY:
                Class<?> comclazz = elementype.getComponentType();
                for (int i = 0; i < length; i++) {
                    try {
                        if (!jsonArray.isNull(i)) {
                            list.add(jsonArray2Array(jsonArray.getJSONArray(i), comclazz));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CODE_BYTE:
                for (int i = 0; i < length; i++) {
                    try {
                        if (!jsonArray.isNull(i)) {
                            list.add((byte) jsonArray.getInt(i));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CODE_INT:
                for (int i = 0; i < length; i++) {
                    try {
                        if (!jsonArray.isNull(i)) {
                            list.add(jsonArray.getInt(i));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CODE_FLOAT:
                for (int i = 0; i < length; i++) {
                    try {
                        if (!jsonArray.isNull(i)) {
                            list.add((float) jsonArray.getDouble(i));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            case CODE_LONG:
                for (int i = 0; i < length; i++) {
                    try {
                        if (!jsonArray.isNull(i)) {
                            list.add(jsonArray.getLong(i));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            case CODE_DOUBLE:
                for (int i = 0; i < length; i++) {
                    try {
                        if (!jsonArray.isNull(i)) {
                            list.add(jsonArray.getDouble(i));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            case CODE_BOOLEAN:
                for (int i = 0; i < length; i++) {
                    try {
                        if (!jsonArray.isNull(i)) {
                            list.add(jsonArray.getBoolean(i));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            case CODE_DATE:
                for (int i = 0; i < length; i++) {
                    try {
                        if (!jsonArray.isNull(i)) {
                            list.add(parseJSONValue2Date(jsonArray.get(i)));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            case CODE_ENUM:
                for (int i = 0; i < length; i++) {
                    try {
                        if (!jsonArray.isNull(i)) {
                            list.add(parseJsonValue2Enum(elementype, jsonArray.getString(i)));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CODE_CHAR:
                for (int i = 0; i < length; i++) {
                    try {
                        if (!jsonArray.isNull(i)) {
                            list.add(jsonArray.getString(i).charAt(0));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CODE_COLLECTION:
                for (int i = 0; i < length; i++) {
                    try {
                        if (!jsonArray.isNull(i)) {
                            list.add(jsonArray2Colletion(jsonArray.getJSONArray(i), elementype, String.class));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            case CODE_MAP:
                for (int i = 0; i < length; i++) {
                    try {
                        if (!jsonArray.isNull(i)) {
                            list.add(jsonObject2Map(jsonArray.getJSONObject(i), elementype, String.class));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CODE_BEAN:
                for (int i = 0; i < length; i++) {
                    try {
                        if (!jsonArray.isNull(i)) {
                            list.add(jsonObject2Bean(jsonArray.getJSONObject(i), elementype));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                for (int i = 0; i < length; i++) {
                    try {
                        if (!jsonArray.isNull(i)) {
                            list.add(jsonArray.get(i));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
        return list;
    }

    private static <T> void putJSONObjectValue2BeanField(JSONObject jsonObject, String name, Class<?> valueClass, Field field, T instance) {
        if (jsonObject == null || name == null || valueClass == null || field == null || instance == null) {
            return;
        }
        try {
            switch (getClazzTag(valueClass)) {
                case CODE_BYTE:
                    int value = jsonObject.getInt(name);
                    field.setByte(instance, (byte) value);
                    break;
                case CODE_INT:
                    field.setInt(instance, jsonObject.getInt(name));
                    break;
                case CODE_FLOAT:
                    field.setFloat(instance, (float) jsonObject.getDouble(name));
                    break;
                case CODE_LONG:
                    field.setLong(instance, jsonObject.getLong(name));
                    break;
                case CODE_DOUBLE:
                    field.setDouble(instance, jsonObject.getDouble(name));
                    break;
                case CODE_BOOLEAN:
                    field.setBoolean(instance, jsonObject.getBoolean(name));
                    break;
                case CODE_CHAR:
                    field.setChar(instance, jsonObject.getString(name).charAt(0));
                    break;
                case CODE_STRING:
                    field.set(instance, jsonObject.getString(name));
                    break;
                case CODE_DATE:
                    field.set(instance, parseJSONValue2Date(jsonObject.get(name)));
                    break;
                case CODE_ENUM:
                    String enumValue = jsonObject.getString(name);
                    field.set(instance, parseJsonValue2Enum(valueClass, enumValue));
                    break;
                case CODE_COLLECTION:
                    ParameterizedType pt = getFildParmaterzendType(field);
                    Class<Object> genericsClz = null;// 实例化泛型的类型
                    if (pt != null) {
                        genericsClz = (Class<Object>) pt.getActualTypeArguments()[0];// <泛型的类型>
                    } else {
                        genericsClz = Object.class;
                    }
                    JSONArray jsonArray = jsonObject.getJSONArray(name);
                    Object o = jsonArray2Colletion(jsonArray, valueClass, genericsClz);
                    field.set(instance, o);
                    break;
                case CODE_ARRAY:// 数组类型
                    Class<?> elementype = valueClass.getComponentType();// 数组中的元素的数据类型
                    Object[] array = jsonArray2Array(jsonObject.getJSONArray(name), elementype);
                    field.set(instance, array);
                    break;
                case CODE_MAP:
                    ParameterizedType pt1 = getFildParmaterzendType(field);
                    Class<Object> genericsClz1 = null;// 实例化泛型的类型
                    if (pt1 != null) {
                        genericsClz1 = (Class<Object>) pt1.getActualTypeArguments()[1];// <泛型的类型>
                    } else {
                        genericsClz1 = Object.class;
                    }
                    field.set(instance, jsonObject2Map(jsonObject.getJSONObject(name), valueClass, genericsClz1));
                    break;
                case CODE_BEAN:
                    field.set(instance, jsonObject2Bean(jsonObject.getJSONObject(name), valueClass));
                    break;
                case CODE_PACK:
                    field.set(instance, jsonObject.get(name));
                case CODE_OBJECT:
                    field.set(instance, jsonObject.get(name));
                    break;

                default:
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static Enum parseJsonValue2Enum(Class enumClass, String value) {
        Method valueMethod;
        if (enumClass == null || value == null || !enumClass.isEnum()) {
            return null;
        }
        try {
            valueMethod = enumClass.getMethod("values");
            Object[] values = (Object[]) valueMethod.invoke(null);

            for (Object value1 : values) {
                Enum e = (Enum) value1;
                if (value.equals(e.name())) {
                    return e;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 获取Field对象的参数化类型（获取泛型的步骤）
     *
     * @param field
     * @return
     */
    private static ParameterizedType getFildParmaterzendType(Field field) {
        Type type = field.getGenericType(); // 得到泛型类型
        ParameterizedType pt = null;
        try {
            pt = (ParameterizedType) type;// 如果没有泛型抛异常
        } catch (Exception e) {
            // TODO: handle exception
        }
        return pt;
    }

    /**
     * 返回date数据类型应该封装成的json样式 有的是获取毫秒值
     * 如date:1232321321321;有的这种封装date:"Date(13213123123)"后期根据需要调整
     *
     * @param date
     * @return
     */
    private static Object setDateJSONValue(Date date) {
        return String.valueOf(date.getTime());
    }

    /**
     * 把JSON的值调整为Date数据类型
     *
     * @param jsonVuale
     * @return
     */
    private static Date parseJSONValue2Date(Object jsonVuale) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHssmm");

        long time = 0;
        try {
            time = sdf.parse((String) jsonVuale).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date date = new Date(time);
        return date;
    }

    /**
     * 根据不同数据类型不同数字，有可直接转化型, 集合 型，Map型，bean型，date型，枚举类型
     *
     * @param clazz
     * @return
     */
    private static int getJSONPutTag(Class<?> clazz) {
        if (iSNumber(clazz) || iSBoolean(clazz) || iSChar(clazz) || iSString(clazz) || clazz == JSONObject.class || clazz == JSONArray.class) {
            return DIRECT_TRAN_TAG;
        }
        if (isArray(clazz) || isCollection(clazz) || isList(clazz)) {
            return ARRAY_OR_COLLECTION;
        }
        if (iSMap(clazz)) {
            return MAP_TAG;
        }
        if (isBean(clazz)) {
            return BEAN_TAG;
        }
        if (isDate(clazz)) {
            return DATE_TAG;
        }
        if (isEnum(clazz)) {
            return ENUM_TAG;
        }
        return 0;

    }

    /**
     * 对应数据类型返回相应的标志数字
     *
     * @param clazz
     * @return
     */
    private static int getClazzTag(Class<?> clazz) {
        // if (iSNumber(clazz)) {// 数字类型
        // return CODE_NUMBER;
        // }
        if (clazz == null) {
            return 100;
        }
        if (clazz == byte.class) {
            return CODE_BYTE;
        }
        if (clazz == int.class) {
            return CODE_INT;
        }
        if (clazz == short.class) {
            return CODE_SHORT;
        }

        if (clazz == float.class) {
            return CODE_FLOAT;
        }
        if (clazz == long.class) {
            return CODE_LONG;
        }
        if (clazz == double.class) {
            return CODE_DOUBLE;
        }
        if (clazz == boolean.class) {
            return CODE_BOOLEAN;
        }
        if (clazz == char.class) {
            return CODE_CHAR;
        }
        if (iSString(clazz)) {
            return CODE_STRING;
        }
        if (isDate(clazz)) {
            return CODE_DATE;
        }
        if (isEnum(clazz)) {
            return CODE_ENUM;
        }
        if (isCollection(clazz)) {
            return CODE_COLLECTION;
        }
        if (isArray(clazz)) {
            return CODE_ARRAY;
        }
        if (iSMap(clazz)) {
            return CODE_MAP;
        }
        if (isBean(clazz)) {
            return CODE_BEAN;
        }
        if (Number.class.isAssignableFrom(clazz) || Character.class == clazz || Enum.class == clazz || Boolean.class == clazz) {
            return CODE_PACK;
        }
        if (clazz == Object.class) {
            return CODE_OBJECT;
        }
        return 100;
    }

    /**
     * 判断对象是否为数组类型
     *
     * @return 如果是数组类型返回True (为空是返回false) 否则返回fasle
     */
    private static boolean isArray(Object obj) {

        return obj != null && obj.getClass().isArray();
    }

    /**
     * 判断对象是否为数组类型
     *
     * @param clazz
     *            待判断的class类
     * @return 如果是数组类型返回True (为空是返回false) 否则返回fasle
     */
    private static boolean isArray(Class<?> clazz) {
        return clazz != null && clazz.isArray();
    }

    /**
     * 判断该class类是否是集合类型 值支持list和set两只集合
     *
     * @param clazz
     *            待判断的class
     * @return 是集合类型返回true 否则返回false
     */
    private static Boolean isCollection(Class<?> clazz) {

        return clazz != null && (Collection.class == clazz || List.class.isAssignableFrom(clazz) || Set.class.isAssignableFrom(clazz));

    }

    /**
     * 判断该对象类是否是集合类型
     *
     * @param obj
     *            待判断的对象
     * @return 是集合类型返回true 否则返回false
     */
    private static Boolean isCollection(Object obj) {
        return obj != null && (Collection.class == obj.getClass() || List.class.isAssignableFrom(obj.getClass()) || Set.class.isAssignableFrom(obj.getClass()));

    }

    /**
     * 判断某个对象是否是一个javaBean 这里要求bean要实现Bean接口
     *
     * @param obj
     *            待判断的对象
     * @return 是javaBean返回 true 当为空或者不是JavaBean时候返回false
     */
    private static boolean isBean(Object obj) {
        return obj != null && Bean.class.isAssignableFrom(obj.getClass());
    }

    /**
     * 判断某个对象是否是一个javaBean 这里要求bean要实现Bean接口
     *
     * @param clazz
     *            待判断的class类
     * @return 是javaBean返回 true 当为空或者不是JavaBean时候返回false
     */
    private static boolean isBean(Class<?> clazz) {
        return clazz != null && Bean.class.isAssignableFrom(clazz);
    }

    /**
     * 判断某个类是否是Map型 要求Class是Map接口或者继承了AbstractMap的类
     *
     * @param clazz
     *            待判断的class类
     * @return 是Map集合的子类返回true 否则返回false
     */
    private static boolean iSMap(Class<?> clazz) {
        return clazz != null && (Map.class == clazz || AbstractMap.class.isAssignableFrom(clazz));
    }

    /**
     * 判断对象是否为数字类型
     *
     * @param clazz
     *            待判断的class类
     * @return 如果是数字类型 返回True (为空是返回false) 否则返回fasle
     */
    private static boolean iSNumber(Class<?> clazz) {
        if (clazz == null) {
            return false;
        } else {
            // 是基本数据类型 但不是char和boolean类型
            if (clazz.isPrimitive() && clazz != char.class && clazz != boolean.class) {
                return true;
            } else {
                return Number.class.isAssignableFrom(clazz);
            }
        }
    }

    /**
     * 判断对象是否为布尔型 boolean Boolean
     *
     * @param clazz
     *            待判断的class类
     * @return 如果是布尔型 返回True (为空是返回false) 否则返回fasle
     */
    private static boolean iSBoolean(Class<?> clazz) {
        return clazz != null && (clazz == boolean.class || Boolean.TYPE.isAssignableFrom(clazz) || Boolean.class.isAssignableFrom(clazz));
    }

    /**
     * 判断对象是否为字符传类型数据
     *
     * @param clazz
     *            待判断的class类
     * @return 如果是字符串 返回True (为空是返回false) 否则返回fasle
     */
    private static boolean iSString(Class<?> clazz) {
        if (clazz == null) {
            return false;
        }
        return clazz == String.class;
    }

    private static boolean isList(Class<?> clazz) {
        return clazz != null && List.class.isAssignableFrom(clazz);
    }

    /**
     * 判断对象是否为字符类型或者字符型的包装类 char Character
     *
     * @param clazz
     *            待判断的class类
     * @return 如果是字符型返回True (为空是返回false) 否则返回fasle
     */
    private static boolean iSChar(Class<?> clazz) {
        if (clazz == null) {
            return false;
        }
        return clazz == char.class || clazz == Character.class;
    }

    /**
     * 日期数据类型
     *
     * @param clazz
     * @return
     */
    private static boolean isDate(Class<?> clazz) {
        if (clazz == null) {
            return false;
        }
        return clazz == Date.class || Date.class.isAssignableFrom(clazz);
    }

    private static boolean isEnum(Class<?> clazz) {
        if (clazz == null) {
            return false;
        }
        return clazz.isEnum();
        // == Enum.class || Enum.class.isAssignableFrom(clazz);
    }
}

