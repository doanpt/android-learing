package com.dvt.imagesearch;

import com.dvt.adapters.ItemImage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by doantrung on 10/26/15.
 */
public class ImageConnection {

    public ArrayList<Object> getImageListFromJsonObj(JSONObject object) {
        ArrayList<Object> arrImage = new ArrayList<>();
        try {
            JSONObject responseObject = object.getJSONObject("responseData");
            JSONArray resultArray = responseObject.getJSONArray("results");
            arrImage = getImageList(resultArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arrImage;
    }

    public ArrayList<Object> getImageList(JSONArray resultArray) {
        ArrayList<Object> listImages = new ArrayList<Object>();
        ItemImage itemImage;

        try {
            for (int i = 0; i < resultArray.length(); i++) {
                JSONObject obj;
                obj = resultArray.getJSONObject(i);
                itemImage = new ItemImage();
                itemImage.setTitleImage(obj.getString("title"));
                itemImage.setThumbUrl(obj.getString("tbUrl"));
                listImages.add(itemImage);
            }
            return listImages;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

}
