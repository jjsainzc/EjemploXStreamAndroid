package com.example.ejemploxstreamandroid;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;
import com.thoughtworks.xstream.io.xml.DomDriver;


public class MainActivity extends Activity {
	private TextView resultado;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		resultado = (TextView) findViewById(R.id.resultado);
		
		Button btn = (Button) findViewById(R.id.button1); 
		btn.setOnClickListener(new OnClickListener() { 
			@Override
			public void onClick(View v) {
				List<Prueba> lista = new ArrayList<Prueba>();
				XStream xstream = new XStream(new DomDriver());
				
				
				StringBuilder sb = new StringBuilder();
				
				lista.add(new Prueba("dato1","dato1"));
				lista.add(new Prueba("dato2","dato2"));
			   
			    xstream.alias("pruebas", List.class);
			    xstream.alias("prueba", Prueba.class);
				
				String result = xstream.toXML(lista);
				
				// para convertir a JSON
				JSONObject jsonObj = new JSONObject();
				
				try {
				    jsonObj = XML.toJSONObject(result);
				} catch (JSONException e) {
				    
				    resultado.setText(e.toString());
				} 
				
				// Para convertir a XML
				try {
					result = XML.toString(jsonObj);
				} catch (JSONException e) {
					resultado.setText(e.toString());
				}
				
				SAXBuilder builder = new SAXBuilder();
				try {
					org.jdom2.Document document = builder.build(new StringReader(result));
					org.jdom2.Element root = document.getRootElement();
					
					for (org.jdom2.Element e: root.getChildren()) {
						sb.append(e.getChildText("campo1")).append(" ");
						sb.append(e.getChildText("campo2")).append("\n");
					}
				} catch (JDOMException e) {
					resultado.setText(e.toString());
				} catch (IOException e) {
					resultado.setText(e.toString());
				}
				
				try {
					resultado.setText(jsonObj.toString(4) + "\n" + FormateadorXML.formatXML(result,5) + "\n" + sb.toString());
				} catch (JSONException e) {
					resultado.setText(e.toString());
				}
				//Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
				//Toast.makeText(MainActivity.this, jsonObj + "\n" + result + "\n" + sb.toString(), Toast.LENGTH_LONG).show();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
