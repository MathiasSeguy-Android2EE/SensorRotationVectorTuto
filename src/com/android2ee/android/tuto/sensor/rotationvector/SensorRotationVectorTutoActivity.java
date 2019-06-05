package com.android2ee.android.tuto.sensor.rotationvector;

/**<ul>
 * <li>SensorRotationVectorTuto</li>
 * <li>com.android2ee.android.tuto.sensor.rotationvector</li>
 * <li>29 nov. 2011</li>
 * 
 * <li>======================================================</li>
 *
 * <li>Projet : Mathias Seguy Project</li>
 * <li>Produit par MSE.</li>
 *
 /**
 * <ul>
 * Android Tutorial, An <strong>Android2EE</strong>'s project.</br> 
 * Produced by <strong>Dr. Mathias SEGUY</strong>.</br>
 * Delivered by <strong>http://android2ee.com/</strong></br>
 *  Belongs to <strong>Mathias Seguy</strong></br>
 ****************************************************************************************************************</br>
 * This code is free for any usage but can't be distribute.</br>
 * The distribution is reserved to the site <strong>http://android2ee.com</strong>.</br>
 * The intelectual property belongs to <strong>Mathias Seguy</strong>.</br>
 * <em>http://mathias-seguy.developpez.com/</em></br> </br>
 * 
 * *****************************************************************************************************************</br>
 *  Ce code est libre de toute utilisation mais n'est pas distribuable.</br>
 *  Sa distribution est reservée au site <strong>http://android2ee.com</strong>.</br> 
 *  Sa propriété intellectuelle appartient à <strong>Mathias Seguy</strong>.</br>
 *  <em>http://mathias-seguy.developpez.com/</em></br> </br>
 * *****************************************************************************************************************</br>
 */
import java.text.DecimalFormat;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

/**
 * @author Mathias Seguy (Android2EE)
 * @goals
 *        This class aims to:
 *        <ul>
 *        <li></li>
 *        </ul>
 */
public class SensorRotationVectorTutoActivity extends Activity implements SensorEventListener {
	// see :http://developer.android.com/reference/android/hardware/SensorEvent.html
	// see also:http://developer.android.com/reference/android/hardware/SensorManager.html
	/**
	 * The Tag for the Log
	 */
	private static final String LOG_TAG = "RotationVectorGyroscope";

	/******************************************************************************************/
	/** Current sensor value **************************************************************************/
	/******************************************************************************************/
	/**
	 * Current value of the rotation vector:
	 * xRotation: x*sin(θ/2)
	 * yRotation: y*sin(θ/2)
	 * zRotation: z*sin(θ/2)
	 * cosRotation: cos(θ/2)
	 * And θ is the angle θ which the device has rotated around the axis <x, y, z>.
	 */
	float xRotation, yRotation, zRotation, cosRotation;
	/**
	 * Max range of the sensor
	 */
	float maxRange;
	/******************************************************************************************/
	/** View **************************************************************************/
	/******************************************************************************************/
	/**
	 * The view that draw the graphic
	 */
	TextView txvRotationValue;
	/******************************************************************************************/
	/** Sensors and co **************************************************************************/
	/******************************************************************************************/
	/** * The sensor manager */
	SensorManager sensorManager;
	/**
	 * The rotation sensor
	 */
	Sensor rotation;

	/******************************************************************************************/
	/** Manage life cycle **************************************************************************/
	/******************************************************************************************/
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// build the GUI
		setContentView(R.layout.main);
		// Then manage the sensors and listen for changes
		// Instantiate the SensorManager
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		// Instantiate the rotation sensor and its max range
		rotation = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
		maxRange = rotation.getMaximumRange();

		// Then build the GUI:
		// find the TextView
		txvRotationValue = (TextView) findViewById(R.id.txtView);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause() {
		// unregister every body
		sensorManager.unregisterListener(this, rotation);
		// and don't forget to pause the thread that redraw the xyAccelerationView
		super.onPause();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		/*
		 * Register the sensor
		 */
		sensorManager.registerListener(this, rotation, SensorManager.SENSOR_DELAY_UI);
		super.onResume();
	}

	/******************************************************************************************/
	/** Drawing method **************************************************************************/
	/******************************************************************************************/

	/**
	 * The method to redraw the view
	 */
	private void redraw() {
		DecimalFormat f = new DecimalFormat("#.##");
		String message = String.format(getString(R.string.magnetic_value), f.format(xRotation), f.format(yRotation), f
				.format(zRotation),f.format(cosRotation));
		txvRotationValue.setText(message);
	}

	/******************************************************************************************/
	/** SensorEventListener **************************************************************************/
	/******************************************************************************************/

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.hardware.SensorEventListener#onAccuracyChanged(android.hardware.Sensor, int)
	 */
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// Nothing to do
		if (sensor.getType() == Sensor.TYPE_GYROSCOPE) {
			// do a log (for the fun, ok don't do a log...)
			String accuracyStr;
			if (SensorManager.SENSOR_STATUS_ACCURACY_HIGH == accuracy) {
				accuracyStr = "SENSOR_STATUS_ACCURACY_HIGH";
			} else if (SensorManager.SENSOR_STATUS_ACCURACY_LOW == accuracy) {
				accuracyStr = "SENSOR_STATUS_ACCURACY_LOW";
			} else if (SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM == accuracy) {
				accuracyStr = "SENSOR_STATUS_ACCURACY_MEDIUM";
			} else {
				accuracyStr = "SENSOR_STATUS_UNRELIABLE";
			}
			Log.d(LOG_TAG, "Sensor's values (" + xRotation + "," + yRotation + "," + zRotation + ") and accuracy : "
					+ accuracyStr);
		}
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.hardware.SensorEventListener#onSensorChanged(android.hardware.SensorEvent)
	 */
	@Override
	public void onSensorChanged(SensorEvent event) {
		// update only when your are in the right case:
		if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
			// la valeur du sinus de l'angle autour de chaque axe
			xRotation = event.values[0];
			yRotation = event.values[1];
			zRotation = event.values[2];
			// la valeur du cosinus de l'angle
			if (event.values.length == 4) {
				cosRotation = event.values[3];
			}
			// you can do something with that but what ?o?
			// see
			// http://developer.android.com/reference/android/hardware/Sensor.html#TYPE_GYROSCOPE
			// then redraw
			redraw();
		}
	}
}