package org.bonitasoft.resources.utils.connection;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.StringReader;

public class Properties extends java.util.Properties
{
	private static final long serialVersionUID = 1;

	/**
	 * Creates a Property object on a file and load it
	 * @param filePath
	 * @throws Exception if file not found
	 */
	public Properties(final String filePath) throws Exception
	{
		super();
		final File configFile = new File(filePath);
		if (!configFile.exists())
			throw new Exception("Could not load configuration file: "+ filePath);
		FileInputStream input = new FileInputStream(configFile);
		extract(input);
	}
	
	/**
	 * Creates a Property object on a file and load it
	 * @param inputStream
	 * @throws Exception if bad stream
	 */
	public Properties(final InputStream input) throws Exception
	{
		super();
		if (input == null) {
			throw new Exception("Could not load configuration");
		}
		extract(input);
	}
	
	private String extractInputStreamContent(InputStream input) {
		StringBuilder builder = new StringBuilder();
		int ch;
		try {
			while((ch = input.read()) != -1){
			    builder.append((char)ch);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return builder.toString();
	}

	private void extract(InputStream input) throws Exception {
		String propertyFileContents = extractInputStreamContent(input);
		this.load(new StringReader(propertyFileContents.replace("\\","\\\\")));
		input.close();
	}

	/**
	 * Gets a property and throws an Exception if not found
	 * @param propertyName
	 * @return propety's value
	 * @throws Exception if not found
	 */
	public String getMandatoryProperty(final String propertyName) throws Exception
	{
		final String property = super.getProperty(propertyName);
		if (property == null || "".equals(property))
			throw new Exception("Configuration: Missing configuration property: "+ propertyName);
		return property;
	}
	
	/**
	 * Gets a property value of enumerated type
	 * @param propertyName
	 * @param isMandatory
	 * @param defaultValue
	 * @param acceptedValueLabels list of accepted value labels
	 * @return
	 * @throws Exception if invalid value is specified of property is missing while being set as mandatory
	 */
	public int getEnumeratedProperty(final String propertyName, final boolean isMandatory, final int defaultValue, final String[] acceptedValueLabels) throws Exception
	{
		// Get property value
		String propertyValue = null;
		if (isMandatory)
			propertyValue = getMandatoryProperty(propertyName);
		else
		{
			propertyValue = getProperty(propertyName);
			if (propertyValue == null)
				return defaultValue;
		}
		propertyValue = propertyValue.trim().toLowerCase();
		// Check for valid value
		boolean isValidValue = false;
		int value = 0;
		for (int i=0; !isValidValue && i<acceptedValueLabels.length; i++)
		{
			isValidValue = acceptedValueLabels[i].equals(propertyValue);
			if (isValidValue)
				value = i;
		}
		if (isValidValue)
			return value;
		else
		{
			String errorMessage = "Invalid value for property '"+ propertyName +"': "+ propertyValue +"\n Accepted values: ";
			for (int i=0; i<acceptedValueLabels.length; i++)
				errorMessage += acceptedValueLabels[i] +" ";
			throw new Exception(errorMessage);
		}
	}
}
