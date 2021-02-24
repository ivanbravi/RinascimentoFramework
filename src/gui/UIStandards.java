package gui;

import processing.core.PApplet;

public class UIStandards {
	private static int[] suits = null;
	private static boolean[] colorAlternative = null;

	private static String font = "Menlo-Regular";
	private static int lightGrey;
	private static int gold;

	private static int fontColorDark;
	private static int fontColorBright;

	private static float strokeWeight = 1.0f;
	private static float assetStrokeWeight = 3.5f;

	public static float assetStrokeWeight(){
		return assetStrokeWeight;
	}

	public static String font(){
		return font;
	}

	public static int lightGrey(){
		return lightGrey;
	}

	public static int fontColorDark(){
		return fontColorDark;
	}

	public static int fontColorBright(){
		return fontColorBright;
	}

	public static int suitColor(int suit){
		if(suits==null){
			System.out.println("SETUP STANDARDS IN PApplet");
			return 0;
		}

		if(suit<0 || suit>=suits.length){
			return 0;
		}

		return suits[suit];
	}

	public static float strokeWeight(){
		return strokeWeight;
	}

	public static int fontColor(int suit){

		if(colorAlternative[suit]){
			return fontColorBright;
		}

		return fontColorDark;
	}

	public static void setup(PApplet a){
		createColors(a);
		lightGrey = a.color(200,200,200);
		gold = a.color(246,236,33);
		fontColorBright = a.color(255);
		fontColorDark = a.color(0);
	}

	public static int goldColor(){
		return gold;
	}

	private static void createColors(PApplet a){
		colorAlternative = new boolean[]{true,
				true,
				true,
				false,
				true,
		};

		suits = new int[]{a.color(20, 136, 67),
				a.color(37, 94, 159),
				a.color(215, 60, 54),
				a.color(250, 250, 250),
				a.color(51, 52, 51) // ADD MORE
		};
	}

}
