package com.tobbetu.en4s;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class Utils {

	private final static String TAG = "Utils";	
    private static Marker place = null;

    /**
     * 
     * @param map
     * @param position
     * 
     *            Parametre olarak aldigi harita uzerindeki, LatLng ile
     *            gosterilen konuma marker ekler.
     */
    public static void addAMarker(GoogleMap map, LatLng position, boolean draggable) {

        if (place != null)
            place.remove();

        place = map.addMarker(new MarkerOptions()
        .position(position)
        .draggable(draggable));

        // map.addMarker(new MarkerOptions()
        // .position(myPos)
        // .title("Yeriniz")
        // .snippet("�u an buradas�n�z.")
        // .icon(BitmapDescriptorFactory
        // .fromResource(R.drawable.ic_launcher)));
    }

    /**
     * 
     * @param map
     * @param position
     * @param zoom
     * 
     *            Harita uzerinde belirtilen konumu ekranda ortalayip, zoom
     *            yapar.
     */
    public static void centerAndZomm(GoogleMap map, LatLng position, int zoom) {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(position, zoom));
    }

    /**
     * 
     * @param context
     * @param position
     * @return Parametre olarak aldigi konumun acik adresini doner.
     */
    public static String getAddress(Context context, LatLng position) {

        String address = "";

        Geocoder gcd = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = gcd.getFromLocation(position.latitude,
                    position.longitude, 1);

            if (addresses.size() > 0) {
                for (int i = 0; i < addresses.get(0).getMaxAddressLineIndex(); i++) {
                    address += addresses.get(0).getAddressLine(i) + ",";
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Couldn't get address", e);
        }
        return address;

    }

    public static String getCity(Context context, LatLng position) {

        String city = null;

        Geocoder gcd = new Geocoder(context, Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = gcd.getFromLocation(position.latitude,
                    position.longitude, 1);

            if (addresses.size() > 0)
                city = addresses.get(0).getLocality();

        } catch (IOException e) {
            Log.e(TAG, "Couldn't get city name", e);
        }

        return city;
    }

    public static void createAlert(Context context, String title,
            String message, boolean cancelable, String posButton,
            String negButton) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set title
        alertDialogBuilder.setTitle(title);

        // set dialog message
        alertDialogBuilder.setMessage(message).setCancelable(cancelable);

        if (!posButton.equals("")) {
            alertDialogBuilder.setPositiveButton(posButton,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // if this button is clicked, close
                            // current activity
                            try {
								finalize();
							} catch (Throwable e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
                        }
                    });
        }
        if (!negButton.equals("")) {
            alertDialogBuilder.setNegativeButton(negButton,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // if this button is clicked, just close
                            // the dialog box and do nothing
                            dialog.dismiss();
                        }
                    });
        }
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    /**
     * 
     * @param customerPosition
     * @param complaintPos
     * @return iki pozisyon arasindaki mesafe 5 kilometreden az ise true,
     *         degilse false doner.
     */
    public static boolean isNear(LatLng customerPosition, LatLng complaintPos) {
        float distance = calculateDistance(customerPosition.latitude,
                customerPosition.longitude, complaintPos.latitude,
                complaintPos.longitude);
        return (distance < 5000);

    }

    public static float calculateDistance(double latx, double lonx,
            double laty, double lony) {
        float[] result = new float[1];
        Location.distanceBetween(latx, lonx, laty, lony, result);
        return result[0];
    }

    public static String locationToJSON(double lat, double lon) {
        JSONObject loc = new JSONObject();
        JSONArray arr = new JSONArray();
        try {
            arr.put(lat);
            arr.put(lon);
            loc.put("location", arr);
            Log.d("Utils.locationTOJSON", loc.toString());
        } catch (JSONException e) {
            Log.e("Utils.locationToJSON", "JSONException throwed", e);
        }
        return loc.toString();
    }
    
    /*
     * Farkli android cihazlarin destekledigi kamera cozunurluklerine gore ayar yapilir.
     * Donen deger, Preview sinifinin constructor ina gonderilir.*/
    public static int[] deviceSupportedScreenSize() { 	
    	int[] tmp = new int[2];  	
    	Camera camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
    	Camera.Parameters params = camera.getParameters();
		ArrayList<Camera.Size>	list = (ArrayList<Size>) params.getSupportedPictureSizes();
		
		for (int i = list.size()-1; i >= 0; i--) {
		
			if (list.get(i).width == 800 && list.get(i).height == 600) {
				tmp[0] = 800;
				tmp[1] = 600;
				break;
//			} else if (list.get(i).width == 640 && list.get(i).height == 480) {
//				tmp[0] = 640;
//				tmp[1] = 480;
//				break;
			} else if (list.get(i).width == 1024 && list.get(i).height == 768) {
				tmp[0] = 1024;
				tmp[1] = 768;
				break;
			} else if (list.get(i).width == 1280 && list.get(i).height == 960) {
				tmp[0] = 1280;
				tmp[1] = 960;
				break;
			} else if (list.get(i).width == 1900 && list.get(i).height == 1600) {
				tmp[0] = 1900;
				tmp[1] = 1600;
				break;
			} else {
				tmp[0] = 0;
				tmp[1] = 0;
			}
		}
		
		camera.release();
		list = null;
		
		Log.i(TAG, "deviceSupportedScreenSize returned " + tmp[0] + "x" + tmp[1]);
		
		return tmp;
    }
}
