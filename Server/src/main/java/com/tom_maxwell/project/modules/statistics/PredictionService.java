package com.tom_maxwell.project.modules.statistics;

import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.Arrays;

/**
 * Created by Tom on 27/03/2016.
 */
@Component
public class PredictionService {

	public static String wrapCoefficients(double[] coefficients){

		String c = Arrays.toString(coefficients);

		c = c.substring(1, c.length() - 1);

		return c;

	}

	public static double[] unwrapCoefficients(String coefficients){

		String[] strings = coefficients.split(", ");

		double[] doubles = new double[strings.length];
		for(int i = 0; i < strings.length; i++){

			String string = strings[i];

			doubles[i] = Double.valueOf(string);
		}

		return doubles;

	}
}
