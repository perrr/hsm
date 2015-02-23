package logic;

import java.util.ArrayList;
import types.Athlete;

public class Training {
	
	public Training(ArrayList<Athlete> athletes) {
		for (Athlete athlete : athletes) {
			changeForm(athlete);
			changeSkills(athlete);
		}
	}
	
	private void changeForm(Athlete athlete) {
		double backgroundForm = 100 - 2 * Math.abs(athlete.getIntensity() - 50);
		if (athlete.getParticipated() == 0)
			backgroundForm /= 2;
		else if (athlete.getParticipated() == 1)
			backgroundForm /= 1.5;
		double random = (Math.random() * 20) - 10;
		double newForm = athlete.getForm() + 0.5 * (backgroundForm - athlete.getForm()) + random;
		athlete.setForm(newForm);
	}
	
	public static void changeExperience(Athlete athlete, int result, int participants) {
		double increase = 0.325 - 0.15 * ((result - 1) / participants - 1);
		athlete.increaseExperience(increase);
	}
	
	private void changeSkills(Athlete athlete) {
		double increase = 0.015 * athlete.getIntensity();
		switch (athlete.getTraining()) {
			case "agility":
				athlete.increaseAgility(increase);
				break;
			case "balance":
				athlete.increaseBalance(increase);
				break;
			case "flight_technique":
				athlete.increaseFlightTechnique(increase);
				break;
			case "landing_technique":
				athlete.increaseLandingTechnique(increase);
				break;
			case "timing":
				athlete.increaseTiming(increase);
				break;
			default:
				throw new IllegalArgumentException();
		}
		if (athlete.getAge() > 26) {
			double decrease = 0.05 * (athlete.getAge() - 26);
			athlete.decreaseAgility(decrease);
			athlete.decreaseBalance(decrease);
			athlete.decreaseFlightTechnique(decrease);
			athlete.decreaseLandingTechnique(decrease);
			athlete.decreaseTiming(decrease);
		}
	}
}